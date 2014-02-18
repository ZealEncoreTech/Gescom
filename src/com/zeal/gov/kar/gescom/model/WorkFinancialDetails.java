package com.zeal.gov.kar.gescom.model;

public class WorkFinancialDetails {

	private String descNo;
	private String description;
	private double totalUnits;
	private double costPerItem;
	private double estimatedQuantity;
	private int subworkId;
	private String updatedBy;
	private String asOnToday;
	private double quantityOver;
	private double totalCost;
	private double costOver;
	private double tUnits;
	private double cUnits;
	private String blockId;
	private int measurementId;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getDescNo() {
		return descNo;
	}

	public void setDescNo(String descNo) {
		this.descNo = descNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(double totalUnits) {
		this.totalUnits = totalUnits;
	}

	public double getCostPerItem() {
		return costPerItem;
	}

	public void setCostPerItem(double costPerItem) {
		this.costPerItem = costPerItem;
	}

	public double getEstimatedQuantity() {
		return estimatedQuantity;
	}

	public void setEstimatedQuantity(double estimatedQuantity) {
		this.estimatedQuantity = estimatedQuantity;
	}

	public double getQuantityOver() {
		return quantityOver;
	}

	public void setQuantityOver(double quantityOver) {
		this.quantityOver = quantityOver;
	}

	public int getSubworkId() {
		return subworkId;
	}

	public void setSubworkId(int subworkId) {
		this.subworkId = subworkId;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getCostOver() {
		return costOver;
	}

	public void setCostOver(double costOver) {
		this.costOver = costOver;
	}

	public double gettUnits() {
		return tUnits;
	}

	public void settUnits(double tUnits) {
		this.tUnits = tUnits;
	}

	public double getcUnits() {
		return cUnits;
	}

	public void setcUnits(double cUnits) {
		this.cUnits = cUnits;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getAsOnToday() {
		return asOnToday;
	}

	public void setAsOnToday(String asOnToday) {
		this.asOnToday = asOnToday;
	}

	public int getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(int measurementId) {
		this.measurementId = measurementId;
	}

}
