package com.zeal.gov.kar.gescom.model;

public class Workmain {
	
	private int workRowid;
	private String workDescription;
	private int projectCategory;
	private int noofLT;
	private String reason;
    private String workStatusid;
	private String Dates;
	private int noofHT;
	private String signature;
	private String assignedUser;
	private String spanLength;
	
	public int getWorkRowid() {
		return workRowid;
	}
	public void setWorkRowid(int workRowid) {
		this.workRowid = workRowid;
	}
	public String getWorkDescription() {
		return workDescription;
	}
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
	public int getProjectCategory() {
		return projectCategory;
	}
	public void setProjectCategory(int projectCategory) {
		this.projectCategory = projectCategory;
	}
	public int getNoofLT() {
		return noofLT;
	}
	public void setNoofLT(int noofLT) {
		this.noofLT = noofLT;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getWorkStatusid() {
		return workStatusid;
	}
	public void setWorkStatusid(String workStatusid) {
		this.workStatusid = workStatusid;
	}
	public String getDates() {
		return Dates;
	}
	public void setDates(String dates) {
		Dates = dates;
	}
	public int getNoofHT() {
		return noofHT;
	}
	public void setNoofHT(int noofHT) {
		this.noofHT = noofHT;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	public String getSpanLength() {
		return spanLength;
	}
	public void setSpanLength(String spanLength) {
		this.spanLength = spanLength;
	}
 
}
