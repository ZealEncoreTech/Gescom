package com.zeal.gov.kar.gescom.calculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Environment;

import com.zeal.gov.kar.gescom.database.BaseService;

public class DynamicEight {
	private Context ctx = null;
	private int projectId;
	private BaseService baseService;
	private int spanLength;
	private int htCount,ltCount;
	Document doc=null;
	
	
	
	public DynamicEight(Context context,int projectId,int spanLength) {
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
			in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VIII.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			HashMap<String, Integer> hmCountForNoofCurves = baseService.getNoofCurvesCount(String.valueOf(projectId));
			int htNoofCurves = hmCountForNoofCurves.get("HtCount");
			int ltNoofCurves = hmCountForNoofCurves.get("LtCount");
			doc = db.parse(in);
			//setSpanLength();
			
			
		    Node htBlock=doc.getElementById("E8B1");
	        Node ltBlock=doc.getElementById("E8B3");
	        Node tcBlock=doc.getElementById("E8B2");
	        if(tc>0)
            {
            	updateTcBlock();
            }
	        ltConductorCalculation();
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
	private void updateTcBlock()
	{
		    HashMap<String,String> tclabour=new HashMap<String, String>();
		    tclabour.put("4075", "4078");
		    tclabour.put("4076", "4079");
		    tclabour.put("4077", "4080");
		    tclabour.put("4031", "4032");
		    //Adding new items
	        Node itemElement=doc.getElementById("E8B2G1");
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
							.createTextNode("E8B2G1I0" + itemNo++));
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
			Node labourElement=doc.getElementById("E8B2G2");
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
							.createTextNode("E8B2G1I0" + itemNo++));
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
		 updateLoadCharge.setTextContent("E8B3G2I01+E8B3G2I02+"+studpolecount);
		 }
	/*	 if(htCount>0)
		 {
		 Element updateHtLoadCharge=doc.getElementById("addStudPoleCountHT");
		 updateHtLoadCharge.setTextContent("((2 * E4B1G1I09)+(E4B1G1I01 + E4B1G1I02))+"+htNoofCurves);
		 }*/
	}
	public void ltConductorCalculation()
	{

        int lno = 100;
		int itemNo = 15; 
		int iNoDisp = 15;
		HashMap<String,String> htCondnmatrllabour=new HashMap<String, String>();
        htCondnmatrllabour.put("4007", "4018");
        htCondnmatrllabour.put("4043", "4049");
        
        Node itemElement=doc.getElementById("E8B3G1");
        
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
						.createTextNode("E8B3G1" + itemNo++));
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
        Node labourElement=doc.getElementById("E8B3G2");
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
						.createTextNode("E8B3G2" + itemNo++));
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
}
