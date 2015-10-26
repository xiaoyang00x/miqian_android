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
import com.miqian.mq.entity.ProjectInfo;
import com.miqian.mq.entity.UserRegular;

import java.util.List;

public class AdapterProjectMatch extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_FOOTER = 2;

    private int maxValue = 999;//最大的值

//    private List<UserRegular.RegInvest> mList;
    private List<ProjectInfo> mList;

    public AdapterProjectMatch(List<ProjectInfo> list) {
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return VIEW_FOOTER;
        } else {
            return VIEW_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_project_match, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ProjectInfo projectInfo = mList.get(position);
            ((ViewHolder) holder).textName.setText(projectInfo.getBdNm());
        } else if (holder instanceof ProgressViewHolder) {
            if (position >= maxValue) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
                if (maxValue <= 15) {
                    ((ProgressViewHolder) holder).textLoading.setVisibility(View.GONE);
                } else {
                    ((ProgressViewHolder) holder).textLoading.setText("没有更多");
                }
            } else {
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).textLoading.setText("加载更多");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() != 0) {
            return mList.size() + 1;//+1 尾部：加载更多
        }
        return 0;
    }

    public void setMaxItem(int value) {
        maxValue = value;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_name);
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
}


