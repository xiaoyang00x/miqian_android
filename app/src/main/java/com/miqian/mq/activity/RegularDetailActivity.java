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
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularEarnDetail;
import com.miqian.mq.entity.RegularProjectFeature;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.entity.RegularProjectMatch;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期赚 定期计划 定期转让详情页
 * @email: cswangduo@163.com
 * @date: 16/6/2
 */
public class RegularDetailActivity extends BaseActivity {

    private MySwipeRefresh swipeRefresh;

    private TextView tv_begin_countdown; // 开标倒计时
    private TextView tv_name; // 标的名称
    private ImageView iv_tag;
    private TextView tv_description; // 标的描述
    private TextView tv_profit_rate; // 标的年利率
    private TextView tv_profit_rate_unit; // 标的年利率 单位
    private TextView tv_time_limit; // 标的期限
    private TextView tv_remain_amount; // 标的剩余金额
    private TextView tv_people_amount; // 已认购人数
    private TextView tv_info_right; // 默认文字(已认购人数)或原年化收益:

    private TextView tv_88; // 88理财节

    private ViewStub viewstub_detail;
    private View viewDetail;

    /*  标的特色相关   */
    private LinearLayout llyt_project_feature;
    private TextView tv_project_feature;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    /*  标的特色相关   */

    /*  底部输入框相关   */
    private EditText et_input;
    private Button btn_buy;
    private View view_close_keyboard;
    private RelativeLayout rlyt_input; // 底部状态:立即认购输入框
    private RelativeLayout rlyt_dialog;
    private TextView tv_dialog_min_amount; // 起投金额
    private TextView tv_dialog_max_amount_tip; // 最大可认购金额/实际支付金额 文案
    private TextView tv_dialog_max_amount; // 最大可认购金额/实际支付金额
    private Button btn_state; // 底部状态:标的状态:已满额 待开标
    private InputMethodManager imm;
    /*  底部输入框相关   */

    private String subjectId; // 标的ID
    private int prodId; // 产品类型:定期计划 定期赚
    private String subjectStatus; // 标的状态

    private String total_profit_rate; // 标的总年化利率:原始+赠送利率

    private RegularProjectInfo mInfo;

    private String input; // 用户输入金额
    private String paymentAmount; // 用户实际支付金额

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private int screenHeight; // 屏幕高度

