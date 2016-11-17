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
import com.miqian.mq.entity.SubscriptionRecords;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;

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
        return new ProductsListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_subscription_records, parent, false));
    }

    @Override
    public int getItemCount() {
        return dataList != null? dataList.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        boolean showLine = true;
        if(position == getItemCount() - 1) {
            showLine = false;
        }else {
            showLine = true;
        }

        ((ProductsListHolder) holder).bindData(dataList.get(position), showLine);

    }


    private class ProductsListHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name;                               // 名称
        private TextView tv_amount;                             // 本金
        private TextView tv_time;                               // 认购时间
        private View line;                                      // 分割线

        public ProductsListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            line = itemView.findViewById(R.id.line);

        }

        public void bindData(SubscriptionRecords.Products info, boolean showLine) {
            if(info == null) return;
            tv_name.setText(info.getSubjectName());
            tv_amount.setText(FormatUtil.formatAmount(info.getRemainAmount()));
            tv_time.setText(FormatUtil.formatDate(info.getTradeTime(), "yyyy-MM-dd\nHH:mm"));
            line.setVisibility(showLine? View.VISIBLE : View.GONE);
        }

    }
}
