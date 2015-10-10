package com.miqian.mq.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterMyRedPaper extends RecyclerView.Adapter {

    private List<Redpaper.CustPromotion> promList;


    public AdapterMyRedPaper(List<Redpaper.CustPromotion> promList) {
        this.promList = promList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_red_packet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Redpaper.CustPromotion promote = promList.get(position);
        ((ViewHolder) holder).textMoney.setText("￥" + promote.getCanUseAmt());
        ((ViewHolder) holder).limitType.setText(promote.getLimitMsg());
        ((ViewHolder) holder).limitDate.setText("有效期至" + Uihelper.timestampToString(promote.getEndTimestamp()));
        ((ViewHolder) holder).promoteChoosed.setVisibility(View.GONE);

        String state = promote.getSta();
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
                ((ViewHolder) holder).imageRedpacketState.setVisibility(View.GONE);
            }
        }else {
            ((ViewHolder) holder).imageRedpacketState.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (promList != null) {
            return promList.size();
        }
        return 0;
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
