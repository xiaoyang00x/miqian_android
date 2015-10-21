package com.miqian.mq.adapter.typeadapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CurSubRecord;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterCurrrentRecord extends RecyclerView.Adapter {

    private List<CurSubRecord> dataList;

    private int maxValue = 999;//最大的值
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;


    public AdapterCurrrentRecord(List<CurSubRecord> dataList) {
        this.dataList = dataList;
    }

    @Override
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_record, parent, false);
            viewHolder = new ViewHolderRecord(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }
    public void setMaxItem(int value) {
        maxValue = value;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderRecord){
            CurSubRecord data = dataList.get(position);
            String traCd = data.getTraCd();

            if (!TextUtils.isEmpty(traCd)) {

                switch (traCd) {
                    case "SR01"://认购交易
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_subscribe);
                        ((ViewHolderRecord) holder).textType.setText(data.getType());
                        ((ViewHolderRecord) holder).tvAmt.setText("-" + data.getAmt());
                        ((ViewHolderRecord) holder).tvInterest.setText("金额(元)");

                        break;
                    case "SS01"://赎回交易
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_redem);
                        ((ViewHolderRecord) holder).textType.setText(data.getType());
                        ((ViewHolderRecord) holder).tvAmt.setText("+" + data.getAmt());
                        ((ViewHolderRecord) holder).tvInterest.setText("利息" + data.getInterest() + "元");
                        break;
                    case "SZ01"://转让交易
                        ((ViewHolderRecord) holder).textType.setText(data.getType());
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_transfer);
                        ((ViewHolderRecord) holder).tvAmt.setText("+" + data.getAmt());
                        ((ViewHolderRecord) holder).tvInterest.setText("利息" + data.getInterest() + "元");
                        break;
                    default:
                        ((ViewHolderRecord) holder).textType.setText("");
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.color.transparent);
                        break;
                }

            } else {//空为活期认购
                ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_subscribe);
                ((ViewHolderRecord) holder).textType.setText(data.getType());
                ((ViewHolderRecord) holder).tvAmt.setText("-" + data.getAmt());
                ((ViewHolderRecord) holder).tvInterest.setText("金额(元)");

            }
            if (!TextUtils.isEmpty(data.getCrtDt())) {
                ((ViewHolderRecord) holder).tvTime.setText(data.getCrtDt());
            } else {
                ((ViewHolderRecord) holder).tvTime.setText("--");
            }
        }
        else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <=15) {
                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
            } else {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        }

    }
    class ViewHolderRecord extends RecyclerView.ViewHolder {

        public TextView textType;
        public TextView tvTime;
        public TextView tvAmt;
        public TextView tvInterest;
        public ImageView ivState;

        public ViewHolderRecord(View itemView) {
            super(itemView);
            textType = (TextView) itemView.findViewById(R.id.text_type);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAmt = (TextView) itemView.findViewById(R.id.text_amt);
            tvInterest = (TextView) itemView.findViewById(R.id.tv_interest);
            ivState = (ImageView) itemView.findViewById(R.id.iv_state);
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
