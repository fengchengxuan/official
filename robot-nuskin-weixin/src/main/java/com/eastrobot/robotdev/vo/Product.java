/**
 * 
 */
package com.eastrobot.robotdev.vo;

/**
 * @author Odin
 *
 */
public class Product {
	private String stockId;
	private String stockName;
	private String stockEngName;
	private String smallIconFilename;
	private String largeIconFileName;
	private String mediumIconFileName;
	private String retailPrice;
	private String wholesalePrice;
	private String stockDescription;
	
	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockEngName() {
		return stockEngName;
	}
	public void setStockEngName(String stockEngName) {
		this.stockEngName = stockEngName;
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
	public String getMediumIconFileName() {
		return mediumIconFileName;
	}
	public void setMediumIconFileName(String mediumIconFileName) {
		this.mediumIconFileName = mediumIconFileName;
	}
	public String getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getWholesalePrice() {
		return wholesalePrice;
	}
	public void setWholesalePrice(String wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	public String getStockDescription() {
		return stockDescription;
	}
	public void setStockDescription(String stockDescription) {
		this.stockDescription = stockDescription;
	}
}
