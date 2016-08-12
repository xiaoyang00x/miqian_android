package com.miqian.mq.receiver;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.UserUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Joy on 2015/8/28.
 */
public class JpushHelper {
    public static String TAG = "Jpush";
    public static Context context;
    private static Set<String> tagSet;

    public static void setAlias(Context mContext) {
        context = mContext;
        tagSet = new LinkedHashSet<String>();
        tagSet.clear();
        // 调用 Handler 来异步设置别名
        //登录状态设置用户token别名，并且取消设置的标签
        if (UserUtil.hasLogin(mContext)) {
//            tagSet.clear();
//            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
        } else {
            tagSet.add("unRegister");
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, UserUtil.getToken(mContext)));
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtil.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context.getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    LogUtil.d(TAG, "Set Tag in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context.getApplicationContext(),
                            null,
                            (Set<String>) msg.obj,
                            mTagsCallback);
                    break;
                default:
                    LogUtil.v(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set alias success";
                    LogUtil.e(TAG, logs);
                    LogUtil.e(TAG, logs + "---alias:" + alias + "---code:" + code );
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias due to timeout. Try again after 60s.";
                    LogUtil.e(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtil.e(TAG, logs);
            }
        }
    };
    private static final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag success";
                    Log.e(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set tags due to timeout. Try again after 60s.";
                    Log.e(TAG, logs);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }

    };
}
