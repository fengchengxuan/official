package com.eastrobot.robotdev.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 常用数据工具类
 * 
 * @author Yale
 * 
 */
public class DataUtils {

	public static long serialNum = 0; // 流水号

	/**
	 * 获取流水号
	 * 
	 * @param length
	 *            格式化长度
	 * @return 流水号
	 */
	public static String createSerialNum(int length) {
		if (serialNum >= 999999) {
			serialNum = 0;
		}
		if (length < 1) {
			length = 6;
		}
		serialNum++;
		DecimalFormat df = new DecimalFormat();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buf.append("0");
		}
		df.applyPattern(buf.toString());

		return df.format(serialNum);
	}

	/**
	 * 创建6位流水号
	 * 
	 * @return 流水号
	 */
	public static String createSerialNum() {
		return createSerialNum(6);
	}

	/**
	 * 根据当前日期返回yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String createDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 根据传入日期返回format格式字符串
	 * 
	 * @return 格式化后的日期字符串
	 */
	public synchronized static String getDateString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 字符串转日期
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param format
	 *            转换格式
	 * @return 转换后的日期对象
	 */
	public static Date getDate(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 返回格式化后的当前日期
	 * 
	 * @param format
	 *            格式
	 * @return 格式后字符串
	 */
	public static String createDateString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 生成指定前缀流水号
	 * 
	 * @return 流水号
	 */
	public static String createFlowNum(String str) {
		return str + createDateString() + createSerialNum();
	}

	/**
	 * 生成流水号
	 * 
	 * @return 流水号
	 */
	public static String createFlowNum() {
		return createDateString() + createSerialNum();
	}

	/**
	 * 获取两个时间分钟差
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 分钟差
	 */
	public synchronized static long getBetweenMin(Date startTime, Date endTime) {
		if (startTime == null || endTime == null) {
			return 0;
		}
		long res = (endTime.getTime() - startTime.getTime()) / (60L * 1000);
		return res == 0 ? 1 : res;
	}

	/**
	 * 获取两个时间分钟差
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 分钟差
	 */
	public synchronized static long getBetweenMin(String startTime,
			String endTime) {
		if (startTime == null || endTime == null) {
			return 0;
		}
		Date start = DataUtils.getDate(startTime, "yyyy-MM-dd HH:mm:ss");
		Date end = DataUtils.getDate(endTime, "yyyy-MM-dd HH:mm:ss");
		return DataUtils.getBetweenMin(start, end);
	}

	/**
	 * 转换答案0和1
	 * 
	 * @param value
	 *            需要转换的值
	 * @return value等于0时返回1，其余返回0
	 */
	public static int transReturn(int value) {
		if (value == 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 秒转分
	 * 
	 * @param s
	 *            秒
	 * @return 分
	 */
	public static long s2m(String s) {
		try {
			long time = 60L;
			long sl = Long.parseLong(s);
			return (sl / time) + 1L;
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * 根据输入流得到字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getString(InputStream is) {
		return getString(is, "gbk");
	}

	public static String getString(InputStream is, String encoding) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, encoding));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}

	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1"); // 选择SHA-1，也可以选择MD5
			byte[] digest = md.digest(inStr.getBytes()); // 返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}
	
	public static void main(String[] args) {
		System.out.println(SHA1("admin0827"));
		System.out.println("09666a68b9f5fbae12b25aa8c9429da778bd48e0");
		System.out.println(createSerialNum(6));
	}
	
	public static String bytetoString(byte[] digest) {
		String str = "";
		String tempStr = "";

		for (int i = 1; i < digest.length; i++) {
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			} else {
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}

}
