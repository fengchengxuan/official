package com.eastrobot.robotdev.handler;


import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import com.incesoft.ibotsdk.RobotSession;

@Component("bind")
@Scope(HandlerScope.SESSION)
public class BindHandler implements Handler<CommandEvent>{
	
	//private final String bindUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("addr.bind.url");
	private RobotMessageResourceProxy rmrp = new RobotMessageResourceProxy();
	
	private WxBindService wxBindService = WxBindService.getInstance();
	
	private HandlerHelper help = HandlerHelper.getInstance();
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		
		try {
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> map = (Map<String,String>)session.getAttribute("key_user_attributes");
			String soatype = map.get("soatype");
			String openid = channel.getUser().getID();
			//查询SOA接口必须是否已经绑定用户
			JSONObject wxjson = wxBindService.isbind(openid,soatype);
			if(ValidationUtils.isEmpty(wxjson.get("code")))
			{
				String alreadyBind = rmrp.get("AlreadyBind",Constants.Platform.WEIXIN);
				channel.send(alreadyBind);
			}else{
				if(500 == wxjson.getInt("code"))
				{
					int status = wxjson.getJSONObject("message").getInt("status");
					if(1==status)
					{
						String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
						String gotoBind = rmrp.get("GoToBindTips",Constants.Platform.WEIXIN,userBindUrl);
						channel.send(gotoBind);
					}else if(10086==status){
						String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
						channel.send(soaInsideError);
					}
				}
			}
			
			
			
		} catch (Exception e) {
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			e.printStackTrace();
		}
		exit();
		return;
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
