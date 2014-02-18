package com.zeal.gov.kar.gescom.calculation;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.Item;
import com.zeal.gov.kar.gescomtesting.CreateEstimationActivity;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class DynamicXmlCreator {
	private Context ctx = null;
	private int projectId;
	private BaseService baseService;
	private int spanLength;
	private int htCount,ltCount;
	Document doc=null;
	public DynamicXmlCreator(Context context,int projectId,int spanLength) {
		this.ctx = context;
		this.projectId=projectId;
		this.spanLength=spanLength;
		
	}
	
	public void changeXml(int lt,int ht,int tc) throws TransformerException {
		try {
//			InputStream in = null;
//			in = ctx.getAssets().open("Estimation_Cat_IV.xml");
			htCount=ht;
			ltCount=lt;
 			File in = null;
			baseService = new BaseService(ctx);
			File categoryRoot = Environment.getExternalStorageDirectory();
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_IV.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			HashMap<String, Integer> hmCountForNoofCurves = baseService.getNoofCurvesCount(String.valueOf(projectId));
			int htNoofCurves = hmCountForNoofCurves.get("HtCount");
			int ltNoofCurves = hmCountForNoofCurves.get("LtCount");
			doc = db.parse(in);
			setSpanLength();
		        Node htBlock=doc.getElementById("E4B1");
	            Node ltBlock=doc.getElementById("E4B3");
	            Node tcBlock=doc.getElementById("E4B2");
	            if(ht<=0)
	            {
	            	htBlock.getParentNode().removeChild(htBlock);
	            }
	            if(lt<=0)
	            {
	            	ltBlock.getParentNode().removeChild(ltBlock);
	            }
	            if(tc<=0)
	            {
	            	tcBlock.getParentNode().removeChild(tcBlock);
	            }
	            if(tc>0)
	            {
	            	updateTcBlock();
	            }
  			if(ht>=1)
			{ 
				 changeHtLine(ht);
			}
			addParameters(doc, lt);//add parameters
			addItem(doc, lt);// items to Part - C  LT LINE   (III) which has id = E4B3G1
 			changeQuantity(doc, lt);//change quantity for itemnumber = E4B3G1I01 
			changeFormulaValues(doc, lt);//change formula for itemnumber = E4B3G1I02 
			changeFormulaForItemE4B3G1I03(doc, lt);//change formula for itemnumber = E4B3G1I03
			//changeFormulaForItemE4B3G1I06(doc, lt);
			ltConductorCalculation();

			if(htNoofCurves>0){
				addHtItemForNoofCurve(htNoofCurves);
				addHtLabourChargeForNoofCurve(htNoofCurves);
			}
			if(ltNoofCurves>0){
				addLtItemForNoofCurve(ltNoofCurves);
				addLtLabourChargeForNoofCurve(ltNoofCurves);
			}
			changeLoadingCharge(ltNoofCurves,htNoofCurves);
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			DOMSource source = new DOMSource(doc);
			ctx.openFileOutput("newCategory.xml", ctx.MODE_PRIVATE);
			File data = Environment.getDataDirectory();
			File currentDB = new File(data,
					"//data//com.zeal.gov.kar.gescomtesting//files//newCategory.xml");

			StreamResult result = new StreamResult(currentDB);
			trans.transform(source, result);
			copyXmlToSdcard() ;
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	
	private void setSpanLength()
	{
		//Update span length here....
		 Element htSapanlength=doc.getElementById("LtSpanLength");
		 htSapanlength.setTextContent(Integer.toString(spanLength));
		 Element ltSapanlength=doc.getElementById("HtSpanLength");
		 ltSapanlength.setTextContent(Integer.toString(spanLength));
	}

	private void changeLoadingCharge(int studpolecount,int htNoofCurves)
	{
		 if(ltCount>0)
		 {
		 Element updateLoadCharge=doc.getElementById("addStudPoleCount");
		 updateLoadCharge.setTextContent("E4B3G2I01+E4B3G2I02+"+studpolecount);
		 }
		 if(htCount>0)
		 {
		 Element updateHtLoadCharge=doc.getElementById("addStudPoleCountHT");
		 updateHtLoadCharge.setTextContent("((2 * E4B1G1I09)+(E4B1G1I01 + E4B1G1I02))+"+htNoofCurves);
		 }
	}
	private void addLtItemForNoofCurve(int count) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
	    Node itemElement=doc.getElementById("E4B3G1");
	    
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("103"));
			childElement.appendChild(parameterid);
			
		    Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B3G1I52"));
				childElement.appendChild(parametername);
		
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I52"));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode("4083"));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode(""+count));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			itemElement.appendChild(childElement);
	
	}

	private void addLtLabourChargeForNoofCurve(int count) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
	    Node itemElement=doc.getElementById("E4B3G2");
        
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("104"));
			childElement.appendChild(parameterid);
			
		    Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B3G2I54"));
				childElement.appendChild(parametername);
		
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I54"));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode("4084"));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode("E4B3G1I52"));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			itemElement.appendChild(childElement);
	
	}

	private void addHtLabourChargeForNoofCurve(int count) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
	    Node itemElement=doc.getElementById("E4B1G2");
        
        int lno = 101;
		int itemNo = 50;
		int iNoDisp = 50;
		
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("102"));
			childElement.appendChild(parameterid);
			
		    Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B1G2I51"));
				childElement.appendChild(parametername);
		
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I51"));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode("4082"));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode("E4B1G1I50"));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			itemElement.appendChild(childElement);
	
	}

	private void addHtItemForNoofCurve(int count) {
		// TODO Auto-generated method stub
	    Node itemElement=doc.getElementById("E4B1G1");
        
        int lno = 101;
		int itemNo = 50;
		int iNoDisp = 50;
		
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("101"));
			childElement.appendChild(parameterid);
			
		    Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B1G1I50"));
				childElement.appendChild(parametername);
		
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I50"));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode("4081"));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode(""+count));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			itemElement.appendChild(childElement);
	}


	private void copyXmlToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//com.zeal.gov.kar.gescomtesting//files//newCategory.xml";
				String backupDBPath = "//testingbackup//Raghu007.xml";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
		}
	}
	
