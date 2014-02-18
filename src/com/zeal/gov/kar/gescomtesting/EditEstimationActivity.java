package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.GescomUtility;
import com.zeal.gov.kar.gescom.model.Item;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.Uploadestimation;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.model.Workmain;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import com.zeal.gov.kar.gescomtesting.CreateEstimationActivity.ExpandableListAdapter;

public class EditEstimationActivity extends Activity implements OnClickListener {

	Spinner spnEditEstimation;
	TableLayout tlForEditEst;
	List<String> lstWorkDescriptionForEditEst;
	ArrayAdapter<String> arradForspnEditEst;
	TableRow trForEditEst;
	LinearLayout llForSign;
	private TextView totalAmount;
	private String rs="\u20A8";
	private static final int EDITESTIMATION = 2;
	private static final int DIGSIGNATURE = 1;
	private double total;
	Button btnSignature, btnSendToServer, btnCancel;
	String strSelectedDesc,strSignature,strSignatureImage,strSignaturePresent; 
	int workRowId;
	List<WorkItems> lstTableContents;
	// HashMap<String, Double> hmForFormula = new HashMap<String, Double>();
	WorkItems contents;
	HashMap<String, WorkItems> hmForWorkItems;
	HashMap<String, Integer> hmForSpinner;
	ImageView imageView;
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	HashMap<TableRow, WorkItems> hmEditest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editestimation);
		totalAmount=(TextView) findViewById(R.id.totalAmount);
		spnEditEstimation = (Spinner) findViewById(R.id.spnWorkDescForEditEst);
