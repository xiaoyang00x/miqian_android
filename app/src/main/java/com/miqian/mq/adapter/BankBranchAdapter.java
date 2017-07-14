package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.BankBranch;

import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 */
public class BankBranchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BankBranch> items;
    private static MyItemClickListener mItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;
    private int maxValue = 999;//最大的值

    public BankBranchAdapter(List<BankBranch> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bankbranch, parent, false);
            viewHolder = new ViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return VIEW_FOOTER;
        } else {
            return VIEW_ITEM;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_bank_branch;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, getPosition());
                }
            });
            tv_bank_branch = (TextView) itemView.findViewById(R.id.tv_bank_branch);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            BankBranch bankBranch = items.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_bank_branch.setText(bankBranch.getBranchName());
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            if (position+1>maxValue) {
                viewHolder.progressBar.setVisibility(View.GONE);
                if (maxValue <= 15) {
                    viewHolder.textLoading.setVisibility(View.GONE);
                } else {
                    viewHolder.textLoading.setText("没有更多");
                }
            } else {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.textLoading.setText("加载更多");
            }
        }

    }

    public void addAll(List<BankBranch> bankBranches) {
        items.addAll(bankBranches);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null && items.size() != 0) {
            return items.size() + 1;//+1 尾部：加载更多
        }
        return items.size();
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
