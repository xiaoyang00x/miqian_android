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

import java.math.BigDecimal;
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
    private boolean isValid;        //卡券是否有效

    /**
     *
     * @param context
     * @param promList
     * @param isValid  卡券是否有效
     */
    public AdapterMyTicket(Context context, List<Promote> promList, boolean isValid) {
        this.promList = promList;
        this.mContext = context;
        this.isValid = isValid;
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
                return null;
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
        if (holder instanceof BaseViewHoleder) {
            BaseViewHoleder tempViewHoleder = (BaseViewHoleder) holder;
            final Promote promote = promList.get(position);
            setText(tempViewHoleder.tv_name, promote.getPromProdName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            setText(tempViewHoleder.tv_percent_limit, promote.getMinBuyAmtOrPerc());
            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getLimitMsg());
            String desUrl = promote.getPromUrl();
            if (Promote.TYPE.JX.getValue().equals(promote.getType())) { // 加息券
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_blue);
                tempViewHoleder.tv_amount_unit.setVisibility(View.GONE);
                tempViewHoleder.tv_precent_unit.setVisibility(View.VISIBLE);
                setText(tempViewHoleder.tv_amount, promote.getGiveYrt());
            } else {
                setText(tempViewHoleder.tv_amount, String.valueOf(promote.getCanUseAmt()));
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
            clickEvent(holder, promote.getType(), promote.getPromProdId(), promote.getPromState(), desUrl);

            setViewEnable(tempViewHoleder);
        }
        else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.GONE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(View.VISIBLE);
            } else {
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(View.GONE);
            }
        }
    }

    // 使用券:(体验金 分享券)只能跳转h5,(其他券)优先跳h5,无h5跳转到定期列表使用页面
    private void clickEvent(RecyclerView.ViewHolder holder, final String type, final String promProdId, final String promState, final String promUrl) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != promUrl) {
                    if (Promote.TYPE.FXQ.getValue().equals(type) || Promote.TYPE.TY.getValue().equals(type)) {
                        WebActivity.startActivity(mContext, promUrl);
                    } else if ("1".equals(promState)) { // 可跳转
                        WebActivity.startActivity(mContext, promUrl);
                    } else {
                        RegularListActivity.startActivity(mContext, promProdId);
                    }
                } else {
                    if (!Promote.TYPE.FXQ.getValue().equals(type) &&
                            !Promote.TYPE.TY.getValue().equals(type)) {
                        RegularListActivity.startActivity(mContext, promProdId);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (promList != null && promList.size() != 0) {
            return promList.size() + 1; // +1 尾部：加载更多
        }
        return 0;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    private void setViewEnable(BaseViewHoleder holder) {
        holder.frame_ticket.setEnabled(isValid);
        holder.tv_name.setEnabled(isValid);
        holder.tv_precent_unit.setEnabled(isValid);
        holder.tv_amount_unit.setEnabled(isValid);
        holder.tv_amount.setEnabled(isValid);
        holder.tv_validate_date.setEnabled(isValid);
        holder.tv_percent_limit.setEnabled(isValid);
        holder.tv_date_limit.setEnabled(isValid);
        holder.tv_use_limit.setEnabled(isValid);
        holder.itemView.setEnabled(isValid);

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

        public BaseViewHoleder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_validate_date = (TextView) itemView.findViewById(R.id.tv_validate_date);
            tv_percent_limit = (TextView) itemView.findViewById(R.id.tv_percent_limit);
            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
            tv_use_limit = (TextView) itemView.findViewById(R.id.tv_use_limit);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_amount_unit = (TextView) itemView.findViewById(R.id.tv_amount_unit);
            tv_precent_unit = (TextView) itemView.findViewById(R.id.tv_precent_unit);
            frame_ticket = (RelativeLayout) itemView.findViewById(R.id.frame_ticket);
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
