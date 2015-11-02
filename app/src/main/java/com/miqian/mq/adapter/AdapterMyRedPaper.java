package com.miqian.mq.adapter;

import android.graphics.Color;
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
public class AdapterMyRedPaper extends RecyclerView.Adapter {

    private List<CustPromotion> promList;
    private int maxValue = 999;//最大的值
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;


    public AdapterMyRedPaper(List<CustPromotion> promList) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_red_packet, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if ((holder instanceof ViewHolder)){
            CustPromotion promote = promList.get(position);
            ((ViewHolder) holder).textMoney.setText("￥" + promote.getTotalAmt());
            ((ViewHolder) holder).limitType.setText(promote.getLimitMsg());
            ((ViewHolder) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            ((ViewHolder) holder).promoteChoosed.setVisibility(View.GONE);

            String state = promote.getStatus();
            ((ViewHolder) holder).imageRedPacket.setBackgroundResource(R.drawable.red_package);
            ((ViewHolder) holder).textMoney.setTextColor(Color.parseColor("#f13e3e"));
            if (!TextUtils.isEmpty(state)) {

                ((ViewHolder) holder).imageRedpacketState.setVisibility(View.VISIBLE);
                if ("YW".equals(state)) {
                    ((ViewHolder) holder).imageRedpacketState.setImageResource(R.drawable.hb_used);
                    ((ViewHolder) holder).imageRedPacket.setBackgroundResource(R.drawable.red_package_grey);
                    ((ViewHolder) holder).textMoney.setTextColor(Color.parseColor("#f8d4d4"));
                } else if ("GQ".equals(state)) {
                    ((ViewHolder) holder).imageRedpacketState.setImageResource(R.drawable.hb_expired);
                    ((ViewHolder) holder).imageRedPacket.setBackgroundResource(R.drawable.red_package_grey);
                    ((ViewHolder) holder).textMoney.setTextColor(Color.parseColor("#f8d4d4"));
                } else {
                    ((ViewHolder) holder).imageRedPacket.setBackgroundResource(R.drawable.red_package);
                    ((ViewHolder) holder).imageRedpacketState.setImageResource(R.color.transparent);

                }
            }else {
                ((ViewHolder) holder).imageRedPacket.setBackgroundResource(R.drawable.red_package);
                ((ViewHolder) holder).imageRedpacketState.setImageResource(R.color.transparent);
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
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textLoading;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            textLoading = (TextView) view.findViewById(R.id.text_loading);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView limitType;
        public TextView limitDate;
        public TextView textMoney;
        public ImageView promoteChoosed;
        public ImageView imageRedpacketState;
        public ImageView imageRedPacket;

        public ViewHolder(View itemView) {
            super(itemView);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
            imageRedpacketState = (ImageView) itemView.findViewById(R.id.image_redpacket_state);
            imageRedPacket = (ImageView) itemView.findViewById(R.id.image_red_packet);
        }
    }

}
