package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_LIST = 1;

    private Context mContext;

    public AdapterMyTicket(Context context, List<Promote> promList) {
        this.promList = promList;
        this.mContext = context;
    }

    //促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? VIEW_TYPE_FOOTER : VIEW_TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_temp, parent, false);
                return new BaseViewHoleder(view);
            case VIEW_TYPE_FOOTER:
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
        if (holder instanceof BaseViewHoleder) {
            BaseViewHoleder tempViewHoleder = (BaseViewHoleder) holder;
            final Promote promote = promList.get(position);
            tempViewHoleder.tv_name.setText(promote.getPromProdName());
            tempViewHoleder.tv_validate_date.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            tempViewHoleder.tv_percent_limit.setText(promote.getMinBuyAmtOrPerc());
            setView(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            tempViewHoleder.tv_use_limit.setText(promote.getLimitMsg());
            tempViewHoleder.tv_amount.setText(String.valueOf(promote.getCanUseAmt()));
            if (Promote.TYPE.JX.getValue().equals(promote.getType())) { // 加息券
                tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_quan);
                tempViewHoleder.tv_amount_unit.setVisibility(View.GONE);
                tempViewHoleder.tv_amount.setText("+" + promote.getGiveYrt());
            } else {
                tempViewHoleder.tv_amount_unit.setVisibility(View.VISIBLE);
                if (Promote.TYPE.SC.getValue().equals(promote.getType())) { // 拾财券 可点击跳转
                    tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_miaoqian);
                    toUseTicket(holder, promote.getPromProdId());
                } else if (Promote.TYPE.HB.getValue().equals(promote.getType())) { // 红包 可点击跳转
                    tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_hongbao);
                    toUseTicket(holder, promote.getPromProdId());
                } else if (Promote.TYPE.TY.getValue().equals(promote.getType())) { // 体验金
                    tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_tiyanjin);
                    toUseTicket(holder, promote.getPromProdId());
                } else if (Promote.TYPE.FXQ.getValue().equals(promote.getType())) { // 分享券
                    tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_fenxiang);
                    toUseTicket(holder, promote.getPromProdId());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebActivity.startActivity(mContext, promote.getShareUrl());
                        }
                    });
                } else if (Promote.TYPE.DK.getValue().equals(promote.getType())) { // 抵扣券
                    tempViewHoleder.iv_icon.setImageResource(R.drawable.ticket_icon_diyong);
                    toUseTicket(holder, promote.getPromProdId());
                } else if (Promote.TYPE.JF.getValue().equals(promote.getType())) { // 积分 暂时没有
                } else if (Promote.TYPE.LP.getValue().equals(promote.getType())) { // 礼品卡 暂时没有
                }
            }
        }
//        if (holder instanceof ViewHolderTicket) { // 拾财券
//            Promote promote = promList.get(position);
//            ((ViewHolderTicket) holder).textMoney.setText("" + promote.getCanUseAmt());
//            ((ViewHolderTicket) holder).textName.setText(promote.getPromProdName());
//            ((ViewHolderTicket) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
//            setView(((ViewHolderTicket) holder).limitMoney, promote.getFitBdTermOrYrt());
//            ((ViewHolderTicket) holder).limitType.setText(promote.getFitProdOrBdType());
//            ((ViewHolderTicket) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
//            toUseTicket(holder, promote.getPromProdId());
//        } else if (holder instanceof ViewHolderShare) {
//            final Promote promote = promList.get(position);
//            ((ViewHolderShare) holder).textMoney.setText("" + promote.getCanUseAmt());
//            ((ViewHolderShare) holder).textName.setText(promote.getPromProdName());
//            ((ViewHolderShare) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
//            setView(((ViewHolderShare) holder).limitMoney, promote.getFitBdTermOrYrt());
//            ((ViewHolderShare) holder).limitType.setText(promote.getFitProdOrBdType());
//            ((ViewHolderShare) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
//            ((ViewHolderShare) holder).frameTicket.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    WebActivity.startActivity(mContext, promote.getShareUrl());
//                }
//            });
////          分享券 这个不跳红包使用界面
////            toUseTicket(holder, promote.getPromProdId());
//        } else if (holder instanceof ViewHolderPackage) { // 红包
//            Promote promote = promList.get(position);
//            ((ViewHolderPackage) holder).textMoney.setText("" + promote.getCanUseAmt());
//            ((ViewHolderPackage) holder).textName.setText(promote.getPromProdName());
//            ((ViewHolderPackage) holder).textPercent.setText(promote.getMinBuyAmtOrPerc());
//            setView(((ViewHolderPackage) holder).limitMoney, promote.getFitBdTermOrYrt());
//            ((ViewHolderPackage) holder).limitType.setText(promote.getFitProdOrBdType());
//            ((ViewHolderPackage) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
//            toUseTicket(holder, promote.getPromProdId());
//        } else
        else if (holder instanceof ProgressViewHolder) {
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

    class BaseViewHoleder extends RecyclerView.ViewHolder {

        protected TextView tv_name;
        protected TextView tv_validate_date;
        protected TextView tv_percent_limit;
        protected TextView tv_date_limit;
        protected TextView tv_use_limit;
        protected TextView tv_amount_unit;
        protected TextView tv_amount;
        protected LinearLayout frame_ticket;
        protected ImageView iv_icon;

        public BaseViewHoleder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_validate_date = (TextView) itemView.findViewById(R.id.tv_validate_date);
            tv_percent_limit = (TextView) itemView.findViewById(R.id.tv_percent_limit);
            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
            tv_use_limit = (TextView) itemView.findViewById(R.id.tv_use_limit);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_amount_unit = (TextView) itemView.findViewById(R.id.tv_amount_unit);
            frame_ticket = (LinearLayout) itemView.findViewById(R.id.frame_ticket);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
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
