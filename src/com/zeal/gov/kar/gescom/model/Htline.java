package com.zeal.gov.kar.gescom.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Htline {
	
	
	private int conductorType;
	private int htId;
	private String imageData;
	private List<String> imageDataLis;
	private List<Transformer> transformerList;
	private Transformer transformer;
	private List<LatLng> locationList;
	private LatLng startLocation;
	private LatLng endLocation;
	private MarkerOptions markerOptions;
	private Ltline ltline;
	private String description;
	private List<Ltline> ltlineList;
	
	public Htline()
	{
		locationList=new ArrayList<LatLng>();
		ltlineList=new ArrayList<Ltline>();
	}
	
	public int getConductorType() {
		return conductorType;
	}

	public void setConductorType(int conductorType) {
		this.conductorType = conductorType;
	}

	public int getHtId() {
		return htId;
	}

	public void setHtId(int htId) {
		this.htId = htId;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
		imageDataLis.add(imageData);
	}

	public List<String> getImageDataLis() {
		return imageDataLis;
	}

	public List<Transformer> getTransformerList() {
		return transformerList;
	}

	public Transformer getTransformer() {
		return transformer;
	}

	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
		transformerList.add(transformer);
	}

	public LatLng getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(LatLng startLocation) {
		this.startLocation = startLocation;
		locationList.add(startLocation);
		
	}

	public LatLng getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(LatLng endLocation) {
		this.endLocation = endLocation;
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

	public List<LatLng> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LatLng> locationList) {
		this.locationList = locationList;
	}

	public Ltline getLtline() {
		return ltline;
	}

	public void setLtline(Ltline ltline) {
		this.ltline = ltline;
		ltlineList.add(ltline);
	}

	public List<Ltline> getLtlineList() {
		return ltlineList;
	}

	public void setLtlineList(List<Ltline> ltlineList) {
		this.ltlineList = ltlineList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
