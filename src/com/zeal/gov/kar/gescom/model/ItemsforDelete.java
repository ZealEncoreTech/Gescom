package com.zeal.gov.kar.gescom.model;

import java.io.Serializable;

public class ItemsforDelete implements Serializable{
private String Serialnumber;
private String workDescription;
private String WorkRowId;

public String getWorkRowId() {
	return WorkRowId;
}
public void setWorkRowId(String workRowId) {
	WorkRowId = workRowId;
}
public String getSerialnumber() {
	return Serialnumber;
}
public void setSerialnumber(String serialnumber) {
	Serialnumber = serialnumber;
}
public String getWorkDescription() {
	return workDescription;
}
public void setWorkDescription(String workDescription) {
	this.workDescription = workDescription;
}

}
