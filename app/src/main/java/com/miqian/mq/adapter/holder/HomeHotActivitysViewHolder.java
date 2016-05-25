package com.miqian.mq.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.HomeNewActivitiesData;
import com.miqian.mq.entity.HomeNewsData;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页热门活动
 */
public class HomeHotActivitysViewHolder extends HomeBaseViewHolder {

    private LinearLayout layout_container;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int imageViewWidth;
    private int imageViewHeight;
    private LayoutInflater inflater;
    public HomeHotActivitysViewHolder(View itemView) {
        super(itemView);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();

        imageViewWidth = itemView.getResources().getDisplayMetrics().widthPixels;
        imageViewHeight = imageViewWidth * 338 / 720;
        layout_container = (LinearLayout) itemView.findViewById(R.id.layout_container);
        inflater = LayoutInflater.from(itemView.getContext());


    }

    @Override
    public void bindView(HomePageInfo mData) {
        layout_container.removeAllViews();

        if(mData != null && mData.getHotActivityData() != null) {

            if(mData.getHotActivityData().size() > 0) {
                for(int i = 0; i < mData.getHotActivityData().size(); i++) {
                    HomeNewActivitiesData homeNewActivitiesData = mData.getHotActivityData().get(i);
                    View activityView = inflater.inflate(R.layout.item_home_activity, null);
                    final ImageView imageView = (ImageView) activityView.findViewById(R.id.imageView);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageViewWidth, imageViewHeight);
                    params.setMargins(0, 0, 0, Uihelper.dip2px(activityView.getContext(), 10));

                    imageLoader.displayImage(homeNewActivitiesData.getImgUrl(), imageView, options);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setTag(homeNewActivitiesData.getJumpUrl());// 绑定imageview 视图
                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            WebActivity.startActivity(imageView.getContext(), imageView.getTag().toString());
                        }
                    });
                    layout_container.addView(activityView, params);
                }
            }
        }
    }
}
