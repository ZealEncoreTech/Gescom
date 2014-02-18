package com.zeal.gov.kar.gescom.model;

public class Task {
	
	private int taskId;
	private int workRowId;
	private int subWorkRowId;
	private String task;
	private double percentageCompleted;
	private String status; 
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getWorkRowId() {
		return workRowId;
	}
	public void setWorkRowId(int workRowId) {
		this.workRowId = workRowId;
	}
	public int getSubWorkRowId() {
		return subWorkRowId;
	}
	public void setSubWorkRowId(int subWorkRowId) {
		this.subWorkRowId = subWorkRowId;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public double getPercentageCompleted() {
		return percentageCompleted;
	}
	public void setPercentageCompleted(double percentageCompleted) {
		this.percentageCompleted = percentageCompleted;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
