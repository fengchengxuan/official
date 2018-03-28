package com.eastrobot.robotdev.handler;



import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.InfoService;
import com.eastrobot.robotdev.nuskin.service.WxBindService;
import com.eastrobot.robotdev.nuskin.utils.EidCacheUtils;
import com.eastrobot.robotdev.nuskin.utils.NuskinUtils;
import com.eastrobot.robotdev.utils.MessageDigestUtils;
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

@Component("eval")
@Scope(HandlerScope.SESSION)
public class PerformanceHandler implements Handler<CommandEvent>{
	
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private HandlerHelper help = HandlerHelper.getInstance();
	private Logger log = Logger.getLogger(this.getClass());
	private String eid;
	
	
	private WxBindService wxBindService = WxBindService.getInstance();
	private InfoService infoService = InfoService.getInstance();
	private int step;
	private JSONArray json = NuskinUtils.getEvalMenu();
	private MessageDigestUtils mdu = MessageDigestUtils.getInstance();
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			RobotSession session = (RobotSession) channel.getRealSession();
			Map<String,String> map = (Map<String,String>)session.getAttribute("key_user_attributes");
			String soatype = map.get("soatype");
			String openid = channel.getUser().getID();
			//查询SOA接口必须是否已经绑定用户
			JSONObject wxjson = wxBindService.isbind(openid,soatype);
		//查询SOA接口必须是否已经绑定用户
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
			this.doDisponse(channel, openid,soatype);
			
		} catch (Exception e) {
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			exit();
			e.printStackTrace();
		}finally{
			step++;
		}
	}
	
	private void doDisponse(Channel channel,String openid,String soatype) throws Exception
	{
		if(ValidationUtils.isNotEmpty(eid))
		{
			PersonalData pd = infoService.getInfo(eid);
			if(ValidationUtils.isNotEmpty(pd))
			{
				
				StringBuilder sb = new StringBuilder();
				String pro_url = rmrp.get("Xiaoi_Dev_Host",Constants.Platform.ALL);
				String header = rmrp.get("EvaluateHeader",Constants.Platform.ALL);
				sb.append(header+"\n");
				for(int i=0;i<json.size();i++)
				{
					JSONObject obj = json.getJSONObject(i);
					String quarter = obj.getString(Constants.QUARTER);
					String year = obj.getString(Constants.YEAR);
					String evalution = rmrp.get("EvaluationURL", "all",pro_url,eid,year,quarter);
					String label = rmrp.get("EvaluateLabel",Constants.Platform.ALL,(i+1)+"",evalution,obj.getString(Constants.YEAR),obj.getString(Constants.QUARTER));
					sb.append(label+"\n");
				}
				String footer = rmrp.get("EvaluateFooter",Constants.Platform.ALL);
				if(ValidationUtils.isNotEmpty(footer))
					sb.append(footer);
				channel.send(sb.toString());
				exit();
				return;
			}
		}
		String userBindUrl = rmrp.get("UserBindURL",Constants.Platform.WEIXIN,openid,soatype);
		String userLoginUrl = rmrp.get("UserLoginURL",Constants.Platform.ALL,openid);
		String notbind = rmrp.get("NotBindTips",Constants.Platform.ALL,"绩效评估查询",userLoginUrl,userBindUrl);
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
	
	public static void main(String[] args) {
	}
}
