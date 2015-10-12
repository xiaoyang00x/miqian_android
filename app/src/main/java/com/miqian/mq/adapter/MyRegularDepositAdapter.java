package com.miqian.mq.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.UserRegular;

import java.io.Serializable;
import java.util.List;

public class MyRegularDepositAdapter extends RecyclerView.Adapter {
    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_HEADER = 2;

    private List<UserRegular.RegInvest> mList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyRegularDepositAdapter(List<UserRegular.RegInvest> list, RecyclerView recyclerView) {
        this.mList = list;

//    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//
//      final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//          super.onScrolled(recyclerView, dx, dy);
//
//          totalItemCount = linearLayoutManager.getItemCount();
//          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//          if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//            // End has been reached
//            // Do something
//            if (onLoadMoreListener != null) {
//              onLoadMoreListener.onLoadMore();
//            }
//            loading = true;
//          }
//        }
//      });
//    }
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return VIEW_HEADER;
        }
        return mList.get(position - 1) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_regular_deposit_item_row, parent, false);
            viewHolder = new ViewHolder(view);
        } else if (viewType == VIEW_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_regular_deposit_item_row_header, parent, false);
            viewHolder = new ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            UserRegular.RegInvest regInvest = mList.get(position - 1);
            ((ViewHolder) holder).bdName.setText(regInvest.getBdNm());
        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size() + 1;
        }
        return 1;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bdName;

        public ViewHolder(View itemView) {
            super(itemView);
            bdName = (TextView) itemView.findViewById(R.id.textView6);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public ItemViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.textView6);
        }
    }

    public static class HeadView extends RecyclerView.ViewHolder {
        public HeadView(View view) {
            super(view);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public static interface OnLoadMoreListener {
        void onLoadMore();
    }
}


