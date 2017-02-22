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
import com.umeng.onlineconfig.OnlineConfigAgent;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterMyTicket extends RecyclerView.Adapter {

    private List<Promote> promList;
    private int maxValue = 999;//最大的值

    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_LIST = 1;
    private static final int VIEW_TYPE_ADD_INTEREST = 2;              //八八双倍收益卡
    private static final int VIEW_TYPE_QQ = 3;                  //2017 手Q红包
    private static final int VIEW_TYPE_FOOTER_TIP = 4;  //过期30天红包提示

    private Context mContext;
    private boolean isValid;        //卡券是否有效
    private boolean isOverdue;        //是否是已过期Tab

    /**
     * @param context
     * @param promList
     * @param isValid  卡券是否有效
     */
    public AdapterMyTicket(Context context, List<Promote> promList, boolean isValid) {
        this.promList = promList;
        this.mContext = context;
        this.isValid = isValid;
    }

    /**
     * @param context
     * @param promList
     * @param isValid  卡券是否有效
     */
    public AdapterMyTicket(Context context, List<Promote> promList, boolean isValid, boolean isOverdue) {
        this.promList = promList;
        this.mContext = context;
        this.isValid = isValid;
        this.isOverdue = isOverdue;
    }

    //促销类型 SC：拾财券  HB：红包 JF：积分 LP：礼品卡 TY：体验金
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            if (isOverdue) {
                return VIEW_TYPE_FOOTER_TIP;
            } else {
                return VIEW_TYPE_FOOTER;
            }
        } else {
            if (promList.get(position) != null && Promote.TYPE.SK.getValue().equals(promList.get(position).getType())) {
                return VIEW_TYPE_ADD_INTEREST;
            } else if (isQQPromote(promList.get(position)) && isValid) {
                return VIEW_TYPE_QQ;
            } else {
                return VIEW_TYPE_LIST;
            }
        }
    }

    public static boolean isQQPromote(Promote promote) {
        boolean flag = false;
        switch (promote.getPromProdId()) {
            case "000000000000000000000230830444":
            case "000000000000000000000230830581":
            case "000000000000000000000230830662":
            case "000000000000000000000230830778":
            case "000000000000000000000230830828":
                flag = true;
                break;
            default:
                break;

        }
        return flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_temp, parent, false);
                return new BaseViewHoleder(view);
            case VIEW_TYPE_QQ:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket_qq, parent, false);
                return new QQViewHoleder(view);
            case VIEW_TYPE_ADD_INTEREST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_baba_festival, parent, false);
                return new BaBaViewHoleder(view);
            case VIEW_TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressViewHolder(view);
            case VIEW_TYPE_FOOTER_TIP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
                return new ProgressTip(view);
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
        if (holder instanceof BaBaViewHoleder) {
            BaBaViewHoleder tempViewHoleder = (BaBaViewHoleder) holder;
            Promote promote = promList.get(position);
            setText(tempViewHoleder.tv_name, promote.getPromProdName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            setText(tempViewHoleder.tv_percent_limit, promote.getMinBuyAmtOrPerc());
            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getFitProdOrBdType());
            String desUrl = promote.getPromUrl();
            setText(tempViewHoleder.tv_amount, String.valueOf(isValid ? promote.getCanUseAmt() : promote.getTotalAmt()));
            tempViewHoleder.tv_amount_unit.setVisibility(View.VISIBLE);
            tempViewHoleder.tv_precent_unit.setVisibility(View.GONE);

            clickEvent(holder, promote.getPromProdId(), promote.getPromState(), desUrl);

            tempViewHoleder.setViewEnable(isValid);
        } else if (holder instanceof BaseViewHoleder) {
            BaseViewHoleder tempViewHoleder = (BaseViewHoleder) holder;
            Promote promote = promList.get(position);
            setText(tempViewHoleder.tv_name, promote.getPromProdName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            setText(tempViewHoleder.tv_percent_limit, promote.getMinBuyAmtOrPerc());
            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getFitProdOrBdType());
            String desUrl = promote.getPromUrl();
            if (Promote.TYPE.JX.getValue().equals(promote.getType())) { // 加息券
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_blue);
                tempViewHoleder.tv_amount_unit.setVisibility(View.GONE);
                tempViewHoleder.tv_precent_unit.setVisibility(View.VISIBLE);
                setText(tempViewHoleder.tv_amount, promote.getGiveYrt());
            } else {
                setText(tempViewHoleder.tv_amount, String.valueOf(isValid ? promote.getCanUseAmt() : promote.getTotalAmt()));
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
            clickEvent(holder, promote.getPromProdId(), promote.getPromState(), desUrl);

            tempViewHoleder.setViewEnable(isValid);
        } else if (holder instanceof QQViewHoleder) {
            QQViewHoleder tempViewHoleder = (QQViewHoleder) holder;
            Promote promote = promList.get(position);
//            setText(tempViewHoleder.tv_name, promote.getPromProdName());
            setText(tempViewHoleder.tv_validate_date, Uihelper.redPaperTime(promote.getEndTimestamp()));
            String qqString = "";
            if (!TextUtils.isEmpty(promote.getFitBdTermOrYrt()) && !TextUtils.isEmpty(promote.getMinBuyAmtOrPerc())) {
                qqString = promote.getMinBuyAmtOrPerc() + ", " + promote.getFitBdTermOrYrt();
            } else if (!TextUtils.isEmpty(promote.getFitBdTermOrYrt())){
                qqString = promote.getFitBdTermOrYrt();
            } else {
                qqString = promote.getMinBuyAmtOrPerc();
            }
            setText(tempViewHoleder.tv_percent_limit, qqString);
//            setText(tempViewHoleder.tv_date_limit, promote.getFitBdTermOrYrt());
            setText(tempViewHoleder.tv_use_limit, promote.getFitProdOrBdType());
            String desUrl = promote.getPromUrl();
            if ("000000000000000000000230830444".equals(promote.getPromProdId())) {
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_qq_8);
            } else if ("000000000000000000000230830581".equals(promote.getPromProdId())) {
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_qq_50);
            } else if ("000000000000000000000230830662".equals(promote.getPromProdId())) {
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_qq_100);
            } else if ("000000000000000000000230830778".equals(promote.getPromProdId())) {
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_qq_210);
            } else if ("000000000000000000000230830828".equals(promote.getPromProdId())) {
                tempViewHoleder.frame_ticket.setBackgroundResource(R.drawable.bg_ticket_qq_520);
            }
            setText(tempViewHoleder.tv_amount, String.valueOf(isValid ? promote.getCanUseAmt() : promote.getTotalAmt()));
            clickEvent(holder, promote.getPromProdId(), promote.getPromState(), desUrl);
