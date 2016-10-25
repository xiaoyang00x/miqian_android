package com.miqian.mq.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MQMarqueeTextView;
import com.miqian.mq.views.MySwipeRefresh;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;

/**
 * @author wangduo
 * @description: 定期赚 定期计划 定期转让详情页
 * @email: cswangduo@163.com
 * @date: 16/6/2
 */
public abstract class ProjectDetailActivity extends BaseActivity {

    protected MySwipeRefresh swipeRefresh;

    /* 标的基本信息 */
    protected TextView tv_begin_countdown; // 开标倒计时
    protected TextView tv_name; // 标的名称
    protected ImageView iv_tag;
    protected TextView tv_description; // 标的描述
    protected TextView tv_profit_rate; // 标的年利率
    protected TextView tv_profit_rate_unit; // 标的年利率 单位％
    protected TextView tv_time_limit; // 标的期限
    protected TextView tv_time_limit_unit; // 标的期限 单位
    protected TextView tv_remain_amount; // 标的可认购金额
    protected TextView tv_people_amount; // 已认购人数
    protected TextView tv_info_right; // 默认文字(已认购人数)或原年化收益:
    /* 标的基本信息 */

    /* 标的活动信息 */
    protected MQMarqueeTextView tv_festival; // 88理财节
    /* 标的活动信息 */

    /* 标的更多信息 */
    protected ViewStub viewstub_detail;
    protected View viewDetail;
    /* 标的更多信息 */

    /* 标的特色相关 */
    protected LinearLayout llyt_project_feature;
    protected ImageView iv1;
    protected ImageView iv2;
    protected ImageView iv3;
    protected TextView tv1;
    protected TextView tv2;
    protected TextView tv3;
    /* 标的特色相关 */

    /* 底部输入框相关 */
    protected EditText et_input;
    protected Button btn_buy;
    protected View view_close_keyboard;
    protected RelativeLayout rlyt_input; // 底部状态:立即认购输入框
    protected RelativeLayout rlyt_dialog;
    protected TextView tv_dialog_min_amount; // 起投金额
    protected TextView tv_dialog_max_amount_tip; // 最大可认购金额/实际支付金额 文案
    protected TextView tv_dialog_max_amount; // 最大可认购金额/实际支付金额
    protected Button btn_state; // 底部状态:标的状态:已满额 待开标
    protected InputMethodManager imm;
    /* 底部输入框相关 */

    protected String subjectId; // 标的ID
    protected int prodId; // 产品类型:定期计划 定期赚
    protected String total_profit_rate; // 标的总年化利率:原始+赠送利率

    protected String input; // 用户输入金额

    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;

    protected int screenHeight; // 屏幕高度

    protected boolean inProcess = false;
    protected final Object mLock = new Object();

    @Override
    public void onCreate(Bundle arg0) {
        subjectId = getIntent().getStringExtra(Constants.SUBJECTID);
        super.onCreate(arg0);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        showDefaultView();
        screenHeight = MobileOS.getScreenHeight(this);
        // 关于Android收起输入法时会出现屏幕部分黑屏解决
        // http://blog.csdn.net/lytxyc/article/details/44622367
        mContentView.getRootView().setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentView.addOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mContentView.removeOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regular_detail;
    }

    public abstract void jumpToNextPageIfInputValid();

    @Override
    public void initView() {
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);

        tv_begin_countdown = (TextView) findViewById(R.id.tv_begin_countdown);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_tag = (ImageView) findViewById(R.id.iv_tag);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_profit_rate = (TextView) findViewById(R.id.tv_profit_rate);
        tv_profit_rate_unit = (TextView) findViewById(R.id.tv_profit_rate_unit);
        tv_time_limit = (TextView) findViewById(R.id.tv_time_limit);
        tv_time_limit_unit = (TextView) findViewById(R.id.tv_time_limit_unit);
        tv_remain_amount = (TextView) findViewById(R.id.tv_remain_amount);
        tv_people_amount = (TextView) findViewById(R.id.tv_people_amount);
        tv_info_right = (TextView) findViewById(R.id.tv_info_right);
        tv_festival = (MQMarqueeTextView) findViewById(R.id.tv_festival);
        viewstub_detail = (ViewStub) findViewById(R.id.viewstub_detail);

