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

@Component("order")
@Scope(HandlerScope.SESSION)
public class OrderStatusHandler implements Handler<CommandEvent>{

	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	private WxBindService wxBindService = WxBindService.getInstance();
	private InfoService infoService = InfoService.getInstance();
	private HandlerHelper help = HandlerHelper.getInstance();
	private String eid;
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> map = (Map<String,String>)session.getAttribute("key_user_attributes");
			String soatype = map.get("soatype");
			String openid = channel.getUser().getID();
			//查询SOA接口必须是否已经绑定用户
			JSONObject wxjson = wxBindService.isbind(openid,soatype);
			//是否绑定
			if(ValidationUtils.isEmpty(wxjson.get("code")))
			{
				eid = wxjson.getString("eid");
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
			}
			this.doDisponse(channel,openid,soatype);
			exit();
		} catch (Exception e) {
			// TODO: handle exception
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			exit();
			e.printStackTrace();
		}
	}
	
	private void doDisponse(Channel channel,String openid,String soatype)
	{
		String type = "订单状态";
		String[] param =(String[])help.param.get();
		boolean bl = false;
		if(ValidationUtils.isNotEmpty(param) && param.length>0)
		{
			if("history".equalsIgnoreCase(param[0]))
				bl = true;
		}
		if(bl)
			type = "历史订单";
		
		if(ValidationUtils.isNotEmpty(eid))
		{
			PersonalData pd = infoService.getInfo(eid);
			if(ValidationUtils.isNotEmpty(pd) && ValidationUtils.isEmpty(pd.getStatusMsg()))
			{
				String userName = pd.getMemberName();
				String response = rmrp.get("OrderInfoResponse",Constants.Platform.WEIXIN,userName,openid,soatype);
				
				if(bl)
				{
					response = rmrp.get("HistoryOrderInfoResponse",Constants.Platform.WEIXIN,userName,openid,soatype);
				}
				channel.send(response);
				exit();
				return;
			}
		}
		String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
		String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
		String notbind = rmrp.get("NotBindTips",Constants.Platform.ALL,type,userLoginUrl,userBindUrl);
		channel.send(notbind);
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
