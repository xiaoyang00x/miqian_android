package com.miqian.mq.adapter.typeadapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RecordCurrent;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterCurrrentRecord extends RecyclerView.Adapter {

    private List<RecordCurrent.CurSubRecord> dataList;


    public AdapterCurrrentRecord(List<RecordCurrent.CurSubRecord> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_record, parent, false);
        return new ViewHolderRecord(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        RecordCurrent.CurSubRecord data = dataList.get(position);
        String traCd = data.getTraCd();

        if (!TextUtils.isEmpty(traCd)) {

            switch (traCd) {
                case "SR01"://认购交易
                    ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_subscribe);
                    ((ViewHolderRecord) holder).textType.setText(data.getType());
                    ((ViewHolderRecord) holder).tvAmt.setText("-" + data.getAmt());
                    ((ViewHolderRecord) holder).tvInterest.setText("金额(元)");

                    break;
                case "SS01"://赎回交易
                    ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_redem);
                    ((ViewHolderRecord) holder).textType.setText(data.getType());
                    ((ViewHolderRecord) holder).tvAmt.setText("+" + data.getAmt());
                    ((ViewHolderRecord) holder).tvInterest.setText("利息" + data.getInterest() + "元");
                    break;
                case "SZ01"://转让交易
                    ((ViewHolderRecord) holder).textType.setText(data.getType());
                    ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_transfer);
                    ((ViewHolderRecord) holder).tvAmt.setText("+" + data.getAmt());
                    ((ViewHolderRecord) holder).tvInterest.setText("利息" + data.getInterest() + "元");
                    break;
                default:
                    ((ViewHolderRecord) holder).textType.setText("");
                    ((ViewHolderRecord) holder).ivState.setImageResource(R.color.transparent);
                    break;
            }

        } else {//空为活期认购
            ((ViewHolderRecord) holder).ivState.setImageResource(R.drawable.record_subscribe);
            ((ViewHolderRecord) holder).textType.setText(data.getType());
            ((ViewHolderRecord) holder).tvAmt.setText("-" + data.getAmt());
            ((ViewHolderRecord) holder).tvInterest.setText("金额(元)");

        }
        if (!TextUtils.isEmpty(data.getCrtDt())) {
            ((ViewHolderRecord) holder).tvTime.setText(data.getCrtDt());
        } else {
            ((ViewHolderRecord) holder).tvTime.setText("--");
        }

    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
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

}
