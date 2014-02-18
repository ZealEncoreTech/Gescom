package com.zeal.gov.kar.gescom.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkItems implements Serializable{
	private int workRowid;
	private int estimationId;
	private int category;
	private int subCategory;
	private int componentId;
	private int workItemTypeId;
	private String workItemDescription;
	private int measurementid;
	private double costPerItem;
	private double totalUnits;
	private double totalAmount;
	private double gpsQuantity;

	private boolean changed;
	private double srRate;
	private int srYear;
	private String BlockId;
	private String GroupId;
	private double baseRate;
	private String BlockName;
	private String Fixed;
	private String constant;
	private String constantValue;
	private String formula;
	private String GroupName;
	private String subWorkid;
	private String amountQuantity;
	private String itemCode;
	private double measurmentQuantity;
	private double mTotalAmount;
	private int decRound;
	private String isInteger;
	public int getWorkRowid() {
		return workRowid;
	}
	public void setWorkRowid(int workRowid) {
		this.workRowid = workRowid;
	}
	public int getEstimationId() {
		return estimationId;
	}
	public void setEstimationId(int estimationId) {
		this.estimationId = estimationId;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(int subCategory) {
		this.subCategory = subCategory;
	}
	public int getComponentId() {
		return componentId;
	}
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	public int getWorkItemTypeId() {
		return workItemTypeId;
	}
	public void setWorkItemTypeId(int workItemTypeId) {
		this.workItemTypeId = workItemTypeId;
	}
	public String getWorkItemDescription() {
		return workItemDescription;
	}
	public void setWorkItemDescription(String workItemDescription) {
		this.workItemDescription = workItemDescription;
	}
	public int getMeasurementid() {
		return measurementid;
	}
	public void setMeasurementid(int measurementid) {
		this.measurementid = measurementid;
	}
	public double getCostPerItem() {
		return costPerItem;
	}
	public void setCostPerItem(double costPerItem) {
		this.costPerItem = costPerItem;
	}
	public double getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(double totalUnits) {
		this.totalUnits = totalUnits;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getSrRate() {
		return srRate;
	}
	public void setSrRate(double srRate) {
		this.srRate = srRate;
	}
	public int getSrYear() {
		return srYear;
	}
	public void setSrYear(int srYear) {
		this.srYear = srYear;
	}
	public String getBlockId() {
		return BlockId;
	}
	public void setBlockId(String blockId) {
		BlockId = blockId;
	}
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
	}
	public double getBaseRate() {
		return baseRate;
	}
	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}
	public String getBlockName() {
		return BlockName;
	}
	public void setBlockName(String blockName) {
		BlockName = blockName;
	}
	public String getFixed() {
		return Fixed;
	}
	public void setFixed(String fixed) {
		Fixed = fixed;
	}
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	public String getConstantValue() {
		return constantValue;
	}
	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	public String getSubWorkid() {
		return subWorkid;
	}
	public void setSubWorkid(String subWorkid) {
		this.subWorkid = subWorkid;
	}
	public String getAmountQuantity() {
		return amountQuantity;
	}
	public void setAmountQuantity(String amountQuantity) {
		this.amountQuantity = amountQuantity;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public double getMeasurmentQuantity() {
		return measurmentQuantity;
	}
	public void setMeasurmentQuantity(double measurmentQuantity) {
		this.measurmentQuantity = measurmentQuantity;
	}
	public double getmTotalAmount() {
		return mTotalAmount;
	}
	public void setmTotalAmount(double mTotalAmount) {
		this.mTotalAmount = mTotalAmount;
	}
	public int getDecRound() {
		return decRound;
	}
	public void setDecRound(int decRound) {
		this.decRound = decRound;
	}
	public String getIsInteger() {
		return isInteger;
	}
	public void setIsInteger(String isInteger) {
		this.isInteger = isInteger;
	}
	/*@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeStringArray(new String[] {String.valueOf(this.workRowid),
				this.itemCode,String.valueOf(this.baseRate),
				String.valueOf(this.totalUnits),this.formula,
				this.getConstantValue(),
				String.valueOf(this.totalAmount),
				String.valueOf(this.workItemTypeId),this.Fixed,this.constant,this.isInteger,String.valueOf(this.decRound),this.amountQuantity,String.valueOf(this.measurementid),String.valueOf(measurmentQuantity),String.valueOf(costPerItem),String.valueOf(mTotalAmount),workItemDescription});

	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public WorkItems createFromParcel(Parcel in) {
			return new WorkItems(in); 
		}

		public WorkItems[] newArray(int size) {
			return new WorkItems[size];
		}
	};

	public WorkItems(Parcel in){
		String[] data = new String[18];

		in.readStringArray(data);
		this.workRowid = Integer.parseInt(data[0]);
		this.itemCode = data[1];
		this.baseRate = Double.parseDouble(data[2]);
		this.totalUnits = Double.parseDouble(data[3]);
		this.formula = data[4];
		this.constantValue = data[5];
		this.totalAmount = Double.parseDouble(data[6]);
		this.workItemTypeId = Integer.parseInt(data[7]);
		this.Fixed = data[8];
		this.constant = data[9];
		this.isInteger = data[10];
		this.decRound = Integer.parseInt(data[11]);
		this.amountQuantity = data[12];
		this.measurementid = Integer.parseInt(data[13]);
		this.measurmentQuantity = Double.parseDouble(data[14]);
		this.costPerItem = Double.parseDouble(data[15]);
		this.mTotalAmount = Double.parseDouble(data[16]);
		this.workItemDescription = data[17];
		
	}
	public WorkItems() {
		// TODO Auto-generated constructor stub
	}*/
	public double getGpsQuantity() {
		return gpsQuantity;
	}
	public void setGpsQuantity(double gpsQuantity) {
		this.gpsQuantity = gpsQuantity;
	}

	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
