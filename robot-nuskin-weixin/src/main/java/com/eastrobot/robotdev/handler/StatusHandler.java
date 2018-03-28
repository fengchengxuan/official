package com.eastrobot.robotdev.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.annotations.handler.StartWith;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import com.incesoft.ibotsdk.RobotSession;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.trueFalseType;

@Component("status")
@StartWith(commands={"status"})
@Scope(HandlerScope.SESSION)
public class StatusHandler implements Handler<CommandEvent>{
	private HandlerHelper help = HandlerHelper.getInstance();
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			String openid = channel.getUser().getID();
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> attrs = (Map<String, String>) session.getAttribute("key_user_attributes");
			if(ValidationUtils.isNotEmpty(attrs))
			{
				if(ValidationUtils.isNotEmpty(attrs.get("login")))
				{
					String eid = attrs.get("login");
					EidCacheUtils.putEidById(openid, eid);
					channel.send("success");
				}else if(ValidationUtils.isNotEmpty(attrs.get("logout")))
				{
					EidCacheUtils.removeEidById(openid);
					channel.send("success");
				}else{
					toAi();
				}
			}else{
				toAi();
			}
			exit();
		} catch (Exception e) {
			// TODO: handle exception
			exit();
			e.printStackTrace();
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
}
