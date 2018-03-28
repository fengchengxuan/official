/**
 * 
 */
package com.eastrobot.robotdev.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.eastrobot.robotdev.vo.PersonalData;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * @author Odin
 * 
 */
public class JsonUtils {
	public static <T> T convertToObj(JSONObject jsonObject, Class<T> cla) {
		if (jsonObject == null)
			return null;
		Field[] fb = cla.getDeclaredFields();
		T t;
		try {
			t = cla.newInstance();
			for (int j = 0; j < fb.length; j++)
			{
				String fieldName = fb[j].getName();
				String fieldNameU = fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
				Method method = cla.getMethod("set" + fieldNameU,fb[j].getType());
				Object info = jsonObject.get(fieldName);
				if(null!=info && JSONNull.getInstance()!=info && !"null".equals(info.toString()))
					method.invoke(t,String.valueOf(info));
			}
			return t;

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> convertToList(JSONArray jsonArray, Class<T> cla) {
		List<T> list = new ArrayList<T>();
		if (jsonArray == null)
			return list;
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				T t = convertToObj(jsonObject, cla);
				list.add(t);
			}

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 针对unskin判断是都绑定
	 * @param object
	 * @return
	 */
	public static boolean isJson(JSONObject object){
		
		if(!object.isEmpty()){
			
			JSONObject json = JSONObject.fromObject(object);
			String json_ = json.toString();
			if(json_.indexOf("status") != -1 && json_.indexOf("message") != -1){
				int status = json.getInt("status");
				if(status == 1){
					return false;
				}
			} else if(json_.indexOf("eid") != -1 && json_.indexOf("expiringInSeconds") != -1
					&& json_.indexOf("expiringTime") != -1){
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {
		String s = "{\"memberId\":970292,\"memberTypeCd\":1,\"memberName\":\"龚丽英\",\"memberPassword\":null,\"distId\":\"CN9610120\",\"memberEmail\":\"784474711@qq.com\",\"phoneNumber\":\"15159945960\",\"createdDtm\":null,\"lastLoginDtm\":null,\"accountStatus\":1,\"shoppingAllow\":\"Y\",\"aroStatus\":{\"buyerId\":null,\"aroId\":\"AR02874159\",\"aroStatusCd\":1},\"entryWhsId\":\"CNDNFZ06\",\"entryWhsName\":\"福州如新生活形象店\",\"creditBalance\":0.0,\"prodCreditBalance\":0,\"memberLevelCd\":1,\"titleName\":null,\"titleLevelCd\":0,\"titleLevelDesc\":null,\"distEmails\":[{\"emailType\":1,\"name\":\"784474711\",\"email\":\"784474711@qq.com\",\"rowId\":\"QUFBU2p5QUFNQUFDU0prQUFJ\",\"isPrimary\":true}],\"notEmpty\":true}";
		PersonalData pd = convertToObj(JSONObject.fromObject(s),PersonalData.class);
		System.out.println(pd.getDistEmails());
	}
}
