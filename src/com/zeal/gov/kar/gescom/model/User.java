package com.zeal.gov.kar.gescom.model;

public class User {

	private String userName;
	private String simNo;
	private String registerAs;
	private String hashPassword;
	private String imeiNo;
	private String mobileNo;
	

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHashPassword() {
		return hashPassword;
	}
	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}
	
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public String getRegisterAs() {
		return registerAs;
	}
	public void setRegisterAs(String registerAs) {
		this.registerAs = registerAs;
	}

	

}
