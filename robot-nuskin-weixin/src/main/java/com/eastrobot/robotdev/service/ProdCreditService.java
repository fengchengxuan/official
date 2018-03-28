package com.eastrobot.robotdev.service;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;

public class ProdCreditService {
	private String soa_request_host;
	private String addr_points4history_url;
	private String addr_orderentry_url;
	
	public String getPoints4History(String eid)
	{
		String result = "";
		try {
			String url = soa_request_host+addr_points4history_url;
			result = HttpClientUtils.doGetXml(url,Constants.ENCODE, eid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getOrderEntrys(String eid,String id)
	{
		String result = "";
		try {
			String url = soa_request_host+addr_orderentry_url+id;
			result = HttpClientUtils.doGetXml(url,Constants.ENCODE, eid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getSoa_request_host() {
		return soa_request_host;
	}

	public void setSoa_request_host(String soa_request_host) {
		this.soa_request_host = soa_request_host;
	}

	public String getAddr_points4history_url() {
		return addr_points4history_url;
	}

	public void setAddr_points4history_url(String addr_points4history_url) {
		this.addr_points4history_url = addr_points4history_url;
	}

	public String getAddr_orderentry_url() {
		return addr_orderentry_url;
	}

	public void setAddr_orderentry_url(String addr_orderentry_url) {
		this.addr_orderentry_url = addr_orderentry_url;
	}
	
}
