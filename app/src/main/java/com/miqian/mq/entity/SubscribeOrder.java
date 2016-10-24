package com.miqian.mq.entity;

/**
 * Created by Jackie on 2015/9/29.
 */
public class SubscribeOrder {

    private String seqNo;//流水号
    private String goldCoin;
    private String goldCoin_url;
    private ShareData shareLink;
    private Popup popup;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(String goldCoin) {
        this.goldCoin = goldCoin;
    }

    public String getGoldCoin_url() {
        return goldCoin_url;
    }

    public void setGoldCoin_url(String goldCoin_url) {
        this.goldCoin_url = goldCoin_url;
    }

    public ShareData getShareLink() {
        return shareLink;
    }

    public void setShareLink(ShareData shareLink) {
        this.shareLink = shareLink;
    }

    public Popup getPopup() {
        return popup;
    }

    public void setPopup(Popup popup) {
        this.popup = popup;
    }
}
