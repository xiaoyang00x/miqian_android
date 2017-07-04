package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.CurrentDetailsInfo;
import com.miqian.mq.entity.CurrentDetailsInfoResult;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MQMarqueeTextView;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guolei_wang on 2017/6/26.
 * Email: gwzheng521@163.com
 * Description: 秒钱宝详情页
 */

public class CurrentDetailActivity extends BaseActivity {

    @BindView(R.id.swipe_refresh) MySwipeRefresh swipeRefresh;
    @BindView(R.id.tv_name)  TextView tv_name; // 标的名称
    @BindView(R.id.iv_tag) ImageView iv_tag;
    @BindView(R.id.tv_description) TextView tv_description; // 标的描述
    @BindView(R.id.tv_profit_rate) TextView tv_profit_rate; // 标的年利率
    @BindView(R.id.tv_profit_rate_unit) TextView tv_profit_rate_unit; // 标的年利率 单位
    @BindView(R.id.tv_time_limit) TextView tv_time_limit; // 标的期限
    @BindView(R.id.tv_remain_amount) TextView tv_remain_amount; // 标的剩余金额
    @BindView(R.id.tv_people_amount) TextView tv_people_amount; // 已认购人数
    @BindView(R.id.tv_info_right) TextView tv_info_right; // 默认文字(已认购人数)或原年化收益:

    @BindView(R.id.tv_88) MQMarqueeTextView tv_88; // 88理财节
    @BindView(R.id.layout_details) LinearLayout layout_details;
    private View viewDetail;

    /*  底部输入框相关   */
    @BindView(R.id.et_input) EditText et_input;
    @BindView(R.id.rlyt_dialog) RelativeLayout rlyt_dialog;
    @BindView(R.id.rlyt_input) RelativeLayout rlyt_input; // 底部状态:立即认购输入框
    @BindView(R.id.btn_buy) Button btn_buy;
    @BindView(R.id.view_close_keyboard) View view_close_keyboard;
    @BindView(R.id.btn_state) Button btn_state; // 底部状态:标的状态:已满额 待开标
    @BindView(R.id.tv_dialog_min_amount) TextView tv_dialog_min_amount; // 起投金额
    @BindView(R.id.tv_dialog_max_amount_tip) TextView tv_dialog_max_amount_tip; // 最大可认购金额/实际支付金额 文案
    @BindView(R.id.tv_dialog_max_amount) TextView tv_dialog_max_amount; // 最大可认购金额/实际支付金额


    private InputMethodManager imm;
    /*  底部输入框相关   */

    private String prodId; // 产品类型
    private int status; // 标的状态:聚合标的状态

    private String total_profit_rate; // 标的总年化利率:原始+赠送利率

    private CurrentDetailsInfo mInfo;

    private String input; // 用户输入金额
    private String paymentAmount; // 用户实际支付金额

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private int screenHeight; // 屏幕高度

