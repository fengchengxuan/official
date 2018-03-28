package com.eastrobot.robotdev.nuskin.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.utils.MD5;
import com.eastrobot.robotdev.nuskin.utils.WhiteListUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.ws.impl.CallService;
import com.eastrobot.robotface.domain.RobotRequestEx;
import com.eastrobot.robotface.domain.RobotResponseEx;
import com.eastrobot.robotface.domain.UserAttribute;

public class LogoutServlet extends HttpServlet {
	private CallService callService;
	@Override
	public void init() throws ServletException {
		ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:app*.xml");
		this.callService = (CallService) ac.getBean("callService");
	}
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String openid = request.getParameter("wid");
		String token = request.getParameter("token");
		String time = request.getParameter("time");
		
		response.setCharacterEncoding(Constants.ENCODE);
		PrintWriter out = response.getWriter();
		
		try{
			if(!WhiteListUtils.isWhiteList(openid))
			{
				out.print(getMessage("账号不在测试范围内", false));
				return;
			}
			
			try {
				long date = Long.parseLong(time);
				long current_time = System.currentTimeMillis();
				long cha = current_time-date;
				System.out.println("时间差："+cha);
				if(Math.abs(cha)>120000)
				{
					out.print(getMessage("访问受限", false));
					return;
				}
			} catch (Exception e) {
				out.print(getMessage("访问受限", false));
				return;
			}
			
			boolean isSave = this.isSaveInfo(token,time);
			String tips = "";
			if(isSave)
			{
				RobotRequestEx robotRequest = new RobotRequestEx();
				robotRequest.setQuestion("status");
				robotRequest.setUserId(openid);
				
				UserAttribute[] list = new UserAttribute[2];
				UserAttribute ua = new UserAttribute();
				ua.setName("platform");
				ua.setValue("weixin");
				list[0] = ua;
				
				UserAttribute ua1 = new UserAttribute();
				ua1.setName("logout");
				ua1.setValue(time);
				list[1] = ua1;
				robotRequest.setAttributes(list);
				
				RobotResponseEx robotResponse = callService.call(robotRequest);
				if(robotResponse.getContent().indexOf("success")>=0)
				{
					tips = "注销成功";
				}else{
					tips = "内部服务器错误";
					isSave = false;
				}
			}else{
				tips = "访问受限";
			}
			out.print(getMessage(tips,isSave));
		}
		catch (Exception e) {
			e.printStackTrace();
			out.print(getMessage("内部服务器错误", false));
		}
	}
	
	private String getMessage(String msg,boolean status)
	{
		JSONObject json = new JSONObject();
		json.put("message",msg);
		json.put("status",status);
		return json.toString();
	}
	
	private boolean isSaveInfo(String token,String eid)
	{
		String str = Constants.NUSKIN+eid;
		String code = MD5.GetMD5Code(str);
		if(ValidationUtils.isNotEmpty(code))
		{
			if(code.equals(token))
				return true;
		}
		return false;
	}
}
