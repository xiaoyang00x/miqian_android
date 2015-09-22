package com.miqian.mq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.miqian.mq.fragment.RegularPlanFragment;

/**
 * Created by guolei_wang on 15/9/19.
 */
public class RegularViewPagerAdapter extends FragmentStatePagerAdapter {

    public RegularViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Fragment getItem(int position) {
        return new RegularPlanFragment();
    }
}
