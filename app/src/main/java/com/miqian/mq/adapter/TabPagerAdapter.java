package com.miqian.mq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> list;
	
	public TabPagerAdapter(FragmentManager fragmentManager, List<Fragment> list) {
		super(fragmentManager);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		if (list != null) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}
}
