package com.miqian.mq.activity;

import android.content.Context;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularProjectFeature;
import com.miqian.mq.entity.RegularProjectInfo;
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

    public static void startActivity(Context context, String subjectId, int prodId) {
        if (RegularBase.REGULAR_03 == prodId) {
            RegularProjectDetailActivity.startActivity(context, subjectId);
        } else if (RegularBase.REGULAR_05 == prodId) {
            RegularPlanDetailActivity.startActivity(context, subjectId);
        }
    }

    protected View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_festival:
//                    WebActivity.startActivity(mActivity, mInfo.getFestival88_url());
                    break;
                case R.id.tv_seemore:
                    MobclickAgent.onEvent(mContext, "1071");
                    if (RegularBase.REGULAR_03 == prodId) {
                        // tab说明：0-项目匹配  1-安全保障 2-认购记录
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3");
                    } else if (RegularBase.REGULAR_05 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5");
                    }
                    break;
                case R.id.tv_info_right:
                case R.id.rlyt_buy_record:
                    if (RegularBase.REGULAR_03 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_earn_detail + subjectId + "/3" + "/2");
                    } else if (RegularBase.REGULAR_05 == prodId) {
                        WebActivity.startActivity(mActivity, Urls.web_regular_plan_detail + subjectId + "/5" + "/2");
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
        if (mInfo.getSubjectMaxBuy() == null || mInfo.getSubjectMaxBuy().compareTo(new BigDecimal("999999999999")) == 0) {
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

    // 标的状态 － 已开标：底部显示输入框；待开标、已满额：底部显示按钮
    protected void updateProjectStatus() {
        switch (mInfo.getSubjectStatus()) {
            case RegularBase.STATE_1:
            case RegularBase.STATE_2:
            case RegularBase.STATE_3:
            case RegularBase.STATE_4:
                tv_begin_countdown.setVisibility(View.VISIBLE);
                tv_begin_countdown.setText(Uihelper.timeToDateRegular(mInfo.getStartTimestamp()));
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_bl3_v2));
                btn_state.setText("待开标");
                break;
            case RegularBase.STATE_5:
                tv_begin_countdown.setVisibility(View.GONE);
                rlyt_dialog.setOnTouchListener(mOnTouchListener);
                rlyt_input.setOnTouchListener(mOnTouchListener);
                btn_state.setVisibility(View.GONE);
                tv_dialog_min_amount.setText(FormatUtil.formatAmount(mInfo.getFromInvestmentAmount()));
                addUnit(tv_dialog_min_amount);
                tv_dialog_min_amount.setOnClickListener(mOnclickListener);
                et_input.setHint(new StringBuilder("输入").append(mInfo.getUnitAmount()).append("的整数倍"));
                tv_dialog_max_amount_tip.setText("最大可认购金额");

                tv_dialog_max_amount.setText(FormatUtil.formatAmount(
                        getUpLimit(mInfo.getSubjectMaxBuy(), mInfo.getResidueAmt())));
                addUnit(tv_dialog_max_amount); // 增加 元 单位符号
                tv_dialog_max_amount.setOnClickListener(mOnclickListener);
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

    // 跳转到下个页面
    @Override
    public void jumpToNextPageIfInputValid() {
        input = et_input.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            BigDecimal downLimit = mInfo.getFromInvestmentAmount(); // 最低认购金额
            BigDecimal remainderLimit = mInfo.getUnitAmount(); // 每份投资金额
            BigDecimal upLimit = getUpLimit(mInfo.getSubjectMaxBuy(), mInfo.getResidueAmt()); // 最大可认购金额

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
                UserUtil.currenPay(mActivity, FormatUtil.getMoneyString(input), String.valueOf(prodId), subjectId, interestRateString, input);
                et_input.setText("");
            }
        } else {
            Toast.makeText(getBaseContext(), "提示：请输入金额", Toast.LENGTH_SHORT).show();
        }
    }

}
