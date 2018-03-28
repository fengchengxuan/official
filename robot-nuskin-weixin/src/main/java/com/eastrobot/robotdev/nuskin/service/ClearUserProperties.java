package com.eastrobot.robotdev.nuskin.service;



import com.eastrobot.robotdev.nuskin.utils.NuskinUtils;

public class ClearUserProperties {
	public void clear()
	{
		NuskinUtils.clearCache();
	}
}
