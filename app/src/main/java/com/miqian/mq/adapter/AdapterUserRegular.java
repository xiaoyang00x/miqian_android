package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.Reg;
import com.miqian.mq.entity.RegInvest;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.Uihelper;

import java.math.BigDecimal;
import java.util.List;

public class AdapterUserRegular extends RecyclerView.Adapter {

    private final int VIEW_HEADER = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;
    private final Context mContext;
    private final Page mPage;

    private MyItemClickListener onItemClickListener;

    private int maxValue = 999;//最大的值
    private String isExpiry;

    private List<RegInvest> mList;
    private Reg mReg;

    public AdapterUserRegular(Context context, List<RegInvest> list, Reg reg, Page page, String isExpiry) {
        this.mContext = context;
        this.mList = list;
        this.mReg = reg;
        this.isExpiry = isExpiry;
        this.mPage = page;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return VIEW_HEADER;
        } else if (position + 1 == getItemCount()) {
            return VIEW_FOOTER;
        } else {
            return VIEW_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_regular, parent, false);
            viewHolder = new ViewHolder(view);
        } else if (viewType == VIEW_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_regular_header, parent, false);
            viewHolder = new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            RegInvest regInvest = mList.get(position - 1);
            if (regInvest == null) {
                return;
            }
            ((ViewHolder) holder).bdName.setText(regInvest.getProductName());
            ((ViewHolder) holder).ivProcessing.setImageDrawable(mContext.getResources().getDrawable(R.drawable.trans));
            if (isExpiry.equals("Y")) {
                ((ViewHolder) holder).textCapital.setText("投资本金");
                ((ViewHolder) holder).textEarningName.setText("已获收益");
                ((ViewHolder) holder).textEarning.setText(regInvest.getYsProfit());
                ((ViewHolder) holder).textCapitalMoney.setText(regInvest.getPurchaseAmount());
                ((ViewHolder) holder).textCapitalMoney.setTextColor(ContextCompat.getColor(mContext, R.color.mq_b4_v2));
                ((ViewHolder) holder).textEarning.setTextColor(ContextCompat.getColor(mContext, R.color.mq_b4_v2));
                ((ViewHolder) holder).textInterestRate.setTextColor(ContextCompat.getColor(mContext, R.color.mq_b4_v2));
                ((ViewHolder) holder).textInterestRatePresent.setTextColor(ContextCompat.getColor(mContext, R.color.mq_b4_v2));
            } else {

                ((ViewHolder) holder).textCapital.setText("待收本金");
                ((ViewHolder) holder).textEarningName.setText("待收收益");
                ((ViewHolder) holder).textEarning.setText(regInvest.getDsProfit());
                ((ViewHolder) holder).textCapitalMoney.setText(regInvest.getDsAmount());
                ((ViewHolder) holder).textCapitalMoney.setTextColor(ContextCompat.getColor(mContext, R.color.mq_r1_v2));
                ((ViewHolder) holder).textEarning.setTextColor(ContextCompat.getColor(mContext, R.color.mq_r1_v2));
                ((ViewHolder) holder).textInterestRate.setTextColor(ContextCompat.getColor(mContext, R.color.mq_r1_v2));
                ((ViewHolder) holder).textInterestRatePresent.setTextColor(ContextCompat.getColor(mContext, R.color.mq_r1_v2));
            }
            BigDecimal realInterest = regInvest.getProductRate();
            BigDecimal presentInterest = regInvest.getProductPlusRate();
            BigDecimal ticketRate = regInvest.getTicketRate();
            int ticketType = regInvest.getTicketType();//促销券类型   0: 红包  1: 秒钱卡   2: 加息卡   3: 双倍加息卡   4: 现金

            ((ViewHolder) holder).imageProjectStatus.setBackgroundResource(R.drawable.trans);

            String bidType = regInvest.getBidType();
            if (Constants.BID_TYPE_SBB.equals(bidType)) {
                ((ViewHolder) holder).textInterestRate.setText((realInterest.add(realInterest) + ""));
                ((ViewHolder) holder).textInterestRatePresent.setText("%");
                ((ViewHolder) holder).imageProjectStatus.setBackgroundResource(R.drawable.double_rate_normal);
            } else if (Constants.BID_TYPE_SBSYK.equals(bidType)) {
                if (ticketType == 3) {
                    realInterest = (realInterest.add(realInterest)).add(presentInterest.add(presentInterest));
                }
                ((ViewHolder) holder).textInterestRate.setText(realInterest + "");
                ((ViewHolder) holder).textInterestRatePresent.setText("%");
                ((ViewHolder) holder).imageProjectStatus.setBackgroundResource(R.drawable.double_card_normal);

            } else {
                ((ViewHolder) holder).textInterestRate.setText(realInterest + "");
                if (presentInterest.compareTo(BigDecimal.ZERO) > 0) {
                    ((ViewHolder) holder).textInterestRatePresent.setText("+" + presentInterest + "%");
                } else if (ticketRate.compareTo(BigDecimal.ZERO) > 0) {
                    ((ViewHolder) holder).textInterestRatePresent.setText("+" + ticketRate + "%");
                } else if (ticketRate.compareTo(BigDecimal.ZERO) > 0 && presentInterest.compareTo(BigDecimal.ZERO) > 0) {
                    ((ViewHolder) holder).textInterestRatePresent.setText("+" + ticketRate.add(presentInterest) + "%");
                } else {
                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
                }
            }

