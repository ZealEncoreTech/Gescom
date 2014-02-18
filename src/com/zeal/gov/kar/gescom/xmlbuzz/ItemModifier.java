package com.zeal.gov.kar.gescom.xmlbuzz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.util.CellRangeAddress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.zeal.gov.kar.gescom.annotations.ColumnInfo;
import com.zeal.gov.kar.gescom.annotations.Paraname;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.xmlmodel.Block;
import com.zeal.gov.kar.gescom.xmlmodel.Estimation;
import com.zeal.gov.kar.gescom.xmlmodel.Group;
import com.zeal.gov.kar.gescom.xmlmodel.Item;
import com.zeal.gov.kar.gescom.xmlmodel.Parameter;
import com.zeal.gov.kar.gescom.xmlmodel.ProjectDetail;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

@SuppressLint("NewApi")
public class ItemModifier {
	
	private List<Item> itemList;
	private List<Group> groupList;
	private List<Block> blockList;
	private List<Parameter> parameterList;
	private HashMap<String,List> xmlData;
	private XmlProcessor xp;
	private static String xmlPath="C:\\Documents and Settings\\ragavendra.dg\\Desktop\\GESCOM\\assets\\Estimation_Cat_IV.xml";
	private ProjectDetail projectDetail;
	private Context context;
    private BaseService baseService;
    public HashMap<String, Item> itemMap = new HashMap<String, Item>();
	private HashMap<String, Item> uncalItemMap = new HashMap<String, Item>(); 
	public HashMap<String, Double> paraMap = new HashMap<String, Double>();
	private double totalAmount;
	private double E4B2G1,E4B2G2,E4B1G1,E4B1G2,E4B3G1,E4B3G2;
	private double E3B1G1,E3B1G2,E8B3G1,E8B3G2,E8B2G1,E8B2G2,E8B1G1,E8B1G2;
	private double E9B1G1,E9B1G2,E9B3G1,E9B3G2,E9B2G1,E9B2G2;
	private double E10B1G1,E10B1G2,E10B3G1,E10B3G2,E10B2G1,E10B2G2;
	private List<String> fieldNames = new ArrayList<String>();
    private HashMap<String,Integer> tcData; 
    
	public ItemModifier(ProjectDetail projectDetail,Context context)
	{
		//Initialization
		 this.xp =new XmlProcessor(projectDetail.getProjectCategory());
		 this.xmlData=xp.getItemList();
		 this.itemList=xmlData.get("item");
		 this.groupList=xmlData.get("group");
		 this.blockList=xmlData.get("block");
		 this.parameterList=xmlData.get("parameter");
		 this.projectDetail=projectDetail;
		 this.context=context;
		 baseService=new BaseService(context);
	}
	
