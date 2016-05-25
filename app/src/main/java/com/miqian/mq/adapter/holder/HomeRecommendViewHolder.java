package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.CyclePagerAdapter;
import com.miqian.mq.entity.HomeAdData;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.HomeRecommendData;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 16/5/23.
 * 首页热门推荐
 */
public class HomeRecommendViewHolder extends HomeBaseViewHolder {

    private ViewPager mViewPager;
    private TextView tv_lable;

    public HomeRecommendViewHolder(View itemView) {
        super(itemView);

        mViewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
        tv_lable = (TextView) itemView.findViewById(R.id.tv_lable);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        int width = itemView.getResources().getDisplayMetrics().widthPixels;
        params.height = width * 338 / 586;
        mViewPager.setLayoutParams(params);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageMargin(Uihelper.dip2px(itemView.getContext(), 10));

        itemView.findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

    }

    @Override
    public void bindView(HomePageInfo data) {
        if(data != null) {
            if(TextUtils.isEmpty(data.getTitle())) {
                tv_lable.setVisibility(View.GONE);
            }else {
                tv_lable.setText(data.getTitle());
                tv_lable.setVisibility(View.VISIBLE);
            }
            if(data.getHotRecommendData() == null) return;
            mViewPager.setAdapter(new AdCyclePagerAdapter(itemView.getContext(), mViewPager, data.getHotRecommendData()));
            if(data.getHotRecommendData().size() > 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }

    class AdCyclePagerAdapter extends CyclePagerAdapter<HomeRecommendData> {

        private LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public AdCyclePagerAdapter(Context context, ViewPager viewPager, ArrayList<HomeRecommendData> dataList) {
            super(context, viewPager, dataList);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        @Override
        public View instantiateItem(Context mContext, HomeRecommendData data) {
            final ImageView imageView = new ImageView(mContext);
            imageLoader.displayImage(data.getImgUrl(), imageView, options);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setTag(data.getJumpUrl());// 绑定imageview 视图
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(imageView.getContext(), imageView.getTag().toString());
                }
            });
            return imageView;
        }
    }

}
