package com.eastrobot.robotdev.handler;



import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
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

/**
 * 积分兑换历史查询
 */
@Component("jfdhls")
@Scope(HandlerScope.SESSION)
public class PointsForHistoryHandler implements Handler<CommandEvent>{
	
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private Logger log = Logger.getLogger(this.getClass());
	private String eid;
	
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
		if(ValidationUtils.isNotEmpty(eid))
		{
			PersonalData pd = infoService.getInfo(eid);
			if(ValidationUtils.isNotEmpty(pd) && ValidationUtils.isEmpty(pd.getStatusMsg()))
			{
				String userName = pd.getMemberName();
				//用户积分
				String creditBalance=ValidationUtils.isNotEmpty(pd.getCreditBalance())?pd.getCreditBalance()+"":"0";
				log.info("积分："+creditBalance);
				String dev_host = rmrp.get("Xiaoi_Dev_Host",Constants.Platform.ALL);
				String p4h_url = dev_host+"prodcredit.action?token="+eid;
				String tips = rmrp.get("Points4History",Constants.Platform.ALL,userName,p4h_url);
				System.out.println(tips);
				channel.send(tips);
				exit();
				return;
			}
		}
		String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
		String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
		String notbind = rmrp.get("NotBindTips",Constants.Platform.ALL,"积分兑换历史查询",userLoginUrl,userBindUrl);
		channel.send(notbind);
		exit();
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
