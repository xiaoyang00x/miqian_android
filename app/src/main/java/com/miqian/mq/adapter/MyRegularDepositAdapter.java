package com.miqian.mq.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miqian.mq.R;
import java.io.Serializable;
import java.util.List;

public class MyRegularDepositAdapter extends RecyclerView.Adapter {
  private final int VIEW_PROG = 0;
  private final int VIEW_ITEM = 1;
  private final int VIEW_HEADER = 2;

  private List<Item> items;

  // The minimum amount of items to have below your current scroll position
  // before loading more.
  private int visibleThreshold = 5;
  private int lastVisibleItem, totalItemCount;
  private boolean loading;
  private OnLoadMoreListener onLoadMoreListener;

  public MyRegularDepositAdapter(List<Item> items, RecyclerView recyclerView) {
    this.items = items;

    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

      final LinearLayoutManager linearLayoutManager =
          (LinearLayoutManager) recyclerView.getLayoutManager();

      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          totalItemCount = linearLayoutManager.getItemCount();
          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            if (onLoadMoreListener != null) {
              onLoadMoreListener.onLoadMore();
            }
            loading = true;
          }
        }
      });
    }
  }

  @Override public int getItemViewType(int position) {
    if (0 == position) {
      return VIEW_HEADER;
    }
    return items.get(position - 1) != null ? VIEW_ITEM : VIEW_PROG;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Log.e("keen", "onCreateViewHolder == " + viewType);
    RecyclerView.ViewHolder vh;
    if (viewType == VIEW_ITEM) {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.my_regular_deposit_item_row, parent, false);
      vh = new ItemViewHolder(v);
    } else if (viewType == VIEW_HEADER) {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.my_regular_deposit_item_row_header, parent, false);

      vh = new ItemViewHolder(v);
    } else {
      View v =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

      vh = new ProgressViewHolder(v);
    }
    return vh;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemViewHolder) {

      //Student singleStudent= (Student) studentList.get(position);
      //
      //((StudentViewHolder) holder).tvName.setText(singleStudent.getName());
      //
      //((StudentViewHolder) holder).tvEmailId.setText(singleStudent.getEmailId());
      //
      //((StudentViewHolder) holder).student= singleStudent;

    } else {
      ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }
  }

  public void setLoaded() {
    loading = false;
  }

  @Override public int getItemCount() {
    return items.size() + 1;
  }

  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.onLoadMoreListener = onLoadMoreListener;
  }

  //
  public static class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;

    public TextView tvEmailId;

    public Item item;

    public ItemViewHolder(View v) {
      super(v);
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

  public static class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public Item() {

    }

    public Item(String name, String emailId) {
      this.name = name;
      this.emailId = emailId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getEmailId() {
      return emailId;
    }

    public void setEmailId(String emailId) {
      this.emailId = emailId;
    }
  }

  public static interface OnLoadMoreListener {
    void onLoadMore();
  }
}


