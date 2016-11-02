package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.CurrentDetailInfo;
import com.miqian.mq.entity.CurrentDetailMoreInfoItem;
import com.miqian.mq.entity.CurrentDetailResult;
import com.miqian.mq.entity.CurrentProjectInfo;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 活期详情
 * @email: cswangduo@163.com
 * @date: 16/10/23
 */
public class CurrentDetailActivity extends ProjectDetailActivity {

    private CurrentDetailInfo mInfo;
    private BigDecimal userMaxBuyAmount; // 当前用户对于该标的最大可认购金额

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, CurrentDetailActivity.class);
        intent.putExtra(Constants.SUBJECTID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        prodId = RegularBase.CURRENT_PROJECT;
        super.onCreate(arg0);
    }

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
        HttpRequest.getCurrentDetail(mContext, subjectId, UserUtil.getUserId(CurrentDetailActivity.this), new ICallback<CurrentDetailResult>() {

            @Override
            public void onSucceed(CurrentDetailResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                if (null != result && null != result.getData()) {
                    mInfo = result.getData();
                    showContentView();
                    updateUI();
                }
                swipeRefresh.setRefreshing(false);
                end();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                Uihelper.showToast(mContext, error);
                showErrorView();
            }
        });
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("活期理财");
    }

    @Override
    protected String getPageName() {
        return null;
    }

    private void updateUI() {
        updateProjectInfo();
        updateFestivalInfo(mInfo.getFestival().getFestival88(), mInfo.getFestival().getFestival88_url());
        updateMoreInfo();
        llyt_project_feature.setVisibility(View.GONE);
        updateProjectStatus();
    }

    /**
     * 标的基本信息
     */
    private void updateProjectInfo() {
        CurrentProjectInfo projectInfo = mInfo.getCurrentInfo();
        /** 标的名称 **/
        tv_name.setText(projectInfo.getSubjectName());
        /** 标的名称 **/

        /** 双倍收益的标的、双倍收益卡标的 图标 **/
        iv_tag.setImageResource(0);
        /** 双倍收益的标的、双倍收益卡标的 图标 **/

        /** 标的描述 如：30天可转 | 100元可转 | 无限购 **/
        StringBuilder sb = new StringBuilder();
        if (0 == projectInfo.getCanTransfer()) {
            sb.append("不可转让").append(" | ");
        } else if (1 == projectInfo.getCanTransfer()) {
            sb.append("可转让").append(" | ");
        }
        if (projectInfo.getFromInvestmentAmount() != null) {
            sb.append(projectInfo.getFromInvestmentAmount().equals(new BigDecimal(0)) ? 1 : projectInfo.getFromInvestmentAmount());
            sb.append("元起投").append(" | ");
        }
        if (projectInfo.getSubjectMaxBuy() == null || projectInfo.getSubjectMaxBuy().compareTo(MAXBUYAMT) == 0) {
            sb.append("无限购");
        } else {
            sb.append("限购");
            sb.append(projectInfo.getSubjectMaxBuy());
            sb.append("元");
        }
        tv_description.setText(sb);
        /** 标的描述 如：30天可转 | 100元可转 | 无限购 **/

        /** 标的年化收益 **/
        total_profit_rate = projectInfo.getYearInterest();
        tv_profit_rate.setText(projectInfo.getYearInterest());
        tv_profit_rate_unit.setText("%");
        /** 标的年化收益 **/

        /** 标的项目期限 **/
        tv_time_limit.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                getResources().getDimensionPixelSize(R.dimen.mq_font1_v2));
        tv_time_limit.setText(projectInfo.getLimit());
        tv_time_limit_unit.setVisibility(View.GONE);
        /** 标的项目期限 **/

        /** 标的可认购金额: 剩余金额/总金额 **/
        tv_remain_amount.setText(
                new StringBuilder("可认购金额:￥").
                        append(FormatUtil.formatAmount(projectInfo.getResidueAmt())).
                        append("/￥").
                        append(FormatUtil.formatAmount(projectInfo.getSubjectTotalPrice())));
        /** 标的可认购金额: 剩余金额/总金额 **/

        /** 标的已认购人数 **/
        tv_people_amount.setText(FormatUtil.formatAmountStr(projectInfo.getPersonTime()));
        tv_people_amount.setVisibility(View.VISIBLE);
        tv_info_right.setText(" 人已购买");
        tv_info_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mActivity, Urls.web_current_detail + subjectId + "/2");
            }
        });
        /** 标的已认购人数 **/
    }

    /**
     * 标的更多信息
     */
    private void updateMoreInfo() {
        if (null == mInfo || null == mInfo.getSubjectBar()) {
            return;
        }
        ArrayList<CurrentDetailMoreInfoItem> mList = mInfo.getSubjectBar().getList();
        if (mList == null || mList.size() <= 0) {
            return;
        }

        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.current_project_detail_more);
            viewDetail = viewstub_detail.inflate();
        }
        LinearLayout content = (LinearLayout) viewDetail.findViewById(R.id.llyt_content);
        content.removeAllViews();

        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        int count = mList.size();
        for (int index = 0; index < count; index++) {
            final CurrentDetailMoreInfoItem item = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_project_detail, null);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(item.getTitle());
            if (!TextUtils.isEmpty(item.getJumpUrl())) {
                ((TextView) mView.findViewById(R.id.tv_right)).setText(">");
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebActivity.startActivity(getBaseContext(), item.getJumpUrl());
                    }
                });
            } else {
                ((TextView) mView.findViewById(R.id.tv_right)).setText(item.getContent());
            }
            content.addView(mView);
        }

        View mView = mInflater.inflate(R.layout.item_project_detail, null);
        ((TextView) mView.findViewById(R.id.tv_left)).setText("更多信息");
        ((TextView) mView.findViewById(R.id.tv_right)).setText(">");
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDetailMoreInfoActivity.startActivity(getBaseContext(), mInfo.getSubjectBar().getMore());
            }
        });
        mView.findViewById(R.id.line).setVisibility(View.GONE);
        content.addView(mView);
    }

    // 标的状态 － 已开标：底部显示输入框；待开标、已满额：底部显示按钮
    protected void updateProjectStatus() {
        switch (mInfo.getCurrentInfo().getSubjectStatus()) {
            case RegularBase.STATE_1:
            case RegularBase.STATE_2:
            case RegularBase.STATE_3:
            case RegularBase.STATE_4:
                tv_begin_countdown.setVisibility(View.VISIBLE);
                tv_begin_countdown.setText(Uihelper.timeToDateRegular(mInfo.getCurrentInfo().getStartTimestamp()));
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_bl3_v2));
                btn_state.setText("待开标");
                break;
            case RegularBase.STATE_5:
                refreshUserMaxBuyAmount();
                tv_begin_countdown.setVisibility(View.GONE);
                rlyt_dialog.setOnTouchListener(mOnTouchListener);
                rlyt_input.setOnTouchListener(mOnTouchListener);
                btn_state.setVisibility(View.GONE);
                tv_dialog_min_amount.setText(FormatUtil.formatAmount(mInfo.getCurrentInfo().getFromInvestmentAmount()));
                addUnit(tv_dialog_min_amount);
                tv_dialog_min_amount.setOnClickListener(null);
                et_input.setHint(new StringBuilder("输入").append(mInfo.getCurrentInfo().getUnitAmount()).append("的整数倍"));
                tv_dialog_max_amount_tip.setText("最大可认购金额");
                tv_dialog_max_amount.setText(FormatUtil.formatAmount(userMaxBuyAmount));
                addUnit(tv_dialog_max_amount); // 增加 元 单位符号
                tv_dialog_max_amount.setOnClickListener(null);
                // 限制输入长度
                et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInfo.getCurrentInfo().getResidueAmt().toString().length())});
                break;
            default:
                tv_begin_countdown.setVisibility(View.GONE);
                btn_state.setVisibility(View.VISIBLE);
                btn_state.setBackgroundColor(getResources().getColor(R.color.mq_b5_v2));
                btn_state.setText("已满额");
                break;
        }
    }

    /**
     * 已登录情况下 跳转到下个页面
     */
    @Override
    public void jumpToNextPageIfInputValid() {
        input = et_input.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(getBaseContext(), "认购金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        CurrentProjectInfo projectInfo = mInfo.getCurrentInfo();
        /**
         * 先判断该标的是否有限购
         * 1、有限购的话则 检查 当前用户对于该标的剩余认购额度 这个字段 是否为空,为空的话则去服务器请求 取得该字段的值
         * 2、无限购的话则 检查 当前用户对于这类产品的剩余认购额度 这个字段 是否为空,为空的话则去服务器请求 取得该字段的值
         **/
        if (projectInfo.getSubjectMaxBuy().compareTo(MAXBUYAMT) != 0) {
            if (projectInfo.getUserSubjectRemainAmt() == null
                    || projectInfo.getUserCurRemainAmt() == null) {
                refreshUserCurrentProjectInfo();
                return;
            }
        } else {
            if (projectInfo.getUserCurRemainAmt() == null) {
                refreshUserCurrentProjectInfo();
                return;
            }
        }

        /** 最低认购金额(左限额) **/
        BigDecimal leftLimit = projectInfo.getFromInvestmentAmount();
        /** 最高认购金额(右限额) **/
        BigDecimal rightLimit = userMaxBuyAmount;
        /** 用户输入的认购金额 **/
        BigDecimal inputAmount = new BigDecimal(input);
        /** 每份投资金额 **/
        BigDecimal perAmount = projectInfo.getUnitAmount();
        /** 用户认购金额 ％ 每份投资金额 **/
        BigDecimal remainder = inputAmount.remainder(perAmount);

        /** 用户认购额度小于起始金额 **/
        if (inputAmount.compareTo(leftLimit) == -1) {
            /** 用户认购额度 为当前可认购的最大金额时（此额度小于起始金额） 则可以让用户认购 **/
            if (inputAmount.compareTo(rightLimit) == 0) {
            } else {
                Toast.makeText(getBaseContext(), "提示：" + leftLimit + "元起投", Toast.LENGTH_SHORT).show();
            }
        }
        /** 用户认购额度大于最大可认购金额 **/
        else if (inputAmount.compareTo(rightLimit) == 1) {
            Toast.makeText(getBaseContext(), "提示：请输入小于等于" + rightLimit + "元", Toast.LENGTH_SHORT).show();
        }
        /** 用户认购额度是否为 每份金额 的整数倍 **/
        else if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            Toast.makeText(getBaseContext(), "提示：请输入" + perAmount + "的整数倍", Toast.LENGTH_SHORT).show();
        }
        /** 用户认购额度满足条件 进入下个页面 **/
        else {
            String interestRateString = total_profit_rate + "%";
            UserUtil.currenPay(mActivity, FormatUtil.getMoneyString(input), String.valueOf(prodId), subjectId, interestRateString);
            et_input.setText("");
        }
    }

    /**
     * 更新 当前用户 对于 该标的 最大可认购额度
     * 取该标的 最大可认购金额、剩余金额、用户限额 该类产品限额的最小值
     */
    private void refreshUserMaxBuyAmount() {
        if (null == mInfo) {
            return;
        }
        CurrentProjectInfo projectInfo = mInfo.getCurrentInfo();
        if (null == projectInfo) {
            return;
        }
        userMaxBuyAmount = getUpLimit(projectInfo.getSubjectMaxBuy(), projectInfo.getResidueAmt());
        userMaxBuyAmount = getUpLimit(userMaxBuyAmount, projectInfo.getUserSubjectRemainAmt());
        userMaxBuyAmount = getUpLimit(userMaxBuyAmount, projectInfo.getUserCurRemainAmt());
    }

    /**
     * 刷新 (已登录)用户在 该秒钱宝标的 下的 认购额度
     */
    public void refreshUserCurrentProjectInfo() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        HttpRequest.getCurrentDetail(mContext, subjectId, UserUtil.getUserId(CurrentDetailActivity.this), new ICallback<CurrentDetailResult>() {

            @Override
            public void onSucceed(CurrentDetailResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                if (null != result && null != result.getData()) {
                    mInfo = result.getData();
                    showContentView();
                    updateUI();
                    jumpToNextPageIfInputValid();
                }
                end();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                Uihelper.showToast(mContext, error);
            }
        });
    }

}
