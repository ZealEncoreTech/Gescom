package com.zeal.gov.kar.gescom.model;

public class Projectestimates {
	
	private int typeOfLine;
	private String startLattitude;
	private String endLattitude;
	private String startLangtitude;
	private String endLangtitude;
	private double distance;
	private int NoofCurves;
	private String receivedFromMobileDate;
	private int estimationId;
	private int coordinatesId;
	private String coordinatesCaption;
	private int workRowId;
	private String isTaping;
	private int materialId;
	private int ltPhase;
	private int ltEstId;
	private String layingMethod;
	public String getLayingMethod() {
		return layingMethod;
	}
	public void setLayingMethod(String layingMethod) {
		this.layingMethod = layingMethod;
	}
	public int getTypeOfLine() {
		return typeOfLine;
	}
	public void setTypeOfLine(int typeOfLine) {
		this.typeOfLine = typeOfLine;
	}
	public String getStartLattitude() {
		return startLattitude;
	}
	public void setStartLattitude(String startLattitude) {
		this.startLattitude = startLattitude;
	}
	public String getEndLattitude() {
		return endLattitude;
	}
	public void setEndLattitude(String endLattitude) {
		this.endLattitude = endLattitude;
	}
	public String getStartLangtitude() {
		return startLangtitude;
	}
	public void setStartLangtitude(String startLangtitude) {
		this.startLangtitude = startLangtitude;
	}
	public String getEndLangtitude() {
		return endLangtitude;
	}
	public void setEndLangtitude(String endLangtitude) {
		this.endLangtitude = endLangtitude;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getNoofCurves() {
		return NoofCurves;
	}
	public void setNoofCurves(int noofCurves) {
		NoofCurves = noofCurves;
	}
	public String getReceivedFromMobileDate() {
		return receivedFromMobileDate;
	}
	public void setReceivedFromMobileDate(String receivedFromMobileDate) {
		this.receivedFromMobileDate = receivedFromMobileDate;
	}
	public int getEstimationId() {
		return estimationId;
	}
	public void setEstimationId(int estimationId) {
		this.estimationId = estimationId;
	}
	public int getCoordinatesId() {
		return coordinatesId;
	}
	public void setCoordinatesId(int coordinatesId) {
		this.coordinatesId = coordinatesId;
	}
	public String getCoordinatesCaption() {
		return coordinatesCaption;
	}
	public void setCoordinatesCaption(String coordinatesCaption) {
		this.coordinatesCaption = coordinatesCaption;
	}
	public int getWorkRowId() {
		return workRowId;
	}
	public void setWorkRowId(int workRowId) {
		this.workRowId = workRowId;
	}
	public String getIsTaping() {
		return isTaping;
	}
	public void setIsTaping(String isTaping) {
		this.isTaping = isTaping;
	}
	/**
	 * @return the materialId
	 */
	public int getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialId the materialId to set
	 */
	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}
	public int getLtEstId() {
		return ltEstId;
	}
	public void setLtEstId(int ltEstId) {
		this.ltEstId = ltEstId;
	}
	public int getLtPhase() {
		return ltPhase;
	}
	public void setLtPhase(int ltPhase) {
		this.ltPhase = ltPhase;
	}
	

}
