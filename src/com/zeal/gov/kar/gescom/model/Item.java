package com.zeal.gov.kar.gescom.model;

import java.io.Serializable;

public class Item implements Serializable {
	public String itemslno = new String();
	public String description = new String();
	public String itemnumber = new String();
	public String itemnumberdisplay = new String();
	public String itemtypeid = new String();
	public String fixed = new String();
	public String constant = new String();
	public String constantvalue = new String();
	public String formula = new String();
	public String blockId = new String();
	public String blockName= new String();
	public String groupId = new String();
	public String groupName= new String();
	public String baseRate=new String();
	public String editQuantity = "0.0";
	public String gpsQuantity =new String();
	public double amount;
	public  String amountquantity = new String();
	public boolean unCalculated = false;
	public String isInt=new String();
	public String decRound=new String();

	
}
