/**
 * 
 */
package com.eastrobot.robotdev.action;



import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.service.EvaluationService;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.Evaluation;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Odin
 *
 */

public class EvaluateAction extends ActionSupport {

	/**
	 * 
	 */
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	@Autowired
	private EvaluationService evaluationService;
	private String token;
	private Integer year;
	private Integer quarter;
	public String execute(){
		
		try{
			Evaluation eval = evaluationService.getEval(token, year, quarter);
			String url = "/h5/pages/empty.jsp";
			if(ValidationUtils.isNotEmpty(eval) && ValidationUtils.isEmpty(eval.getStatusMsg()))
			{
				url = "/h5/pages/evaluate.jsp";
				ServletActionContext.getRequest().setAttribute("eval",eval);
			}else{
				String tips = "";
				if(ValidationUtils.isNotEmpty(eval))
				{
					JSONObject obj = JSONObject.fromObject(eval.getStatusMsg());
					int code = obj.getInt("code");
					if(500 == code)
					{
						int status = obj.getJSONObject("message").getInt("status");
						if(10086 == status)
							tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					}else if(401 == code){
						tips = "抱歉，该链接已经失效，如需查询请返回聊天界面重新询问。";
					}
				}else{
					tips = "抱歉，您目前没有可以查询的绩效评估报告。";
				}
				//跳转无结果页面
				url = "/h5/pages/empty.jsp";
				ServletActionContext.getRequest().setAttribute("tips",tips);
			}
			ServletActionContext.getRequest().getRequestDispatcher(url).
			forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		}
		catch (Exception e) {
			try {
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
				ServletActionContext.getRequest().getRequestDispatcher("/h5/pages/empty.jsp").
				forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			} catch (Exception e1) {
			}
		}
		return null;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getQuarter() {
		return quarter;
	}
	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}
	
	
}
