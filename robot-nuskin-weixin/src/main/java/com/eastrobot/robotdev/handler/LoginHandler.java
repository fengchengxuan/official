package com.eastrobot.robotdev.handler;

import java.util.Map;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.InfoService;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.PersonalData;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import com.incesoft.ibotsdk.RobotSession;

@Component("login")
@Scope(HandlerScope.SESSION)
public class LoginHandler implements Handler<CommandEvent>{
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private WxBindService wxBindService = WxBindService.getInstance();
	private InfoService infoService = InfoService.getInstance();
	private String eid;
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
			String tips="";
			if(ValidationUtils.isEmpty(wxjson.get("code")))
			{
				tips = rmrp.get("CanNotLoginWithBindStatus",Constants.Platform.WEIXIN);
			}else{
				int code = wxjson.getInt("code");
				if(500==code && 10086==wxjson.getJSONObject("message").getInt("status"))
				{
					String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					channel.send(soaInsideError);
					exit();
					return;
				}else{
					eid = EidCacheUtils.getEidById(openid);
				}
				
				String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
				if(ValidationUtils.isNotEmpty(eid))
				{
					PersonalData pd = infoService.getInfo(eid);
					if(ValidationUtils.isNotEmpty(pd) && ValidationUtils.isEmpty(pd.getStatusMsg()))
					{
						tips = rmrp.get("UserAlreadyLogin",Constants.Platform.ALL,pd.getMemberName(),userLoginUrl);
					}else{
						tips = rmrp.get("UserLogin",Constants.Platform.ALL,userLoginUrl);
					}
				}else{
					tips = rmrp.get("UserLogin",Constants.Platform.ALL,userLoginUrl);
				}
			}
			channel.send(tips);
			exit();
		} catch (Exception e) {
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
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
