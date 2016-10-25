package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CurSubRecord;
import com.miqian.mq.entity.SubscriptionRecords;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgl on 2016/10/25.
 * 我的秒钱宝认购记录适配器
 */
public class SubscriptionRecordsAdapter extends RecyclerView.Adapter {

    private ArrayList<SubscriptionRecords.Products> dataList;
    private Context mContext;

    public SubscriptionRecordsAdapter(Context context, ArrayList<SubscriptionRecords.Products> dataList) {
        this.dataList = dataList;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductsListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_current_match, parent, false));
    }

    @Override
    public int getItemCount() {
        return dataList != null? dataList.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ProductsListHolder) holder).bindData(dataList.get(position - 1));

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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textLoading;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            textLoading = (TextView) view.findViewById(R.id.text_loading);
        }
    }

    private class ProductsListHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name;                               // 名称
        private TextView tv_principal;                          // 本金
        private TextView tv_collect_earnings;                   // 待收收益
        private TextView tv_next_due_date;                      // 下一还款日
        private TextView tv_view_notes;                         // 查看认购记录

        public ProductsListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_principal = (TextView) itemView.findViewById(R.id.tv_principal);
            tv_collect_earnings = (TextView) itemView.findViewById(R.id.tv_collect_earnings);
            tv_next_due_date = (TextView) itemView.findViewById(R.id.tv_next_due_date);
            tv_view_notes = (TextView) itemView.findViewById(R.id.tv_view_notes);

            tv_view_notes.setVisibility(View.GONE);
        }

        public void bindData(SubscriptionRecords.Products info) {
            if(info == null) return;
            tv_name.setText(info.getSubjectName());
            tv_principal.setText(FormatUtil.formatAmount(info.getRemainAmount()));
            tv_collect_earnings.setText(FormatUtil.formatAmount(info.getRemainInterest()));
            tv_next_due_date.setText(TextUtils.concat("认购时间:", FormatUtil.formatDate(info.getTradeTime(), "yyyy年MM月dd日  HH:mm:ss")));
        }

    }
}
