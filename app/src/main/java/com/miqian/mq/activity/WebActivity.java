package com.miqian.mq.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSONObject;
import com.miqian.mq.R;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.listener.ListenerManager;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.ShareUtils;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.Dialog_Login;
import com.miqian.mq.views.SwipeWebView;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.WebChromeClientEx;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class WebActivity extends BaseActivity implements LoginListener {
    public static final String KEY_URL = "KEY_URL";
    public static final String JS_INTERFACE_NAME = "MIAOQIAN";

    private String url;
    private SwipeWebView webview;
    private ProgressBar progressBar;
    private View load_webview_error;
    private View tv_refresh;
    private String defaultAgent;     //默认 UA

    public static void startActivity(Context context, String url) {
        context.startActivity(getIntent(context, url));
    }

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(KEY_URL);
        super.onCreate(savedInstanceState);

        ListenerManager.registerLoginListener(WebActivity.class.getSimpleName(), this);
    }

    @Override
    public void obtainData() {

    }

    @Override
    protected String getPageName() {
        return "内置浏览器";
    }

    @Override
    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        webview = (SwipeWebView) findViewById(R.id.webview);
        load_webview_error = findViewById(R.id.load_webview_error);
        tv_refresh = findViewById(R.id.tv_refresh);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        setUserAgent(settings);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(false);

        settings.setSupportZoom(true);
        settings.setSavePassword(false);
        webview.setWebChromeClient(new WebChromeClientEx() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setTitleText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }

                super.onProgressChanged(view, progress);
            }

        });
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        webview.addJavascriptInterface(this, JS_INTERFACE_NAME);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl(url);
            }
        });
        loadUrl(url);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

    }

    private void loadUrl(String url) {
        if (MobileOS.getNetworkType(this) == -1) {
            webview.setVisibility(View.GONE);
            load_webview_error.setVisibility(View.VISIBLE);
            mTitle.setTitleText("无网络");
        } else {
            webview.setVisibility(View.VISIBLE);
            load_webview_error.setVisibility(View.GONE);
            webview.loadUrl(url);
        }
    }

    Dialog_Login dialog_login = null;

    private Dialog_Login initDialogLogin(final Context context, final Class<?> cls, int type) {
        if (dialog_login == null || dialog_login.type != type) {
            dialog_login = new Dialog_Login(context, type) {
                @Override
                public void login(String telephone, String password) {
                    // TODO: 2015/10/10 Loading
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            UserInfo userInfo = result.getData();
                            UserUtil.saveUserInfo(context, userInfo);
                            GestureLockSetActivity.startActivity(context, cls);
                        }

                        @Override
                        public void onFail(String error) {
                            Uihelper.showToast(context, error);
                        }
                    }, telephone, password);
                }
            };
        }
        return dialog_login;
    }

    @JavascriptInterface
    public void call(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @JavascriptInterface
    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //登录窗口
    @JavascriptInterface
    public void login() {
        UserUtil.loginActivity(this, null, initDialogLogin(this, null, 0));
    }

    //分享接口
    @JavascriptInterface
    public void share(String jsonStr) {
        ShareUtils.share(this, jsonStr);
    }

    //充值页面(需要登录)
    @JavascriptInterface
    public void startIntoActivity() {
        UserUtil.loginActivity(this, IntoActivity.class, initDialogLogin(this, IntoActivity.class, 1));
    }

    //红包、券列表页面(需要登录)
    @JavascriptInterface
    public void startTicketActivity() {
        UserUtil.loginActivity(this, MyTicketActivity.class, initDialogLogin(this, MyTicketActivity.class, 2));
    }

    //定期赚详情页面
    @JavascriptInterface
    public void startRegularEarn(String subjectId) {
        RegularEarnActivity.startActivity(this, subjectId);
    }

    //定期计划详情页面
    @JavascriptInterface
    public void startRegularPlan(String subjectId) {
        RegularPlanActivity.startActivity(this, subjectId);
    }

    //活期首页
    @JavascriptInterface
    public void startCurrent() {
        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_CURRENT, null);
    }

    //定期首页
    @JavascriptInterface
    public void startRegular() {
        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_REGULAR, null);
    }

    @Override
    public void onDestroy() {
        ListenerManager.unregisterLoginListener(WebActivity.class.getSimpleName());
        webview.removeAllViews();
        webview.destroy();
        dialog_login = null;
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyText(String text) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(text, text);
        cmb.setPrimaryClip(clipData);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webview != null && webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置 UserAgent
     *
     * @param settings
     */
    private void setUserAgent(WebSettings settings) {
        if (TextUtils.isEmpty(defaultAgent)) {
            defaultAgent = settings.getUserAgentString();
        }
        String ua = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", UserUtil.getToken(mContext));
        jsonObject.put("userid", UserUtil.getUserId(mContext));
        jsonObject.put("deviceId", MobileOS.getIMEI(mContext));
        jsonObject.put("cType", "android");
        jsonObject.put("appName", "miqian");
        jsonObject.put("appVersion", MobileOS.getClientVersion(mContext));
        jsonObject.put("osVersion", MobileOS.getOsVersion());
        jsonObject.put("deviceModel", MobileOS.getDeviceModel());

        ua = jsonObject.toString();
        settings.setUserAgentString(defaultAgent + " miaoqian_json: " + ua);
        LogUtil.d("webactivity3333", defaultAgent + " miaoqian_json: " + ua);
    }

    @Override
    public void loginSuccess() {
        if (webview != null && !TextUtils.isEmpty(url)) {
            setUserAgent(webview.getSettings());
            webview.reload();
        }
    }

    @Override
    public void logout() {
        if (webview != null && !TextUtils.isEmpty(url)) {
            setUserAgent(webview.getSettings());
            webview.reload();
        }
    }
}
