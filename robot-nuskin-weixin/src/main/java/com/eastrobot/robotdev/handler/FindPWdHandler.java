package com.eastrobot.robotdev.handler;

import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;

@Component("findpwd")
@Scope(HandlerScope.SESSION)
public class FindPWdHandler implements Handler<CommandEvent>{

	private final String findpwdUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("addr.findpwd.url");
	private HandlerHelper help = HandlerHelper.getInstance();
	private int step;
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			if(0==step)
			{
				String telltheCNcode = rmrp.get("TellTheCNCode",Constants.Platform.ALL);
				channel.send(telltheCNcode);
			}else{
				String input = help.ctx().getInput().trim();
				if("tc".equalsIgnoreCase(input))
				{
					String exitService = rmrp.get("ExitService",Constants.Platform.ALL);
					channel.send(exitService);
					exit();
					return;
				}else{
					input = input.toUpperCase();
					String url = findpwdUrl+URLEncoder.encode(input,Constants.ENCODE);
					String result = HttpClientUtils.doGetXml(url,Constants.ENCODE,null);
					if(ValidationUtils.isNotEmpty(result))
					{
						JSONObject json = JSONObject.fromObject(result);
						if(ValidationUtils.isEmpty(json.get("code")))
						{
							String email = json.getString("email");
							String PwdSendByEmail = rmrp.get("PwdSendByEmail", Constants.Platform.ALL, email);
							channel.send(PwdSendByEmail);
							exit();
							return;
						}else{
							int status = json.getJSONObject("message").getInt("status");
							if(10086 == status)
							{
								String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
								channel.send(soaInsideError);
								exit();
								return;
							}else if(2 == status)
							{
								channel.send("抱歉! 您没有设置邮箱!无法发送密码");
								exit();
								return;
							}else{
								String CNCodeIsNotFind = rmrp.get("CNCodeIsNotFind",Constants.Platform.ALL);
								channel.send(CNCodeIsNotFind);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			exit();
			e.printStackTrace();
		}finally{
			step++;
		}
	}

	private void toAi()
	{
		help.nav.toAI();
	}
	
	private void exit()
	{
		help.ss.x();
	}
	
	public static void main(String[] args) throws Exception {
		FindPWdHandler fwh = new FindPWdHandler();
		String url = fwh.findpwdUrl+URLEncoder.encode("CN1234567",Constants.ENCODE);
		String result = HttpClientUtils.doGetXml(url,Constants.ENCODE,null);
		if(ValidationUtils.isNotEmpty(result))
		{
			JSONObject json = JSONObject.fromObject(result);
			String email = json.getString("email");
			System.out.println("邮件已发送至您的邮箱"+email);
		}else{
			System.out.println("改密失败");
		}
	}
}