//            int showType = CalculateUtil.getShowInterest(regInvest.getProjectState(), regInvest.getSubjectType(), regInvest.getProductRate()
//                    , regInvest.getProductPlusRate(), regInvest.getTransedAmt());
//            String projectState = regInvest.getProjectState();

//            switch (showType) {
//                case CalculateUtil.INTEREST_SHOWTYPE_ONE:
//                    showProjectImage(projectState, (ViewHolder) holder);
//                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_TWO:
//                    showProjectImage(projectState, (ViewHolder) holder);
//                    if (!TextUtils.isEmpty(realInterest)) {
//                        ((ViewHolder) holder).textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                    }
//                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_THREE:
//                    showProjectImage(projectState, (ViewHolder) holder);
//                    ((ViewHolder) holder).textInterestRatePresent.setText("+" + presentInterest + "%");
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_FOUR:
//                    if (!TextUtils.isEmpty(realInterest)) {
//                        ((ViewHolder) holder).textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                    }
//                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
//                    ((ViewHolder) holder).imageProjectStatus.setVisibility(View.VISIBLE);
//                    ((ViewHolder) holder).imageProjectStatus.setImageResource(R.drawable.double_rate_normal);
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_FIVE:
//                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_SIX:
//                    if (!TextUtils.isEmpty(realInterest)) {
//                        ((ViewHolder) holder).textInterestRate.setText(Float.parseFloat(realInterest) * 2 + "");
//                    }
//                    ((ViewHolder) holder).textInterestRatePresent.setText("%");
//                    ((ViewHolder) holder).imageProjectStatus.setVisibility(View.VISIBLE);
//                    ((ViewHolder) holder).imageProjectStatus.setImageResource(R.drawable.double_card_normal);
//                    break;
//                case CalculateUtil.INTEREST_SHOWTYPE_SEVEN:
//                    ((ViewHolder) holder).textInterestRatePresent.setText("+" + presentInterest + "%");
//                    break;
//                default:
//                    break;
//            }

            ((ViewHolder) holder).textDateSubscribe.setText(Uihelper.timestampToDateStr_other(regInvest.getStartTime()) + "认购");
            ((ViewHolder) holder).textDateOver.setText(Uihelper.timestampToDateStr_other(regInvest.getEndTime()) + "到期");
            String status = regInvest.getStatus();
            if ("01".equals(status)) {//认购处理中
                ((ViewHolder) holder).ivProcessing.setImageDrawable(mContext.getResources().getDrawable(R.drawable.processing));
            }
        } else if (holder instanceof ProgressViewHolder) {
            if (position > maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 10) {
                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
            } else {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        } else if (holder instanceof HeaderViewHolder) {

            ((HeaderViewHolder) holder).textRegularCount.setText(mPage.getTotalRecord() + "");
            if (isExpiry.equals("Y")) {
                ((HeaderViewHolder) holder).textCapitalName.setText("投资本金(元)");
                ((HeaderViewHolder) holder).textEarningName.setText("已获收益(元)");
                if (mReg != null) {
                    ((HeaderViewHolder) holder).textCapital.setText(mReg.getPerTotalAmt());
                    ((HeaderViewHolder) holder).textEarning.setText(mReg.getPerTotalIncome());
                }

            } else {
                ((HeaderViewHolder) holder).textCapitalName.setText("总待收本金(元)");
                ((HeaderViewHolder) holder).textEarningName.setText("总待收收益(元)");
                if (mReg != null) {
                    ((HeaderViewHolder) holder).textCapital.setText(mReg.getRegTotalAmt());
                    ((HeaderViewHolder) holder).textEarning.setText(mReg.getRegTotalIncome());
                }
            }
//            else {
//                ((HeaderViewHolder) holder).textRegular.setText("已转让(笔)");
//                ((HeaderViewHolder) holder).textCapitalName.setText("累计金额(元)");
//                ((HeaderViewHolder) holder).textEarningName.setText("");
//                ((HeaderViewHolder) holder).textCapital.setText("");
//                if (mReg != null) {
//                    ((HeaderViewHolder) holder).textEarning.setText(mReg.getTransTotalAmt());
//                }
//            }
        }
    }

