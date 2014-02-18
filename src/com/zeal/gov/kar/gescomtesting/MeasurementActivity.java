package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.GescomUtility;
import com.zeal.gov.kar.gescom.model.Item;
import com.zeal.gov.kar.gescom.model.MeasurementEstimation;
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.model.Uploadestimation;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;

@SuppressLint({ "DrawAllocation", "NewApi" })
public class MeasurementActivity extends Activity implements OnClickListener {
	Spinner spnMeasurement;
	private LinearLayout  linLayForImage;
	private Point p;
	private ImageView addtoLayout;
	TableLayout tlForEditEst;
	private TextView totalamount;
	private String rs="\u20A8";
	private double total;
	private List<String> lstMeasurement;
	TableRow trForEditEst;
	private ArrayAdapter<String> editMeasurementAdapter;
	String strSelectedDescription,strSignature,strSelectedDesc;
	HashMap<String, WorkItems> hmForWorkItems;
	int workRowId,workItemTypeId,spnPosition;
	String measurementstatus;
	List<WorkItems> lstTableContents;
	WorkItems contents,editingItems;
	Button btnBack;
	HashMap<Integer, Double> hmForFormula;
	List<MeasurementEstimation> lstmeasureest = new ArrayList<MeasurementEstimation>();
	BaseService baseService = new BaseService(this);
	String status;
	HashMap<String, Integer> hmForSpinner;
	HashMap<TableRow, WorkItems> hmMeasurementTable;
	double remainingQuantity;
	private static final int IMAGECODE =100;
	private ArrayList<ApprovedPhotoCoordinates> mainImageList;
	List<TaskDetails> lstWorkRowprg = new ArrayList<TaskDetails>();
	List<TaskDetails> lstWorkRowten = new ArrayList<TaskDetails>();
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	TextView spnForWorkDesc;
	Spinner spnMeasurementUnit;
	TextView tvWorkDesc;
	EditText etQuantity,etCostPerItem,etAmount;
	ArrayAdapter<String> adapForWorkDesc,adapForMeasurementUnit;
	List<String> lstForWorkDesc,lstOfMeasurement;
	String strSpnDesc ;
	Button btnCancel,btnSave,btnMenu,btnDownloadLatestitems;
	HashMap<String, Double> hmForCheckedFormula;
	String strSelectedmeasurement;
	int measurementid;
	String strworkitemtypeid,strWorkRowId;
	Dialog dialog,window;
	CheckBox headerCheckBox,checkBox;
	HashMap<View, TaskDetails> hmOtherWorks;
	List<TaskDetails> lstWorksNoinDb,selectedDownloadList;
	private HashMap<Integer, HashMap<Integer, Double>> measurementQuantityOver;
	
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measurement);
		copyDatabaseToSdcard();

		spnMeasurement = (Spinner)findViewById(R.id.spnMeasurement);
//		btnsend = (Button) findViewById(R.id.btnSend);
//		btnFinalSend = (Button)findViewById(R.id.btnFinalSend);
		btnBack = (Button)findViewById(R.id.btnBack);
		totalamount =(TextView)findViewById(R.id.totalAmount);
//		btnsend.setOnClickListener(this);
//		btnFinalSend.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		//		drawer = (SlidingDrawer)findViewById(R.id.slidingDrawer);
		//		btnMenu = (Button) findViewById(R.id.btnMenu);
		//		btnMenu.setOnClickListener(this);
