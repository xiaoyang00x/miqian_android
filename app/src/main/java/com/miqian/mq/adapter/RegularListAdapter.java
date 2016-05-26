package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.holder.RegularEarnViewHoder;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.entity.SubjectCategoryData;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.views.DecoratorViewPager;
import com.miqian.mq.views.indicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_NORMAL = 1;

    private ArrayList<RegularBaseData> regularBaseDatas = new ArrayList<>();
    private Context mContext;
    private boolean isHasHeader = false;

    public RegularListAdapter(GetRegularInfo info, Context mContext) {
        this.mContext = mContext;

        if (info.getSubjectData() != null) {
            for (int i = 0; i < info.getSubjectData().size(); i++) {
                SubjectCategoryData categoryData = info.getSubjectData().get(i);
                if (categoryData != null && categoryData.getSubjectInfo().size() > 0) {
                    categoryData.getSubjectInfo().get(0).setShowLable(true);
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryName(categoryData.getSubjectCategoryName());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryIconUrl(categoryData.getSubjectCategoryIconUrl());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryDesc(categoryData.getSubjectCategoryDesc());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryDescUrl(categoryData.getSubjectCategoryDescUrl());
                    regularBaseDatas.addAll(categoryData.getSubjectInfo());
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new RegularEarnViewHoder(inflater.inflate(R.layout.regular_earn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularEarnViewHoder) {
            final RegularBaseData regularEarn = regularBaseDatas.get(isHasHeader ? position - 1 : position);
            RegularEarnViewHoder viewHolder = (RegularEarnViewHoder) holder;
            viewHolder.bindView(mContext, regularEarn, regularEarn.isShowLable());
        }
    }

    @Override
    public int getItemCount() {
        int itemsCount = regularBaseDatas == null ? 0 : regularBaseDatas.size();
        return !isHasHeader ? itemsCount : itemsCount + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isHasHeader) {
            return ITEM_TYPE_NORMAL;
        }
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }

}