        llyt_project_feature = (LinearLayout) findViewById(R.id.llyt_project_feature);
        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);
        iv3 = (ImageView) findViewById(R.id.iv_3);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);

        rlyt_dialog = (RelativeLayout) findViewById(R.id.rlyt_dialog);
        et_input = (EditText) findViewById(R.id.et_input);
        rlyt_input = (RelativeLayout) findViewById(R.id.rlyt_input);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        view_close_keyboard = findViewById(R.id.view_close_keyboard);
        btn_state = (Button) findViewById(R.id.btn_state);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        tv_dialog_min_amount = (TextView) findViewById(R.id.tv_dialog_min_amount);
        tv_dialog_max_amount = (TextView) findViewById(R.id.tv_dialog_max_amount);
        tv_dialog_max_amount_tip = (TextView) findViewById(R.id.tv_dialog_max_amount_tip);

        view_close_keyboard.setVisibility(View.GONE);

        initListener();
    }

    private void initListener() {
        et_input.setOnFocusChangeListener(mOnFousChangeListener);
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        view_close_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtil.hasLogin(ProjectDetailActivity.this)) {
                    UserUtil.showLoginDialog(ProjectDetailActivity.this);
                } else {
                    jumpToNextPageIfInputValid();
                }
            }
        });
        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoardView();
            }
        });
    }

    protected View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (btn_state.getVisibility() == View.VISIBLE) { // 输入框未显示（被按钮覆盖了）
                return;
            }
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > screenHeight / 3)) {
                showKeyBoardView();
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > screenHeight / 3)) {
                hideKeyBoardView();
            }
        }
    };


    protected MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };

    protected View.OnFocusChangeListener mOnFousChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showKeyBoardView();
            }
        }
    };

    /**
     * 屏蔽触摸 键盘弹出后 其它区域的点击事件
     */
    protected View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    /**
     * 弹出输入法键盘后额外显示view
     */
    protected void showKeyBoardView() {
        view_close_keyboard.setVisibility(View.VISIBLE);
        rlyt_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏输入法后键盘额外显示view
     */
    protected void hideKeyBoardView() {
        view_close_keyboard.setVisibility(View.GONE);
        rlyt_dialog.setVisibility(View.GONE);
    }

    protected void closeKeyboard() {
        if (null != imm) {
            imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
        }
    }

    /**
     * 标的活动信息
     *
     * @param festival88
     * @param festival88_url
     */
    protected void updateFestivalInfo(String festival88, final String festival88_url) {
        if (!TextUtils.isEmpty(festival88)) {
            tv_festival.setVisibility(View.VISIBLE);
            tv_festival.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_festival.getPaint().setAntiAlias(true);
            tv_festival.setText(festival88);
            if (!TextUtils.isEmpty(festival88_url)) {
                tv_festival.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebActivity.startActivity(mActivity, festival88_url);
                    }
                });
            }
        } else {
            tv_festival.setVisibility(View.GONE);
        }
    }

    /**
     * 为文本增加单位(元)
     *
     * @param textView
     */
    protected void addUnit(TextView textView) {
        SpannableString spannableString = new SpannableString("元");
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.f3_b4_V2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
    }

    /**
     * 获取最大可认购金额
     *
     * @param upLimit   最大可认购金额
     * @param leftLimit 剩余金额
     * @return
     */
    protected BigDecimal getUpLimit(BigDecimal upLimit, BigDecimal leftLimit) {
        if (upLimit == null) {
            return leftLimit;
        } else {
            return leftLimit.compareTo(upLimit) < 0 ? leftLimit : upLimit;
        }
    }

}
