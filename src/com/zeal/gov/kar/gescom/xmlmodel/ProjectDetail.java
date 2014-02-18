package com.zeal.gov.kar.gescom.xmlmodel;

import com.zeal.gov.kar.gescom.annotations.ColumnInfo;
import com.zeal.gov.kar.gescom.annotations.Paraname;


public class ProjectDetail {
	
	private int projectId;
	private String projectName;
	private int projectCategory;
	private String soilType;
	@Paraname(name="HTSL")
	private double htSpanLength;
	@Paraname(name="LTSL")
	private double ltSpanLength;
	@Paraname(name="HTL")
	private double htLength;
	@Paraname(name="LTL")
	private double ltLength;
	@Paraname(name="HTB")
	private double htBend;
	@Paraname(name="LTB")
	private double ltBend;
	@Paraname(name="LTC")
	private double ltCount;
	@Paraname(name="HTC")
	private double htCount;
	@Paraname(name="TCC")
	private double tcCount;
	@Paraname(name="HTSP")
	private double htStudpoleCout;
	@Paraname(name="LTSP")
	private double ltStudpoleCout;
	@Paraname(name="HTIP")
	private double htIntrPoleCount;
	@Paraname(name="LTIP")
	private double ltIntrPoleCount;
	@Paraname(name="LM")
	private String layingMethod;
	
	@ColumnInfo(name = "Project Id", order = 1)
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	@ColumnInfo(name = "Spanlength of LT", order = 3,ignore=true)
	public double getLtSpanLength() {
		return ltSpanLength;
	}
	public void setLtSpanLength(double ltSpanLength) {
		this.ltSpanLength = ltSpanLength;
	}
	@ColumnInfo(name = "Spanlength", order = 8)
	public double getHtSpanLength() {
		return htSpanLength;
	}
	public void setHtSpanLength(double htSpanLength) {
		this.htSpanLength = htSpanLength;
	}
	@ColumnInfo(name = "Length of HT", order = 4)
	public double getHtLength() {
		return htLength;
	}
	public void setHtLength(double htLength) {
		this.htLength = htLength;
	}
	@ColumnInfo(name = "Bend in LT", order = 7)
	public double getLtBend() {
		return ltBend;
	}
	public void setLtBend(double ltBend) {
		this.ltBend = ltBend;
	}
	@ColumnInfo(name = "Bend in HT", order = 6)
	public double getHtBend() {
		return htBend;
	}
	public void setHtBend(int htBend) {
		this.htBend = htBend;
	}
	@ColumnInfo(name = "Length of LT", order = 5)
	public double getLtLength() {
		return ltLength;
	}
	public void setLtLength(double ltLength) {
		this.ltLength = ltLength;
	}
	@ColumnInfo(name = "No of TC", order = 11)
	public double getTcCount() {
		return tcCount;
	}
	public void setTcCount(double tcCount) {
		this.tcCount = tcCount;
	}
	@ColumnInfo(name = "No of LT", order = 10)
	public double getLtCount() {
		return ltCount;
	}
	public void setLtCount(int ltCount) {
		this.ltCount = ltCount;
	}
	@ColumnInfo(name = "No of HT", order = 9)
	public double getHtCount() {
		return htCount;
	}
	public void setHtCount(int htCount) {
		this.htCount = htCount;
	}
	@ColumnInfo(name = "Studpoles in LT", order = 13)
	public double getLtStudpoleCout() {
		return ltStudpoleCout;
	}
	public void setLtStudpoleCout(double ltStudpoleCout) {
		this.ltStudpoleCout = ltStudpoleCout;
	}
	@ColumnInfo(name = "Studepoles in HT", order = 12)
	public double getHtStudpoleCout() {
		return htStudpoleCout;
	}
	public void setHtStudpoleCout(double htStudpoleCout) {
		this.htStudpoleCout = htStudpoleCout;
	}
	@ColumnInfo(name = "Project Name", order = 2)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@ColumnInfo(name = "Category", order = 3)
	public int getProjectCategory() {
		return projectCategory;
	}
	public void setProjectCategory(int projectCategory) {
		this.projectCategory = projectCategory;
	}
	@ColumnInfo(name = "Bend in HT", order = 3,ignore=true)
	public String getLayingMethod() {
		return layingMethod;
	}
	public void setLayingMethod(String layingMethod) {
		this.layingMethod = layingMethod;
	}
	@ColumnInfo(name = "Intermediate poles in LT ", order = 15)
	public double getLtIntrPoleCount() {
		return ltIntrPoleCount;
	}
	public void setLtIntrPoleCount(double ltIntrPoleCount) {
		this.ltIntrPoleCount = ltIntrPoleCount;
	}
	@ColumnInfo(name = "Intermediate poles in HT ", order = 14)
	public double getHtIntrPoleCount() {
		return htIntrPoleCount;
	}
	public void setHtIntrPoleCount(double htIntrPoleCount) {
		this.htIntrPoleCount = htIntrPoleCount;
	}
	public String getSoilType() {
		return soilType;
	}
	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}
}
