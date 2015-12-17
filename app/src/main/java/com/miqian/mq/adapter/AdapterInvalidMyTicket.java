package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.user.MyTicketInvalidActivity;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.utils.Uihelper;
import com.yintong.secure.widget.Progress;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterInvalidMyTicket extends RecyclerView.Adapter {

    private List<Promote> promList;
    private int maxValue = 999;//最大的值

    private static final int VIEW_TYPE_HB = 0;
    private static final int VIEW_TYPE_SC = 1;
    private final int VIEW_FOOTER = 2;

    private Context mContext;

    public AdapterInvalidMyTicket(Context context, List<Promote> promList) {
        mContext = context;
        this.promList = promList;
    }

    //促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return VIEW_FOOTER;
        } else {
            Promote promote = promList.get(position);
            if (promote.getType().equals("HB")) {
                return VIEW_TYPE_HB;
            } else if (promote.getType().equals("SC")) {
                return VIEW_TYPE_SC;
            }
            return VIEW_TYPE_HB;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_HB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_red_packet, parent, false);
                return new ViewHolderPackage(view);
            case VIEW_TYPE_SC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket, parent, false);
                return new ViewHolderTicket(view);
            case VIEW_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderTicket) {
            Promote promote = promList.get(position);
            ((ViewHolderTicket) holder).textMoney.setText("" + promote.getTotalAmt());
            ((ViewHolderTicket) holder).textName.setText(promote.getPromProdName());
            ((ViewHolderTicket) holder).textPercent.setText("限定使用比例" + promote.getToUseRate() + "%");
            ((ViewHolderTicket) holder).limitMoney.setText(promote.getMinBuyAmtMsg());
            ((ViewHolderTicket) holder).limitType.setText(promote.getLimitMsg());
            ((ViewHolderTicket) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
        } else if (holder instanceof ViewHolderPackage) {
            Promote promote = promList.get(position);
            ((ViewHolderPackage) holder).textMoney.setText("" + promote.getTotalAmt());
            ((ViewHolderPackage) holder).textName.setText(promote.getPromProdName());
            ((ViewHolderPackage) holder).textPercent.setText("限定使用比例" + promote.getToUseRate() + "%");
            ((ViewHolderPackage) holder).limitMoney.setText(promote.getMinBuyAmtMsg());
            ((ViewHolderPackage) holder).limitType.setText(promote.getLimitMsg());
            ((ViewHolderPackage) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));

        } else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 5) {
                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
//                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.GONE);
//                ((ProgressViewHolder) holder).frameNone.setVisibility(View.VISIBLE);
            } else {
//                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.VISIBLE);
//                ((ProgressViewHolder) holder).frameNone.setVisibility(View.GONE);
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

        public TextView textMoney;
        private TextView textName;
        public TextView textPercent;
        public TextView limitMoney;
        public TextView limitType;
        public TextView limitDate;
        public RelativeLayout frameTicket;

        public ViewHolderTicket(View itemView) {
            super(itemView);
            frameTicket = (RelativeLayout) itemView.findViewById(R.id.frame_ticket);
            frameTicket.setBackgroundResource(R.drawable.ticket_bg_invalid);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            textPercent = (TextView) itemView.findViewById(R.id.text_percent);
            limitMoney = (TextView) itemView.findViewById(R.id.limit_money);

            limitType.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            limitDate.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textName.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textMoney.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textPercent.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            limitMoney.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
        }
    }

    class ViewHolderPackage extends RecyclerView.ViewHolder {

        private TextView textMoney;
        private TextView textName;
        private TextView textPercent;
        private TextView limitMoney;
        private TextView limitType;
        private TextView limitDate;
        private RelativeLayout framePackage;

        public ViewHolderPackage(View itemView) {
            super(itemView);
            framePackage = (RelativeLayout) itemView.findViewById(R.id.frame_package);
            framePackage.setBackgroundResource(R.drawable.package_bg_invalid);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            textPercent = (TextView) itemView.findViewById(R.id.text_percent);
            limitMoney = (TextView) itemView.findViewById(R.id.limit_money);

            limitType.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            limitDate.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textName.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textMoney.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            textPercent.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            limitMoney.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private TextView textLoading;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            textLoading = (TextView) view.findViewById(R.id.text_loading);
        }
    }
}
