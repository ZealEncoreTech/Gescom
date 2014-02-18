package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.GescomUtility;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.Uploadestimation;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import com.zeal.gov.kar.gescom.xmlbuzz.ItemModifier;
import com.zeal.gov.kar.gescom.xmlmodel.CableDetail;
import com.zeal.gov.kar.gescom.xmlmodel.Item;
import com.zeal.gov.kar.gescom.xmlmodel.ProjectDetail;

public class CreateEstimationActivity extends Activity implements OnItemSelectedListener {

	private Spinner spinProjects;
	private List<String> projectList;
	private Button btnSave, btnSignature, btnCancel, btnSendtoserver;
	private TextView totalAmount;
	private ArrayAdapter<String> projectAdapter;
	private BaseService baseService;
	private HashMap<String, Integer> projectData;
	private TableLayout  btnTable;
	private int projectWorkrowId;
	private HashMap<String, Item> info;
	private TreeMap<String, Item> sortedMap;
	private int projectCategory;
	private String rs = "\u20A8", strSignature, currentYear;
	private double total;
	private final int DIGITAL_SIGNATURE = 100;
	private static final int PREFERENCECODE = 200;
	private int spanLength;
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	private List<Item> Itemlist;
	private String layingMethod;
	private boolean signaturePresent;
	private String soilType;
	private RadioGroup sl,soilCondition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_estimation);

		uiIntialization();
	}

	private void uiIntialization() {
		Itemlist=new ArrayList<Item>();
		listDataHeader = new ArrayList<String>();
		listDataHeader.add("HT-Line");
		listDataHeader.add("Transformer");
		listDataHeader.add("LT-Line");
		listAdapter = new ExpandableListAdapter(this, listDataHeader);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		expListView.setAdapter(listAdapter);
		expListView.refreshDrawableState();
		baseService = new BaseService(CreateEstimationActivity.this);
		btnSave = (Button) findViewById(R.id.savetoLocal);
		btnSignature = (Button) findViewById(R.id.btnSignature);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSendtoserver = (Button) findViewById(R.id.btnSendToServer);
		totalAmount = (TextView) findViewById(R.id.TotalofEstimation);
		btnTable = (TableLayout) findViewById(R.id.buttonTable);
		disableEnableControls(false, btnTable);
		btnCancel.setEnabled(true);
		signaturePresent=false;
		btnSignature.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent digSign = new Intent(CreateEstimationActivity.this,
						DigitalSignatureActivity.class);
				System.out.println(projectWorkrowId);
				digSign.putExtra("workRowId", String.valueOf(projectWorkrowId));
				CreateEstimationActivity.this.startActivityForResult(digSign,
						DIGITAL_SIGNATURE);
				copyDatabaseToSdcard();
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				copyDatabaseToSdcard();
			}
		});
		btnSendtoserver.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(signaturePresent)
				{
				updateEstimationToServer sendToServer = new updateEstimationToServer();
				sendToServer.execute();
				copyDatabaseToSdcard();
				}
				else
				{
					Toast.makeText(CreateEstimationActivity.this, "please put a signature ", Toast.LENGTH_LONG).show();
				}
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				saveEstimation task = new saveEstimation();
				task.execute();
				btnSave.setVisibility(View.GONE);
				projectList.remove(spinProjects.getSelectedItem().toString());
                 
			}
		});

		String status = "CCO";
		projectData = (HashMap<String, Integer>) baseService
				.getProjectesForEstimation(status,Appuser.getUserName());

		projectList = new ArrayList<String>();
		projectList.add("Select Project for Estimation");
		/*Iterator<Entry<String, Integer>> projectIterator = projectData
				.entrySet().iterator();
		while (projectIterator.hasNext()) {

			Map.Entry pairs = (Map.Entry) projectIterator.next();
			projectList.add(pairs.getKey().toString());
		}*/
		projectList.addAll(baseService.getProjectforSavecordinate(status,Appuser.getUserName()));
		spinProjects = (Spinner) findViewById(R.id.estProjectSpinner);
		projectAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, projectList);

		spinProjects.setOnItemSelectedListener(this);
		spinProjects.setAdapter(projectAdapter);

		spanLengthSetting();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_estimation, menu);
		return true;
	}
	   @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	 
	        case R.id.multipleEstimation:
	            Intent i = new Intent(this, MatreialDetails.class);
	            startActivityForResult(i,PREFERENCECODE);
	            break;
	 
	        }
	 
	        return true;
	    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if (position > 0) {
			
			// To get category Id of the selected project..
			String workDescription = projectList.get(position);
			projectWorkrowId = projectData.get(workDescription);
			
			projectCategory = baseService.getProjectCategory(projectWorkrowId);
			Log.e("<<<Category ID of Project>>>",
					Integer.toString(projectCategory));
           btnSave.setVisibility(View.VISIBLE);
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			LayoutInflater li = getLayoutInflater();
			View v = li.inflate(R.layout.estsetting, null);
			alertbox.setView(v);
			sl=(RadioGroup) v.findViewById(R.id.spradioGroup);
			soilCondition=(RadioGroup) v.findViewById(R.id.soilradioGroup);
			if(projectCategory==1)
			{disableEnableControls(false, sl);
			disableEnableControls(false, soilCondition);}
			
			alertbox.setMessage("Provide Details"); // Message to be displayed
			alertbox.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					switch(sl.getCheckedRadioButtonId()) {
	                case R.id.radio50:
	                	spanLength=50;
	                    break;
	                case R.id.radio40:
	                	spanLength=40;
	                    break;
	                case R.id.radio30:
	                	spanLength=30;
	                    break;
	                }
					switch(soilCondition.getCheckedRadioButtonId()) {
	                case R.id.radiohardsoil:
	                	soilType="SS";
	                    break;
	                case R.id.radiosoftsoil:
	                	soilType="HS";
	                    break;	         
	                }
					createEstimation task = new createEstimation();
					task.execute(Integer.toString(projectWorkrowId));
					baseService.updateSpanLength(String.valueOf(spanLength), String.valueOf(projectWorkrowId));
				}
			});
			alertbox.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							spinProjects.setSelection(0);
						}

					});

			// show the alert box will be swapped by other code later
			alertbox.show();
			
			
		} else {
			totalAmount.setText("");
			Itemlist.clear();
			listAdapter.notifyDataSetChanged();
		}

	}

	private void disableEnableControls(boolean enable, ViewGroup vg) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (child instanceof ViewGroup) {
				disableEnableControls(enable, (ViewGroup) child);
			} else {
				child.setEnabled(enable);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == DIGITAL_SIGNATURE && resultCode == 1) {
			String responseFromDigSign = data
					.getStringExtra("responsefromDigSign");
			if (responseFromDigSign.equalsIgnoreCase("success")) {
				signaturePresent=true;
				Toast.makeText(CreateEstimationActivity.this,
						"Signed Successfully", Toast.LENGTH_LONG).show();

			}
		}
		if(requestCode == PREFERENCECODE)
		{
		spanLengthSetting();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	private void spanLengthSetting()
	{
		 /*PreferenceManager.setDefaultValues(this,R.layout.materialdetails,false);
		 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		 spanLength=Integer.parseInt(sharedPrefs.getString("SpanLenght",""));
		 layingMethod=sharedPrefs.getString("layingMethod","");*/
	}
	private void createEstimation(String workRowId) {
		
		
        int ltNumber= baseService.isEstimationTypeAvailable(Integer.toString(projectWorkrowId), "2");
        int htNumber= baseService.isEstimationTypeAvailable(Integer.toString(projectWorkrowId), "1");
        int tcNumber= baseService.isEstimationTypeAvailable(Integer.toString(projectWorkrowId), "3");
       /* if(number>1 ||htNumber>1 )
        {*/
     /*   if(projectCategory==4)
        {
        	DynamicXmlCreator dynamicXml=new DynamicXmlCreator(CreateEstimationActivity.this,projectWorkrowId,spanLength);
        	try {
				dynamicXml.changeXml(ltNumber,htNumber,tcNumber);
				ProjectEstimation st = new ProjectEstimation(this, projectCategory,true);
				info = (HashMap<String, Item>) st.executeProjectEstimation(workRowId);
			    total=st.getTotalAmount();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
        /*if(projectCategory==8 || projectCategory==4)
        {*/
        /*	DynamicEight dynamicXml=new DynamicEight(CreateEstimationActivity.this,projectWorkrowId,spanLength);
        	try {
				dynamicXml.changeXml(ltNumber,htNumber,tcNumber);
				ProjectEstimation st = new ProjectEstimation(this, projectCategory,true);
				info = (HashMap<String, Item>) st.executeProjectEstimation(workRowId);
			    total=st.getTotalAmount();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			*/
        if(projectCategory == 4 || projectCategory == 1 || projectCategory == 10)
        {
        	Itemlist.clear();
        	ProjectDetail pd=new ProjectDetail();
        	pd.setProjectId(projectWorkrowId);
        	pd.setProjectCategory(projectCategory);
        	pd.setLayingMethod(layingMethod);
    		pd.setHtLength(baseService.getLineLength(Integer.toString(projectWorkrowId), "1"));
    		pd.setLtLength(baseService.getLineLength(Integer.toString(projectWorkrowId), "2"));
    		HashMap<String, Integer> StudePoleCount = baseService.getStudePoleCount(Integer.toString(projectWorkrowId));
    		pd.setHtBend(baseService.findBendCount(Integer.toString(projectWorkrowId), "1") );
    		pd.setLtBend(baseService.findBendCount(Integer.toString(projectWorkrowId), "2"));
    		pd.setLtSpanLength(spanLength);
    		pd.setHtSpanLength(spanLength);
    		pd.setLtIntrPoleCount(baseService.getIntermediatePoleCount(Integer.toString(projectWorkrowId), "2"));
    		pd.setHtIntrPoleCount(baseService.getIntermediatePoleCount(Integer.toString(projectWorkrowId), "1"));
    		pd.setHtCount(baseService.findHtCount(Integer.toString(projectWorkrowId)));
    		pd.setLtCount(baseService.findLtCount(Integer.toString(projectWorkrowId)));
    		pd.setTcCount(baseService.findTcCount(Integer.toString(projectWorkrowId)));
    		pd.setHtStudpoleCout(StudePoleCount.get("HtCount"));
    		pd.setLtStudpoleCout(StudePoleCount.get("LtCount"));
    		pd.setProjectName(spinProjects.getSelectedItem().toString());
    		pd.setSoilType(soilType);
    		ItemModifier IM=new ItemModifier(pd,this);
    		IM.createEstimation();
    		info = (HashMap<String, Item>)IM.itemMap;
    		total=0.0;
    		IM.clear();
        }
         if(projectCategory == 8 || projectCategory==9)
         {
        	Itemlist.clear();
         	CableDetail pd=new CableDetail();
         	pd.setProjectId(projectWorkrowId);
         	pd.setProjectCategory(projectCategory);
         	pd.setLayingMethod(layingMethod);
         	if(projectCategory==9)
         	{
     		pd.setHtLength(baseService.getLineLengthofconductor(Integer.toString(projectWorkrowId), "1"));
         	}
         	else{pd.setHtLength(baseService.getLineLength(Integer.toString(projectWorkrowId), "1"));}
     		pd.setLtLength(baseService.getLineLength(Integer.toString(projectWorkrowId), "2"));
     		HashMap<String, Integer> StudePoleCount = baseService.getStudePoleCount(Integer.toString(projectWorkrowId));
     		pd.setHtBend(baseService.findBendCount(Integer.toString(projectWorkrowId), "1") );
     		pd.setLtBend(baseService.findBendCount(Integer.toString(projectWorkrowId), "2"));
     		pd.setLtSpanLength(spanLength);
     		pd.setHtSpanLength(spanLength);
     		pd.setLtIntrPoleCount(baseService.getIntermediatePoleCount(Integer.toString(projectWorkrowId), "2"));
     		pd.setHtIntrPoleCount(baseService.getIntermediatePoleCount(Integer.toString(projectWorkrowId), "1"));
     		pd.setHtCount(baseService.findHtCount(Integer.toString(projectWorkrowId)));
     		pd.setLtCount(baseService.findLtCount(Integer.toString(projectWorkrowId)));
     		pd.setTcCount(baseService.findLtCount(Integer.toString(projectWorkrowId)));
     		pd.setHtStudpoleCout(StudePoleCount.get("HtCount"));
     		pd.setLtStudpoleCout(StudePoleCount.get("LtCount"));
     		pd.setProjectName(spinProjects.getSelectedItem().toString());
     		pd.setCableHorizontalDrillLength(baseService.getLayingLength(Integer.toString(projectWorkrowId),"1", "H"));
     		pd.setCableManualLength(baseService.getLayingLength(Integer.toString(projectWorkrowId),"1", "M"));
     		pd.setCableFourLength(baseService.getConductorLengthWithOutPhase(Integer.toString(projectWorkrowId),"1", "4093","0"));
     		pd.setCableTwoLength(baseService.getConductorLengthWithOutPhase(Integer.toString(projectWorkrowId),"1", "4112","0"));
     		pd.setCableNineLength(baseService.getConductorLengthWithOutPhase(Integer.toString(projectWorkrowId),"1", "4120","0"));
     		pd.setNoOfCables(baseService.getMaterialInformation(Integer.toString(projectWorkrowId), "1").size());
     		
     		ItemModifier IM=new ItemModifier(pd,this);
     		//IM.loopIt();
     		IM.createEstimation();
     		info = (HashMap<String, Item>)IM.itemMap;
     		total=0.0;
     		IM.clear();
         }
    		
    		
    		
       /* }
        else
        {*/
		/*ProjectEstimation st = new ProjectEstimation(this, projectCategory,false);
		info = (HashMap<String, Item>) st.executeProjectEstimation(workRowId);
	     total=st.getTotalAmount();*/
       /* }*/
		sortedMap = new TreeMap<String, Item>(info);
		Itemlist.clear();

		Iterator<Entry<String, Item>> iterator = sortedMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Item> entry = iterator.next();
			String itemNumber = entry.getKey();
			Item item = entry.getValue();
			if(!item.amountquantity.equalsIgnoreCase("P"))
			{
				Itemlist.add(item);
				total=total+item.amount;
				System.out.println(""+item.itemtypeid+" = "+item.amount );
			}
			else
			{
				iterator.remove();
			}
		}
		
	}

	private void savetoLocalDB() {
		baseService.deleteProjectDataFromEstimates(String.valueOf(projectWorkrowId));
		Iterator<Entry<String, Item>> iterator = sortedMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Item> entry = iterator.next();
			Item item = entry.getValue();
			sendToServer(item);
		}
	}

	@SuppressLint("NewApi")
	private void sendToServer(Item item) {
		Log.e("sssssss", item.itemtypeid);
		int measurementId = 0;
		WorkItems workItems = new WorkItems();
		workItems.setWorkRowid(projectWorkrowId);
		workItems.setWorkItemTypeId(Integer.parseInt(item.itemtypeid));
		workItems.setWorkItemDescription(item.description);
		workItems.setCostPerItem(Double.parseDouble(item.baseRate));
		workItems.setTotalUnits(Double.parseDouble(item.editQuantity));
		workItems.setTotalAmount(item.amount);
		workItems.setGpsQuantity(Double.parseDouble(item.editQuantity));
		workItems.setItemCode(item.itemnumber);
		workItems.setSrRate(Double.parseDouble(item.baseRate));
		workItems.setSrYear(2013);
		measurementId = baseService.getMeasurementIds(item.itemtypeid);
		workItems.setMeasurementid(measurementId);
		workItems.setItemCode(item.itemnumber);
		workItems.setBlockId(item.blockId);
		workItems.setGroupId(item.getGroupId());
		workItems.setBaseRate(Double.parseDouble(item.baseRate));
		workItems.setBlockName(item.blockName);
		workItems.setFixed(item.fixed);
		workItems.setConstant(item.constant);
		workItems.setConstantValue(item.constantvalue);
		workItems.setFormula(item.formula);
		workItems.setGroupName(item.groupName);
		workItems.setAmountQuantity(item.amountquantity);
		workItems.setWorkItemTypeId(Integer.parseInt(item.itemtypeid));
		workItems.setMeasurmentQuantity(0);
		workItems.setmTotalAmount(0);
		if (!item.decRound.isEmpty()) {
			workItems.setDecRound(Integer.parseInt(item.decRound));
		}
		if (!item.isInt.isEmpty()) {
			workItems.setIsInteger(item.isInt);
		}

		baseService.addToworkItems(workItems);
	}

	private class saveEstimation extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				CreateEstimationActivity.this);
                    
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Saving Estimation....");
			this.dialog.setTitle("Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();
		//	this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String Result = new String();
			savetoLocalDB();
			return Result;
		}

		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			baseService.updateWorkMain(Integer.toString(projectWorkrowId),
					"EST");
			Toast.makeText(CreateEstimationActivity.this,
					"Estimation Saved successfully", Toast.LENGTH_LONG).show();
			copyDatabaseToSdcard();
		}
	}

	private class createEstimation extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				CreateEstimationActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Creating Estimation....");
			this.dialog.setTitle("Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected String doInBackground(final String... arg0) {
			String Result = new String();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					createEstimation(arg0[0]);
				}
			});

			return Result;
		}

		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			totalAmount.setText("Total Amount : " + rs + "."
					+ Integer.toString((int) total));
			btnSignature.setEnabled(true);
			btnSendtoserver.setEnabled(true);
			btnSave.setEnabled(true);
			Toast.makeText(CreateEstimationActivity.this,
					"Estimation Created successfully", Toast.LENGTH_LONG)
					.show();
			listAdapter.notifyDataSetChanged();
		}
	}

	private class updateEstimationToServer extends
			AsyncTask<String, Void, String> {

		ProgressDialog dialog = new ProgressDialog(
				CreateEstimationActivity.this);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Uploading...Please Wait!!");
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			BaseService baseService = new BaseService(
					CreateEstimationActivity.this);
			// StringBuffer buffer = new StringBuffer();
			Element root = new Element("NewDataSet");
			Document doc = new Document(root);
			try {

				doc.setRootElement(root);
				String data = Integer.toString(projectWorkrowId);
				System.out.println(data);
				// buffer.append("<NewDataSet>");
				List<Projectestimates> lstProjEst = baseService
						.getProjectEstimationList(projectWorkrowId);
				List<WorkItems> lstWorkItems = baseService
						.getTableContents(projectWorkrowId);
				List<ProjectEstimationType> lstTypes = baseService
						.getProjectEstimationTypeList(projectWorkrowId);

				Iterator<Projectestimates> itrProjEst = lstProjEst.iterator();
				while (itrProjEst.hasNext()) {
					Projectestimates projEst = itrProjEst.next();
					Element coordinate = new Element("dtCoordinates");
					coordinate.addContent(new Element("WorkRowId")
							.setText(String.valueOf(projEst.getWorkRowId())));
					coordinate
							.addContent(new Element("Estimationid")
									.setText(String.valueOf(projEst
											.getEstimationId())));
					coordinate.addContent(new Element("TypeOfLine")
							.setText(String.valueOf(projEst.getTypeOfLine())));
					coordinate
							.addContent(new Element("CoordinatesId")
									.setText(String.valueOf(projEst
											.getCoordinatesId())));
					coordinate.addContent(new Element("StartLattitude")
							.setText(projEst.getStartLattitude()));
					coordinate.addContent(new Element("StartLangtitude")
							.setText(projEst.getStartLangtitude()));
					coordinate.addContent(new Element("Distance")
							.setText(String.valueOf(projEst.getDistance())));
					coordinate.addContent(new Element("CoordinatesCaption")
							.setText(projEst.getCoordinatesCaption()));
					coordinate.addContent(new Element("NoofCurves")
							.setText(String.valueOf(projEst.getNoofCurves())));
					coordinate.addContent(new Element("IsTaping")
							.setText(projEst.getIsTaping()));
					coordinate.addContent(new Element("ltEstId").setText(Integer.toString(projEst.getLtEstId())));
					coordinate.addContent(new Element("MaterialId").setText(Integer.toString(projEst.getMaterialId())));
					if(projEst.getLayingMethod()==null)
					{
						coordinate.addContent(new Element("LayingMethod").setText("N"));
					}
					else{
					coordinate.addContent(new Element("LayingMethod").setText(projEst.getLayingMethod()));
					}
		
					doc.getRootElement().addContent(coordinate);

				}

				Iterator<WorkItems> itrWorkItems = lstWorkItems.iterator();
				while (itrWorkItems.hasNext()) {
					WorkItems items = itrWorkItems.next();
					Element wItems = new Element("dtEstItems");
					wItems.addContent(new Element("WorkRowId").setText(String.valueOf(items.getWorkRowid())));
					wItems.addContent(new Element("EstimationId")
							.setText(String.valueOf(items.getEstimationId())));
					wItems.addContent(new Element("SubWorkid").setText(String
							.valueOf(items.getSubWorkid())));
					wItems.addContent(new Element("Category").setText(String
							.valueOf(items.getCategory())));
					wItems.addContent(new Element("SubCategory").setText(String
							.valueOf(items.getSubCategory())));
					wItems.addContent(new Element("ComponentId").setText(String
							.valueOf(items.getComponentId())));
					wItems.addContent(new Element("WorkItemTypeId")
							.setText(String.valueOf(items.getWorkItemTypeId())));
					wItems.addContent(new Element("WorkItemDescription")
							.setText(items.getWorkItemDescription()));
					wItems.addContent(new Element("Measurementid")
							.setText(String.valueOf(items.getMeasurementid())));
					wItems.addContent(new Element("TotalUnits").setText(String
							.valueOf(items.getTotalUnits())));
					wItems.addContent(new Element("CostPerItem").setText(String
							.valueOf(items.getCostPerItem())));
					wItems.addContent(new Element("SRRate").setText(String
							.valueOf(items.getSrRate())));
					wItems.addContent(new Element("SRYear").setText(String
							.valueOf(items.getSrYear())));
					wItems.addContent(new Element("BaseRate").setText(String
							.valueOf(items.getBaseRate())));
					wItems.addContent(new Element("TotalAmount").setText(String
							.valueOf(items.getTotalAmount())));
					wItems.addContent(new Element("BlockId").setText(items
							.getBlockId()));
					wItems.addContent(new Element("BlockName").setText(items
							.getBlockName()));
					wItems.addContent(new Element("GroupId").setText(items
							.getGroupId()));
					wItems.addContent(new Element("GroupName").setText(items
							.getGroupName()));
					wItems.addContent(new Element("Fixed").setText(items
							.getFixed()));
					wItems.addContent(new Element("Constant").setText(items
							.getConstant()));
					wItems.addContent(new Element("ConstantValue")
							.setText(items.getConstantValue()));
					wItems.addContent(new Element("Formula").setText(items
							.getFormula()));
					wItems.addContent(new Element("AmountQuantity")
							.setText(items.getAmountQuantity()));
					wItems.addContent(new Element("ItemCode").setText(items
							.getItemCode()));
					wItems.addContent(new Element("DecRound").setText(String
							.valueOf(items.getDecRound())));
					wItems.addContent(new Element("IsInteger").setText(items
							.getIsInteger()));
					wItems.addContent(new Element("IsNegative").setText("N"));
					wItems.addContent(new Element("LineType").setText("0"));
					doc.getRootElement().addContent(wItems);

					

				}

				Iterator<ProjectEstimationType> itrProjEstType = lstTypes
						.iterator();
				while (itrProjEstType.hasNext()) {
					ProjectEstimationType projEstType = itrProjEstType.next();
					Element estType = new Element("dtEstimationType");
					estType.addContent(new Element("ProjectId").setText(String.valueOf(projEstType.getProjectId())));
					estType.addContent(new Element("EstimationId")
							.setText(String.valueOf(projEstType
									.getEstimationId())));
					estType.addContent(new Element("EstDescription")
							.setText(projEstType.getEstDescription()));
					estType.addContent(new Element("LineId").setText(String
							.valueOf(projEstType.getLineId())));
					estType.addContent(new Element("ltEstId").setText(String
							.valueOf(projEstType.getLtEstId())));
					doc.getRootElement().addContent(estType);
					

				}

				/*Element projectInfo = new Element("projectInfo");
				projectInfo.addContent(new Element("spanLength").setText(String.valueOf(spanLength)));
				projectInfo.addContent(new Element("projectCategory").setText(String.valueOf(projectCategory)));
				doc.getRootElement().addContent(projectInfo);*/
				Element projectInfo = new Element("projectInfo");
				projectInfo.addContent(new Element("spanLength").setText(String.valueOf(spanLength)));
				projectInfo.addContent(new Element("projectCategory").setText(String.valueOf(projectCategory)));
				doc.getRootElement().addContent(projectInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			
			try {
				xmlOutput.output(doc,
						openFileOutput("Raghu.xml", Context.MODE_PRIVATE));
				
				copyXmlToSdcard();
			} catch (IOException e) {

				e.printStackTrace();

			}
			String sXml = xmlOutput.outputString(doc);
			System.out.println("+++++++++" + sXml);
			String strSignPath = baseService
					.getSignaturePathFromWorkmain(projectWorkrowId);
			File signPath = new File(strSignPath);
			Uri uri = Uri.fromFile(signPath);
			String bitmapString = null;
			try {
				Bitmap signBitmap = decodeUri(uri);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				signBitmap.compress(CompressFormat.JPEG, 90, bos);
				byte[] value = bos.toByteArray();
				bitmapString = Base64.encodeToString(value, Base64.DEFAULT);

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			strSignature = strSignPath + "@" + bitmapString;
			if (!(bitmapString != null && bitmapString.trim().length() != 0)) {
				strSignature = "1@1";
			}

			Uploadestimation est = new Uploadestimation();
			est.setiWorkRowid(projectWorkrowId);
			est.setSignature(strSignature);
			est.setStrStatus("ESA");
			est.setsXml(sXml);
			String result = null;
			if (Internet.isAvailable(CreateEstimationActivity.this)) {
				try{
				Soapproxy soapproxy = new Soapproxy(
						CreateEstimationActivity.this);
				result = soapproxy.uploadEstimation(est);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					result = "noInternet";
				}
			} else {
				result = "noInternet";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			this.dialog.dismiss();
			GescomUtility.setCountForSpnInEditEst(0);
			if (result.equalsIgnoreCase("success")) {
				BaseService baseService = new BaseService(
						CreateEstimationActivity.this);
				baseService.updateWorkMain(String.valueOf(projectWorkrowId),
						"ESA");
				Toast.makeText(CreateEstimationActivity.this,
						"Estimation Uploaded to Server Successfully",
						Toast.LENGTH_LONG).show();
				Intent userMenuIntent = new Intent(
						CreateEstimationActivity.this, UsermenuActivity.class);
				userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(userMenuIntent);
				finish();
			} else if (result.equalsIgnoreCase("noInternet")||result.equalsIgnoreCase("Failure")) {
				BaseService baseService = new BaseService(CreateEstimationActivity.this);
				baseService.updateWorkMain(String.valueOf(projectWorkrowId),"CCO");
				baseService.deleteProjectDataFromEstimates(String.valueOf(projectWorkrowId));
				Toast.makeText(
						CreateEstimationActivity.this,
						"Data Connection Unavailable.. Try again.",
						Toast.LENGTH_LONG).show();
				Intent userMenuIntent = new Intent(
						CreateEstimationActivity.this, UsermenuActivity.class);
				userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(userMenuIntent);
				finish();
			}
		}

	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);

		final int REQUIRED_SIZE = 100;

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}

	private void copyDatabaseToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//com.zeal.gov.kar.gescom//databases//gescom.db";
				String backupDBPath = "gescom.db";
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
	private void copyXmlToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//com.zeal.gov.kar.gescomtesting//files//Raghu.xml";
				String backupDBPath = "Raghu.xml";
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
			
			e.printStackTrace();
		}
	}
	class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		

		private List<Item> itemlist;
		
		public ExpandableListAdapter(Context context, List<String> listDataHeader) 
		{
			this._context = context;
			this._listDataHeader = listDataHeader;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final String childText = (String) getChild(groupPosition, childPosition);

			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
			View view = infalInflater.inflate(R.layout.exp, null, false);
			TableLayout table = (TableLayout) view.findViewById(R.id.item_table);
			TableRow Header = new TableRow(CreateEstimationActivity.this);
			for (int j = 0; j <= 4; j++) {
				TextView cell = new TextView(CreateEstimationActivity.this) {
					@Override
					protected void onDraw(Canvas canvas) {
						super.onDraw(canvas);
						Rect rect = new Rect();
						Paint paint = new Paint();
						paint.setStyle(Paint.Style.STROKE);
					paint.setColor(Color.WHITE);
						paint.setStrokeWidth(2);
						getLocalVisibleRect(rect);
						canvas.drawRect(rect, paint);
					}
				};
				if (j == 0) {
					cell.setText("ID");
				}
				if (j == 1) {
					cell.setText("DISCRIPTION");
			}
				if (j == 2) {
					cell.setText("COST/ITEM");
				}
				if (j == 3) {
					cell.setText("QUANTITY");
				}
				if (j == 4) {
					cell.setText("VALUE");
				}

			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
				cell.setBackgroundColor(Color.rgb(100, 100, 100));
				Header.addView(cell);
		}
			table.addView(Header);
			
			
			for (Item item : Itemlist) {
           if (item.blockId.equalsIgnoreCase("E"+projectCategory+"B2") && groupPosition==1) {
				
			table.addView(givemerow(item));
            }
            if (item.blockId.equalsIgnoreCase("E"+projectCategory+"B3") && groupPosition==2) {
				
				table.addView(givemerow(item));
                }
            if (item.blockId.equalsIgnoreCase("E"+projectCategory+"B1") && groupPosition==0) {
				
				table.addView(givemerow(item));
              }
		}
			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_group, null);
			}

			TextView lblListHeader = (TextView) convertView
					.findViewById(R.id.simple_expandable_list_item_2_text1);
			lblListHeader.setTypeface(null, Typeface.BOLD);
			lblListHeader.setText(headerTitle);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	
	}
	
	private TableRow givemerow( Item item)
	{
		TableRow row = new TableRow(CreateEstimationActivity.this);
		for (int j = 0; j <= 4; j++) {

			TextView cell = new TextView(CreateEstimationActivity.this) {
				@Override
				protected void onDraw(Canvas canvas) {
					super.onDraw(canvas);
					Rect rect = new Rect();
					Paint paint = new Paint();
					paint.setStyle(Paint.Style.STROKE);
					paint.setColor(Color.WHITE);
					paint.setStrokeWidth(2);
					getLocalVisibleRect(rect);
					canvas.drawRect(rect, paint);
				}

			};
			if (j == 0) {
				cell.setText(item.itemtypeid);
			}
			if (j == 1) {
				cell.setText(item.description);
			}
			if (j == 2) {
				cell.setText(item.baseRate);
			}
			if (j == 3) {
				cell.setText(item.editQuantity);
			}
	   	    if (j == 4) {
				cell.setText(Double.toString(Math.round(item.amount)));
			//total = total + item.amount;
		}

			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
			row.addView(cell);
			
		}	
		return row;
	}
	
}
