package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CapitalItem;

import java.util.List;

/**
 * Created by sunyong on 9/23/15.
 */
public class CapitalRecordAdapter extends RecyclerView.Adapter {
    List<CapitalItem> list;

    public CapitalRecordAdapter(List<CapitalItem> list) {
        this.list = list;

    }

    private int maxValue = 999;//最大的值
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_capital_record_row, parent, false);
            viewHolder = new ItemVH(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if ((vh instanceof ItemVH)){
            if (list.get(position) != null) {
                CapitalItem item = list.get(position);
                ((ItemVH) vh).tvMoney.setText(item.getTraAmt());
                ((ItemVH) vh).tvPeerCustLoginNm.setText(item.getPeerCustLoginNm());
                ((ItemVH) vh).tvTraOpNm.setText(item.getTraOpNm());
                ((ItemVH) vh).tvTime.setText(item.getTraDt() + " " + item.getTraTm());
                ((ItemVH) vh).tvTime.setText(item.getTraDt() + " " + item.getTraTm() + "(" + item.getRem() + ")");
            }
        } else if (vh instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) vh).progressBar.setVisibility(View.GONE);
                if (maxValue <=15) {
                    ((ProgressViewHolder) vh).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) vh).textLoading.setText("没有更多");
                }
            } else {
                ((ProgressViewHolder) vh).progressBar.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) vh).textLoading.setText("加载更多");
            }
        }



    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }

    public static class ItemVH extends RecyclerView.ViewHolder {
        TextView tvTraOpNm, tvPeerCustLoginNm, tvMoney, tvTime;

        public ItemVH(View v) {
            super(v);
            tvTraOpNm = (TextView) v.findViewById(R.id.tv_traOpNm);
            tvPeerCustLoginNm = (TextView) v.findViewById(R.id.tv_peerCustLoginNm);
            tvMoney = (TextView) v.findViewById(R.id.money);
            tvTime = (TextView) v.findViewById(R.id.time_rem);
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
