package com.miqian.mq.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.miqian.mq.R;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.HashMap;

/**
 * Created by Jackie on 2015/10/10.
 */

public class WebViewActivity extends BaseActivity {

    private static final String URL = "url";
    private static final String TAG = "WebViewActivity";
    private static final String FROM_NOTIFY = "formNotify";

    //	private boolean bFromNotify = false;
    private int mBackCount = 0;
    private boolean bFillAddressBack = false;

    private ProgressBar pbLoadProgress;
    private LinearLayout llLoadingView;

    private HashMap<String, String> titleUrlMap = new HashMap<String, String>();

    public interface OnWebViewListener {
        public void onResult(int operationType);
    }

    public static OnWebViewListener mListener;

    public static Intent getNotifyIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(FROM_NOTIFY, true);
        intent.putExtra("is_use_web_title", true);
        intent.putExtra(URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getIntentOutside(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        return it;
    }

    public static void doIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void doIntent(Context context, String url, String title, OnWebViewListener listener) {
        WebViewActivity.mListener = listener;
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void doIntent(Context context, String url, boolean bUseWebTitle, OnWebViewListener listener) {
        WebViewActivity.mListener = listener;
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("is_use_web_title", bUseWebTitle);
        intent.putExtra(URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static OnActivityListener mActivityListner;

    public interface OnActivityListener {
        public void OnActivityFinish();
    }

    public static void doIntentEx(Context context, String url, boolean bUseWebTitle, OnActivityListener listener) {
        mActivityListner = listener;
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("is_use_web_title", bUseWebTitle);
        intent.putExtra(URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 指定外部网页
     *
     * @param context
     * @param url
     */
    public static void doLoginOutside(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebView mWebView;
    //	private RelativeLayout mWebContainer;
    private String mUrl;
    private String title;
    private boolean isError = false;
    private boolean bBackFinish = false;
    private boolean bBackTwice = false;

//	private boolean isActivityFinish = false;

    @Override
    protected void onDestroy() {
        super.onDestroy(); // To change body of overridden methods use File |
        // Settings | File Templates.
        try {
            // mWebView.removeJavascriptInterface(jsName);
//			isActivityFinish = true;

            if (mActivityListner != null) {
                mActivityListner.OnActivityFinish();
            }

            if (null != titleUrlMap) {
                titleUrlMap.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean bUseWebTitle = false;
    private JavaScriptInterface javaScriptInterface;
    private String jsName = "";

    private void getIntentData() {
        try {
            if (getIntent().hasExtra(URL)) {
                mUrl = getIntent().getStringExtra(URL);
            }
            if (mUrl == null) {
                finish();
            }
            if (getIntent().hasExtra("title")) {
                title = getIntent().getStringExtra("title");
            }

            if (getIntent().hasExtra("is_use_web_title")) {
                bUseWebTitle = getIntent().getBooleanExtra("is_use_web_title", false);
            }

//			bFromNotify = getIntent().getBooleanExtra(FROM_NOTIFY, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleFinishActivity() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        getIntentData();
        super.onNewIntent(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getIntentData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
    }

    private void initUI() {
        pbLoadProgress = (ProgressBar) findViewById(R.id.pbLoadProgress);
        llLoadingView = (LinearLayout) findViewById(R.id.llLoadingView);

        if (bUseWebTitle) {
            mTitle.setTitleText("");
        } else {
            if (!TextUtils.isEmpty(title)) {
                mTitle.setTitleText(title);
            } else {
                mTitle.setTitleText("");
            }
        }

    }

    @SuppressLint("JavascriptInterface")
    private void initLogic() {
        mWebView = (WebView) findViewById(R.id.webview);
//		mWebContainer = (RelativeLayout) findViewById(R.id.webContainer);
        // mWebView = new WebView(WebViewActivity.this);
        // ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
        // ViewGroup.LayoutParams.MATCH_PARENT,
        // ViewGroup.LayoutParams.MATCH_PARENT);
        // mWebContainer.addView(mWebView, layoutParams);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setVerticalScrollBarEnabled(false); // 垂直不显示
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setUserAgentString("Android-" + MobileOS.getAppVersionName(getApplicationContext()));
        javaScriptInterface = new JavaScriptInterface();
        jsName = "tlsj";

        mWebView.addJavascriptInterface(javaScriptInterface, jsName);

//        if (!TextUtils.isEmpty(mUrl)) {
//            String temp = "";
//            if (mUrl.endsWith("#zc") || mUrl.endsWith("#zr")) {
//                temp = mUrl.substring(mUrl.length() - 3, mUrl.length());
//                mUrl = mUrl.substring(0, mUrl.length() - 3);
//            }

//            String params = "&appVersion=" + MobileOS.getAppVersionName(getApplicationContext()) + "&cfrom=1&appkey_name=app_miqian&noSign=1" + "&user_id="
//                    + Pref.getString(Pref.USERID, getApplication(), "") + "&imei=" + URLEncoder.encode(RSAUtils.encryptByPublic(MobileOS.getIMEI(getApplication())));
//            if (mUrl.contains("?")) {
//                mUrl = mUrl + params;
//            } else {
//                mUrl = mUrl + "?" + params;
//            }
//            mUrl += temp;
//        }

        try {
//            UIhelper.trace("cccc: android.os.Build.VERSION.SDK_INT : " + android.os.Build.VERSION.SDK_INT);
//            UIhelper.trace(TAG, "访问网址：" + mUrl);
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.loadUrl(mUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    @Override
    public void onBackPressed() {
        handleClickBack();
    }

    private void handleClickBack() {
        try {
            if (bBackFinish) {
                finish();
            } else {
                if (bBackTwice) {
                    if (mWebView.canGoBack())
                        mWebView.goBack();
                    if (mWebView.canGoBack())
                        mWebView.goBack();
                } else {
                    if (mWebView.canGoBack()) {
                        // 调用js,若js没反应，直接退出
                        mWebView.loadUrl("javascript:androidGetInfo()");
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (bBackFinish) {
                                    finish();
                                    return;
                                }
                                if (mBackCount == 0) {
                                    if (mWebView.canGoBack())
                                        mWebView.goBack();
                                    else {
                                        finish();
                                    }
                                } else {
                                    for (int i = 0; i < mBackCount; i++) {
                                        if (mWebView.canGoBack())
                                            mWebView.goBack();
                                        else {
                                            finish();
                                        }
                                    }
                                    mBackCount = 0;
                                }
                            }
                        }, 250);
                    } else {
                        finish();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

//            UIhelper.trace(TAG, "onProgressChanged:" + newProgress);
            if (newProgress == 100) {
                pbLoadProgress.setVisibility(View.GONE);
                mWebView.requestFocus();
                if (bUseWebTitle) {
                    mTitle.setTitleText(titleUrlMap.get(view.getUrl()));
                }
            } else {
                pbLoadProgress.setVisibility(View.VISIBLE);
                pbLoadProgress.setProgress(newProgress);
            }
        }

        // 网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title); // To change body of overridden
//            UIhelper.trace(TAG, "webTitle:" + title + view.getUrl());
            if (bUseWebTitle) {
                if (!TextUtils.isEmpty(title)) {
                    mTitle.setTitleText(title);
                    titleUrlMap.put(view.getUrl(), title);
                }
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            try {
                isError = true;
//                UIhelper.trace(TAG, "onReceivedError errorCode-------->:" + errorCode + "------>description：" + description);
                mWebView.setVisibility(View.GONE);
                llLoadingView.setVisibility(View.VISIBLE);
                if (MobileOS.getNetworkType(mContext) == -1) {
                    Uihelper.showToast(WebViewActivity.this, getResources().getString(R.string.net_error));
                }

                if (!TextUtils.isEmpty(title)) {
                    mTitle.setTitleText(title);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                super.onReceivedSslError(view, handler, error); // To change
                // body of
            }
//            UIhelper.trace(TAG, "onReceivedSslError errorCode-------->");
            isError = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
//                UIhelper.trace(TAG, "shouldOverrideUrlLoading url-------->:" + url);
                return super.shouldOverrideUrlLoading(view, url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isError) {
                llLoadingView.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume(); // To change body of overridden methods UIhelper File
        // |
        // Settings | File Templates.
        // 跳转填写我的地址，回来重新刷新页面
        if (bFillAddressBack) {
            mWebView.reload();
        }
    }

    final class JavaScriptInterface {

        JavaScriptInterface() {
        }

        public void getInfo(boolean bfinish) {
            bBackFinish = bfinish;
            mBackCount = 0;
        }

        /**
         * 获取返回次数
         *
         * @param count
         */
        public void getBackCount(int count) {
            mBackCount = count;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void obtainData() {
        initLogic();
        setListeners();
    }

    @Override
    public void initView() {
        initUI();
    }

    private void setListeners() {
        llLoadingView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mWebView) {
                    mWebView.reload();
                }
            }
        });
    }

}