    @Override
    public void onCreate(Bundle arg0) {
        prodId = getIntent().getStringExtra(Constants.PRODID);
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

    public static void startActivity(Context context, String prodId) {
        Intent intent = new Intent(context, CurrentDetailActivity.class);
        intent.putExtra(Constants.PRODID, prodId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



    @Override
    public void initView() {
        ButterKnife.bind(this);

        view_close_keyboard.setVisibility(View.GONE);
        view_close_keyboard.setOnClickListener(mOnclickListener);
        btn_buy.setOnClickListener(mOnclickListener);
        et_input.addTextChangedListener(mTextWatcher);
        et_input.setOnFocusChangeListener(mOnFousChangeListener);
        et_input.setOnClickListener(mOnclickListener);
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_current_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("秒钱宝");
    }

    @Override
    protected String getPageName() {
        return "秒钱宝详情页";
    }

    // 获取最大可认购金额
    private BigDecimal getUpLimit() {
        BigDecimal upLimit = mInfo.getMaxInvestAmount(); // 最大可认购金额
        BigDecimal leftLimit = mInfo.getRemainAmount(); // 剩余金额

        if (upLimit == null) {
            return leftLimit;
        } else {
            return leftLimit.compareTo(upLimit) < 0 ? leftLimit : upLimit;
        }
    }

    // 跳转到下个页面
    private void jumpToNextPageIfInputValid() {
        if (!TextUtils.isEmpty(input)) {
            BigDecimal downLimit = mInfo.getMinInvestAmount(); // 最低认购金额
            BigDecimal remainderLimit = mInfo.getUnitAmount(); // 续投金额
            BigDecimal upLimit = getUpLimit(); // 最大可认购金额

            BigDecimal money = new BigDecimal(input);
            BigDecimal remainder = money.remainder(remainderLimit);
            if (money.compareTo(downLimit) == -1) {
                Toast.makeText(getBaseContext(), "提示：" + downLimit + "元起投", Toast.LENGTH_SHORT).show();
            } else if (money.compareTo(upLimit) == 1) {
                Toast.makeText(getBaseContext(), "提示：请输入小于等于" + upLimit + "元", Toast.LENGTH_SHORT).show();
            } else if (remainder.compareTo(BigDecimal.ZERO) != 0) {
                Toast.makeText(getBaseContext(), "提示：请输入" + remainderLimit + "的整数倍", Toast.LENGTH_SHORT).show();
            } else {
                //TODO 跳转到认购页
//                String interestRateString = total_profit_rate + "%  期限：" + mInfo.getLimit() + "天";
//                UserUtil.currenPay(mActivity, FormatUtil.getMoneyString(input), String.valueOf(prodId), subjectId, interestRateString, paymentAmount);
//                et_input.setText("");
            }
        } else {
            Toast.makeText(getBaseContext(), "提示：请输入金额", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    // 获取数据:定期项目
    @Override
    public void obtainData() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        swipeRefresh.setRefreshing(true);
        HttpRequest.getCurrentDetail(mContext, prodId, new ICallback<MqResult<CurrentDetailsInfo>>() {

            @Override
            public void onSucceed(MqResult<CurrentDetailsInfo> result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                if (result == null || result.getData() == null) {
                    return;
                }
                showContentView();
                mInfo = result.getData();
                status = Constants.getCurrentStatus(mInfo.getStatus());
                updateUI();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                showErrorView();
            }
        });
    }

    private void updateUI() {
        // 标的名称
        tv_name.setText(mInfo.getProductName());

        // 标的描述
        StringBuilder sb = new StringBuilder();

        if(mInfo.getProductTags() != null && mInfo.getProductTags().size() > 0) {
            for(int i = 0; i < mInfo.getProductTags().size(); i++) {
                if(i != 0) {
                    sb.append(" | ");
                }
                sb.append(mInfo.getProductTags().get(i));
            }
            tv_description.setText(sb);
        }

        // 标的年利率
        tv_profit_rate.setText(mInfo.getYearRate());
        tv_profit_rate_unit.setText("%");
        total_profit_rate = mInfo.getYearRate();

        // 标的项目期限
        tv_time_limit.setText("活存活取");

        // 标的本金 剩余金额 购买人数等信息
        tv_remain_amount.setText(
                new StringBuilder("可认购金额:￥").
                        append(FormatUtil.formatAmount(mInfo.getRemainAmount())).
                        append("/￥").
                        append(FormatUtil.formatAmount(mInfo.getBidAmount())));
        tv_people_amount.setText(String.valueOf(mInfo.getInvestedCount()));
        tv_people_amount.setVisibility(View.VISIBLE);
        tv_info_right.setText(" 人已购买");
        tv_info_right.setOnClickListener(mOnclickListener);

        if (!TextUtils.isEmpty(mInfo.getActivityTitle())) {
            tv_88.setVisibility(View.VISIBLE);
            tv_88.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_88.getPaint().setAntiAlias(true);
            tv_88.setText(mInfo.getActivityTitle());
            if (!TextUtils.isEmpty(mInfo.getActivityJumpUrl())) {
                tv_88.setOnClickListener(mOnclickListener);
            }
        } else {
            tv_88.setVisibility(View.GONE);
        }
        updateProjectStatus();
        updateRegularTransferDetail();
    }

    // 更新标的状态
    private void updateProjectStatus() {
        switch (status) {
            case Constants.STATUS_DKB:
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_bl3_v2));
                btn_state.setText("待开标");
                break;
            case Constants.STATUS_YKB:
                setMaxTime(mInfo.getEndTimeStamp() - System.currentTimeMillis());
                mHandler.post(mRunnable);

                rlyt_dialog.setOnTouchListener(mOnTouchListener);
                rlyt_input.setOnTouchListener(mOnTouchListener);
                btn_state.setVisibility(View.GONE);
                tv_dialog_min_amount.setText(FormatUtil.formatAmount(mInfo.getMinInvestAmount()));
                addUnit(tv_dialog_min_amount);
                tv_dialog_min_amount.setOnClickListener(mOnclickListener);
                et_input.setHint(new StringBuilder("输入").append(mInfo.getUnitAmount()).append("的整数倍"));
                tv_dialog_max_amount_tip.setText("最大可认购金额");

                tv_dialog_max_amount.setText(FormatUtil.formatAmount(getUpLimit()));
                addUnit(tv_dialog_max_amount); // 增加 元 单位符号
                tv_dialog_max_amount.setOnClickListener(mOnclickListener);
                // 限制输入长度
                et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInfo.getRemainAmount().toString().length())});
                break;
            default:
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_b5_v2));
                btn_state.setText("已满额");
                break;
        }
    }

    // 更新 定期项目转让/定期计划转让 view
    private void updateRegularTransferDetail() {
        layout_details.removeAllViews();

        if(mInfo.getTitleColumn() != null && mInfo.getTitleColumn().size() > 0) {
            for(int i = 0; i < mInfo.getTitleColumn().size(); i++) {
                View product_detail_item = getLayoutInflater().inflate(R.layout.product_detail_item, null);
                TextView tv_lable = (TextView)product_detail_item.findViewById(R.id.tv_lable);
                TextView tv_value = (TextView)product_detail_item.findViewById(R.id.tv_value);

                final CurrentDetailsInfo.ColumnInfo columnInfo = mInfo.getTitleColumn().get(i);
                tv_lable.setText(columnInfo.getTitle());
                if(TextUtils.isEmpty(columnInfo.getJumpUrl())) {
                    tv_value.setText(columnInfo.getValue());
                }else {
                    tv_value.setText(">");
                    product_detail_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WebActivity.startActivity(view.getContext(), columnInfo.getJumpUrl());
                        }
                    });
                }

                layout_details.addView(product_detail_item);
