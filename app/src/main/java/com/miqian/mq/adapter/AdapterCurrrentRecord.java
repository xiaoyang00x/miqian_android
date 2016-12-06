package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.FundFlow;
import com.miqian.mq.utils.FormatUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterCurrrentRecord extends RecyclerView.Adapter {

    private List<FundFlow> dataList;

    private int maxValue = 999;//最大的值
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_FOOTER = 2;

    public AdapterCurrrentRecord(List<FundFlow> dataList) {
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
        if (dataList != null && dataList.size() != 0) {
            return dataList.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderRecord) {
            FundFlow data = dataList.get(position);
            String traCd = data.getOperateBillType();

            if (!TextUtils.isEmpty(traCd)) {

                ((ViewHolderRecord) holder).textType.setText(data.getOperateName());
                ((ViewHolderRecord) holder).tvAmt.setText(data.getCapitalAmt().toString());
                if(data.getInterestAmt().compareTo(BigDecimal.ZERO) == 0) {
                    ((ViewHolderRecord) holder).tvInterest.setText("金额(元)");
                }else {
                    ((ViewHolderRecord) holder).tvInterest.setText("利息" + data.getInterestAmt() + "元");
                }

                switch (traCd) {
                    case FundFlow.BILL_TYPE_RG://认购交易
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_subscribe);
                        break;
                    case FundFlow.BILL_TYPE_SH://赎回交易
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_redem);
                        break;
                    case FundFlow.BILL_TYPE_ZR://转让交易
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_zr);
                        break;
                    case FundFlow.BILL_TYPE_TX://提现
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_tx);
                        break;
                    case FundFlow.BILL_TYPE_HK://到期还款
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_hk);
                        break;
                    case FundFlow.BILL_TYPE_CZ://充值
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_cz);
                        break;
                    default:
                        ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_other);
                        break;
                }

            }
            ((ViewHolderRecord) holder).tvTime.setText(FormatUtil.formatDate(data.getOperateTIme(), "yyyy-MM-dd HH:mm:ss"));
        } else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 15) {
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
