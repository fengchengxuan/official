package com.eastrobot.robotdev.nuskin.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.nuskin.utils.MediaUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;


public class UpLoadImageService{
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private Logger log = Logger.getLogger(this.getClass());
	
	private List<String> getFileContents()
	{
		String path = this.getClass().getResource("/").getPath();
		String robotPath = path.substring(0,path.indexOf("dev"))+"robot/attachments/";
		log.info("图片地址："+robotPath);
		List<String> list = new ArrayList<String>();
		File dir = new File(robotPath);
		File[] fl = dir.listFiles();
		for(File f:fl)
		{
			String name = f.getName();
			System.out.println("picture name:"+name);
			list.add(name);
		}
		return list;
	}
	
	public void upload()
	{
		List<String> list = this.getFileContents();
		String gongzhonghao = rmrp.get("WeixinOpenId",Constants.Platform.WEIXIN);
		JSONArray array = JSONArray.fromObject(gongzhonghao);
		for(int i=0;i<array.size();i++)
		{
			int num = 0;
			String openid = array.getJSONObject(i).getString("openId");
			Map<String,String> map = new HashMap<String, String>();
			for(String name:list)
			{
				try {
					String mediaId = MediaUtils.getInstance().send(name,openid);
					if(!"error".equalsIgnoreCase(mediaId))
					{
						try {
							map.put(name,mediaId);
							num++;
							log.info("图片【"+name+"】上传成功");
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			
			try {
				MediaUtils.getInstance().putCache(openid, map);
				log.info("共"+list.size()+"张图片，成功上传至【"+array.getJSONObject(i).getString("name")+"】"+num+"张图片");
			} catch (Exception e){
			}
			
		}
		
	}
	
}
