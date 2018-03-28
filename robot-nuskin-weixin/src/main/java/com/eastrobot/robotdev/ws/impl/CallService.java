package com.eastrobot.robotdev.ws.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import cn.eastrobot.robotdev.ws.client.RobotCommand;
import cn.eastrobot.robotdev.ws.client.RobotRequest;
import cn.eastrobot.robotdev.ws.client.RobotResponse;
import cn.eastrobot.robotdev.ws.client.RobotServiceWx;
import cn.eastrobot.robotdev.ws.client.UserAttribute;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.domain.RobotRequestEx;
import com.eastrobot.robotface.domain.RobotResponseEx;

public class CallService{
	
	private Map<Integer, RobotServiceWx> robotServiceExMap;

	private static final Logger log = LoggerFactory.getLogger(CallService.class);
	
	public RobotResponseEx call(RobotRequestEx request)
	{
		String userId = request.getUserId();
		RobotResponseEx responseEx = new RobotResponseEx();
		try {
			int hostSize = robotServiceExMap.size();
			int hostNum = Math.abs(userId.hashCode()%hostSize);
			log.info("总共"+hostSize+"台服务器;用户进第"+hostNum+"服务器");
			
			String question = request.getQuestion();
			log.debug("传入callService的参数:"+question+"|"+userId+"|"+request.getAttributes().length);
			
			question = Constants.DO_NOW+question;
			request.setQuestion(question);
			//调用用户对应的服务器客户端
			RobotServiceWx robotServiceEx = robotServiceExMap.get(hostNum);
			
			Client proxy = ClientProxy.getClient(robotServiceEx);
			HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			policy.setConnectionTimeout(3000); //连接超时时间
			policy.setReceiveTimeout(10000);//请求超时时间.
			conduit.setClient(policy);
			
			RobotResponse robotResponse = robotServiceEx.ask(change2request(request));
			responseEx = change2ResponseEx(robotResponse);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
			throw new RuntimeException("call webservice exception", e);
		}
		return responseEx;
	}
	
	public static RobotRequest change2request(RobotRequestEx requestEx)
	{
		RobotRequest request = new RobotRequest();
		request.setUserId(requestEx.getUserId());
		request.setQuestion(requestEx.getQuestion());
		request.setSessionId(requestEx.getSessionId());
		request.setMaxReturn(requestEx.getMaxReturn());
		
		
		com.eastrobot.robotface.domain.UserAttribute[] attr = requestEx.getAttributes();
		if(attr.length>0)
		{
			List<UserAttribute> list = new ArrayList<UserAttribute>();
			for(int i=0;i<attr.length;i++)
			{
				UserAttribute ua = new UserAttribute();
				ua.setName(attr[i].getName());
				ua.setValue(attr[i].getValue());
				list.add(ua);
			}
			request.setAttributes(list);
		}
		
		return request;
	}
	
	public static RobotResponseEx change2ResponseEx(RobotResponse response)
	{
		if(ValidationUtils.isNotEmpty(response))
		{
			RobotResponseEx responseEx = new RobotResponseEx();
			responseEx.setContent(response.getContent());
			responseEx.setModuleId(response.getModuleId());
			responseEx.setType(response.getType());
			
			responseEx.setNodeId(response.getNodeId());
			responseEx.setSimilarity(response.getSimilarity());
			
			List<RobotCommand> list = response.getCommands();
			if(list.size()>0)
			{
				com.eastrobot.robotface.domain.RobotCommand[] commands = new com.eastrobot.robotface.domain.RobotCommand[list.size()];
				for(int i=0;i<commands.length;i++)
				{
					com.eastrobot.robotface.domain.RobotCommand rc = new com.eastrobot.robotface.domain.RobotCommand();
					rc.setName(list.get(i).getName());
					rc.setState(list.get(i).getState());
					
					List<String> str = list.get(i).getArgs();
					if(null!=str)
					{
						String[] args = new String[str.size()];
						for(int j=0;j<str.size();j++)
						{
							args[j] = str.get(j);
						}
						rc.setArgs(args);
					}
					commands[i] = rc;
					
				}
				responseEx.setCommands(commands);
			}
			
			List<String> rq = response.getRelatedQuestions();
			if(rq.size()>0)
			{
				String[] relatedQuestions = new String[rq.size()];
				System.out.println("relatedQuestions:"+relatedQuestions);
				for(int i = 0;i<rq.size();i++)
				{
					relatedQuestions[i]=rq.get(i);
				}
				responseEx.setRelatedQuestions(relatedQuestions);
			}
			return responseEx;
		}
		return null;
	}


	public void setRobotServiceExMap(Map<Integer, RobotServiceWx> robotServiceExMap) {
		this.robotServiceExMap = robotServiceExMap;
	}

	
}
