package com.eastrobot.robotdev.service;

import net.sf.json.JSONObject;

import com.eastrobot.robotdev.Constants;
import com.eastrobot.robotdev.utils.HttpClientUtils;
import com.eastrobot.robotdev.utils.JsonUtils;
import com.eastrobot.robotdev.utils.ValidationUtils;
import com.eastrobot.robotdev.vo.Evaluation;

public class EvaluationService {
	private String soa_request_host;
	private String addr_evalution_url;
	
	public Evaluation getEval(String eid,int year,int quarter)
	{
		try {
			
			String url = soa_request_host+addr_evalution_url+"?year="+year+"&quarter="+quarter;
			String result = HttpClientUtils.doGetXml(url, Constants.ENCODE, eid);
			
			if(ValidationUtils.isNotEmpty(result))
			{
				JSONObject obj = JSONObject.fromObject(result);
				if(ValidationUtils.isEmpty(obj.get("code")))
				{
					Evaluation eval = JsonUtils.convertToObj(obj,Evaluation.class);
					return eval;
				}else{
					Evaluation eval = new Evaluation();
					eval.setStatusMsg(result);
					return eval;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getSoa_request_host() {
		return soa_request_host;
	}
	public void setSoa_request_host(String soa_request_host) {
		this.soa_request_host = soa_request_host;
	}
	public String getAddr_evalution_url() {
		return addr_evalution_url;
	}
	public void setAddr_evalution_url(String addr_evalution_url) {
		this.addr_evalution_url = addr_evalution_url;
	}
	
}
