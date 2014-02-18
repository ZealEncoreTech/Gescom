package com.zeal.gov.kar.gescom.model;

public class SavecoordDetails {
	 private String projectName;
	 private int workrowId;
	 private String itemName;
	 private String caption;
	 private int bends;
	 private double latitute;
	 private double longitute;
	 private double distance;
	 private int coordinateCount;
	 private int estimationId;
   
	 
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getBends() {
		return bends;
	}
	public void setBends(int bends) {
		this.bends = bends;
	}
	public double getLatitute() {
		return latitute;
	}
	public void setLatitute(double latitute) {
		this.latitute = latitute;
	}
	public double getlongitute() {
		return longitute;
	}
	public void setlongitute(double longitute) {
		this.longitute = longitute;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getWorkrowId() {
		return workrowId;
	}
	public void setWorkrowId(int workrowId) {
		this.workrowId = workrowId;
	}
	public int getCoordinateCount() {
		return coordinateCount;
	}
	public void setCoordinateCount(int coordinateCount) {
		this.coordinateCount = coordinateCount;
	}
	public int getEstimationId() {
		return estimationId;
	}
	public void setEstimationId(int estimationId) {
		this.estimationId = estimationId;
	}

}
