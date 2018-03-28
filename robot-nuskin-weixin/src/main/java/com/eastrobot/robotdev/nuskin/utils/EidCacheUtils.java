package com.eastrobot.robotdev.nuskin.utils;


public class EidCacheUtils {
	private static LRUTCache<String,String> cache = new LRUTCache<String,String>(100000,20*60);
	
	public static String getEidById(String userid)
	{
		String eid = cache.get(userid);
		System.out.println("获取登陆eid:"+userid+"|"+eid);
		return eid;
	}
	
	public static void putEidById(String userid,String eid)
	{
		cache.put(userid,eid);
		System.out.println("存入eid："+userid+"|"+eid);
	}
	
	public static void removeEidById(String userid)
	{
		cache.remove(userid);
		System.out.println("删除用户登陆状态："+userid);
	}
}