//            tempViewHoleder.setViewEnable(isValid);
        } else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.GONE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(isValid ? View.VISIBLE : View.GONE);
            } else {
                ((ProgressViewHolder) holder).frameLoad.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).frameNone.setVisibility(View.GONE);
            }
        }else if (holder instanceof ProgressTip) {
            if (position >= maxValue) {
                ((ProgressTip) holder).frameLoad.setVisibility(View.GONE);
                ((ProgressTip) holder).frameTip.setVisibility(View.VISIBLE);
            } else {
                ((ProgressTip) holder).frameLoad.setVisibility(View.VISIBLE);
                ((ProgressTip) holder).frameTip.setVisibility(View.GONE);
            }
        }
    }

    // promState为1时可以跳转（有URL跳转到H5,否则跳转到定期列表）
    private void clickEvent(RecyclerView.ViewHolder holder, final String promProdId, final String promState, final String promUrl) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(promState)) {
                    if (!TextUtils.isEmpty(promUrl)) {
                        WebActivity.startActivity(mContext, promUrl);
                    } else {
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

        public void setViewEnable(boolean isValid) {
            frame_ticket.setEnabled(isValid);
            tv_name.setEnabled(isValid);
            tv_precent_unit.setEnabled(isValid);
            tv_amount_unit.setEnabled(isValid);
            tv_amount.setEnabled(isValid);
            tv_validate_date.setEnabled(isValid);
            tv_percent_limit.setEnabled(isValid);
            tv_date_limit.setEnabled(isValid);
            tv_use_limit.setEnabled(isValid);
            itemView.setEnabled(isValid);

        }
    }

    class QQViewHoleder extends RecyclerView.ViewHolder {

        //        protected TextView tv_name;
        protected TextView tv_validate_date;
        protected TextView tv_percent_limit;
        //        protected TextView tv_date_limit;
        protected TextView tv_use_limit;
        protected TextView tv_amount_unit;
        //        protected TextView tv_precent_unit;
        protected TextView tv_amount;
        protected RelativeLayout frame_ticket;

        public QQViewHoleder(View itemView) {
            super(itemView);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_amount_unit = (TextView) itemView.findViewById(R.id.tv_amount_unit);
            tv_percent_limit = (TextView) itemView.findViewById(R.id.tv_percent_limit);
            tv_validate_date = (TextView) itemView.findViewById(R.id.tv_validate_date);
            tv_use_limit = (TextView) itemView.findViewById(R.id.tv_use_limit);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
//            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
//            tv_precent_unit = (TextView) itemView.findViewById(R.id.tv_precent_unit);
            frame_ticket = (RelativeLayout) itemView.findViewById(R.id.frame_ticket);
        }

//        public void setViewEnable(boolean isValid) {
//            frame_ticket.setEnabled(isValid);
//            tv_name.setEnabled(isValid);
//            tv_precent_unit.setEnabled(isValid);
//            tv_amount_unit.setEnabled(isValid);
//            tv_amount.setEnabled(isValid);
//            tv_validate_date.setEnabled(isValid);
//            tv_percent_limit.setEnabled(isValid);
//            tv_date_limit.setEnabled(isValid);
//            tv_use_limit.setEnabled(isValid);
//            itemView.setEnabled(isValid);
//
//        }
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

        public void setViewEnable(boolean isValid) {
            super.setViewEnable(isValid);
            img_tag.setImageResource(isValid ? R.drawable.icon_babafestival_enabled : R.drawable.icon_babafestival_unabled);
            img_background.setImageResource(isValid ? R.drawable.icon_babacard_enabled : R.drawable.icon_babacard_unabled);
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
            if (Uihelper.getConfigCrowd(mContext)) {
                btOverdue.setVisibility(View.GONE);
            }
        }
    }

    class ProgressTip extends RecyclerView.ViewHolder {

        private LinearLayout frameLoad;
        private LinearLayout frameTip;

        public ProgressTip(View view) {
            super(view);
            frameLoad = (LinearLayout) view.findViewById(R.id.frame_load);
            frameTip = (LinearLayout) view.findViewById(R.id.frame_tip);
        }
    }
}
