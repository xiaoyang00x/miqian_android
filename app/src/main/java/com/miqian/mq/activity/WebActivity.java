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

import com.miqian.mq.R;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.views.SwipeWebView;
import com.miqian.mq.views.WebChromeClientEx;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class WebActivity extends BaseFragmentActivity {
    public static final String KEY_URL = "KEY_URL";
    public static final String JS_INTERFACE_NAME = "MIAOQIAN";

    private String url;
    private SwipeWebView webview;
    private ProgressBar progressBar;
    private View load_webview_error;
    private View tv_refresh;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra(KEY_URL);
        findView();
        initView();
    }

    @Override
    protected String getPageName() {
        return "内置浏览器";
    }

    private void findView() {
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        webview = (SwipeWebView)findViewById(R.id.webview);
        load_webview_error = findViewById(R.id.load_webview_error);
        tv_refresh = findViewById(R.id.tv_refresh);
    }

    private void initView() {

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        String defaultAgent = settings.getUserAgentString();
        if (TextUtils.isEmpty(defaultAgent)) {
            settings.setUserAgentString("MiaoQian");
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setAppCacheEnabled(true);
        settings.setSupportZoom(true);
        settings.setSavePassword(false);
        webview.setWebChromeClient(new WebChromeClientEx() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
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
    private void loadUrl(String url) {
        if (MobileOS.getNetworkType(this) == -1) {
            webview.setVisibility(View.GONE);
            load_webview_error.setVisibility(View.VISIBLE);
            setTitle("无网络");
        }else {
            webview.setVisibility(View.VISIBLE);
            load_webview_error.setVisibility(View.GONE);
            webview.loadUrl(url);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        webview.removeAllViews();
        webview.destroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyText(String text) {
        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipData clipData = ClipData.newPlainText(text, text);
            cmb.setPrimaryClip(clipData);
        } else {
            cmb.setText(text);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(webview != null && webview.canGoBack()) {
                webview.goBack();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
