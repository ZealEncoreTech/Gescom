package com.zeal.gov.kar.gescom.model;

import java.io.Serializable;

public class ApprovedPhotoCoordinates implements Serializable {
	private String imageData;
	private int projectId;
	private int estimationId;
	private String loginId;
	private int poleId;
	private String photoCaption;
	private String latitude;
	private String longitude;
	private String CaptionDate;
	private String capturedOn;
	private int coordinatesId;
	private String photoId;
	private String workStarus;
	private int ltEstId;
	private int typeOfLine;
	private String isImageuploaded;
	
	
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String fileName;
	public String capturedBy;
	public String coordinates;
	public int workRowId;
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
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCapturedBy() {
		return capturedBy;
	}
	public void setCapturedBy(String capturedBy) {
		this.capturedBy = capturedBy;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	public String getCapturedOn() {
		return capturedOn;
	}
	public void setCapturedOn(String capturedOn) {
		this.capturedOn = capturedOn;
	}
	public String getImageData() {
		return imageData;
	}
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getEstimationId() {
		return estimationId;
	}
	public void setEstimationId(int estimationId) {
		this.estimationId = estimationId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public int getPoleId() {
		return poleId;
	}
	public void setPoleId(int poleId) {
		this.poleId = poleId;
	}
	public String getPhotoCaption() {
		return photoCaption;
	}
	public void setPhotoCaption(String photoCaption) {
		this.photoCaption = photoCaption;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getCaptionDate() {
		return CaptionDate;
	}
	public void setCaptionDate(String captionDate) {
		CaptionDate = captionDate;
	}
	public String getWorkStarus() {
		return workStarus;
	}
	public void setWorkStarus(String workStarus) {
		this.workStarus = workStarus;
	}
	public int getLtEstId() {
		return ltEstId;
	}
	public void setLtEstId(int ltEstId) {
		this.ltEstId = ltEstId;
	}
	public int getTypeOfLine() {
		return typeOfLine;
	}
	public void setTypeOfLine(int typeOfLine) {
		this.typeOfLine = typeOfLine;
	}
	public String getIsImageuploaded() {
		return isImageuploaded;
	}
	public void setIsImageuploaded(String isImageuploaded) {
		this.isImageuploaded = isImageuploaded;
	}

	}
