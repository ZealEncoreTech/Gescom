package com.zeal.gov.kar.gescom.calculation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.zeal.gov.kar.gescom.annotations.Paraname;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.Item;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.xmlmodel.CableDetail;
import com.zeal.gov.kar.gescom.xmlmodel.ProjectDetail;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

@SuppressLint("NewApi")
public class ProjectEstimation {

	private Context ctx = null;
	private HashMap<String, Item> itemMap = new HashMap<String, Item>();
	private HashMap<String, Item> uncalItemMap = new HashMap<String, Item>(); 
	public HashMap<String, Double> paraMap = new HashMap<String, Double>();
	private final BaseService bs;
	private String blockId, blockName;
	private String groupId, groupName;
	private double totalAmount;
	private BaseService baseService;
	private boolean multiEstimation;
	private double E4B2G1,E4B2G2,E4B1G1,E4B1G2,E4B3G1,E4B3G3,E4B3G2;
	private double E3B1G1,E3B1G2,E8B3G1,E8B3G2,E8B2G1,E8B2G2,E8B1G1,E8B1G2;
	int i=0;
	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	private int projectCategory;
	private String workRowId;

	public ProjectEstimation(Context context, int projectCategory,boolean data,String workRowId) {
		this.ctx = context;
		this.workRowId = workRowId;
		bs = new BaseService(context);
		this.projectCategory = projectCategory;
		multiEstimation=data;
	}

	public Map<String, Item> executeProjectEstimation(String workRowId) {
		itemMap.clear();
		totalAmount=0.0;
		uncalItemMap.clear();
		this.workRowId = workRowId;
		Document doc = readPorjectEstimationCatagoryXML();
		parseXmlToCalculateProjectEstimation(doc);
		totalAmount();
		return itemMap;
		
	}
	
	private void totalAmount()
	{
		totalAmount=0.0;
		Iterator<Entry<String, Item>> iterat = itemMap.entrySet().iterator();
		while (iterat.hasNext()) {
			Entry<String, Item> entry = iterat.next();
			totalAmount=totalAmount+entry.getValue().amount;
		}
		
	}

