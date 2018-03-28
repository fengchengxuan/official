package com.eastrobot.robotdev.utils;

/**
 * 验证工具类
 * 
 * @author Yale
 * 
 */
public class ValidationUtils {

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 验证对象是否为空或者空字符串
	 * 
	 * @param object
	 *            需要验证的对象
	 * @return 为空返回true, 否则返回false
	 */
	public static boolean isEmpty(Object object) {
		return object == null;
	}

	/**
	 * 验证对象是否不为空或者空字符串
	 * 
	 * @param object
	 *            需要验证的对象
	 * @return 不为空返回true, 否则返回false
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

}
