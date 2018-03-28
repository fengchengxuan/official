package com.eastrobot.robotdev.action;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.service.ProdCreditService;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.OrderEntry;
import com.eastrobot.robotdev.vo.ProductCredit;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.opensymphony.xwork2.ActionSupport;

public class ProdcreditAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private String token;
	private String id;
	@Autowired
	private ProdCreditService prodCreditService;
	@Override
	public String execute() throws Exception {
		try {
			String result = prodCreditService.getPoints4History(token);
			String url = "/h5/pages/empty.jsp";
			if(ValidationUtils.isNotEmpty(result))
			{
				if(!result.startsWith("{"))
				{
					url = "/h5/pages/prodcredit.jsp";
					List<ProductCredit> list = this.getPoints4History(result);
					ServletActionContext.getRequest().setAttribute("list",list);
				}else{
					String tips = "";
					JSONObject obj = JSONObject.fromObject(result);
					int code = obj.getInt("code");
					if(500 == code)
					{
						int status = obj.getJSONObject("message").getInt("status");
						if(10086 == status)
							tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					}else if(401 == code){
						tips = "抱歉，该链接已经失效，如需查询请返回聊天界面重新询问。";
					}else{
						tips = "抱歉，没有可以查询的结果。";
					}
					ServletActionContext.getRequest().setAttribute("tips",tips);
				}
			}else{
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
			}
			ServletActionContext.getRequest().getRequestDispatcher(url).
			forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		} catch (Exception e) {
			try {
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
				ServletActionContext.getRequest().getRequestDispatcher("/h5/pages/empty.jsp").
				forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			} catch (Exception e1) {
			}
		}
		return null;
	}
	
	public String show() throws Exception {
		try {
			String result = prodCreditService.getOrderEntrys(token,id);
			String url = "/h5/pages/empty.jsp";
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONObject json = JSONObject.fromObject(result);
				if(ValidationUtils.isEmpty(json.get("code")))
				{
					url = "/h5/pages/orderentry.jsp";
					List<OrderEntry> list = this.getOrderEntrys(result);
					ServletActionContext.getRequest().setAttribute("list",list);
				}else{
					String tips = "抱歉，没有可以查询的结果。";
					int code = json.getInt("code");
					if(500 == code)
					{
						int status = json.getJSONObject("message").getInt("status");
						if(10086 == status)
							tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					}else if(401 == code){
						tips = "抱歉，该链接已经失效，如需查询请返回聊天界面重新询问。";
					}
					ServletActionContext.getRequest().setAttribute("tips",tips);
				}
			}else{
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
			}
			ServletActionContext.getRequest().getRequestDispatcher(url).
			forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		} catch (Exception e) {
			try {
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
				ServletActionContext.getRequest().getRequestDispatcher("/h5/pages/empty.jsp").
				forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			} catch (Exception e1) {
			}
		}
		return null;
	}
	
	private List<ProductCredit> getPoints4History(String result)
	{
		List<ProductCredit> list = new ArrayList<ProductCredit>();
		try {
			
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONArray array = JSONArray.fromObject(result);
				for(int i=0;i<array.size();i++)
				{
					JSONObject obj = array.getJSONObject(i);
					ProductCredit pc = JsonUtils.convertToObj(obj,ProductCredit.class);
					list.add(pc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private List<OrderEntry> getOrderEntrys(String result)
	{
		List<OrderEntry> list = new ArrayList<OrderEntry>();
		try {
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONObject json = JSONObject.fromObject(result);
				JSONArray array = json.getJSONArray("webSoList").getJSONObject(0).getJSONArray("webSoItemList");
				if(ValidationUtils.isNotEmpty(array))
				{
					for(int i=0;i<array.size();i++)
					{
						OrderEntry oe = JsonUtils.convertToObj(array.getJSONObject(i),OrderEntry.class);
						list.add(oe);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
