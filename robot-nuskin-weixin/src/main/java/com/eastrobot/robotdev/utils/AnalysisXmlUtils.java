package com.eastrobot.robotdev.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.eastrobot.robotdev.Constants;
/**
 * 
 * 
 * */
public class AnalysisXmlUtils {
	private static final Logger log = Logger.getLogger(AnalysisXmlUtils.class);
	
	//获取xml文件内容
	public static String getXmlString(String addr)
	{
		StringBuffer sb = new StringBuffer("");
		try {
			File file = new File(addr);
			if(file.exists())
			{
				FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
				BufferedReader br = new BufferedReader(inputStreamReader);
				String line;
				while(ValidationUtils.isNotEmpty(line=br.readLine()))
					sb.append(line);
				
				log.debug("原xml:"+sb);
				br.close();
				inputStreamReader.close();
				fileInputStream.close();
			}else
			{
				log.debug("文件不存在~~~");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}
	
	public static List<Map<String, String>> getXmlMap(String addr)
	{
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		try {
			String xml = getXmlString(addr);
			if(ValidationUtils.isNotEmpty(xml))
			{
				Document document = DocumentHelper.parseText(xml);
				Element rootElement = document.getRootElement();
				List<Element> list = rootElement.elements("item");
				Iterator<Element> items = list.iterator();
				while(items.hasNext())
				{
					Element item = items.next();
					String title = item.elementText("Title");
					String description = item.elementText("Description");
					String picUrl = item.elementText("PicUrl");
					String url = item.elementText("Url");
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", title);
					map.put("description", description);
					map.put("picUrl", picUrl);
					map.put("url", url);
					result.add(map);
				}
			}else{
				log.debug("文件为空~~~~");
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getXml(String addr)
	{
		String xml = null;
		List<Map<String, String>> result = getXmlMap(addr);
		if(result.size()>0)
		{
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("Articles");
			for(Map<String, String> map : result)
			{
				Element item = root.addElement("item");
				item.addElement("Title").addCDATA(map.get("title"));
				item.addElement("Description").addCDATA(map.get("description"));
				item.addElement("PicUrl").addCDATA(map.get("picUrl"));
				item.addElement("Url").addCDATA(map.get("url"));
			}
			xml = doc.asXML();
			log.debug("articles xml is:"+xml);
		}
		return xml;
	}
	
	public static String xml2json(String xml)
	{
		try {
			if(ValidationUtils.isNotEmpty(xml))
			{
				xml = xml.replaceFirst("<!\\[CDATA\\[","");
				xml = xml.substring(0,xml.lastIndexOf("]]>"));
				JSONObject obj = new JSONObject();
				Document doc = DocumentHelper.parseText(xml);
				Element articles = doc.getRootElement();
				List<Element> list = articles.elements("item");
				
				Iterator<Element> items = list.iterator();
				JSONArray array = new JSONArray();
				while(items.hasNext())
				{
					Element item = items.next();
					String title = item.elementText("Title");
					String description = item.elementText("Description");
					String picUrl = item.elementText("PicUrl");
					if(picUrl.indexOf("imgmsgData")==0)
						picUrl = PropertiesUtil.getValueByKey("robot.context.addr")+picUrl;
					String url = item.elementText("Url");
					if(url.indexOf("imgmsgData")==0)
						url = PropertiesUtil.getValueByKey("robot.context.addr")+url;
					
					JSONObject jObject = new JSONObject();
					jObject.put(Constants.ImgTxtMsg.TITLE, title);
					jObject.put(Constants.ImgTxtMsg.DESCRIPTION, description);
					jObject.put(Constants.ImgTxtMsg.PIC_URL, picUrl);
					jObject.put(Constants.ImgTxtMsg.URL, url);
					array.add(jObject);
				}
				obj.put("articles",array);
				return obj.toString();
			}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		return "";
	}
	public static void main(String[] args) {
		String xml = "<![CDATA[<Articles time=\"2015年02月09日\">\n<item>\n<Title><![CDATA[单图文]]></Title>\n<Description><![CDATA[这里是测试摘要]]></Description>\n<PicUrl><![CDATA[http://www.baidu.com/img/bdlogo.png]]></PicUrl>\n<Url><![CDATA[http://www.baidu.com]]></Url>\n</item>\n\n<item>\n<Title><![CDATA[单图文]]></Title>\n<Description><![CDATA[这里是测试摘要]]></Description>\n<PicUrl><![CDATA[http://www.baidu.com/img/bdlogo.png]]></PicUrl>\n<Url><![CDATA[http://www.baidu.com]]></Url>\n</item>\n</Articles>]]>";
		System.out.println(xml2json(xml));
	}
}
