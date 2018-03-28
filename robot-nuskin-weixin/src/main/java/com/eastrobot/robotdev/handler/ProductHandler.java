package com.eastrobot.robotdev.handler;

import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.Product;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;

@Component("product")
@Scope(HandlerScope.SESSION)
public class ProductHandler implements Handler<CommandEvent>{

	private final String proUrl = PropertiesUtil.getValueByKey("soa.request.host")+PropertiesUtil.getValueByKey("product.search.url");
	private HandlerHelper help = HandlerHelper.getInstance();
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	@Override
	public void handle(Channel channel, CommandEvent event) {
		try {
			String[] param = (String[])help.param.get();
			if(ValidationUtils.isNotEmpty(param) && param.length>0)
			{
				String keyword = param[0];
				String url = proUrl+URLEncoder.encode(keyword,"utf-8");
				String result = HttpClientUtils.doGetXml(url,"utf-8",null);
				System.out.println(result);
				if(ValidationUtils.isNotEmpty(result))
				{
					if(result.startsWith("["))
					{
						JSONArray jsonArray = new JSONArray();
						JSONArray array = JSONArray.fromObject(result);
						for(int i=0;i<array.size();i++)
						{
							JSONObject obj = array.getJSONObject(i);
							Product product = JsonUtils.convertToObj(obj,Product.class);
							obj.clear();
							obj.put(Constants.ImgTxtMsg.TITLE,product.getStockName());
							String description = "";
							obj.put(Constants.ImgTxtMsg.DESCRIPTION,description);
							String img_url = PropertiesUtil.getValueByKey("addr.prodimage.host")+product.getMediumIconFileName();
							obj.put(Constants.ImgTxtMsg.PIC_URL,img_url);
							String pro_url = rmrp.get("Xiaoi_Dev_Host",Constants.Platform.ALL)+"product!show.action?id="+product.getStockId();
							obj.put(Constants.ImgTxtMsg.URL,pro_url);
							jsonArray.add(obj);
						}
						JSONObject jobj = new JSONObject();
						jobj.put("articles",jsonArray);
						System.out.println(jobj);
						channel.send(Constants.IMG_TXT_MSG+jobj.toString());
					}else if(result.startsWith("{")){
						JSONObject obj = JSONObject.fromObject(result);
						int code = obj.getInt("code");
						if(500==code)
						{
							int status = obj.getJSONObject("message").getInt("status");
							if(10086 == status)
							{
								String soaInsideError = rmrp.get("SOAInsideError",Constants.Platform.ALL);
								channel.send(soaInsideError);
							}else{
								channel.send("没有查询到相关产品");
							}
						}
					}
					exit();
					return;
				}
			}
			toAi();
			exit();
			return;
		} catch (Exception e) {
			String errortips = rmrp.get("ServiceErrorTips",Constants.Platform.ALL);
			channel.send(errortips);
			exit();
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
