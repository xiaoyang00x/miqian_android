/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.miqian.mq.views.wheelview;


import com.miqian.mq.entity.BankInfo;

import java.util.List;

/**
 * The simple Array wheel adapter
 * 
 * @param <T>
 *            the element type
 */
public class ListWheelAdapter implements WheelAdapter {

	/** The default items length */
	public static final int DEFAULT_LENGTH = -1;

	// items
	// private T items[];
	private List<BankInfo> list;

	// length
	// private int length;

	public ListWheelAdapter(List<BankInfo> list) {
		this.list = list;
	}

	@Override
	public String getItem(int index) {
		if (list != null && index >= 0 && index < list.size()) {
			BankInfo bankInfo = list.get(index);
			return bankInfo.getValue();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		if (list != null && list.size() > 0) {
			return list.size();
		}
		return 0;
	}

	@Override
	public int getMaximumLength() {
		if (list != null && list.size() > 0) {
			return list.size();
		}
		return 0;
	}

}
