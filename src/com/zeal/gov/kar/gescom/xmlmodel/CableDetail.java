package com.zeal.gov.kar.gescom.xmlmodel;

import com.zeal.gov.kar.gescom.annotations.Paraname;

public class CableDetail extends ProjectDetail {
	@Paraname(name="HCML")
    private double cableManualLength;
	@Paraname(name="HCHL")
    private double cableHorizontalDrillLength;
	@Paraname(name="HCLF")
	private double cableFourLength;
	@Paraname(name="HCLN")
	private double cableNineLength;
	@Paraname(name="HCLT")
	private double cableTwoLength;
	@Paraname(name="HNOC")
	private double noOfCables;
	
	
	public double getCableManualLength() {
		return cableManualLength;
	}
	public void setCableManualLength(double cableManualLength) {
		this.cableManualLength = cableManualLength;
	}
	public double getCableHorizontalDrillLength() {
		return cableHorizontalDrillLength;
	}
	public void setCableHorizontalDrillLength(double cableHorizontalDrillLength) {
		this.cableHorizontalDrillLength = cableHorizontalDrillLength;
	}
	public double getCableFourLength() {
		return cableFourLength;
	}
	public void setCableFourLength(double cableFourLength) {
		this.cableFourLength = cableFourLength;
	}
	public double getCableNineLength() {
		return cableNineLength;
	}
	public void setCableNineLength(double cableNineLength) {
		this.cableNineLength = cableNineLength;
	}
	public double getCableTwoLength() {
		return cableTwoLength;
	}
	public void setCableTwoLength(double cableTwoLength) {
		this.cableTwoLength = cableTwoLength;
	}
	public double getNoOfCables() {
		return noOfCables;
	}
	public void setNoOfCables(double noOfCables) {
		this.noOfCables = noOfCables;
	}
	
	
	
   
     
}
