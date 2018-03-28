package com.eastrobot.robotdev.handler;


import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.InfoService;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
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

@Component("info")
@Scope(HandlerScope.SESSION)
public class GrzlcxHandler implements Handler<CommandEvent>{
	
	private RobotMessageResourceProxy rmrp = new RobotMessageResourceProxy();
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
			if(ValidationUtils.isNotEmpty(pd))
			{
				String errormsg = pd.getStatusMsg();
				if(ValidationUtils.isEmpty(errormsg))
				{
					//CN卡号
					String distId = pd.getDistId();
					//姓名
					String memberName = ValidationUtils.isNotEmpty(pd.getMemberName())?pd.getMemberName():"暂无";
					//手机号
					String phoneNum = ValidationUtils.isNotEmpty(pd.getPhoneNumber())?pd.getPhoneNumber():"暂无";
					//积分
					String creditBalance=ValidationUtils.isNotEmpty(pd.getCreditBalance())?pd.getCreditBalance()+"":"0";
					//星级状态
					Object aroStatus = pd.getAroStatus();
					String star = "未激活";
					if(ValidationUtils.isNotEmpty(aroStatus))
					{
						JSONObject status = JSONObject.fromObject(aroStatus);
						int cd = status.getInt("aroStatusCd");
						if(1==cd)
							star = "激活";
					}
					String name = "暂无";
					String email = "暂无";
					JSONArray distEmails = JSONArray.fromObject(pd.getDistEmails());
					for(int i=0;i<distEmails.size();i++)
					{
						JSONObject jobj = distEmails.getJSONObject(i);
						if(jobj.containsKey("emailType"))
						{
							if(1==jobj.getInt("emailType"))
							{
								name = jobj.getString("name");
								email = jobj.getString("email");
								break;
							}
						}
					}
					String infoStr = rmrp.get("PersonalDateTips",Constants.Platform.ALL,memberName,distId,creditBalance,phoneNum,name,email,star);
					channel.send(infoStr);
				}else{
					JSONObject error = JSONObject.fromObject(errormsg);
					int code = error.getInt("code");
					if(code == 401)
					{
						String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
						String userLoginTimeOut = rmrp.get("UserLoginTimeOut", Constants.Platform.ALL,userLoginUrl);
						channel.send(userLoginTimeOut);
					}else if(code == 500)
					{
						int status = error.getJSONObject("message").getInt("status");
						if(10086==status)
						{
							String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
							channel.send(soaInsideError);
						}
					}
				}
				
				exit();
				return;
			}
		}
		
		String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
		String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
		String notbind = rmrp.get("NotBindTips",Constants.Platform.ALL,"个人资料查询",userLoginUrl,userBindUrl);
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
