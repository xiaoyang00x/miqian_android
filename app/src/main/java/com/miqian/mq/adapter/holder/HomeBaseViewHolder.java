package com.miqian.mq.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miqian.mq.entity.HomePageInfo;

/**
 * Created by guolei_wang on 16/5/24.
 */
public abstract class HomeBaseViewHolder extends  RecyclerView.ViewHolder {

    public HomeBaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(HomePageInfo mData);
}
