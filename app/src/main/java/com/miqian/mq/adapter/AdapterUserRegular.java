package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Reg;
import com.miqian.mq.entity.RegInvest;

import java.util.List;

public class AdapterUserRegular extends RecyclerView.Adapter {

    private final int VIEW_HEADER = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;

    private MyItemClickListener onItemClickListener;

    private int maxValue = 999;//最大的值
    private int mType = 0;

    private List<RegInvest> mList;
    private Reg mReg;

    public AdapterUserRegular(List<RegInvest> list, Reg reg, int type) {
        this.mList = list;
        this.mReg = reg;
        this.mType = type;
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

            ((ViewHolder) holder).bdName.setText(regInvest.getBdNm());
            if (regInvest.getBearingStatus().equals("Y")) {
                ((ViewHolder) holder).textCapital.setText("投资本金");
                ((ViewHolder) holder).textCapitalMoney.setText(regInvest.getPrnAmt());
                ((ViewHolder) holder).imageProjectStatus.setVisibility(View.GONE);
                ((ViewHolder) holder).frameEarning.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).textCapital.setText("待收本金");
                ((ViewHolder) holder).textCapitalMoney.setText(regInvest.getRegAmt());
                if (regInvest.getProjectState().equals("1")) {
                    ((ViewHolder) holder).imageProjectStatus.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).imageProjectStatus.setImageResource(R.drawable.user_regular_transfering);
                } else if (regInvest.getProjectState().equals("2")) {
                    ((ViewHolder) holder).imageProjectStatus.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).imageProjectStatus.setImageResource(R.drawable.user_regular_transfer_red);
                } else {
                    ((ViewHolder) holder).imageProjectStatus.setVisibility(View.GONE);
                }
                ((ViewHolder) holder).frameEarning.setVisibility(View.VISIBLE);
            }

            if (regInvest.getProdId().equals("3")) {
                ((ViewHolder) holder).imageProjectType.setImageResource(R.drawable.user_regular_regular);
            } else {
                ((ViewHolder) holder).imageProjectType.setImageResource(R.drawable.user_regular_plan);
            }


            ((ViewHolder) holder).textEarning.setText(regInvest.getRegIncome());
            ((ViewHolder) holder).textInterestRate.setText(regInvest.getRealInterest());

            ((ViewHolder) holder).textDateSubscribe.setText("认购日期" + regInvest.getCrtDt());
            ((ViewHolder) holder).textDateOver.setText("到期日" + regInvest.getDueDt());
        } else if (holder instanceof ProgressViewHolder) {
            if (position > maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 5) {
                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
            } else {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textRegularCount.setText(maxValue + "笔");
            if (mType == 1) {
                ((HeaderViewHolder) holder).frameCapital.setVisibility(View.GONE);
            } else {
                ((HeaderViewHolder) holder).frameCapital.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).textCapital.setText("待收本金：" + mReg.getRegTotalAmt());
                ((HeaderViewHolder) holder).textEarning.setText("待收收益：" + mReg.getRegTotalIncome());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size() + 2;//+2 头部：定期投资的笔数 和 尾部：加载更多
        }
        return 0;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bdName;
        public ImageView imageProjectStatus;
        public ImageView imageProjectType;

        public TextView textCapital;
        public TextView textCapitalMoney;
        public TextView textEarning;
        public TextView textInterestRate;
        public LinearLayout frameEarning;

        public TextView textDateSubscribe;
        public TextView textDateOver;

        public ViewHolder(View itemView) {
            super(itemView);
            bdName = (TextView) itemView.findViewById(R.id.text_name);
            imageProjectStatus = (ImageView) itemView.findViewById(R.id.image_project_status);
            imageProjectType = (ImageView) itemView.findViewById(R.id.image_project_type);

            textCapital = (TextView) itemView.findViewById(R.id.text_capital);
            textCapitalMoney = (TextView) itemView.findViewById(R.id.text_capital_money);
            textEarning = (TextView) itemView.findViewById(R.id.text_earning);
            textInterestRate = (TextView) itemView.findViewById(R.id.text_interest_rate);
            frameEarning = (LinearLayout) itemView.findViewById(R.id.frame_earning);

            textDateSubscribe = (TextView) itemView.findViewById(R.id.text_date_subscribe);
            textDateOver = (TextView) itemView.findViewById(R.id.text_date_over);

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
        public RelativeLayout frameCapital;

        public HeaderViewHolder(View view) {
            super(view);
            textRegularCount = (TextView) view.findViewById(R.id.text_regular_count);
            textCapital = (TextView) view.findViewById(R.id.text_capital);
            textEarning = (TextView) view.findViewById(R.id.text_earning);
            frameCapital = (RelativeLayout) view.findViewById(R.id.frame_capital);
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


