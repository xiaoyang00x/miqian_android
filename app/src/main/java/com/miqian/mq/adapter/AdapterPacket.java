package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Promote;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterPacket extends RecyclerView.Adapter {

    private List<Promote> promList;

    public AdapterPacket(List<Promote> promList) {
        this.promList = promList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_red_packet, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        Promote promote = promList.get(position);
        ((ViewHolder)holder).textMoney.setText(promote.getPromTotalAmt());
    }

    @Override
    public int getItemCount() {
        if (promList != null) {
            return promList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textType;
        public TextView textMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            textType = (TextView) itemView.findViewById(R.id.text_type);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
        }
    }

}
