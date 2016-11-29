package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.TranferDetailInfo;

import java.util.List;

public class AdapterTransferDetail extends RecyclerView.Adapter {

    private List<TranferDetailInfo> mList;

    public AdapterTransferDetail(List<TranferDetailInfo> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transfer_detail, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            TranferDetailInfo tranferDetailInfo = mList.get(position);
            ((ViewHolder) holder).textTransferingMoney.setText(tranferDetailInfo.getPrin());
            ((ViewHolder) holder).textTransferStatus.setText(tranferDetailInfo.getTransState());
//            ((ViewHolder) holder).textTransferMoney.setText(tranferDetailInfo.getBuyAmt());
            ((ViewHolder) holder).textDate.setText(tranferDetailInfo.getApplDt());
            ((ViewHolder) holder).textInterest.setText(tranferDetailInfo.getDiscount());
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textTransferingMoney;
        public TextView textTransferStatus;
//        public TextView textTransferMoney;
        public TextView textDate;
        public TextView textInterest;

        public ViewHolder(View itemView) {
            super(itemView);
            textTransferingMoney = (TextView) itemView.findViewById(R.id.text_transfering_money);
            textTransferStatus = (TextView) itemView.findViewById(R.id.text_transfer_status);
//            textTransferMoney = (TextView) itemView.findViewById(R.id.text_transfer_money);
            textDate = (TextView) itemView.findViewById(R.id.text_date);
            textInterest = (TextView) itemView.findViewById(R.id.text_interest);
        }
    }
}


