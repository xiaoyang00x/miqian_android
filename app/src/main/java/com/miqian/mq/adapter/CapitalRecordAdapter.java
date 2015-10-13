package com.miqian.mq.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CapitalRecord;

import java.util.List;

/**
 * Created by sunyong on 9/23/15.
 */
public class CapitalRecordAdapter extends RecyclerView.Adapter {
    List<CapitalRecord.CapitalItem> list;
    private int visibleThreshold = 0;
    private int lastVisibleItem, totalItemCount;

    public CapitalRecordAdapter(List<CapitalRecord.CapitalItem> list, RecyclerView recyclerView) {
        this.list = list;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager =
                    (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    //todo  如果最后一个item的高度小于屏幕的高度。则不执行加载更多
                    //DisplayMetrics displaymetrics = new DisplayMetrics();
                    //((Activity)recyclerView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    //int height = displaymetrics.heightPixels;
                    //if(linearLayoutManager.getChildAt(lastVisibleItem).getHeight() < height){
                    //  return ;
                    //}
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                    visibleThreshold = 1;
                }
            });
        }
    }

    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1 && list.get(position) == null) {
            return VIEW_PROG;
        }
        return VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (VIEW_PROG == viewType) {
            View v =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

            return new ProgressViewHolder(v);
        }
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View v = mInflater.inflate(R.layout.activity_capital_record_row, parent, false);
        return new ItemVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (list.get(position) != null) {
             CapitalRecord.CapitalItem  item= list.get(position);
            if (!TextUtils.isEmpty(item.getTraAmt())){
                ((ItemVH) vh).tvMoney.setText(item.getTraAmt());
            }
            if (!TextUtils.isEmpty(item.getPeerCustLoginNm())){
                ((ItemVH) vh).tvPeerCustLoginNm.setText(item.getPeerCustLoginNm());
            }
            if (!TextUtils.isEmpty(item.getTraOpNm())){
                ((ItemVH) vh).tvTraOpNm.setText(item.getTraOpNm());
            }
            if (!TextUtils.isEmpty(item.getTraDt())&&(!TextUtils.isEmpty(item.getTraTm()))){
                ((ItemVH) vh).tvTime.setText(item.getTraDt()+" "+item.getTraTm());
                if (!TextUtils.isEmpty(item.getRem())){
                    ((ItemVH) vh).tvTime.setText(item.getTraDt()+" "+item.getTraTm()+"("+item.getRem()+")");
                }
            }


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemVH extends RecyclerView.ViewHolder {
        TextView tvTraOpNm, tvPeerCustLoginNm, tvMoney,tvTime;

        public ItemVH(View v) {
            super(v);
            tvTraOpNm = (TextView) v.findViewById(R.id.tv_traOpNm);
            tvPeerCustLoginNm = (TextView) v.findViewById(R.id.tv_peerCustLoginNm);
            tvMoney = (TextView) v.findViewById(R.id.money);
            tvTime = (TextView) v.findViewById(R.id.time_rem);
        }
    }

    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public static interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