	public Document readPorjectEstimationCatagoryXML() {
		Document doc = null;
		try {
//			InputStream in = null;
			File in = null;
			File categoryRoot = Environment.getExternalStorageDirectory();
			switch (projectCategory) {
			case 1:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_I.xml");
//				in = ctx.getAssets().open("Estimation_Cat_I.xml");
				break;
			case 2:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_II.xml");
//				in = ctx.getAssets().open("Estimation_Cat_II.xml");
				break;
			case 3:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_III.xml");
//				in = ctx.getAssets().open("Estimation_Cat_III.xml");
				break;
			case 4:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_IV.xml");
//				in = ctx.getAssets().open("Estimation_Cat_IV.xml");
				break;
			case 5:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_V.xml");
//				in = ctx.getAssets().open("Estimation_Cat_V.xml");
				break;
			case 6:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VI.xml");
//				in = ctx.getAssets().open("Estimation_Cat_VI.xml");
				break;
			case 7:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VII.xml");
//				in = ctx.getAssets().open("Estimation_Cat_VII.xml");
				break;
			case 8:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_VIII.xml");
//				in = ctx.getAssets().open("Estimation_Cat_VIII.xml");
				break;
			case 9:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_IX.xml");
//				in = ctx.getAssets().open("Estimation_Cat_IX.xml");
				break;
			case 10:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_X.xml");
//				in = ctx.getAssets().open("Estimation_Cat_X.xml");
				break;
			case 11:
				in  = new File(categoryRoot+"/GescomCategoryXML/Estimation_Cat_XI.xml");
//				in = ctx.getAssets().open("Estimation_Cat_XI.xml");
				break;
			}

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			if(multiEstimation)
			{
				doc = db.parse(new File("//data//data//com.zeal.gov.kar.gescomtesting//files//newCategory.xml"));
			}
			else{
			doc = db.parse(in);
			}
			NodeList parameter = doc.getElementsByTagName("parameter");
//			for (int i = 0; i < parameter.getLength(); ++i) {
//				Element element = (Element) parameter.item(i);
//				NodeList parameterChild = element.getChildNodes();
				try{
					parseParameter();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			}
			int ltNumber=baseService.isEstimationTypeAvailable(workRowId, "2");
			for(int i=1;i<=ltNumber;i++)
			{
				double paraValue = (double) baseService.getMultiLineLength(workRowId,"2", Integer.toString(i));
				paraMap.put("LT"+i+"P", paraValue);
			}
			int htNumber=baseService.isEstimationTypeAvailable(workRowId, "1");
			for(int i=1;i<=htNumber;i++)
			{
				double paraValue = (double) baseService.getMultiLineLength(workRowId,"1", Integer.toString(i));
				paraMap.put("HT"+i+"P", paraValue);
			}
			System.out.println(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public HashMap<String, WorkItems> processEditEstimationRequest(
			HashMap<String, WorkItems> it) {
		
		String changedItem = new String();
		ArrayList<String> changedItems = new ArrayList<String>();
		
		readPorjectEstimationCatagoryXML();

		// Extracting input Hash map.
		Iterator<Entry<String, WorkItems>> iterator = it.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, WorkItems> entry = iterator.next();
			String itemCode = entry.getKey();
			WorkItems items = entry.getValue();
			Item item = new Item();
			item.amount = items.getTotalAmount();
			item.editQuantity = Double.toString(items.getTotalUnits());
			item.amountquantity = items.getAmountQuantity();
			item.itemnumber = items.getItemCode();
			item.formula = items.getFormula();
			if (items.isChanged()) {
				changedItem = itemCode;
			}
			item.fixed = items.getFixed();
			item.baseRate = Double.toString(items.getBaseRate());
			item.constant = items.getConstant();
			item.constantvalue = items.getConstantValue();
			item.itemtypeid = Integer.toString(items.getWorkItemTypeId());
			itemMap.put(itemCode, item);
		}

		
		// calculation
		Map<String, Item> sortedItemMap = new TreeMap<String, Item>(itemMap);
		Iterator<Entry<String, Item>> iterat = sortedItemMap.entrySet().iterator();
		while (iterat.hasNext()) {
			Entry<String, Item> entry = iterat.next();
			
			Item item = entry.getValue();
			
			if (item.formula.contains(changedItem)) {
				changedItems.add(item.itemnumber);
				runFormula(item);
			}
			
			Iterator<String> changedIt = changedItems.iterator();
			
			while (changedIt.hasNext()) {
				String data = changedIt.next();

				if (item.formula.contains(data)) {
					changedItems.add(item.itemnumber);
					runFormula(item);
					break;
				}
			}
		}

//		
//		processUnCalculatedItems();
//		verifyAllItemsEstimated();
		
		// Replace values in input Hash map.
		HashMap<String, WorkItems> iter = new HashMap<String, WorkItems>();
		Iterator<Entry<String, Item>> its = itemMap.entrySet().iterator();
		totalAmount=0.0;
		while (its.hasNext()) {
			Entry<String, Item> entry = its.next();

			Item item = entry.getValue();
			String itemNumber = item.itemnumber;
			if (it.containsKey(itemNumber)) {
				WorkItems wi = it.get(itemNumber);
				wi.setTotalUnits(Double.parseDouble(item.editQuantity));
				wi.setTotalAmount(item.amount);
				totalAmount=totalAmount+item.amount;
				iter.put(itemNumber, wi);
			}
		}
		itemMap.clear();
		Log.e("Changed Item", changedItem);
		Log.e("Effected Items", changedItems.toString());
		i++;
		if(i<2)
		{
			processEditEstimationRequest(iter);
		}
		
		
		return iter;
	}

	private void parseXmlToCalculateProjectEstimation(Document doc) {
		processFirstTimeToEstimate(doc);
		processUnCalculatedItems();
		verifyAllItemsEstimated();
		updateTaxPart();
		
	}

	private void processFirstTimeToEstimate(Document doc) {
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
				NodeList itemList = groupElement.getElementsByTagName("item");
				for (int i = 0; i < itemList.getLength(); ++i) {
					Element itemElement = (Element) itemList.item(i);
					NodeList itemChild = itemElement.getChildNodes();
					Item item = parseNodeGetItem(itemChild);
					
					runFormula(item);
					itemMap.put(item.itemnumber, item);
					 /* if(!item.unCalculated)
		                {
		                	 sumofGroups(item);
		                }
*/				}

			}
		}
	}