	public void loopIt()
	{
		removeUnwantedItems();
		for (Item itam : itemList) {
			System.out.println(itam.itemtypeid);
		}
	}
	public void clear()
	{
		 this.xmlData.clear();
		 this.itemList.clear();
		 this.groupList.clear();
		 this.blockList.clear();
		 this.parameterList.clear();
		 this.paraMap.clear();
	}
	
	
	private void editHtItems()
	{
	
		/*if(projectDetail.getProjectCategory()==4 && projectDetail.getHtCount()>0)
		{
	     getItemById("4004").formula=getItemById("4004").formula.concat("+ 6*"+Integer.toString( projectDetail.getHtCount()));
	     getItemById("4005").formula=getItemById("4005").formula.concat("-"+Integer.toString( projectDetail.getHtCount())+")*3");
		//TODO Edit HT items here.
		getItemById("4003").formula=getItemById("4003").formula.concat("+"+Integer.toString( projectDetail.getHtCount()));
		if(projectDetail.getHtStudpoleCout()>0)
		{
		getItemById("4081").formula=getItemById("4081").formula.replace("0",Integer.toString( projectDetail.getHtStudpoleCout()));
		getItemById("4029").formula=getItemById("4029").formula.concat("+"+projectDetail.getHtStudpoleCout());
		}
		if(projectDetail.getHtIntrPoleCount()>0)
		{
		getItemById("4002").formula=getItemById("4002").formula.concat("+"+Integer.toString(projectDetail.getHtIntrPoleCount()));
		
		}
		}*/
		if(projectDetail.getProjectCategory()==4)
		{
			if(projectDetail.getSoilType().equalsIgnoreCase("HS"))
			{
				getItemById("4014").itemtypeid="4165";
				getItemById("4015").itemtypeid="4166";
			}
		}
		if((projectDetail.getProjectCategory()==8||projectDetail.getProjectCategory()==9) && projectDetail.getHtCount()>0)
		{
		    HashMap<String,Integer> ltMaterial=baseService.getMaterialInformation(Integer.toString(projectDetail.getProjectId()), "1");
		    List<String> layingMethod=baseService.findLayingMethod(Integer.toString(projectDetail.getProjectId()));
		    
		    if(!ltMaterial.containsKey("4093"))
		    {
			    itemList.remove(getItemById("4093"));
		    	itemList.remove(getItemById("4094"));
		    	itemList.remove(getItemById("4095"));
		    	itemList.remove(getItemById("4103"));
		    	getItemById("4105").formula=getItemById("4105").formula.replace("E8B1G1I04", "0");
		    	getItemById("4106").formula=getItemById("4106").formula.replace("E8B1G1I07", "0");
		    	if(projectDetail.getProjectCategory()==9)
		    	{
		    		getItemById("4106").formula=getItemById("4106").formula.replace("E9B1G1I14", "0");
			    	getItemById("4105").formula=getItemById("4105").formula.replace("E9B1G1I11", "0");
		    	}
		    }
		    else
		    {
		    	//Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","2")
		    	getItemById("4093").formula=getItemById("4093").formula.replace("HTP1", Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"1", "4093","0")));
		    }
		    if(!ltMaterial.containsKey("4112"))
		    {
		    	itemList.remove(getItemById("4112"));
		    	itemList.remove(getItemById("4113"));
		    	itemList.remove(getItemById("4114"));
		    	itemList.remove(getItemById("4122"));
		    	getItemById("4105").formula=getItemById("4105").formula.replace("E8B1G1I05", "0");
		    	getItemById("4106").formula=getItemById("4106").formula.replace("E8B1G1I08", "0");
		    	if(projectDetail.getProjectCategory()==9)
		    	{
		    		getItemById("4106").formula=getItemById("4106").formula.replace("E9B1G1I15", "0");
			    	getItemById("4105").formula=getItemById("4105").formula.replace("E9B1G1I12", "0");
		    	}
		    }
		    else
		    {
		    	//Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","2")
		    	getItemById("4112").formula=getItemById("4112").formula.replace("HTL", Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"1", "4112","0")));
		    }
		    if(!ltMaterial.containsKey("4120"))
		    {
		    	itemList.remove(getItemById("4120"));
		    	itemList.remove(getItemById("4121"));
		    	itemList.remove(getItemById("4117"));
		    	itemList.remove(getItemById("4119"));
		    	getItemById("4105").formula=getItemById("4105").formula.replace("E8B1G1I06", "0");
		    	getItemById("4106").formula=getItemById("4106").formula.replace("E8B1G1I09", "0");
		    	if(projectDetail.getProjectCategory()==9)
		    	{
		    		getItemById("4106").formula=getItemById("4106").formula.replace("E9B1G1I16", "0");
			    	getItemById("4105").formula=getItemById("4105").formula.replace("E9B1G1I13", "0");
		    	}
		    }
		    else
		    {
		    	//Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","2")
		    	getItemById("4120").formula=getItemById("4120").formula.replace("HTP1", Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"1", "4120","0")));
		    }
		  /*  getItemById("4100").formula=getItemById("4100").formula.replace("1",String.valueOf(ltMaterial.size()));
		    getItemById("4101").formula=getItemById("4101").formula.replace("1",String.valueOf(ltMaterial.size()));
		   if(!ltMaterial.containsKey("4093")&&ltMaterial.containsKey("4112"))
		    {
		    	getItemById("4093").itemtypeid=getItemById("4093").itemtypeid="4112";
		    	getItemById("4094").itemtypeid=getItemById("4094").itemtypeid="4113";
		    	getItemById("4095").itemtypeid=getItemById("4095").itemtypeid="4114";
		    	getItemById("4103").itemtypeid=getItemById("4103").itemtypeid="4122";
		    }
		    if(!ltMaterial.containsKey("4093")&&ltMaterial.containsKey("4120"))
		    {
		    	getItemById("4093").itemtypeid=getItemById("4093").itemtypeid="4120";
		    	getItemById("4094").itemtypeid=getItemById("4094").itemtypeid="4121";
		    	getItemById("4095").itemtypeid=getItemById("4095").itemtypeid="4117";
		    	getItemById("4103").itemtypeid=getItemById("4103").itemtypeid="4119";
		    }*/
		    
		 /*   if(!projectDetail.getLayingMethod().equals("Manual"))
		    {
		    	itemList.remove(getItemById("4097"));
		    	Item item1 =new Item("26", "E8B1G2I01", "4115", "N", "N", "0.00", projectDetail.getHtLength()+"/0.05", "Q","E8B1G2","E8B1");
				itemList.add(item1);
				Item item2 =new Item("27", "E8B1G2I02", "4116", "N", "N", "0.00",projectDetail.getHtLength()+"-("+ projectDetail.getHtLength()+"/0.05)", "Q","E8B1G2","E8B1");
				itemList.add(item2);
		    }*/
		    //getItemById("4108").formula=getItemById("4108").formula.replace("HTP1",Double.toString(baseService.getLayingLength(Integer.toString(projectDetail.getProjectId()),"1", "M")));
		    if(!layingMethod.contains("M"))
		    {
		  //  getItemById("4097").formula=getItemById("4097").formula.replace("HTP1", Double.toString(baseService.getLayingLength(Integer.toString(projectDetail.getProjectId()),"1", "M")));
		    itemList.remove(getItemById("4097"));
		    }
		   
		    if(!layingMethod.contains("H"))
		    {
		    /*String hDrillLength=Double.toString(baseService.getLayingLength(Integer.toString(projectDetail.getProjectId()),"1", "H"));
		    Item item1 =new Item("26", "E8B1G2I051", "4115", "N", "N", "0.00", hDrillLength+"*0.05", "Q","E8B1G2","E8B1");
			itemList.add(item1);
			Item item2 =new Item("27", "E8B1G2I052", "4116", "N", "N", "0.00",hDrillLength+"-("+ hDrillLength+"*0.05)", "Q","E8B1G2","E8B1");
			itemList.add(item2);*/
		    	itemList.remove(getItemById("4115"));
		    	itemList.remove(getItemById("4116"));
		    }
		}
		
	}

	
	
	
	
	private void editLtItems()
	{
		if(projectDetail.getProjectCategory()==4)
		{
			if(projectDetail.getSoilType().equalsIgnoreCase("HS"))
			{
				getItemById("4045").itemtypeid="4167";
				getItemById("4046").itemtypeid="4168";
			}
		}
		//TODO Edit LT items here.
		if((projectDetail.getProjectCategory()==4  || projectDetail.getProjectCategory()==8 || projectDetail.getProjectCategory()==9) && projectDetail.getLtCount()>0 )
		{ 
		/*getItemById("4038").editQuantity=getItemById("4038").editQuantity=Integer.toString( projectDetail.getLtCount());
		
		
		    getItemById("4040").formula=getItemById("4040").formula.concat("+"+Integer.toString(projectDetail.getLtCount())); //for resolving intermediate pole issue when distance is less than span length.
		    if(projectDetail.getLtStudpoleCout()>0)
		     {
		     getItemById("4083").formula=getItemById("4083").formula.replace("0",Integer.toString( projectDetail.getLtStudpoleCout()));
		     getItemById("4057").formula=getItemById("4057").formula.concat("+"+projectDetail.getLtStudpoleCout());
		     }
			if(projectDetail.getLtIntrPoleCount()>0)
			{
			getItemById("4039").formula=getItemById("4039").formula.concat("+"+Integer.toString(projectDetail.getLtIntrPoleCount()));
			getItemById("4040").formula=getItemById("4040").formula.replace("+1", "");	
			//getItemById("4039").formula=getItemById("4039").formula.concat("+"+Integer.toString(projectDetail.getLtIntrPoleCount()));
			getItemById("4041").formula=getItemById("4041").formula.concat("-"+Integer.toString(projectDetail.getLtIntrPoleCount())+"*4");
			}*/
			
			StringBuffer formula=new StringBuffer();
			formula.append("(("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","1"))+"/1000*(2*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","2"))+"/1000*(3*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2","4043","3"))+"/1000*(4*1.045))");
			formula.append("+"+"("+Double.toString(baseService.getConductorLengthWithPhase(Integer.toString(projectDetail.getProjectId()),"2", "4043","4"))+"/1000*(5*1.045)))");
			getItemById("4043").formula=getItemById("4043").formula=formula.toString();
			getItemById("4049").formula=getItemById("4049").formula=formula.toString();
			
		}
	    
	    
	}
	
	public double getGrandTotal()
	{
		return totalAmount;
	}
	
	 
	
	private void editTcItems()
	{
		if(projectDetail.getProjectCategory()!=1){
		//TODO Edit TC items here.
		int lno = 63;
		int itemNo = 1;
		String tcgroupId="E"+projectDetail.getProjectCategory()+"B2G1I0";
		String tcLabourgroupId="E"+projectDetail.getProjectCategory()+"B2G2I0";
		HashMap<String,String> tclabour=new HashMap<String, String>();
	    tclabour.put("4075", "4078");
	    tclabour.put("4076", "4079");
	    tclabour.put("4077", "4080");
	    tclabour.put("4031", "4032");
	    
	    tcData=baseService.getMaterialInformation(Integer.toString(projectDetail.getProjectId()), "3");
		System.out.println(tcData);
		if(tcData.size()>0)
		{
		//Add TC to TC Block.
		Iterator<Entry<String, Integer>> iterator = tcData.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			String tcType = entry.getKey();
			int tcCount=entry.getValue();
			Item item =new Item(""+lno++, tcgroupId+""+itemNo++, ""+tcType, "N", "N", "0.00", ""+tcCount, "Q","E"+projectDetail.getProjectCategory()+"B2G1","E"+projectDetail.getProjectCategory()+"B2");
			item.groupName="Particulars";
			itemList.add(item);
		}
		
		//Add Tc labour charges.
		Iterator<Entry<String, Integer>> iterator2 = tcData.entrySet().iterator();
		while (iterator2.hasNext()) {
			Entry<String, Integer> entry = iterator2.next();
			String tcType = entry.getKey();
			int tcCount=entry.getValue();
			Item item =new Item(""+lno++, tcLabourgroupId+""+itemNo++, ""+tclabour.get(tcType), "N", "N", "0.00", ""+tcCount, "Q","E"+projectDetail.getProjectCategory()+"B2G2","E"+projectDetail.getProjectCategory()+"B2");
			item.groupName="Labour Charges";
			itemList.add(item);
		}
		}
		}
	}
	
	
	
	private void arrangeItems()
	{
		// Adding items to corresponding blocks.
		//Arrangement.
		for(Item it : itemList)
		{
			Group group=getGroupById(it.getGroupId());
			group.setItem(it);
		}
		for(Group group:groupList)
		{
			Block bl=getBlockById(group.getBlockId());
			bl.setGroup(group);
			
		}	
	}
	
	
	
	private void editItems()
	{
		///Xml editing here .....		
		if(projectDetail.getLtCount()>0)
		editLtItems();
		if(projectDetail.getHtCount()>0)
		editHtItems();
		if(baseService.getMaterialInformation(Integer.toString(projectDetail.getProjectId()), "3").size()>0)
		editTcItems();		
	}

	
	
	private void editParameters() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		/*//Set HT distance.
		getParameterById("HTP1").setParametervalue(Double.toString(projectDetail.getHtLength()));
		paraMap.put("HTP1", projectDetail.getHtLength());
		
		//Set LT distance.
		getParameterById("LTP1").setParametervalue(Double.toString(projectDetail.getLtLength()));
		paraMap.put("LTP1", projectDetail.getLtLength());
		
		//Set HT span length value.
		getParameterById("HTP2").setParametervalue(Double.toString(projectDetail.getHtSpanLength()));
		paraMap.put("HTP2", (double)projectDetail.getHtSpanLength());

		//Set LT span length value.
		getParameterById("LTP2").setParametervalue(Double.toString(projectDetail.getLtSpanLength()));
		paraMap.put("LTP2", (double)projectDetail.getLtSpanLength());
		
		//Set HT bends value.
		getParameterById("HTP3").setParametervalue(Double.toString(projectDetail.getHtBend()));
		paraMap.put("HTP3", (double)projectDetail.getHtBend());
		
		//Set  LT bends values.
		getParameterById("LTP3").setParametervalue(Double.toString(projectDetail.getLtBend()));
		paraMap.put("LTP3",(double) projectDetail.getLtBend());*/
		
		System.out.println(projectDetail.getClass().getName());
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
		
		System.out.println(paraMap);
	}
	
	private void removeUnwantedItems()
	{
		tcData=baseService.getMaterialInformation(Integer.toString(projectDetail.getProjectId()), "3");
		System.out.println(tcData);
		Iterator<Item> itemIterator=itemList.iterator();
		while (itemIterator.hasNext()) {
			Item item=itemIterator.next();
			if(projectDetail.getHtCount()==0  && item.blockId.equalsIgnoreCase("E"+projectDetail.getProjectCategory()+"B1"))
			{
				itemIterator.remove();
			}
			if(tcData.size()==0  && item.blockId.equalsIgnoreCase("E"+projectDetail.getProjectCategory()+"B2"))
			{
				itemIterator.remove();
			}
			if(projectDetail.getLtCount()==0  && item.blockId.equalsIgnoreCase("E"+projectDetail.getProjectCategory()+"B3"))
			{
				itemIterator.remove();
			}
		}
	}
	public void createEstimation()
	{
		try {
			editParameters();
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
		removeUnwantedItems();
		editItems();
		for (Item item : itemList) {
			runFormula(item);
			itemMap.put(item.itemnumber, item);
			sumofGroups(item);
		}
		
		verifyAllItemsEstimated();
		updateTaxPart();
		Iterator<Entry<String, Item>> iterator = itemMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Item> entry = iterator.next();
			String itemNumber = entry.getKey();
			Item item = entry.getValue();
			Log.e("TAG --- LIST", item.itemtypeid + " : " + item.editQuantity
					+ " : " + item.amount + " :" + item.baseRate + " :"
					+ item.constantvalue + " :" + item.isInt);
		}
		arrangeItems();
		totalSum();
		createXLM();
		createExcel();
		
	}
	
	private void createXLM()
	{
		//blockList.remove(getBlockById("E4B3"));
 		Estimation est=new Estimation();
 		est.setBlocks(blockList);
 		est.setParameters(parameterList);
 
 		//Creating xml here.
 		XStream xstream = new XStream(new DomDriver());
 		xstream.registerConverter(new CustomMapper());
 		xstream.setMode(XStream.NO_REFERENCES);
 		xstream.alias("Parameter", Parameter.class);
 		xstream.alias("Group", Group.class);
 		xstream.alias("Item", Item.class);
 		xstream.alias("Block", Block.class);
 		xstream.alias("Estimation", Estimation.class);
 		xstream.autodetectAnnotations(true);
 		System.out.println(xstream.toXML(est));
 		FileOutputStream fos=null;
 		try {
			context.openFileOutput("newCategory.xml", context.MODE_PRIVATE);
			File data = Environment.getDataDirectory();
			File xmlFile = new File(data,
					"//data//com.zeal.gov.kar.gescomtesting//files//newCategory.xml");
			FileOutputStream fs = new FileOutputStream(xmlFile);
	        xstream.toXML(est, fs);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		finally{
 	        if(fos != null){
 	            try{
 	                fos.close();
 	            }catch (IOException e) {
 	                e.printStackTrace();
 	            }
 	        }
 	    }
 		copyXmlToSdcard();
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
	public void createXml()
	{
		//Editing parameters 
		//editParameters();
		// Editing items according to project details.
		editItems();		
		//Arrange items properly.
		arrangeItems();
		
		
		
		
		//Adding blocks and parameter to estimation xml.
 		//blockList.remove(getBlockById("E4B1"));
 		if(tcData.size()<=0)
		{
 			blockList.remove(getBlockById("E4B2"));
		}
 		//blockList.remove(getBlockById("E4B3"));
 		Estimation est=new Estimation();
 		est.setBlocks(blockList);
 		est.setParameters(parameterList);
 
 		//Creating xml here.
 		XStream xstream = new XStream(new DomDriver());
 		xstream.registerConverter(new CustomMapper());
 		xstream.setMode(XStream.NO_REFERENCES);
 		xstream.alias("Parameter", Parameter.class);
 		xstream.alias("Group", Group.class);
 		xstream.alias("Item", Item.class);
 		xstream.alias("Block", Block.class);
 		xstream.alias("Estimation", Estimation.class);
 		xstream.autodetectAnnotations(true);
 		System.out.println(xstream.toXML(est));
 		FileOutputStream fos=null;
 		try {
			context.openFileOutput("newCategory.xml", context.MODE_PRIVATE);
			File data = Environment.getDataDirectory();
			File xmlFile = new File(data,
					"//data//com.zeal.gov.kar.gescomtesting//files//newCategory.xml");
			FileOutputStream fs = new FileOutputStream(xmlFile);
	        xstream.toXML(est, fs);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		finally{
 	        if(fos != null){
 	            try{
 	                fos.close();
 	            }catch (IOException e) {
 	                e.printStackTrace();
 	            }
 	        }
 	    }
 		copyXmlToSdcard();
		
	}
	
	
	
	
	public Item getItemById(String id) {
		Item item = null;
		for (Item it : itemList) {
			if (it.itemtypeid.equalsIgnoreCase(id)) {
				item = it;
			}
		}
		return item;
	}

	public Block getBlockById(String id) {
		Block block = null;
		for (Block bl : blockList) {
			if (bl.getBlockId().equalsIgnoreCase(id)) {
				block = bl;
			}
		}
		return block;
	}

	public Group getGroupById(String id) {
		Group group = null;
		for (Group gp : groupList) {
			if (gp.getGroupid().equalsIgnoreCase(id)) {
				group = gp;
			}
		}
		return group;
	}

	public Parameter getParameterById(String id) {
		Parameter parameter = null;
		for (Parameter para : parameterList) {
			if (para.getParameterid().equalsIgnoreCase(id)) {
				parameter = para;
			}
		}
		return parameter;
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
						itemMap.remove(item);
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
	private void runFormula(Item item) {

		Double baseRate = baseService.getBaseRate(item.itemtypeid);
		item.description = baseService.getDescription(item.itemtypeid);
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
			item.editQuantity=item.qunatity; 
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
						}
						else {
							item.unCalculated = true;
							uncalItemMap.put(item.itemnumber, item);
							break;
						}
					} else if (paraMap.containsKey(check)) {
						localHashMap.put(check, paraMap.get(check));
					} else {

						item.unCalculated = true;
						uncalItemMap.put(item.itemnumber, item);
						break;
					}
				}
			}

			if (!item.unCalculated) {
				Calculable calc = null;
				if (!item.formula.trim().isEmpty()) {
					try {
						double formulaResult = 0.0;
						System.out.println(item.formula);
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

							baseRate = baseService.getBaseRate(item.itemtypeid);
							item.baseRate = Double.toString(baseRate);
							item.unCalculated = false;
							totalAmount = totalAmount + item.amount;
							// item.quantity=Double.toString(formulaResult);
						} else if (item.amountquantity.equalsIgnoreCase("Q")) {
							// TODO: formulaResult BaseRate to Amount

							baseRate = baseService.getBaseRate(item.itemtypeid);
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
	private void updateTaxPart()
	{
		totalAmount=0.0;
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
				totalAmount=totalAmount+item.amount;
		}
	}
	private void totalSum()
	{
		totalAmount=0.0;
		Iterator<Entry<String, Item>> iterator = itemMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Item> entry = iterator.next();
			String itemNumber = entry.getKey();
			Item item = entry.getValue();
				totalAmount=totalAmount+item.amount;
		}
	}
	private void sumofGroups(Item item)
	   {
		   if(item.getGroupId().equalsIgnoreCase("E4B2G1"))
	       {
	      	 E4B2G1=E4B2G1+item.amount;
	      	 paraMap.put("E4B2G1",E4B2G1);            
	      	 
	       }
		   else  if(item.getGroupId().equalsIgnoreCase("E3B1G1"))
	       {
			 E3B1G1=E3B1G1+item.amount;
	      	 paraMap.put("E3B1G1",E3B1G1);       
	       }
		   else  if(item.getGroupId().equalsIgnoreCase("E3B1G2"))
	       {
			 E3B1G2=E3B1G2+item.amount;
	      	 paraMap.put("E3B1G2",E3B1G2);       
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E4B2G2"))
	       {
	      	 E4B2G2=E4B2G2+item.amount;
	      	 paraMap.put("E4B2G2",E4B2G2);       
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E4B1G1"))
	       {
	      	 E4B1G1=E4B1G1+item.amount;
	      	 paraMap.put("E4B1G1",E4B1G1);  
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E4B1G2"))
	       {
	      	 E4B1G2=E4B1G2+item.amount;
	      	 paraMap.put("E4B1G2",E4B1G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E4B3G1"))
	       {
	      	 E4B3G1=E4B3G1+item.amount;
	      	 paraMap.put("E4B3G1",E4B3G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E4B3G2"))
	       {
	      	 E4B3G2=E4B3G2+item.amount;
	      	 paraMap.put("E4B3G2",E4B3G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E8B1G1"))
	       {
	    	 E8B1G1=E8B1G1+item.amount;
	      	 paraMap.put("E8B1G1",E8B1G1); 
	       }
		   
	       else  if(item.getGroupId().equalsIgnoreCase("E8B1G2"))
	       {
	    	   E8B1G2=E8B1G2+item.amount;
	      	 paraMap.put("E8B1G2",E8B1G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E8B3G1"))
	       {
	    	   E8B3G1=E8B3G1+item.amount;
	      	 paraMap.put("E8B3G1",E8B3G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E8B3G2"))
	       {
	    	   E8B3G2=E8B3G2+item.amount;
	      	 paraMap.put("E8B3G2",E8B3G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E8B2G1"))
	       {
	    	   E8B2G1=E8B2G1+item.amount;
	      	 paraMap.put("E8B2G1",E8B2G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E8B2G2"))
	       {
	    	   E8B2G2=E8B2G2+item.amount;
	      	 paraMap.put("E8B2G2",E8B2G2); 
	       }
		   //E8B1G1,E8B3G1,E8B2G1
		 //E9B1G1,E9B1G2,E9B3G1,E9B3G2,E9B2G1,E9B2G2;
	       else  if(item.getGroupId().equalsIgnoreCase("E9B1G1"))
	       {
	    	   E9B1G1=E9B1G1+item.amount;
	      	 paraMap.put("E9B1G1",E9B1G1); 
	       }
		   
	       else  if(item.getGroupId().equalsIgnoreCase("E9B1G2"))
	       {
	    	   E9B1G2=E9B1G2+item.amount;
	      	 paraMap.put("E9B1G2",E9B1G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E9B3G1"))
	       {
	    	   E9B3G1=E9B3G1+item.amount;
	      	 paraMap.put("E9B3G1",E9B3G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E9B3G2"))
	       {
	    	   E9B3G2=E9B3G2+item.amount;
	      	 paraMap.put("E9B3G2",E9B3G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E9B2G1"))
	       {
	    	   E9B2G1=E9B2G1+item.amount;
	      	 paraMap.put("E9B2G1",E9B2G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E9B2G2"))
	       {
	    	   E9B2G2=E9B2G2+item.amount;
	      	 paraMap.put("E9B2G2",E9B2G2); 
	       }

		 else  if(item.getGroupId().equalsIgnoreCase("E10B1G1"))
	       {
	    	   E10B1G1=E10B1G1+item.amount;
	      	 paraMap.put("E10B1G1",E9B1G1); 
	       }
		   
	       else  if(item.getGroupId().equalsIgnoreCase("E10B1G2"))
	       {
	    	   E10B1G2=E10B1G2+item.amount;
	      	 paraMap.put("E10B1G2",E10B1G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E10B3G1"))
	       {
	    	   E10B3G1=E10B3G1+item.amount;
	      	 paraMap.put("E10B3G1",E10B3G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E10B3G2"))
	       {
	    	   E10B3G2=E10B3G2+item.amount;
	      	 paraMap.put("E10B3G2",E10B3G2); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E10B2G1"))
	       {
	    	   E10B2G1=E9B2G1+item.amount;
	      	 paraMap.put("E10B2G1",E10B2G1); 
	       }
	       else  if(item.getGroupId().equalsIgnoreCase("E10B2G2"))
	       {
	    	   E10B2G2=E10B2G2+item.amount;
	      	 paraMap.put("E10B2G2",E10B2G2); 
	       }
	   }
	   
	   private void createExcel()
	   {
		    Collections.sort(itemList, new groupCapare());
		    HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Estimation for "+projectDetail.getProjectId());
			  
			Header headers = sheet.getHeader();
			headers.setCenter(HSSFHeader.font("Stencil-Normal", "Italic") +HSSFHeader.fontSize((short) 16) +"Estimation for "+projectDetail.getProjectName());
           
			PrintSetup ps = sheet.getPrintSetup();
			sheet.setAutobreaks(true);
			ps.setFitHeight((short)1);
			ps.setFitWidth((short)1);
			
			
            //Cell style for header row
            CellStyle cs = workbook.createCellStyle();
            cs.setFillForegroundColor(IndexedColors.LIME.getIndex());
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            cs.setLocked(true);
            cs.setWrapText(false);
            Font f = workbook.createFont();
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);
            f.setFontHeightInPoints((short) 12);
            cs.setFont(f);
            
            CellStyle style;
            Font titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short)12);
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            style.setFont(titleFont);
            
            
            //Cell style for summary row
            CellStyle css = workbook.createCellStyle();
            css.setAlignment(CellStyle.ALIGN_CENTER);
            f = workbook.createFont();
            f.setFontHeightInPoints((short) 10);
            css.setFont(f);
            
            CellStyle dss = workbook.createCellStyle();
            dss.setAlignment(CellStyle.ALIGN_LEFT);
            dss.setFont(f);

            HSSFRow projectHeader =sheet.createRow(0);
            projectHeader.setHeightInPoints(30);
            projectHeader.createCell(0).setCellValue(""+projectDetail.getProjectName().replace("*", " "));
            projectHeader.getCell(0).setCellStyle(css);
            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:E1"));
            
            
			HSSFRow header = sheet.createRow(1);
			header.createCell(0).setCellValue("ID    ");
			header.getCell(0).setCellStyle(cs);
			
			header.createCell(1).setCellValue("DESCRIPTION    ");
			header.getCell(1).setCellStyle(cs);
			
			header.createCell(2).setCellValue("RATE  ");
			header.getCell(2).setCellStyle(cs);
			
			header.createCell(3).setCellValue("QUANTITY  ");
			header.getCell(3).setCellStyle(cs);
			
			header.createCell(4).setCellValue("AMOUNT   ");
			header.getCell(4).setCellStyle(cs);
			sheet.createFreezePane(0, 2,0,3);
			sheet.protectSheet("raghu");
			
			sheet.createFreezePane(0,1);
			 sheet.setColumnWidth(0, (10 * 500));
		     sheet.setColumnWidth(1, (25 * 500));
		     sheet.setColumnWidth(2, (10 * 500));
		     sheet.setColumnWidth(3, (10 * 500));
		     sheet.setColumnWidth(4, (10 * 500));
			int tblRow=2;
			double grandTotal=0.0;
			for(Block block: blockList)
			{
				
				HSSFRow blockRow = sheet.createRow(tblRow++);
				blockRow.createCell(0).setCellValue(block.getBlockName());
				blockRow.getCell(0).setCellStyle(style);
				blockRow.setHeightInPoints(20);
				int bRow=blockRow.getRowNum();
				bRow++;
				sheet.addMergedRegion(CellRangeAddress.valueOf("A"+bRow+":E"+bRow));
				for(Group group:block.getGrouplist())
				{ 
					HSSFRow groupRow = sheet.createRow(tblRow++);
					groupRow.createCell(0).setCellValue(group.getGroupname());
					groupRow.getCell(0).setCellStyle(css);
					int data=groupRow.getRowNum();
					double total=0.0;
					data++;
					sheet.addMergedRegion(CellRangeAddress.valueOf("A"+data+":E"+data));
					for(Item item: group.getItems())
					{
					
			
			/*for(Item item:itemList)
			{*/
		
			System.out.println(item.getGroupId()+"::::::::::::::::::::::::::::::::::::::::");
			HSSFRow dataRow = sheet.createRow(tblRow++);
			dataRow.setRowStyle(css);
			dataRow.createCell(0).setCellValue(Integer.parseInt(item.itemtypeid));
			dataRow.getCell(0).setCellStyle(css);
			dataRow.createCell(1).setCellValue(item.description);
			dataRow.getCell(1).setCellStyle(dss);
			dataRow.createCell(2).setCellValue(Double.parseDouble(item.baseRate));
			dataRow.getCell(2).setCellStyle(css);
			dataRow.createCell(3).setCellValue(Double.parseDouble(item.editQuantity));
			dataRow.getCell(3).setCellStyle(css);
			dataRow.createCell(4).setCellValue(item.amount);
			total=total+item.amount;
			dataRow.getCell(4).setCellStyle(css);
			
					}
					grandTotal=grandTotal+total;
					HSSFRow groupTotalRow = sheet.createRow(tblRow++);
					groupTotalRow.createCell(0).setCellValue("Total");
					groupTotalRow.getCell(0).setCellStyle(css);
					groupTotalRow.createCell(4).setCellValue(total);
					int groupTotalRowCont=groupTotalRow.getRowNum();
					groupTotalRowCont++;
					sheet.addMergedRegion(CellRangeAddress.valueOf("A"+groupTotalRowCont+":C"+groupTotalRowCont));
				}
				
			}
			HSSFRow grandTotalRow = sheet.createRow(tblRow++);
			grandTotalRow.createCell(0).setCellValue("Grand Total");
			grandTotalRow.createCell(4).setCellValue(grandTotal);
			
			HSSFSheet projectDetailSheet = workbook.createSheet("Project Details of "+projectDetail.getProjectName().replace("*", " "));
            projectDetailSheet.setColumnWidth(0, (10 * 500));
            projectDetailSheet.setColumnWidth(1, (25 * 500));
            projectDetailSheet.protectSheet("raghu");
			HSSFRow projectDetailHeader = projectDetailSheet.createRow(0);
			projectDetailHeader.createCell(0).setCellValue("ITEM");
			projectDetailHeader.getCell(0).setCellStyle(cs);
			projectDetailHeader.createCell(1).setCellValue("QUANTITY");
			projectDetailHeader.getCell(1).setCellStyle(cs);
			
			
			try {
				
				setupFieldsForClass(projectDetail.getClass());
				Class<? extends Object> classz = projectDetail.getClass();
				Map<Integer,String> sort=new TreeMap<Integer, String>();
				
				int rowCount=1;
				for (String field : fieldNames) {
					
					Method method = classz.getMethod("get" + capitalize(field));
					java.lang.annotation.Annotation[] ant=method.getDeclaredAnnotations();
					for (int i = 0; i < ant.length; i++) {
						
						ColumnInfo c=(ColumnInfo) ant[i];
						if(!c.ignore())
						{
							Object value = method.invoke(projectDetail, (Object[]) null);
							sort.put(c.order(), c.name()+","+value);
							System.out.println(c.name());
						}
						
					}
				} 
				
				Iterator<Entry<Integer,String>> rowIt=sort.entrySet().iterator();
				while(rowIt.hasNext())
				{
					HSSFRow pdRow = projectDetailSheet.createRow(rowCount++);
					Map.Entry pairs = (Map.Entry) rowIt.next();
					String data=pairs.getValue().toString();
					String[] finalData=data.split(",");
					pdRow.setRowStyle(css);
	    			pdRow.createCell(0).setCellValue(finalData[0]);
	    			pdRow.getCell(0).setCellStyle(css);
	    			pdRow.createCell(1).setCellValue(finalData[1]);
	    			pdRow.getCell(1).setCellStyle(css);
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				context.openFileOutput("work.xls", context.MODE_PRIVATE);
				File data = Environment.getDataDirectory();
				File xmlFile = new File(data,"//data//com.zeal.gov.kar.gescomtesting//files//work.xls");
				FileOutputStream out = new FileOutputStream(xmlFile);
				
				workbook.write(out);
				out.close();
				
				System.out.println("Excel written successfully..");
				File sd = Environment.getExternalStorageDirectory();
				if (sd.canWrite()) {
					String currentDBPath = "//data//com.zeal.gov.kar.gescomtesting//files//work.xls";
					String backupDBPath = "//testingbackup//work"+projectDetail.getProjectId()+".xls";
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
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	   private class groupCapare implements Comparator<Item>
	   {

		@Override
		public int compare(Item lhs, Item rhs) {
			// TODO Auto-generated method stub
			return lhs.getGroupId().compareTo(rhs.getGroupId());
		}
		   
	   }
	   private boolean setupFieldsForClass(Class<?> clazz) throws Exception {
	        Field[] fields = clazz.getDeclaredFields();
	        for (int i = 0; i < fields.length; i++) {
	                fieldNames.add(fields[i].getName());
	        }
	        return true;
	}
		public String capitalize(String string) {
			String capital = string.substring(0, 1).toUpperCase();
			return capital + string.substring(1);
		}

}
