/**
 * 
 */
package com.eastrobot.robotdev.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指定相关问解析
 * @author Odin
 *
 */
public class RqUtils {
	private static Logger bocLog = LoggerFactory.getLogger(RqUtils.class);
	/**
	 * 解析content组装指定相关问
	 * @param content
	 * @param userId
	 * @return
	 */
	public static Map<String, List<String>> getRelatedQuestion(String content, String userId){
		String[] rqs = content.split("\r\n");
		bocLog.info("rqs length:"+rqs.length);
		List<String> list = new ArrayList<String>();
		for(int i=0;i<rqs.length;i++){
			if(!StringUtils.isEmpty(rqs[i]))
				list.add(rqs[i].substring(rqs[i].indexOf(".")+1));	
		}
		
		//将新的相关问置入到内存中，提供下次使用
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put(userId, list);
		return map;
	}
	
	/**
	 * 解析content组装动态菜单
	 * @param content
	 * @return
	 */
	public static List<String> getDynamicMenu(String content){
		String[] rqs = content.split("\r\n");
		bocLog.info("rqs length:"+rqs.length);
		List<String> list = new ArrayList<String>();
		for(int i=0;i<rqs.length;i++){
			if(!StringUtils.isEmpty(rqs[i]))
				list.add(rqs[i].substring(rqs[i].indexOf(".")+1));	
		}
		return list;
	}
	
	public static void main(String[] args) {
		String content = "天睚时顺于顺\r\n\r\n您可能还关注以下相关问题：\r\n1.纸黄金\r\n2.离岸用户名\r\n3.企业馆登录密码须设置成什么格式？\r\n4.太平洋卡密码挂失及密码重置方法\r\n5.什么是交易密码";
		bocLog.info(content.substring(0, content.indexOf("您可能还关注以下相关问题：")));
		content = content.substring(content.indexOf("您可能还关注以下相关问题：")+13);
		bocLog.info(content);
		Map map = getRelatedQuestion(content, "aaa");
		List list = (List) map.get("aaa");
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
	}
}
