package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularListActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.user.MyTicketInvalidActivity;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterMyTicket extends RecyclerView.Adapter {

    private List<Promote> promList;
    private int maxValue = 999;//最大的值

    private final int VIEW_FOOTER = 0;
    private static final int VIEW_TYPE_HB = 1;
    private static final int VIEW_TYPE_SC = 2;
    private static final int VIEW_TYPE_FXQ = 3;

    private Context mContext;

    public AdapterMyTicket(Context context, List<Promote> promList) {
        this.promList = promList;
        this.mContext = context;
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
            } else if (promote.getType().equals("FXQ")) {
                return VIEW_TYPE_FXQ;
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
            case VIEW_TYPE_FXQ:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket, parent, false);
                return new ViewHolderShare(view);
            case VIEW_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressViewHolder(view);
        }
    }


    private void setView(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(text);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderTicket) {
            Promote promote = promList.get(position);
            ((ViewHolderTicket) holder).textMoney.setText("" + promote.getTotalAmt());
            ((ViewHolderTicket) holder).textName.setText(promote.getPromProdName());
            ((ViewHolderTicket) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
            setView(((ViewHolderTicket) holder).limitMoney, promote.getFitBdTermOrYrt());
            ((ViewHolderTicket) holder).limitType.setText(promote.getFitProdOrBdType());
            ((ViewHolderTicket) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            toUseTicket(holder, promote.getPromProdId());
        } else if (holder instanceof ViewHolderShare) {
            final Promote promote = promList.get(position);
            ((ViewHolderShare) holder).textMoney.setText("" + promote.getTotalAmt());
            ((ViewHolderShare) holder).textName.setText(promote.getPromProdName());
            ((ViewHolderShare) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
            setView(((ViewHolderShare) holder).limitMoney, promote.getFitBdTermOrYrt());
            ((ViewHolderShare) holder).limitType.setText(promote.getFitProdOrBdType());
            ((ViewHolderShare) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            ((ViewHolderShare) holder).frameTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(mContext, promote.getShareUrl());
                }
            });
//          分享券 这个不跳红包使用界面
//            toUseTicket(holder, promote.getPromProdId());
        } else if (holder instanceof ViewHolderPackage) {
            Promote promote = promList.get(position);
            ((ViewHolderPackage) holder).textMoney.setText("" + promote.getTotalAmt());
            ((ViewHolderPackage) holder).textName.setText(promote.getPromProdName());
            ((ViewHolderPackage) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
            setView(((ViewHolderPackage) holder).limitMoney, promote.getFitBdTermOrYrt());
            ((ViewHolderPackage) holder).limitType.setText(promote.getFitProdOrBdType());
            ((ViewHolderPackage) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            toUseTicket(holder, promote.getPromProdId());
        } else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
//                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
//                if (maxValue <= 5) {
//                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
//                } else {
//                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
//                }
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.GONE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(View.VISIBLE);
            } else {
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(View.GONE);
//                ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
//                ((ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        }


    }

    private void toUseTicket(RecyclerView.ViewHolder holder, final String promProdId) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegularListActivity.startActivity(mContext, promProdId);
            }
        });
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

        public ViewHolderTicket(View itemView) {
            super(itemView);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            textPercent = (TextView) itemView.findViewById(R.id.text_percent);
            limitMoney = (TextView) itemView.findViewById(R.id.limit_money);
        }
    }

    class ViewHolderShare extends RecyclerView.ViewHolder {

        public TextView textMoney;
        private TextView textName;
        public TextView textPercent;
        public TextView limitMoney;
        public TextView limitType;
        public TextView limitDate;
        private RelativeLayout frameTicket;


        public ViewHolderShare(View itemView) {
            super(itemView);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            textPercent = (TextView) itemView.findViewById(R.id.text_percent);
            limitMoney = (TextView) itemView.findViewById(R.id.limit_money);
            frameTicket = (RelativeLayout) itemView.findViewById(R.id.frame_ticket);
            frameTicket.setBackgroundResource(R.drawable.ticket_share_bg);
        }
    }

    class ViewHolderPackage extends RecyclerView.ViewHolder {

        public TextView textMoney;
        private TextView textName;
        public TextView textPercent;
        public TextView limitMoney;
        public TextView limitType;
        public TextView limitDate;

        public ViewHolderPackage(View itemView) {
            super(itemView);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            textPercent = (TextView) itemView.findViewById(R.id.text_percent);
            limitMoney = (TextView) itemView.findViewById(R.id.limit_money);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout frameLoad;
        private LinearLayout frameNone;
        private Button btOverdue;

        public ProgressViewHolder(View view) {
            super(view);
            frameLoad = (LinearLayout) view.findViewById(R.id.frame_load);
            frameNone = (LinearLayout) view.findViewById(R.id.frame_none);
            btOverdue = (Button) view.findViewById(R.id.bt_overdue);
            btOverdue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyTicketInvalidActivity.startActivity(mContext);
                }
            });
        }
    }
}
