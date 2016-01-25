package com.miqian.mq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.receiver.NetBroadReceiver;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Joy on 2015/9/1.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements NetBroadReceiver.netEventHandler {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();
    protected Context mContext;
    protected Context mApplicationContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        mApplicationContext = getApplicationContext();

        if (MobileOS.isKitOrNewer()) {
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setStatusBarTintResource(R.color.mq_r1);
        }


        //注册监听home键被按下广播
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        //注册监听锁屏广播
        registerReceiver(mScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onNetChange() {

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //友盟统计
        if (!exsitFragment) {
            MobclickAgent.onPageStart(getPageName());
        }
        if (MyApplication.isBackStage()) {
            MyApplication.setIsBackStage(false);
            if (UserUtil.hasLogin(getBaseContext()) &&
                    Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), false) &&
                    !TextUtils.isEmpty(Pref.getString(Pref.GESTUREPSW, getBaseContext(), null))) {
                long curTime = System.currentTimeMillis();
                if (curTime - MyApplication.homePressTime > 20 * 1000) {
                    GestureLockVerifyActivity.startActivity(getBaseContext(), null);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //友盟统计
        if (!exsitFragment) {
            MobclickAgent.onPageEnd(getPageName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册广播
        unregisterReceiver(mHomeKeyEventReceiver);
        unregisterReceiver(mScreenOffReceiver);
    }

    public BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    // 设置为在后台运行的标志
                    // 表示按了home键,程序到了后台
                    MyApplication.getInstance().setIsBackStage(true);

                }
            }
        }
    };

    public BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 设置为在后台运行的标志
            // 表示按了home键,程序到了后台
            MyApplication.getInstance().setIsBackStage(true);
        }
    };

    /**
     * 获取页面名称，用于友盟页面统计
     *
     * @return
     */
    protected abstract String getPageName();

    public void setExsitFragment(boolean exsitFragment) {
        this.exsitFragment = exsitFragment;
    }

    private boolean exsitFragment = false;//当前Activity是否存在Fragment

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (Uihelper.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
