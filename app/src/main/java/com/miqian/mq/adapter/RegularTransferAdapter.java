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
import com.miqian.mq.entity.RegularTransferInfo;

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

    private ArrayList<RegularTransferInfo> mList = new ArrayList<>();
    private Context mContext;
    private int totalCount; // 定期转让总量

    public RegularTransferAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void addAll(ArrayList<RegularTransferInfo> list) {
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
        return size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private class RegularTransferHolder extends RecyclerView.ViewHolder {

        private TextView tv_title; // 标题
        private TextView profit_rate; // 年化收益
        private TextView tv_time_limit; // 项目期限
        private Button btn_state; // 立即购买(已售罄)按钮
        private TextView tv_remain_amount; // 剩余金额
        private TextView tv_total_amount; // 项目期限

        public RegularTransferHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            profit_rate = (TextView) itemView.findViewById(R.id.profit_rate);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            tv_total_amount = (TextView) itemView.findViewById(R.id.tv_total_amount);
        }

        public void bindData(final int position) {
            RegularTransferInfo info = mList.get(position);
            tv_title.setText(info.getSubjectName());
            profit_rate.setText(info.getPredictRate());
            tv_time_limit.setText(info.getLimit());
            tv_remain_amount.setText("剩余金额:" + info.getResidueAmt());
            tv_total_amount.setText("原始债券总额:" + info.getSubjectTotalPrice());
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout frameLoad;

        public ProgressViewHolder(View view) {
            super(view);
            frameLoad = (LinearLayout) view.findViewById(R.id.frame_load);
        }

        public void showOrHide() {
            if (frameLoad.getVisibility() == View.VISIBLE && size() >= totalCount) {
                frameLoad.setVisibility(View.GONE);
            } else if (frameLoad.getVisibility() == View.GONE && size() < totalCount) {
                frameLoad.setVisibility(View.VISIBLE);
            }
        }

    }

}
