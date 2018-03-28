package com.eastrobot.robotdev.nuskin.service;

import net.sf.json.JSONObject;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.PersonalData;

public class InfoService {
	private static InfoService infoService;
	public static InfoService getInstance()
	{
		if(null==infoService)
			infoService = new InfoService();
		return infoService;
	}
	private final String infoUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("addr.info.url");
	private InfoService(){}
	
	
	//通过eid获取用户信息
	public PersonalData getInfo(String eid){
		try {
			String result = HttpClientUtils.doGetXml(infoUrl,Constants.ENCODE,eid);
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONObject json = JSONObject.fromObject(result);
				if(ValidationUtils.isEmpty(json.get("code")))
				{
					return JsonUtils.convertToObj(json, PersonalData.class);
				}else{
					PersonalData pd = new PersonalData();
					pd.setStatusMsg(result);
					return pd;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		InfoService is = new InfoService();
		PersonalData j = is.getInfo("uaw6zHwv0wzw0xyxtyuywSrky7qA4e6jBA");
		System.out.println(j.getStatusMsg());
	}
}
