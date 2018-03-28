package com.eastrobot.robotdev.nuskin.utils;

import org.apache.log4j.Logger;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.RegExUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotface.domain.RobotRequestEx;
import com.eastrobot.robotface.domain.RobotResponseEx;

public class FaqVoteUtils {
	private static Logger log = Logger.getLogger(FaqVoteUtils.class);
	private static LRUTCache<String,String> cache = new LRUTCache<String,String>(100000,30*60);
	private static RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	public static RobotRequestEx filterRequest(RobotRequestEx request)
	{
		String userId = request.getUserId();
		String question = request.getQuestion().trim();
		log.info("start userId and question:"+userId+"|"+question);
		Object obj = cache.get(userId);
		if(ValidationUtils.isEmpty(obj))
			return request;
		if(RegExUtils.validateStr(question,"no"))
		{
			String faqStr = (String) obj;
			if(ValidationUtils.isNotEmpty(faqStr))
			{
				request.setQuestion(faqStr);
			}
		}
		//未解决仅维持一次上下文
		cache.remove(userId);
		return request;
	}
	
	public static RobotResponseEx filterResponse(String userId,RobotResponseEx response)
	{
		if(ValidationUtils.isEmpty(response))
			response = new RobotResponseEx();
		String content = response.getContent();
		if(null==content)
			content = "";
		
		content = getFaqStr(userId, content);
		response.setContent(content);
		return response;
	}
	
	private static String getFaqStr(String userId,String content)
	{
		if(ValidationUtils.isEmpty(content))
			return content;
		StringBuffer sb = new StringBuffer(content);
		int faqstart = sb.indexOf("[faq]");
		
		if(faqstart<0)
			return content;
		String label = sb.substring(faqstart);
		sb.delete(faqstart,sb.length());
		int end = label.lastIndexOf("[/faq]");
		String faqStr = label.substring(5,end);
		log.info("faqString字符串:"+faqStr);
		cache.put(userId,faqStr);
		
		String faqTips = rmrp.get("FaqVoteTips",Constants.Platform.ALL);
		sb.append(faqTips);
		
		return sb.toString();
	}
	
}
