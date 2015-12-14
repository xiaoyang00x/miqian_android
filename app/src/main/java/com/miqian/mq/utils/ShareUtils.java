package com.miqian.mq.utils;

import android.app.Activity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by guolei_wang on 15/12/11.
 */
public class ShareUtils {

    public static void share(Activity mActivity) {
        ShareSDK.initSDK(mActivity);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题：微信、QQ（新浪微博不需要标题）
        oks.setTitle("秒钱理财分秒必赚");  //最多30个字符

        // text是分享文本：所有平台都需要这个字段
        oks.setText("下载秒钱客户端，马上去赚钱");  //最多40个字符

        // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
        //oks.setImagePath(Environment.getExternalStorageDirectory() + "/meinv.jpg");//确保SDcard下面存在此张图片

        //网络图片的url：所有平台
        oks.setImageUrl("https://res.shicaidai.com/owp-war/front/new/images2/wbyh_new_03.jpg");//网络图片rul

        // url：仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.miaoqian.com");   //网友点进链接后，可以看到分享的详情

        // Url：仅在QQ空间使用
        oks.setTitleUrl("http://www.miaoqian.com");  //网友点进链接后，可以看到分享的详情

        // 启动分享GUI
        oks.show(mActivity);
    }
}