//		btnDownloadLatestitems = (Button) findViewById(R.id.btndownload);
//		btnDownloadLatestitems.setOnClickListener(this);
		Soapproxy  soapproxy = new Soapproxy(MeasurementActivity.this);
		//		lstmeasureest = soapproxy.getEstimateForMeasurement("1", "326");
		//	baseService.updateWorkMain("412","PRG");

		/*if(Internet.isAvailable(MeasurementActivity.this)){
			InitializeSpinner spn = new InitializeSpinner();
			spn.execute();
		}
		else{*/
			lstMeasurement = new ArrayList<String>();
			lstMeasurement.add("---select work---");
			hmForSpinner = new HashMap<String, Integer>();
			lstMeasurement.addAll(baseService.getProjectforSavecordinate("PRG",Appuser.getUserName()));
			editMeasurementAdapter = new ArrayAdapter<String>(
					MeasurementActivity.this,
					android.R.layout.simple_spinner_item,
					lstMeasurement);
			spnMeasurement.setAdapter(editMeasurementAdapter);

			// spnCreateEstimation.
			Iterator<String> itrForSpn = lstMeasurement.iterator();
			int count = 0;
			while (itrForSpn.hasNext()) {
				String string = (String) itrForSpn.next();
				hmForSpinner.put(string, count++);
			}
			/*Toast.makeText(MeasurementActivity.this, "Data Connection unAvailable,latest projects may not be downloaded.", Toast.LENGTH_LONG).show();
		}*/

		mainImageList=new ArrayList<ApprovedPhotoCoordinates>();

		/*btnImageCapture=(Button) findViewById(R.id.btnImageCapture);
		btnImageCapture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent imageIntent = new Intent(MeasurementActivity.this, CameraView.class);
				//				String projectID=Integer.toString(projectData.get(spinProject.getSelectedItem().toString()));
				imageIntent.putExtra("Projectid",String.valueOf(workRowId));
				imageIntent.putExtra("workstatus","AC");
				startActivityForResult(imageIntent,IMAGECODE);
			}
		});

		btnViewImages=(Button) findViewById(R.id.btnImages);
		btnViewImages.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popUpwindow();
			}
		});*/

		spnMeasurement.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {

				if (pos > 0) {
					spnPosition = pos;
//					btnImageCapture.setEnabled(true);
//					btnsend.setEnabled(true);
//					btnViewImages.setEnabled(true);
					strSelectedDesc = element.getItemAtPosition(pos)
							.toString();

					BaseService bService = new BaseService(MeasurementActivity.this);

					strSelectedDescription = element.getItemAtPosition(pos).toString();

					workRowId = baseService.getWorkRowId(strSelectedDescription);
					measurementQuantityOver = GescomUtility.getHmForMeasurementQuantityOver();
					if(measurementQuantityOver != null){
						if(measurementQuantityOver.containsKey(workRowId)){
							hmForFormula = measurementQuantityOver.get(workRowId);
						}else{
							hmForFormula = new HashMap<Integer, Double>();
						}
					}else{
						measurementQuantityOver = new HashMap<Integer, HashMap<Integer,Double>>();
					}
//					hmForFormula = GescomUtility.getHmQuantityOver();
					if(hmForFormula == null){
						hmForFormula = new HashMap<Integer, Double>();
					}
					/*if(!GescomUtility.isMeasurementUpdateToZero()){
						GescomUtility.setMeasurementUpdateToZero(true);
						bService.updateMeasurementQuantityTozeroForFirstTime(Appuser.getUserName(),"PRG",workRowId);
					}*/
					Appuser.setSpnDescForEditEst(strSelectedDescription);
					lstTableContents=new ArrayList<WorkItems>();
					lstTableContents = bService
							.getTableContent(workRowId);
					mainImageList.clear();
					projectPhotos();
					//					baseService.updateWorkMain(String.valueOf(workRowId),"SMV");
					measurementprogress measurement = new measurementprogress();
					measurement.execute();


				}
				/*else{
					tlForEditEst.removeAllViews();
				}*/
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
//		btnImageCapture.setEnabled(false);
//		btnsend.setEnabled(false);
//		btnViewImages.setEnabled(false);

	}
	public void projectPhotos(){
		BaseService baseService = new BaseService(this);
		if (baseService.isProjectphotosAvailable(String.valueOf(workRowId))) {
			List<ApprovedPhotoCoordinates> prePhotos = baseService
					.getProgressImages(
							String.valueOf(workRowId),
							"AC");
			Iterator<ApprovedPhotoCoordinates> itImage = prePhotos.iterator();
			while (itImage.hasNext()) {
				ApprovedPhotoCoordinates preImage = itImage.next();
				try {
					File imagePath = new File(preImage.getFileName());
					if (imagePath.exists()) {
						Bitmap bitmap = BitmapFactory.decodeFile(imagePath
								.getAbsolutePath());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						byte[] b = baos.toByteArray();
						String imageData = Base64.encodeToString(b,
								Base64.DEFAULT);
						preImage.setImageData(imageData);
					} else {
						Toast.makeText(MeasurementActivity.this,
								"Some images are missing!!!", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				mainImageList.add(preImage);
			}
		}
	}

	protected double getTotalEstimationAmount(List<WorkItems> lstTableContents2) {
		// TODO Auto-generated method stub
		total = 0.0;
		hmForWorkItems = new HashMap<String, WorkItems>();
		Iterator<WorkItems> itr = lstTableContents2.iterator();
		while(itr.hasNext()){
			WorkItems items = itr.next();
			hmForWorkItems.put(items.getItemCode(), items);
			total=total+items.getmTotalAmount();
		}

		return total;
	}
	private class InitializeSpinner extends AsyncTask<String, Void, String>{

		private final ProgressDialog dialog = new ProgressDialog(
				MeasurementActivity.this);
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Please Wait!!");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = new String();

			int countForSpn = GescomUtility.getCountForSpn();
			Soapproxy proxy = new Soapproxy(MeasurementActivity.this);
//			if(countForSpn!=1){
				GescomUtility.setCountForSpn(1);
				lstWorkRowten = proxy.getWorksStatus(Appuser.getUserName(), "TEN");
				lstWorkRowprg = proxy.getWorksStatus(Appuser.getUserName(), "PRG");
//			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			spinnerInitialisation();
			setSpinnerAfterMeasurementEdit();
			this.dialog.dismiss();

		}

	}

	public void onBackPressed()
	{
		super.onBackPressed();
		GescomUtility.setCountForSpnSelected(0);
		Appuser.setSpnDescForEditEst("");
/*		if(GescomUtility.getHmQuantityOver()!=null){
			GescomUtility.getHmQuantityOver().clear();
		}
*/		Intent intent = new Intent(MeasurementActivity.this,UsermenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == IMAGECODE && resultCode == RESULT_OK){
			//			@SuppressWarnings("unchecked")
			ArrayList<ApprovedPhotoCoordinates> datas=(ArrayList<ApprovedPhotoCoordinates>) data.getSerializableExtra("ImageList");
			Iterator<ApprovedPhotoCoordinates> itImage=datas.iterator();
			while(itImage.hasNext())
			{
				mainImageList.add(itImage.next());
			}
			/*if(datas.size()>0)
			{
				UploadImage imageUploader=new UploadImage();
				imageUploader.execute("");
			}*/
			System.out.println(datas.size());
		}
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


	private void spinnerInitialisation() {
		lstMeasurement = new ArrayList<String>();
		lstMeasurement.add("---select work---");
		Soapproxy proxy = new Soapproxy(getApplicationContext());


		System.out.println(lstWorkRowprg);
		System.out.println(lstWorkRowten);
		hmForSpinner = new HashMap<String, Integer>();
		//		baseService.updateworkstatus(413,"PRG");


		lstWorksNoinDb = new ArrayList<TaskDetails>();

		Iterator<TaskDetails> itr = lstWorkRowprg.iterator();
		while(itr.hasNext()){
			TaskDetails workRowId = itr.next();
			long res = baseService.updateworkstatus(workRowId.getWorkRowId(),"PRG");
			if(res<1){
				lstWorksNoinDb.add(workRowId);
			}

		} 
		Iterator<TaskDetails> itr1 = lstWorkRowten.iterator();
		while(itr1.hasNext()){
			TaskDetails workRowId = itr.next();
			long res = baseService.updateworkstatus(workRowId.getWorkRowId(), "PRG");
			if(res<1){
				lstWorksNoinDb.add(workRowId);
			}
		}

		//	lstMeasurement.addAll(((proxy.getWorkStatus("je_engg", "ten")));
		lstMeasurement.addAll(baseService.getProjectforSavecordinate("PRG",Appuser.getUserName()));
		editMeasurementAdapter = new ArrayAdapter<String>(
				MeasurementActivity.this,
				android.R.layout.simple_spinner_item,
				lstMeasurement);
		spnMeasurement.setAdapter(editMeasurementAdapter);

		// spnCreateEstimation.
		Iterator<String> itrForSpn = lstMeasurement.iterator();
		int count = 0;
		while (itrForSpn.hasNext()) {
			String string = (String) itrForSpn.next();
			hmForSpinner.put(string, count++);
		}
		
		if(lstWorksNoinDb.size() > 0){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Works");
			alert.setMessage("Some of the works are not present in local database, do you want to view it for synchronization?");
			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					showWorksNotInDb(lstWorksNoinDb);
				}
			});
			
			alert.setNegativeButton("No Later",  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			alert.create();
			alert.show();
			
		}
		else
		{
			Toast.makeText(MeasurementActivity.this, "No works are found to import dude!!", Toast.LENGTH_LONG).show();
		}
	}


	protected void showWorksNotInDb(List<TaskDetails> ls) {
		// TODO Auto-generated method stub
		hmOtherWorks = new HashMap<View, TaskDetails>();
		window = new Dialog(MeasurementActivity.this);
		LayoutInflater inflater = getLayoutInflater();
		window.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view1 = inflater.inflate(R.layout.custom_table, null);
		window.addContentView(view1,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(window.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.getWindow().setAttributes(lp);
		LinearLayout layout = (LinearLayout) window.findViewById(R.id.LtTablelayout);

		//Header for the table 
		View headerView = inflater.inflate(R.layout.custom_table_row, null);
		headerCheckBox = (CheckBox) headerView.findViewById(R.id.checkBox1);
		TextView headerDesc = (TextView) headerView.findViewById(R.id.tvDesc);
		TextView headerWorkId = (TextView) headerView.findViewById(R.id.tvWorkid);
		Button cancel = (Button) view1.findViewById(R.id.btnCancel);
		Button download = (Button) view1.findViewById(R.id.btnDownload);
		TextView headerMoreInfo = (TextView) headerView.findViewById(R.id.tvmoreinfo);
		headerDesc.setBackgroundColor(Color.rgb(136, 54, 250));
		headerMoreInfo.setBackgroundColor(Color.rgb(136, 54, 250));
		headerWorkId.setBackgroundColor(Color.rgb(136, 54, 250));
		headerCheckBox.setBackgroundColor(Color.rgb(136, 54, 250));
		headerDesc.setText("Work Description");
		headerDesc.setTextAppearance(MeasurementActivity.this, R.style.boldText);
		headerWorkId.setText("Work Id");
		headerWorkId.setTextAppearance(MeasurementActivity.this, R.style.boldText);
		headerMoreInfo.setText("");
		layout.addView(headerView,0);
		headerCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(headerCheckBox.isChecked()){
					checkAllFromList();
				}else{
					unCheckAllFromList();
				}
			}
		});
		
		
		
		
		//Here  starts the child tables 
		for(int i = 0 ;i < ls.size() ; i++){
			View view = inflater.inflate(R.layout.custom_table_row, null);
			checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			final TextView desc = (TextView) view.findViewById(R.id.tvDesc);
			TextView workId = (TextView) view.findViewById(R.id.tvWorkid);
			TextView moreInfo = (TextView) view.findViewById(R.id.tvmoreinfo);
			desc.setText(ls.get(i).getWorkDescription());
			workId.setText(""+ls.get(i).getWorkRowId());
			checkBox.setChecked(false);
			layout.addView(view);
			hmOtherWorks.put(view, ls.get(i));
			
			moreInfo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MeasurementActivity.this, desc.getText().toString(), Toast.LENGTH_LONG).show();
				}
			});
		}//end of child table adding 
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				window.dismiss();
			}
		});
		
		
		download.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<TaskDetails> selectedList = getCheckedWorks();
				System.out.println(selectedList);
				
				if(selectedList != null){
					if(selectedList.size()>0){
						selectedDownloadList = selectedList;
						System.out.println(selectedList);
						window.dismiss();
						new DownloadDataNotPresentInDb().execute();
					}
					else{
						Toast.makeText(MeasurementActivity.this, "Please check the project before clicking on the download button", Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(MeasurementActivity.this, "Please check the project before clicking on the download button", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		window.show();
	}
	
	private class DownloadDataNotPresentInDb extends AsyncTask<String, Void, Boolean>{
		private final ProgressDialog dialog = new ProgressDialog(
				MeasurementActivity.this);
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Downloading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean backRes = false;
			Soapproxy soapproxy = new Soapproxy(MeasurementActivity.this);
			Parser parser = new Parser(MeasurementActivity.this);
			Iterator<TaskDetails> itr = selectedDownloadList.iterator();
			while(itr.hasNext()){
				TaskDetails workId = itr.next();
				String result = soapproxy.getSurveyCoordinates(workId.getWorkRowId());
				if(result.equals("ServerTimeOut") || result.equals("noInternet")){
					backRes = true;
				}else{
					HashMap<String,Object> hm = parser.parseResponseForWorkNotPresentInDb(result);
				}
			}
			return backRes;
		}

		@Override
		protected void onPostExecute(Boolean result) { 
			super.onPostExecute(result);
			this.dialog.dismiss();
			Iterator<TaskDetails> itr = selectedDownloadList.iterator();
			if(result){
				Toast.makeText(MeasurementActivity.this, "Unable to process some of your request please try again later", Toast.LENGTH_LONG).show();
			}
			if(baseService == null ){
				baseService = new BaseService(MeasurementActivity.this);
			}
			while(itr.hasNext()){
				TaskDetails workId = itr.next();
				baseService.updateWorkMain(String.valueOf(workId.getWorkRowId()), "PRG");
			}
			lstMeasurement.clear();
			lstMeasurement.add("---select work---");
			lstMeasurement.addAll(baseService.getProjectforSavecordinate("PRG",Appuser.getUserName()));
			editMeasurementAdapter = new ArrayAdapter<String>(
					MeasurementActivity.this,
					android.R.layout.simple_spinner_item,
					lstMeasurement);
			spnMeasurement.setAdapter(editMeasurementAdapter);

			// spnCreateEstimation.
			Iterator<String> itrForSpn = lstMeasurement.iterator();
			int count = 0;
			while (itrForSpn.hasNext()) {
				String string = (String) itrForSpn.next();
				hmForSpinner.put(string, count++);
			}
		}
	}
	
	protected List<TaskDetails> getCheckedWorks() {
		// TODO Auto-generated method stub
		List<TaskDetails> checkedWorks = new ArrayList<TaskDetails>();;
		Iterator<Entry<View, TaskDetails>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, TaskDetails> entry = iterator.next();
			View view = entry.getKey();
			TaskDetails workid = entry.getValue();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			if(checkBox.isChecked()){
				checkedWorks.add(workid);
			}
		}
		return checkedWorks;
	}
	protected void checkAllFromList() {
		// TODO Auto-generated method stub
		Iterator<Entry<View, TaskDetails>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, TaskDetails> entry = iterator.next();
			View view = entry.getKey();
			TaskDetails items = entry.getValue();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			checkBox.setChecked(true);
		}
	}
	
	protected void unCheckAllFromList() {
		// TODO Auto-generated method stub
		Iterator<Entry<View, TaskDetails>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, TaskDetails> entry = iterator.next();
			View view = entry.getKey();
			TaskDetails items = entry.getValue();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			checkBox.setChecked(false);
		}
	}
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.btnSend:
			measurementstatus = "SMV";
			Updatemeasurement updatemeasure = new Updatemeasurement();
			updatemeasure.execute();

			break;*/
	/*	case R.id.btnFinalSend:
			measurementstatus = "AME";
			Updatemeasurement updatemeasurement = new  Updatemeasurement();
			updatemeasurement.execute();
			break;*/
			
		case R.id.btnBack:
			GescomUtility.setCountForSpnSelected(0);
			Appuser.setSpnDescForEditEst("");
