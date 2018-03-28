package com.eastrobot.robotdev.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eastrobot.robotdev.utils.ValidationUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.eastrobot.robotdev.ws.client.RobotRequest;
import cn.eastrobot.robotdev.ws.client.RobotResponse;
import cn.eastrobot.robotdev.ws.client.RobotServiceWx;
import cn.eastrobot.robotdev.ws.client.RobotServiceWx_Service;
import cn.eastrobot.robotdev.ws.client.UserAttribute;




public class AskTest{
	private RobotServiceWx client;
	public AskTest(){
		try {
			String url = "http://localhost/dev/ws/RobotServiceWx?wsdl";
//			String url = "http://rxbjpnewr.demo.xiaoi.com/dev/ws/RobotServiceWx?wsdl";
//			String url = "http://xiaoitest.cn.nuskin.com:8000/dev/ws/RobotServiceWx?wsdl";
//			String url = "http://xiaoistage.cn.nuskin.com:8000/dev/ws/RobotServiceWx?wsdl";
//			String url = "http://42.159.201.81:8000/dev/ws/RobotServiceWx?wsdl";
//			String url = "http://xiaoiprodtest.cn.nuskin.com:8001/dev/ws/RobotServiceWx?wsdl";
			RobotServiceWx_Service rs = new RobotServiceWx_Service(new URL(url));
			client = rs.getDefaultRobotServiceWxPort();
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	
	
	
	public int getResponse(String question,String userId,String msgid){
		RobotRequest request = new RobotRequest();
		request.setQuestion(question);
		
		request.setUserId(userId);
		
		List<UserAttribute> list = new ArrayList<UserAttribute>();
		UserAttribute ua = new UserAttribute();
		ua.setName("platform");
		ua.setValue("weixin");
		list.add(ua);
		
		UserAttribute ua1 = new UserAttribute();
		ua1.setName("openid");
		ua1.setValue("gh_0832d297b615");
		list.add(ua1);
		
		UserAttribute ua2 = new UserAttribute();
		ua2.setName("msgid");
		ua2.setValue(msgid);
		list.add(ua2);
		
		UserAttribute ua3 = new UserAttribute();
		ua3.setName("soatype");
		ua3.setValue("5");
		list.add(ua3);
		request.setAttributes(list);
		
		RobotResponse response = client.ask(request);
		
		if(ValidationUtils.isNotEmpty(response))
		{
			JSONArray array = JSONArray.fromObject(response.getCommands());
			
			System.out.println(array);
			System.out.println(JSONObject.fromObject(response));
		}else{
			System.out.println("结果为空");
		}
		return response.getType();
	}

	
	
	public static void main(String[] args){
//		try {
//			int total = 0;
//			int step = 0;
//			ReadExcel re = new ReadExcel();
//			List<List<String>> result = re.readExcel("D:\\nuskin.xlsx",0);
//			List<String> questions = new ArrayList<String>();
//			for(int i=0;i<result.size();i++)
//			{
//				questions.add(result.get(i).get(0));
//			}
//			
//			AskTest t = new AskTest();
//			String userid = "o3Q2dt69F8ntAPZdbNlocNJ-35YA";
//			
//			boolean does = true;
//			while(does)
//			{
//				int index = (int) Math.round(1000*Math.random());
//				String question = questions.get(index);
//				long millis = Math.round(30000*Math.random());
//				try {
//					int type = t.getResponse(question, userid);
//					if(0==type)
//						step++;
//				} catch (Exception e) {
//					step++;
//				}
//				
//				System.out.println(question+"|"+millis);
//				total++;
//				System.out.println("总问题数"+total+"默认回复次数："+step);
//				try {
//					Thread.sleep(millis);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (Exception e) {
//		}
//		
//		
//		
//		
		AskTest t = new AskTest();
		while(true)
		{
			try {
				System.out.println("请输入问题：");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String question = br.readLine();
				String userid = "ox34quHBXPU0ZOt_jB9nuH44dADA";
				t.getResponse(question,userid,UUID.randomUUID().toString().replaceAll("-",""));
			} catch (Exception e){
			}
		}
	}
}