	private void processUnCalculatedItems() {
		Iterator<Entry<String, Item>> iterator = itemMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Item> entry = iterator.next();
			String itemNumber = entry.getKey();
			Item item = entry.getValue();
			if (item.unCalculated) {
				item.unCalculated = false;
				runFormula(item);
				if (item.unCalculated) {
					uncalItemMap.put(itemNumber, item);
				}
				itemMap.put(itemNumber, item);
				// iterator.remove();
			}
                if(!item.unCalculated)
                {
                	 sumofGroups(item);
                }
			Log.e("TAG --- LIST", item.itemtypeid + " : " + item.editQuantity
					+ " : " + item.amount + " :" + item.baseRate + " :"
					+ item.constantvalue + " :" + item.isInt);
			
           
		}
		System.out.println(paraMap);
		
	}
private void updateTaxPart()
{
	Iterator<Entry<String, Item>> iterator = itemMap.entrySet().iterator();
	while (iterator.hasNext()) {
		Entry<String, Item> entry = iterator.next();
		String itemNumber = entry.getKey();
		Item item = entry.getValue();
		/*if (item.groupId.equalsIgnoreCase("E4B1G3")) 
		    {*/
			runFormula(item);
			itemMap.put(itemNumber, item);
			/*}*/
	}
}
   private void sumofGroups(Item item)
   {
	   if(item.groupId.equalsIgnoreCase("E4B2G1"))
       {
      	 E4B2G1=E4B2G1+item.amount;
      	 paraMap.put("E4B2G1",E4B2G1);            
      	 
       }
	   else  if(item.groupId.equalsIgnoreCase("E3B1G1"))
       {
		 E3B1G1=E3B1G1+item.amount;
      	 paraMap.put("E3B1G1",E3B1G1);       
       }
	   else  if(item.groupId.equalsIgnoreCase("E3B1G2"))
       {
		 E3B1G2=E3B1G2+item.amount;
      	 paraMap.put("E3B1G2",E3B1G2);       
       }
       else  if(item.groupId.equalsIgnoreCase("E4B2G2"))
       {
      	 E4B2G2=E4B2G2+item.amount;
      	 paraMap.put("E4B2G2",E4B2G2);       
       }
       else  if(item.groupId.equalsIgnoreCase("E4B1G1"))
       {
      	 E4B1G1=E4B1G1+item.amount;
      	 paraMap.put("E4B1G1",E4B1G1);  
       }
       else  if(item.groupId.equalsIgnoreCase("E4B1G2"))
       {
      	 E4B1G2=E4B1G2+item.amount;
      	 paraMap.put("E4B1G2",E4B1G2); 
       }
       else  if(item.groupId.equalsIgnoreCase("E4B3G1"))
       {
      	 E4B3G1=E4B3G1+item.amount;
      	 paraMap.put("E4B3G1",E4B3G1); 
       }
       else  if(item.groupId.equalsIgnoreCase("E4B3G2"))
       {
      	 E4B3G2=E4B3G2+item.amount;
      	 paraMap.put("E4B3G2",E4B3G2); 
       }
       else  if(item.groupId.equalsIgnoreCase("E8B1G1"))
       {
    	   E8B1G1=E8B1G1+item.amount;
      	 paraMap.put("E8B1G1",E8B1G1); 
       }
	   
       else  if(item.groupId.equalsIgnoreCase("E8B1G2"))
       {
    	   E8B1G2=E8B1G2+item.amount;
      	 paraMap.put("E8B1G2",E8B1G2); 
       }
       else  if(item.groupId.equalsIgnoreCase("E8B3G1"))
       {
    	   E8B3G1=E8B3G1+item.amount;
      	 paraMap.put("E8B3G1",E8B3G1); 
       }
       else  if(item.groupId.equalsIgnoreCase("E8B3G2"))
       {
    	   E8B3G2=E8B3G2+item.amount;
      	 paraMap.put("E8B3G2",E8B3G2); 
       }
       else  if(item.groupId.equalsIgnoreCase("E8B2G1"))
       {
    	   E8B2G1=E8B2G1+item.amount;
      	 paraMap.put("E8B2G1",E8B2G1); 
       }
       else  if(item.groupId.equalsIgnoreCase("E8B2G2"))
       {
    	   E8B2G2=E8B2G2+item.amount;
      	 paraMap.put("E8B2G2",E8B2G2); 
       }
	   //E8B1G1,E8B3G1,E8B2G1
	   
   }
	private void verifyAllItemsEstimated() {
		while (!uncalItemMap.isEmpty()) {
			Iterator<Entry<String, Item>> uniterator = uncalItemMap.entrySet()
					.iterator();
			while (uniterator.hasNext()) {
				Entry<String, Item> entry = uniterator.next();
				String itemNumber = entry.getKey();
				Item item = entry.getValue();
			
				if (item.unCalculated) {

					item.unCalculated = false;
					runFormula(item);
					itemMap.put(itemNumber, item);
					if (item.unCalculated) {
						uncalItemMap.put(itemNumber, item);
					} else {
							 
						sumofGroups(item);			            
						uniterator.remove();
					}

					// iterator.remove();
				}

				Log.e("Verification --- LIST", item.itemtypeid + " : "
						+ item.editQuantity + " : " + item.amount + " :"
						+ item.baseRate + " :" + item.constantvalue + " :"
						+ item.isInt);

			}

		}
		System.out.println(paraMap);
	}

	private void parseGroup(NodeList nodeList) {

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("groupid")) {
					groupId = node.getTextContent().trim();
					System.out.println(node.getTextContent().trim());
				}
				if (node.getNodeName().equalsIgnoreCase("groupname")) {
					groupName = node.getTextContent().trim();
					System.out.println(node.getTextContent().trim());
				}
			}
		}
	}

	private void parseParameter() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		//		String parameterId = new String();
		//		Double parameterValue = 0.0;
		baseService = new BaseService(ctx);
		/*		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("parameterid")) {
					parameterId = node.getTextContent().trim();

					if (parameterId.equalsIgnoreCase("LTP1")) {
						parameterValue = baseService.getLineLength(workRowId,
								"2");
					} else if (parameterId.equalsIgnoreCase("HTP1")) {
						parameterValue = baseService.getLineLength(workRowId,
								"1");
					} else if (parameterId.equalsIgnoreCase("LTP3")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "2");
					} else if (parameterId.equalsIgnoreCase("HTP3")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "1");
					}

				}
				if (node.getNodeName().equalsIgnoreCase("parametervalue")) {
					if (parameterId.equalsIgnoreCase("LTP2")
							|| parameterId.equalsIgnoreCase("HTP2")|| parameterId.equalsIgnoreCase("TCP1")) {
						parameterValue = Double.parseDouble(node
								.getTextContent().trim());
					}

				}

				if (node.getNodeName().equalsIgnoreCase("parameterid")) {
					parameterId = node.getTextContent().trim();

					if (parameterId.equalsIgnoreCase("LTD")) {
						parameterValue = baseService.getLineLength(workRowId,
								"2");
					} else if (parameterId.equalsIgnoreCase("HTD")) {
						parameterValue = baseService.getLineLength(workRowId,
								"1");
					} else if (parameterId.equalsIgnoreCase("LTB")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "2");
					} else if (parameterId.equalsIgnoreCase("HTB")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "1");
					}else if (parameterId.equalsIgnoreCase("LTSL")) {
						parameterValue = (double) baseService.getSpanLength(
								workRowId);
					}else if (parameterId.equalsIgnoreCase("HTSL")) {
						parameterValue = (double) baseService.getSpanLength(
								workRowId);
					}else if (parameterId.equalsIgnoreCase("LTIP")) {
						parameterValue = (double)baseService.getIntermediatePoleCount(workRowId, "2");
					}else if (parameterId.equalsIgnoreCase("HTIP")) {
						parameterValue = (double) baseService.getIntermediatePoleCount(workRowId, "1");
					}else if (parameterId.equalsIgnoreCase("HTB")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "1");
					}else if (parameterId.equalsIgnoreCase("HTB")) {
						parameterValue = (double) baseService.getNumberOfBends(
								workRowId, "1");
					}



				}
				if (node.getNodeName().equalsIgnoreCase("parametervalue")) {
					if (parameterId.equalsIgnoreCase("LTP2")
							|| parameterId.equalsIgnoreCase("HTP2")|| parameterId.equalsIgnoreCase("TCP1")) {
						parameterValue = Double.parseDouble(node
								.getTextContent().trim());
					}

				}

			}
		}*/
		if(projectCategory == 4 || projectCategory == 1)
		{
			ProjectDetail projectDetail=new ProjectDetail();
			projectDetail.setProjectId(Integer.valueOf(workRowId));
			projectDetail.setProjectCategory(projectCategory);
			projectDetail.setHtLength(baseService.getLineLength(workRowId, "1"));
			projectDetail.setLtLength(baseService.getLineLength(workRowId, "2"));
			HashMap<String, Integer> StudePoleCount = baseService.getStudePoleCount(workRowId);
			projectDetail.setHtBend(baseService.findBendCount(workRowId, "1") );
			projectDetail.setLtBend(baseService.findBendCount(workRowId, "2"));
			projectDetail.setLtSpanLength(baseService.getSpanLength(workRowId));
			projectDetail.setHtSpanLength(baseService.getSpanLength(workRowId));
			projectDetail.setLtIntrPoleCount(baseService.getIntermediatePoleCount(workRowId, "2"));
			projectDetail.setHtIntrPoleCount(baseService.getIntermediatePoleCount(workRowId, "1"));
			projectDetail.setHtCount(baseService.findHtCount(workRowId));
			projectDetail.setLtCount(baseService.findLtCount(workRowId));
			projectDetail.setTcCount(baseService.findTcCount(workRowId));
			projectDetail.setHtStudpoleCout(StudePoleCount.get("HtCount"));
			projectDetail.setLtStudpoleCout(StudePoleCount.get("LtCount"));
			Field[] firlds=projectDetail.getClass().getDeclaredFields();
			for (int i = 0; i < firlds.length; i++) {
				Field field = firlds[i];
				Method method = projectDetail.getClass().getMethod("get" + capitalize(field.getName()));
				Annotation[] ant=field.getAnnotations();
				for (int j = 0; j < ant.length; j++) {
					Paraname ann = (Paraname)ant[j];

					Object value=method.invoke(projectDetail, (Object[])null);
					if(value instanceof Double)
					{
						paraMap.put(ann.name(),(Double)value);
						System.out.println(ann.name()+" = " +value);
					}

				}

			}
			Field[] superFields=projectDetail.getClass().getSuperclass().getDeclaredFields();
			for (int i = 0; i < superFields.length; i++) {
				Field field = superFields[i];
				Method method = projectDetail.getClass().getMethod("get" + capitalize(field.getName()));
				Annotation[] ant=field.getAnnotations();
				for (int j = 0; j < ant.length; j++) {
					Paraname ann = (Paraname)ant[j];

					Object value=method.invoke(projectDetail, (Object[])null);
					if(value instanceof Double)
					{
						paraMap.put(ann.name(),(Double)value);
						System.out.println(ann.name()+" = " +value);
					}

				}

			}
		}
		if(projectCategory == 8 || projectCategory==9)
		{
			CableDetail projectDetail = new CableDetail();
			projectDetail.setProjectId(Integer.valueOf(workRowId));
			projectDetail.setProjectCategory(projectCategory);
			if(projectCategory==9)
			{
				projectDetail.setHtLength(baseService.getLineLengthofconductor(workRowId, "1"));
			}
			else{projectDetail.setHtLength(baseService.getLineLength(workRowId, "1"));}
			projectDetail.setLtLength(baseService.getLineLength(workRowId, "2"));
			HashMap<String, Integer> StudePoleCount = baseService.getStudePoleCount(workRowId);
			projectDetail.setHtBend(baseService.findBendCount(workRowId, "1") );
			projectDetail.setLtBend(baseService.findBendCount(workRowId, "2"));
			projectDetail.setLtSpanLength(baseService.getSpanLength(workRowId));
			projectDetail.setHtSpanLength(baseService.getSpanLength(workRowId));
			projectDetail.setLtIntrPoleCount(baseService.getIntermediatePoleCount(workRowId, "2"));
			projectDetail.setHtIntrPoleCount(baseService.getIntermediatePoleCount(workRowId, "1"));
			projectDetail.setHtCount(baseService.findHtCount(workRowId));
			projectDetail.setLtCount(baseService.findLtCount(workRowId));
			projectDetail.setTcCount(baseService.findLtCount(workRowId));
			projectDetail.setHtStudpoleCout(StudePoleCount.get("HtCount"));
			projectDetail.setLtStudpoleCout(StudePoleCount.get("LtCount"));
			projectDetail.setCableHorizontalDrillLength(baseService.getLayingLength(workRowId,"1", "H"));
			projectDetail.setCableManualLength(baseService.getLayingLength(workRowId,"1", "M"));
			projectDetail.setCableFourLength(baseService.getConductorLengthWithPhase(workRowId,"1", "4093","0"));
			projectDetail.setCableTwoLength(baseService.getConductorLengthWithPhase(workRowId,"1", "4112","0"));
			projectDetail.setCableNineLength(baseService.getConductorLengthWithPhase(workRowId,"1", "4120","0"));
			projectDetail.setNoOfCables(baseService.getMaterialInformation(workRowId, "1").size());
			Field[] firlds=projectDetail.getClass().getDeclaredFields();
			for (int i = 0; i < firlds.length; i++) {
				Field field = firlds[i];
				Method method = projectDetail.getClass().getMethod("get" + capitalize(field.getName()));
				Annotation[] ant=field.getAnnotations();
				for (int j = 0; j < ant.length; j++) {
					Paraname ann = (Paraname)ant[j];

					Object value=method.invoke(projectDetail, (Object[])null);
					if(value instanceof Double)
					{
						paraMap.put(ann.name(),(Double)value);
						System.out.println(ann.name()+" = " +value);
					}

				}

			}
			Field[] superFields=projectDetail.getClass().getSuperclass().getDeclaredFields();
			for (int i = 0; i < superFields.length; i++) {
				Field field = superFields[i];
				Method method = projectDetail.getClass().getMethod("get" + capitalize(field.getName()));
				Annotation[] ant=field.getAnnotations();
				for (int j = 0; j < ant.length; j++) {
					Paraname ann = (Paraname)ant[j];

					Object value=method.invoke(projectDetail, (Object[])null);
					if(value instanceof Double)
					{
						paraMap.put(ann.name(),(Double)value);
						System.out.println(ann.name()+" = " +value);
					}

				}

			}

		}

		System.out.println(paraMap);
	}

	public String capitalize(String string) {
		String capital = string.substring(0, 1).toUpperCase();
		return capital + string.substring(1);
	}
	// To get Block id and Block Name.
	private void parseBlock(NodeList nodeList) {

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equalsIgnoreCase("blockid")) {
					blockId = node.getTextContent().trim();
					System.out.println(node.getTextContent().trim());
				}
				if (node.getNodeName().equalsIgnoreCase("blockname")) {
					blockName = node.getTextContent().trim();
					System.out.println(node.getTextContent().trim());
				}
			}
		}
	}

	// To get Item related data.
	private Item parseNodeGetItem(NodeList nodeList) {
		Item item = new Item();

		for (int y = 0; y < nodeList.getLength(); ++y) {
			Node node = nodeList.item(y);
			item.blockId = blockId;
			item.blockName = blockName;
			item.groupId = groupId;
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
							item.editQuantity = value;
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
					}
					else {
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

	private void runFormula(Item item) {

		Double baseRate = bs.getBaseRate(item.itemtypeid);
		item.description = bs.getDescription(item.itemtypeid);
		System.out.println(item.itemtypeid);

		if (item.fixed.equalsIgnoreCase("Y")
				&& item.constant.equalsIgnoreCase("Y")) {
			// item.quantity = "0.0";
			item.amount = Math.round(Double.parseDouble(item.constantvalue));
			item.baseRate = Double.toString(baseRate);
			
			totalAmount = totalAmount + item.amount;
		}

		if (item.fixed.equalsIgnoreCase("Y")
				&& item.constant.equalsIgnoreCase("N")) {

			item.amount = Math.round(baseRate * Double.parseDouble(item.editQuantity));
			item.baseRate = Double.toString(baseRate);
			totalAmount = totalAmount + item.amount;
		}

		if (item.fixed.equalsIgnoreCase("N")
				&& item.constant.equalsIgnoreCase("N")) {
			Map<String, Double> localHashMap = new HashMap<String, Double>();
			StringTokenizer tokenizer = new StringTokenizer(item.formula
					.toString().trim(), "+-()*/ 	");
			while (tokenizer.hasMoreElements()) {
				String check = tokenizer.nextToken();
				Item itemValue = new Item();
				if (check.startsWith("E") || check.startsWith("H")
						|| check.startsWith("L") || check.startsWith("T")) {
					if (itemMap.containsKey(check)) {
						itemValue = itemMap.get(check);
						if (!itemValue.unCalculated) {
							if (item.amountquantity.equalsIgnoreCase("Q")) {
								localHashMap.put(check, Double
										.parseDouble(itemValue.editQuantity));
							} else if (item.amountquantity
									.equalsIgnoreCase("A")) {
								localHashMap.put(check, itemValue.amount);
							}
						} else {
							item.unCalculated = true;
							break;
						}
					} else if (paraMap.containsKey(check)) {
						localHashMap.put(check, paraMap.get(check));
					} else {
						item.unCalculated = true;
						break;
					}
				}
			}

			if (!item.unCalculated) {
				Calculable calc = null;
				if (!item.formula.trim().isEmpty()) {
					try {
						double formulaResult = 0.0;
						calc = new ExpressionBuilder(item.formula)
								.withVariables(localHashMap).build();
			
						formulaResult = calc.calculate();
						if (item.isInt.equalsIgnoreCase("Y")) {
							formulaResult = (int) formulaResult;
						}

						if (item.decRound.equalsIgnoreCase("3")) {
							formulaResult = (Math.round(formulaResult * 100.0) / 100.0);
						} else {
							formulaResult = Math.round(formulaResult);
						}
						if (formulaResult < 0) {
							formulaResult = 0.0;
						}
						if (item.amountquantity.equalsIgnoreCase("A")) {

							item.amount = Math.round(formulaResult);

							baseRate = bs.getBaseRate(item.itemtypeid);
							item.baseRate = Double.toString(baseRate);
							item.unCalculated = false;
							totalAmount = totalAmount + item.amount;
							// item.quantity=Double.toString(formulaResult);
						} else if (item.amountquantity.equalsIgnoreCase("Q")) {
							// TODO: formulaResult BaseRate to Amount

							baseRate = bs.getBaseRate(item.itemtypeid);
							item.baseRate = Double.toString(baseRate);
							item.editQuantity = Double.toString(formulaResult);

							item.amount = Math.round(formulaResult * baseRate);
							totalAmount = totalAmount + item.amount;
							item.unCalculated = false;
						}else if (item.amountquantity.equalsIgnoreCase("P"))
						{
							item.editQuantity = Double.toString(formulaResult);
						}
					} catch (UnknownFunctionException e) {
						e.printStackTrace();
					} catch (UnparsableExpressionException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}