//			if(GescomUtility.getHmQuantityOver()!=null){
//				GescomUtility.getHmQuantityOver().clear();}
			Intent intent = new Intent(MeasurementActivity.this,UsermenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
		/*	
		case R.id.btndownload:
			if(spnMeasurement.getSelectedItemPosition()>0){
				new DownloadLatestItemQuantities().execute();
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;*/
		default:
			break;
		}

	}

	private class DownloadLatestItemQuantities extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				MeasurementActivity.this);
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Downloading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Soapproxy soapproxy = new Soapproxy(getApplicationContext());
			lstmeasureest = soapproxy.getEstimateForMeasurement("1", String.valueOf(workRowId));
			int c = GescomUtility.getCountForSpnSelected();
			Iterator<MeasurementEstimation> itr = lstmeasureest.iterator();
//			lstTableContents=new ArrayList<WorkItems>();
//			List<WorkItems> prevValues = new ArrayList<WorkItems>();
			if(baseService == null){
			baseService = new BaseService(MeasurementActivity.this);
			}
			/*prevValues = baseService
					.getTableContent(workRowId);
			*/
			while(itr.hasNext()){
				MeasurementEstimation measureEstimation = itr.next();
				baseService.updateworkItemsForNewMeasurements(measureEstimation,measureEstimation.getWorkItemTypeId(),workRowId);
			}
			/*lstTableContents = baseService
					.getTableContent(workRowId);
			for(WorkItems items:lstTableContents){
				hmForFormula.put(items.getWorkItemTypeId(), (items.getMeasurmentQuantity() - getgetMeasurmentQuantityForThisWorkitems(prevValues,items.getWorkItemTypeId())));
			}
			measurementQuantityOver.put(workRowId, hmForFormula);*/
			return "";
		}

		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			this.dialog.dismiss();
			spnMeasurement.setAdapter(editMeasurementAdapter);
			spnMeasurement.setSelection(spnPosition,true);
		}

	}

	private class Updatemeasurement extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				MeasurementActivity.this);
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Please Wait!!");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}


		@Override
		protected String doInBackground(String... params) {
			String result = "";

			StringBuffer buffer = new StringBuffer();
			Element root = new Element("Measurments");
			Soapproxy soapproxy = new Soapproxy(MeasurementActivity.this);
			try {


				//				buffer.append("<NewDataSet>");
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				List<WorkItems> lstWorkItems = baseService
						.getTableContent(workRowId);
				if (!(strSignature != null && strSignature.trim().length() != 0)) {
					strSignature = "1@1";
				}
				Iterator<WorkItems> itrWorkItems = lstWorkItems.iterator();
				while (itrWorkItems.hasNext()) {
					Document document = new Document();
					Element wItems = new Element("Measurments");
					//					Document doc = new Document(wItems);
					//					doc.setRootElement(wItems);
					WorkItems items = itrWorkItems.next();

					wItems.addContent(new Element("WorkRowId").setText(String.valueOf(items.getWorkRowid())));
					wItems.addContent(new Element("EstimationId").setText(String.valueOf(items.getEstimationId())));
					wItems.addContent(new Element("SubWorkId").setText(String.valueOf(items.getSubWorkid())));
					//	wItems.addContent(new Element("Category").setText(String.valueOf(items.getCategory())));
					//	wItems.addContent(new Element("SubCategory").setText(String.valueOf(items.getSubCategory())));
					//	wItems.addContent(new Element("ComponentId").setText(String.valueOf(items.getComponentId())));
					wItems.addContent(new Element("workitemtypeid").setText(String.valueOf(items.getWorkItemTypeId())));
					//	wItems.addContent(new Element("WorkItemDescription").setText(items.getWorkItemDescription()));
					wItems.addContent(new Element("MeasurementId").setText(String.valueOf(items.getMeasurementid())));
					wItems.addContent(new Element("MeasurmentQuantity").setText(String.valueOf(items.getMeasurmentQuantity())));
					//	wItems.addContent(new Element("TotalUnits").setText(String.valueOf(items.getTotalUnits())));
					//	wItems.addContent(new Element("CostPerItem").setText(String.valueOf(items.getCostPerItem())));
					//	wItems.addContent(new Element("SRRate").setText(String.valueOf(items.getSrRate())));
					//	wItems.addContent(new Element("SRYear").setText(String.valueOf(items.getSrYear())));
					//	wItems.addContent(new Element("BaseRate").setText(String.valueOf(items.getBaseRate())));
					wItems.addContent(new Element("MTotalAmount").setText(String.valueOf(items.getmTotalAmount())));
					//	wItems.addContent(new Element("BlockId").setText(items.getBlockId()));
					//	wItems.addContent(new Element("BlockName").setText(items.getBlockName()));
					//	wItems.addContent(new Element("GroupId").setText(items.getGroupId()));
					//	wItems.addContent(new Element("GroupName").setText(items.getGroupName()));
					//	wItems.addContent(new Element("Fixed").setText(items.getFixed()));
					//	wItems.addContent(new Element("Constant").setText(items.getConstant()));
					//	wItems.addContent(new Element("ConstantValue").setText(items.getConstantValue()));
					//	wItems.addContent(new Element("Formula").setText(items.getFormula()));
					//	wItems.addContent(new Element("AmountQuantity").setText(items.getAmountQuantity()));
					//	wItems.addContent(new Element("ItemCode").setText(items.getItemCode()));
					//	wItems.addContent(new Element("DecRound").setText(String.valueOf(items.getDecRound())));
					//	wItems.addContent(new Element("IsInteger").setText(items.getIsInteger()));
					//	wItems.addContent(new Element("IsNegative").setText("N"));
					//	wItems.addContent(new Element("LineType").setText("0"));
					//					doc.getRootElement().addContent(wItems);
					//					String sXml = xmlOutput.outputString(doc);
					String sXml = xmlOutput.outputString(wItems);
					System.out.println("@@@@@@@" + sXml);
					Uploadestimation measure = new Uploadestimation();

					measure.setiWorkRowid(workRowId);
					measure.setSignature(strSignature);
//					document.addContent(wItems);
					// display nice nice
					
					/*if(remainingQuantity==0.0){
						measure.setStrStatus("AME");
					}
					else{
						measure.setStrStatus("SMV");
					}
					 */
					/*xmlOutput.setFormat(Format.getPrettyFormat());
					//					buffer.append("</NewDataSet>");
					//					System.out.println("++++++" + buffer);
					try {
						xmlOutput.output(document, openFileOutput("measurement.xml", Context.MODE_PRIVATE));
						FileOutputStream fos = openFileOutput("RAGHU", Context.MODE_PRIVATE);
						fos.write(buffer.toString().getBytes());

					} catch (IOException e) {

						e.printStackTrace();

					}*/

					measure.setStrStatus(measurementstatus);
					measure.setsXml(sXml);

					result = soapproxy.uploadEstimation(measure);
				}
				
//				copyXML();
				if(measurementstatus.equalsIgnoreCase("AME")){
					Iterator<ApprovedPhotoCoordinates> itr = mainImageList.iterator();
					while(itr.hasNext()){
						ApprovedPhotoCoordinates photoCoordinates = itr.next();
						/*if(remainingQuantity == 0.0){
						photoCoordinates.setWorkStarus("AC");
					}
					else{
						photoCoordinates.setWorkStarus("IP");
					}*/
						soapproxy.uploadWorkImages(photoCoordinates);
					}
				}

				// display nice nice
				//			buffer.append("</NewDataSet>");
				//			System.out.println("++++++" + buffer);
				/*try {
						xmlOutput.output(doc, openFileOutput("Raghu.xml", Context.MODE_PRIVATE));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}*/


			}catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			this.dialog.dismiss();
			GescomUtility.setCountForSpnSelected(0);
//			if(GescomUtility.getHmQuantityOver()!=null){
//				GescomUtility.getHmQuantityOver().clear();}
			Appuser.setSpnDescForEditEst("");
			if(result.equalsIgnoreCase("success") && measurementstatus.equalsIgnoreCase("AME")){
				baseService.updateworkstatus(workRowId,"AME");
				Toast.makeText(getApplicationContext(), "Final Measurement successfully uploaded", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MeasurementActivity.this, UsermenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(getApplicationContext(), "Internet not available Please Try Again",Toast.LENGTH_LONG);
			}
			if(result.equalsIgnoreCase("success") && measurementstatus.equalsIgnoreCase("SMV")){
				//				baseService.updateworkstatus(workRowId,"SMV");
				Toast.makeText(getApplicationContext(), "Field Measurement details uploaded successfully to server", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MeasurementActivity.this, UsermenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(getApplicationContext(), "Internet not available Please Try Again",Toast.LENGTH_LONG);
			} 
		}
	}
	private class measurementprogress extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				MeasurementActivity.this);
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			this.dialog.setTitle("");
			this.dialog.setMessage("Please Wait!!");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String str="";

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					//	tlForEditEst.removeAllViews();
					listDataHeader = new ArrayList<String>();
					listDataHeader.add("HT-Line");
					listDataHeader.add("Transformer");
					listDataHeader.add("LT-Line");
					listAdapter =  new ExpandableListAdapter(MeasurementActivity.this, listDataHeader);
					expListView = (ExpandableListView) findViewById(R.id.lvExp);
					expListView.setAdapter(listAdapter);
					expListView.refreshDrawableState();
					total = getTotalEstimationAmount(lstTableContents);
					totalamount.setText("Total Amount : " + rs + "."
							+ Integer.toString((int) total));


				}
			});
			return str;
		}
		@Override
		protected void onPostExecute(String result) {  
			super.onPostExecute(result);
			this.dialog.dismiss();


		}
	}

	private void popUpwindow()
	{
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View input = inflater.inflate(R.layout.popup, null);
		int OFFSET_X = 20;
		int OFFSET_Y = 20;

		final PopupWindow pw = new PopupWindow(input, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		linLayForImage = (LinearLayout)input.findViewById(R.id.linLayForImage);

		Iterator<ApprovedPhotoCoordinates> itImage=mainImageList.iterator();
		while(itImage.hasNext())
		{

			ApprovedPhotoCoordinates imageDetails = itImage.next();
			byte[] decodedString = Base64.decode(imageDetails.getImageData(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 

			addtoLayout = new ImageView(getApplicationContext());
			addtoLayout.setPadding(02, 05, 02, 05);
			addtoLayout.setImageBitmap(decodedByte);
			linLayForImage.addView(addtoLayout);
		}


		pw.showAtLocation(input, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

		Button cancelButton = (Button) input.findViewById(R.id.dismiss);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}
		}) ;

	}
	public void onWindowFocusChanged(boolean hasFocus) {

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
//		btnImageCapture.getLocationOnScreen(location);

		//Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];
	}

	public void setSpinnerAfterMeasurementEdit(){
		Intent intentFromMeasurement = getIntent();
		String responseFromMeasurement = intentFromMeasurement.getStringExtra("responsefrommeasurement");
		String strwTypeId = intentFromMeasurement.getStringExtra("workAItemTypeId");
		if(strwTypeId!=null){

			workItemTypeId =  Integer.parseInt(strwTypeId);
		}
		String strSpnDesc = Appuser.getSpnDescForEditEst();
		if(responseFromMeasurement!=null){
			if(responseFromMeasurement.equalsIgnoreCase("success")){

				Toast.makeText(MeasurementActivity.this, "Measurement details Updated successfully", Toast.LENGTH_LONG).show();

			}}

		if (strSpnDesc != null && strSpnDesc.trim().length() != 0) {
			int pos = hmForSpinner.get(strSpnDesc);
			spnMeasurement.setSelection(pos);
		}
	}
	protected TableRow addRowsToEditEstimationTable(
			WorkItems contents) {
		trForEditEst = new TableRow(MeasurementActivity.this);
		for (int i = 0; i <= 4; i++) {
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
				cell.setText(contents.getWorkItemDescription());
			}
			if (i == 1) {
				cell.setText(String.valueOf(contents.getTotalUnits()));
			}
			if (i == 2) {
				cell.setText(String.valueOf(contents.getMeasurmentQuantity()));
			}

			if(i == 3){
				/*
						HashMap<Integer, Double> hm = GescomUtility.getHmQuantityOver();
						if(hm!=null){
							if(hm.containsKey(contents.getWorkItemTypeId())){
								cell.setText(String.valueOf(contents.getMeasurmentQuantity()));
							}
							else{
								cell.setText("0.0");
							}
						}
						else{
							cell.setText("0.0");
						}

			 */
				if(hmForFormula != null){
					if(hmForFormula.containsKey(contents.getWorkItemTypeId())){
						cell.setText(Double.toString(hmForFormula.get(contents.getWorkItemTypeId())));
					}
					else{
						cell.setText("0.0");
					}
				}else{
					cell.setText("0.0");
				}
			}

			if (i == 4) {
				cell.setText(String.valueOf(contents.getTotalUnits()-contents.getMeasurmentQuantity()));
				remainingQuantity = remainingQuantity+(contents.getTotalUnits()-contents.getMeasurmentQuantity());
			}


			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
			//				cell.setBackgroundColor(Color.rgb(100, 100, 100));
			trForEditEst.addView(cell);
		}
		hmMeasurementTable.put(trForEditEst, contents);
		//				tlForEditEst.addView(trForEditEst);
		trForEditEst.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("RemainingQty", ""+remainingQuantity);
				TableRow row = (TableRow) v;
				WorkItems items = hmMeasurementTable.get(row);
				if(items != null){
					if(items.getGroupId().contains("G3")){
						Toast.makeText(MeasurementActivity.this, "Sorry, you can't edit Tax part", Toast.LENGTH_LONG).show();
					}else{
						showEditMeasurementPopup(items,row);
					}
				}

			}

		});
		return trForEditEst;

	}

	@SuppressWarnings("unchecked")
	private void showEditMeasurementPopup(final WorkItems items, TableRow row){
		final List<Integer> measurementIdsForDecimal =  new ArrayList<Integer>(Arrays.asList(2,5,6,7,8,9,10,12,13,15,16,24));
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.activity_editmeasurement, null);
		dialog = new Dialog(MeasurementActivity.this);
		dialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		dialog.setTitle("Edit Measurement");
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		editingItems = items;
		spnForWorkDesc = (TextView) view.findViewById(R.id.spnForWorkDesc);
		spnMeasurementUnit = (Spinner) view.findViewById(R.id.spnMeasurementUnits);
		tvWorkDesc = (TextView) view.findViewById(R.id.tvWorkDesc);

		etQuantity = (EditText) view.findViewById(R.id.etQuantity);
		etCostPerItem = (EditText) view.findViewById(R.id.etCostPerItem);
		etAmount = (EditText) view.findViewById(R.id.etAmount);

		strworkitemtypeid = String.valueOf(items.getWorkItemTypeId());

		String strWorkDesc = items.getWorkItemDescription();
		final double strQuantity = items.getMeasurmentQuantity();
		double strestimatedQuantity = items.getTotalUnits();
		double  strRemainingQuantity = items.getTotalUnits()-items.getMeasurmentQuantity();

		BaseService baseService = new BaseService(getApplicationContext());
		int measurementdescription = baseService.getmeasurementID(strworkitemtypeid,String.valueOf(workRowId));

		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnSave = (Button) view.findViewById(R.id.btnSave);

		spnForWorkDesc.setText(spnMeasurement.getSelectedItem().toString());
		tvWorkDesc.setText(strWorkDesc);
		etQuantity.setText(String.valueOf(strQuantity));
		etCostPerItem.setText(String.valueOf(strestimatedQuantity));
		etAmount.setText(String.valueOf((strRemainingQuantity)));
		//			spinnerInitianlization();
		lstOfMeasurement = new ArrayList<String>();
		lstOfMeasurement.add("-select-");
		HashMap<String, Object> hmForMUnits = baseService.getMeasurementUnit();
		
		lstOfMeasurement.addAll((List<String>) hmForMUnits.get("List"));
		final HashMap<Integer, String> hmMUnits = (HashMap<Integer, String>) hmForMUnits.get("hmMUnits");
		adapForMeasurementUnit = new ArrayAdapter<String>(MeasurementActivity.this, android.R.layout.simple_list_item_1,lstOfMeasurement);
		spnMeasurementUnit.setAdapter(adapForMeasurementUnit);
		spnMeasurementUnit.setSelection(measurementdescription);
		adapForMeasurementUnit.notifyDataSetChanged();
