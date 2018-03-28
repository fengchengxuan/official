package com.eastrobot.robotdev.handler;


import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.InfoService;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.RegExUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.PersonalData;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import com.incesoft.ibotsdk.RobotSession;

@Component("unbind")
@Scope(HandlerScope.SESSION)
public class UnBindHandler implements Handler<CommandEvent>{
	
	private final String unbindUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("addr.unbind.url");
	private RobotMessageResourceProxy rmrp = new RobotMessageResourceProxy();
	private int step;
	private WxBindService wxBindService = WxBindService.getInstance();
	private InfoService infoService = InfoService.getInstance();
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
				String eid = wxjson.getString("eid");
				PersonalData pd = infoService.getInfo(eid);
				String userName = pd.getMemberName();
				if(0==step)
				{
					String menu = rmrp.get("UnBindMenu",Constants.Platform.WEIXIN,userName);
					channel.send(menu);
				}else{
					String input = help.ctx().getInput().trim();
					if(RegExUtils.validateStr(input,"1|是"))
					{
						JSONObject jsonStr = new JSONObject();
						jsonStr.put(Constants.BindInfo.externalId,openid);
						jsonStr.put(Constants.BindInfo.externalTypeCd,soatype);
						String result = HttpClientUtils.doPostXml(unbindUrl, jsonStr.toString(),"utf-8");
						String tips = "";
						if("".equals(result))
						{
							String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
							String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
							tips = rmrp.get("UnBindSuccess",Constants.Platform.WEIXIN,userName,userLoginUrl,userBindUrl);
							EidCacheUtils.removeEidById(openid);
						}else{
							JSONObject obj = JSONObject.fromObject(result);
							if(500 == obj.getInt("code"))
							{
								int status = obj.getJSONObject("message").getInt("status");
								if(10086 == status)
								{
									tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
								}else if(1 == status || 2 == status){
									tips = "系统中未绑定该账户，无需解绑";
								}
							}
							tips = rmrp.get("UnBindFailed",Constants.Platform.WEIXIN);
						}
						channel.send(tips);
					}else if(RegExUtils.validateStr(input,"2|否"))
					{
						String doNotUnBind = rmrp.get("ExitService",Constants.Platform.WEIXIN);
						channel.send(doNotUnBind);
					}else{
						toAi();
					}
					exit();
					return;
				}
			}else{
				String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
				String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
				String DonotNeedUnbind = rmrp.get("DonotNeedUnbind",Constants.Platform.WEIXIN,userLoginUrl,userBindUrl);
				channel.send(DonotNeedUnbind);
				exit();
				return;
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
}
