package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularDetailActivity;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期转让列表数据适配器
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularTransferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_NORMAL = 0; //
    private final int ITEM_TYPE_FOOTER = 1; // 加载更多

    private ArrayList<RegularProjectInfo> mList;
    private Context mContext;
    private int totalCount; // 定期转让总量

    public RegularTransferAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void addAll(ArrayList<RegularProjectInfo> list) {
        mList.addAll(list);
    }

    public void clear() {
        mList.clear();
    }

    // 列表总条目数
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    private int size() {
        return (mList == null || mList.size() <= 0) ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_NORMAL:
                return new RegularTransferHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regular_content, parent, false));
            case ITEM_TYPE_FOOTER:
                return new ProgressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularTransferHolder) {
            RegularTransferHolder viewHolder = (RegularTransferHolder) holder;
            viewHolder.bindData(position);
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.showOrHide();
        }
    }

    @Override
    public int getItemCount() {
        return size() == 0 ? 0 : size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    private class RegularTransferHolder extends RecyclerView.ViewHolder {

        private TextView tv_name; // 标题
        private TextView tv_profit_rate; // 年化收益
        private TextView tv_profit_rate_unit; // 年利率单位:%, 有加息的话如:+0.5%
        private TextView tv_time_limit; // 项目期限
        private TextView tv_time_limit_unit; // 项目期限单位:天
        private TextView tv_remain_amount; // 剩余金额
        private Button btn_state; // 立即购买(已售罄)按钮
        private View divider;

        public RegularTransferHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_profit_rate_unit = (TextView) itemView.findViewById(R.id.tv_profit_rate_unit);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            tv_time_limit_unit = (TextView) itemView.findViewById(R.id.tv_time_limit_unit);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bindData(final int position) {
            final RegularProjectInfo info = mList.get(position);

            tv_name.setText(info.getSubjectName());
            tv_profit_rate.setText(info.getPredictRate());
            tv_time_limit.setText(info.getLimit());
            tv_remain_amount.setText(
                    new StringBuilder("可认购本金:￥").
                            append(FormatUtil.formatAmount(info.getResidueAmt())));

            switch (info.getSubjectStatus()) {
                case RegularBase.STATE_01:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    btn_state.setBackgroundResource(R.drawable.btn_default_selector);
                    btn_state.setText("立即认购");
                    break;
                default:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
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

            divider.setVisibility(
                    position + 2 == getTotalCount() ? View.GONE : View.VISIBLE);
        }

    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout frameLoad;

        public ProgressViewHolder(View view) {
            super(view);
            frameLoad = (LinearLayout) view.findViewById(R.id.frame_load);
        }

        public void showOrHide() {
            frameLoad.setVisibility(
                    size() >= totalCount ? View.GONE : View.VISIBLE);
        }

    }

}
