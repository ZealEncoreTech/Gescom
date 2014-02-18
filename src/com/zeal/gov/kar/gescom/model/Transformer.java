package com.zeal.gov.kar.gescom.model;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Transformer {
	private int transId;
	private int htId;
	private int materialType;
	private LatLng Location;
	private String Description;
	private MarkerOptions markerOptions;
	public int getTransId() {
		return transId;
	}

	public void setTransId(int transId) {
		this.transId = transId;
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}


	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

	public LatLng getLocation() {
		return Location;
	}

	public void setLocation(LatLng location) {
		Location = location;
	}

	public int getHtId() {
		return htId;
	}

	public void setHtId(int htId) {
		this.htId = htId;
	}

}
