package com.eastrobot.robotdev.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtils {

	/**
	 * 查找字符串中符合表达式的字符
	 * 
	 * @param str
	 * @param regEx
	 * @return
	 */
	public static String findStr(String str, String regEx) {
		String result = null;
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		boolean isFind = mat.find();
		if (isFind) {
			result = mat.group(1);
		}
		return result;
	}

	public static boolean validateStr(String str, String regEx) {
		if (ValidationUtils.isEmpty(str)) {
			return false;
		}
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str.toLowerCase());
		boolean isFind = mat.matches();
		return isFind;
	}

}
