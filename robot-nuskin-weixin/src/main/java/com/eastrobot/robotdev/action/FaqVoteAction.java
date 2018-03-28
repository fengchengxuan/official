/**
 * 
 */
package com.eastrobot.robotdev.action;



import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.model.Question;
import com.eastrobot.robotdev.nuskin.utils.FaqVoteCacheUtils;
import com.eastrobot.robotdev.nuskin.utils.LRUTCache;
import com.eastrobot.robotdev.nuskin.utils.NuskinUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.opensymphony.xwork2.ActionSupport;

/**
 *
 */

public class FaqVoteAction extends ActionSupport {
	/**
	 * 
	 */
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	public static LRUTCache<String,String> cache = new LRUTCache<String,String>(100000,20*60);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String time;
	private String msgid;
	private String userId;
	private String reason;
	public String execute() throws Exception{
		log.debug("-userId-msgid-reason-:"+userId+"|"+msgid+"|"+reason);
		String tips = "抱歉，该链接已经失效，如需查询请返回聊天界面重新询问。";
		String url = "/h5/pages/tips.jsp";
		Question question = FaqCacheAction.cache.get(userId);
		String status = cache.get(msgid);
		if(ValidationUtils.isEmpty(status))
		{
			if(ValidationUtils.isNotEmpty(question) && question.getMsgId().endsWith(msgid))
			{
				question.setReason(reason);
				NuskinUtils.sendFaqVote(question);
				FaqVoteCacheUtils.setStatus(msgid);
				tips = rmrp.get("FaqVoteSuccess",Constants.Platform.WEIXIN);
				if(ValidationUtils.isEmpty(tips))
				{
					tips = "您的意见发送成功，如有问题咨询请返回聊天界面继续询问小如新吧";
				}
			}
		}else{
			tips = rmrp.get("FaqAlreadySelect",Constants.Platform.ALL);
			if(ValidationUtils.isEmpty(tips))
				tips = "亲爱的用户，您已经对该问题做出了反馈，我们将尽快处理，如有问题请继续咨询小如新吧。";
		}
		request.setAttribute("tips",tips);
		request.getRequestDispatcher(url).forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}

	public String putstatus()
	{
		log.debug("-----同步缓存："+msgid+"-----");
		if(ValidationUtils.isNotEmpty(msgid))
		{
			try {
				cache.put(msgid,"true");
				response.getWriter().print("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String remove()
	{
		if(ValidationUtils.isNotEmpty(msgid))
		{
			cache.remove(msgid);
		}
		return null;
	}
	
	public String getstatus()
	{
		if(ValidationUtils.isNotEmpty(msgid))
		{
			try {
				String res = cache.get(msgid);
				log.debug("获取msgid状态："+res);
				res = ValidationUtils.isEmpty(res)?"":res;
				response.getWriter().print(res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
