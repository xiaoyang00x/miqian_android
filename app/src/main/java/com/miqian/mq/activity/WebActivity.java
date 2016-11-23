package com.miqian.mq.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.alibaba.fastjson.util.Base64;
import com.miqian.mq.R;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.ShareData;
import com.miqian.mq.listener.JsShareListener;
import com.miqian.mq.listener.ListenerManager;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.ShareUtils;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.Dialog_Login;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.SwipeWebView;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.WebChromeClientEx;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class WebActivity extends BaseActivity implements LoginListener, JsShareListener {
    public static final String KEY_URL = "KEY_URL";
    public static final String JS_INTERFACE_NAME = "MIAOQIAN";

    private String url;
    protected SwipeWebView webview;
    protected MySwipeRefresh swipe_refresh;
    protected ProgressBar progressBar;
    protected View load_webview_error;
    protected View tv_refresh;
    private String defaultAgent;     //默认 UA
    protected boolean isRefresh;       //是否支持下拉刷新

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
        Uri uri = Uri.parse(url);
        isRefresh = "1".equals(uri.getQueryParameter("refresh"));
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
        swipe_refresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        load_webview_error = findViewById(R.id.load_webview_error);
        tv_refresh = findViewById(R.id.tv_refresh);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        setUserAgent(settings);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
        webview.setWebViewClient(new MqWebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mTitle.setTitleText(view.getTitle());
                swipe_refresh.setRefreshing(false);
                end();

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                swipe_refresh.setRefreshing(false);
                end();
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

        swipe_refresh.setEnabled(isRefresh);
        swipe_refresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                loadUrl(url);
            }
        });

        loadUrl(url);

        getmTitle().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

    }

    private void loadUrl(String url) {
        // 无网络且不加载本包内asset目录下的url
        if (MobileOS.getNetworkType(this) == -1 && !url.startsWith("file:///android_asset/")) {
            webview.setVisibility(View.GONE);
            load_webview_error.setVisibility(View.VISIBLE);
            mTitle.setTitleText("无网络");
        } else {
            begin();
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
                            if (Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), true)) {
                                GestureLockSetActivity.startActivity(context, cls,false);
                            } else if (null != cls) {
                                startActivity(new Intent(context, cls));
                            }
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
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Uihelper.showToast(mContext, "您尚未开启通话权限，请开启后再尝试。");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
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
        UserUtil.loginActivity(initDialogLogin(this, null, 0));
    }

    //分享接口
    @JavascriptInterface
    public void share(String jsonStr) {
        ShareUtils.share(this, JsonUtil.parseObject(jsonStr, ShareData.class), this);
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
        RegularDetailActivity.startActivity(this, subjectId, RegularBase.REGULAR_PROJECT);
    }

    //定期计划详情页面
    @JavascriptInterface
    public void startRegularPlan(String subjectId) {
        RegularDetailActivity.startActivity(this, subjectId, RegularBase.REGULAR_PLAN);
    }

    //定期详情页面
    @JavascriptInterface
    public void startRegularDetail(String subjectId, int product_id) {
        RegularDetailActivity.startActivity(this, subjectId, product_id);
    }

    //秒钱宝首页
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

    @JavascriptInterface
    public void copy(String text) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(text, text);
        cmb.setPrimaryClip(clipData);
    }

    @JavascriptInterface
    public void showpic(String img_url) {
        ShowWebImgActivity.startActivity(mContext, img_url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断当前网页是否有前一页，如果有则返回，否则 finish()
     */
    private void goBack() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    /**
     * 设置 UserAgent
     *
     * @param settings
     */
    protected void setUserAgent(WebSettings settings) {
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

    @Override
    public void shareLog(String json) {
        webview.loadUrl("javascript:webview.share_log(" + json + ")");
    }


    class MqWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window." + JS_INTERFACE_NAME + ".onGetMetadata("
                    + "document.querySelector('meta[name=\"miaoqian_right_content\"]').getAttribute('content')" + ");");
            super.onPageFinished(view, url);
        }
    }

    @JavascriptInterface
    public void onGetMetadata(String s) {
        if (TextUtils.isEmpty(s)) return;
        String jsonStr = new String(Base64.decodeFast(s));
        LogUtil.d("WEBVIEW", jsonStr);
        final ShareData shareData = JsonUtil.parseObject(jsonStr, ShareData.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getmTitle().setRightText(shareData.getRight_name());
                getmTitle().setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shareData.getType() == ShareData.TYPE_LINKS) {
                            WebActivity.startActivity(WebActivity.this, shareData.getLinks());
                        } else {
                            ShareUtils.share(WebActivity.this, shareData, WebActivity.this);
                        }
                    }
                });
            }
        });


    }

}