private void updateTcBlock()
{
	    HashMap<String,String> tclabour=new HashMap<String, String>();
	    tclabour.put("4075", "4078");
	    tclabour.put("4076", "4079");
	    tclabour.put("4077", "4080");
	    tclabour.put("4031", "4032");
	    //Adding new items
        Node itemElement=doc.getElementById("E4B2G1");
        int lno = 63;
		int itemNo = 9;
		int iNoDisp = 9;
		HashMap<String,Integer> tcData=baseService.getMaterialInformation(Integer.toString(projectId), "3");
		Iterator<Entry<String, Integer>> iterator = tcData.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			String tcType = entry.getKey();
			int tcCount=entry.getValue();
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("" + lno++));
			childElement.appendChild(parameterid);
			
			Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B2G1I0" + itemNo++));
				childElement.appendChild(parametername);
			
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I"
					+ iNoDisp++));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode(tcType));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode(Integer.toString(tcCount)));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			itemElement.appendChild(childElement);
		}
		Node labourElement=doc.getElementById("E4B2G2");
		Iterator<Entry<String, Integer>> iterator2 = tcData.entrySet().iterator();
		while (iterator2.hasNext()) {
			Entry<String, Integer> entry = iterator2.next();
			String tcType = entry.getKey();
			int tcCount=entry.getValue();
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("" + lno++));
			childElement.appendChild(parameterid);
			
			Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B2G1I0" + itemNo++));
				childElement.appendChild(parametername);
			
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I"
					+ iNoDisp++));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode(tclabour.get(tcType)));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			parametermeasurementid4.appendChild(doc
					.createTextNode(Integer.toString(tcCount)));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			labourElement.appendChild(childElement);
		}
		
}
	private void changeFormulaForItemE4B3G1I06(Document doc, int n) {

		NodeList block = doc.getElementsByTagName("block");
		for (int b = 0; b < block.getLength(); ++b) {
			Element element = (Element) block.item(b);

			NodeList groupList = element.getElementsByTagName("group");
			for (int g = 0; g < groupList.getLength(); g++) {
				Element groupElement = (Element) groupList.item(g);
				NodeList groupChild = groupElement.getChildNodes();

				if (parseGroup(groupChild)) {
					NodeList itemList = groupElement
							.getElementsByTagName("item");
					for (int i = 0; i < itemList.getLength(); ++i) {
						Element itemElement = (Element) itemList.item(i);
						NodeList itemChild = itemElement.getChildNodes();
						if (parseNodeGetItem3(itemChild)) {
							Node node = itemChild.item(15);							
							if (node.getNodeName().equalsIgnoreCase("formula")) {
								String formula = "((LT1P";		
								for (int p = 2; p <= n; p++) {
									formula = formula + "+LT" + p + "P";
								}
								node.setTextContent("" + formula
										+ ")/1000)*4*1.045");
								break;
							}
						}
					}
				}
			}
		}
	}

	private boolean parseNodeGetItem3(NodeList itemChild) {
		for (int y = 0; y < itemChild.getLength(); ++y) {
			Node node = itemChild.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getNodeName().equalsIgnoreCase("itemnumber")) {

					if (node.getTextContent().toString()
							.equalsIgnoreCase("E4B3G1I06")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void changeFormulaForItemE4B3G1I03(Document doc, int n) {

		NodeList block = doc.getElementsByTagName("block");
		for (int b = 0; b < block.getLength(); ++b) {
			Element element = (Element) block.item(b);
			

			NodeList groupList = element.getElementsByTagName("group");
			for (int g = 0; g < groupList.getLength(); g++) {
				Element groupElement = (Element) groupList.item(g);
				NodeList groupChild = groupElement.getChildNodes();

				if (parseGroup(groupChild)) {
					NodeList itemList = groupElement
							.getElementsByTagName("item");
					for (int i = 0; i < itemList.getLength(); ++i) {
						Element itemElement = (Element) itemList.item(i);
						NodeList itemChild = itemElement.getChildNodes();
						if (parseNodeGetItem2(itemChild)) {
							Node node = itemChild.item(15);
							if (node.getNodeName().equalsIgnoreCase("formula")) {
								String formula = "(E4B3G1I01 + E4B3G1I02)+" + n;
								node.setTextContent(formula);
								break;
							}
						}
					}
				}
			}
		}
	}

	private boolean parseNodeGetItem2(NodeList itemChild) {

		for (int y = 0; y < itemChild.getLength(); ++y) {
			Node node = itemChild.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("itemnumber")) {
					if (node.getTextContent().toString()
							.equalsIgnoreCase("E4B3G1I03")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void changeFormulaValues(Document doc, int n) {
		NodeList block = doc.getElementsByTagName("block");
		for (int b = 0; b < block.getLength(); ++b) {
			Element element = (Element) block.item(b);

			NodeList groupList = element.getElementsByTagName("group");
			for (int g = 0; g < groupList.getLength(); g++) {
				Element groupElement = (Element) groupList.item(g);
				NodeList groupChild = groupElement.getChildNodes();

				if (parseGroup(groupChild)) {
					NodeList itemList = groupElement
							.getElementsByTagName("item");
					for (int i = 0; i < itemList.getLength(); ++i) {
						Element itemElement = (Element) itemList.item(i);
						NodeList itemChild = itemElement.getChildNodes();
						if (parseNodeGetItem1(itemChild)) {
							Node node = itemChild.item(15);
							if (node.getNodeName().equalsIgnoreCase("formula")) {
								String formula = "(E4B3G1I09";
								int f = 10;
								for (int p = 1; p < n; p++) {
									formula = formula + "+E4B3G1I" + f++;
								}
								node.setTextContent("" + formula
										+ ")-E4B3G1I01 +"+baseService.getIntermediatePoleCount(Integer.toString(projectId), "2"));
								break;
							}
						}
					}
				}
			}
		}
	}

	private void changeQuantity(Document doc, int n) {
		NodeList block = doc.getElementsByTagName("block");
		for (int b = 0; b < block.getLength(); ++b) {
			Element element = (Element) block.item(b);
	

			NodeList groupList = element.getElementsByTagName("group");
			for (int g = 0; g < groupList.getLength(); g++) {
				Element groupElement = (Element) groupList.item(g);
				NodeList groupChild = groupElement.getChildNodes();

				if (parseGroup(groupChild)) {
					NodeList itemList = groupElement
							.getElementsByTagName("item");
					for (int i = 0; i < itemList.getLength(); ++i) {
						Element itemElement = (Element) itemList.item(i);
						NodeList itemChild = itemElement.getChildNodes();
						if (parseNodeGetItem(itemChild)) {
							Node node = itemChild.item(17);
	
							if (node.getNodeName().equalsIgnoreCase("quantity")) {
								node.setTextContent("" + n);
								break;
							}
						}

					}
				}
			}
		}
	}

	private boolean parseNodeGetItem(NodeList nodeList) {

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getNodeName().equalsIgnoreCase("itemnumber")) {
					if (node.getTextContent().toString()
							.equalsIgnoreCase("E4B3G1I01")) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean parseNodeGetItem1(NodeList nodeList) {

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getNodeName().equalsIgnoreCase("itemnumber")) {
			
					if (node.getTextContent().toString()
							.equalsIgnoreCase("E4B3G1I02")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void addItem(Document doc, int n) {
		NodeList block = doc.getElementsByTagName("block");
		for (int b = 0; b < block.getLength(); ++b) {
			Element element = (Element) block.item(b);


			NodeList groupList = element.getElementsByTagName("group");
			for (int g = 0; g < groupList.getLength(); g++) {
				Element groupElement = (Element) groupList.item(g);
				NodeList groupChild = groupElement.getChildNodes();

				if (parseGroup(groupChild)) {
					Node items = groupChild.item(5);

					int lno = 60;
					int itemNo = 10;
					int iNoDisp = 9;
					for (int i = 1; i <= n; i++) {
						Element childElement = doc.createElement("item");

						Element parameterid = doc.createElement("itemslno");
						parameterid.appendChild(doc.createTextNode("" + lno++));
						childElement.appendChild(parameterid);
						if (i == 1) {
							Element parametername = doc
									.createElement("itemnumber");
							parametername.appendChild(doc
									.createTextNode("E4B3G1I09"));
							childElement.appendChild(parametername);
						} else {
							Element parametername = doc
									.createElement("itemnumber");
							parametername.appendChild(doc
									.createTextNode("E4B3G1I" + itemNo++));
							childElement.appendChild(parametername);
						}
						Element parametervalue = doc
								.createElement("itemnumberdisplay");
						parametervalue.appendChild(doc.createTextNode("I"
								+ iNoDisp++));
						childElement.appendChild(parametervalue);

						Element parametermeasurementid = doc
								.createElement("itemtypeid");
						parametermeasurementid.appendChild(doc
								.createTextNode("4039"));
						childElement.appendChild(parametermeasurementid);

						Element parametermeasurementid1 = doc
								.createElement("fixed");
						parametermeasurementid1.appendChild(doc
								.createTextNode("N"));
						childElement.appendChild(parametermeasurementid1);

						Element parametermeasurementid2 = doc
								.createElement("constant");
						parametermeasurementid2.appendChild(doc
								.createTextNode("N"));
						childElement.appendChild(parametermeasurementid2);

						Element parametermeasurementid3 = doc
								.createElement("constantvalue");
						parametermeasurementid3.appendChild(doc
								.createTextNode(""));
						childElement.appendChild(parametermeasurementid3);

						Element parametermeasurementid4 = doc
								.createElement("formula");
						parametermeasurementid4.appendChild(doc
								.createTextNode("LT" + i + "P/"+spanLength));
						childElement.appendChild(parametermeasurementid4);

						Element parametermeasurementid5 = doc
								.createElement("quantity");
						parametermeasurementid5.appendChild(doc
								.createTextNode(""));
						childElement.appendChild(parametermeasurementid5);

						Element parametermeasurementid6 = doc
								.createElement("amountquantity");
						parametermeasurementid6.appendChild(doc
								.createTextNode("P"));
						childElement.appendChild(parametermeasurementid6);

						items.appendChild(childElement);
					}
				}
			}
		}
	}

	private void addParameters(Document doc, int n) {
		Node parameterElement = doc.getElementsByTagName("parameters").item(0);
		for (int i = 1; i <= n; i++) {
			Element childElement = doc.createElement("parameter");

			Element parameterid = doc.createElement("parameterid");
			parameterid.appendChild(doc.createTextNode("LT" + i + "P"));
			childElement.appendChild(parameterid);

			Element parametername = doc.createElement("parametername");
			parametername
					.appendChild(doc
							.createTextNode("Line Length in Meters (Determined by GPS Points)"));
			childElement.appendChild(parametername);

			Element parametervalue = doc.createElement("parametervalue");
			parametervalue.appendChild(doc.createTextNode("LTLTP" + i));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("parametermeasurementid");
			parametermeasurementid.appendChild(doc.createTextNode("D"));
			childElement.appendChild(parametermeasurementid);

			parameterElement.appendChild(childElement);
		}
	}
	
	
	private boolean parseGroup(NodeList nodeList) {

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("groupid")) {
					String groupId = node.getTextContent().trim();
					if (groupId.equalsIgnoreCase("E4B3G1")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private void changeHtLine(int n)
	{
		
		
		 Node parameterElement=doc.getElementById("123");
         for(int i=1;i<=n;i++)
         {
         Element childElement = doc.createElement("parameter");

         Element parameterid = doc.createElement("parameterid");
         parameterid.appendChild(doc.createTextNode("HT"+i+"P"));
 		 childElement.appendChild(parameterid);
 		
 		Element parametername = doc.createElement("parametername");
 		parametername.appendChild(doc.createTextNode("Line Length in Meters (Determined by GPS Points)"));
 		childElement.appendChild(parametername);
 		
 		Element parametervalue = doc.createElement("parametervalue");
 		parametervalue.appendChild(doc.createTextNode("HTHTP"+i));
 		childElement.appendChild(parametervalue);
 		
 		Element parametermeasurementid = doc.createElement("parametermeasurementid");
 		parametermeasurementid.appendChild(doc.createTextNode("D"));
 		childElement.appendChild(parametermeasurementid);
 	 
 	    parameterElement.appendChild(childElement);
         }
         
 	    
         
         //Adding new items
         Node itemElement=doc.getElementById("E4B1G1");
         
            int lno = 60;
			int itemNo = 15;
			int iNoDisp = 15;
			for (int i = 1; i <= n; i++) {
				Element childElement = doc.createElement("item");

				Element parameterid = doc.createElement("itemslno");
				parameterid.appendChild(doc.createTextNode("" + lno++));
				childElement.appendChild(parameterid);
				
			    Element parametername = doc
							.createElement("itemnumber");
					parametername.appendChild(doc
							.createTextNode("E4B1G1I" + itemNo++));
					childElement.appendChild(parametername);
			
				Element parametervalue = doc
						.createElement("itemnumberdisplay");
				parametervalue.appendChild(doc.createTextNode("I"
						+ iNoDisp++));
				childElement.appendChild(parametervalue);

				Element parametermeasurementid = doc
						.createElement("itemtypeid");
				parametermeasurementid.appendChild(doc
						.createTextNode("4039"));
				childElement.appendChild(parametermeasurementid);

				Element parametermeasurementid1 = doc
						.createElement("fixed");
				parametermeasurementid1.appendChild(doc
						.createTextNode("N"));
				childElement.appendChild(parametermeasurementid1);

				Element parametermeasurementid2 = doc
						.createElement("constant");
				parametermeasurementid2.appendChild(doc
						.createTextNode("N"));
				childElement.appendChild(parametermeasurementid2);

				Element parametermeasurementid3 = doc
						.createElement("constantvalue");
				parametermeasurementid3.appendChild(doc
						.createTextNode(""));
				childElement.appendChild(parametermeasurementid3);

				Element parametermeasurementid4 = doc
						.createElement("formula");
				parametermeasurementid4.appendChild(doc
						.createTextNode("HT" + i + "P/"+spanLength));
				childElement.appendChild(parametermeasurementid4);

				Element parametermeasurementid5 = doc
						.createElement("quantity");
				parametermeasurementid5.appendChild(doc
						.createTextNode(""));
				childElement.appendChild(parametermeasurementid5);

				Element parametermeasurementid6 = doc
						.createElement("amountquantity");
				parametermeasurementid6.appendChild(doc
						.createTextNode("P"));
				childElement.appendChild(parametermeasurementid6);

				itemElement.appendChild(childElement);
			}
			
			//4003
			Node formulaElement1=doc.getElementById("change");
			formulaElement1.setTextContent(formulaElement1.getTextContent()+" + "+(n-1));
			
			//4002
			Node formulaElement2=doc.getElementById("change1");
			String fomula="E4B1G1I";
			int data=15,i=1;
			StringBuilder foo=new StringBuilder();
			for ( ; i <= n; i++) {
			    if(i!=n)
			    {
				foo.append(fomula+data+"+");
			    }
			    else
			    {foo.append(fomula+data);}
				data++;
			}
			formulaElement2.setTextContent("("+foo.toString()+")"+"-E4B1G1I01-E4B1G1I09+"+baseService.getIntermediatePoleCount(Integer.toString(projectId), "1"));
			
			//4007
			/*Node formulaElement3=doc.getElementById("change2");
			StringBuilder foo3=new StringBuilder();
			for (int i3=1 ; i3 <= n; i3++) {
			    if(i3!=n)
			    {
				foo3.append("HT"+i3+"P+");
			    }
			    else
			    {foo3.append("HT"+i3+"P");}
			}
			formulaElement3.setTextContent("(("+foo3.toString()+")"+"/1000 * 3)* 1.045");*/
			
			//4009
			
			Node formulaElement4=doc.getElementById("change3");
			StringBuilder f4009=new StringBuilder();
			for (int k=1 ; k <= n; k++) {
			    if(k!=n)
			    {
			    	f4009.append("HT"+k+"P+");
			    }
			    else
			    {f4009.append("HT"+k+"P");}
			}
			formulaElement4.setTextContent("("+f4009.toString()+")/500");
			
 			HashMap<String,String> htCondnmatrllabour=new HashMap<String, String>();
	        htCondnmatrllabour.put("4007", "4018");
	        htCondnmatrllabour.put("4043", "4049");

			Iterator<Entry<String, String>> iterator = htCondnmatrllabour.entrySet().iterator();
	        
	        while (iterator.hasNext()) {
	        	Entry<String, String> entry = iterator.next();
	        	if((baseService.getConductorLength(Integer.toString(projectId),"1", entry.getKey())>0.0))
	        	{
	        	Element childElement = doc.createElement("item");
				Element parameterid = doc.createElement("itemslno");
				parameterid.appendChild(doc.createTextNode("" + lno++));
				childElement.appendChild(parameterid);
				
			    Element parametername = doc
							.createElement("itemnumber");
					parametername.appendChild(doc
							.createTextNode("E4B1G1I" + itemNo++));
					childElement.appendChild(parametername);
			
				Element parametervalue = doc
						.createElement("itemnumberdisplay");
				parametervalue.appendChild(doc.createTextNode("I"
						+ iNoDisp++));
				childElement.appendChild(parametervalue);

				Element parametermeasurementid = doc
						.createElement("itemtypeid");
				parametermeasurementid.appendChild(doc
						.createTextNode(entry.getKey()));
				childElement.appendChild(parametermeasurementid);

				Element parametermeasurementid1 = doc
						.createElement("fixed");
				parametermeasurementid1.appendChild(doc
						.createTextNode("N"));
				childElement.appendChild(parametermeasurementid1);

				Element parametermeasurementid2 = doc
						.createElement("constant");
				parametermeasurementid2.appendChild(doc
						.createTextNode("N"));
				childElement.appendChild(parametermeasurementid2);

				Element parametermeasurementid3 = doc
						.createElement("constantvalue");
				parametermeasurementid3.appendChild(doc
						.createTextNode(""));
				childElement.appendChild(parametermeasurementid3);

				Element parametermeasurementid4 = doc
						.createElement("formula");
				parametermeasurementid4.appendChild(doc
						.createTextNode(Double.toString(baseService.getConductorLength(Integer.toString(projectId),"1", entry.getKey()))+"/1000*(3*1.045)"));
				childElement.appendChild(parametermeasurementid4);

				Element parametermeasurementid5 = doc
						.createElement("quantity");
				parametermeasurementid5.appendChild(doc
						.createTextNode(""));
				childElement.appendChild(parametermeasurementid5);

				Element parametermeasurementid6 = doc
						.createElement("amountquantity");
				parametermeasurementid6.appendChild(doc
						.createTextNode("Q"));
				childElement.appendChild(parametermeasurementid6);
				
				Element parametermeasurementid7 = doc
						.createElement("DecRound");
				parametermeasurementid7.appendChild(doc
						.createTextNode("3"));
				childElement.appendChild(parametermeasurementid7);

				itemElement.appendChild(childElement);
	        	}
	        }
	        	
	        	Node labourElement=doc.getElementById("E4B1G2");
	        	Iterator<Entry<String, String>> iterator2 = htCondnmatrllabour.entrySet().iterator();
		        
	        	while (iterator2.hasNext()) {
		        	Entry<String, String> entry2 = iterator2.next();
		        	String htConductor = entry2.getKey();
	    			String htConductorLabour = entry2.getValue();
		        	if((baseService.getConductorLength(Integer.toString(projectId),"1",htConductor)>0.0))
		        	{
	    			
	    			Element childElement = doc.createElement("item");

	    			Element parameterid = doc.createElement("itemslno");
	    			parameterid.appendChild(doc.createTextNode("" + lno++));
	    			childElement.appendChild(parameterid);
	    			
	    			Element parametername = doc
	    						.createElement("itemnumber");
	    				parametername.appendChild(doc
	    						.createTextNode("E4B1G2I" + itemNo++));
	    				childElement.appendChild(parametername);
	    			
	    			Element parametervalue = doc
	    					.createElement("itemnumberdisplay");
	    			parametervalue.appendChild(doc.createTextNode("I"
	    					+ iNoDisp++));
	    			childElement.appendChild(parametervalue);

	    			Element parametermeasurementid = doc
	    					.createElement("itemtypeid");
	    			parametermeasurementid.appendChild(doc
	    					.createTextNode(htConductorLabour));
	    			childElement.appendChild(parametermeasurementid);

	    			Element parametermeasurementid1 = doc
	    					.createElement("fixed");
	    			parametermeasurementid1.appendChild(doc
	    					.createTextNode("N"));
	    			childElement.appendChild(parametermeasurementid1);

	    			Element parametermeasurementid2 = doc
	    					.createElement("constant");
	    			parametermeasurementid2.appendChild(doc
	    					.createTextNode("N"));
	    			childElement.appendChild(parametermeasurementid2);

	    			Element parametermeasurementid3 = doc
	    					.createElement("constantvalue");
	    			parametermeasurementid3.appendChild(doc
	    					.createTextNode(""));
	    			childElement.appendChild(parametermeasurementid3);

	    			Element parametermeasurementid4 = doc
	    					.createElement("formula");
	    			parametermeasurementid4.appendChild(doc
	    					.createTextNode(Double.toString((baseService.getConductorLength(Integer.toString(projectId),"1", entry2.getKey()))/1000*(3*1.045))));
	    			childElement.appendChild(parametermeasurementid4);

	    			Element parametermeasurementid5 = doc
	    					.createElement("quantity");
	    			parametermeasurementid5.appendChild(doc
	    					.createTextNode(""));
	    			childElement.appendChild(parametermeasurementid5);

	    			Element parametermeasurementid6 = doc
	    					.createElement("amountquantity");
	    			parametermeasurementid6.appendChild(doc
	    					.createTextNode("Q"));
	    			childElement.appendChild(parametermeasurementid6);

	    			Element parametermeasurementid7 = doc
							.createElement("DecRound");
					parametermeasurementid7.appendChild(doc
							.createTextNode("3"));
					childElement.appendChild(parametermeasurementid7);
					
	    			labourElement.appendChild(childElement);
		        	}
	    		}
			}
	        
	public void ltConductorCalculation()
	{

        int lno = 100;
		int itemNo = 15; 
		int iNoDisp = 15;
		HashMap<String,String> htCondnmatrllabour=new HashMap<String, String>();
        htCondnmatrllabour.put("4007", "4018");
        htCondnmatrllabour.put("4043", "4049");
        
        Node itemElement=doc.getElementById("E4B3G1");
        
		Iterator<Entry<String, String>> iterator = htCondnmatrllabour.entrySet().iterator();
        
        while (iterator.hasNext()) {
        	Entry<String, String> entry = iterator.next();
        	double dis=baseService.getConductorLength(Integer.toString(projectId),"2", entry.getKey());
        	if((baseService.getConductorLength(Integer.toString(projectId),"2", entry.getKey())>0.0))
        	{
        	Element childElement = doc.createElement("item");
			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("" + lno++));
			childElement.appendChild(parameterid);
			
		    Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B3G1" + itemNo++));
				childElement.appendChild(parametername);
		
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I"
					+ iNoDisp++));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode(entry.getKey()));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			
			StringBuffer formula=new StringBuffer();
			formula.append("(("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry.getKey(),"1"))+"/1000*(2*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry.getKey(),"2"))+"/1000*(3*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry.getKey(),"3"))+"/1000*(4*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry.getKey(),"4"))+"/1000*(5*1.045)))");
			parametermeasurementid4.appendChild(doc
					.createTextNode(formula.toString()));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);
			
			Element parametermeasurementid7 = doc
					.createElement("DecRound");
			parametermeasurementid7.appendChild(doc
					.createTextNode("3"));
			childElement.appendChild(parametermeasurementid7);

			itemElement.appendChild(childElement);
        	}
        
		}
        Node labourElement=doc.getElementById("E4B3G2");
    	Iterator<Entry<String, String>> iterator2 = htCondnmatrllabour.entrySet().iterator();
        
    	while (iterator2.hasNext()) {
        	Entry<String, String> entry2 = iterator2.next();
        	String htConductor = entry2.getKey();
			String htConductorLabour = entry2.getValue();
        	if((baseService.getConductorLength(Integer.toString(projectId),"2",htConductor)>0.0))
        	{
			
			Element childElement = doc.createElement("item");

			Element parameterid = doc.createElement("itemslno");
			parameterid.appendChild(doc.createTextNode("" + lno++));
			childElement.appendChild(parameterid);
			
			Element parametername = doc
						.createElement("itemnumber");
				parametername.appendChild(doc
						.createTextNode("E4B3G2" + itemNo++));
				childElement.appendChild(parametername);
			
			Element parametervalue = doc
					.createElement("itemnumberdisplay");
			parametervalue.appendChild(doc.createTextNode("I"
					+ iNoDisp++));
			childElement.appendChild(parametervalue);

			Element parametermeasurementid = doc
					.createElement("itemtypeid");
			parametermeasurementid.appendChild(doc
					.createTextNode(htConductorLabour));
			childElement.appendChild(parametermeasurementid);

			Element parametermeasurementid1 = doc
					.createElement("fixed");
			parametermeasurementid1.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid1);

			Element parametermeasurementid2 = doc
					.createElement("constant");
			parametermeasurementid2.appendChild(doc
					.createTextNode("N"));
			childElement.appendChild(parametermeasurementid2);

			Element parametermeasurementid3 = doc
					.createElement("constantvalue");
			parametermeasurementid3.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid3);

			Element parametermeasurementid4 = doc
					.createElement("formula");
			
			StringBuffer formula=new StringBuffer();
			formula.append("(("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry2.getKey(),"1"))+"/1000*(2*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry2.getKey(),"2"))+"/1000*(3*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry2.getKey(),"3"))+"/1000*(4*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectId),"2", entry2.getKey(),"4"))+"/1000*(5*1.045)))");
			parametermeasurementid4.appendChild(doc
					.createTextNode(formula.toString()));
			childElement.appendChild(parametermeasurementid4);

			Element parametermeasurementid5 = doc
					.createElement("quantity");
			parametermeasurementid5.appendChild(doc
					.createTextNode(""));
			childElement.appendChild(parametermeasurementid5);

			Element parametermeasurementid6 = doc
					.createElement("amountquantity");
			parametermeasurementid6.appendChild(doc
					.createTextNode("Q"));
			childElement.appendChild(parametermeasurementid6);

			Element parametermeasurementid7 = doc
					.createElement("DecRound");
			parametermeasurementid7.appendChild(doc
					.createTextNode("3"));
			childElement.appendChild(parametermeasurementid7);
			
			labourElement.appendChild(childElement);
        	}
		}
	}
	
}