package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.views.GestureCueView;
import com.miqian.mq.views.GestureLockView;
import com.miqian.mq.views.WFYTitle;

/**
 * 手势锁设置
 * Created by wangduo on 15/12/8.
 */
public class GestureLockSetActivity extends BaseActivity {

    private TextView tv_tip;
    private GestureCueView lockTipView;
    private GestureLockView lockView;
    private String firstEnterPsw; // 缓存第一次设置的密码
    private boolean isFirstSet; // 是否第一次设置密码

    private static final int MINLENGTH_PSW = 4; // 密码最小长度

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gesture_lock_set);
//        initView();
//        initData();
//    }

    @Override
    public void obtainData() {
        initData();
    }

    private static Class<?> desClass; // 设置完手势密码后跳转页面

    public static void startActivity(Context context, Class cl) {
        desClass = cl;
        Intent intent = new Intent(context, GestureLockSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return "设置手势密码";
    }

//    @Override
//    public void onBackPressed() {
//    }

    @Override
    public void initView() {
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        lockTipView = (GestureCueView) findViewById(R.id.lockTipView);
        lockView = (GestureLockView) findViewById(R.id.lockView);
        lockView.setOnPatterChangeListener(onPatterChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_lock_set;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("设置手势密码");
        mTitle.setRightText("跳过");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.saveBoolean(Pref.GESTURESTATE, false, getBaseContext());
                finish();
            }
        });
        mTitle.setIvLeftVisiable(View.GONE);
    }

    private void initData() {
//        setActionLeftListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        isFirstSet = true;
        firstEnterPsw = null;
//        setTitle("设置手势密码");
//        showLeftAction(false);
//        tv_right.setVisibility(View.VISIBLE);
//        tv_right.setText("跳过");
//        showRightAction(true);
//        setActionRightListener(onClickListener);
    }

//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Pref.saveBoolean(Pref.GESTURESTATE, false, getBaseContext());
//            finish();
//        }
//    };

    private void startShake() {
        TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(50);
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        tv_tip.startAnimation(animation);
    }

    private GestureLockView.OnPatterChangeListener onPatterChangeListener = new GestureLockView.OnPatterChangeListener() {
        @Override
        public void onPatterChanged(String password) {
            if (isFirstSet) { // 首次密码设置
                if (password.length() < MINLENGTH_PSW) { // 密码少于指定位数
                    tv_tip.setText(ERROR_LENGTH);
                    lockView.showErrorState();
                    startShake();
                } else {
                    lockTipView.showSelectedPoint(password);
                    isFirstSet = false;
                    firstEnterPsw = password;
                    lockView.resetAndInvalidate();
                    tv_tip.setText(INPUT_AGAIN);
                }
            } else {
                // 密码设置成功
                if (password.equals(firstEnterPsw)) {
                    tv_tip.setText(SET_DONE);
                    Pref.saveBoolean(Pref.GESTURESTATE, true, getBaseContext());
                    Pref.saveString(Pref.GESTUREPSW, firstEnterPsw, getBaseContext());
                    Pref.saveInt(Pref.UNLOCKCOUNT, Constants.MAXCOUNT, getBaseContext());
                    if (null != desClass) {
                        startActivity(getBaseContext(), desClass);
                    }
                    Toast.makeText(getBaseContext(), "手势密码设置成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    lockView.showErrorState();
                    tv_tip.setText(ERROR_INPUT);
                    lockTipView.resetAndInvalidate();
                    isFirstSet = true;
                    startShake();
                }
            }
        }

        @Override
        public void onPatterStart() {
            if (isFirstSet) {
//                tv_tip.setText(INPUT);
            } else {
//                tv_tip.setText(INPUT_AGAIN);
            }
        }
    };

    private static final String INPUT = "请绘制您的手势密码";
    private static final String INPUT_AGAIN = "再次绘制您的手势密码";
    private static final String ERROR_LENGTH = "至少需要连接4个点，请重新绘制";
    private static final String ERROR_INPUT = "两次绘制不同，请重新绘制";
    private static final String SET_DONE = "设置手势密码成功";

}
