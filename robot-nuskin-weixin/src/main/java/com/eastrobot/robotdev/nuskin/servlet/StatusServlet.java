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

public class StatusServlet extends HttpServlet {
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
		String eid = request.getParameter("eid");
		
		response.setCharacterEncoding(Constants.ENCODE);
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try{
			if(!WhiteListUtils.isWhiteList(openid))
			{
				json.put("message","账号不在测试范围内");
				json.put("status",false);
				out.print(json);
				return;
			}
			
			boolean isSave = this.isSaveInfo(token, eid);
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
				ua1.setName("login");
				ua1.setValue(eid);
				list[1] = ua1;
				robotRequest.setAttributes(list);
				
				RobotResponseEx robotResponse = callService.call(robotRequest);
				if(robotResponse.getContent().indexOf("success")>=0)
				{
					json.put("message","获取成功");
				}else{
					json.put("message","内部服务器错误");
					isSave = false;
				}
			}else{
				json.put("message","访问受限");
			}
			json.put("status",isSave);
			out.print(json);
		}
		catch (Exception e) {
			e.printStackTrace();
			json.put("message","内部服务器错误");
			json.put("status",false);
			out.print(json);
		}
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