//                layout_details.addView(getLayoutInflater().inflate(R.layout.divide_line, null));
            }
        }
    }

    private View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (status != Constants.STATUS_DKB) {
                return;
            }
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > screenHeight / 3)) {
                showKeyBoardView();
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > screenHeight / 3)) {
                hideKeyBoardView();
            }
        }
    };

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_buy:
                    if (!UserUtil.hasLogin(CurrentDetailActivity.this)) {
                        UserUtil.toRegisterctivity(CurrentDetailActivity.this);
                    } else {
                        jumpToNextPageIfInputValid();
                    }
                    break;
                case R.id.view_close_keyboard:
                    closeKeyboard();
                    break;
                case R.id.et_input:
                    showKeyBoardView();
                    break;
                case R.id.tv_88:
                    WebActivity.startActivity(mActivity, mInfo.getActivityJumpUrl());
                    break;
//                case R.id.tv_seemore:
//                    MobclickAgent.onEvent(mContext, "1071");
//                    if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_04 == prodId) {
//                        // tab说明：0-项目匹配  1-安全保障 2-认购记录
//                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
//                    } else if (RegularBase.REGULAR_05 == prodId || RegularBase.REGULAR_06 == prodId) {
//                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");
//                    }
//                    break;
                case R.id.tv_info_right:
                case R.id.rlyt_buy_record:
                    //TODO 点击跳转到认购记录
                    break;
                case R.id.tv_dialog_max_amount:
                    et_input.setText(getUpLimit().toString());
                    Selection.setSelection(et_input.getText(), et_input.getText().toString().length());
                    break;
                case R.id.tv_dialog_min_amount:
                    et_input.setText(mInfo.getMinInvestAmount().toString());
                    Selection.setSelection(et_input.getText(), et_input.getText().toString().length());
                    break;
                default:
                    break;
            }
        }
    };

    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };

    private View.OnFocusChangeListener mOnFousChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showKeyBoardView();
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculatePaymentAmountAndShow();
        }
    };

    // 屏蔽触摸 点击事件
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    // 弹出输入法键盘后额外view
    private void showKeyBoardView() {
        view_close_keyboard.setVisibility(View.VISIBLE);
        rlyt_dialog.setVisibility(View.VISIBLE);
        calculatePaymentAmountAndShow();
    }

    // 隐藏输入法后键盘额外view
    private void hideKeyBoardView() {
        view_close_keyboard.setVisibility(View.GONE);
        rlyt_dialog.setVisibility(View.GONE);
    }

    private void closeKeyboard() {
        if (null != imm) {
            imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
        }
    }

    // 计算实际支付金额
    private void calculatePaymentAmountAndShow() {
        paymentAmount = input = et_input.getText().toString().trim();
    }

    // 增加单位(元)
    private void addUnit(TextView textView) {
        SpannableString spannableString = new SpannableString("元");
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.f3_b4_V2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
    }

    private long maxTime;

    private void setMaxTime(long time) {
        maxTime = time;
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (maxTime <= 0) {
                // 倒计时结束(转让已结束)
                status = Constants.STATUS_YMB;
                updateProjectStatus();
            }
            maxTime -= 1000L;
            mHandler.postDelayed(this, 1000L);
        }
    };

    // 时间差转换为*天*时*分*秒
    private StringBuilder timeToString(long between) {
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between - day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        long minute = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        return new StringBuilder("结束时间:").
                append(String.format("%02d", day)).append("天").
                append(String.format("%02d", hour)).append("时").
                append(String.format("%02d", minute)).append("分").
                append(String.format("%02d", second)).append("秒");
    }

    public static void main(String[] args) {
        System.out.println(String.format("%02d", 1L));
    }

}
