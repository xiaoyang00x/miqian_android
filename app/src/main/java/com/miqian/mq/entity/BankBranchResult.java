package com.miqian.mq.entity;

import java.util.List;

public class BankBranchResult{
	
	private List<BankBranch> list;
	private Page  pageInfo;

	public Page getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Page pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<BankBranch> getList() {
		return list;
	}

	public void setList(List<BankBranch> list) {
		this.list = list;
	}
}
