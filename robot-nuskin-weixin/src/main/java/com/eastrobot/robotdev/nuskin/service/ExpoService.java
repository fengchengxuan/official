package com.eastrobot.robotdev.nuskin.service;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;

public class ExpoService {
	private static ExpoService expoService;
	private ExpoService(){}
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	public static ExpoService getInstance()
	{
		if(null==expoService)
			expoService = new ExpoService();
		return expoService;
	}
	private final String host = PropertiesUtil.getValueByKey("soa.request.host");
	//促销活动地址
	private final String expoUrl = host+PropertiesUtil.getValueByKey("addr.expo.url");
	//expo满额礼和新人礼
	private final String promotionUrl = host+PropertiesUtil.getValueByKey("addr.expo.promotion.url");

	
	//获取促销时间
	public JSONObject getCuXiaoInfo()
	{
		try {
			String result = HttpClientUtils.doGetXml(expoUrl,Constants.ENCODE,null);
			if(ValidationUtils.isNotEmpty(result))
			{
				return JSONObject.fromObject(result);
			}
		} catch (Exception e) {
		}
		return null;
	}
	//获取介绍
	public String getIntroduce(int promotionId)
	{
		String introduce = "";
		JSONArray array = this.getActivities();
		JSONObject obj = array.getJSONObject(0);
		if(ValidationUtils.isNotEmpty(obj.get("code")))
		{
			if(10086 == obj.getJSONObject("message").getInt("status"))
			{
				introduce = rmrp.get("SOAInsideError",Constants.Platform.ALL);
			}else{
				introduce = rmrp.get("NoResultFound",Constants.Platform.ALL);
			}
		}else{
			if(ValidationUtils.isNotEmpty(array))
			{
				for(int i=0;i<array.size();i++)
				{
					JSONObject _obj = array.getJSONObject(i);
					if(promotionId ==_obj.getInt("promotionId"))
					{
						introduce = _obj.getString("promotionDesc");
						break;
					}
				}
			}
		}
		return introduce.replaceAll("</?br/?>","\n");
	}
	
	private JSONArray getActivities()
	{
		try {
			String result = HttpClientUtils.doGetXml(promotionUrl,Constants.ENCODE,null);
			System.out.println(result);
			if(ValidationUtils.isNotEmpty(result))
			{
				if(result.startsWith("{"))
				{
					JSONArray array = new JSONArray();
					array.add(result);
					return array;
				}
				else
					return JSONArray.fromObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		System.out.println(getInstance().getIntroduce(Constants.FreshMan_Gift));
//		System.out.println(getInstance().getIntroduce(Constants.Full_QuoTa_Gift));
//		System.out.println(getInstance().getIntroduce(Constants.LUCKY_DRAW));
		System.out.println("论文<br>娃儿</br/>儿玩儿</br>玩<br/>儿".replaceAll("</?br/?>","\n"));
	}
}
