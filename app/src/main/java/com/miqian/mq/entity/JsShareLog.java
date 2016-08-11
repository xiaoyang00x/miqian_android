package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 16/8/10.
 * 分享后回调给js的数据
 * webview.share_log({'is_success':1,'platform':'1,2'});
 */
public class JsShareLog {
    private int is_success;
    private String platform;

    public int getIs_success() {
        return is_success;
    }

    public void setIs_success(int is_success) {
        this.is_success = is_success;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
