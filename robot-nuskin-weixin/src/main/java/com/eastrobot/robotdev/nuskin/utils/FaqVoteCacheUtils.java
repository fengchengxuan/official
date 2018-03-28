package com.eastrobot.robotdev.nuskin.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;

public class FaqVoteCacheUtils {
	private static Logger log = Logger.getLogger(FaqVoteCacheUtils.class);
	public static Question getQuestion(String userid)
	{
		String url = PropertiesUtil.getValueByKey("localhost.addr")+"/dev/faq-cache!get.action?userId="+userid;
		try {
			String result = HttpClientUtils.doGetXml(url,Constants.ENCODE,null);
			if(ValidationUtils.isNotEmpty(result) && result.startsWith("{"))
			{
				return JsonUtils.convertToObj(JSONObject.fromObject(result),Question.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void putQuestion(final String userid,final Question question)
	{
		Map<String,String> map = new HashMap<String, String>();
		String value = JSONObject.fromObject(question).toString();
		map.put("userId",userid);
		map.put("qjson",NuskinUtils.changeFormat(value,"utf-8","iso-8859-1"));
		int i=0;
		while(true)
		{
			String key = "call."+i+".addr";
			String url = PropertiesUtil.getValueByKey(key);
			if(ValidationUtils.isEmpty(url))
				break;
			url = url.substring(0,url.indexOf("ws"))+"/faq-cache!put.action";
			try {
				String res = HttpClientUtils.doPost(url,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
	}
	
	public static void removeQuestion(final String userid)
	{
		Map<String,String> map = new HashMap<String, String>();
		map.put("userId",userid);
		int i=0;
		while(true)
		{
			String key = "call."+i+".addr";
			String url = PropertiesUtil.getValueByKey(key);
			if(ValidationUtils.isEmpty(url))
				break;
			url = url.substring(0,url.indexOf("ws"))+"/faq-cache!remove.action";
			try {
				String res = HttpClientUtils.doPost(url,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
	}
	
	public static void setStatus(final String msgid)
	{
		int i=0;
		while(true)
		{
			String key = "call."+i+".addr";
			String url = PropertiesUtil.getValueByKey(key);
			if(ValidationUtils.isEmpty(url))
				break;
			url = url.substring(0,url.indexOf("ws"))+"/faq-vote!putstatus.action?msgid="+msgid;
			try {
				String res = HttpClientUtils.doGetXml(url,Constants.ENCODE,null);
				log.info("putstatus："+res);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}
