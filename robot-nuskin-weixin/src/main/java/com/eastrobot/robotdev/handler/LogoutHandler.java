package com.eastrobot.robotdev.handler;


import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.InfoService;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
import com.eastrobot.robotdev.utils.RegExUtils;
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

@Component("logout")
@Scope(HandlerScope.SESSION)
public class LogoutHandler implements Handler<CommandEvent>{
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private WxBindService wxBindService = WxBindService.getInstance();
	private InfoService infoService = InfoService.getInstance();
	private HandlerHelper help = HandlerHelper.getInstance();
	private int step;
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> map = (Map<String,String>)session.getAttribute("key_user_attributes");
			String openid = channel.getUser().getID();
			//查询SOA接口必须是否已经绑定用户
			JSONObject wxjson = wxBindService.isbind(openid,map.get("soatype"));
			if(ValidationUtils.isNotEmpty(wxjson.get("code")))
			{
				int code = wxjson.getInt("code");
				if(500==code && 10086==wxjson.getJSONObject("message").getInt("status"))
				{
					String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					channel.send(soaInsideError);
					exit();
					return;
				}
				
				String eid = EidCacheUtils.getEidById(openid);
				if(ValidationUtils.isNotEmpty(eid))
				{
					PersonalData pd = infoService.getInfo(eid);
					if(ValidationUtils.isNotEmpty(pd) && ValidationUtils.isEmpty(pd.getStatusMsg()))
					{
						String userLogoutTips = rmrp.get("UserLogoutTips", Constants.Platform.ALL,pd.getMemberName(),openid);
						channel.send(userLogoutTips);
					}else{
						String dotNotNeedLogout = rmrp.get("DotNotNeedLogout", Constants.Platform.ALL);
						channel.send(dotNotNeedLogout);
					}
				}else{
					String dotNotNeedLogout = rmrp.get("DotNotNeedLogout", Constants.Platform.ALL);
					channel.send(dotNotNeedLogout);
				}
			}else{
				String CanNotLogoutWithBindStatus = rmrp.get("CanNotLogoutWithBindStatus", Constants.Platform.WEIXIN);
				channel.send(CanNotLogoutWithBindStatus);
			}
			exit();
			return;
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
}
