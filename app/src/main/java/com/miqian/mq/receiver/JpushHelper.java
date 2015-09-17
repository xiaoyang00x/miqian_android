package com.miqian.mq.receiver;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.miqian.mq.utils.UserUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Joy on 2015/8/28.
 */
public class JpushHelper {
    public  static String TAG="Alias";
    public static Context context;

    public static void setAlias(Context mContext) {
        context=mContext;
           // 调用 Handler 来异步设置别名

        if (UserUtil.hasLogin(mContext)){
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, UserUtil.getToken(mContext)));
        }

        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "9c718d9123dd4377b0c8bbe65fe0f72e"));
    }
    private static final int MSG_SET_ALIAS = 1001;
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context.getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }};

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。


                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
}
