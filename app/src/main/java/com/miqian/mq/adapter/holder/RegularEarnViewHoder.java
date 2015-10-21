package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.miqian.mq.R;
import com.miqian.mq.activity.RegularEarnActivity;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.utils.FormatUtil;

/**
 * Created by guolei_wang on 15/10/13.
 */
public class RegularEarnViewHoder extends RecyclerView.ViewHolder {
    private View layout_regular_earn_head;
    private View layout_item;
    private TextView tv_lable_name;
    private TextView tv_invest_security;
    private TextView tv_title;
    private TextView tv_sub_title;
    private TextView tv_annurate_interest_rate;
    private TextView tv_duration;
    private TextView tv_progress;
    private TextView tv_buy_now;
    private TextView tv_sale_number;
    private TextView tv_description;
    private TextView tv_add_interest;
    private DonutProgress circlebar;
    private View divider_view;

    public RegularEarnViewHoder(View itemView) {
        super(itemView);

        layout_item = itemView.findViewById(R.id.layout_item);
        layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
        tv_lable_name = (TextView) itemView.findViewById(R.id.tv_lable_name);
        tv_invest_security = (TextView) itemView.findViewById(R.id.tv_invest_security);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        tv_sub_title = (TextView) itemView.findViewById(R.id.tv_sub_title);
        tv_annurate_interest_rate = (TextView) itemView.findViewById(R.id.tv_annurate_interest_rate);
        tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
        tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);
        tv_sale_number = (TextView) itemView.findViewById(R.id.tv_sale_number);
        tv_buy_now = (TextView) itemView.findViewById(R.id.tv_buy_now);
        tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        tv_add_interest = (TextView) itemView.findViewById(R.id.tv_add_interest);
        circlebar = (DonutProgress) itemView.findViewById(R.id.circlebar);
        divider_view = itemView.findViewById(R.id.divider_view);

        tv_lable_name.setText(R.string.regular_earn);
        tv_invest_security.setText(R.string.principal_interest_security);
    }

    /**
     * 设置 lable
     * @param name
     */
    public void setLableName(String name) {
        tv_lable_name.setText(name);
    }

    /**
     * 是否显示 divider
     * @param show
     */
    public void showDiverView(boolean show) {
        divider_view.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    public void bindView(final Context mContext, final RegularEarn regularEarn, boolean showLable) {

        tv_title.setText(regularEarn.getSubjectName());
        tv_sub_title.setText("项目总额" +  FormatUtil.formatAmount(regularEarn.getSubjectTotalPrice()) + "  " + regularEarn.getPayMode());

        tv_duration.setText(regularEarn.getLimit() + "天");
        circlebar.setProgress((new Float(regularEarn.getPurchasePercent()).intValue()));
        circlebar.setProgress((new Float(regularEarn.getPurchasePercent()).intValue()));


        //待开标
        if("00".equals(regularEarn.getSubjectStatus())) {
            tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_sale_number.setVisibility(View.GONE);
            tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest_unbegin);

            tv_progress.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "MM月dd日"));
            tv_buy_now.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "hh:mm"));
        }else {
            tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_b5));
            tv_sale_number.setVisibility(View.VISIBLE);
            tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);

            tv_buy_now.setText(R.string.buy_now);
            tv_progress.setText(regularEarn.getPurchasePercent() + "%");
            tv_sale_number.setText("已认购" + regularEarn.getPersonTime() + "人");
        }
        tv_annurate_interest_rate.setText(regularEarn.getYearInterest() + "%");
        if(TextUtils.isEmpty(regularEarn.getPromotionDesc())) {
            tv_description.setVisibility(View.GONE);
        }else {
            tv_description.setVisibility(View.VISIBLE);
            tv_description.setText(regularEarn.getPromotionDesc());
        }

        if("Y".equalsIgnoreCase(regularEarn.getPresentationYesNo())) {
            tv_add_interest.setVisibility(View.VISIBLE);
            tv_add_interest.setText(regularEarn.getPresentationYearInterest());
        }else {
            tv_add_interest.setVisibility(View.GONE);
        }

        if (!showLable) {
            layout_regular_earn_head.setVisibility(View.GONE);
        } else {
            layout_regular_earn_head.setVisibility(View.VISIBLE);
        }

        layout_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RegularEarnActivity.startActivity(mContext, regularEarn.getSubjectId());
            }
        });
    }
}
