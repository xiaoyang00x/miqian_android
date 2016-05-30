package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularEarnActivity;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.HomeSelectionProject;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.utils.FormatUtil;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页精选项目
 */
public class HomeSelectionHolder extends HomeBaseViewHolder {

    private TextView tv_lable;
    private LinearLayout layout_container;
    private LayoutInflater inflater;
    private View divider;
    private Context mContext;

    public HomeSelectionHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        inflater = LayoutInflater.from(itemView.getContext());
        tv_lable = (TextView)itemView.findViewById(R.id.tv_lable);
        divider = itemView.findViewById(R.id.divider);
        layout_container = (LinearLayout) itemView.findViewById(R.id.layout_container);

        divider.setVisibility(View.GONE);

    }

    @Override
    public void bindView(HomePageInfo mData) {
        layout_container.removeAllViews();
        if(mData != null && mData.getSubjectInfoData() != null) {
            if(TextUtils.isEmpty(mData.getTitle())) {
                tv_lable.setVisibility(View.GONE);
            }else {
                tv_lable.setText(mData.getTitle());
                tv_lable.setVisibility(View.VISIBLE);
            }

            if(mData.getSubjectInfoData().size() > 0) {
                for(int i = 0; i < mData.getSubjectInfoData().size(); i++) {
                    layout_container.addView(initProjectView(mData.getSubjectInfoData().get(i)));
                }
            }
        }
    }

    /**
     * 初始化项目控件
     * @param data
     * @return
     */
    private View initProjectView(final HomeSelectionProject data) {
        View projectView = inflater.inflate(R.layout.item_home_project, null);
        //项目名称
        TextView tv_project_name = (TextView)projectView.findViewById(R.id.tv_project_name);
        //年化收益
        TextView profit_rate = (TextView)projectView.findViewById(R.id.profit_rate);
        //加息 %
        TextView tv_add_interest = (TextView)projectView.findViewById(R.id.tv_add_interest);
        //项目期限
        TextView tv_time_limit = (TextView)projectView.findViewById(R.id.tv_time_limit);
        //项目角标
        TextView tv_corner_mark = (TextView)projectView.findViewById(R.id.tv_corner_mark);
        //剩余金额/总金额
        TextView tv_remain_amount = (TextView)projectView.findViewById(R.id.tv_remain_amount);
        //开始时间
        TextView tv_begin_time = (TextView)projectView.findViewById(R.id.tv_begin_time);
        //天
        TextView tv_day = (TextView)projectView.findViewById(R.id.tv_day);
        //btn_buy
        Button btn_buy = (Button) projectView.findViewById(R.id.btn_buy);

        tv_project_name.setText(data.getSubjectName());
        profit_rate.setText(data.getYearInterest());
        if("1".equals(data.getPresentationYesNo())) {
            tv_add_interest.setText("+ " + data.getPresentationYearInterest() + "%");
        }else {
            tv_add_interest.setText("%");
        }
        tv_time_limit.setText(data.getLimit());
        if(TextUtils.isEmpty(data.getSubscript())) {
            tv_corner_mark.setVisibility(View.INVISIBLE);
        }else {
            tv_corner_mark.setText(data.getSubscript());
            tv_corner_mark.setVisibility(View.VISIBLE);
        }
        tv_remain_amount.setText("剩余金额:￥"+ FormatUtil.formatAmount(data.getResidueAmt())+"/" + FormatUtil.formatAmount(data.getSubjectTotalPrice()));

        //待开标
        if ("00".equals(data.getSubjectStatus())) {
            profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_bl3_v2));
            tv_add_interest.setTextColor(mContext.getResources().getColor(R.color.mq_bl3_v2));
            tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_bl3_v2));
            tv_day.setTextColor(mContext.getResources().getColor(R.color.mq_bl3_v2));
            btn_buy.setBackgroundResource(R.drawable.btn_no_begin);
            btn_buy.setText("待开标");
            btn_buy.setEnabled(true);

            tv_begin_time.setText(FormatUtil.formatDate(data.getStartTimestamp(), "MM月dd日 HH:mm发售"));
            tv_begin_time.setVisibility(View.VISIBLE);
        } else if ("01".equals(data.getSubjectStatus())) {
            profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
            tv_add_interest.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
            tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
            tv_day.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
            btn_buy.setBackgroundResource(R.drawable.btn_default_selector);
            btn_buy.setText(R.string.buy_now);
            btn_buy.setEnabled(true);

            tv_begin_time.setVisibility(View.GONE);

        } else {
            profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
            tv_add_interest.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
            tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
            tv_day.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
            btn_buy.setBackgroundResource(R.drawable.btn_has_done);
            btn_buy.setText("已满额");
            btn_buy.setEnabled(false);

            tv_begin_time.setVisibility(View.GONE);
        }

        btn_buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(data.getJumpProjectUrl())) {
                    if (RegularBaseData.PRODID_REGULAR_PLAN.equals(data.getProdId())) {
                        RegularPlanActivity.startActivity(view.getContext(), data.getSubjectId());
                    } else {
                        RegularEarnActivity.startActivity(view.getContext(), data.getSubjectId());
                    }
                }else {
                    WebActivity.startActivity(view.getContext(), data.getJumpProjectUrl());
                }
            }
        });
        return projectView;
    }
}
