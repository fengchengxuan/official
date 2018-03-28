package com.eastrobot.robotdev.nuskin.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;

public class MediaUtils {
	private static MediaUtils mediaUtils;
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private Logger log = Logger.getLogger(MediaUtils.class);
	private LRUTCache<String,Map<String,String>> cache = new LRUTCache<String,Map<String,String>>(1000,48*60*60);
	private Integer errorStep=0;
	private MediaUtils(){}
	public static MediaUtils getInstance()
	{
		if(ValidationUtils.isEmpty(mediaUtils))
			mediaUtils = new MediaUtils();
		return mediaUtils;
	}
	/**
	* 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
	* 
	* @param url 请求地址 form表单url地址
	* @param filePath 文件在服务器保存路径
	* @return String url的响应信息返回值
	* @throws IOException
	*/
	public String send(String imageName,String openid) throws Exception 
	{
		synchronized (Constants.CACHE_LOCK) {
			String mediaId = "error";
			try {
				String path = getPath(imageName);
//				String path = imageName;
				String result = null;
				File file = new File(path);
				if (!file.exists() || !file.isFile()) 
				{
					throw new Exception("文件不存在:"+path);
				}
				/**
				* 第一部分
				*/
				String interfaceurl = rmrp.get("MediaInterfaceUrl",Constants.Platform.WEIXIN)+openid;
//				String interfaceurl = "http://wecms.cn.nuskin.com:12345/Interface/uploadMedia?type=image&openId="+openid;
				URL urlObj = new URL(interfaceurl);
				// 连接
				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			
			
				/**
				* 设置关键值
				*/
				con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false); // post方式不能使用缓存
			
				// 设置请求头信息
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				// 设置边界
				String BOUNDARY = "----------" + System.currentTimeMillis();
				con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
				// 请求正文信息
				// 第一部分：
				StringBuilder sb = new StringBuilder();
				sb.append("--"); // 必须多两道线
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + ".jpg\"\r\n");
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				//sb.append("Content-Type:application/octet-stream\r\n\r\n");
			
				byte[] head = sb.toString().getBytes("utf-8");
				// 获得输出流
				OutputStream out = new DataOutputStream(con.getOutputStream());
				// 输出表头
				out.write(head);
			
			
				// 文件正文部分
				// 把文件已流文件的方式 推入到url中
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) 
				{
					out.write(bufferOut, 0, bytes);
				}
				in.close();
			
			
				// 结尾部分
				byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			
			
				out.write(foot);
			
			
				out.flush();
				out.close();
			
			
				StringBuffer buffer = new StringBuffer();
				BufferedReader reader = null;
				try {
					// 定义BufferedReader输入流来读取URL的响应
					reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String line = null;
					while ((line = reader.readLine()) != null)
					{
						buffer.append(line);
					}
					if(result==null){
						result = buffer.toString();
					}
				} catch (Exception e) {
					System.out.println("发送POST请求出现异常！" + e);
					e.printStackTrace();
					throw new Exception("数据读取异常");
				} finally {
					if(reader!=null){
					reader.close();
					}
				}
				
				JSONObject obj = JSONObject.fromObject(result);
				mediaId = obj.getString("mediaId");
				if("error".equalsIgnoreCase(mediaId))
				{
					errorStep++;
					log.error("上传图片失败:"+imageName+"|"+result);
					//连续三次上传失败，则不再上传。
					if(errorStep<=3)
					{
						send(imageName, openid);
					}else{
						log.error("~~图片【"+imageName+"】上传失败~~");
					}
				}
				errorStep=0;
			} catch (Exception e) {
				log.debug("调用cms上传图片接口失败");
			}
			return mediaId;
		}
	}
	
	private String getPath(String name)
	{
		String path = MediaUtils.class.getResource("/").getPath();
		System.out.println("path:=====>"+path);
		path = path.substring(0,path.indexOf("dev"))+"robot/attachments/"+name;
		log.info("图片地址："+path);
		return path;
	}
	
	public void putCache(String openid,Map<String,String> map)
	{
		synchronized (Constants.CACHE_LOCK) {
			try {
				cache.put(openid,map);
				log.debug("存入mediaid："+openid+"|"+map+"==========>"+cache.getClass().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getMediaId(String openid,String imgName)
	{
		Map<String,String> map = getMediaId(openid);
		if(ValidationUtils.isNotEmpty(map))
		{
			String mediaId = map.get(imgName);
			log.debug("对应微信公众号的mediaId："+openid+"|"+imgName+"|"+mediaId);
			return mediaId;
		}
		return null;
	}
	
	public Map<String,String> getMediaId(String openid)
	{
		return cache.get(openid);
	}
	
	public static void main(String[] args) throws Exception {
		String openid = "gh_539aed712999";
		String path = "D:/jetty-6.1.26/webapps/robot/attachments/20150325170719725";
		
		String res = MediaUtils.getInstance().send(path,openid);
		System.out.println(res);
	}
	
}
