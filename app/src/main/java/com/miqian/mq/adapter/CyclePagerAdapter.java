package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by guolei_wang on 16/5/23.
 * ViewPager实现无限循环了逻辑如下
 * --数据下标-----------------1------2-------3-----4-------------
 * --Views下标---------0-----1------2-------3-----4-----5-------
 */
public abstract class CyclePagerAdapter<E> extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ArrayList<E> mDataList;
    private ViewPager mViewPager;

    public CyclePagerAdapter(Context context, ViewPager viewPager, ArrayList<E> dataList) {
        mContext = context;
        mViewPager = viewPager;
        mDataList = dataList;
        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    public int getCount() {
        return getViewsCount();
    }

    /**
     * @return ViewPager所包含的实际的 View 的数量
     */
    private int getViewsCount() {
        int count = mDataList == null ? 0 : mDataList.size();
        if (count > 1) {
            return count + 2;
        }

        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = instantiateItem(mContext, mDataList.get(getRealDataPosition(position)));
        container.addView(view);
        return view;
    }

    public abstract View instantiateItem(Context mContext, E data);

    /**
     * @param position view 所在的 position 从 0 开始
     * @return 返回所对应的数据在数组中的真实位置  从0开始
     */
    private int getRealDataPosition(int position) {
        int realDataPositon = 0;
        if (mDataList.size() > 1) {
            if (position == 0) {
                realDataPositon = mDataList.size() - 1;
            } else if (position <= mDataList.size()) {
                realDataPositon = position - 1;
            } else {
                realDataPositon = 0;
            }
        }
        return realDataPositon;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (getViewsCount() > 1) {
            if (position < 1) {
                position = mDataList.size();
                mViewPager.setCurrentItem(position, false);
            } else if (position > mDataList.size()) {
                position = 1;
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