    @Override
    public void onCreate(Bundle arg0) {
        subjectId = getIntent().getStringExtra(Constants.SUBJECTID);
        prodId = getIntent().getIntExtra(Constants.PRODID, -1);
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

    public static void startActivity(Context context, String subjectId, int prodId) {
        Intent intent = new Intent(context, RegularDetailActivity.class);
        intent.putExtra(Constants.SUBJECTID, subjectId);
        intent.putExtra(Constants.PRODID, prodId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

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
        tv_remain_amount = (TextView) findViewById(R.id.tv_remain_amount);
        tv_people_amount = (TextView) findViewById(R.id.tv_people_amount);
        tv_info_right = (TextView) findViewById(R.id.tv_info_right);
        tv_88 = (TextView) findViewById(R.id.tv_88);
        viewstub_detail = (ViewStub) findViewById(R.id.viewstub_detail);

        llyt_project_feature = (LinearLayout) findViewById(R.id.llyt_project_feature);
        tv_project_feature = (TextView) findViewById(R.id.tv_project_feature);
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
        view_close_keyboard.setOnClickListener(mOnclickListener);
        btn_buy.setOnClickListener(mOnclickListener);
        et_input.addTextChangedListener(mTextWatcher);
        et_input.setOnFocusChangeListener(mOnFousChangeListener);
        et_input.setOnClickListener(mOnclickListener);
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regular_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        switch (prodId) {
            case RegularBase.REGULAR_03:
                mTitle.setTitleText("定期项目");
                break;
//            case RegularBase.REGULAR_04:
//                mTitle.setTitleText("定期项目转让");
//                break;
            case RegularBase.REGULAR_05:
                mTitle.setTitleText("定期计划");
                break;
//            case RegularBase.REGULAR_06:
//                mTitle.setTitleText("定期计划转让");
//                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        switch (prodId) {
            case RegularBase.REGULAR_03:
                return "定期赚详情";
//            case RegularBase.REGULAR_04:
//                return "定期赚转让详情";
            case RegularBase.REGULAR_05:
                return "定期计划详情";
//            case RegularBase.REGULAR_06:
//                return "定期计划转让详情";
            default:
                return "";
        }
    }

    // 获取最大可认购金额
    private BigDecimal getUpLimit() {
        BigDecimal upLimit = mInfo.getSubjectMaxBuy(); // 最大可认购金额
        BigDecimal leftLimit = mInfo.getResidueAmt(); // 剩余金额

        if (upLimit == null) {
            return leftLimit;
        } else {
            return leftLimit.compareTo(upLimit) < 0 ? leftLimit : upLimit;
        }
    }

    // 跳转到下个页面
    private void jumpToNextPageIfInputValid() {
        if (!TextUtils.isEmpty(input)) {
            BigDecimal downLimit = mInfo.getFromInvestmentAmount(); // 最低认购金额
            BigDecimal remainderLimit = mInfo.getContinueInvestmentLimit(); // 续投金额
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
                String interestRateString = total_profit_rate + "%  期限：" + mInfo.getLimit() + "天";
                UserUtil.currenPay(mActivity, FormatUtil.getMoneyString(input), String.valueOf(prodId), subjectId, interestRateString, paymentAmount);
                et_input.setText("");
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
        HttpRequest.getRegularDetail(mContext, subjectId, prodId, new ICallback<RegularDetailResult>() {

            @Override
            public void onSucceed(RegularDetailResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                if (result == null || result.getData() == null
                        || result.getData().getSubjectData() == null
                        || result.getData().getSubjectData().size() <= 0) {
                    return;
                }
                showContentView();
                mInfo = result.getData().getSubjectData().get(0);
                subjectStatus = mInfo.getSubjectStatus();
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
        updateRegularCom();
        switch (prodId) {
            case RegularBase.REGULAR_03:
                updateRegularProjectDetail();
                break;
            case RegularBase.REGULAR_05:
                updateRegularPlanDetail();
                break;
//            case RegularBase.REGULAR_04:
//            case RegularBase.REGULAR_06:
//                updateRegularTransferDetail();
//                break;
            default:
                break;
        }
        updateRegularProjectFeature();
    }

    // 更新 定期项目详情 中间部分 标的信息
    private void updateRegularProjectDetail() {
        ArrayList<RegularEarnDetail> mList = mInfo.getSubjectBar();
        if (mList == null || mList.size() <= 0) {
            return;
        }

        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.regular_project_detail);
            viewDetail = viewstub_detail.inflate();
        }
        LinearLayout content = (LinearLayout) viewDetail.findViewById(R.id.llyt_content);
        TextView tv_seemore = (TextView) viewDetail.findViewById(R.id.tv_seemore);

        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        int count = mList.size() > 4 ? 4 : mList.size();
        for (int index = 0; index < count; index++) {
            RegularEarnDetail detail = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_regular_project_detail, null);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(detail.getTitle());
            ((TextView) mView.findViewById(R.id.tv_right)).setText(detail.getName());
            content.addView(mView);
        }
        tv_seemore.setOnClickListener(mOnclickListener);
    }

    // 更新 定期计划详情 中间部分 标的信息
    private void updateRegularPlanDetail() {
        ArrayList<RegularProjectMatch> mList = mInfo.getMatchItem();

        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.regular_plan_detail);
            viewDetail = viewstub_detail.inflate();
        }
        LinearLayout content = (LinearLayout) viewDetail.findViewById(R.id.llyt_content);
        TextView tv_seemore = (TextView) viewDetail.findViewById(R.id.tv_seemore);
        tv_seemore.setOnClickListener(mOnclickListener);

        if (mList == null || mList.size() <= 0) {
            viewDetail.findViewById(R.id.llyt1).setVisibility(View.GONE);
            return;
        }

        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        // 匹配项目(默认展示三条)。如不足三条，则有几条展示几条
        int count = mList.size() > 3 ? 3 : mList.size();
        for (int index = 0; index < count; index++) {
            RegularProjectMatch projectMatch = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_regular_plan_detail, null);
            ((TextView) mView.findViewById(R.id.tv_content)).setText(projectMatch.getName());
            if (index == count - 1) {
                mView.findViewById(R.id.line).setVisibility(View.GONE);
            }
            content.addView(mView);
        }
    }

