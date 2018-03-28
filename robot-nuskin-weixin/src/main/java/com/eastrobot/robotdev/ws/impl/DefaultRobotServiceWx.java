package com.eastrobot.robotdev.ws.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.utils.FaqVoteUtils;
import com.eastrobot.robotdev.nuskin.utils.NuskinUtils;
import com.eastrobot.robotdev.nuskin.utils.WhiteListUtils;
import com.eastrobot.robotdev.utils.DateUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.ws.RobotServiceWx;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotface.RobotServiceEx;
import com.eastrobot.robotface.RobotServiceExProxy;
import com.eastrobot.robotface.domain.RobotRequestEx;
import com.eastrobot.robotface.domain.RobotResponseEx;
import com.eastrobot.robotface.domain.UserAttribute;
import com.eastrobot.robotface.util.ServiceBinder;

@WebService(serviceName = "RobotServiceWx", endpointInterface = "com.eastrobot.robotdev.ws.RobotServiceWx", targetNamespace = "http://www.eastrobot.cn/ws/RobotServiceWx")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, use = SOAPBinding.Use.LITERAL, style = SOAPBinding.Style.DOCUMENT)
public class DefaultRobotServiceWx implements RobotServiceWx {
	private static RobotServiceEx robotServiceEx = new RobotServiceExProxy();
	private static Logger log = LoggerFactory.getLogger(DefaultRobotServiceWx.class);
	private static RobotMessageResource rmrp = new RobotMessageResourceProxy();
	//特殊日期温馨提示的每月日期
	private static final int specialDay = 20;
	public DefaultRobotServiceWx()
	{
		ServiceBinder.bind(RobotServiceWx.class.getName(),this);
	}
	
	@Autowired
	private CallService callService;
	/**
	 * 智能问答
	 * 
	 * @param robotRequest
	 * @return
	 */
	public RobotResponseEx ask(RobotRequestEx robotRequestEx) {
		RobotResponseEx response = new RobotResponseEx();
		if (null == robotRequestEx) {
			log.info("ERROR：请求对象robotRequestWx为空");
			this.systemtips(response,"请求参数异常，请稍后重试");
			return response;
		}
		
		long begin = System.currentTimeMillis();
		log.info("接收到请求的时间："+DateUtils.long2Str(begin,"yyyy-MM-dd HH:mm:ss:SSSS"));
		String question = robotRequestEx.getQuestion().trim();
		String userId = robotRequestEx.getUserId();
		
		log.info("userId--question:"+userId+"|"+question);
		Map<String,String> map = this.array2map(robotRequestEx.getAttributes());
		log.info("userAttr:"+map);
		String openid = map.get("openid");
		
		try {
			if(!question.startsWith(Constants.DO_NOW))
			{
				try {
					//负载均衡
					response=callService.call(robotRequestEx);
					return response;
				} catch (Exception e) {
					//如果调用的服务器挂了由本级继续处理
					log.error("", e);
				}
			}
			question = question.replaceFirst(Constants.DO_NOW, "");
			
			//业务需求，将菜单返回上级由p改成f。
			if(question.equalsIgnoreCase("b"))
				question = "f";
			else if(question.equalsIgnoreCase("f"))
				question = "b";
			
			//是否属于白名单
			boolean inWhiteList = WhiteListUtils.isWhiteList(userId);
			if(!inWhiteList)
			{
				return null;
			}
			robotRequestEx.setQuestion(question);
			
			try {
				robotRequestEx = FaqVoteUtils.filterRequest(robotRequestEx);
			} catch (Exception e) {
				log.error("未解决处理request失败");
			}
			
			
			response = robotServiceEx.deliver(robotRequestEx);
			//过滤温馨提示日期
			String content = response.getContent();
			if(ValidationUtils.isNotEmpty(content) && !content.startsWith(Constants.IMG_TXT_MSG))
			{
				String tips = "";
				Calendar calendar = Calendar.getInstance();
				int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int mouth = calendar.get(Calendar.MONTH)+1;
				
				String testStr = rmrp.get("TestSpecialDay",Constants.Platform.WEIXIN);
				if(ValidationUtils.isNotEmpty(testStr))
				{
					JSONObject obj = JSONObject.fromObject(testStr);
					mouth = obj.getInt("mouth");
					day = obj.getInt("day");
				}
				
				if(day==specialDay || day==lastDay || day==(lastDay-1))
				{
					if(ValidationUtils.isEmpty(NuskinUtils.getValue(userId)))
					{
						if(day==specialDay)
						{
							tips = rmrp.get("EachMonth20."+mouth,Constants.Platform.WEIXIN);
							if(ValidationUtils.isEmpty(tips))
								tips = "今天20号了，有没有积极行动，完成目标奖。";
						}else if(day==lastDay)
						{
							tips = rmrp.get("LastDayOfMouth."+mouth,Constants.Platform.WEIXIN);
							if(ValidationUtils.isEmpty(tips))
								tips = "月底啦，加油啊，有没有超标完成本月绩效？敢想敢做一定行噢。";
						}else
						{
							tips = rmrp.get("Last2DaysOfMouth."+mouth,Constants.Platform.WEIXIN);
							if(ValidationUtils.isEmpty(tips))
								tips = "月底啦，加油啊，有没有超标完成本月绩效？敢想敢做一定行噢。";
						}
						content = tips+content;
						response.setContent(content);
						NuskinUtils.putCache(userId,"true");
					}
				}
			}
			
			//定义需要的参数，一个是微信请求参数和微信响应参数
			NuskinUtils.changeCommands(response,openid);
			
			try {
				response = FaqVoteUtils.filterResponse(userId, response);
			} catch (Exception e) {
				log.error("未解决处理response失败");
			}
			response.setContent(removeLastSpace(response.getContent()));
			log.info("commands数据:"+JSONArray.fromObject(response.getCommands()));
			log.info("响应答案：" + response.getContent());
			
			long finish = System.currentTimeMillis();
			log.info("处理完毕时的时间"+DateUtils.long2Str(finish,"yyyy-MM-dd HH:mm:ss:SSSS"));
			log.info("小i处理处理时长："+(finish-begin)+"毫秒");
			System.out.println("");
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			this.systemtips(response,"系统异常，请稍后重试!");
			return response;
		}
	}

	private Map<String,String> array2map(UserAttribute[] attrs)
	{
		Map<String, String> map = new HashMap<String, String>();
		for(UserAttribute ua:attrs)
		{
			map.put(ua.getName(),ua.getValue());
		}
		return map;
	}
	public static String removeLastSpace(String content){
		if(ValidationUtils.isNotEmpty(content))
		{
			StringBuilder sb = new StringBuilder(content);
			while( (sb.lastIndexOf("\r\n")>=0 && (sb.lastIndexOf("\r\n") == (sb.length()-2))) || (sb.lastIndexOf("\n")>=0 && (sb.lastIndexOf("\n") == (sb.length()-1))))
			{
				if(sb.lastIndexOf("\r\n") == (sb.length()-2))
					sb.delete(sb.lastIndexOf("\r\n"),sb.length());
				else
					sb.deleteCharAt(sb.lastIndexOf("\n"));
			}
			
			content = sb.toString();
		}
		return content.replaceAll("\r\n|\r","\n").replaceAll("@n@","\n\n");
	}
	
	private void systemtips(RobotResponseEx response,String errorMsg)
	{
		response.setType(-1);
		response.setContent(errorMsg);
	}
	
}