package com.miqian.mq.entity;

/**
 * Created by Jackie on 2016/1/11.
 * 版本强制更新
 */
public class UpdateInfo {

    private String title;//标题
    private String versionDesc;//版本信息
    private String softDesc;//软件大小
    private String upgradeDesc;//更新内容
    private String downloadUrl;//下载地址
    private String upgradeSign;//  0：无需升级1：建议升级2：强制升级
    private String version;//需强制更新的版本号

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getSoftDesc() {
        return softDesc;
    }

    public void setSoftDesc(String softDesc) {
        this.softDesc = softDesc;
    }

    public String getUpgradeDesc() {
        return upgradeDesc;
    }

    public void setUpgradeDesc(String upgradeDesc) {
        this.upgradeDesc = upgradeDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpgradeSign() {
        return upgradeSign;
    }

    public void setUpgradeSign(String upgradeSign) {
        this.upgradeSign = upgradeSign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
