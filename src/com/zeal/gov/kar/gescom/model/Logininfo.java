package com.zeal.gov.kar.gescom.model;

public class Logininfo {
	private String loginId;
	private String passWord;
	private String officeName;
	private String activated;
	private String activatedOn;
	
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getActivated() {
		return activated;
	}
	public void setActivated(String activated) {
		this.activated = activated;
	}
	public String getActivatedOn() {
		return activatedOn;
	}
	public void setActivatedOn(String activatedOn) {
		this.activatedOn = activatedOn;
	}

}