//		if(spnMeasurementUnit.getSelectedItemPosition() == 17 || spnMeasurementUnit.getSelectedItemPosition() == 22){ 
		if(!(measurementIdsForDecimal.contains(spnMeasurementUnit.getSelectedItemPosition()))){
			etQuantity.setText(""+Math.round(Double.valueOf(strQuantity)));
			etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
//			etQuantity.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
		}else{
			etQuantity.setText(String.valueOf(strQuantity));
			etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
		}
		/*else{
			etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}*/
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Double.valueOf(etQuantity.getText().toString())<= editingItems.getTotalUnits()){
					double mTotalAmount = Double.parseDouble(etQuantity.getText().toString())*editingItems.getCostPerItem();
//					updateMeasurement();
					dialog.dismiss();
					editingItems.setMeasurementid(measurementid);	
					editingItems.setMeasurmentQuantity(Double.parseDouble(etQuantity.getText().toString()));
					editingItems.setmTotalAmount(mTotalAmount);
					editingItems.setWorkItemTypeId(Integer.parseInt(strworkitemtypeid));
					BaseService baseService = new BaseService(getApplicationContext());
					long value = baseService.updateworkitemsformeasurement(editingItems,String.valueOf(workRowId));
					if(strQuantity - Double.parseDouble(etQuantity.getText().toString()) != 0){
						hmForFormula.put(items.getWorkItemTypeId(), (Double.parseDouble(etQuantity.getText().toString()) - strQuantity));
					}
					measurementQuantityOver.put(workRowId, hmForFormula);
					GescomUtility.setHmForMeasurementQuantityOver(measurementQuantityOver);
					spnMeasurement.setAdapter(editMeasurementAdapter); 
					spnMeasurement.setSelection(spnPosition,true);
					
				}else{
					Toast.makeText(getApplicationContext(), "Quantity should be less than or equal to EstimatedQuantity",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		etQuantity.setOnKeyListener(new OnKeyListener() {                 
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	        	//You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
	        	if(keyCode == KeyEvent.KEYCODE_DEL){  
	        		//this is for backspace
	        		if(!etQuantity.getText().toString().isEmpty()){
	        			if(!(etQuantity.getText().toString().startsWith(".")) || etQuantity.getText().toString().endsWith(".")){
	        				etAmount.setText(String.valueOf(Double
	        						.valueOf(etCostPerItem.getText().toString())
	        						* Double.valueOf(etQuantity.getText().toString())));
	        			}
	        		}
	        	}
	        	return false;       
	        }
		});
		
		etQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
//				if (spnMeasurementUnit.getSelectedItemPosition() == )
					if(count>=1){
						if(!etQuantity.getText().toString().startsWith(".")){
						etAmount.setText(String.valueOf(Double.valueOf(etCostPerItem.getText().toString()) - Double.valueOf(s.toString())));
						}
					}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				etAmount.setFocusable(false);
			}
		});
		spnMeasurementUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				if(pos>0){
					BaseService baseService = new BaseService(getApplicationContext());

					strSelectedmeasurement= element.getItemAtPosition(pos).toString();
					measurementid = baseService.getmeasurementid((strSelectedmeasurement));

				}
