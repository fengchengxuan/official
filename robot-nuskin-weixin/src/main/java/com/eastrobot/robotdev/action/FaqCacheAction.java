/**
 * 
 */
package com.eastrobot.robotdev.action;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.nuskin.utils.LRUTCache;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.opensymphony.xwork2.ActionSupport;

/**
 *
 */

public class FaqCacheAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	public static LRUTCache<String,Question> cache = new LRUTCache<String,Question>(100000,20*60);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String qjson;
	private String userId;
	public String put() throws Exception
	{
		Question question = JsonUtils.convertToObj(JSONObject.fromObject(qjson),Question.class);
		cache.put(userId,question);
		return null;
	}

	public String get() throws Exception
	{
		response.setCharacterEncoding("utf-8");
		Question que = cache.get(userId);
		JSONObject json = JSONObject.fromObject(que);
		response.getWriter().print(json);
		return null;
	}
	
	public String remove() throws Exception
	{
		cache.remove(userId);
		return null;
	}
	
	public String getQjson() {
		return qjson;
	}

	public void setQjson(String qjson) {
		this.qjson = qjson;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
