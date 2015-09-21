package com.miqian.mq.views.sortlist;


import com.miqian.mq.entity.CityInfo;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CityInfo> {

	public int compare(CityInfo o1, CityInfo o2) {
		if (o1.getBegin_letter().equals("@") || o2.getBegin_letter().equals("#")) {
			return -1;
		} else if (o1.getBegin_letter().equals("#") || o2.getBegin_letter().equals("@")) {
			return 1;
		} else {
			return o1.getBegin_letter().compareTo(o2.getBegin_letter());
		}
	}

}
