package com.miqian.mq.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.miqian.mq.entity.ShareData;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by guolei_wang on 15/12/11.
 */
public class ShareUtils {

    public static void share(Activity mActivity,String json) {
        if(TextUtils.isEmpty(json)) return;
        try{
            Gson gson = new Gson();
            ShareData shareData = gson.fromJson(json, ShareData.class);
            ShareSDK.initSDK(mActivity);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // title标题：微信、QQ（新浪微博不需要标题）
            oks.setTitle(shareData.getTitle());  //最多30个字符

            // text是分享文本：所有平台都需要这个字段
            oks.setText(shareData.getContent());  //最多40个字符

            // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
            //oks.setImagePath(Environment.getExternalStorageDirectory() + "/meinv.jpg");//确保SDcard下面存在此张图片

            //网络图片的url：所有平台
            oks.setImageUrl(shareData.getImageUrl());//网络图片rul

            // url：仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(shareData.getContentUrl());   //网友点进链接后，可以看到分享的详情

            // Url：仅在QQ空间使用
            oks.setTitleUrl(shareData.getContentUrl());  //网友点进链接后，可以看到分享的详情

            // 启动分享GUI
            oks.show(mActivity);
        }catch (Exception e) {

        }
    }
}
