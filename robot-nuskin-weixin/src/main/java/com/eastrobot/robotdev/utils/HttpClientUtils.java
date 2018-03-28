package com.eastrobot.robotdev.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;

import com.eastrobot.robotdev.Constants;

public class HttpClientUtils {
	private static int error = 0;
	//private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";
	
	private static HttpClient httpClient;

	private final static int connsPerHost = 100;

	private final static int connReadTimeout = 10000;

	private final static int connTimeout = 30000;

	private final static int connFetchTimeout = 30000;

	static {
		HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = httpConnectionManager.getParams();
		params.setDefaultMaxConnectionsPerHost(connsPerHost);
		params.setMaxTotalConnections(Integer.MAX_VALUE);
		params.setConnectionTimeout(connTimeout);
		params.setSoTimeout(connReadTimeout);
		httpClient = new HttpClient(httpConnectionManager);
		httpClient.getParams().setConnectionManagerTimeout(connFetchTimeout);
	}

	
	/**
	 * 拼接Header的Get请求
	 * @param url
	 * @param encode
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public static String doGetXml(String url,String encode,String eid) throws Exception {
		GetMethod get = geGettMethod(url);
		get.addRequestHeader("Accept","application/json");
		get.addRequestHeader("Accept-Encoding","gzip,deflate");
		get.addRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		if(ValidationUtils.isNotEmpty(eid))
			get.addRequestHeader("Authorization",eid);
		get.addRequestHeader("Connection","keep-alive");
		get.addRequestHeader("Host", "we360test.cn.nuskin.com");
		get.addRequestHeader("Referer", "https://we360test.cn.nuskin.com/ws/apidocs/");
		
		String response = null;
		encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
//		try {
			int code = httpClient.executeMethod(get);
			if(HttpServletResponse.SC_OK == code)
			{
				InputStream is = get.getResponseBodyAsStream();
				Header header = get.getResponseHeader("Content-Encoding");
				String contentEncoding = "";
				if(ValidationUtils.isNotEmpty(header))
					contentEncoding = header.getValue();
				if(ValidationUtils.isNotEmpty(contentEncoding) && contentEncoding.toLowerCase().indexOf("gzip") != -1){
					GZIPInputStream gzin = new GZIPInputStream(is);
		            InputStreamReader isr = new InputStreamReader(gzin, "utf-8"); // 设置读取流的编码格式，自定义编码
		            response = stream2String(gzin, encode);
				}else{
					response = stream2String(is,encode);
				}
			}else
			{
				response = new String(get.getResponseBody(), encode);
				response = change2errorMsg(code,response);
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
			get.releaseConnection();
//		}
		return response;
	}
	
	public static String doGet(String url,String encode) throws Exception {
		HttpClient client = getClient(url);
		GetMethod get = geGettMethod(url);
		String response = null;
		encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
		try {
			int code = client.executeMethod(get);
			response = new String(get.getResponseBody(), encode);
			if(HttpServletResponse.SC_OK != client.executeMethod(get))
				response = change2errorMsg(code,response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return response;
	}
	
	public static String doPost(String url, String encode) throws Exception {
		HttpClient client = getClient(url);
		PostMethod post = getPostMethod(url);
		String response = null;
		encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
		try {
			if(HttpServletResponse.SC_OK == client.executeMethod(post))
				response = new String(post.getResponseBody(), encode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return response;
	}
	
	public static String doPost(String url, Map<String, String> params)
			throws Exception {
		HttpClient client = getClient(url);
		PostMethod post = gePostMethod(url, params.entrySet());
		post.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");	
        post.addRequestHeader("Accept-Encoding","gzip,deflate");
        post.addRequestHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.addRequestHeader("Connection","keep-alive");
        post.addRequestHeader("Cookie","cnonce=740233; sig=a4951dc655df2c28fd9002ed8e11d08bd70245b0; JSESSIONID=lyZybjiLV_FOL4s8iCshL0IGfL8vih3bh_TStcqRIiSkthvuEyGm!437441808; nonce=328524; Hm_lvt_b5716ef87990056024422ae4fb494926=1440733825; Hm_lpvt_b5716ef87990056024422ae4fb494926=1440733825; _ga=GA1.2.207791279.1440733825; _gat=1");
        post.addRequestHeader("Host","rxbjpnewr.demo.xiaoi.com");
        post.addRequestHeader("Referer","http://rxbjpnewr.demo.xiaoi.com/dev/h5/demo/faqvote.html");
        post.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
//        postMethod.addRequestHeader("Content-Length","4975");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		
		String response = null;
		try {
			client.executeMethod(post);
			response = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return response;
	}
	public static String doPostXml(String url,String jsonStr,String encode)throws Exception
	{
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Accept","application/json");
		post.addRequestHeader("Accept-Encoding","gzip,deflate");
		post.addRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		post.addRequestHeader("Cache-Control","no-cache");
		post.addRequestHeader("Connection","keep-alive");
		post.addRequestHeader("Content-Type","application/json;charset=UTF-8");
		post.addRequestHeader("Host", "we360test.cn.nuskin.com");
		post.addRequestHeader("Pragma","no-cache");
		
		
		RequestEntity requestEntity = new StringRequestEntity(jsonStr,"text/xml","UTF-8");
		post.setRequestEntity(requestEntity);
		encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
		String response = null;
//		try{
			int code = httpClient.executeMethod(post);
			if(HttpServletResponse.SC_OK == code)
			{
				InputStream is = post.getResponseBodyAsStream();
				Header header = post.getResponseHeader("Content-Encoding");
				String contentEncoding = "";
				if(ValidationUtils.isNotEmpty(header))
					contentEncoding = header.getValue();
				if(ValidationUtils.isNotEmpty(contentEncoding) && contentEncoding.toLowerCase().indexOf("gzip") != -1){
					GZIPInputStream gzin = new GZIPInputStream(is);
		            InputStreamReader isr = new InputStreamReader(gzin, "utf-8"); // 设置读取流的编码格式，自定义编码
		            response = stream2String(gzin, encode);
				}else{
					response = stream2String(is,encode);
				}
			}else
			{
				response = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
				response = change2errorMsg(code,response);
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
			post.releaseConnection();
//		}
		return response;
	}
	
	/*public static String doPutXml(String url,String jsonStr,String encode)throws Exception
	{
		PutMethod put = new PutMethod(url);
		put.addRequestHeader("Accept","application/json");
		put.addRequestHeader("Accept-Encoding","gzip,deflate");
		put.addRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		put.addRequestHeader("Connection","keep-alive");
		put.addRequestHeader("Host", "we360test.cn.nuskin.com");
		put.addRequestHeader("Content-Type","application/json;charset=UTF-8");
		
		RequestEntity requestEntity = new StringRequestEntity(jsonStr,"text/xml","UTF-8");
		put.setRequestEntity(requestEntity);
		encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
		String response = null;
		try
		{
			if(HttpServletResponse.SC_OK == httpClient.executeMethod(put))
				response = stream2String(put.getResponseBodyAsStream(),encode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			put.releaseConnection();
		}
		return response;
	}*/

