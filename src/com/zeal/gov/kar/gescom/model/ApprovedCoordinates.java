package com.zeal.gov.kar.gescom.model;

public class ApprovedCoordinates {
	public int Estimationid;
	public int projectId;
	public String Latitude;
	public String Longitude;
	public String loginId;
	public int iPoleId;
	public String sCaption;
	public int iPoleType;
	private String capturedOn;
	private int workRowId;
	private int coordinatesId;
	private String capturedBy;
	private String ltEstId;
	private int typeOfLine;
	
	public String getLtEstId() {
		return ltEstId;
	}
	public void setLtEstId(String ltEstId) {
		this.ltEstId = ltEstId;
	}
	public int getEstimationid() {
		return Estimationid;
	}
	public void setEstimationid(int estimationid) {
		Estimationid = estimationid;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public int getiPoleId() {
		return iPoleId;
	}
	public void setiPoleId(int iPoleId) {
		this.iPoleId = iPoleId;
	}
	
	public String getsCaption() {
		return sCaption;
	}
	public void setsCaption(String sCaption) {
		this.sCaption = sCaption;
	}
	public int getiPoleType() {
		return iPoleType;
	}
	public void setiPoleType(int iPoleType) {
		this.iPoleType = iPoleType;
	}
	public String getCapturedOn() {
		return capturedOn;
	}
	public void setCapturedOn(String capturedOn) {
		this.capturedOn = capturedOn;
	}
	public int getWorkRowId() {
		return workRowId;
	}
	public void setWorkRowId(int workRowId) {
		this.workRowId = workRowId;
	}
	public int getCoordinatesId() {
		return coordinatesId;
	}
	public void setCoordinatesId(int coordinatesId) {
		this.coordinatesId = coordinatesId;
	}
	public String getCapturedBy() {
		return capturedBy;
	}
	public void setCapturedBy(String capturedBy) {
		this.capturedBy = capturedBy;
	}
	public int getTypeOfLine() {
		return typeOfLine;
	}
	public void setTypeOfLine(int typeOfLine) {
		this.typeOfLine = typeOfLine;
	}
	
	
}
