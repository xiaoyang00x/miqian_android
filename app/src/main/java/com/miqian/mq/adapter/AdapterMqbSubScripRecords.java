package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.NewCurrentFundFlow;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by guolei_wang on 2017/7/12.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝资金记录
 */

public class AdapterMqbSubScripRecords extends RecyclerView.Adapter {

    List<NewCurrentFundFlow.NewCurrentRecords> list;
    private int operateType;

    private int maxValue = 999;//最大的值
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;

    public AdapterMqbSubScripRecords(List<NewCurrentFundFlow.NewCurrentRecords> list, int operateType) {
        this.list = list;
        this.operateType = operateType;
    }

    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return VIEW_FOOTER;
        } else {
            return VIEW_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mqb_subscription_records, parent, false);
            viewHolder = new AdapterMqbSubScripRecords.ItemVH(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new AdapterMqbSubScripRecords.ProgressViewHolder(view);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if ((vh instanceof AdapterMqbSubScripRecords.ItemVH)) {
            if (list.get(position) != null) {
                NewCurrentFundFlow.NewCurrentRecords item = list.get(position);
                ((AdapterMqbSubScripRecords.ItemVH) vh).tvMoney.setText("￥"+ FormatUtil.formatAmountStr(item.getAmt()));
                ((AdapterMqbSubScripRecords.ItemVH) vh).tvTraOpNm.setText(item.getOperateName());
                ((AdapterMqbSubScripRecords.ItemVH) vh).tvTime.setText(Uihelper.timestampToDateStr_other(item.getTraTime())+item.getRemark());

                switch (item.getStatus()) {
                    case NewCurrentFundFlow.NewCurrentRecords.STATUS_00:
                        ((ItemVH) vh).iv_state.setImageResource(R.drawable.icon_process);
                        break;
                    case NewCurrentFundFlow.NewCurrentRecords.STATUS_01:
                        if(20 == operateType) {
                            ((ItemVH) vh).iv_state.setImageResource(R.drawable.icon_subscription);
                        }else {
                            ((ItemVH) vh).iv_state.setImageResource(R.drawable.icon_redeem);
                        }
                        break;
                    default:
                        ((ItemVH) vh).iv_state.setImageResource(R.drawable.icon_xubiao);
                        break;
                }
            }
        } else if (vh instanceof AdapterMqbSubScripRecords.ProgressViewHolder) {
            if (position >= maxValue) {
                ((AdapterMqbSubScripRecords.ProgressViewHolder) vh).progressBar.setVisibility(View.GONE);
                if (maxValue <= 15) {
                    ((AdapterMqbSubScripRecords.ProgressViewHolder) vh).textLoading.setVisibility(View.GONE);
                } else {
                    ((AdapterMqbSubScripRecords.ProgressViewHolder) vh).textLoading.setText("没有更多");
                }
            } else {
                ((AdapterMqbSubScripRecords.ProgressViewHolder) vh).progressBar.setVisibility(View.VISIBLE);
                ((AdapterMqbSubScripRecords.ProgressViewHolder) vh).textLoading.setText("加载更多");
            }
        }


    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() != 0) {
            return list.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }

    public static class ItemVH extends RecyclerView.ViewHolder {
        TextView tvTraOpNm, tvMoney, tvTime;
        ImageView iv_state;

        public ItemVH(View v) {
            super(v);
            iv_state = (ImageView) v.findViewById(R.id.iv_state);
            tvTraOpNm = (TextView) v.findViewById(R.id.text_type);
            tvMoney = (TextView) v.findViewById(R.id.text_amt);
            tvTime = (TextView) v.findViewById(R.id.tv_time);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textLoading;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            textLoading = (TextView) view.findViewById(R.id.text_loading);
        }
    }

}
