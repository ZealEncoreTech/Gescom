package com.zeal.gov.kar.gescom.xmlmodel;

public class Parameter {
	private String parameterid;
	private String parametername;
	private String parametervalue;
	private String parametermeasurementid;
	
	
	public String getParametername() {
		return parametername;
	}
	public void setParametername(String parametername) {
		this.parametername = parametername;
	}
	public String getParametervalue() {
		return parametervalue;
	}
	public void setParametervalue(String parametervalue) {
		this.parametervalue = parametervalue;
	}
	public String getParametermeasurementid() {
		return parametermeasurementid;
	}
	public void setParametermeasurementid(String parametermeasurementid) {
		this.parametermeasurementid = parametermeasurementid;
	}
	public String getParameterid() {
		return parameterid;
	}
	public void setParameterid(String parameterid) {
		this.parameterid = parameterid;
	}
}