    // 更新 定期项目 定期计划 公共部分 头部 (标的信息)
    private void updateRegularCom() {
        // 标的名称
        tv_name.setText(mInfo.getSubjectName());

        if (mInfo.getSubjectType().equals(RegularProjectInfo.TYPE_RATE)) {
            iv_tag.setImageResource(R.drawable.double_rate_detail);
        } else if (mInfo.getSubjectType().equals(RegularProjectInfo.TYPE_CARD)) {
            iv_tag.setImageResource(R.drawable.double_card_detail);
        } else {
            iv_tag.setImageResource(0);
        }

        // 标的描述
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mInfo.getTransferFlag())) {
            sb.append(mInfo.getTransferFlag()).append(" | ");
        }
        if (mInfo.getFromInvestmentAmount() != null) {
            sb.append(mInfo.getFromInvestmentAmount().equals(new BigDecimal(0)) ? 1 : mInfo.getFromInvestmentAmount());
            sb.append("元起投").append(" | ");
        }
        if (RegularBase.REGULAR_04 == prodId || RegularBase.REGULAR_06 == prodId) {
            sb.append("次日计息");
        } else if (mInfo.getSubjectMaxBuy() == null || mInfo.getSubjectMaxBuy().compareTo(new BigDecimal("999999999999")) == 0) {
            sb.append("无限购");
        } else {
            sb.append("限购");
            sb.append(mInfo.getSubjectMaxBuy());
            sb.append("元");
        }
        tv_description.setText(sb);

        // 标的是否有加息 (转让标的 无加息)
        if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId) {
            // 标的年利率
            tv_profit_rate.setText(mInfo.getYearInterest());
            tv_profit_rate_unit.setText("%");
            if (mInfo.getSubjectType().equals(RegularProjectInfo.TYPE_RATE)) {
                total_profit_rate = new BigDecimal(mInfo.getYearInterest()).multiply(new BigDecimal("2")).toString();
                tv_profit_rate.setText(total_profit_rate);
            } else if ("Y".equals(mInfo.getPresentationYesNo())) {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(mInfo.getPresentationYearInterest()).
                                append("%"));
                total_profit_rate = new BigDecimal(mInfo.getYearInterest()).
                        add(new BigDecimal(mInfo.getPresentationYearInterest())).
                        toString();
            } else {
                total_profit_rate = mInfo.getYearInterest();
            }
        } else {
            // 标的年利率
            tv_profit_rate.setText(mInfo.getPredictRate());
            tv_profit_rate_unit.setText("%");
            total_profit_rate = mInfo.getPredictRate();
        }

        // 标的项目期限
        tv_time_limit.setText(mInfo.getLimit());

        // 标的本金 剩余金额 购买人数等信息
        if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId) {
            tv_remain_amount.setText(
                    new StringBuilder("可认购金额:￥").
                            append(FormatUtil.formatAmount(mInfo.getResidueAmt())).
                            append("/￥").
                            append(FormatUtil.formatAmount(mInfo.getSubjectTotalPrice())));
            tv_people_amount.setText(FormatUtil.formatAmountStr(mInfo.getPersonTime()));
            tv_people_amount.setVisibility(View.VISIBLE);
            tv_info_right.setText(" 人已购买");
            tv_info_right.setOnClickListener(mOnclickListener);
        } else { // 转让
            tv_remain_amount.setText(
                    new StringBuilder("可认购本金:￥").
                            append(FormatUtil.formatAmount(mInfo.getResidueAmt())));
            tv_info_right.setText(
                    new StringBuilder("原标的年化收益: ").
                            append(mInfo.getOriginalRate()).
                            append("%"));
            tv_people_amount.setVisibility(View.GONE);
        }
        if ((RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId)
                && !TextUtils.isEmpty(mInfo.getFestival88())) {
            tv_88.setVisibility(View.VISIBLE);
            tv_88.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_88.getPaint().setAntiAlias(true);
            tv_88.setText(mInfo.getFestival88());
            if (!TextUtils.isEmpty(mInfo.getFestival88_url())) {
                tv_88.setOnClickListener(mOnclickListener);
            }
        } else {
            tv_88.setVisibility(View.GONE);
        }
        updateProjectStatus();
    }

    // 更新标的状态
    private void updateProjectStatus() {
        switch (subjectStatus) {
            case RegularBase.STATE_00:
                tv_begin_countdown.setVisibility(View.VISIBLE);
                tv_begin_countdown.setText(Uihelper.timeToDateRegular(mInfo.getStartTimestamp()));
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_bl3_v2));
                btn_state.setText("待开标");
                break;
            case RegularBase.STATE_01:
                if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId) {
                    tv_begin_countdown.setVisibility(View.GONE);
                } else {
                    tv_begin_countdown.setVisibility(View.VISIBLE);
                    setMaxTime(mInfo.getEndTimestamp() - System.currentTimeMillis());
                    mHandler.post(mRunnable);
                }
                rlyt_dialog.setOnTouchListener(mOnTouchListener);
                rlyt_input.setOnTouchListener(mOnTouchListener);
                btn_state.setVisibility(View.GONE);
                tv_dialog_min_amount.setText(FormatUtil.formatAmount(mInfo.getFromInvestmentAmount()));
                addUnit(tv_dialog_min_amount);
                tv_dialog_min_amount.setOnClickListener(mOnclickListener);
                if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId) {
                    et_input.setHint(new StringBuilder("输入").append(mInfo.getContinueInvestmentLimit()).append("的整数倍"));
                    tv_dialog_max_amount_tip.setText("最大可认购金额");

                    tv_dialog_max_amount.setText(FormatUtil.formatAmount(getUpLimit()));
                    addUnit(tv_dialog_max_amount); // 增加 元 单位符号
                    tv_dialog_max_amount.setOnClickListener(mOnclickListener);
                } else {
                    et_input.setHint("输入认购本金");
                    tv_dialog_max_amount_tip.setText("实际支付金额");
                }
                // 限制输入长度
                et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInfo.getResidueAmt().toString().length())});
                break;
            default:
                tv_begin_countdown.setVisibility(View.GONE);
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_b5_v2));
                btn_state.setText("已满额");
                break;
        }
    }

    // 更新 定期项目转让/定期计划转让 view
    private void updateRegularTransferDetail() {
        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.regular_transfer_detail);
            viewDetail = viewstub_detail.inflate();
        }
        TextView tv_info1 = (TextView) viewDetail.findViewById(R.id.tv_info1);
        TextView tv_info2 = (TextView) viewDetail.findViewById(R.id.tv_info2);
        TextView tv_info3 = (TextView) viewDetail.findViewById(R.id.tv_info3);
        RelativeLayout rlyt_buy_record = (RelativeLayout) viewDetail.findViewById(R.id.rlyt_buy_record);
        RelativeLayout rlyt_original_name = (RelativeLayout) viewDetail.findViewById(R.id.rlyt_original_name);
        TextView tv_original_name = (TextView) viewDetail.findViewById(R.id.tv_original_name);
        tv_info1.setText(FormatUtil.formatAmount(mInfo.getResidueAmt()));
        tv_info2.setText(FormatUtil.formatAmount(mInfo.getActualAmt()));
        tv_info3.setText(FormatUtil.formatAmount(mInfo.getPredictIncome()));
        tv_original_name.setText(mInfo.getOriginalSubjectName());
        rlyt_buy_record.setOnClickListener(mOnclickListener);
        rlyt_original_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegularDetailActivity.startActivity(getBaseContext(), mInfo.getOriginalSubjectId(), mInfo.getProdId() - 1);
            }
        });
    }

    // 更新标的特色
    private void updateRegularProjectFeature() {
        if (RegularBase.REGULAR_04 == prodId || RegularBase.REGULAR_06 == prodId) {
            llyt_project_feature.setVisibility(View.GONE);
            tv_project_feature.setVisibility(View.GONE);
            return;
        }
        llyt_project_feature.setVisibility(View.VISIBLE);
        tv_project_feature.setVisibility(View.VISIBLE);

        if (RegularBase.REGULAR_03 == prodId) {
            tv1.setText("严格风控");
            iv1.setImageResource(R.drawable.ic_ygfk);
        } else if (RegularBase.REGULAR_05 == prodId) {
            tv1.setText("分散投资");
            iv1.setImageResource(R.drawable.ic_fstz);
        }

        ArrayList<RegularProjectFeature> mList = mInfo.getSubjectFeature();
        if (null == mList || mList.size() < 3) {
            return;
        }
        final RegularProjectFeature feature1 = mList.get(0);
        tv1.setText(feature1.getTitle());
        if (!TextUtils.isEmpty(feature1.getImgUrl())) {
            imageLoader.displayImage(feature1.getImgUrl(), iv1, options);
        }
        if (!TextUtils.isEmpty(feature1.getJumpUrl())) {
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(getBaseContext(), feature1.getJumpUrl());
                }
            });
        }

        final RegularProjectFeature feature2 = mList.get(1);
        tv2.setText(feature2.getTitle());
        if (!TextUtils.isEmpty(feature2.getImgUrl())) {
            imageLoader.displayImage(feature2.getImgUrl(), iv2, options);
        }
        if (!TextUtils.isEmpty(feature2.getJumpUrl())) {
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(getBaseContext(), feature2.getJumpUrl());
                }
            });
        }

        final RegularProjectFeature feature3 = mList.get(2);
        tv3.setText(feature3.getTitle());
        if (!TextUtils.isEmpty(feature3.getImgUrl())) {
            imageLoader.displayImage(feature3.getImgUrl(), iv3, options);
        }
        if (!TextUtils.isEmpty(feature3.getJumpUrl())) {
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(getBaseContext(), feature3.getJumpUrl());
                }
            });
        }
    }

    private View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (!subjectStatus.equals(RegularBase.STATE_01)) {
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
                    if (!UserUtil.hasLogin(RegularDetailActivity.this)) {
                        UserUtil.showLoginDialog(RegularDetailActivity.this);
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
                    WebActivity.startActivity(mActivity, mInfo.getFestival88_url());
                    break;
                case R.id.tv_seemore:
                    MobclickAgent.onEvent(mContext, "1071");
                    if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_04 == prodId) {
                        // tab说明：0-项目匹配  1-安全保障 2-认购记录
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if (RegularBase.REGULAR_05 == prodId || RegularBase.REGULAR_06 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");
                    }
                    break;
                case R.id.tv_info_right:
                case R.id.rlyt_buy_record:
                    if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_04 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3" + "/2");
                    } else if (RegularBase.REGULAR_05 == prodId || RegularBase.REGULAR_06 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5" + "/2");
                    }
                    break;
                case R.id.tv_dialog_max_amount:
                    et_input.setText(getUpLimit().toString());
                    Selection.setSelection(et_input.getText(), et_input.getText().toString().length());
                    break;
                case R.id.tv_dialog_min_amount:
                    et_input.setText(mInfo.getFromInvestmentAmount().toString());
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
        if (RegularBase.REGULAR_03 == prodId || RegularBase.REGULAR_05 == prodId) {
            return;
        }
        try {
            if (TextUtils.isEmpty(paymentAmount) || null == mInfo.getDiscountRate()) {
                tv_dialog_max_amount.setText("--");
            } else {
                BigDecimal temp = mInfo.getDiscountRate().divide(new BigDecimal(100));
                temp = new BigDecimal(1).subtract(temp);
                temp = new BigDecimal(input).multiply(temp);
                paymentAmount = FormatUtil.getMoneyString(temp.toString());
                String str = FormatUtil.formatAmountStr(paymentAmount);
                tv_dialog_max_amount.setText(str);
            }
        } catch (Exception e) {
            tv_dialog_max_amount.setText("--");
        }
        addUnit(tv_dialog_max_amount);
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
                subjectStatus = RegularBase.STATE_03;
                updateProjectStatus();
            }
            tv_begin_countdown.setText(timeToString(maxTime));
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
