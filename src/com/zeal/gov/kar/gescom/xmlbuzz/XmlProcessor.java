package com.zeal.gov.kar.gescom.xmlbuzz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;

import com.zeal.gov.kar.gescom.xmlmodel.Block;
import com.zeal.gov.kar.gescom.xmlmodel.Group;
import com.zeal.gov.kar.gescom.xmlmodel.Item;
import com.zeal.gov.kar.gescom.xmlmodel.Parameter;

public class XmlProcessor {
	private String blockId, blockName;
	private String groupId, groupName;
	private List<Group> groupLists;
	private List<Item> items;
	private List<Block> blockLists;
	private List<Parameter> parameterList;
	private int projectCategory;

	public XmlProcessor(int projectCategory) {
		this.projectCategory = projectCategory;
	}

	public HashMap<String, List> getItemList() {
		items = new ArrayList<Item>();
		groupLists = new ArrayList<Group>();
		blockLists = new ArrayList<Block>();
		parameterList = new ArrayList<Parameter>();
		HashMap<String, List> xml = new HashMap<String, List>();
		File in = null;
		File categoryRoot = Environment.getExternalStorageDirectory();
		switch (projectCategory) {
		case 1:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_I.xml");
//			in = ctx.getAssets().open("Estimation_Cat_I.xml");
			break;
		case 2:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_II.xml");
//			in = ctx.getAssets().open("Estimation_Cat_II.xml");
			break;
		case 3:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_III.xml");
//			in = ctx.getAssets().open("Estimation_Cat_III.xml");
			break;
		case 4:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_IV.xml");
//			in = ctx.getAssets().open("Estimation_Cat_IV.xml");
			break;
		case 5:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_V.xml");
//			in = ctx.getAssets().open("Estimation_Cat_V.xml");
			break;
		case 6:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VI.xml");
//			in = ctx.getAssets().open("Estimation_Cat_VI.xml");
			break;
		case 7:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VII.xml");
//			in = ctx.getAssets().open("Estimation_Cat_VII.xml");
			break;
		case 8:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VIII.xml");
//			in = ctx.getAssets().open("Estimation_Cat_VIII.xml");
			break;
		case 9:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_IX.xml");
//			in = ctx.getAssets().open("Estimation_Cat_IX.xml");
			break;
		case 10:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_X.xml");
//			in = ctx.getAssets().open("Estimation_Cat_X.xml");
			break;
		case 11:
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_XI.xml");
//			in = ctx.getAssets().open("Estimation_Cat_XI.xml");
			break;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(in);
			
			items.clear();
			parameterList.clear();
			
			NodeList parameterNode = doc.getElementsByTagName("parameter");
			for (int p = 0; p < parameterNode.getLength(); p++) {
				Element parameterElement = (Element) parameterNode.item(p);
				NodeList paraChild = parameterElement.getChildNodes();
				parseParameter(paraChild);
			}
			
			
			NodeList block = doc.getElementsByTagName("block");
			for (int b = 0; b < block.getLength(); ++b) {
				Element element = (Element) block.item(b);
				NodeList blockChild = element.getChildNodes();
				parseBlock(blockChild);

				NodeList groupList = element.getElementsByTagName("group");
				for (int g = 0; g < groupList.getLength(); g++) {
					Element groupElement = (Element) groupList.item(g);
					NodeList groupChild = groupElement.getChildNodes();
					parseGroup(groupChild);
					NodeList itemList = groupElement
							.getElementsByTagName("item");
					for (int i = 0; i < itemList.getLength(); ++i) {
						Element itemElement = (Element) itemList.item(i);
						NodeList itemChild = itemElement.getChildNodes();
						Item item = parseNodeGetItem(itemChild);

						items.add(item);

					}

				}
			}
			xml.put("block", blockLists);
			xml.put("group", groupLists);
			xml.put("item", items);
			xml.put("parameter", parameterList);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}

	private void parseBlock(NodeList nodeList) {
		Block block = new Block();
		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("blockid")) {
					blockId = node.getTextContent().trim();
					block.setBlockId(blockId);
				}
				if (node.getNodeName().equalsIgnoreCase("blockname")) {
					blockName = node.getTextContent().trim();
					block.setBlockName(blockName);
				}
			}
		}
		blockLists.add(block);
	}

	private void parseGroup(NodeList nodeList) {
		Group group = new Group();
		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("groupid")) {
					groupId = node.getTextContent().trim();
					group.setGroupid(groupId);
					group.setBlockId(blockId);

				}
				if (node.getNodeName().equalsIgnoreCase("groupname")) {
					groupName = node.getTextContent().trim();
					group.setGroupname(groupName);

				}
			}
		}
		groupLists.add(group);
	}

	private void parseParameter(NodeList nodeList) {
		Parameter parameter = new Parameter();
		for (int p = 0; p < nodeList.getLength(); p++) {

			Node node = nodeList.item(p);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("parameterid")) {
					parameter.setParameterid(node.getTextContent().trim());

				}
				if (node.getNodeName().equalsIgnoreCase("parametername")) {
					parameter.setParametername(node.getTextContent().trim());

				}
				if (node.getNodeName().equalsIgnoreCase("parametervalue")) {
					parameter.setParametervalue(node.getTextContent().trim());

				}
				if (node.getNodeName().equalsIgnoreCase(
						"parametermeasurementid")) {
					parameter.setParametermeasurementid(node.getTextContent()
							.trim());

				}
			}

		}
		parameterList.add(parameter);
	}

	private Item parseNodeGetItem(NodeList nodeList) {
		Item item = new Item();

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			item.blockId = blockId;
			item.blockName = blockName;
			item.setGroupId(groupId);
			item.groupName = groupName;
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("formula")) {
					item.formula = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("amountquantity")) {
					item.amountquantity = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("itemtypeid")) {
					item.itemtypeid = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("itemnumber")) {
					item.itemnumber = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("itemslno")) {
					item.itemslno = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("fixed")) {
					item.fixed = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("constant")) {
					item.constant = node.getTextContent().trim();
				}
				if (node.getNodeName().equalsIgnoreCase("quantity")) {
					if (node.getTextContent().trim() != null) {
						String value = node.getTextContent().trim();
						if (!value.isEmpty()) {
							item.qunatity = value;
						}
					}
				}
				if (node.getNodeName().equalsIgnoreCase("constantvalue")) {
					if (!node.getTextContent().trim().isEmpty()) {
						item.constantvalue = node.getTextContent().trim();
					} else {
						item.constantvalue = "0.00";
					}
				}
				if (node.getNodeName().equalsIgnoreCase("IsInteger")) {
					if (node.getTextContent().trim().equalsIgnoreCase("Y")) {
						item.isInt = "Y";
					} else {
						item.isInt = "N";
					}
				}
				if (node.getNodeName().equalsIgnoreCase("DecRound")) {
					item.decRound = node.getTextContent().trim();
				}
			}
		}

		return item;
	}

}
