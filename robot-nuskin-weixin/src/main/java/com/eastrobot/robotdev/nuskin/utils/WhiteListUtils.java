package com.eastrobot.robotdev.nuskin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;

public class WhiteListUtils {
	private static RobotMessageResource rmrp = new RobotMessageResourceProxy();
	
	public static boolean isWhiteList(String userId)
	{
		String startUseWhiteList = rmrp.get("StartUseWhiteList",Constants.Platform.WEIXIN);
		if("true".equalsIgnoreCase(startUseWhiteList))
		{
			String listStr = rmrp.get("WhiteList",Constants.Platform.WEIXIN);
			String key = "WhiteList_other";
			int num = 0;
			while(true)
			{
				num++;
				String res = rmrp.get(key+num,Constants.Platform.WEIXIN);
				if(ValidationUtils.isEmpty(res))
					break;
				else
					listStr = listStr+","+res;
			}
			List<String> list = new ArrayList<String>();
			if(ValidationUtils.isNotEmpty(listStr))
			{
				String[] str = listStr.split(",");
				for(String s:str)
					list.add(s);
			}
			return list.contains(userId);
		}else{
			return true;
		}
	}
	
}