//    public void showProjectImage(String projectstatus, ViewHolder holder) {
//        holder.imageProjectStatus.setVisibility(View.VISIBLE);
//        if (projectstatus.equals("0")) {
//            holder.imageProjectStatus.setImageResource(R.drawable.user_regular_transfering);
//        } else if (projectstatus.equals("1")) {
//            holder.imageProjectStatus.setImageResource(R.drawable.user_regular_transfer_wjx);
//        } else if (projectstatus.equals("2")) {
//            holder.imageProjectStatus.setImageResource(R.drawable.user_regular_transfered);
//        }
//    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() != 0) {
            return mList.size() + 2;//+2 头部：定期投资的笔数 和 尾部：加载更多
        }
        return 1;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bdName;
        public ImageView imageProjectStatus;

        public TextView textCapital;
        public TextView textCapitalMoney;
        public TextView textEarning;
        public TextView textInterestRate;
        public TextView textInterestRatePresent;
        public TextView textEarningName;
        public TextView textRight;
        public ImageView ivProcessing;


        public TextView textDateSubscribe;
        public TextView textDateOver;

        public ViewHolder(View itemView) {
            super(itemView);
            bdName = (TextView) itemView.findViewById(R.id.text_name);
            imageProjectStatus = (ImageView) itemView.findViewById(R.id.image_project_status);
            textCapital = (TextView) itemView.findViewById(R.id.text_capital);
            textCapitalMoney = (TextView) itemView.findViewById(R.id.text_capital_money);
            textEarning = (TextView) itemView.findViewById(R.id.text_earning);
            textInterestRate = (TextView) itemView.findViewById(R.id.text_interest_rate);
            textInterestRatePresent = (TextView) itemView.findViewById(R.id.text_interest_rate_present);
            textDateSubscribe = (TextView) itemView.findViewById(R.id.text_date_subscribe);
            textDateOver = (TextView) itemView.findViewById(R.id.text_date_over);

            textEarningName = (TextView) itemView.findViewById(R.id.text_earning_name);
            textRight = (TextView) itemView.findViewById(R.id.text_right);
            ivProcessing = (ImageView) itemView.findViewById(R.id.iv_processing);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition() - 1);
                    }
                }
            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView textRegularCount;
        public TextView textCapital;
        public TextView textEarning;
        public TextView textCapitalName;
        public TextView textEarningName;
        public TextView textRegular;

        public HeaderViewHolder(View view) {
            super(view);
            textRegularCount = (TextView) view.findViewById(R.id.text_regular_count);
            textCapital = (TextView) view.findViewById(R.id.text_capital);
            textEarning = (TextView) view.findViewById(R.id.text_earning);
            textCapitalName = (TextView) view.findViewById(R.id.text_capital_name);
            textEarningName = (TextView) view.findViewById(R.id.text_earning_name);
            textRegular = (TextView) view.findViewById(R.id.text_regular);
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

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
