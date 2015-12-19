package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterPacket extends RecyclerView.Adapter {

    private List<Promote> promList;
    private MyItemClickListener onItemClickListener;
    private int mPosition = -1;

    private static final int VIEW_TYPE_HB = 0;
    private static final int VIEW_TYPE_SC = 1;

    public AdapterPacket(List<Promote> promList) {
        this.promList = promList;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_HB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_used, parent, false);
                return new ViewHolder(view);
            case VIEW_TYPE_SC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket, parent, false);
                return new ViewHolderTicket(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_used, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Promote promote = promList.get(position);
//        if (promote.getType().equals("HB")) {
            ((ViewHolder) holder).textMoney.setText("" + promote.getCanUseAmt());
            ((ViewHolder) holder).textName.setText(promote.getPromProdName());
            ((ViewHolder) holder).limitType.setText(promote.getMinBuyAmtOrPerc());
            ((ViewHolder) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
            if (mPosition == position) {
                ((ViewHolder) holder).promoteChoosed.setImageResource(R.drawable.promote_choosed);
            } else {
                ((ViewHolder) holder).promoteChoosed.setImageResource(R.drawable.promote_no_choosed);
            }
//        } else if (promote.getType().equals("SC")) {
//            ((ViewHolderTicket) holder).textMoney.setText("￥" + promote.getCanUseAmt());
//            ((ViewHolderTicket) holder).textType.setText(promote.getPromProdName());
//            ((ViewHolderTicket) holder).limitType.setText(promote.getLimitMsg());
//            ((ViewHolderTicket) holder).limitDate.setText(Uihelper.redPaperTime(promote.getEndTimestamp()));
//            if (mPosition == position) {
//                ((ViewHolderTicket) holder).promoteChoosed.setImageResource(R.drawable.promote_choosed);
//            } else {
//                ((ViewHolderTicket) holder).promoteChoosed.setImageResource(R.drawable.promote_no_choosed);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        if (promList != null) {
            return promList.size();
        }
        return 0;
    }

    //促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    @Override
    public int getItemViewType(int position) {
//        Promote promote = promList.get(position);
//        if (promote.getType().equals("HB")) {
//            return VIEW_TYPE_HB;
//        } else if (promote.getType().equals("SC")) {
//            return VIEW_TYPE_SC;
//        }
        return VIEW_TYPE_HB;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView limitType;
        public TextView limitDate;
        public TextView textMoney;
        public TextView textName;
        public ImageView promoteChoosed;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition != getLayoutPosition()) {
                        mPosition = getLayoutPosition();
                    } else {
                        mPosition = -1;
                    }
                    onItemClickListener.onItemClick(v, mPosition);
                    notifyDataSetChanged();
                }
            });
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
        }
    }

    class ViewHolderTicket extends RecyclerView.ViewHolder {

        public TextView textType;
        public TextView limitType;
        public TextView limitDate;
        public TextView textMoney;
        public ImageView promoteChoosed;

        public ViewHolderTicket(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition != getLayoutPosition()) {
                        mPosition = getLayoutPosition();
                    } else {
                        mPosition = -1;
                    }
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, mPosition);
                    }
                    notifyDataSetChanged();
                }
            });
            textType = (TextView) itemView.findViewById(R.id.text_type);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
}
