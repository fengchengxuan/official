/**
 * 
 */
package com.eastrobot.robotdev.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONArray;


import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.vo.Product;

/**
 * @author Odin
 *
 */

//@Component
public class ProductService {
	//@Value("#{settings['soa.request.host']}")
	private String soa_request_host;
	//@Value("#{settings['category.url']}")
	private String category_url;
	//@Value("#{settings['product.url']}")
	private String product_url;
	//@Value("#{settings['product.search.url']}")
	private String product_search_url;
	
	public String getCategoryById(String id){
		String result = "";
		try {
			String url = soa_request_host + category_url;
			url += id;
			result = HttpClientUtils.doGet(url, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getProductById(String stockId){
		String result = "";
		try {
			String url = soa_request_host + product_url;
			url += stockId;
			result = HttpClientUtils.doGet(url, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Product getProductByName(String name){
		try {
			String url = soa_request_host + product_search_url;
			url += URLEncoder.encode(name, "utf-8");
			String result = HttpClientUtils.doGet(url, "utf-8");
			System.out.println(result);
			try {
				JSONArray jsonarray = JSONArray.fromObject(result);
				if(jsonarray!=null && jsonarray.size()>0)
					return JsonUtils.convertToObj(jsonarray.getJSONObject(0), Product.class);
			} catch (Exception e) {
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void setSoa_request_host(String soa_request_host) {
		this.soa_request_host = soa_request_host;
	}
	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}
	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}
	public void setProduct_search_url(String product_search_url) {
		this.product_search_url = product_search_url;
	}
	
	
}
