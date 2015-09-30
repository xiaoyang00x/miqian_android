package com.miqian.mq.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
  List<CapitalRecord.Item> list;
  private int visibleThreshold = 0;
  private int lastVisibleItem, totalItemCount;

  public CapitalRecordAdapter(List<CapitalRecord.Item> list, RecyclerView recyclerView) {
    this.list = list;

    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

      final LinearLayoutManager linearLayoutManager =
          (LinearLayoutManager) recyclerView.getLayoutManager();

      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

  @Override public int getItemViewType(int position) {
    if (position == list.size() - 1 && list.get(position) == null) {
      return VIEW_PROG;
    }
    return VIEW_ITEM;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (VIEW_PROG == viewType) {
      View v =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

      return new ProgressViewHolder(v);
    }
    LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
    View v = mInflater.inflate(R.layout.activity_capital_record_row, parent, false);
    return new ItemVH(v);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
    //vh.title.setText(list.get(position).getTitle());
    //vh.sub_title.setText(list.get(position).getSub_title());
    if (list.get(position) != null) ((ItemVH) vh).money.setText(list.get(position).getTraAmt());

    if (position == 5) {
      ((ItemVH) vh).money.setText(9.09 + "");
    }
    //vh.time.setText(list.get(position).getTime());

  }

  @Override public int getItemCount() {
    return list.size();
  }

  public static class ItemVH extends RecyclerView.ViewHolder {
    TextView title, sub_title, money, time;

    public ItemVH(View v) {
      super(v);
      title = (TextView) v.findViewById(R.id.title);
      sub_title = (TextView) v.findViewById(R.id.sub_title);
      money = (TextView) v.findViewById(R.id.money);
      time = (TextView) v.findViewById(R.id.time);
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
      progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
    }
  }
}
