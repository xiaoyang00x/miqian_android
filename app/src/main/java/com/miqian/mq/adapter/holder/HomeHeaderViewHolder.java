package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdPageAdapter;
import com.miqian.mq.entity.HomeAdData;
import com.miqian.mq.utils.Uihelper;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 15/10/13.
 */
public class HomeHeaderViewHolder extends RecyclerView.ViewHolder {

    private ViewPager mHomeViewpager;
    private LinearLayout layout_indicator;
    private ImageView[] imageViews;
    private ArrayList<HomeAdData> mDataList;
    private HomeAdPageAdapter mAdpagerAdapter;
    private int currentPageState = ViewPager.SCROLL_STATE_IDLE;// Viewpager状态

    public HomeHeaderViewHolder(View itemView, ArrayList<HomeAdData> mDataList) {
        super(itemView);
        this.mDataList = mDataList;
        mHomeViewpager = (ViewPager) itemView.findViewById(R.id.vp_home_viewpager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mHomeViewpager.getLayoutParams();
        int width = itemView.getResources().getDisplayMetrics().widthPixels;
        params.height = width * 440 / 720;
        mHomeViewpager.setLayoutParams(params);
        layout_indicator = (LinearLayout) itemView.findViewById(R.id.layout_indicator);
        setIndicators(mDataList.size(), layout_indicator.getContext());
        initAdapter(itemView);
    }

    public void setIndicators(int amount, Context ctx) {
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
            layout_indicator.addView(imageView);
        }
    }

    public void initAdapter(View view) {
        mAdpagerAdapter = new HomeAdPageAdapter(view.getContext(), mDataList);
        mHomeViewpager = (ViewPager) view.findViewById(R.id.vp_home_viewpager);
        mHomeViewpager.setAdapter(mAdpagerAdapter);
        if (mDataList.size() != 0) {
            int maxSize = 65535;
            int pos = (maxSize / 2) - (maxSize / 2) % mDataList.size(); // 计算初始位置
            mHomeViewpager.setOnPageChangeListener(new MyOnPageChangeListener());
            mHomeViewpager.setCurrentItem(pos, true);
        }
    }

    /**
     * 首页焦点图滑动监听
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            currentPageState = state;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int index) {
            setImageBackground(index % mAdpagerAdapter.getItemCount());
        }
    }

    /**
     * 设置滑动图片时的背景图
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < imageViews.length; i++) {
            if (null == imageViews[i]) {
                return;
            }

            if (i == selectItems) {
                imageViews[i].setBackgroundResource(R.drawable.home_indicator_selected);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.home_indicator_normal);
            }
        }
    }

    public static final int MSG_ACTION_SLIDE_PAGE = 999;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ACTION_SLIDE_PAGE:
                    if (currentPageState == ViewPager.SCROLL_STATE_IDLE) {
                        //Fixed by keen: NullPointerException
                        if (null != mHomeViewpager && mHomeViewpager.getAdapter() != null && mHomeViewpager.getAdapter().getCount() > 1) {
                            int current = mHomeViewpager.getCurrentItem();
                            // 获取当前的索引加一，以滑动到下一项
                            mHomeViewpager.setCurrentItem((current + 1), true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
