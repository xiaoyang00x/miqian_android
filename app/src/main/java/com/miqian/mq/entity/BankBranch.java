package com.miqian.mq.entity;

public class BankBranch {
	
	private String provinceName;
	private String city;
	private String bankName;
	private String branchName;
	private String bankCode;
	private String shortBranchName;

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getShortBranchName() {
		return shortBranchName;
	}

	public void setShortBranchName(String shortBranchName) {
		this.shortBranchName = shortBranchName;
	}
}
