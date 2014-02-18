package com.zeal.gov.kar.gescom.model;

public class ProjectEstimationType {
	private int projectId;
	private int estimationId;
	private String estDescription;
	private String lineId;
	private boolean older;
	private int ltEstId;
	
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
	public String getEstDescription() {
		return estDescription;
	}
	public void setEstDescription(String estDescription) {
		this.estDescription = estDescription;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	public boolean isOlder() {
		return older;
	}
	public void setOlder(boolean older) {
		this.older = older;
	}
	public int getLtEstId() {
		return ltEstId;
	}
	public void setLtEstId(int ltEstId) {
		this.ltEstId = ltEstId;
	}
}
