package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.CyclePagerAdapter;
import com.miqian.mq.entity.HomeAdData;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 16/5/23.
 * 首页广告轮播图
 */
public class HomeAdViewHolder extends HomeBaseViewHolder {
    private ViewPager mViewPager;
    private LinearLayout mIndicator;
    private ImageView[] imageViews;

    public HomeAdViewHolder(View itemView) {
        super(itemView);

        mViewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        int width = itemView.getResources().getDisplayMetrics().widthPixels;
        params.height = width * 506 / 720;
        mViewPager.setLayoutParams(params);
        mIndicator = (LinearLayout) itemView.findViewById(R.id.layout_indicator);
    }

    @Override
    public void bindView(HomePageInfo mData) {
        if(mData != null && mData.getBsAdListData() !=  null) {
            setIndicators(mData.getBsAdListData().size(), mIndicator.getContext());
            mViewPager.setAdapter(new AdCyclePagerAdapter(itemView.getContext(), mViewPager, mData.getBsAdListData()));
        }
    }

    public void setIndicators(int amount, Context ctx) {
        mIndicator.removeAllViews();
        imageViews = new ImageView[amount];
        for (int i = 0; i < amount; i++) {
            ImageView imageView = new ImageView(ctx);
            final LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(Uihelper.dip2px(ctx, 8), Uihelper.dip2px(ctx, 8));
            lp.setMargins(0, 0, Uihelper.dip2px(ctx, 8), 0);
            imageView.setLayoutParams(lp);
            imageViews[i] = imageView;

            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.home_indicator_selected);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.home_indicator_normal);
            }
            mIndicator.addView(imageView);
        }
    }

    class AdCyclePagerAdapter extends CyclePagerAdapter<HomeAdData> {

        private LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public AdCyclePagerAdapter(Context context, ViewPager viewPager, ArrayList<HomeAdData> dataList) {
            super(context, viewPager, dataList);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        @Override
        public View instantiateItem(Context mContext, HomeAdData data) {
            final ImageView imageView = new ImageView(mContext);
            imageLoader.displayImage(data.getImgUrl(), imageView, options);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setTag(data.getJumpUrl());// 绑定imageview 视图
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(imageView.getContext(), "1002");
                    WebActivity.startActivity(imageView.getContext(), imageView.getTag().toString());
                }
            });
            return imageView;
        }
    }

}