	private static PostMethod gePostMethod(String url,Set<Entry<String, String>> entrySet) {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("User-Agent", USER_AGENT);
		NameValuePair[] nvps = new NameValuePair[entrySet.size()];
		int i = 0;
		for (Entry<String, String> entry : entrySet) {
			NameValuePair nvp = new NameValuePair(entry.getKey(), entry.getValue());
			nvps[i] = nvp;
			i++;
		}
		post.setRequestBody(nvps);
		return post;

	}
	
	private static HttpClient getClient(String url) {
		HttpClient client = httpClient;
		client.getHostConfiguration().setHost(url);
		return client;
	}

	private static GetMethod geGettMethod(String url) {
		GetMethod get = new GetMethod(url);
		get.setRequestHeader("User-Agent", USER_AGENT);
		return get;

	}
	
	private static PostMethod getPostMethod(String url) {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("User-Agent", USER_AGENT);
		return post;
	}
	
	private static String stream2String(InputStream inputStream,String encode) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(inputStream,encode);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		
		while((line=br.readLine())!=null)
		{
			sb.append(line);
		}
		if(inputStream!=null)
			inputStream.close();
		if(isr!=null)
			isr.close();
		if(br!=null)
			br.close();
		return sb.toString();
	}
	
	private static String change2errorMsg(int code,String message)
	{
		JSONObject obj = new JSONObject();
		obj.put("code",code);
		obj.put("message", message);
		return obj.toString();
	}
		
}
