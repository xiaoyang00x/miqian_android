package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RepaymentInfo;

import java.util.List;

public class AdapterRepayment extends RecyclerView.Adapter {

    private List<RepaymentInfo> mList;

    public AdapterRepayment(List<RepaymentInfo> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_repayment, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            RepaymentInfo repaymentInfo = mList.get(position);
            ((ViewHolder) holder).textDate.setText(repaymentInfo.getRepaymentDate());
            ((ViewHolder) holder).textRepaymentStatus.setText(repaymentInfo.getState());
            ((ViewHolder) holder).textCapital.setText("应还本金：" + repaymentInfo.getRepaymentPrincipal());
            ((ViewHolder) holder).textEarning.setText("应付收益：" + repaymentInfo.getRepaymentProfilt());
            ((ViewHolder) holder).textPresent.setText("赠送：" + repaymentInfo.getRepaymentPresentation());
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

        public TextView textDate;
        public TextView textRepaymentStatus;
        public TextView textCapital;
        public TextView textEarning;
        public TextView textPresent;

        public ViewHolder(View itemView) {
            super(itemView);
            textDate = (TextView) itemView.findViewById(R.id.text_date);
            textRepaymentStatus = (TextView) itemView.findViewById(R.id.text_repayment_status);
            textCapital = (TextView) itemView.findViewById(R.id.text_capital);
            textEarning = (TextView) itemView.findViewById(R.id.text_earning);
            textPresent = (TextView) itemView.findViewById(R.id.text_present);
        }
    }
}