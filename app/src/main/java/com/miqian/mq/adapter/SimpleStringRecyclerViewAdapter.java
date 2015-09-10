package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miqian.mq.R;
import com.miqian.mq.views.MaterialProgressBarSupport;
import java.util.List;

/**
 * Created by sunyong on 9/6/15.
 */
public class SimpleStringRecyclerViewAdapter
    extends RecyclerView.Adapter {
  private static final int TYPE_HEADER = Integer.MIN_VALUE;
  private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
  private static final int TYPE_ADAPTEE_OFFSET = 2;
  private List<String> mValues;
  private boolean hasFooter;

  private boolean hasMoreData;

  public int getBasicItemCount() {
    return mValues.size();
  }

  public static class FooterViewHolder extends RecyclerView.ViewHolder {
    public final MaterialProgressBarSupport mProgressView;
    public final TextView mTextView;

    public FooterViewHolder(View view) {
      super(view);
      mProgressView = (MaterialProgressBarSupport) view.findViewById(R.id.progress_view);
      mTextView = (TextView) view.findViewById(R.id.tv_content);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mTextView.getText();
    }
  }

  public String getValueAt(int position) {
    return mValues.get(position);
  }

  public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
    mValues = items;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_view_load_more, parent, false);
      return new FooterViewHolder(view);

  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

      //没有更多数据
      if(hasMoreData){
        ((FooterViewHolder) holder).mProgressView.setVisibility(View.VISIBLE);
        ((FooterViewHolder) holder).mProgressView.startProgress();
        //((FooterViewHolder) holder).mProgressView.setIndeterminate(true);
        ((FooterViewHolder) holder).mTextView.setText("load more ...");
      } else {
        ((FooterViewHolder) holder).mProgressView.stopProgress();
        ((FooterViewHolder) holder).mProgressView.setVisibility(View.GONE);
        //((FooterViewHolder) holder).mProgressView.st;
        ((FooterViewHolder) holder).mTextView.setText("没有更多数据了");
      }

  }

  @Override
  public int getItemCount() {
    return mValues.size() + (hasFooter ? 1 : 0);
  }

  @Override
  public int getItemViewType(int position) {

    if (position == getBasicItemCount() && hasFooter) {
      return TYPE_FOOTER;
    }
    return super.getItemViewType(position);
  }


  public boolean hasFooter() {
    return hasFooter;
  }

  public void setHasFooter(boolean hasFooter) {
    if(this.hasFooter != hasFooter) {
      this.hasFooter = hasFooter;
      notifyDataSetChanged();
    }
  }


  public boolean hasMoreData() {
    return hasMoreData;
  }

  public void setHassMoreData(boolean isMoreData) {
    if(this.hasMoreData != isMoreData) {
      this.hasMoreData = isMoreData;
      notifyDataSetChanged();
    }
  }
  public void setHasMoreDataAndFooter(boolean hasMoreData, boolean hasFooter) {
    if(this.hasMoreData != hasMoreData || this.hasFooter != hasFooter) {
      this.hasMoreData = hasMoreData;
      this.hasFooter = hasFooter;
      notifyDataSetChanged();
    }
  }
}