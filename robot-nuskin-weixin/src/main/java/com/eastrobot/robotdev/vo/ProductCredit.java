package com.eastrobot.robotdev.vo;



import com.eastrobot.robotdev.utils.DateUtils;

public class ProductCredit {
	private String distId;
	private String transTypeCd;
	private String transTypeDesc;
	private String createDtmStr;
	private String productCredit;
    private String referenceId;
    
    
	public String getDistId() {
		return distId;
	}
	public void setDistId(String distId) {
		this.distId = distId;
	}
	public String getTransTypeDesc() {
		return transTypeDesc;
	}
	public void setTransTypeDesc(String transTypeDesc) {
		this.transTypeDesc = transTypeDesc;
	}
	public String getCreateDtmStr() {
		return createDtmStr;
	}
	public void setCreateDtmStr(String createDtmStr) {
		this.createDtmStr = DateUtils.date2Str(DateUtils.str2Date(createDtmStr,"yyyyMMdd HHmmss"),"yyyy-MM-dd HH:mm:ss");
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getTransTypeCd() {
		return transTypeCd;
	}
	public void setTransTypeCd(String transTypeCd) {
		this.transTypeCd = transTypeCd;
	}
	public String getProductCredit() {
		return productCredit;
	}
	public void setProductCredit(String productCredit) {
		this.productCredit = productCredit;
	}
	
}
