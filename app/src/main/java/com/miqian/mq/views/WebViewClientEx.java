package com.miqian.mq.views;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/******************************************
 * 类描述： TODO
 * 类名称：WebViewClientEx  
 * @version: 1.0
 * @author: guolei_wang
 * @time: 2015-1-15 下午4:28:52 
 ******************************************/
public class WebViewClientEx extends WebViewClient {
	@Override
    public void onLoadResource(WebView view, String url) {
        //injectJavascriptInterfaces(view);
        super.onLoadResource(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        //injectJavascriptInterfaces(view);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        //injectJavascriptInterfaces(view);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        //injectJavascriptInterfaces(view);
        super.onPageFinished(view, url);
    }

}
