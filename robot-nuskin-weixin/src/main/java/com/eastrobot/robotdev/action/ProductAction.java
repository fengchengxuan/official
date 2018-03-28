/**
 * 
 */
package com.eastrobot.robotdev.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.service.ProductService;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.Category;
import com.eastrobot.robotdev.vo.Product;
import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Allowed;

/**
 * @author Odin
 *
 */
public class ProductAction extends ActionSupport {

	/**
	 * 
	 */
	private RobotMessageResource rmrp = new RobotMessageResourceProxy();
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ProductService productService;
	
	private String id;
	private String name;
	private String des;
	private String img;
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String execute(){
		try{
			String description = rmrp.get(des,Constants.Platform.ALL);
			String result = productService.getCategoryById(id);
			String url = "/h5/pages/empty.jsp";
			if(ValidationUtils.isNotEmpty(result))
			{
				if(!result.startsWith("{"))
				{
					url = "/h5/pages/category.jsp";
					List<Category> list = this.getCategoryById(result);
					ServletActionContext.getRequest().setAttribute("list", list);
					ServletActionContext.getRequest().setAttribute("description", description);
					ServletActionContext.getRequest().setAttribute("img",img);
					ServletActionContext.getRequest().setAttribute("title",title);
				}else{
					String tips = "抱歉，没有可以查询的结果。";
					JSONObject obj = JSONObject.fromObject(result);
					int code = obj.getInt("code");
					if(500 == code)
					{
						int status = obj.getJSONObject("message").getInt("status");
						if(10086 == status)
							tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					}
					ServletActionContext.getRequest().setAttribute("tips",tips);
				}
			}else{
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
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
			e.printStackTrace();
		}
		return null;
	}
	
	public String show(){
		try{
			String result = productService.getProductById(id);
			String url = "/h5/pages/empty.jsp";
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONObject obj = JSONObject.fromObject(result);
				if(ValidationUtils.isEmpty(obj.get("code")))
				{
					Product product = this.getProductById(result);
					url = "/h5/pages/product.jsp";
					ServletActionContext.getRequest().setAttribute("product", product);
				}else{
					int code = obj.getInt("code");
					String tips = "抱歉，没有可以查询的结果。";
					if(500 == code)
					{
						int status = obj.getJSONObject("message").getInt("status");
						if(10086 == status)
							tips = rmrp.get("SOAInsideError",Constants.Platform.ALL);
					}
					ServletActionContext.getRequest().setAttribute("tips",tips);
				}
			}else{
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
			}
			ServletActionContext.getRequest().getRequestDispatcher(url).
	    	forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		}catch (Exception e) {
			try {
				ServletActionContext.getRequest().setAttribute("tips","内部网络拥堵，请稍后再试");
				ServletActionContext.getRequest().getRequestDispatcher("/h5/pages/empty.jsp").
				forward(ServletActionContext.getRequest(), ServletActionContext.getResponse());
			} catch (Exception e1) {
			}
		}
		return null;
	}
	
	private List<Category> getCategoryById(String json){
		ArrayList<Category> list = new ArrayList<Category>();
		try{
			JSONArray jsonarray = JSONArray.fromObject(json);
			for (int i = 0; i < jsonarray.size(); i++) {
				list.add(JsonUtils.convertToObj(jsonarray.getJSONObject(i), Category.class));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private Product getProductById(String json)
	{
		try{
			return JsonUtils.convertToObj(JSONObject.fromObject(json), Product.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
