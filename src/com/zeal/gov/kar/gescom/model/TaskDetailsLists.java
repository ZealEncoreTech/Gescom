package com.zeal.gov.kar.gescom.model;

import java.util.List;

public class TaskDetailsLists {
	private List<MainTaskDetailsWork> lstMainTaskDetailsWorks;
	private List<SubTaskDetailsWork> lstSubTaskDetailsWorks;
	private List<Task> lstTasks;
	
	public List<MainTaskDetailsWork> getLstMainTaskDetailsWorks() {
		return lstMainTaskDetailsWorks;
	}
	public void setLstMainTaskDetailsWorks(
			List<MainTaskDetailsWork> lstMainTaskDetailsWorks) {
		this.lstMainTaskDetailsWorks = lstMainTaskDetailsWorks;
	}
	public List<SubTaskDetailsWork> getLstSubTaskDetailsWorks() {
		return lstSubTaskDetailsWorks;
	}
	public void setLstSubTaskDetailsWorks(
			List<SubTaskDetailsWork> lstSubTaskDetailsWorks) {
		this.lstSubTaskDetailsWorks = lstSubTaskDetailsWorks;
	}
	public List<Task> getLstTasks() {
		return lstTasks;
	}
	public void setLstTasks(List<Task> lstTasks) {
		this.lstTasks = lstTasks;
	}
	

}
