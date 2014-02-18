package com.zeal.gov.kar.gescom.xmlmodel;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Group {
	private String groupid;
	private String groupname;
	@XStreamOmitField
	private String blockId;
	private List<Item> Items;
	@XStreamOmitField
    private Item item;
	public Group() {
		Items=new ArrayList<Item>();
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public List<Item> getItems() {
		return Items;
	}

	public void setItems(List<Item> items) {
		Items = items;
	}

	public Item getItem() {
		return item;
		
	}

	public void setItem(Item item) {
		this.item = item;
		Items.add(item);
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
}
