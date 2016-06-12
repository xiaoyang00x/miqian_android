package com.miqian.mq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public BankBranchAdapter(List<BankBranch> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bankbranch, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
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
        BankBranch bankBranch = items.get(position);
        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_bank_branch.setText(bankBranch.getShortBranchName());
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


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
