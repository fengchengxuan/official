package com.eastrobot.robotdev.vo;

import net.sf.json.JSONArray;

public class Copy_2_of_PersonalData {
	private String distId;
	private String memberName;
	private Integer creditBalance;
	private Integer prodCreditBalance;
	private String phoneNumber;
	private String entryWhsId;
	private String entryWhsName;
	private JSONArray distEmails;
	private Object aroStatus;
	private String statusMsg;
	
	public String getDistId() {
		return distId;
	}
	public void setDistId(String distId) {
		this.distId = distId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Integer getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(Integer creditBalance) {
		this.creditBalance = creditBalance;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public JSONArray getDistEmails() {
		return distEmails;
	}
	public void setDistEmails(JSONArray distEmails) {
		this.distEmails = distEmails;
	}
	public Integer getProdCreditBalance() {
		return prodCreditBalance;
	}
	public void setProdCreditBalance(Integer prodCreditBalance) {
		this.prodCreditBalance = prodCreditBalance;
	}
	public String getEntryWhsId() {
		return entryWhsId;
	}
	public void setEntryWhsId(String entryWhsId) {
		this.entryWhsId = entryWhsId;
	}
	public String getEntryWhsName() {
		return entryWhsName;
	}
	public void setEntryWhsName(String entryWhsName) {
		this.entryWhsName = entryWhsName;
	}
	public Object getAroStatus() {
		return aroStatus;
	}
	public void setAroStatus(Object aroStatus) {
		this.aroStatus = aroStatus;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
}
