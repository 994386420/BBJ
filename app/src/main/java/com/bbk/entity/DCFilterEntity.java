package com.bbk.entity;

import java.io.Serializable;

public class DCFilterEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3957568040799409670L;
	private int showCode;
	private String productType;
	private String producttypeName;
	private String domain;
	private String domainCh;
	private String brand;
	private String sku;
	private String time;
	private boolean hasSku;
	
	public DCFilterEntity(int showCode,String productType, String producttypeName,
			String domain, String domainCh, String brand, String sku,
			String time, boolean hasSku) {
		super();
		this.showCode = showCode;
		this.productType = productType;
		this.producttypeName = producttypeName;
		this.domain = domain;
		this.domainCh = domainCh;
		this.brand = brand;
		this.sku = sku;
		this.time = time;
		this.hasSku = hasSku;
	}
	
	public int getShowCode() {
		return showCode;
	}

	public void setShowCode(int showCode) {
		this.showCode = showCode;
	}

	public String getProductType() {
		return productType;
	}
	public String getProducttypeName() {
		return producttypeName;
	}
	public String getDomain() {
		return domain;
	}
	public String getDomainCh() {
		return domainCh;
	}
	public String getBrand() {
		return brand;
	}
	public String getSku() {
		return sku;
	}
	public String getTime() {
		return time;
	}
	public boolean isHasSku() {
		return hasSku;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public void setProducttypeName(String producttypeName) {
		this.producttypeName = producttypeName;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setDomainCh(String domainCh) {
		this.domainCh = domainCh;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setHasSku(boolean hasSku) {
		this.hasSku = hasSku;
	}

	@Override
	public String toString() {
		return "DCFilterEntity [showCode=" + showCode + ", productType="
				+ productType + ", producttypeName=" + producttypeName
				+ ", domain=" + domain + ", domainCh=" + domainCh + ", brand="
				+ brand + ", sku=" + sku + ", time=" + time + ", hasSku="
				+ hasSku + "]";
	}
}
