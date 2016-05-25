package com.miqian.mq.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.miqian.mq.R;
import com.miqian.mq.adapter.holder.HomeAdViewHolder;
import com.miqian.mq.adapter.holder.HomeBaseViewHolder;
import com.miqian.mq.adapter.holder.HomeBulletinHolder;
import com.miqian.mq.adapter.holder.HomeContactViewHolder;
import com.miqian.mq.adapter.holder.HomeHeaderViewHolder;
import com.miqian.mq.adapter.holder.HomeHotActivitysViewHolder;
import com.miqian.mq.adapter.holder.HomeNewsViewHolder;
import com.miqian.mq.adapter.holder.HomeOperationHolder;
import com.miqian.mq.adapter.holder.HomeRecommendViewHolder;
import com.miqian.mq.adapter.holder.HomeSelectionHolder;
import com.miqian.mq.adapter.holder.RegularEarnViewHoder;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.entity.SubjectCategoryData;

import java.util.ArrayList;

/**
 * Created by guolei_wang
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeBaseViewHolder> {
    HomePageInfo info;
    Activity activity;
    ArrayList<HomePageInfo> mDatas;

    public HomeAdapter(Activity activity, ArrayList<HomePageInfo> datas) {
        this.activity = activity;
        mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        HomePageInfo info = getItem(position);
        if(info != null) {
            return info.getModule();
        }
        return -9999;
    }

    private HomePageInfo getItem(int position) {
        return mDatas == null? null : mDatas.get(position);
    }


    @Override
    public int getItemCount() {
        return mDatas == null? 0 : mDatas.size();
    }


    @Override
    public HomeBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HomePageInfo.MODULE_LOOP:
                return new HomeAdViewHolder(mInflater.inflate(R.layout.home_ad_recyclerview,parent, false));
            case HomePageInfo.MODULE_BULLETIN:
                return new HomeBulletinHolder(mInflater.inflate(R.layout.item_home_bulletin,parent, false));
            case HomePageInfo.MODULE_HOT_RECOMMEND:
                return new HomeRecommendViewHolder(mInflater.inflate(R.layout.item_home_recommend,parent, false));
            case HomePageInfo.MODULE_NEW_ACTIVITIES:
                return new HomeHotActivitysViewHolder(mInflater.inflate(R.layout.item_home_newer,parent, false));
            case HomePageInfo.MODULE_NEWS:
                return new HomeNewsViewHolder(mInflater.inflate(R.layout.item_home_news,parent, false));
            case HomePageInfo.MODULE_OPERATION_DATA:
                return new HomeOperationHolder(mInflater.inflate(R.layout.item_home_operation,parent, false));
            case HomePageInfo.MODULE_SELECTION:
                return new HomeSelectionHolder(mInflater.inflate(R.layout.item_home_selection,parent, false));
            case HomePageInfo.MODULE_TEL:
                return new HomeContactViewHolder(mInflater.inflate(R.layout.item_home_contact,parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(HomeBaseViewHolder viewHolder, int position) {
        HomePageInfo homePageInfo = getItem(position);
        if (homePageInfo != null) {
            viewHolder.bindView(homePageInfo);
        }
    }


    public void notifyDataSetChanged(HomePageInfo info) {
        this.info = info;
        notifyDataSetChanged();
    }

//    /**
//     * 设置图片自动滚动
//     */
//    public void setAutoScroll() {
//        if(homeHeaderViewHolder != null) {
//            homeHeaderViewHolder.handler.sendEmptyMessage(HomeHeaderViewHolder.MSG_ACTION_SLIDE_PAGE);
//        }
//    }
}