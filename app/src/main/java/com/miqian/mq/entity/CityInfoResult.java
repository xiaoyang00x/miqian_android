package com.miqian.mq.entity;

import java.util.List;

public class CityInfoResult extends Meta {

	private List<CityInfo> data;

	public List<CityInfo> getData() {
		return data;
	}

	public void setData(List<CityInfo> data) {
		this.data = data;
	}
}
