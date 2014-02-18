package com.zeal.gov.kar.gescom.model;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ltline {
	private int ltId;
	private int htId;
	private int conductorType;
	private List<String> imageDataList;
	private List<LatLng> locationList;
	private LatLng startLocation;
	private LatLng endLocation;
	private String Description;
	private MarkerOptions markerOptions;
	public Ltline()
	{
		locationList=new ArrayList<LatLng>();
		
	}
	public int getLtId() {
		return ltId;
	}

	public void setLltId(int ltId) {
		this.ltId = ltId;
	}

	public int getConductorType() {
		return conductorType;
	}

	public void setConductorType(int conductorType) {
		this.conductorType = conductorType;
	}

	public List<String> getImageDataList() {
		return imageDataList;
	}

	public void setImageDataList(List<String> imageDataList) {
		this.imageDataList = imageDataList;
	}

	public List<LatLng> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LatLng> locationList) {
		this.locationList = locationList;
		locationList.add(startLocation);
	}

	public LatLng getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(LatLng startLocation) {
		this.startLocation = startLocation;
		locationList.add(startLocation);
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

	public int getHtId() {
		return htId;
	}

	public void setHtId(int htId) {
		this.htId = htId;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public LatLng getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(LatLng endLocation) {
		this.endLocation = endLocation;
	}

}
