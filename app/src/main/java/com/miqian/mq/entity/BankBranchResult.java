package com.miqian.mq.entity;

import java.util.List;

public class BankBranchResult extends Meta {
	
	private List<BankBranch> data;

	public List<BankBranch> getData() {
		return data;
	}

	public void setData(List<BankBranch> data) {
		this.data = data;
	}
}