//				if(pos == 17 || pos == 22){
				if(!(measurementIdsForDecimal.contains(pos))){
					etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
				}else{
					etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
				}

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});


		dialog.show();


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

			//				final String childText = (String) getChild(groupPosition, childPosition);
			//				System.out.println(childText);

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
			//				expListView.setSelectedChild(groupPosition, childPosition, shouldExpandGroup)
			View view = infalInflater.inflate(R.layout.exp, null, false);
			TableLayout table = (TableLayout) view.findViewById(R.id.item_table);
			TableRow Header = new TableRow(MeasurementActivity.this);
			for (int j = 0; j <= 4; j++) {
				TextView cell = new TextView(MeasurementActivity.this) {
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
					cell.setText("Work Description");
				}
				if (j == 1) {
					cell.setText("Estimated Quantity");
				}

				if(j == 2){
					cell.setText("Measurement Quantity");
				}

				if(j == 3){
					cell.setText("Quantity over");
				}

				if(j == 4){
					cell.setText("Remaining Quantity");

				}

				cell.setPadding(6, 4, 6, 4);
				cell.setTextColor(Color.WHITE);
				cell.setBackgroundColor(Color.rgb(100, 100, 100));
				Header.addView(cell);
			}
			table.addView(Header,0);
			if(hmMeasurementTable != null){
				hmMeasurementTable.clear();
			}
			hmMeasurementTable  = new HashMap<TableRow, WorkItems>();


			for (WorkItems item : lstTableContents) {
				if (item.getBlockId().contains("B2") && groupPosition==1) {

					table.addView(addRowsToEditEstimationTable(item));
				}
				if (item.getBlockId().contains("B3") && groupPosition==2) {

					table.addView(addRowsToEditEstimationTable(item));
				}
				if (item.getBlockId().contains("B1") && groupPosition==0) {

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_DownloadIQuantities:
			if(spnMeasurement.getSelectedItemPosition()>0){
				new DownloadLatestItemQuantities().execute();
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.menu_Change_quatities_EstQ:
			
			if(spnMeasurement.getSelectedItemPosition()>0){
				if(baseService == null){
					baseService = new BaseService(MeasurementActivity.this);
				}
				lstTableContents=new ArrayList<WorkItems>();
				List<WorkItems> prevValues = new ArrayList<WorkItems>();
				BaseService service = new BaseService(MeasurementActivity.this);
				prevValues = service
						.getTableContent(workRowId);
				baseService.updateMeasurementQuantitiesToEstQuantities(String.valueOf(workRowId));
				lstTableContents = service
						.getTableContent(workRowId);
				for(WorkItems items:lstTableContents){
					hmForFormula.put(items.getWorkItemTypeId(), (items.getMeasurmentQuantity() - getgetMeasurmentQuantityForThisWorkitems(prevValues,items.getWorkItemTypeId())));
				}
				measurementQuantityOver.put(workRowId, hmForFormula);
				spnMeasurement.setAdapter(editMeasurementAdapter);
				spnMeasurement.setSelection(spnPosition,true);

			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
						
			break;

		case R.id.menu_Sendtoserver:
			if(spnMeasurement.getSelectedItemPosition()>0){
				if(isAnyMeasurementQuantitiesZero()){
					AlertDialog.Builder alert = new AlertDialog.Builder(MeasurementActivity.this);
					alert.setTitle("Warning");
					alert.setIcon(R.drawable.dialog_warning);
					alert.setMessage("You are about to send values to server where some of the measurement quantities are not defined, do you wish to continue?");
					alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							measurementstatus = "SMV";
							Updatemeasurement updatemeasure = new Updatemeasurement();
							updatemeasure.execute();
						}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
					alert.create();
					alert.show();
				}else{
					measurementstatus = "SMV";
					Updatemeasurement updatemeasure = new Updatemeasurement();
					updatemeasure.execute();
				}
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.menu_final_send:
			if(spnMeasurement.getSelectedItemPosition()>0){
				if(isAnyMeasurementQuantitiesZero()){
					AlertDialog.Builder alert = new AlertDialog.Builder(MeasurementActivity.this);
					alert.setTitle("Warning");
					alert.setIcon(R.drawable.dialog_warning);
					alert.setMessage("You are about to send values to server where some of the measurement quantities are not defined, do you wish to continue?");
					alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							measurementstatus = "AME";
							Updatemeasurement updatemeasurement = new  Updatemeasurement();
							updatemeasurement.execute();
						}
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
					alert.create();
					alert.show();

				}else{
					measurementstatus = "AME";
					Updatemeasurement updatemeasurement = new  Updatemeasurement();
					updatemeasurement.execute();
				}
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.menu_View_images:
			if(spnMeasurement.getSelectedItemPosition()>0){
				popUpwindow();
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.menu_Capture_image:
			if(spnMeasurement.getSelectedItemPosition()>0){
				Intent imageIntent = new Intent(MeasurementActivity.this, CameraView.class);
				//				String projectID=Integer.toString(projectData.get(spinProject.getSelectedItem().toString()));
				imageIntent.putExtra("Projectid",String.valueOf(workRowId));
				imageIntent.putExtra("workstatus","AC");
				startActivityForResult(imageIntent,IMAGECODE);
			}else{
				Toast.makeText(MeasurementActivity.this, "Please select project", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.menu_old_project:
			
			InitializeSpinner spn = new InitializeSpinner();
			spn.execute();
			break;
		default:
			break;
		}

		return true;
	}
	
	private double getgetMeasurmentQuantityForThisWorkitems(List<WorkItems> prevValues,
			int workItemTypeId2) {
		// TODO Auto-generated method stub
		for(WorkItems items:prevValues){
			if(items.getWorkItemTypeId() == workItemTypeId2){
				return items.getMeasurmentQuantity();
			}
		}
		return 0;
	}
	private boolean isAnyMeasurementQuantitiesZero() {
		// TODO Auto-generated method stub
		BaseService bService = new BaseService(MeasurementActivity.this);
		lstTableContents = bService
				.getTableContent(workRowId);
		for(WorkItems items : lstTableContents){
			if(items.getMeasurmentQuantity() == 0){
				if(items.getTotalUnits() == 0){
					continue;
				}
				else{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_measurement, menu);
		return true;
	}

}

