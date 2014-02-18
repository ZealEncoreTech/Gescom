package com.zeal.gov.kar.gescom.xmlmodel;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Item {
	
	public  Item()
	{
		
	}
	public Item(String itemslno,String itemnumber,String itemtypeid,String fixed,String constant,String constantvalue,String formula,String amountquantity,String groupId,String blockId)
	{
		this.itemslno=itemslno;
		this.itemnumber=itemnumber;
		this.itemtypeid=itemtypeid;
		this.fixed=fixed;
		this.constant=constant;
		this.constantvalue=constantvalue;
		this.formula=formula;
		this.amountquantity=amountquantity;
		this.setGroupId(groupId);
		this.blockId=blockId;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String itemslno = new String();
	@XStreamOmitField
	public String description = new String();
	public String itemnumber = new String();
	public String itemnumberdisplay = new String();
	public String itemtypeid = new String();
	public String fixed = new String();
	public String constant = new String();
	public String constantvalue = new String();
	public String formula = new String();
	@XStreamOmitField
	public String blockId = new String();
	@XStreamOmitField
	public String blockName= new String();
	@XStreamOmitField
	private String groupId = new String();
	@XStreamOmitField
	public String groupName= new String();
	@XStreamOmitField
	public String baseRate=new String();
	@XStreamOmitField
	public String editQuantity = "0.0";
	@XStreamOmitField
	public String gpsQuantity =new String();
	@XStreamOmitField
	public double amount;
	public  String amountquantity = new String();
	@XStreamOmitField
	public boolean unCalculated = false;

	public String qunatity=new String();
	
	public String isInt=new String();
	
	public String decRound=new String();

}
