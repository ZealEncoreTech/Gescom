package com.zeal.gov.kar.gescom.model;

public class Projectdata {
	
	private int ProjectId;
    private String ProjectName;
    private int ProjectCategory;
    private String HTStartPoint;
    private String HTEndPoint;
    private String LTStartPoint;
    private String LTEndPoint;
    private String NoOFCurves;
    private double Distance;
    private int EstimateId;
    
    
	public int getProjectId() {
		return ProjectId;
	}
	public void setProjectId(int projectId) {
		ProjectId = projectId;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public int getProjectCategory() {
		return ProjectCategory;
	}
	public void setProjectCategory(int projectCategory) {
		ProjectCategory = projectCategory;
	}
	public String getHTStartPoint() {
		return HTStartPoint;
	}
	public void setHTStartPoint(String hTStartPoint) {
		HTStartPoint = hTStartPoint;
	}
	public String getHTEndPoint() {
		return HTEndPoint;
	}
	public void setHTEndPoint(String hTEndPoint) {
		HTEndPoint = hTEndPoint;
	}
	public String getLTStartPoint() {
		return LTStartPoint;
	}
	public void setLTStartPoint(String lTStartPoint) {
		LTStartPoint = lTStartPoint;
	}
	public String getLTEndPoint() {
		return LTEndPoint;
	}
	public void setLTEndPoint(String lTEndPoint) {
		LTEndPoint = lTEndPoint;
	}
	public String getNoOFCurves() {
		return NoOFCurves;
	}
	public void setNoOFCurves(String noOFCurves) {
		NoOFCurves = noOFCurves;
	}
	public double getDistance() {
		return Distance;
	}
	public void setDistance(double distance) {
		Distance = distance;
	}
	public int getEstimateId() {
		return EstimateId;
	}
	public void setEstimateId(int estimateId) {
		EstimateId = estimateId;
	}
}
