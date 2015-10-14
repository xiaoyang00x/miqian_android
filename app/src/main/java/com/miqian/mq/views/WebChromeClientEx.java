package com.miqian.mq.views;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


/******************************************
 * 类描述： TODO
 * 类名称：WebChromeClientEx  
 * @version: 1.0
 * @author: guolei_wang
 * @time: 2015-1-15 下午3:52:12 
 ******************************************/
public class WebChromeClientEx extends WebChromeClient {

    
    @Override
    public  void onProgressChanged(WebView view, int newProgress) {
//        injectJavascriptInterfaces(view);
        super.onProgressChanged(view, newProgress);
    }
    
    @Override
    public  boolean onJsPrompt(WebView view, String url, String message,
            String defaultValue, JsPromptResult result) {
        if (view instanceof SwipeWebView) {

            if (((SwipeWebView) view).handleJsInterface(view, url, message, defaultValue, result)) {
                return true;
            }
        }
        
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
    
    @Override
    public void onReceivedTitle(WebView view, String title) {
//        injectJavascriptInterfaces(view);/**/
    }
    

    
}
