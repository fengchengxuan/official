package com.eastrobot.robotdev.handler;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.service.ExpoService;
import com.eastrobot.robotdev.utils.DateUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.annotations.handler.StartWith;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;

@Component("expo")
@Scope(HandlerScope.SESSION)
public class ExpoActivitiesHandler implements Handler<CommandEvent>{

	private ExpoService expoService = ExpoService.getInstance();
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private HandlerHelper help = HandlerHelper.getInstance();
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			String type = "";
			String[] str = (String[]) help.param.get();
			if(ValidationUtils.isNotEmpty(str))
			{
				String introduce="暂无相关信息";
				type = str[0];
				//满额礼
				if(type.equalsIgnoreCase("mel"))
				{
					introduce = expoService.getIntroduce(Constants.Full_QuoTa_Gift);
				}else if(type.equalsIgnoreCase("xrl")){
					introduce = expoService.getIntroduce(Constants.FreshMan_Gift);
				}else if(type.equalsIgnoreCase("cxhd")){
					JSONObject obj = expoService.getCuXiaoInfo();
					if(ValidationUtils.isNotEmpty(obj))
					{
						if(ValidationUtils.isEmpty(obj.get("code")))
						{
//							String mouth = DateUtils.date2Str(DateUtils.str2Date(obj.getString("beginString"),"yyyyMMdd HHmm"),"M");
//							String begin = DateUtils.date2Str(DateUtils.str2Date(obj.getString("beginString"),"yyyyMMdd HHmm"),Constants.DATE_FORMAT);
//							String end = DateUtils.date2Str(DateUtils.str2Date(obj.getString("endString"),"yyyyMMdd HHmm"),Constants.DATE_FORMAT);
							
							String mouth = StringUtils.substringBetween(obj.getString("beginString"),"年","月");
							String begin = obj.getString("beginString");
							String end = obj.getString("endString");
							introduce = rmrp.get("SalePromotionTips","all",mouth,begin,end);
						}else{
							if(10086 == obj.getJSONObject("message").getInt("status"))
							{
								introduce = rmrp.get("SOAInsideError",Constants.Platform.ALL);
							}else{
								introduce = rmrp.get("NoResultFound",Constants.Platform.ALL);
							}
						}
					}
				}else if(type.equalsIgnoreCase("lucky")){
					introduce = expoService.getIntroduce(Constants.LUCKY_DRAW);
				}
				channel.send(introduce);
			}else{
				toAi();
			}
			exit();
			return;
		} catch (Exception e) {
			// TODO: handle exception
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
