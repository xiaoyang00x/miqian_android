package com.miqian.mq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.miqian.mq.R;
import com.miqian.mq.entity.Title;
import com.miqian.mq.net.HttpUtils;
import com.miqian.mq.net.Param;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ChannelUtil;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.SwipeWebView;
import com.miqian.mq.views.WebChromeClientEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guolei_wang on 15/9/25.
 */
public class WebBankActivity extends WebActivity {

    private String url_hf;
    private String sign;

    private int state;

    public static void startActivity(Activity activity, String url, ArrayList<String> list, int type) {
        activity.startActivityForResult(getIntent(activity, Urls.getServer(activity) + url, list), type);
    }

    private static Intent getIntent(Context context, String url, ArrayList<String> list) {
        Intent intent = new Intent(context, WebBankActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putStringArrayListExtra("list", list);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = 0;
        ArrayList list = getIntent().getStringArrayListExtra("list");
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new Param("timer", "" + System.currentTimeMillis()));
        HttpUtils.sortParam(list);
        sign = HttpUtils.getSign(list);
        url_hf = getIntent().getStringExtra(KEY_URL);
        url_hf += HttpUtils.getUrl(list);
        LogUtil.e("", "WebBankActivity url : " + url_hf);
        Uri uri = Uri.parse(url_hf);
        isRefresh = "1".equals(uri.getQueryParameter("refresh"));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getPageName() {
        return "内置浏览器_江西银行";
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
                loadUrl(url_hf);
            }
        });

        swipe_refresh.setEnabled(isRefresh);
        swipe_refresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                loadUrl(url_hf);
            }
        });

        loadUrl(url_hf);

        getmTitle().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private void loadUrl(String url) {
        if (MobileOS.getNetworkType(this) == -1 && !url.startsWith("file:///android_asset/")) {
            webview.setVisibility(View.GONE);
            load_webview_error.setVisibility(View.VISIBLE);
            mTitle.setTitleText("无网络");
        } else {
            begin();
            webview.setVisibility(View.VISIBLE);
            load_webview_error.setVisibility(View.GONE);
            webview.loadUrl(url, getHeader(sign));
        }
    }

    private Map<String, String> getHeader(String sign) {
        Map<String, String> headers = new HashMap<>();
        headers.put("deviceId", MobileOS.getIMEI(this));
        headers.put("cType", "android");
        headers.put("deviceModel", MobileOS.getDeviceModel());
        headers.put("appName", "miqian");
        headers.put("appVersion", MobileOS.getClientVersion(this));
        headers.put("channelCode", ChannelUtil.getChannel(this));
        headers.put("sign", sign);
        headers.put("token", UserUtil.getToken(this));
        headers.put("osVersion", MobileOS.getOsVersion());
        headers.put("netWorkStandard", MobileOS.getNetworkString(this));
        headers.put("Connection", "close");
        return headers;
    }

    @JavascriptInterface
    public void jxBankCallBak(String code) {
        LogUtil.e("", "WebBankActivity :hfCallback : " + code);
        Title title = JsonUtil.parseObject(code, Title.class);
        int num = title.getTitle();
        switch (num) {
            case 5://跳转修改交易密码
                SendCaptchaActivity.enterActivity(mActivity, TypeUtil.CAPTCHA_TRADE_PW);
                break;
        }
        state = 1;
        goBack();
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
     * HF返回只能返回原生页面
     */
    private void goBack() {
        LogUtil.e("", "WebBankActivity : goBack : " + state);
        Intent intent = new Intent();
        setResult(state, intent);
        finish();
    }


}
