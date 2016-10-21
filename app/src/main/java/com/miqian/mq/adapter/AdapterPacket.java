package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private static final int VIEW_TYPE_ADD_INTEREST = 2;              //八八双倍收益卡

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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_temp, parent, false);
                return new BaseViewHoleder(view);
            case VIEW_TYPE_ADD_INTEREST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_baba_festival, parent, false);
                return new BaBaViewHoleder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_temp, parent, false);
                return new BaseViewHoleder(view);
        }
    }

    // 检查TextView将要填充内容的有效性,如果内容为空(无效)则隐藏该TextView
    private void setText(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaBaViewHoleder) {
            BaBaViewHoleder tempViewHoleder = (BaBaViewHoleder) holder;
            final Promote promote = promList.get(position);
            setText(tempViewHoleder.tv_name, promote.getName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            setText(tempViewHoleder.tv_percent_limit, promote.getMinBuyAmtOrPerc());
            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getFitProdOrBdType());
//            String desUrl = promote.getPromUrl();
            setText(tempViewHoleder.tv_amount, String.valueOf(promote.getUsableAmt()));
            tempViewHoleder.tv_amount_unit.setVisibility(View.VISIBLE);
            tempViewHoleder.tv_precent_unit.setVisibility(View.GONE);
            if (mPosition == position) {
                tempViewHoleder.promoteChoosed.setVisibility(View.VISIBLE);
            } else {
                tempViewHoleder.promoteChoosed.setVisibility(View.GONE);
            }
        } else if (holder instanceof BaseViewHoleder) {
            BaseViewHoleder tempViewHoleder = (BaseViewHoleder) holder;
            final Promote promote = promList.get(position);
            setText(tempViewHoleder.tv_name, promote.getName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            setText(tempViewHoleder.tv_percent_limit, promote.getMinBuyAmtOrPerc());
            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getFitProdOrBdType());
            if (Promote.TYPE.JX.getValue().equals(promote.getType())) { // 加息券
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_blue);
                tempViewHoleder.tv_amount_unit.setVisibility(View.GONE);
                tempViewHoleder.tv_precent_unit.setVisibility(View.VISIBLE);
                setText(tempViewHoleder.tv_amount, promote.getAddRate());
            } else {
                setText(tempViewHoleder.tv_amount, String.valueOf(promote.getUsableAmt()));
                tempViewHoleder.tv_amount_unit.setVisibility(View.VISIBLE);
                tempViewHoleder.tv_precent_unit.setVisibility(View.GONE);

                if (Promote.TYPE.SC.getValue().equals(promote.getType())) { // 拾财券
                    tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_yellow);
                } else if (Promote.TYPE.HB.getValue().equals(promote.getType())) { // 红包
                    tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_red);
                } else {
                    tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_black);
                }

            }
            if (mPosition == position) {
                tempViewHoleder.promoteChoosed.setVisibility(View.VISIBLE);
            } else {
                tempViewHoleder.promoteChoosed.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (promList != null) {
            return promList.size();
        }
        return 0;
    }

    //促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金 SK:双倍收益卡
    @Override
    public int getItemViewType(int position) {
        if (promList.get(position) != null && Promote.TYPE.SK.getValue().equals(promList.get(position).getType())) {
            return VIEW_TYPE_ADD_INTEREST;
        } else {
            return VIEW_TYPE_HB;
        }
    }

    class BaseViewHoleder extends RecyclerView.ViewHolder {

        protected TextView tv_name;
        protected TextView tv_validate_date;
        protected TextView tv_percent_limit;
        protected TextView tv_date_limit;
        protected TextView tv_use_limit;
        protected TextView tv_amount_unit;
        protected TextView tv_precent_unit;
        protected TextView tv_amount;
        protected RelativeLayout frame_ticket;
        public ImageView promoteChoosed;

        public BaseViewHoleder(View itemView) {
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
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_validate_date = (TextView) itemView.findViewById(R.id.tv_validate_date);
            tv_percent_limit = (TextView) itemView.findViewById(R.id.tv_percent_limit);
            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
            tv_use_limit = (TextView) itemView.findViewById(R.id.tv_use_limit);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_amount_unit = (TextView) itemView.findViewById(R.id.tv_amount_unit);
            tv_precent_unit = (TextView) itemView.findViewById(R.id.tv_precent_unit);
            frame_ticket = (RelativeLayout) itemView.findViewById(R.id.frame_ticket);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
        }
    }

    /**
     * 八八双倍收益卡holder
     */
    class BaBaViewHoleder extends BaseViewHoleder {

        protected ImageView img_background;
        protected ImageView img_tag;

        public BaBaViewHoleder(View itemView) {
            super(itemView);
            img_background = (ImageView) itemView.findViewById(R.id.img_background);
            img_tag = (ImageView) itemView.findViewById(R.id.img_tag);
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
}
