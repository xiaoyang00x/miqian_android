package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
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
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 16/5/23.
 * 首页广告轮播图
 */
public class HomeAdViewHolder extends HomeBaseViewHolder implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private LinearLayout mIndicator;
    private int currentPageState = ViewPager.SCROLL_STATE_IDLE;// Viewpager状态


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
        if (mData != null && mData.getBsAdListData() != null) {

            mViewPager.setAdapter(new AdCyclePagerAdapter(itemView.getContext(), mViewPager, mIndicator, mData.getBsAdListData(), this));

            if (mData.getBsAdListData().size() > 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        currentPageState = state;
    }


    class AdCyclePagerAdapter extends CyclePagerAdapter<HomeAdData> {
        private LinearLayout mIndicator;
        private ImageView[] imageViews;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;
        private ViewPager.OnPageChangeListener pageChangeListener;

        public AdCyclePagerAdapter(Context context, ViewPager viewPager, LinearLayout mIndicator, ArrayList<HomeAdData> dataList, ViewPager.OnPageChangeListener pageChangeListener) {
            super(context, viewPager, dataList);
            imageLoader = ImageLoader.getInstance();
            this.mIndicator = mIndicator;
            this.pageChangeListener = pageChangeListener;
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();

            setIndicators(dataList.size(), mIndicator.getContext());
        }

        @Override
        public View instantiateItem(Context mContext, HomeAdData data) {
            final ImageView imageView = new ImageView(mContext);
            imageLoader.displayImage(data.getImgUrl(), imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    ((ImageView) view).setImageResource(R.drawable.bg_ad_default);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    ((ImageView) view).setImageResource(R.drawable.bg_ad_default);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setTag(data.getJumpUrl() == null ? "" : data.getJumpUrl());// 绑定imageview 视图
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(imageView.getContext(), "1002");
                    WebActivity.startActivity(imageView.getContext(), imageView.getTag().toString());
                }
            });
            return imageView;
        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.d("onPageSelected begin child.position = " + position);
            super.onPageSelected(position);
            setImageBackground(getRealDataPosition(position));
            LogUtil.d("onPageSelected getRealDataPosition(position) = " + getRealDataPosition(position));
            LogUtil.d("onPageSelected end child.position = " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            pageChangeListener.onPageScrollStateChanged(state);
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


    }

    public static final int MSG_ACTION_SLIDE_PAGE = 999;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ACTION_SLIDE_PAGE:
                    if (currentPageState == ViewPager.SCROLL_STATE_IDLE) {
                        //Fixed by keen: NullPointerException
                        if (null != mViewPager && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 1) {
                            int current = mViewPager.getCurrentItem();
                            // 获取当前的索引加一，以滑动到下一项
                            mViewPager.setCurrentItem((current + 1), true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
