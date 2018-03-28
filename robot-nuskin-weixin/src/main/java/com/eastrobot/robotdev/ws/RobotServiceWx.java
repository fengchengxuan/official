package com.eastrobot.robotdev.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.eastrobot.robotface.domain.RobotRequestEx;
import com.eastrobot.robotface.domain.RobotResponseEx;


@WebService(targetNamespace = "http://www.eastrobot.cn/ws/RobotServiceWx")
public interface RobotServiceWx {

	/**
	 * 智能问答
	 * 
	 * @param robotRequestn
	 */
	@WebMethod(action = "http://www.eastrobot.cn/ws/RobotServiceWx/ask")
	@WebResult(name = "robotResponse")
	public RobotResponseEx ask(@WebParam(name = "robotRequest")
	RobotRequestEx robotRequest);
}
