/**
 * 
 */
package com.eastrobot.robotdev.vo;


/**
 * @author Odin
 *
 */
public class Category {
	private Integer stockId;
	private String stockName;
	private String smallIconFilename;
	private String largeIconFileName;
	private Object retailPrice;
	private Object wholesalePrice;
	
	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getSmallIconFilename() {
		return smallIconFilename;
	}

	public void setSmallIconFilename(String smallIconFilename) {
		this.smallIconFilename = smallIconFilename;
	}

	public String getLargeIconFileName() {
		return largeIconFileName;
	}

	public void setLargeIconFileName(String largeIconFileName) {
		this.largeIconFileName = largeIconFileName;
	}

	public Object getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Object retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Object getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(Object wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
}
