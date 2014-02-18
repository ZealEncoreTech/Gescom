package com.zeal.gov.kar.gescom.model;

public class MeasurementEstimation {
	private int workRowId;
	private int EstimationId;
	private int subWorkID;
	private int workItemTypeId;
	private Double costPerItem;
	private int measurementId;
	private Double totalUnits;
	private Double totalAmount;
	private Double measurementQuantity;
	private Double mTotalAmount;
	
	public int getWorkRowId() {
		return workRowId;
	}
	public void setWorkRowId(int workRowId) {
		this.workRowId = workRowId;
	}
	public int getEstimationId() {
		return EstimationId;
	}
	public void setEstimationId(int estimationId) {
		EstimationId = estimationId;
	}
	public int getSubWorkID() {
		return subWorkID;
	}
	public void setSubWorkID(int subWorkID) {
		this.subWorkID = subWorkID;
	}
	public int getWorkItemTypeId() {
		return workItemTypeId;
	}
	public void setWorkItemTypeId(int workItemTypeId) {
		this.workItemTypeId = workItemTypeId;
	}
	public Double getCostPerItem() {
		return costPerItem;
	}
	public void setCostPerItem(Double costPerItem) {
		this.costPerItem = costPerItem;
	}
	public int getMeasurementId() {
		return measurementId;
	}
	public void setMeasurementId(int measurementId) {
		this.measurementId = measurementId;
	}
	public Double getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(Double totalUnits) {
		this.totalUnits = totalUnits;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getMeasurementQuantity() {
		return measurementQuantity;
	}
	public void setMeasurementQuantity(Double measurementQuantity) {
		this.measurementQuantity = measurementQuantity;
	}
	public Double getmTotalAmount() {
		return mTotalAmount;
	}
	public void setmTotalAmount(Double mTotalAmount) {
		this.mTotalAmount = mTotalAmount;
	}
	
}
