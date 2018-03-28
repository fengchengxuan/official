package com.eastrobot.robotdev.nuskin.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.utils.AnalysisXmlUtils;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.PropertiesUtil;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.eastrobot.robotface.domain.RobotCommand;
import com.eastrobot.robotface.domain.RobotResponseEx;
import com.sun.xml.wss.impl.misc.Base64;


public class NuskinUtils {
	private static RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private static LRUTCache<String,String> cache = new LRUTCache<String,String>(100000,24*3600);
	public static void putCache(String userId,String value)
	{
		cache.put(userId, value);
	}
	public static String getValue(String userId)
	{
		return cache.get(userId);
	}
	public static void clearCache()
	{
		cache.clear();
		System.out.println("============清除温馨提示缓存============");
	}
	//获取当前绩效评估的年份季度
	public static JSONArray getEvalMenu()
	{
		JSONArray array = new JSONArray();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int mouth = cal.get(Calendar.MONTH)+1;
		int quarter = 0;
		if(mouth<=3)
			quarter = 1;
		else if(mouth<=6)
			quarter = 2;
		else if(mouth<=9)
			quarter = 3;
		else
			quarter = 4;
		
		for(int i=0;i<4;i++)
		{
			JSONObject obj = new JSONObject();
			quarter = quarter-1;
			year = quarter>0?year:year-1;
			quarter = quarter>0?quarter:4;
			obj.put(Constants.QUARTER, quarter+"");
			obj.put(Constants.YEAR, year+"");
			array.add(obj);
		}
		return array;
	}
	//改变commands
	public static void changeCommands(RobotResponseEx response,String openid)
	{
		if(ValidationUtils.isNotEmpty(response))
		{
			RobotCommand[] commands = response.getCommands();
			if(null!=commands && commands.length>0)
			{
				for(int i=0;i<commands.length;i++)
				{
					RobotCommand command = commands[i];
					String name = command.getName();
					if("imgtxtmsg".equalsIgnoreCase(name))
					{
						String[] str = command.getArgs();
						//String xml = str[3];
						str[3] = AnalysisXmlUtils.xml2json(str[3]);
						command.setArgs(str);
					}
					//处理图片消息
					else if("imgmsg".equalsIgnoreCase(name))
					{
						String[] str = command.getArgs();
						String image = str[0];
						String mediaId;
						try {
							Map<String,String> map = MediaUtils.getInstance().getMediaId(openid);
							if(ValidationUtils.isNotEmpty(map))
							{
								if(!map.containsKey(image))
								{
									mediaId = MediaUtils.getInstance().send(image,openid);
									if(!"error".equalsIgnoreCase(mediaId))
									{
										map.put(image,mediaId);
										MediaUtils.getInstance().putCache(openid, map);
									}
								}else{
									mediaId = map.get(image);
								}
							}else{
								map = new HashMap<String, String>();
								mediaId = MediaUtils.getInstance().send(image,openid);
								if(!"error".equalsIgnoreCase(mediaId))
								{
									map.put(image,mediaId);
									MediaUtils.getInstance().putCache(openid, map);
								}
							}
							if(mediaId.equalsIgnoreCase("error") || mediaId.equals(image))
							{
								String cMSUploadImageFailed = rmrp.get("CMSUploadImageFailed",Constants.Platform.WEIXIN);
								response.setCommands(null);
								response.setContent(cMSUploadImageFailed);
							}else{
								str[0] = mediaId;
								command.setArgs(str);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			String content = response.getContent();
			if(ValidationUtils.isNotEmpty(content) && content.startsWith(Constants.IMG_TXT_MSG))
			{
				RobotCommand[] rc = new RobotCommand[1];
				RobotCommand command = new RobotCommand();
				command.setName("imgtxtmsg");
				command.setState(1);
				String[] args = new String[4];
				args[0] = "";
				args[1] = "1";
				args[2] = "UTF-8";
				content = content.replaceFirst(Constants.IMG_TXT_MSG,"").replaceAll("\r\n|\n","");
				args[3] = content;
				command.setArgs(args);
				rc[0] = command;
				response.setCommands(rc);
				response.setContent("");
			}
		}
	}
	
	public static void sendFaqVote(final Question question)
	{
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				String url = PropertiesUtil.getValueByKey("localhost.addr")+"/robot/faq-vote.action?jsoncallback=true&platform=weixin";
				Map<String,String> params = new HashMap<String, String>();
				try {
					params.put("sessionId",question.getSessionId());
					params.put("userId",question.getUserId());
					String que = new String(Base64.decode(question.getQuestion()),"gbk");
					que = new String(getUTF8BytesFromGBKString(que));
					params.put("question",changeFormat(que,"utf-8","iso-8859-1"));
					
					params.put("faqId",question.getFaqId());
					String answer = new String(Base64.decode(question.getAnswer()),"gbk");
					answer = new String(getUTF8BytesFromGBKString(answer));
					params.put("answer",changeFormat(answer,"utf-8","iso-8859-1"));
					
					params.put("reason",changeFormat(question.getReason(),"utf-8","iso-8859-1"));
					params.put("option","2");
					String result = HttpClientUtils.doPost(url, params);
					System.out.println("提交未解决："+result.equals(""));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		ThreadPoolUtils.pool.execute(thread);
	}
	
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
        int n = gbkStr.length();  
        byte[] utfBytes = new byte[3 * n];  
        int k = 0;  
        for (int i = 0; i < n; i++) {  
            int m = gbkStr.charAt(i);  
            if (m < 128 && m >= 0) {  
                utfBytes[k++] = (byte) m;  
                continue;  
            }  
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
        }  
        if (k < utfBytes.length) {  
            byte[] tmp = new byte[k];  
            System.arraycopy(utfBytes, 0, tmp, 0, k);  
            return tmp;  
        }  
        return utfBytes;  
    }
	public static String changeFormat(String str,String oldEncode,String newEncode)
	{
		String res = "";
		try {
			if(ValidationUtils.isNotEmpty(str))
				str = str.replaceAll("\r","").replaceAll("\n","").replaceAll(" ","");
			res = new String(str.getBytes(oldEncode),newEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
//		String question = "{\"answer\":\"x+vBqs+1yOfQwr/Nt/7IyM/fNDAwLTAwNC01Njc4oaM=\",\"faqId\":\"00143073229274276304005056954d20\",\"msgId\":\"8862b0111263447a8bbd0ee3405b92cf\",\"option\":\"2\",\"question\":\"1PXDtMihz/u2qbWl\",\"reason\":\"答案与问题不匹配\",\"sessionId\":\"95c2ee77d76b4a68aa4acf9a7f76204d\",\"standardQ\":\"tqm1pbXEyKHP+7e9t6g=\",\"userId\":\"xiaoitest\"}";
//		String url = "http://xiaoitest.cn.nuskin.com:8000/robot/faq-vote.action?jsoncallback=true&platform=weixin";
//		StringBuilder sb = new StringBuilder(url);
//		Question q = JsonUtils.convertToObj(JSONObject.fromObject(question),Question.class);
//		sendFaqVote(q);
//		sb.append("&sessionId="+q.getSessionId());
//		sb.append("&userId="+q.getUserId());
//		sb.append("&question="+URLEncoder.encode(q.getQuestion(),"utf-8"));
//		sb.append("&faqId="+q.getFaqId());
//		sb.append("&answer="+URLEncoder.encode(q.getAnswer(),"utf-8"));
//		sb.append("&reason="+URLEncoder.encode(q.getReason(),"utf-8"));
//		sb.append("&option=2");
//		HttpClientUtils.doGetXml(sb.toString(),"utf-8",null);
		byte[] byt = Base64.decode("tqm1pcihz/s=");
		System.out.println(new String(byt,"gbk"));
		System.out.println(new String(byt,"utf-8"));
	}
}
