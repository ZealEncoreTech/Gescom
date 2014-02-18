package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.collections.map.MultiValueMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.Htline;
import com.zeal.gov.kar.gescom.model.Ltline;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.model.Transformer;
import com.zeal.gov.kar.gescom.model.WorkFinancialDetails;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SurveySketchActivity extends  FragmentActivity  implements OnItemSelectedListener {

	private Spinner spnSurvey;
	MultiValueMap descMap;
	BaseService baseService;
	private Htline htline;
	int lstsize;
	List<TaskDetails> lstTaskDetails;
	List<String> lstWorkDescription;
	List<Projectestimates> lstSurveyCoordinates;
	List<ProjectEstimationType> lstEstimationTypes;
	private ArrayList<ApprovedPhotoCoordinates> mainImageList;
	private List<Ltline> ltlineList;
	ArrayAdapter<String> arrayAdapterWorkDesc;
	int WorkRowId;
	private Map<Marker,Projectestimates> hmMarker;
	private List<String> descriptionList,itemList;
	private GoogleMap mMap;
	HashMap<Integer, Integer> hmForSpinner;
	private ArrayAdapter<String> descpAdapter;
	private List<Htline> htlineList;
	private String projectName;
	private HashMap<String, Integer> projectData;
	private Ltline ltline;
	private List<Transformer> transformerList;
	private Transformer transformer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey_sketch);
		spnSurvey = (Spinner)findViewById(R.id.spnSurveySketch);
		descMap = new MultiValueMap();
		descriptionList = new LinkedList<String>();
		baseService = new BaseService(SurveySketchActivity.this);
		hmMarker = new HashMap<Marker, Projectestimates>();
		lstSurveyCoordinates = new ArrayList<Projectestimates>();
		lstEstimationTypes = new ArrayList<ProjectEstimationType>();
		GetTaskDetails details = new GetTaskDetails();
		details.execute();
		
		htlineList = new ArrayList<Htline>();
		
		ltlineList=new ArrayList<Ltline>();
		
		transformerList=new ArrayList<Transformer>();
		if (mMap == null) {

			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			if (mMap != null) {
				setUpMap();
				
			}
		}
		
		
		
		
	}
	public void onBackPressed()
	{
		super.onBackPressed();
		copyDatabaseToSdcard();
		
	}
	private void copyDatabaseToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			Toast.makeText(SurveySketchActivity.this, "Latest Db Copied to sddcard", Toast.LENGTH_LONG).show();
			if (sd.canWrite()) {
				String currentDBPath = "//data//com.zeal.gov.kar.gescomtesting//databases//gescom.db";
				String backupDBPath = "//testingbackup//gescom.db";
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
	@SuppressWarnings("unchecked")
	public void Spinnerinitialization(){
	/*	HashMap<String,Object> hm = baseService.getprojectsforsurvey(Appuser.getUserName());
		 = (HashMap<String,Integer>) hm.get("MultiValueMap");
		descriptionList.add("Select Project");
		descriptionList.addAll((List<String>) hm.get("List"));
		descpAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, descriptionList);
		spnSurvey.setAdapter(descpAdapter);*/
		
		lstWorkDescription = new ArrayList<String>();
		lstWorkDescription.add("Select Work");
		Iterator<TaskDetails> itr = lstTaskDetails.iterator();
		hmForSpinner = new HashMap<Integer, Integer>();
		int count = 1;
		while (itr.hasNext()) {
			TaskDetails taskDetails = itr.next();
			hmForSpinner.put(count++, taskDetails.getWorkRowId());
			lstWorkDescription.add(taskDetails.getWorkDescription());
			
		}
		arrayAdapterWorkDesc = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lstWorkDescription);
		lstsize = lstWorkDescription.size()-1;
		spnSurvey.setAdapter(arrayAdapterWorkDesc);
		spnSurvey.setOnItemSelectedListener(this);
		
		
		
	}
	private class GetTaskDetails extends AsyncTask<String, Void, String>{
		ProgressDialog dialog = new ProgressDialog(SurveySketchActivity.this);
		Soapproxy soapproxy = new Soapproxy(SurveySketchActivity.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("Downloading");
			dialog.setMessage("Please Wait!!");
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
				String result = "";
				
				try{
					lstTaskDetails = new ArrayList<TaskDetails>();
				result = soapproxy.getSurveyWorks(Appuser.getUserName());
					if(result.equalsIgnoreCase("ServerTimeOut")||result.equalsIgnoreCase("noInternet"))
					{
						Log.e("Error", ""+result);
						return result;
					}
					else{
						Parser parser = new Parser(SurveySketchActivity.this);
						
						lstTaskDetails = parser.getSurveyWorks(result);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					result = "error";
				}
			return result;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if(lstTaskDetails.size()==0){
				message("Currently no Projects are there");
			}
			else if(result.equalsIgnoreCase("ServerTimeOut")){
				message("Failed to connect to server");
			}
			else if(result.equalsIgnoreCase("noInternet")){
				message("Data connection unavailble, Please enable connection and try again");
			}
			else{
				if(lstTaskDetails.size()>0){
					 
					
					
					Spinnerinitialization();
					message(lstsize+"  "+"Projects download successfull");
				}
				else if(result.equalsIgnoreCase("error")){
					message("Error requesting service, please try again later");
					startActivity();
				}
				else{
					message("Error requesting service, please try again later");
					startActivity();
				}
			}
		}
		
	}
	
	private class StoringData extends AsyncTask<String, Void, String>{
		ProgressDialog dialog = new ProgressDialog(SurveySketchActivity.this);
		Soapproxy soapproxy = new Soapproxy(SurveySketchActivity.this);
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("Please wait");
			dialog.setMessage("Storing Data");
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = new String();
					// TODO Auto-generated method stub
			try{
					
					if(result.equalsIgnoreCase("ServerTimeOut")||result.equalsIgnoreCase("noInternet")){
						Log.e("Error", ""+result);
						return result;
					}
					else{
						result = soapproxy.getSurveyCoordinates(WorkRowId);
						Parser parser = new Parser(SurveySketchActivity.this);
						HashMap<String,Object> hm = parser.getSurveyCoordinatesData(result);
						lstSurveyCoordinates = (List<Projectestimates>) hm.get("lst1");
						lstEstimationTypes = (List<ProjectEstimationType>) hm.get("lst2");
					}
			}catch (Exception e) {
				e.printStackTrace();
				result = "error";
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if(result.equalsIgnoreCase("ServerTimeOut"))
			{
				message("Failed to connect to server");
			}
			else if(result.equalsIgnoreCase("noInternet")){
				message("Data connection lost,Please try again later");
			}
			else if(lstSurveyCoordinates.size()>0){
				Iterator<Projectestimates> itr = lstSurveyCoordinates.iterator();
				while (itr.hasNext()) {
					Projectestimates estimates = itr.next();
					if(!baseService.isProjectEstimationDatavailable(String.valueOf(estimates.getWorkRowId()),String.valueOf(estimates.getEstimationId()),estimates.getCoordinatesCaption(),String.valueOf(estimates.getTypeOfLine()),String.valueOf(estimates.getLtEstId()))){
				baseService.addToProjectEstimatesTemp(estimates);
					}
			}
				Iterator<ProjectEstimationType> itr1 = lstEstimationTypes.iterator();
				while (itr1.hasNext()) {
					ProjectEstimationType estimationType = itr1.next();
					if(!baseService.isProjectEstimationTypeDatavailable(String.valueOf(estimationType.getProjectId()),String.valueOf(estimationType.getEstimationId()),estimationType.getEstDescription(),estimationType.getLineId(),String.valueOf(estimationType.getLtEstId()))){
				baseService.addToProjectEstimatesType(estimationType);
					}
			}
				mMap.clear();
				
			htlineList=baseService.getHtlinesTemp(String.valueOf(WorkRowId));
			ltlineList=baseService.getLtinesTemp(String.valueOf(WorkRowId));
			transformerList=baseService.getTransformersTemp(String.valueOf(WorkRowId));
			fetchPriviousData();
			}	
			else if(result.equalsIgnoreCase("error")){
				message("Error requesting service, please try again later");
				startActivity();
			}
			else{
				message("Error requesting service, please try again later");
				startActivity();
			}
		}
		
	}
	private void startActivity(){
		Intent userMenuIntent = new Intent(SurveySketchActivity.this,UsermenuActivity.class);
		userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(userMenuIntent);
		finish();
	}
	private void message(String msg){
		Toast.makeText(SurveySketchActivity.this,
				msg, Toast.LENGTH_LONG).show();

	}
	private  void fetchPriviousData()
	{
		boolean reasult = baseService.isProjectdataAvailableTemp(String.valueOf(WorkRowId));
		if (reasult) {
			Projectestimates preData = null;
			List<Projectestimates> preValues = baseService
					.getSurveySketchDetails(String.valueOf(WorkRowId));
			Iterator<Projectestimates> itPPreValues = preValues.iterator();
			while (itPPreValues.hasNext()) {
				preData = itPPreValues.next();
				if(preData.getTypeOfLine()==1)
				{
					htline=getHtLineByID(preData.getEstimationId());
					htline.setStartLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					htline.setEndLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					Marker mark=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet(Double.toString(preData.getDistance())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
					hmMarker.put(mark,preData );
				}
				else if(preData.getTypeOfLine()==2)
				{
					ltline=getLtLineById(preData.getEstimationId(), preData.getLtEstId());
					ltline.setStartLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					ltline.setEndLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					Marker mark=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet(Double.toString(preData.getDistance())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
					hmMarker.put(mark,preData );
				}
				else if(preData.getTypeOfLine()==3)
				{
					transformer=getTransformerById(preData.getEstimationId(), preData.getLtEstId());
					transformer.setLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					Marker mark=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet("0.0").icon(BitmapDescriptorFactory.fromResource(R.drawable.transformer)).draggable(true));
					hmMarker.put(mark,preData );
				}
				
			}
			if(htline!=null)
			if(!htline.getLocationList().isEmpty()&& htline!=null)
			{
				for(Htline preHt:htlineList)
			    if(!preHt.getLocationList().isEmpty())
				mMap.addPolyline((new PolylineOptions()).addAll(preHt.getLocationList()).width(3).color(Color.BLUE).geodesic(true));
				
			}
			if(ltline!=null)
			if(!ltline.getLocationList().isEmpty())
			{
				for(Ltline preLt:ltlineList)
				if(!preLt.getLocationList().isEmpty())
				mMap.addPolyline((new PolylineOptions()).addAll(preLt.getLocationList()).width(3).color(Color.RED).geodesic(true));
				
			}
			if(preValues.size()>0){
				Projectestimates projectestimates = preValues.get(preValues.size()-1);
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLngFromEstimates(projectestimates), 15.5f));
			}
		}
/*		if (baseService.isProjectphotosAvailable(Integer.toString(projectData
				.get(projectName)))) {
			List<ApprovedPhotoCoordinates> prePhotos = baseService
					.getProgressImages(
							Integer.toString(projectData.get(projectName)),
							"BS");
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
						Toast.makeText(SurveySketchActivity.this,
								"Some images are missing!!!", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				mainImageList.add(preImage);
			}
		}*/
	}
	private Htline getHtLineByID(int ID) {
		Htline htObj = null;
		for (Htline ht : htlineList) {
			if (ht.getHtId()==ID) {
				return ht;
				
			}
		}
		return htObj;
	}
	private Ltline getLtLineById(int htId,int ltId) {
		Ltline ltObj = null;
		for (Ltline lt : ltlineList) {
			if (lt.getHtId() == htId && lt.getLtId()==ltId) {
				return lt;
				
			}
		}
		return ltObj;
	}
	private Transformer getTransformerById(int htId,int tcId) {
		Transformer transObj = null;
		for (Transformer trans : transformerList) {
			if (trans.getHtId()==htId && trans.getTransId()==tcId) {
				return trans;
				
			}
		}
		return transObj;
	}
	private void setUpMap() {

		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//mMap.setMyLocationEnabled(true);
		
		itemList = new LinkedList<String>();
		itemList.add("Select Line Type");
		itemList.add("HT Line");
		itemList.add("LT Line");
		itemList.add("Transformer");
		
         mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				// TODO Auto-generated method stub
				View v = getLayoutInflater().inflate(R.layout.markerinfowindow, null);
				String title = marker.getTitle();
				String distance = marker.getSnippet();
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
				TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
				TextView tvLong = (TextView) v.findViewById(R.id.tvLong);
				TextView tvLineType = (TextView) v.findViewById(R.id.tvLineType);
				Projectestimates prEst = hmMarker.get(marker);
				tvTitle.setText(title);
				tvDistance.setText(distance);
				tvLat.setText(String.valueOf(marker.getPosition().latitude));
				tvLong.setText(String.valueOf(marker.getPosition().longitude));
				if(hmMarker != null){
					if(prEst!=null){
						tvLineType.setText(itemList.get(prEst.getTypeOfLine()));
					}
				}
				return v;
			}
		});
	
	}
	private LatLng getLatLngFromEstimates(Projectestimates projectestimates) {
		// TODO Auto-generated method stub
		
		return new LatLng(Double.parseDouble(projectestimates.getStartLattitude()), Double.parseDouble(projectestimates.getStartLangtitude()));
	}

	@Override
	public void onItemSelected(AdapterView<?> adp, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		int id = adp.getId();
		switch (id) {
		case R.id.spnSurveySketch:
			if(pos > 0){
				
				
				String strWorkDescription = adp.getItemAtPosition(
						pos).toString();
				System.out.println("workrowId From HM----"+hmForSpinner.get(pos));
				Iterator<TaskDetails> itr = lstTaskDetails.iterator();
				while (itr.hasNext()) {
					TaskDetails taskDetails = itr.next();
					if (taskDetails.getWorkDescription()
							.equalsIgnoreCase(strWorkDescription)) {
						WorkRowId = taskDetails.getWorkRowId();
						}
					}
					System.out
				.println("WorkRowId for selected description----"
						+ WorkRowId);
					Soapproxy soapproxy = new Soapproxy(this);
					/*soapproxy.getSurveyCoordinates(WorkRowId);
					String response = soapproxy.getSurveyCoordinates(WorkRowId);*/
					StoringData storingData = new StoringData();
					storingData.execute();
					

			}
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
