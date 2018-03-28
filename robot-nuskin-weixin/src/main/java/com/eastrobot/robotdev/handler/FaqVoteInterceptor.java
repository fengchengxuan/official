package com.eastrobot.robotdev.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.nuskin.utils.FaqVoteCacheUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Event;
import com.eastrobot.robotnerve.HandlerContext;
import com.eastrobot.robotnerve.HandlerDef;
import com.eastrobot.robotnerve.HandlerInterceptor;
import com.eastrobot.robotnerve.InterceptorChain;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.TextEvent;
import com.eastrobot.robotnerve.throughput.HandlerOutput;
import com.eastrobot.robotnerve.throughput.SendAction;
import com.eastrobot.robotnerve.throughput.TextSendAction;
import com.incesoft.ibotsdk.RobotSession;

@Component
public class FaqVoteInterceptor implements HandlerInterceptor {
	private static Logger log = Logger.getLogger(FaqVoteInterceptor.class);
	private HandlerHelper h = HandlerHelper.getInstance();
	private static RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	@Override
	public void interceptHandler(Channel channel, Event event, HandlerDef handlerDef,
			InterceptorChain chain) {
		log.info("***********************FaqVoteIntercepter******************************");
		if(event instanceof TextEvent && null != event){
			HandlerContext context = HandlerContext.getContext();
			
			TextEvent textEvent = (TextEvent) event;
			String userId = textEvent.getUserId();
			String question = textEvent.getInput();
			if(ValidationUtils.isNotEmpty(question))
				question = question.trim();
			
			if("no".equalsIgnoreCase(question))
			{
				Question qt = FaqVoteCacheUtils.getQuestion(userId);
				if(ValidationUtils.isNotEmpty(qt))
				{
					String msgid = qt.getMsgId();
					h.nav.to("faqHelp",new String[]{msgid});
				}
			}else{
				FaqVoteCacheUtils.removeQuestion(userId);
			}
			// 往后执行，拿到答案类型
			chain.doIntercept(channel, event);
			// 答案类型
			Integer type = (Integer)((RobotSession) channel.getRealSession()).getAttribute("key_answer_type");
			if(type==1)
			{
				HandlerOutput output = context.getHandlerOutput();
				List<SendAction> actions = new ArrayList<SendAction>();
				List<SendAction> otherActs = new ArrayList<SendAction>();
				if (output != null) {
					actions = output.getActions();
				}
				StringBuilder sb = new StringBuilder();
				for (SendAction act : actions) {
					if (act instanceof TextSendAction) {
						sb.append(((TextSendAction) act).getText());
					} else {
						otherActs.add(act);
					}
				}
				
				String answer = this.filterResponse(userId,sb.toString(),channel);
				if(output != null)
					output.clear();
				channel.send(answer);
			}
		} else {
			chain.doIntercept(channel, event);
		}
	}
	
	private static String filterResponse(String userId,String answer,Channel channel)
	{
		try {
			if(ValidationUtils.isEmpty(answer))
				answer = "";
			StringBuffer sb = new StringBuffer(answer);
			int faqstart = sb.indexOf("[faq]");
			if(faqstart<0)
				return answer;
			String label = sb.substring(faqstart);
			sb.delete(faqstart,sb.length());
			int end = label.lastIndexOf("[/faq]");
			String faqStr = label.substring(5,end);
			log.info("faqString字符串:"+faqStr);
			
			JSONObject json = JSONObject.fromObject(faqStr);
			String standardQ = json.getString("standardQ");
			json.put("standardQ",standardQ);
			
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> map = (Map<String,String>)session.getAttribute("key_user_attributes");
			String msgid = map.get("msgid");
			if(ValidationUtils.isEmpty(msgid))
				msgid = UUID.randomUUID().toString().replaceAll("-","");
			json.put("msgId",msgid);
			
			String question = json.getString("question");
			json.put("question",question);
			
			String ans = json.getString("answer");
			json.put("answer",ans);
			
			json.put("sessionId",channel.getSessionId());
			json.put("userId",userId);
			
			FaqVoteCacheUtils.putQuestion(userId,JsonUtils.convertToObj(json,Question.class));
			String faqTips = rmrp.get("FaqVoteTips",Constants.Platform.ALL);
			
			sb.append(faqTips);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
