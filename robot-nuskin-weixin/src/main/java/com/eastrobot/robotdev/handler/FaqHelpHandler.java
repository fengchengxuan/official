package com.eastrobot.robotdev.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.nuskin.utils.FaqVoteCacheUtils;
import com.eastrobot.robotdev.nuskin.utils.NuskinUtils;
import com.eastrobot.robotdev.nuskin.utils.ThreadPoolUtils;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.RegExUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.annotations.handler.StartWith;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;

@Component("faqHelp")
@StartWith(commands={"faqHelp"})
@Scope(HandlerScope.SESSION)
public class FaqHelpHandler implements Handler<CommandEvent>{
	private Logger log = Logger.getLogger(this.getClass());
	private HandlerHelper help = HandlerHelper.getInstance();
	private RobotMessageResourceProxy rmrp = new RobotMessageResourceProxy();
	private static final String regex = "(\\d+[\\.|、] *[\u4e00-\u9fa5\\w\\d.\\(\\)\\[\\]\"\\{\\}（）【】+\\-\\*“”/、@#$%^&,，。！!~`·<>?？：；:;'’‘—=-\\|]*)(?=(\r\n)*)";
	private int step;
	private Map<String,String> map;
	
	private Thread time;
	private boolean exitThread = false;
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		log.info("-----进入faq未解决原因调查服务-----");
		try {
			String userid = channel.getUser().getID();
			Question question = FaqVoteCacheUtils.getQuestion(userid);
			if(0==step)
			{
				String[] msgid = (String[]) help.param.get();
				StringBuilder url = new StringBuilder(rmrp.get("Xiaoi_Dev_Host",Constants.Platform.ALL));
				url.append("h5/pages/faqvote.jsp?");
				url.append("time="+System.currentTimeMillis());
				url.append("&msgid="+msgid[0]);
				url.append("&userId="+userid);
				String menu = rmrp.get("FaqVoteMenu",Constants.Platform.ALL,url.toString());
				if(ValidationUtils.isEmpty(menu))
					menu = "亲，为了更好地解决您的问题，请输入以下序号选择未解决的原因。"+
						   "1. 答案太长\r\n"+
						   "2. 答案与问题不匹配\r\n"+
						   "3. 答案看不懂\r\n"+
						   "如果以上原因都不是您想要的，请点击<a href=\""+url+"\">这里</a>填入您的原因。";
				
				if(ValidationUtils.isEmpty(map))
					map = new HashMap<String,String>();
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(menu);
				while(m.find())
				{
					String reason = m.group();
					String key = reason.substring(0,reason.indexOf(".")>0?reason.indexOf("."):reason.indexOf("、"));
					String value = reason.substring((reason.indexOf(".")>0?reason.indexOf("."):reason.indexOf("、"))+1).trim().replaceAll("\r\n","");
					map.put(key, value);
				}
				channel.send(menu);
				this.timepiece(question,userid,question.getMsgId());
				return;
			}else{
				exitThread = true;
				try {
					time.stop();
				} catch (Exception e) {
				}
				String input = help.ctx().getInput().trim();
				if(ValidationUtils.isNotEmpty(question))
				{
					if(RegExUtils.validateStr(input,"\\d+"))
					{
						String msgid = question.getMsgId();
						String res = this.getCache(msgid);
						if(ValidationUtils.isEmpty(res))
						{
							if(map.containsKey(input))
							{
								question.setReason(map.get(input));
								log.info("提交未解决："+JSONObject.fromObject(question));
								NuskinUtils.sendFaqVote(question);
								FaqVoteCacheUtils.removeQuestion(userid);
								String tips = rmrp.get("faqUnresolvedReply",Constants.Platform.ALL);
								if(ValidationUtils.isEmpty(tips))
									tips = "很遗憾没有帮到您，不过我正在努力学习，希望更好地服务大家。建议您可以拨打客服热线400-004-5678咨询";
								channel.send(tips);
								FaqVoteCacheUtils.setStatus(msgid);
								exit();
							}else{
								String menuInputNumberIncorrect = rmrp.get("menuInputNumberIncorrect",Constants.Platform.ALL);
								if(ValidationUtils.isEmpty(menuInputNumberIncorrect))
									menuInputNumberIncorrect = "输入错误，请重新输入序号。";
								channel.send(menuInputNumberIncorrect);
							}
						}else{
							String alreadySelect = rmrp.get("FaqAlreadySelect",Constants.Platform.ALL);
							if(ValidationUtils.isEmpty(alreadySelect))
								alreadySelect = "亲爱的用户，您已经对该问题做出了反馈，我们将尽快处理，如有问题请继续咨询小如新吧。";
							channel.send(alreadySelect);
							exit();
						}
						return;
					}else{
						String status = this.getCache(question.getMsgId());
						if(ValidationUtils.isEmpty(status))
						{
							FaqVoteCacheUtils.removeQuestion(userid);
							NuskinUtils.sendFaqVote(question);
							FaqVoteCacheUtils.setStatus(question.getMsgId());
						}
						toAi();
						exit();
					}
				}else{
					toAi();
					exit();
				}
				
			}
		} catch (Exception e) {
			exitThread = true;
			time.stop();
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			exit();
			e.printStackTrace();
		}finally{
			step++;
		}
	}

	
	private String getCache(String msgid)
	{
		String url = PropertiesUtil.getValueByKey("localhost.addr")+"/dev/faq-vote!getstatus.action?msgid="+msgid;
		try {
			String result = HttpClientUtils.doGetXml(url,Constants.ENCODE,null);
			if(ValidationUtils.isNotEmpty(result))
				return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private void timepiece(final Question question,final String userid,final String msgid){
		time = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					long sleepT = 5*60*1000;
					time.sleep(sleepT);
				} catch (Exception e) {
				}
				if(!exitThread)
				{
					String status = getCache(msgid);
					System.out.println("---------会话超时--------");
					if(ValidationUtils.isEmpty(status))
					{
						NuskinUtils.sendFaqVote(question);
						FaqVoteCacheUtils.removeQuestion(userid);
						FaqVoteCacheUtils.setStatus(msgid);
					}
				}
			}
		});
//		time.start();
		ThreadPoolUtils.pool.execute(time);
	}
	
	private void toAi()
	{
		help.nav.toAI();
	}
	
	private void exit()
	{
		help.ss.x();
	}
}