//		tlForEditEst = (TableLayout) findViewById(R.id.tlEditEst);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSendToServer = (Button) findViewById(R.id.btnSendToServer);
		btnSignature = (Button) findViewById(R.id.btnSignature);
		btnCancel.setOnClickListener(this);
		btnSendToServer.setOnClickListener(this);
		btnSignature.setOnClickListener(this);
		btnSignature.setEnabled(false);
		btnSendToServer.setEnabled(false);
		//llForSign = (LinearLayout) findViewById(R.id.llForSign);
		String strSpnDesc = Appuser.getSpnDescForEditEst();
		
		
		spinnerInitialisation();

		if (strSpnDesc != null && strSpnDesc.trim().length() != 0) {
			int pos = hmForSpinner.get(strSpnDesc);
			spnEditEstimation.setSelection(pos);
		}

		spnEditEstimation
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> element,
							View arg1, int pos, long arg3) {

						if (pos > 0) {
							btnSignature.setEnabled(true);
							btnSendToServer.setEnabled(true);
							strSelectedDesc = element.getItemAtPosition(pos)
									.toString();
							Appuser.setSpnDescForEditEst(strSelectedDesc);
//							tlForEditEst.removeAllViews();
//							headerForEditEstimationTable();
							/*GenerateEditEstimationTable table = new GenerateEditEstimationTable();
							table.execute();*/
							// addRowsToEditEstimationTable(lstTableContents);
							BaseService baseService = new BaseService(EditEstimationActivity.this);
							workRowId = baseService.getWorkRowId(strSelectedDesc);

							lstTableContents = baseService.getTableContents(workRowId);
							listDataHeader = new ArrayList<String>();
							listDataHeader.add("HT-Line");
							listDataHeader.add("Transformer");
							listDataHeader.add("LT-Line");
							listAdapter = new ExpandableListAdapter(EditEstimationActivity.this, listDataHeader);
							expListView = (ExpandableListView) findViewById(R.id.lvExp);
							expListView.setAdapter(listAdapter);
							expListView.refreshDrawableState();
							if(hmForWorkItems != null){
								hmForWorkItems.clear();
							}
							total = getTotalEstimationAmount(lstTableContents);
							totalAmount.setText("Total Amount : " + rs + "."
									+ Integer.toString((int) total));

						} else {
							
//							tlForEditEst.removeAllViews();
							btnSignature.setEnabled(false);
							btnSendToServer.setEnabled(false);
						}

					}

					public void onNothingSelected(AdapterView<?> element) {
						
					}
				});
	}

	
	protected double getTotalEstimationAmount(List<WorkItems> lstTableContents2) {
		// TODO Auto-generated method stub
		total = 0.0;
		hmForWorkItems = new HashMap<String, WorkItems>();
		Iterator<WorkItems> itr = lstTableContents2.iterator();
		while(itr.hasNext()){
			WorkItems items = itr.next();
			hmForWorkItems.put(items.getItemCode(), items);
			total=total+items.getTotalAmount();
		}
		
		return total;
	}


	private void spinnerInitialisation() {
		BaseService baseService = new BaseService(EditEstimationActivity.this);
		
		lstWorkDescriptionForEditEst = new ArrayList<String>();
		lstWorkDescriptionForEditEst.add("---select work---");
		lstWorkDescriptionForEditEst.addAll(baseService
				.getProjectforSavecordinate("EST", Appuser.getUserName()));

		arradForspnEditEst = new ArrayAdapter<String>(
				EditEstimationActivity.this,
				android.R.layout.simple_spinner_item,
				lstWorkDescriptionForEditEst);
		spnEditEstimation.setAdapter(arradForspnEditEst);

		hmForSpinner = new HashMap<String, Integer>();
		Iterator<String> itrForSpn = lstWorkDescriptionForEditEst.iterator();
		int count = 0;
		while (itrForSpn.hasNext()) {
			String string = (String) itrForSpn.next();
			hmForSpinner.put(string, count++);
		}
		// spnCreateEstimation.
		if(GescomUtility.getCountForSpnInEditEst()==0){
			GescomUtility.setCountForSpnInEditEst(1);
		if(lstWorkDescriptionForEditEst.size()==1){
			/*AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
			updateDialog.setTitle("Projects");
			updateDialog.setMessage("No projects");
			updateDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			updateDialog.create();
			updateDialog.show();*/
			Toast.makeText(EditEstimationActivity.this, "Currently no projects are Assigned", Toast.LENGTH_LONG).show();
		}
		else{
			/*AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
			updateDialog.setTitle("Projects");
			updateDialog.setMessage("You have "+(lstWorkDescriptionForEditEst.size()-1)+" new projects");
			updateDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			updateDialog.create();
			updateDialog.show();*/
			Toast.makeText(EditEstimationActivity.this, "You have "+(lstWorkDescriptionForEditEst.size()-1)+" new projects", Toast.LENGTH_LONG).show();
		}
		}
	}

	private void headerForEditEstimationTable() {
		TableRow header = new TableRow(this);
		for (int i = 0; i <= 5; i++) {
			TextView cell = new TextView(this) {
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
			if (i == 0) {
				cell.setText("WorkitemId");
			}
			if (i == 1) {
				cell.setText("ItemDesc");
			}
			if (i == 2) {
				cell.setText("CostPerItem");
			}
			if (i == 3) {
				cell.setText("GPSQuantity");
			}
			if (i == 4) {
				cell.setText("ItemQuantity");
			}
			if (i == 5) {
				cell.setText("ItemValue");
			}
		
			cell.setPadding(6, 4, 6, 4);
			cell.setBackgroundColor(Color.rgb(100, 100, 100));
			header.addView(cell);
		}
		tlForEditEst.addView(header);

	}

	protected TableRow addRowsToEditEstimationTable(WorkItems contents) {
		total=0.0;
		System.out.println("<Total>"+total);
		// WorkItems contents;
//		Iterator<WorkItems> itr = lstTableContents.iterator();
//		hmForWorkItems = new HashMap<String, WorkItems>();
//		while (itr.hasNext()) {
//			contents = itr.next();
//			hmForWorkItems.put(contents.getItemCode(), contents);
			trForEditEst = new TableRow(EditEstimationActivity.this);
			for (int i = 0; i <= 5; i++) {
				TextView cell = new TextView(this) {
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
				// double formulaValue = calculateFormulaValue(contents);
				if (i == 0) {
					cell.setText(String.valueOf(contents.getWorkItemTypeId()));
				}
				if (i == 1) {
					cell.setText(contents.getWorkItemDescription());
				}
				if (i == 2) {
					cell.setText(String.valueOf(contents.getBaseRate()));
				}
				if (i == 3) {

					cell.setText(String.valueOf(contents.getGpsQuantity()));
				}
				if (i == 4) {

					cell.setText(String.valueOf(contents.getTotalUnits()));
				}
				if (i == 5) {

					cell.setText(String.valueOf(contents.getTotalAmount()));
					total=total+contents.getTotalAmount();
				}
				
				cell.setPadding(6, 4, 6, 4);
				cell.setTextColor(Color.WHITE);
				trForEditEst.addView(cell);
			}
//			tlForEditEst.addView(trForEditEst);
			hmEditest.put(trForEditEst, contents);
			trForEditEst.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					WorkItems selectedItem = hmEditest.get(v);
					if(selectedItem != null) {
						if(selectedItem.getGroupId().contains("G3")){
							Toast.makeText(EditEstimationActivity.this, "Sorry, you can't edit Tax part", Toast.LENGTH_LONG).show();
						}
						else{
							TableRow row = (TableRow) v;
							TextView workItemTypeId = (TextView) row.getChildAt(0);
							TextView workDesc = (TextView) row.getChildAt(1);
							TextView costperItem = (TextView) row.getChildAt(2);
							TextView itemQuantity = (TextView) row.getChildAt(4);
							TextView itemValue = (TextView) row.getChildAt(5);

							Intent editEstIntent = new Intent(
									EditEstimationActivity.this, EditEstimation.class);
							editEstIntent.putExtra("spnDesc", strSelectedDesc);
							editEstIntent.putExtra("workDesc", workDesc.getText()
									.toString());
							editEstIntent.putExtra("costperItem", costperItem.getText()
									.toString());
							editEstIntent.putExtra("Quantity", itemQuantity.getText()
									.toString());
							editEstIntent.putExtra("Amount", itemValue.getText()
									.toString());
							editEstIntent.putExtra("workItemTypeID", workItemTypeId
									.getText().toString());
							editEstIntent.putExtra("hmForWorkItems", hmForWorkItems);
							editEstIntent.putExtra("workRowId", String.valueOf(workRowId));
							//					spnEditEstimation.setSelection(0);
							startActivityForResult(editEstIntent, EDITESTIMATION);
						}
					}
				}
			});
