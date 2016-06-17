package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularDetailActivity;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RegularBaseData> mList = new ArrayList<>();
    private Context mContext;

    public RegularListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void add(RegularBaseData info) {
        mList.add(info);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RegularListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regular_content, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularListHolder) {
            ((RegularListHolder) holder).bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class RegularListHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name; // 名称
        private TextView tv_profit_rate; // 年利率
        private TextView tv_profit_rate_unit; // 年利率单位:%, 有加息的话如:+0.5%
        private TextView tv_time_limit; // 期限
        private TextView tv_time_limit_unit; // 期限单位:元
        private TextView tv_begin_time; // 开始时间
        private TextView tv_remain_amount; // 剩余可购金额
        private Button btn_state; // 立即购买(已售罄)按钮
        private View divider;

        public RegularListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_profit_rate_unit = (TextView) itemView.findViewById(R.id.tv_profit_rate_unit);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            tv_time_limit_unit = (TextView) itemView.findViewById(R.id.tv_time_limit_unit);
            tv_begin_time = (TextView) itemView.findViewById(R.id.tv_begin_time);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bindData(final int position) {
            final RegularBaseData info = mList.get(position);
            tv_name.setText(info.getSubjectName());
            tv_profit_rate.setText(info.getYearInterest());
            tv_profit_rate_unit.setText("%");
            if (1 == info.getPresentationYesNo()) {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(info.getPresentationYearInterest()).
                                append("%"));
            }
            tv_time_limit.setText(info.getLimit());
            tv_remain_amount.setText(
                    new StringBuilder("可认购金额:￥").
                            append(FormatUtil.formatAmount(info.getResidueAmt())).
                            append("/￥").
                            append(FormatUtil.formatAmount(info.getSubjectTotalPrice())));
            divider.setVisibility(position + 1 == getItemCount() ? View.GONE : View.VISIBLE);
            switch (info.getSubjectStatus()) {
                case RegularBase.STATE_00:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_begin_time.setText(Uihelper.timeToDateRegular(info.getStartTimestamp()));
                    tv_begin_time.setVisibility(View.VISIBLE);
                    btn_state.setBackgroundResource(R.drawable.btn_no_begin);
                    btn_state.setText("待开标");
                    break;
                case RegularBase.STATE_01:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_begin_time.setVisibility(View.GONE);
                    btn_state.setBackgroundResource(R.drawable.btn_default_selector);
                    btn_state.setText("立即认购");
                    break;
                default:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_begin_time.setVisibility(View.GONE);
                    btn_state.setBackgroundResource(R.drawable.btn_has_done);
                    btn_state.setText("已满额");
                    break;
            }
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegularDetailActivity.startActivity(
                            mContext, info.getSubjectId(), info.getProdId());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegularDetailActivity.startActivity(
                            mContext, info.getSubjectId(), info.getProdId());
                }
            });
        }

    }


}
