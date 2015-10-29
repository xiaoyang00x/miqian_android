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
import com.miqian.mq.entity.CustPromotion;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterMyTicket extends RecyclerView.Adapter {

    private List<CustPromotion> promList;
    private int maxValue = 999;//最大的值
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;

    public AdapterMyTicket(List<CustPromotion> promList) {
        this.promList = promList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket, parent, false);
            viewHolder = new ViewHolderTicket(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if ((holder instanceof ViewHolderTicket)){
            CustPromotion promote = promList.get(position);
            ((ViewHolderTicket) holder).textMoney.setText("￥" + promote.getPromAmt());
            ((ViewHolderTicket) holder).textType.setText("拾财券 【抵用比例"+ promote.getPrnUsePerc() + "%】");
            ((ViewHolderTicket) holder).limitType.setText(promote.getLimitMsg());
            ((ViewHolderTicket) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            ((ViewHolderTicket) holder).promoteChoosed.setVisibility(View.GONE);

            String state = promote.getSta();
            ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.color.transparent);
            if (!TextUtils.isEmpty(state)) {

                ((ViewHolderTicket) holder).imageState.setVisibility(View.VISIBLE);
                if ("YW".equals(state)) {
                    ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.drawable.hb_used);
                    ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg_grey);
                } else if ("GQ".equals(state)) {
                    ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.drawable.hb_expired);
                    ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg_grey);
                } else {
                    ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.color.transparent);
                    ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg);
                }
            }else {
                ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.color.transparent);
                ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg);
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

    @Override
    public int getItemCount() {
        if (promList != null && promList.size() != 0) {
            return promList.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }
    public void setMaxItem(int value) {
        maxValue = value;
    }

    class ViewHolderTicket extends RecyclerView.ViewHolder {

        public TextView textType;
        public TextView limitType;
        public TextView limitDate;
        public TextView textMoney;
        public ImageView promoteChoosed;
        public ImageView imageState;

        public ViewHolderTicket(View itemView) {
            super(itemView);
            textType = (TextView) itemView.findViewById(R.id.text_type);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
            imageState = (ImageView) itemView.findViewById(R.id.image_state);
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
