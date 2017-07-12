package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.miqian.mq.R;
import com.miqian.mq.activity.current.ActivityRedeemMqb;
import com.miqian.mq.entity.NewCurrentFoundFlow;
import com.miqian.mq.entity.Page;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 2017/7/6.
 * Email: gwzheng521@163.com
 * Description: 我的秒钱宝适配器
 */

public class AdapterUserMqb extends RecyclerView.Adapter {

    private final int VIEW_HEADER = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;
    private final Context mContext;
    private final Page mPage;
    private ArrayList<NewCurrentFoundFlow.InvestInfo> mList;

    private AdapterUserMqb.MyItemClickListener onItemClickListener;

    private int maxValue = 999;//最大的值

    private NewCurrentFoundFlow.NewCurrent newCurrentInfo;

    public AdapterUserMqb(Context context, ArrayList<NewCurrentFoundFlow.InvestInfo> list, NewCurrentFoundFlow.NewCurrent newCurrentInfo, Page page) {
        this.mContext = context;
        this.newCurrentInfo = newCurrentInfo;
        this.mPage = page;
        mList = list;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_new_current, parent, false);
            viewHolder = new AdapterUserMqb.ViewHolder(view);
        } else if (viewType == VIEW_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_mqb_header, parent, false);
            viewHolder = new AdapterUserMqb.HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new AdapterUserMqb.ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdapterUserMqb.ViewHolder) {

            final NewCurrentFoundFlow.InvestInfo info = mList.get(position - 1);
            if (info == null) {
                return;
            }
            ((ViewHolder) holder).tv_project_name.setText(info.getProductName());
            ((ViewHolder) holder).profit_rate.setText(info.getProductRate());
            ((ViewHolder) holder).tv_amount.setText(info.getPurchaseAmount());
            ((ViewHolder) holder).tv_buy_time.setText(FormatUtil.formatDate(info.getStartTime(), "认购时间：yyyy-MM-dd HH:mm:ss"));

            switch (info.getProductStatus()) {
                case NewCurrentFoundFlow.STATUS_01:
                    ((ViewHolder) holder).btn_buy.setEnabled(false);
                    ((ViewHolder) holder).btn_buy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_red));
                    ((ViewHolder) holder).btn_buy.setText("赎回");
                    ((ViewHolder) holder).lableView.setVisibility(View.VISIBLE);
                    break;
                case NewCurrentFoundFlow.STATUS_02:
                    ((ViewHolder) holder).btn_buy.setEnabled(false);
                    ((ViewHolder) holder).btn_buy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_no_begin));
                    ((ViewHolder) holder).btn_buy.setText("赎回中");
                    ((ViewHolder) holder).lableView.setVisibility(View.GONE);
                    break;
                default:
                    ((ViewHolder) holder).btn_buy.setEnabled(true);
                    ((ViewHolder) holder).btn_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityRedeemMqb.startActivity(mContext, info);
                        }
                    });
                    ((ViewHolder) holder).btn_buy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_red));
                    ((ViewHolder) holder).btn_buy.setText("赎回");
                    ((ViewHolder) holder).lableView.setVisibility(View.GONE);
                    break;
            }
        } else if (holder instanceof AdapterUserMqb.ProgressViewHolder) {
            if (position > maxValue) {
                ((AdapterUserMqb.ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 5) {
                    ((AdapterUserMqb.ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((AdapterUserMqb.ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
            } else {
                ((AdapterUserMqb.ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((AdapterUserMqb.ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        } else if (holder instanceof AdapterUserMqb.HeaderViewHolder) {
            ((HeaderViewHolder) holder).tv_earning.setText(newCurrentInfo.getLastdayProfit());
            ((HeaderViewHolder) holder).tv_captial.setText(newCurrentInfo.getPrnAmt());
            ((HeaderViewHolder) holder).tv_total_count.setText(String.valueOf(mPage.getTotalRecord()));
            ((HeaderViewHolder) holder).tv_total_earning.setText(newCurrentInfo.getInterest());
            ((HeaderViewHolder) holder).tv_interest.setText(newCurrentInfo.getYearRate());
        }
    }


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

        /**
         * 项目名称
         */
        public TextView tv_project_name;

        /**
         * 年化收益
         */
        public TextView profit_rate;

        /**
         * 认购金额
         */
        public TextView tv_amount;

        /**
         * 项目角标
         */
        public LabelView lableView;

        /**
         * 认购时间
         */
        public TextView tv_buy_time;

        /**
         * 赎回按钮
         */
        public Button btn_buy;

        public ViewHolder(View projectView) {
            super(projectView);
            tv_project_name = (TextView)projectView.findViewById(R.id.tv_project_name);
            profit_rate = (TextView)projectView.findViewById(R.id.profit_rate);
            tv_amount = (TextView)projectView.findViewById(R.id.tv_amount);
            lableView = (LabelView) projectView.findViewById(R.id.lableView);
            tv_buy_time = (TextView)projectView.findViewById(R.id.tv_buy_time);
            btn_buy = (Button) projectView.findViewById(R.id.btn_buy);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        /**
         * 昨日收益
         */
        public TextView tv_earning;

        /**
         * 在投本金
         */
        public TextView tv_captial;

        /**
         * 持有笔数
         */
        public TextView tv_total_count;

        /**
         * 在投收益
         */
        public TextView tv_total_earning;
        /**
         * 年化收益率
         */
        public TextView tv_interest;

        public HeaderViewHolder(View view) {
            super(view);
            tv_earning = (TextView) view.findViewById(R.id.earning);
            tv_captial = (TextView) view.findViewById(R.id.captial);
            tv_total_count = (TextView) view.findViewById(R.id.total_count);
            tv_total_earning = (TextView) view.findViewById(R.id.total_earning);
            tv_interest = (TextView) view.findViewById(R.id.text_interest);
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

    public void setOnItemClickListener(AdapterUserMqb.MyItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
