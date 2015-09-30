package com.miqian.mq.views.sortlist;


import com.miqian.mq.entity.CityInfo;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CityInfo> {

	public int compare(CityInfo o1, CityInfo o2) {
		if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
			return -1;
		} else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
			return 1;
		} else {
			return o1.getLetter().compareTo(o2.getLetter());
		}
	}

}
