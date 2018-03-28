package com.eastrobot.robotdev.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
/**
 * @author rickey
 * 		将 “一亿” 以内的大写中文数字转换为阿拉伯型数字。
 * 		注：只能转换正数，如果返回-1怎说明出错了。
 * */
public class NumberUtils {
	private final static String Chinese_Num = "1|零|一|壹|2|两|俩|倆|二|贰|貳|3|三|叁|弎|4|四|肆|5|五|伍|6|六|陆|陸|7|七|柒|8|八|捌|9|九|玖";
	private final static String jsonNum = "{0:0,零:0,1:1,一:1,壹:1,2:2,两:2,俩:2,倆:2,二:2,贰:2,貳:2,3:3,三:3,叁:3,弎:3,4:4,四:4,肆:4,5:5,五:5,伍:5,6:6,六:6,陆:6,陸:6,7:7,七:7,柒:7,8:8,八:8,捌:8,9:9,九:9,玖:9}";
	private final static String Units = "十|拾|百|佰|k|K|千|仟|w|W|万|萬";
	private final static String jsonUnits = "{十:10,拾:10,百:100,佰:100,k:1000,K:1000,千:1000,仟:1000,w:10000,W:10000,万:10000,萬:10000}";
	
	//该方法只能将最大单位是"万"的中文数字转化成阿拉伯数字
	public static double Chinese2Dbl(String chineseNum){
		try{
			JSONObject json_Num = JSONObject.fromObject(jsonNum);
			JSONObject json_Units = JSONObject.fromObject(jsonUnits);
			StringBuffer sb = new StringBuffer(chineseNum.replaceAll("点","."));
			Pattern pat = Pattern.compile(Chinese_Num);
			Matcher mat = pat.matcher(sb);
			Map<Integer, Integer> numMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> unitMap = new HashMap<Integer, Integer>();
			
			/**1.查找字符串中的中文数字，并替换成阿拉伯数字。
			 * 2.将替换后的阿拉伯数字的和对应的位置以位置为键值的形式存入map
			 * */
			while(mat.find()){
				String num = json_Num.get(mat.group()).toString();
				int end = mat.end();
				numMap.put(end, Integer.parseInt(num));
				sb.replace(end-1, end, num);
			}
			
			Pattern pat1 = Pattern.compile(Units);
			Matcher mat1 = pat1.matcher(sb);
			/**1.查找字符串中的中文单位。
			 * 2.将查找到的中文单位转换成阿拉伯单位。
			 * 3.以单位对应的位置为键值的形式将单位存入map
			 * */
			while(mat1.find())
			{
				String unit = json_Units.get(mat1.group()).toString();
				int end = mat1.end();
				unitMap.put(end, Integer.parseInt(unit));
			}
	//		System.out.println(sb);
	//		System.out.println("num:"+numMap);
	//		System.out.println("unit:"+unitMap);
			double result = 0;
			
			/**
			 * 1.如果不存在中文单位，则直接返回替换后的字符串。
			 * 2.如果存在中文单位，则进行如下解析。
			 * */
			if(unitMap.size()>0){
				Set<Integer> uset = unitMap.keySet();
				Set<Integer> nset = numMap.keySet();
				String startStr = "",endStr = "";
				double startNum = 0,endNum = 0;
				int uMax = -1,nMax = -1,uMin = 10,nMin = 10,unitMaxKey=-1,unit = 0;
				//获取单位map中索引值最大的键和最小的键以及键值最大的键
				for(Integer key:uset)
				{
					if(key>uMax)
						uMax=key;
					if(key<uMin)
						uMin=key;
					
					unit = Math.max(unit,unitMap.get(key));
					unitMaxKey = unit==unitMap.get(key)?key:unitMaxKey;
				}
				//获取数字map中索引值最大的键和最小的键
				for(Integer key:nset)
				{
					if(key>nMax)
						nMax=key;
					if(key<nMin)
						nMin=key;
				}
				/**
				 *1.如果数字中最小的键小于单位中最小的键,说明第一个单位前面有数字。
				 *2.截取第一个单位前面的数字作为起始值。例如20万零一，此时20作为起始值。
				 */
				if(nMin<uMin)
				{
					startStr = sb.substring(0,uMin-1);
					startNum = Double.parseDouble(startStr);
				}
				//System.out.println("start:"+startStr);
				/**
				 * 1.如果数字中最大的键大于单位中最大的键，说明最后一个单位后面还有数字。
				 * 2.截取最后一个单位后面的数字作为末尾值，例如20万零一，此时01就
				 * */
				if(nMax>uMax)
					endStr = sb.substring(uMax);
				
				//System.out.println("endStr before:"+endStr);
				if(endStr.startsWith("0")){
					while(endStr.startsWith("0"))
					{
						endStr = endStr.substring(1);
					}
				}else if(ValidationUtils.isNotEmpty(endStr)){
					endStr = (Double.parseDouble(endStr)*unitMap.get(uMax)/Math.pow(10,endStr.length()))+"";
				}
				
				//System.out.println("endStr after:"+endStr);
				endNum = ValidationUtils.isNotEmpty(endStr)?Double.parseDouble(endStr):0;
				
				
				for(Integer key:uset)
				{
					//如果第一个字符不为单位，则将单位乘以前一位置
					if(1!=key)
					{
						if(key>uMin)
						{
							if(numMap.get(key-1)!=null)
							{
								if(key==unitMaxKey)
									result = (result+numMap.get(key-1))*unitMap.get(key);
								else
									result += unitMap.get(key)*numMap.get(key-1);
							}else{
								result = result*unitMap.get(key);
							}
						}else
							result = startNum*unitMap.get(key);
					}else{
						result += unitMap.get(key);
					}
				}
				
				result+=endNum;
				
			}else{
				result = Integer.parseInt(sb.toString());
			}
			
			return result;
		}catch(Exception e){
			return -1;
		}
	}

	
	public static void main(String[] args) {
		System.out.println(Chinese2Dbl("贰万1十4"));
	}
	
	//返回32为流水字符号
	public static String get32SerialNum(){
		StringBuffer sb = new StringBuffer(UUID.randomUUID().toString().replaceAll("-",""));
		String time = DateUtils.date2Str(new Date(), "yyMMddHHmmssSSS");
		sb.delete(0,time.length()).append(time);
		return sb.toString();
	}
}
