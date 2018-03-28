package com.eastrobot.robotdev.test;

import java.util.Calendar;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.utils.MD5;
import com.eastrobot.robotdev.utils.HttpClientUtils;



public class Test {
	public static void sendEid(String userid)
	{
		long time = System.currentTimeMillis();
		String token = MD5.GetMD5Code(Constants.NUSKIN+time);
		String url = "http://xiaoistagetest.cn.nuskin.com:8001/dev/logout?token="+token+"&wid="+userid+"&time="+time;
		System.out.println(url);
		String response = "";
		try {
			response = HttpClientUtils.doGet(url,"utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);
	}
	
	public static void login()
	{
		String userid = "undefined";
		String eid = "uhuerngui23po20fkegkre";
		String token = MD5.GetMD5Code(Constants.NUSKIN+eid);
		String url = "http://xiaoistagetest.cn.nuskin.com:8001/dev/status?token="+token+"&wid="+userid+"&eid="+eid;
		System.out.println(url);
		String response = "";
		try {
			response = HttpClientUtils.doGet(url,"utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);
	}
	
	public static void main(String[] args) throws Exception
	{
		sendEid("undefined");
		login();
	}
}
