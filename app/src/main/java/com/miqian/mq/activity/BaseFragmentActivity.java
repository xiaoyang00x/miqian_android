package com.miqian.mq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.receiver.NetBroadReceiver;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Joy on 2015/9/1.
 */
public abstract class BaseFragmentActivity extends FragmentActivity
        implements NetBroadReceiver.netEventHandler {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();
    protected Context mContext;
    protected Context mApplicationContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        mApplicationContext = getApplicationContext();

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
        final View parent = getWindow().getDecorView();
        setTitleResource(parent, R.layout.title_bar_layout);
    }

    protected LinearLayout title_container;

    protected void setTitleResource(View parent, int resId) {
        title_container = (LinearLayout) parent.findViewById(R.id.titlebar_container);
        if (title_container != null) {
            View content = LayoutInflater.from(this).inflate(resId, null);
            int head_title_size = getResources().getDimensionPixelSize(R.dimen.title_height);
            content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, head_title_size));
            title_container.addView(content);
            initHeadViews(parent);
        } else {
            LogUtil.e(TAG, "why I am null, *****************");
        }
    }

    protected ImageView img_left;
    protected TextView tv_right;
    private ImageView img_right;
    private View layout_left;
    private View layout_right;
    private TextView title;

    protected void initHeadViews(View parent) {

        img_left = (ImageView) parent.findViewById(R.id.img_left);
        img_right = (ImageView) parent.findViewById(R.id.img_right);
        layout_left = parent.findViewById(R.id.layout_left);
        layout_right = parent.findViewById(R.id.layout_right);
        tv_right = (TextView) parent.findViewById(R.id.tv_right);
        title = (TextView) parent.findViewById(R.id.lable_title);

        setActionLeftListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void showLeftAction(boolean show) {
        if (img_left != null) {
            img_left.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void showRightAction(boolean show) {
        if (layout_right != null) {
            layout_right.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void showRightAction(int resId, boolean show) {
        if (layout_right != null && img_right != null) {
            img_right.setImageResource(resId);
            layout_right.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setActionLeftListener(View.OnClickListener listener) {
        if (layout_left != null) {
            layout_left.setOnClickListener(listener);
        }
    }

    public void setActionRightListener(View.OnClickListener listener) {
        if (layout_right != null) {
            layout_right.setOnClickListener(listener);
        }
    }

    public void setTitle(int resId) {
        if (title != null) {
            title.setText(resId);
        }
    }

    public void setTitle(String str) {
        if (title != null) {
            title.setText(str);
        }
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
            if (UserUtil.hasLogin(getBaseContext()) && Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), false)) {
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
}