//		}
			return trForEditEst;

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		
		if(requestCode == DIGSIGNATURE && resultCode == 1){
			String responseFromDigSign = data.getStringExtra("responsefromDigSign");
			if(responseFromDigSign.equalsIgnoreCase("success")){
				/*AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
				updateDialog.setTitle("Message");
				updateDialog.setMessage("Digitally Signed Successfully");
				updateDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				updateDialog.create();
				updateDialog.show();*/
				Toast.makeText(EditEstimationActivity.this, "Digitally Signed Successfully", Toast.LENGTH_LONG).show();

			}}
		else if(requestCode == EDITESTIMATION && resultCode == 1){
			String responseFromEditEst = data.getStringExtra("responseFromEditEst");
			
			String strSpnDesc = Appuser.getSpnDescForEditEst();
			
			total = 0.0;
			if (strSpnDesc != null) {
				int pos = hmForSpinner.get(strSpnDesc);
				spnEditEstimation.setAdapter(arradForspnEditEst);
				spnEditEstimation.setSelection(pos,true);
				
			}

			if(responseFromEditEst.equalsIgnoreCase("success")){
				/*AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
					updateDialog.setTitle("Message");
					updateDialog.setMessage("Updated Successfully");
					updateDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					updateDialog.create();
					updateDialog.show();*/
				Toast.makeText(EditEstimationActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();

			}
		}
		}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSendToServer:
			BaseService baseService = new BaseService(EditEstimationActivity.this);
	       strSignaturePresent = baseService.getSignaturePathFromWorkmain(workRowId);
	       if(!strSignaturePresent.equalsIgnoreCase("not available")){
			updateEstimationToServer toServer = new updateEstimationToServer();
			toServer.execute();
	       }else{
	    	   Toast.makeText(this, "please put a signature", Toast.LENGTH_LONG).show();
	       }
			break;

		case R.id.btnCancel:
			GescomUtility.setCountForSpnInEditEst(0);
			Appuser.setSpnDescForEditEst(null);
			Intent menuIntent = new Intent(EditEstimationActivity.this,UsermenuActivity.class);
			EditEstimationActivity.this.startActivity(menuIntent);
			finish();
			break;
		case R.id.btnSignature:
			Intent digSign = new Intent(EditEstimationActivity.this,DigitalSignatureActivity.class);
			digSign.putExtra("workRowId", String.valueOf(workRowId));
			EditEstimationActivity.this.startActivityForResult(digSign, DIGSIGNATURE);
			break;
		default:
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		GescomUtility.setCountForSpnInEditEst(0);
		Appuser.setSpnDescForEditEst(null);
		Intent menuIntent = new Intent(EditEstimationActivity.this,UsermenuActivity.class);
		EditEstimationActivity.this.startActivity(menuIntent);
		finish();
	}

	private class GenerateEditEstimationTable extends
			AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(EditEstimationActivity.this);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Please Wait!!");
			this.dialog.setCancelable(false);
			this.dialog.show();
			BaseService baseService = new BaseService(
					EditEstimationActivity.this);
			workRowId = baseService.getWorkRowId(strSelectedDesc);

			lstTableContents = baseService.getTableContents(workRowId);

			// addValuesToHashmap(lstTableContents);
			System.out.println("<Total>"+total);
