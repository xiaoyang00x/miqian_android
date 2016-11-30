package com.miqian.mq.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.miqian.mq.entity.JsShareLog;
import com.miqian.mq.entity.ShareData;
import com.miqian.mq.listener.JsShareListener;

import java.util.HashMap;
import java.util.LinkedList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by guolei_wang on 15/12/11.
 */
public class ShareUtils {

    public static void share(Activity mActivity, final ShareData shareData, final JsShareListener jsShareListener) {
        if (shareData == null) return;
        try {
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

            oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                @Override
                public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                    if ("SinaWeibo".equals(platform.getName())) {
                        paramsToShare.setUrl(null);
                        paramsToShare.setText(shareData.getContent() + " " + shareData.getContentUrl());
                    }
                }
            });

            oks.setCallback(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    setShareLog(true, platform, jsShareListener);
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    setShareLog(false, platform, jsShareListener);

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            if (!TextUtils.isEmpty(shareData.getShare_platform())) {
                String[] allPlatforms = {"1", "2", "3", "4", "5", "6", "7"};
                String[] platforms = shareData.getShare_platform().split(",");
                String[] hiddenPlatforms = minus(allPlatforms, platforms);
                if (hiddenPlatforms.length > 0) {
                    for (String platformName : hiddenPlatforms) {
                        if (ShareData.PLATFORM_WECHAT.equals(platformName)) {
                            oks.addHiddenPlatform("Wechat");
                        } else if (ShareData.PLATFORM_WECHAT_MOMENTS.equals(platformName)) {
                            oks.addHiddenPlatform("WechatMoments");
                        } else if (ShareData.PLATFORM_SINA.equals(platformName)) {
                            oks.addHiddenPlatform("SinaWeibo");
                        } else if (ShareData.PLATFORM_QQ.equals(platformName)) {
                            oks.addHiddenPlatform("QQ");
                        } else if (ShareData.PLATFORM_QQ_ZONE.equals(platformName)) {
                            oks.addHiddenPlatform("QZone");
                        } else if (ShareData.PLATFORM_EMAIL.equals(platformName)) {
                            oks.addHiddenPlatform("Email");
                        } else if (ShareData.PLATFORM_SHORT_MSG.equals(platformName)) {
                            oks.addHiddenPlatform("ShortMessage");
                        }
                    }
                }
            }

            // 启动分享GUI
            oks.show(mActivity);
        } catch (Exception ignored) {
        }
    }

    //求两个数组的差集
    private static String[] minus(String[] arr1, String[] arr2) {
        LinkedList<String> list = new LinkedList<>();
        LinkedList<String> history = new LinkedList<>();
        String[] longerArr = arr1;
        String[] shorterArr = arr2;
        //找出较长的数组来减较短的数组
        if (arr1.length > arr2.length) {
            longerArr = arr2;
            shorterArr = arr1;
        }
        for (String str : longerArr) {
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : shorterArr) {
            if (list.contains(str)) {
                history.add(str);
                list.remove(str);
            } else {
                if (!history.contains(str)) {
                    list.add(str);
                }
            }
        }

        String[] result = {};
        return list.toArray(result);
    }

    private static void setShareLog(boolean isSuccess, Platform platform, JsShareListener jsShareListener) {
        JsShareLog shareLog = new JsShareLog();
        shareLog.setIs_success(isSuccess ? 1 : 0);
        if ("Wechat".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_WECHAT);
        } else if ("WechatMoments".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_WECHAT_MOMENTS);
        } else if ("SinaWeibo".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_SINA);
        } else if ("QQ".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_QQ);
        } else if ("QZone".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_QQ_ZONE);
        } else if ("Email".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_EMAIL);
        } else if ("ShortMessage".equals(platform.getName())) {
            shareLog.setPlatform(ShareData.PLATFORM_SHORT_MSG);
        }
        Gson gson = new Gson();
        try {
            jsShareListener.shareLog(gson.toJson(shareLog));
        } catch (Exception ignored) {
        }
    }
}
