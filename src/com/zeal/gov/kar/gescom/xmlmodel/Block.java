package com.zeal.gov.kar.gescom.xmlmodel;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Block {
	private String blockId;
	private String blockName;
	@XStreamAlias("Groups")
	private List<Group> groupList;
	@XStreamOmitField
	private Group group;
	public Block()
	{
		groupList=new ArrayList<Group>();
	}
	public String getBlockId() {
		return blockId;
	}
	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
		groupList.add(group);
	}

	
	public List<Group>  getGrouplist() {
		// TODO Auto-generated method stub
		return groupList;
	}
}
