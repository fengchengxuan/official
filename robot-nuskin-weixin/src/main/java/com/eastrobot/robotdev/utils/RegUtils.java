/**
 * 
 */
package com.eastrobot.robotdev.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Odin
 *
 */
public class RegUtils {
	private static Logger bocLog = LoggerFactory.getLogger(RegUtils.class);
	
	public static int isNaN(String input){
		input = input.replaceAll("\\pP|\\pS", "");//过滤标点符号
		bocLog.info(input);
		
		if(input.length()<1){
			//纯标点符号
			return 1;
		}
		
		Pattern p = Pattern.compile("^[0-9]*$");//检查是否为纯数字
		Matcher m = p.matcher(input);
		if(input.length()!=4 && m.find()){
			//纯数字
			return 2;
		}
		p = Pattern.compile("^[A-Za-z]+$");
		m = p.matcher(input);
		if(m.find()){
			if(input.equalsIgnoreCase("hello") || input.equalsIgnoreCase("hi")){
				return 0;
			}
			bocLog.info("1");
			//纯字母
			return 3;
		}
		p = Pattern.compile("[\u4e00-\u9fa5]");
		m = p.matcher(input);
		bocLog.info("input length 41:"+input.length());
		if(m.find() && input.length()==1){
			bocLog.info("2");
			//纯汉字
			return 4;
		}
		return 0;
	}
	
	public static boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("^[0-9]*$"); 
		Matcher isNum = pattern.matcher(str); 
		if( !isNum.matches() ){ 
			return false; 
		} 
		return true; 
	} 
	
	public static void main(String[] args) {
		String a = "是";
		System.out.println(isNaN(a));
//		String b = "-42342342";
//		System.out.println(isNumeric(b));
	}
}
