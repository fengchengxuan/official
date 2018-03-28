package com.eastrobot.robotdev.nuskin.service;


import net.sf.json.JSONObject;

import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;

public class WxBindService {
	private static WxBindService wxBindService;
	private WxBindService(){}
	public static WxBindService getInstance()
	{
		if(null==wxBindService)
			wxBindService = new WxBindService();
		return wxBindService;
	}
	private final String checkBindUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("addr.checkbind.url");
	public JSONObject isbind(String openid,String soatype){
		try {
			String wxUrl = checkBindUrl+openid+"/"+soatype;
			System.out.println(wxUrl);
			String wxbindMesg = HttpClientUtils.doGet(wxUrl, "utf-8");
			JSONObject jsWx = JSONObject.fromObject(wxbindMesg);
			return jsWx;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		WxBindService wb = new WxBindService();
		//System.out.println(wb.isbind("ox34quOqBcAAmD8Mb23HP2Zb7R"));
		String url = "https://stage.cn.nuskin.com:443/ws/api/auth/external/token/oMdzOt8Bn8zZc4E-FetS4jeZ2mpw/2";
		System.out.println(wb.isbind("oSiW-jkwxZuTAPD2rjgEOyPi66uM","2"));
	}
}
