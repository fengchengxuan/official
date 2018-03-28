package com.eastrobot.robotdev.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日期工具类
 * 
 * @author Yale
 * 
 */
public abstract class DateUtils {

	protected static final Logger log = LoggerFactory
			.getLogger(DateUtils.class);
	private static final String SIMPLE_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期类型转字符串
	 * 
	 * @param date
	 *            需要转换的日期
	 * @param sdf
	 *            日期格式
	 * @return 转换后的字符串
	 */
	private static String date2Str(Date date,SimpleDateFormat sdf){
		return sdf.format(date);
	}
	/**
	 * 字符串转日期类型
	 * 
	 * @param str
	 *            需要转换的日期
	 * @param sdf
	 *            日期格式
	 * @return 转换后的日期对象
	 */
	private static Date str2Date(String str,SimpleDateFormat sdf){
		Date date=null;
		try {
			date=sdf.parse(str);
		} catch (ParseException e) {
			log.error("",e);
		}
		return date;
	}
	/**
	 * 日期类型转字符串
	 * 
	 * @param date
	 *            需要转换的日期
	 * @return 转换后的字符串(默认格式"yyyy-MM-dd HH:mm:ss")
	 */
	public static String date2Str(Date date){
		return DateUtils.date2Str(date, SIMPLE_DATE_FORMATE);
	}
	/**
	 * 日期类型转字符串
	 * 
	 * @param date
	 *            需要转换的日期
	 * @param format
	 *            需要转换的格式
	 * @return 按format格式转换后的字符串
	 */
	public static String date2Str(Date date,String format){
		return DateUtils.date2Str(date, new SimpleDateFormat(format));
	}
	/**
	 * 字符串转日期类型
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @return 转换后的日期对象(默认格式"yyyy-MM-dd HH:mm:ss")
	 */
	public static Date str2Date(String str) {
		return DateUtils.str2Date(str, SIMPLE_DATE_FORMATE);
	}

	/**
	 * 字符串转日期类型
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param format
	 *            日期格式
	 * @return 按format格式转换后的日期对象
	 */
	public static Date str2Date(String str, String format) {
		return DateUtils.str2Date(str, new SimpleDateFormat(format));
	}
	//获取GMT时间
	public static String getGMTdate()
	{
		Calendar cd = Calendar.getInstance();  
	    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
	    sdf.setTimeZone(TimeZone.getTimeZone("GMT")); 
	    String timeStr = sdf.format(cd.getTime());
		return timeStr;
	}
	
	public static String long2Str(Long time,String format)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		return date2Str(cal.getTime(),format);
	}
	/**
	 * 获取日期相关信息
	 * 
	 * @param date
	 *            日期对象
	 * @param field
	 *            Calendar中定义的静态属性
	 * @return 日期相关信息
	 */
	public static int getField(Date date,int field){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(field);
	}

	public static long compareMin(Date startTime, Date endTime) {
		long ms = endTime.getTime() - startTime.getTime();
		return ms / 1000 / 60;
	}
	public static void main(String[] args) {
		String time = "20150126 114415";
		Date d = str2Date(time,"yyyyMMdd HHmmss");
		String s = date2Str(d,"yyyy-MM-dd HH:mm:ss");
		System.out.println(s);
	}
}