//			addRowsToEditEstimationTable(lstTableContents);
			System.out.println("<Total>"+total);
			totalAmount.setText("Total Amount : "+rs+"."+total);
		}

		@Override
		protected String doInBackground(String... params) {

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			this.dialog.dismiss();
		}

	}

	private class updateEstimationToServer extends
			AsyncTask<String, Void, String> {
		
		ProgressDialog dialog = new ProgressDialog(EditEstimationActivity.this);

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Uploading...Please Wait!!");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			BaseService baseService = new BaseService(
					EditEstimationActivity.this);
//			StringBuffer buffer = new StringBuffer();
			Element root = new Element("NewDataSet");
			Document doc = new Document(root);
			try {
				
				doc.setRootElement(root);
			
//			buffer.append("<NewDataSet>");
			List<Projectestimates> lstProjEst = baseService
					.getProjectEstimationList(workRowId);
			List<WorkItems> lstWorkItems = baseService
					.getTableContents(workRowId);
			List<ProjectEstimationType> lstTypes = baseService
					.getProjectEstimationTypeList(workRowId);

			Iterator<Projectestimates> itrProjEst = lstProjEst.iterator();
			while (itrProjEst.hasNext()) {
				Projectestimates projEst = itrProjEst.next();
				Element coordinate = new Element("dtCoordinates");
				coordinate.addContent(new Element("WorkRowId").setText(String.valueOf(projEst.getWorkRowId())));
				coordinate.addContent(new Element("Estimationid").setText(String.valueOf(projEst.getEstimationId())));
				coordinate.addContent(new Element("TypeOfLine").setText(String.valueOf(projEst.getTypeOfLine())));
				coordinate.addContent(new Element("CoordinatesId").setText(String.valueOf(projEst.getCoordinatesId())));
				coordinate.addContent(new Element("StartLattitude").setText(projEst.getStartLattitude()));
				coordinate.addContent(new Element("StartLangtitude").setText(projEst.getStartLangtitude()));
				coordinate.addContent(new Element("Distance").setText(String.valueOf(projEst.getDistance())));
				coordinate.addContent(new Element("CoordinatesCaption").setText(projEst.getCoordinatesCaption()));
				coordinate.addContent(new Element("NoofCurves").setText(String.valueOf(projEst.getNoofCurves())));
				coordinate.addContent(new Element("IsTaping").setText(projEst.getIsTaping()));
				coordinate.addContent(new Element("ltEstId").setText(String.valueOf(projEst.getLtEstId())));
				
				doc.getRootElement().addContent(coordinate);

				/*buffer.append("<dtCoordinates>");
				buffer.append("<WorkRowId>" + projEst.getWorkRowId()
						+ "</WorkRowId>");
				buffer.append("<Estimationid>" + projEst.getEstimationId()
						+ "</Estimationid>");
				buffer.append("<TypeOfLine>" + projEst.getTypeOfLine()
						+ "</TypeOfLine>");
				buffer.append("<CoordinatesId>" + projEst.getCoordinatesId()
						+ "</CoordinatesId>");
				buffer.append("<StartLattitude>" + projEst.getStartLattitude()
						+ "</StartLattitude>");
				buffer.append("<StartLangtitude>"
						+ projEst.getStartLangtitude() + "</StartLangtitude>");
				buffer.append("<Distance>" + projEst.getDistance()
						+ "</Distance>");
				buffer.append("<CoordinatesCaption>"
						+ projEst.getCoordinatesCaption()
						+ "</CoordinatesCaption>");
				buffer.append("<NoofCurves>" + projEst.getNoofCurves()
						+ "</NoofCurves>");
				buffer.append("<IsTaping>" + projEst.getIsTaping()
						+ "</IsTaping>");
				buffer.append("</dtCoordinates>");*/
			}

			Iterator<WorkItems> itrWorkItems = lstWorkItems.iterator();
			while (itrWorkItems.hasNext()) {
				WorkItems items = itrWorkItems.next();
				Element wItems = new Element("dtEstItems");
				wItems.addContent(new Element("WorkRowId").setText(String.valueOf(items.getWorkRowid())));
				wItems.addContent(new Element("EstimationId").setText(String.valueOf(items.getEstimationId())));
				wItems.addContent(new Element("SubWorkid").setText(String.valueOf(items.getSubWorkid())));
				wItems.addContent(new Element("Category").setText(String.valueOf(items.getCategory())));
				wItems.addContent(new Element("SubCategory").setText(String.valueOf(items.getSubCategory())));
				wItems.addContent(new Element("ComponentId").setText(String.valueOf(items.getComponentId())));
				wItems.addContent(new Element("WorkItemTypeId").setText(String.valueOf(items.getWorkItemTypeId())));
				wItems.addContent(new Element("WorkItemDescription").setText(items.getWorkItemDescription()));
				wItems.addContent(new Element("Measurementid").setText(String.valueOf(items.getMeasurementid())));
				wItems.addContent(new Element("TotalUnits").setText(String.valueOf(items.getTotalUnits())));
				wItems.addContent(new Element("CostPerItem").setText(String.valueOf(items.getCostPerItem())));
				wItems.addContent(new Element("SRRate").setText(String.valueOf(items.getSrRate())));
				wItems.addContent(new Element("SRYear").setText(String.valueOf(items.getSrYear())));
				wItems.addContent(new Element("BaseRate").setText(String.valueOf(items.getBaseRate())));
				wItems.addContent(new Element("TotalAmount").setText(String.valueOf(items.getTotalAmount())));
				wItems.addContent(new Element("BlockId").setText(items.getBlockId()));
				wItems.addContent(new Element("BlockName").setText(items.getBlockName()));
				wItems.addContent(new Element("GroupId").setText(items.getGroupId()));
				wItems.addContent(new Element("GroupName").setText(items.getGroupName()));
				wItems.addContent(new Element("Fixed").setText(items.getFixed()));
				wItems.addContent(new Element("Constant").setText(items.getConstant()));
				wItems.addContent(new Element("ConstantValue").setText(items.getConstantValue()));
				wItems.addContent(new Element("Formula").setText(items.getFormula()));
				wItems.addContent(new Element("AmountQuantity").setText(items.getAmountQuantity()));
				wItems.addContent(new Element("ItemCode").setText(items.getItemCode()));
				wItems.addContent(new Element("DecRound").setText(String.valueOf(items.getDecRound())));
				wItems.addContent(new Element("IsInteger").setText(items.getIsInteger()));
				wItems.addContent(new Element("IsNegative").setText("N"));
				wItems.addContent(new Element("LineType").setText("0"));
				doc.getRootElement().addContent(wItems);
				
				/*buffer.append("<dtEstItems>");
				buffer.append("<WorkRowid>" + items.getWorkRowid()
						+ "</WorkRowid>");
				buffer.append("<EstimationId>" + items.getEstimationId()
						+ "</EstimationId>");
				buffer.append("<SubWorkid>" + items.getSubWorkid()
						+ "</SubWorkid>");
				buffer.append("<Category>" + items.getCategory()
						+ "</Category>");
				buffer.append("<SubCategory>" + items.getSubCategory()
						+ "</SubCategory>");
				buffer.append("<ComponentId>" + items.getComponentId()
						+ "</ComponentId>");
				buffer.append("<WorkItemTypeId>" + items.getWorkItemTypeId()
						+ "</WorkItemTypeId>");
				buffer.append("<WorkItemDescription>"
						+ items.getWorkItemDescription()
						+ "</WorkItemDescription>");
				buffer.append("<Measurementid>" + items.getMeasurementid()
						+ "</Measurementid>");
				buffer.append("<TotalUnits>" + items.getTotalUnits()
						+ "</TotalUnits>");
				buffer.append("<CostPerItem>" + items.getCostPerItem()
						+ "</CostPerItem>");
				buffer.append("<SRRate>" + items.getSrRate() + "</SRRate>");
				buffer.append("<SRYear>" + items.getSrYear() + "</SRYear>");
				buffer.append("<BaseRate>" + items.getBaseRate()
						+ "</BaseRate>");
				buffer.append("<TotalAmount>" + items.getTotalAmount()
						+ "</TotalAmount>");
				buffer.append("<BlockId>" + items.getBlockId() + "</BlockId>");
				buffer.append("<BlockName>" + items.getBlockName()
						+ "</BlockName>");
				buffer.append("<GroupId>" + items.getGroupId() + "</GroupId>");
				buffer.append("<GroupName>" + items.getGroupName()
						+ "</GroupName>");
				buffer.append("<Fixed>" + items.getFixed() + "</Fixed>");
				buffer.append("<Constant>" + items.getConstant()
						+ "</Constant>");
				buffer.append("<ConstantValue>" + items.getConstantValue()
						+ "</ConstantValue>");
				buffer.append("<Formula>" + items.getFormula() + "</Formula>");
				buffer.append("<AmountQuantity>" + items.getAmountQuantity()
						+ "</AmountQuantity>");
				buffer.append("<ItemCode>" + items.getItemCode()
						+ "</ItemCode>");
				buffer.append("<DecRound>" + items.getDecRound()
						+ "</DecRound>");
				buffer.append("<IsInteger>" + items.getIsInteger()
						+ "</IsInteger>");
				buffer.append("<IsNegative>N</IsNegative>");
				buffer.append("<LineType>0</LineType>");
				buffer.append("</dtEstItems>");*/
				

			}

			Iterator<ProjectEstimationType> itrProjEstType = lstTypes
					.iterator();
			while (itrProjEstType.hasNext()) {
				ProjectEstimationType projEstType = itrProjEstType.next();
				Element estType = new Element("dtEstimationType");
				estType.addContent(new Element("ProjectId").setText(String.valueOf(projEstType.getProjectId())));
				estType.addContent(new Element("EstimationId").setText(String.valueOf(projEstType.getEstimationId())));
				estType.addContent(new Element("EstDescription").setText(projEstType.getEstDescription()));
				estType.addContent(new Element("LineId").setText(String.valueOf(projEstType.getLineId())));
				estType.addContent(new Element("ltEstId").setText(String.valueOf(projEstType.getLtEstId())));
				doc.getRootElement().addContent(estType);
				/*buffer.append("<dtEstimationType>");
				buffer.append("<ProjectId>" + projEstType.getProjectId()
						+ "</ProjectId>");
				buffer.append("<EstimationId>" + projEstType.getEstimationId()
						+ "</EstimationId>");
				buffer.append("<EstDescription>"
						+ projEstType.getEstDescription() + "</EstDescription>");
				buffer.append("<LineId>" + projEstType.getLineId()
						+ "</LineId>");
				buffer.append("</dtEstimationType>");*/

			}
			Element projectInfo = new Element("projectInfo");
			Workmain wm=baseService.getWorkmain(String.valueOf(workRowId));
			projectInfo.addContent(new Element("spanLength").setText(String.valueOf(wm.getSpanLength())));
			projectInfo.addContent(new Element("projectCategory").setText(String.valueOf(wm.getProjectCategory())));
			doc.getRootElement().addContent(projectInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}


			XMLOutputter xmlOutput = new XMLOutputter();
			 
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
//			buffer.append("</NewDataSet>");
//			System.out.println("++++++" + buffer);
			try {
				xmlOutput.output(doc, openFileOutput("Raghudg.xml", Context.MODE_PRIVATE));
				/*FileOutputStream fos = openFileOutput("RAGHU", Context.MODE_PRIVATE);
				fos.write(buffer.toString().getBytes());*/
				
			} catch (IOException e) {

				e.printStackTrace();

			}
			String sXml = xmlOutput.outputString(doc);
			System.out.println("+++++++++" + sXml);
			String strSignPath = baseService.getSignaturePathFromWorkmain(workRowId);
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
			strSignature = strSignPath+"@"+bitmapString;
			if (!(bitmapString != null && bitmapString.trim().length() != 0)) {
				strSignature = "1@1";
			}

			Uploadestimation est = new Uploadestimation();
			est.setiWorkRowid(workRowId);
			est.setSignature(strSignature);
			est.setStrStatus("ESA");
			est.setsXml(sXml);
			String result = null ;
			if(Internet.isAvailable(EditEstimationActivity.this)){
				try{
			Soapproxy soapproxy = new Soapproxy(EditEstimationActivity.this);
			result = soapproxy.uploadEstimation(est);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					result = "noInternet";
				}
			}
			else{
				result = "noInternet";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			this.dialog.dismiss();
			GescomUtility.setCountForSpnInEditEst(0);
			Appuser.setSpnDescForEditEst("");
			if(result.equalsIgnoreCase("success")){
				BaseService baseService = new BaseService(EditEstimationActivity.this);
				baseService.updateWorkMain(String.valueOf(workRowId), "ESA");
				Toast.makeText(EditEstimationActivity.this,
						"Estimation Uploaded to Server Successfully", Toast.LENGTH_LONG).show();
				Intent userMenuIntent = new Intent(EditEstimationActivity.this,UsermenuActivity.class);
				userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(userMenuIntent);
				finish();
			}
			else if (result.equalsIgnoreCase("noInternet")) {
				Toast.makeText(EditEstimationActivity.this, "Data Connection Unavailable..Please enable connection and try again",Toast.LENGTH_LONG).show();
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
	
	public static Bitmap decodeBase64(String input) 
	{
		
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
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
			
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

//			final String childText = (String) getChild(groupPosition, childPosition);
//			System.out.println(childText);
			
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/*if(groupPosition == 1){
				
				if(expListView.isGroupExpanded(0)){
					expListView.setSelectedChild(0, childPosition, false);
				}
				if(expListView.isGroupExpanded(2)){
					expListView.setSelectedChild(2, childPosition, false);
				}
			}else if(groupPosition == 2){
				if(expListView.isGroupExpanded(0)){
					expListView.setSelectedChild(0, childPosition, false);
				}
				if(expListView.isGroupExpanded(1)){
					expListView.setSelectedChild(1, childPosition, false);
				}
			}
			else if(groupPosition == 0){
				if(expListView.isGroupExpanded(1)){
					expListView.setSelectedChild(1, childPosition, false);
				}
				if(expListView.isGroupExpanded(2)){
					expListView.setSelectedChild(2, childPosition, false);
				}
			}*/
//			expListView.setSelectedChild(groupPosition, childPosition, shouldExpandGroup)
			View view = infalInflater.inflate(R.layout.exp, null, false);
			TableLayout table = (TableLayout) view.findViewById(R.id.item_table);
			TableRow Header = new TableRow(EditEstimationActivity.this);
			for (int j = 0; j <= 5; j++) {
				TextView cell = new TextView(EditEstimationActivity.this) {
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
					cell.setText("WorkitemId");
				}
				if (j == 1) {
					cell.setText("ItemDesc");
				}
				if (j == 2) {
					cell.setText("CostPerItem");
				}
				if (j == 3) {
					cell.setText("GPSQuantity");
				}
				if (j == 4) {
					cell.setText("ItemQuantity");
				}
				if (j == 5) {
					cell.setText("ItemValue");
				}

			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
				cell.setBackgroundColor(Color.rgb(100, 100, 100));
				Header.addView(cell);
		}
			table.addView(Header);
			if(hmEditest != null ){
				hmEditest.clear();
			}
			hmEditest = new HashMap<TableRow, WorkItems>();
			
			for (WorkItems item : lstTableContents) {
           if (item.getBlockId().equalsIgnoreCase("E4B2") && groupPosition==1) {
				
			table.addView(addRowsToEditEstimationTable(item));
            }
            if (item.getBlockId().equalsIgnoreCase("E4B3") && groupPosition==2) {
				
            	table.addView(addRowsToEditEstimationTable(item));
                }
            if (item.getBlockId().equalsIgnoreCase("E4B1") && groupPosition==0) {
				
            	table.addView(addRowsToEditEstimationTable(item));
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
}
