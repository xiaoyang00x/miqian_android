package com.miqian.mq.activity;

import android.content.Context;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularProjectFeature;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/10/22
 */

public abstract class RegularDetailActivity extends ProjectDetailActivity {

    protected RegularProjectInfo mInfo;
    private BigDecimal userMaxBuyAmount; // 当前用户对于该标的最大可认购金额

    public static void startActivity(Context context, String subjectId, int prodId) {
        if (RegularBase.REGULAR_PROJECT == prodId) {
            RegularProjectDetailActivity.startActivity(context, subjectId);
        } else if (RegularBase.REGULAR_PLAN == prodId) {
            RegularPlanDetailActivity.startActivity(context, subjectId);
        }
    }

    protected View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_seemore:
                    MobclickAgent.onEvent(mContext, "1071");
                    if (RegularBase.REGULAR_PROJECT == prodId) {
                        // tab说明：0-项目匹配  1-安全保障 2-认购记录
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + "/" + subjectId + "/" + mInfo.getProjectCode() + "/0");
                    } else if (RegularBase.REGULAR_PLAN == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + "/" + subjectId + "/0");
                    }
                    break;
                case R.id.tv_info_right:
                case R.id.rlyt_buy_record:
                    if (RegularBase.REGULAR_PROJECT == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + "/" + subjectId + "/" + mInfo.getProjectCode() + "/2");
                    } else if (RegularBase.REGULAR_PLAN == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + "/" + subjectId + "/2");
                    }
                    break;
                case R.id.tv_dialog_max_amount:
                    et_input.setText(getUpLimit(mInfo.getSubjectMaxBuy(), mInfo.getResidueAmt()).toString());
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

    // 获取数据:定期项目
    @Override
    public void obtainData() {
        // 暂预留
        ICallback<RegularDetailResult> iCallback = new ICallback<RegularDetailResult>() {

            @Override
            public void onSucceed(RegularDetailResult result) {
            }

            @Override
            public void onFail(String error) {
            }
        };
//        swipeRefresh.setRefreshing(true);
        requestData(iCallback);
    }

    public void requestData(final ICallback<RegularDetailResult> iCallback) {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        HttpRequest.getRegularDetail(getApplicationContext(), subjectId, prodId, UserUtil.getUserId(RegularDetailActivity.this), new ICallback<RegularDetailResult>() {

            @Override
            public void onSucceed(RegularDetailResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                if (null != result && null != result.getData()
                        && null != result.getData().getSubjectData()) {
                    showContentView();
                    mInfo = result.getData().getSubjectData();
                    updateUI();
                    iCallback.onSucceed(result);
                }
                swipeRefresh.setRefreshing(false);
                end();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                Uihelper.showToast(mContext, error);
                if (null == mInfo) {
                    showErrorView();
                }
                swipeRefresh.setRefreshing(false);
                end();
            }
        });
    }

    /**
     * 刷新 (已登录)用户在 该定期标的 下的 认购额度
     */
    protected void refreshUserRegularProjectInfo() {
        ICallback<RegularDetailResult> iCallback = new ICallback<RegularDetailResult>() {

            @Override
            public void onSucceed(RegularDetailResult result) {
                jumpToNextPageIfInputValid();
            }

            @Override
            public void onFail(String error) {
            }
        };
        requestData(iCallback);
    }

    protected void updateUI() {
        updateProjectInfo();
        updateFestivalInfo(mInfo.getFestival88(), mInfo.getFestival88_url());
        updateMoreInfo();
        updateProjectFeature();
        updateProjectStatus();
    }

    protected abstract void updateMoreInfo();

    /**
     * 标的基本信息
     */
    protected void updateProjectInfo() {
        /** 标的名称 **/
        tv_name.setText(mInfo.getSubjectName());
        /** 标的名称 **/

        /** 双倍收益的标的、双倍收益卡标的 图标 **/
        if (mInfo.getSubjectType().equals(RegularProjectInfo.TYPE_RATE)) {
            iv_tag.setImageResource(R.drawable.double_rate_detail);
        } else {
            iv_tag.setImageResource(0);
        }
        /** 双倍收益的标的、双倍收益卡标的 图标 **/

        /** 标的描述 如：30天可转 | 100元可转 | 无限购 **/
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mInfo.getTransferFlag())) {
            sb.append(mInfo.getTransferFlag()).append(" | ");
        }
        if (mInfo.getFromInvestmentAmount() != null) {
            sb.append(mInfo.getFromInvestmentAmount().equals(new BigDecimal(0)) ? 1 : mInfo.getFromInvestmentAmount());
            sb.append("元起投").append(" | ");
        }
        if (mInfo.getSubjectMaxBuy() == null || mInfo.getSubjectMaxBuy().compareTo(MAXBUYAMT) == 0) {
            sb.append("无限购");
        } else {
            sb.append("限购");
            sb.append(mInfo.getSubjectMaxBuy());
            sb.append("元");
        }
        tv_description.setText(sb);
        /** 标的描述 如：30天可转 | 100元可转 | 无限购 **/

        /** 标的年化收益 **/
        if (mInfo.getSubjectType().equals(RegularProjectInfo.TYPE_RATE)) { // 双倍收益的标的
            total_profit_rate = new BigDecimal(mInfo.getYearInterest()).multiply(new BigDecimal("2")).toString();
            tv_profit_rate.setText(total_profit_rate);
            tv_profit_rate_unit.setText("%");
        } else if ("Y".equals(mInfo.getPresentationYesNo())) {
            total_profit_rate = new BigDecimal(mInfo.getYearInterest()).
                    add(new BigDecimal(mInfo.getPresentationYearInterest())).
                    toString();
            tv_profit_rate.setText(mInfo.getYearInterest());
            tv_profit_rate_unit.setText(
                    new StringBuilder("+").
                            append(mInfo.getPresentationYearInterest()).
                            append("%"));
        } else {
            total_profit_rate = mInfo.getYearInterest();
            tv_profit_rate.setText(mInfo.getYearInterest());
            tv_profit_rate_unit.setText("%");
        }
        /** 标的年化收益 **/

        /** 标的项目期限 **/
        tv_time_limit.setText(mInfo.getLimit());
        /** 标的项目期限 **/

        /** 标的可认购金额: 剩余金额/总金额 **/
        tv_remain_amount.setText(
                new StringBuilder("可认购金额:￥").
                        append(FormatUtil.formatAmount(mInfo.getResidueAmt())).
                        append("/￥").
                        append(FormatUtil.formatAmount(mInfo.getSubjectTotalPrice())));
        /** 标的可认购金额: 剩余金额/总金额 **/

        /** 标的已认购人数 **/
        tv_people_amount.setText(FormatUtil.formatAmountStr(mInfo.getPersonTime()));
        tv_people_amount.setVisibility(View.VISIBLE);
        tv_info_right.setText(" 人已购买");
        tv_info_right.setOnClickListener(mOnclickListener);
        /** 标的已认购人数 **/
    }

    /**
     * 标的特色
     */
    protected void updateProjectFeature() {
        llyt_project_feature.setVisibility(View.VISIBLE);

        if (RegularBase.REGULAR_PROJECT == prodId) {
            tv1.setText("严格风控");
            iv1.setImageResource(R.drawable.ic_ygfk);
        } else if (RegularBase.REGULAR_PLAN == prodId) {
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

    // 标的状态 － 已开标：底部显示输入框；待开标、已满额：底部显示按钮
    protected void updateProjectStatus() {
        switch (mInfo.getSubjectStatus()) {
            case RegularBase.STATE_1:
            case RegularBase.STATE_2:
            case RegularBase.STATE_3:
            case RegularBase.STATE_4:
                tv_begin_countdown.setVisibility(View.VISIBLE);
                tv_begin_countdown.setText(Uihelper.timeToDateRegular(mInfo.getStartTimestamp()));
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_bl3_v2));
                btn_state.setText("待开标");
                showStateButton();
                hideInputEditText();
                hideViewToLogin();
                break;
            case RegularBase.STATE_5:
                refreshUserMaxBuyAmount();
                tv_begin_countdown.setVisibility(View.GONE);
                tv_dialog_min_amount.setText(FormatUtil.formatAmount(mInfo.getFromInvestmentAmount()));
                addUnit(tv_dialog_min_amount);
                tv_dialog_min_amount.setOnClickListener(mOnclickListener);
                et_input.setHint(new StringBuilder("输入").append(mInfo.getUnitAmount()).append("的整数倍"));
                tv_dialog_max_amount_tip.setText("最大可认购金额");

                tv_dialog_max_amount.setText(FormatUtil.formatAmount(userMaxBuyAmount));
                addUnit(tv_dialog_max_amount); // 增加 元 单位符号
                tv_dialog_max_amount.setOnClickListener(mOnclickListener);
                // 限制输入长度
                et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInfo.getResidueAmt().toString().length())});
                if (UserUtil.hasLogin(getApplicationContext())) {
                    enableInputEditText();
                    hideViewToLogin();
                } else {
                    showViewToLogin();
                    disableInputEditText();
                }
                hideStateButton();
                break;
            default:
                tv_begin_countdown.setVisibility(View.GONE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_b5_v2));
                btn_state.setText("已满额");
                showStateButton();
                hideInputEditText();
                hideViewToLogin();
                break;
        }
    }

    // 跳转到下个页面
    @Override
    public void jumpToNextPageIfInputValid() {
        input = et_input.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            Toast.makeText(getBaseContext(), "认购金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 先判断该标的是否有限购
         * 1、有限购的话则 检查 当前用户对于该标的剩余认购额度 这个字段 是否为空,为空的话则去服务器请求 取得该字段的值
         * 2、无限购的话则 进入后续操作
         **/
        if (mInfo.getSubjectMaxBuy().compareTo(MAXBUYAMT) != 0) {
            if (mInfo.getUserSubjectRemainAmt() == null) {
                refreshUserRegularProjectInfo();
                return;
            }
        }

        /** 最低认购金额(左限额) **/
        BigDecimal leftLimit = mInfo.getFromInvestmentAmount();
        /** 最高认购金额(右限额) **/
        BigDecimal rightLimit = userMaxBuyAmount;
        /** 用户输入的认购金额 **/
        BigDecimal inputAmount = new BigDecimal(input);
        /** 每份投资金额 **/
        BigDecimal perAmount = mInfo.getUnitAmount();
        /** 用户认购金额 ％ 每份投资金额 **/
        BigDecimal remainder = inputAmount.remainder(perAmount);

        /** 用户认购额度小于起始金额 **/
        if (inputAmount.compareTo(leftLimit) == -1) {
            /** 用户认购额度 为当前可认购的最大金额时（此额度小于起始金额） 则可以让用户认购 否则提示错误**/
            if (inputAmount.compareTo(rightLimit) != 0) {
                Toast.makeText(getBaseContext(), "提示：" + leftLimit + "元起投", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /** 用户认购额度大于最大可认购金额 **/
        else if (inputAmount.compareTo(rightLimit) == 1) {
            Toast.makeText(getBaseContext(), "提示：您的认购额已超过该标的最大限购额", Toast.LENGTH_SHORT).show();
            return;
        }
        /** 用户认购额度是否为 每份金额 的整数倍 **/
        else if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            Toast.makeText(getBaseContext(), "提示：请输入" + perAmount + "的整数倍", Toast.LENGTH_SHORT).show();
            return;
        }
        /** 用户认购额度满足条件 进入下个页面 **/
        String interestRateString = total_profit_rate + "%  期限：" + mInfo.getLimit() + "天";
        UserUtil.currenPay(mActivity, FormatUtil.getMoneyString(input), String.valueOf(prodId), subjectId, interestRateString);
        et_input.setText("");
    }

    /**
     * 更新 当前用户 对于 该标的 最大可认购额度
     * 取该标的 最大可认购金额、剩余金额、用户限额 该类产品限额的最小值
     */
    private void refreshUserMaxBuyAmount() {
        if (null == mInfo) {
            return;
        }
        userMaxBuyAmount = getUpLimit(mInfo.getSubjectMaxBuy(), mInfo.getResidueAmt());
        userMaxBuyAmount = getUpLimit(userMaxBuyAmount, mInfo.getUserSubjectRemainAmt());
    }

}
