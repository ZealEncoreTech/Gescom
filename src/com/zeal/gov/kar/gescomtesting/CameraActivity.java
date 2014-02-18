package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedCoordinates;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.Htline;
import com.zeal.gov.kar.gescom.model.Ltline;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.Transformer;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;

public class CameraActivity extends FragmentActivity implements LocationListener,
OnClickListener {
	private EditText Caption;
	private GoogleMap map;
	private Spinner captionText, workDescription, poleType,spinEatimationType,updateSpn;
	private LocationManager locationManager;
	private BaseService baseService;
	private Button saveCoordinates, sendToServer,btnshowcoordinates,btnCancel,btnShowHideMap;
	//	private Button captureImage;
	private LinearLayout linLayForImage,layout;
	private String strImageFileName,strBase64,edit,
	strLatitude, strLongitude, strWorkStatus,filePath;

	private Bitmap capturedBitmap,captionImage;
	private ImageView ivCaptured,addtoLayout,ivCapturedImage;
	private Marker currMarker;
	private boolean isLocationLoaded =true;
	private int imageId;
	private List<String> spinnerCaptionlist, spinnerDescription, spinnerpole,
	setSpinner,lstEstimationType,lstEstID;
	private ArrayAdapter<String> captionItemsadpter, descriptionItemsadapter,
	poleItemsadapter,adpEstimationType,adp;
	private LinearLayout llForImage;
	private TextView tvMessage, tvDisplayCoordinates,tvShowCoordinatesP;
	private EditText etPhotoCaption;
	private Location imageLocation, startLocation, endLocation, currentLocation;
	private Soapproxy proxy;
	private HashMap<String,String> estDescMap;
	private static final int PICTURE_RESULT = 0;
	private String strSelectedPole, strSelectedwork;
	private int countForCoor = 0, countForPhotoId = 0;
	private List<ApprovedPhotoCoordinates> lstApprovedPhotoCoor = new ArrayList<ApprovedPhotoCoordinates>();
	private List<ApprovedCoordinates> lstapprovedcoordinates = new ArrayList<ApprovedCoordinates>();
	private ApprovedCoordinates approvedcordinates = new ApprovedCoordinates();
	private ApprovedCoordinates selectedApprCoor;
	private List<Htline> htlineList;
	private List<Ltline> ltlineList;
	private List<Transformer> transformerList;
	private Htline htline;
	private Ltline ltline;
	private Transformer transformer;
	int workRowId;
	Uri imageUri;
	String ltEstId;
	String Result;
	String est1,finalSendStr;
	private PopupWindow window;
	String fileName=null;
	List<String> lstCaption ;
	ArrayAdapter<String> aaCaption ;
	private HashMap<Marker, ApprovedCoordinates> hmACMarker = new HashMap<Marker, ApprovedCoordinates>();
	private HashMap<Marker, Projectestimates> hmGPEMarker = new HashMap<Marker, Projectestimates>();
	private int width,height;
	private MenuItem menuItem;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera);
		//		UIInitialization initialize = new UIInitialization();
		//		initialize.execute();
		uiInitialIzation();
	}



	@SuppressWarnings("unchecked")
	private void uiInitialIzation() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) CameraActivity.this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();  // deprecated
		height = display.getHeight(); 

		copyDatabaseToSdcard();
		estDescMap=new HashMap<String, String>();
		lstCaption = new ArrayList<String>();
		lstCaption.add("Select caption");
		baseService = new BaseService(getApplicationContext());
		Caption = (EditText) findViewById(R.id.etCaption);
		captionText = (Spinner) findViewById(R.id.spnphotocaption);
		call();
		workDescription = (Spinner) findViewById(R.id.spnworkdescription);
		poleType = (Spinner) findViewById(R.id.spnpoletype);
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10, 0, CameraActivity.this);
		setSpinner = new ArrayList<String>();
		linLayForImage = (LinearLayout) findViewById(R.id.linLayForImage);
		btnShowHideMap = (Button) findViewById(R.id.btnShowHideMap);
		layout = (LinearLayout) findViewById(R.id.llayout);
		final List<String> itemList = new ArrayList<String>();
		itemList.add("Select Line Type");
		itemList.add("HT Line");
		itemList.add("LT Line");
		itemList.add("Transformer");
		if (map == null) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			if (map != null) {
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker marker) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						// TODO Auto-generated method stub
						if(baseService == null){
							baseService = new BaseService(CameraActivity.this);
						}
						View view;
						view = getLayoutInflater().inflate(R.layout.info_ic_map, null);
						TextView tvCaption = (TextView) view.findViewById(R.id.tvCaption);
						TextView tvEstType = (TextView) view.findViewById(R.id.tvEstType);
						TextView tvPoleType = (TextView) view.findViewById(R.id.tvPoleType);
						TextView tvTOL = (TextView) view.findViewById(R.id.tvTOL);
						LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llForImage);
						approvedcordinates = hmACMarker.get(marker);

						if(approvedcordinates != null){
							String caption = approvedcordinates.getsCaption();
							lstApprovedPhotoCoor = baseService.gettableapprovephotocoor(workRowId);
							String estType = baseService.getEstTypeDesc(approvedcordinates);
							Iterator<ApprovedPhotoCoordinates> itr = lstApprovedPhotoCoor
									.iterator();
							boolean isImageAvailable = false;
							Bitmap bitmap = null;
							while (itr.hasNext()) {
								try {
									ApprovedPhotoCoordinates approvedphotocoordinates = itr.next();
									if(caption.equalsIgnoreCase(approvedphotocoordinates.getPhotoCaption())){
										String imageFileName = approvedphotocoordinates.getFileName();
										File imageFile = new File(imageFileName);
										Uri uri = Uri.fromFile(imageFile);
										bitmap = decodeUri(uri);
										if(bitmap != null){
											String string = imageFileName.replace("/mnt/sdcard/GescomPhotos/Gescom-", "");
											string = string.replace(".jpg", "");
											Date date = new Date(Long.parseLong(string));
											bitmap = timestampItAndSave(bitmap, approvedphotocoordinates.getPhotoCaption(),date.toString());
											isImageAvailable = true;
										}
									}
								}
								catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
							if(!isImageAvailable){
								bitmap = BitmapFactory.decodeResource(CameraActivity.this.getResources(), 
										R.drawable.images1);
								addtoLayout = new ImageView(getApplicationContext());
								addtoLayout.setImageBitmap(bitmap);
								linearLayout.addView(addtoLayout);
							}else{
								if(bitmap!=null){
									addtoLayout = new ImageView(getApplicationContext());
									addtoLayout.setPadding(10, 05, 10, 05);
									addtoLayout.setImageBitmap(bitmap);
									linearLayout.addView(addtoLayout);
								}
							}
							tvCaption.setText(approvedcordinates.getsCaption());
							tvEstType.setText(estType);
							tvPoleType.setText(spinnerpole.get(approvedcordinates.getiPoleType()));
							tvTOL.setText(itemList.get(approvedcordinates.getTypeOfLine()));
						}
						else{
							view = getLayoutInflater().inflate(R.layout.markerinfowindow, null);
							String title = marker.getTitle();
							String distance = marker.getSnippet();
							TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
							TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
							TextView tvLat = (TextView) view.findViewById(R.id.tvLat);
							TextView tvLong = (TextView) view.findViewById(R.id.tvLong);
							TextView tvLineType = (TextView) view.findViewById(R.id.tvLineType);
							Projectestimates prEst = hmGPEMarker.get(marker);
							tvTitle.setText(title);
							tvDistance.setText(distance);
							tvLat.setText(String.valueOf(marker.getPosition().latitude));
							tvLong.setText(String.valueOf(marker.getPosition().longitude));
							if(hmGPEMarker != null){
								if(prEst!=null){
									tvLineType.setText(itemList.get(prEst.getTypeOfLine()));
								}
							}
						}
						return view;
					}
				});
			}
		}
		btnShowHideMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(workDescription.getSelectedItemId()>0){
					map.clear();
					fetchPriviousData();
					String string = btnShowHideMap.getText().toString();
					if(string.equalsIgnoreCase("Show Map")){
						btnShowHideMap.setText("Hide Map");

						layout.getLayoutParams().height = (int) height/25*10;
						layout.getLayoutParams().width = (int)width/16*10;
						layout.requestLayout();
						layout.setVisibility(View.VISIBLE);
					}
					if(string.equalsIgnoreCase("Hide Map")){
						btnShowHideMap.setText("Show Map");
						//	        			LinearLayout layout = (LinearLayout) findViewById(R.id.llayout);
						layout.setVisibility(View.GONE);
					}
				}
				else {
					Toast.makeText(CameraActivity.this, "Please select the project", Toast.LENGTH_LONG).show();
				}

			}
		});

		btnCancel=(Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//		captureImage = (Button) findViewById(R.id.btnCapture);
		//		captureImage.setEnabled(true);//testing
		//		captureImage.setOnClickListener(this);
		saveCoordinates = (Button) findViewById(R.id.btnSaveCoordinates);
		//		saveCoordinates.setEnabled(false);//testing
		saveCoordinates.setOnClickListener(this);
		sendToServer = (Button) findViewById(R.id.btnSend);
		sendToServer.setOnClickListener(this);
		spinEatimationType=(Spinner) findViewById(R.id.spinEstimationType);
		spinnerDescription = new ArrayList<String>();
		spinnerDescription.add("Please select project");
		spinnerDescription.addAll(baseService.getWorkDescription("AME", "SMV",
				Appuser.getUserName()));
		tvShowCoordinatesP = (TextView) findViewById(R.id.tvCurrentCoorsP);
		tvDisplayCoordinates = (TextView) findViewById(R.id.tvCurrentCoorsC);

		currentLocation = locationManager
				.getLastKnownLocation(locationManager.GPS_PROVIDER);


		ivCaptured = new ImageView(getApplicationContext());
		descriptionItemsadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerDescription);
		workDescription.setAdapter(descriptionItemsadapter);
		workDescription.setOnItemSelectedListener(new OnItemSelectedListener() {
			//
			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				if(pos>0){
					strSelectedwork = element.getItemAtPosition(pos).toString();

					countForCoor = 0;
					countForPhotoId = 0;
					workRowId = baseService.getWorkRowId(strSelectedwork);
					lstCaption.clear();
					lstCaption.add("Select caption");
					lstCaption.addAll(baseService.getCaptionListFromApprCoor(workRowId));
					call();
					lstApprovedPhotoCoor = baseService.gettableapprovephotocoor(workRowId);	
					Iterator<ApprovedPhotoCoordinates> itr = lstApprovedPhotoCoor
							.iterator();
					while (itr.hasNext()) {
						try { 
							ApprovedPhotoCoordinates approvedphotocoordinates = itr.next();
							String imageFileName = approvedphotocoordinates.getFileName();
							File imageFile = new File(imageFileName);
							Uri uri = Uri.fromFile(imageFile);
							Bitmap bitmap = decodeUri(uri);
							//Gescom-"+System.currentTimeMillis()+".jpg
							///mnt/sdcard/GescomPhotos/Gescom-1378294010035.jpg
							String string = imageFileName.replace("/mnt/sdcard/GescomPhotos/Gescom-", "");
							string = string.replace(".jpg", "");
							Date date = new Date(Long.parseLong(string));
							bitmap = timestampItAndSave(bitmap, approvedphotocoordinates.getPhotoCaption(),date.toString());
							addBitmaptoLayout(bitmap);
						}
						catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					// lstApprovedPhotoCoor = new
					// ArrayList<ApprovedPhotoCoordinates>();
					lstEstimationType.clear();

					estDescMap.clear();
					lstEstimationType.add("Estimation Type");
					/*estDescMap=baseService.getMultiDescription(Integer.toString(workRowId));
					Iterator<Entry<String, String>> descIterator=estDescMap.entrySet().iterator();
					while(descIterator.hasNext())
					{
						Entry<String, String> entry = descIterator.next();
						lstEstimationType.add(entry.getKey());
						//	System.out.println(entry.getValue());
						//ltEstId = entry.getValue();
						System.out.println(entry.getValue());

					}	*/	
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm = baseService.getMultiList(Integer.toString(workRowId));
					estDescMap=(HashMap<String, String>) hm.get("HashMap");
					lstEstimationType.addAll((List<String>) hm.get("List"));
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		lstEstimationType=new ArrayList<String>();
		lstEstimationType.add("Estimation Type");

		//		HashMap<String, Object> hm = new HashMap<String, Object>();
		//		hm = baseService.getMultiDescription(Integer.toString(workRowId));
		//		estDescMap=(HashMap<String, String>) hm.get("HashMap");
		//		lstEstimationType.addAll((List<String>) hm.get("List"));
		/*		Iterator<Entry<String, String>> descIterator=estDescMap.entrySet().iterator();
		while(descIterator.hasNext())
		{
			Entry<String, String> entry = descIterator.next();
			lstEstimationType.add(entry.getKey());

		}*/
		adpEstimationType=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,lstEstimationType);
		spinEatimationType.setAdapter(adpEstimationType);
		spinEatimationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				String est = element.getItemAtPosition(pos).toString();
				if(workDescription.getSelectedItemPosition() > 0){

					est1 = estDescMap.get(est);
					System.out.println(est1);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		//
		spinnerpole = new ArrayList<String>();
		spinnerpole.add("Select pole type");
		spinnerpole.addAll(baseService.getPoleType());

		poleItemsadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerpole);
		poleType.setAdapter(poleItemsadapter);
		spinnerCaptionlist = new ArrayList<String>();
		poleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				String string = element.getItemAtPosition(pos).toString();
				strSelectedPole = string;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		captionText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos>0){
					final int position = pos;
					final String strCaption = element.getItemAtPosition(pos).toString();
					selectedApprCoor = new ApprovedCoordinates();
					List<ApprovedCoordinates> lstAp = baseService.getapprovecoordinates(workRowId);
					lstapprovedcoordinates = lstAp;
					selectedApprCoor = getApprCoordinate(lstAp, strCaption);
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.listview_popup, null); 
					window = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
					window.setBackgroundDrawable(new BitmapDrawable());
					window.setFocusable(true);
					ListView listView = (ListView) view.findViewById(R.id.lvPopup);
					List<String> lst = new ArrayList<String>();
					lst.add("Take photo for this caption");
					lst.add("Add another poletype here");
					lst.add("Edit this point");
					lst.add("Delete this point");
					lst.add("Cancel");
					ArrayAdapter<String> options = new ArrayAdapter<String>(CameraActivity.this,
							android.R.layout.simple_list_item_1, android.R.id.text1, lst);
					listView.setAdapter(options);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String selectedView = arg0.getItemAtPosition(arg2).toString();
							if(selectedView.equalsIgnoreCase("Take photo for this caption")){
								Caption.setText(strCaption);
								takePhotoForThisCaption(strCaption);
								captionText.setSelection(0);
							}
							else if(selectedView.equalsIgnoreCase("Edit this point")){
								if(!baseService.isThisCaptionFromApprovedCoorUploaded(workRowId,strCaption)){
									editThisPoint(strCaption,position);
									captionText.setSelection(0);
								}else{
									Toast.makeText(CameraActivity.this, "Sorry, you can't edit as coordinates for this point is already uploaded to server", Toast.LENGTH_LONG).show();
								}
							}
							else if(selectedView.equalsIgnoreCase("Delete this point")){
								if(!baseService.isThisCaptionFromApprovedCoorUploaded(workRowId,strCaption)){
									deleteThisPoint(strCaption, position);
									captionText.setSelection(0);
								}else{
									Toast.makeText(CameraActivity.this, "Sorry,you can't delete as coordinates for this point is already uploaded to server", Toast.LENGTH_LONG).show();
								}
							}
							else if(selectedView.equalsIgnoreCase("Add another poletype here")){
								addAnotherPoleTypeHere(strCaption,position);
								Caption.setText("");
								captionText.setSelection(0);
							}
							else if(selectedView.equalsIgnoreCase("Cancel")){
								captionText.setSelection(0);
							}
							window.dismiss();
						}


					});
					window.showAtLocation(view, Gravity.CENTER, 20, 20);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressLint("NewApi")
	private void addAnotherPoleTypeHere(final String strCaption,final int position) {
		// TODO Auto-generated method stub
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this); 
		}
		final List<ApprovedCoordinates> lstApCoor = baseService.getapprovecoordinates(workRowId);
		ApprovedCoordinates aCoor = getApprCoordinate(lstApCoor, strCaption);
		edit = Caption.getText().toString();
		if(spinEatimationType.getSelectedItemId()>0){
			if(poleType.getSelectedItemPosition()>0){
				if(!Caption.getText().toString().isEmpty()){
					if(!lstCaption.contains(edit)){

						if(baseService == null){
							baseService = new BaseService(CameraActivity.this);
						}
						//					captureImage.setEnabled(true);
						tvDisplayCoordinates.setText(String.valueOf(currentLocation.getLatitude())+","+String.valueOf(currentLocation.getLongitude()));
						approvedcordinates.setLoginId(Appuser.getUserName());

						int estimationId = baseService.getEstimationIdFromDesc(String.valueOf(workRowId),spinEatimationType.getSelectedItem().toString());
						approvedcordinates.setEstimationid(estimationId);

						int selectedTypeOfLine = baseService.getTypeOfLine(String.valueOf(workRowId),spinEatimationType.getSelectedItem().toString());

						approvedcordinates.setLatitude(aCoor.getLatitude());
						approvedcordinates.setLongitude(aCoor.getLongitude());
						countForCoor = baseService.getlastcoordinateid(workRowId,est1,String.valueOf(estimationId),String.valueOf(selectedTypeOfLine));
						approvedcordinates.setCoordinatesId(++countForCoor);
						approvedcordinates.setLtEstId(est1);
						lstCaption.add(edit);
						adp.notifyDataSetChanged();

						//Caption.setText(null);
						call();
						SimpleDateFormat datefarmate = new SimpleDateFormat(
								"dd-MM-yyyy hh:mm:ss");
						Calendar calender = Calendar.getInstance();
						String date = datefarmate.format(calender.getTime());
						approvedcordinates.setCapturedOn(date);

						//		setSpinner.add(edit);
						approvedcordinates.setsCaption(edit);
						approvedcordinates.setiPoleType(baseService
								.getpoleId(strSelectedPole));
						approvedcordinates.setiPoleId(approvedcordinates.getCoordinatesId());
						approvedcordinates.setProjectId(workRowId);
						approvedcordinates.setTypeOfLine(selectedTypeOfLine);
						lstapprovedcoordinates.add(approvedcordinates);

						baseService.insertintoapprovecoordinates(approvedcordinates);
						copyDatabaseToSdcard();
						Caption.setText("");
						captionText.setSelection(0);
					}
					else{
						Toast.makeText(getApplicationContext(), "Please Provide different caption", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(CameraActivity.this, "Please enter caption", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(CameraActivity.this, "Please select poletype", Toast.LENGTH_LONG).show();
			}

		}else{
			Toast.makeText(CameraActivity.this, "Please select estimation type", Toast.LENGTH_LONG).show();
		}
	}

	private void deleteThisPoint(final String strCaption, final int pos) {
		// TODO Auto-generated method stub
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this); 
		}
		final List<ApprovedCoordinates> lstApCoor = baseService.getapprovecoordinates(workRowId);
		final ApprovedCoordinates ac = getApprCoordinate(lstApCoor, strCaption);
		AlertDialog.Builder alertbox = new AlertDialog.Builder(CameraActivity.this);
		alertbox.setIcon(R.drawable.dialog_warning);
		alertbox.setTitle("Delete this point");
		alertbox.setMessage("You are about to delete coordinate which in turn may delete its respective photos, do you want to continue anyway?");
		alertbox.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int res  = baseService.deleteApprCoorFromTable(String.valueOf(workRowId), ac);
				if(res>0){
					lstCaption.remove(pos);
					adp.notifyDataSetChanged();
				}
				int deletedResult = 0;
				int i = ac.getCoordinatesId();
				for(ApprovedCoordinates coor:lstApCoor){
					if(ac.getEstimationid() == coor.getEstimationid() && ac.getLtEstId().equalsIgnoreCase(coor.getLtEstId()) && ac.getTypeOfLine() == coor.getTypeOfLine()){
						ApprovedCoordinates currApCo = getApprCoordinate(lstApCoor, coor.getsCaption());
						if(ac.getCoordinatesId()<currApCo.getCoordinatesId()){
							if(currApCo != null){
								currApCo.setCoordinatesId(currApCo.getCoordinatesId()-1);
								baseService.updateAppCoorForID(currApCo);
								String cap = currApCo.getsCaption();
								if(baseService.isApprPhotoCoorAvailable(String.valueOf(workRowId),cap)){
									deletedResult = deletedResult+baseService.updateApprPhCoorId(String.valueOf(workRowId),cap,String.valueOf(currApCo.getCoordinatesId()));
								}
							}
						}
					}
				}
				/*for(ApprovedCoordinates coor:lstApCoor){
					if(c.getEstimationid() == coor.getEstimationid() && c.getLtEstId().equalsIgnoreCase(coor.getLtEstId()) && c.getTypeOfLine() == coor.getTypeOfLine()){
						coor.setCoordinatesId(++i);
						if(baseService.isApprCoorAvailable(String.valueOf(workRowId),coor)){
							deletedResult = deletedResult+baseService.deleteApprCoorFromTable(String.valueOf(workRowId), coor);
						}
					}
				}*/

				if(baseService.isApprPhotoCoorAvailable(String.valueOf(workRowId),strCaption)){
					deletedResult = deletedResult+baseService.deleteApprPhotoCoor(String.valueOf(workRowId),strCaption);
				}
				if(deletedResult>0){
					Toast.makeText(CameraActivity.this, "Coordinates have been deleted successfully", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(CameraActivity.this, "Coordinates have not been captured for this caption to delete", Toast.LENGTH_LONG).show();
				}
			}
		});
		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		alertbox.show();
	}

	private void editThisPoint(final String strCaption, final int pos) {
		// TODO Auto-generated method stub
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this); 
		}
		AlertDialog.Builder alertbox = new AlertDialog.Builder(CameraActivity.this);
		List<ApprovedCoordinates> lstApCoor = baseService.getapprovecoordinates(workRowId);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.updateview_for_ic_screen, null);
		ApprovedCoordinates apCoor = getApprCoordinate(lstApCoor,strCaption);
		alertbox.setView(view);
		//		final PopupWindow lWindow =  new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
		updateSpn= (Spinner) view.findViewById(R.id.spnUpdate);
		updateSpn.setAdapter(poleItemsadapter);
		final EditText editText = (EditText) view.findViewById(R.id.edUpdate);
		editText.setText(strCaption);
		alertbox.setTitle("Edit this point");
		alertbox.setPositiveButton("Update", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				lstCaption.remove(pos);
				lstCaption.add(editText.getText().toString());
				adp.notifyDataSetChanged();
				baseService.updateAppCoor(String.valueOf(workRowId),editText.getText().toString(),baseService.getpoleId(updateSpn.getSelectedItem().toString()),strCaption);
				baseService.updateApprPhotoCoor(String.valueOf(workRowId),editText.getText().toString(),strCaption);
				adp.notifyDataSetChanged();
				captionText.setSelection(0);
				Caption.setText("");
			}
		});
		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				captionText.setSelection(0);
				Caption.setText("");
				dialog.dismiss();
			}
		});
		alertbox.show();
		/*((Button)view.findViewById(R.id.btnUpdate)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lstCaption.remove(pos);
				lstCaption.add(editText.getText().toString());
				baseService.updateAppCoor(String.valueOf(workRowId),editText.getText().toString(),baseService.getpoleId(updateSpn.getSelectedItem().toString()),strCaption);
				baseService.updateApprPhotoCoor(String.valueOf(workRowId),editText.getText().toString(),strCaption);
				adp.notifyDataSetChanged();
				captionText.setSelection(0);
				Caption.setText("");
				lWindow.dismiss();
			}
		});
		((Button)view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captionText.setSelection(0);
				Caption.setText("");
				lWindow.dismiss();
			}
		});

		lWindow.showAtLocation(view, Gravity.CENTER, 20, 20);*/
	}

	private ApprovedCoordinates getApprCoordinate(
			List<ApprovedCoordinates> lstApCoor, String strCaption) {
		// TODO Auto-generated method stub
		ApprovedCoordinates coordinates = new ApprovedCoordinates();
		for(ApprovedCoordinates coor:lstApCoor){
			if(coor.getsCaption().equalsIgnoreCase(strCaption)){
				coordinates = coor;
				break;
			}
		}
		return coordinates;
	}

	private void takePhotoForThisCaption(String strCaption) {
		// TODO Auto-generated method stub
		if(workDescription.getSelectedItemId()>0){
			//			if(spinEatimationType.getSelectedItemId()>0)
			//			{
			edit = Caption.getText().toString();
			if(edit.length()>0){
				if(lstCaption.contains(edit)){
					String root = Environment.getExternalStorageDirectory().toString();
					File imageFileFolder = new File(root + "/GescomPhotos");
					if(!imageFileFolder.exists()){
						imageFileFolder.mkdirs();
					}
					File imageFile = new File(imageFileFolder,"Gescom-"+System.currentTimeMillis()+".jpg");
					imageUri = Uri.fromFile(imageFile);

					Log.e("TAG", imageUri.getPath());
					Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
					startActivityForResult(cameraIntent, PICTURE_RESULT);
				}else{
					Toast.makeText(getApplicationContext(), "Please select caption from caption drop down", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please select caption from caption drop down", Toast.LENGTH_LONG).show();
			}
			/*}
			else
			{
				Toast.makeText(CameraActivity.this, "Please select Estimation type", Toast.LENGTH_SHORT).show();
			}*/
		}
		else{
			Toast.makeText(CameraActivity.this, "Please select project", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout) findViewById(R.id.llayout);
		if(layout.isShown()){
			btnShowHideMap.setText("Show Map");
//			MenuItem menuItem = (MenuItem) findViewById(R.id.menu_show_map);
//			menuItem.setTitle("Show Map");
			if(menuItem != null){
				menuItem.setTitle("Show Map");
			}
			layout.setVisibility(View.GONE);
		}else{
			Toast.makeText(CameraActivity.this, "Use back button provided on screen to go back to menu", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_camera, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_show_map:
			if(workDescription.getSelectedItemId()>0){
				menuItem = item;
				String string = item.getTitle().toString();
				if(string.equalsIgnoreCase("Show Map")){
					item.setTitle("Hide Map");
					LinearLayout layout = (LinearLayout) findViewById(R.id.llayout);
					layout.getLayoutParams().height = (int) height;
					layout.getLayoutParams().width = (int)width;
					layout.requestLayout();
					layout.setVisibility(View.VISIBLE);
					measurementMap();
				}
				if(string.equalsIgnoreCase("Hide Map")){
					btnShowHideMap.setText("Show Map");
					map.clear();
					item.setTitle("Show Map");
					LinearLayout layout = (LinearLayout) findViewById(R.id.llayout);
					layout.setVisibility(View.GONE);
				}
			}
			else {
				Toast.makeText(CameraActivity.this, "Please select the project", Toast.LENGTH_LONG).show();
			}
			break;

		}

		return true;
	}
	public Projectestimates getNextPE(List<Projectestimates> lstPE,
			Projectestimates pe) {
		// TODO Auto-generated method stub
		for(Projectestimates pe1:lstPE){
			if(pe1.getEstimationId() == pe.getEstimationId() && pe1.getTypeOfLine() == pe.getTypeOfLine() && pe1.getLtEstId() == pe.getLtEstId() && pe1.getCoordinatesId() == pe.getCoordinatesId()+1){
				return pe1;
			}
		}
		return null;
	}

	public List<List<LatLng>> getSurveyCoordinateList(){
		List<List<LatLng>> llst = new ArrayList<List<LatLng>>();
		List<LatLng> lst;
		BaseService baseService = new BaseService(CameraActivity.this);
		List<Projectestimates> lstPE = baseService.getProjectEstimationList(workRowId);
		for(Projectestimates pe :lstPE){
			LatLng latLng = getLatLngFromEstimates(pe);
			Projectestimates pe1 = getNextPE(lstPE,pe);
			if(pe1 != null){
				lst = new ArrayList<LatLng>();
				LatLng latLng2 = getLatLngFromEstimates(pe1);
				lst.add(latLng);
				lst.add(latLng2);
				llst.add(lst);
			}

		}
		return llst;
	}

	private void measurementMap() {
		// TODO Auto-generated method stub
		hmACMarker.clear();
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this);
		}
		Marker marker;
		List<ApprovedCoordinates> lstApCoor = baseService.getapprovecoordinates(workRowId);
		if(lstApCoor != null){
			if(lstApCoor.size()>0){
				for(ApprovedCoordinates ac:lstApCoor){
					if(ac.getiPoleType() == 1){
						marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ac.getLatitude()), Double.parseDouble(ac.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)));
						hmACMarker.put(marker, ac);
					}
					else if(ac.getiPoleType() == 2){
						marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ac.getLatitude()), Double.parseDouble(ac.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.transformer)));
						hmACMarker.put(marker, ac);
					}
					else if(ac.getiPoleType() == 4){
						marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ac.getLatitude()), Double.parseDouble(ac.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)));
						hmACMarker.put(marker, ac);
					}
					Polyline polyline = map.addPolyline(new PolylineOptions().addAll(getPrevThisList(ac,lstApCoor)).width(1).color(Color.GREEN).geodesic(true));

					String string = polyline.toString();
					List<LatLng> l = polyline.getPoints();
					String string2 = polyline.getId();
				}

				//					location.setLatitude(12.991050);
				//					location.setLongitude(77.543248);


				/*List<LatLng> ls = new ArrayList<LatLng>();
					LatLng l1 = new LatLng(12.991050,77.543248);
					LatLng l2 = new LatLng(12.991004,77.543616);
					ls.add(l1);
					ls.add(l2);
					map.addPolyline(new PolylineOptions().addAll(ls).width(1).color(Color.RED).geodesic(true));
					if(ls.size() == 2){
						testMap testMap = new testMap(ls.get(0), ls.get(1));
						LatLng newlLatLng = testMap.getLatlng(10);
						map.addMarker(new MarkerOptions().position(newlLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).snippet("testing1").title("testing2"));
					}*/
			}

			/*Location location = new Location(LocationManager.NETWORK_PROVIDER);
				List<List<LatLng>> lt = getSurveyCoordinateList();
				List<LatLng> l2 = lt.get(0);
				List<LatLng> l1 = lt.get(1);
				location.setLatitude(l1.get(0).latitude);
				location.setLongitude(l1.get(0).longitude);
				Location location2 = new Location(LocationManager.NETWORK_PROVIDER);
				location2.setLatitude(l1.get(1).latitude);
				location2.setLongitude(l1.get(1).longitude);
				double val = location.bearingTo(location2);
				double lat1 = Math.toRadians(l1.get(0).latitude);
				 double lon1 = Math.toRadians(l1.get(0).longitude);
				 double lat2 = Math.toRadians(l1.get(1).latitude);
				 double lon2 = Math.toRadians(l1.get(1).longitude);  
				 val = Math.toRadians(val);

				double dLon = Math.toRadians(l1.get(1).longitude-l1.get(0).longitude);
				double brng = Math.atan2(Math.sin(dLon) * Math.cos(lat2),
                        Math.cos(lat1) * Math.sin(lat2) -
                        Math.sin(lat1) * Math.cos(lat2) * 
                        Math.cos(dLon));
//				lat2: =ASIN(SIN(lat1)*COS(d/ER) + COS(lat1)*SIN(d/ER)*COS(brng))

				double lat3 = Math.asin( Math.sin(lat1)*Math.cos(30/6374) + 
			              Math.cos(lat1)*Math.sin(30/6374)*Math.cos(brng) );
				double lon3 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(30/6374)*Math.cos(lat1), 
			                     Math.cos(30/6374)-Math.sin(lat1)*Math.sin(lat3));
				map.addMarker(new MarkerOptions().position(new LatLng(Math.toDegrees(lat3),Math.toDegrees(lon3))));*/
		}


	}

	private List<LatLng> getPrevThisList(ApprovedCoordinates ac,
			List<ApprovedCoordinates> lstApCoor) {
		// TODO Auto-generated method stub
		List<LatLng> latlngs = new ArrayList<LatLng>();
		LatLng latLng = new LatLng(Double.parseDouble(ac.getLatitude()), Double.parseDouble(ac.getLongitude()));
		latlngs.add(latLng);
		for(ApprovedCoordinates apcoor:lstApCoor){
			if(apcoor.getEstimationid() == ac.getEstimationid() && Integer.parseInt(apcoor.getLtEstId()) == Integer.parseInt(ac.getLtEstId()) && apcoor.getCoordinatesId() == (ac.getCoordinatesId()-1)){
				latlngs.add(new LatLng(Double.parseDouble(apcoor.getLatitude()), Double.parseDouble(apcoor.getLongitude())));
				break;
			}
		}
		return latlngs;
	}

	protected void addBitmaptoLayout(Bitmap bitmap) {
		// TODO Auto-generated method stub
		addtoLayout = new ImageView(getApplicationContext());
		addtoLayout.setPadding(10, 05, 10, 05);
		addtoLayout.setImageBitmap(bitmap);
		linLayForImage.addView(addtoLayout);
	}
	protected void onResume()
	{
		super.onResume();
		Log.e("imageUri","HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("imageUri","HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {

			if(data != null && data.getData()!=null){
				imageUri = data.getData();
				Log.e("imageUri",imageUri.getPath());
			}
			try {
				capturedBitmap = decodeUri(imageUri);
				String strFileN = imageUri.getLastPathSegment();
				strImageFileName = strFileN;
				filePath = imageUri.getPath();
				Log.d("Image FileName", strImageFileName);

				String caption = Caption.getText().toString();
				String string = filePath.replace("/mnt/sdcard/GescomPhotos/Gescom-", "");
				string = string.replace(".jpg", "");
				Date date = new Date(Long.parseLong(string));
				captionImage = timestampItAndSave(capturedBitmap, caption,date.toString());

				addtoLayout = new ImageView(getApplicationContext());
				addtoLayout.setPadding(10, 05, 10, 05);
				addtoLayout.setImageBitmap(captionImage);
				linLayForImage.addView(addtoLayout);
				imageLocation = locationManager
						.getLastKnownLocation(locationManager.GPS_PROVIDER);
				strLatitude = String.valueOf(imageLocation.getLatitude());
				strLongitude = String.valueOf(imageLocation.getLongitude());

				imageId = addtoLayout.getId();

				addtoLayout.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						ImageView view = (ImageView) v;
						loadPhoto(view, LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
					}
				});
				countForPhotoId = baseService.getlatestphotoid(workRowId);
				countForPhotoId++;
				setapprovephotocoordinates();
				Caption.setText(null);


			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void copyDatabaseToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {//change
				String currentDBPath = "//data//com.zeal.gov.kar.gescomtesting//databases//gescom.db";// change the xml file path to testing file path
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
		}
	}
	private void copyXML() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {//change
				String xml = "//data//com.zeal.gov.kar.gescomtesting//files//Image.xml";//change the xml file path to testing file path
				String backupDBPath = "//testingbackup//Image.xml";
				File currentDB = new File(data, xml);
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
	class UpdateImage extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				CameraActivity.this);


		@Override protected void onPreExecute() {
			this.dialog.setMessage("Uploading...Please wait");
			dialog.setCancelable(false);
			this.dialog.show();
		}
		@SuppressLint("SdCardPath")
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			Soapproxy proxy = new Soapproxy(getApplicationContext());
			BaseService baseservice = new BaseService(getApplicationContext());//change
			lstapprovedcoordinates = baseservice.getapprovecoordinates(workRowId,"N");//need to add set call for ltestid in getapprovecoordinates method in base service and also in xml
			Iterator<ApprovedCoordinates> itr1 = lstapprovedcoordinates.iterator();
			while(itr1.hasNext()){
				ApprovedCoordinates approvedCoordinates  = itr1.next();

				String res = proxy.coordinatestoserver(approvedCoordinates);

				if(res.equals("success")){
					baseservice.updateApprovedcoordinatesSyncyes(String.valueOf(workRowId));
				}
			}

			lstApprovedPhotoCoor = baseservice.gettableapprovephotocoor(workRowId,"N");	
			Iterator<ApprovedPhotoCoordinates> itr = lstApprovedPhotoCoor
					.iterator();
			while (itr.hasNext()) {
				try {
					ApprovedPhotoCoordinates approvedphotocoordinates = itr.next();
					File imageFile = new File(approvedphotocoordinates.getFileName());
					Uri uri = Uri.fromFile(imageFile);
					Bitmap bitmap = decodeUri(uri);
					String string = approvedphotocoordinates.getFileName().replace("/mnt/sdcard/GescomPhotos/Gescom-", "");
					string = string.replace(".jpg", "");
					Date date = new Date(Long.parseLong(string));
					bitmap = timestampItAndSave(bitmap, approvedphotocoordinates.getPhotoCaption(),date.toString());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);

					byte[] b = baos.toByteArray();
					String imageData = uri.getLastPathSegment()+"@"+approvedphotocoordinates.getCoordinates()+"@ @"+Base64.encodeToString(b, Base64.DEFAULT)+"!";
					approvedphotocoordinates.setImageData(imageData);
					StringTokenizer tokenizer = new StringTokenizer(approvedphotocoordinates.getCoordinates(), ",");
					int count =1;
					while (tokenizer.hasMoreElements()) {
						String check = tokenizer.nextToken();
						if(count==1){
							approvedphotocoordinates.setLatitude(check);  
						}
						if(count==2){
							approvedphotocoordinates.setLongitude(check);
						}
						count++;
					}

					Element estType = new Element("dtEstimationType");
					estType.addContent(new Element("ProjectId").setText(imageData));
					estType.addContent(new Element("ProjectId").setText(""+approvedphotocoordinates.getWorkRowId()));
					estType.addContent(new Element("EstimationId").setText(""+approvedphotocoordinates.getEstimationId()));
					estType.addContent(new Element("LoginId").setText(approvedphotocoordinates.getCapturedBy()));
					estType.addContent(new Element("PoleId").setText(""+approvedphotocoordinates.getCoordinatesId()));
					estType.addContent(new Element("PhotoCaption").setText(approvedphotocoordinates.getPhotoCaption()));
					estType.addContent(new Element("Latitude").setText(approvedphotocoordinates.getLatitude()));
					estType.addContent(new Element("Longitude").setText(approvedphotocoordinates.getLongitude()));
					estType.addContent(new Element("ScaptionDate").setText(approvedphotocoordinates.getCapturedOn()));
					estType.addContent(new Element("ltEstId").setText(String.valueOf(approvedphotocoordinates.getLtEstId())));
					estType.addContent(new Element("TypeOfLine").setText(String.valueOf(approvedphotocoordinates.getTypeOfLine())));
					estType.addContent(new Element("FinalSend")).setText(finalSendStr);
					Document document = new Document();
					document.addContent(estType);
					XMLOutputter xmlOutput = new XMLOutputter();

					// display nice nice
					xmlOutput.setFormat(Format.getPrettyFormat());
					//					buffer.append("</NewDataSet>");
					//					System.out.println("++++++" + buffer);
					try {
						xmlOutput.output(document, openFileOutput("Image.xml", Context.MODE_PRIVATE));
						/*FileOutputStream fos = openFileOutput("RAGHU", Context.MODE_PRIVATE);
						fos.write(buffer.toString().getBytes());*/

					} catch (IOException e) {

						e.printStackTrace();

					}
					copyXML();
					String response = proxy.Imagestoserver(approvedphotocoordinates);
					if(response.equals("success")){
						baseservice.updateApprovedphotocoordinatesSyncyes(String.valueOf(workRowId));
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			String  value =baseservice.isUploadedYes(String.valueOf(workRowId));
			SimpleDateFormat datefarmate = new SimpleDateFormat(
					"dd-MM-yyyy hh:mm:ss");
			Calendar calender = Calendar.getInstance();
			String date = datefarmate.format(calender.getTime());
			if(value.equalsIgnoreCase("Y")){
				baseservice.updateStatusWorkmain(String.valueOf(workRowId),"CMP",String.valueOf(date));
			}
			result = lstapprovedcoordinates.size()+" Coordinates and "+ lstApprovedPhotoCoor.size()+" photos are been uploaded";
			return result;
		}
		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			Toast.makeText(CameraActivity.this, strResult, Toast.LENGTH_LONG).show();
			Intent userMenuIntent = new Intent(CameraActivity.this, UsermenuActivity.class);
			userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(userMenuIntent);
			finish();
		}

	}

	public void setapprovephotocoordinates() {
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this);
		}
		ApprovedPhotoCoordinates approvedPhotoCoordinates = new ApprovedPhotoCoordinates();

		approvedPhotoCoordinates.setLoginId(Appuser.getUserName());
		SimpleDateFormat datefarmate = new SimpleDateFormat(
				"dd-MM-yyyy hh:mm:ss");
		Calendar calender = Calendar.getInstance();
		String date = datefarmate.format(calender.getTime());
		approvedPhotoCoordinates.setCaptionDate(date);
		approvedPhotoCoordinates.setLatitude(String.valueOf(currentLocation.getLatitude()));
		approvedPhotoCoordinates.setLongitude(String.valueOf(currentLocation.getLongitude()));
		approvedPhotoCoordinates.setEstimationId(selectedApprCoor.getEstimationid());

		approvedPhotoCoordinates.setPhotoCaption(edit);
		approvedPhotoCoordinates.setFileName(filePath);
		approvedPhotoCoordinates.setCoordinates(String.valueOf(currentLocation.getLatitude())+","+String.valueOf(currentLocation.getLongitude()));
		approvedPhotoCoordinates.setProjectId(workRowId);
		approvedPhotoCoordinates.setPoleId(selectedApprCoor.getCoordinatesId());
		approvedPhotoCoordinates.setCoordinatesId(selectedApprCoor.getCoordinatesId());
		approvedPhotoCoordinates.setImageData(strImageFileName + "@"
				+ approvedPhotoCoordinates.getLatitude()+","
				+ approvedPhotoCoordinates.getLongitude() + "@ @" + strBase64
				+ "!");
		approvedPhotoCoordinates.setPhotoId(String.valueOf(countForPhotoId));
		approvedPhotoCoordinates.setLtEstId(Integer.valueOf(selectedApprCoor.getLtEstId()));
		approvedPhotoCoordinates.setTypeOfLine(selectedApprCoor.getTypeOfLine());
		lstApprovedPhotoCoor.add(approvedPhotoCoordinates);
		long value = baseService.insertintoapprovephotocoordinates(approvedPhotoCoordinates);

		String response;
		if(value>0){
			response = "success";
			Toast.makeText(getApplicationContext(),"Image saved successfully",Toast.LENGTH_LONG).show();
		}
		else{
			response = "failed";
		}
		//	copyDatabaseToSdcard();

	}

	protected void loadPhoto(ImageView imageView, int width, int height) {
		ImageView tempImageView = imageView;

		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.fullimage,
				(ViewGroup) findViewById(R.id.layoutRoot), false);
		ImageView image = (ImageView) layout.findViewById(R.id.full_image_view);
		image.setImageDrawable(tempImageView.getDrawable());
		imageDialog.setView(layout);
		imageDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		imageDialog.create();
		imageDialog.show();

	}

	private Bitmap timestampItAndSave(Bitmap toEdit, String caption,String time) {
		SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String dateTime = sdate.format(Calendar.getInstance().getTime());
		Bitmap canvasBitmap = toEdit.copy(Bitmap.Config.ARGB_8888, true);
		Canvas imageCanvas = new Canvas(canvasBitmap);
		Paint imagePaint = new Paint();
		imagePaint.setTextAlign(Align.CENTER);
		imagePaint.setTextSize(16f);
		imagePaint.setColor(Color.RED);
		imageCanvas.drawText(time, canvasBitmap.getWidth() / 2,
				(canvasBitmap.getHeight() - 25), imagePaint);
		imageCanvas.drawText(caption, canvasBitmap.getWidth() / 2, 20,
				imagePaint);

		return canvasBitmap;

	}

	public void onLocationChanged(Location location) {
		tvShowCoordinatesP.setText(location.getLatitude() + ","
				+ location.getLongitude());

		currentLocation.setLatitude(location.getLatitude());
		currentLocation.setLongitude(location.getLongitude());
		saveCoordinates.setEnabled(true);//testing

		// TODO Auto-generated method stub
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		if(currMarker!=null){
			currMarker.remove();
		}

		currMarker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation)).title("My Current Location"));
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
		map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
		if(isLocationLoaded){
			map.animateCamera(CameraUpdateFactory.zoomTo(15));
			isLocationLoaded = false;
		}
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(this);
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.btnCapture:
			if(workDescription.getSelectedItemId()>0){
				if(spinEatimationType.getSelectedItemId()>0)
				{
					edit = Caption.getText().toString();
					if(edit.length()>0){
						if(lstCaption.contains(edit)){
							String root = Environment.getExternalStorageDirectory().toString();
							File imageFileFolder = new File(root + "/GescomPhotos");
							if(!imageFileFolder.exists()){
								imageFileFolder.mkdirs();
							}
							File imageFile = new File(imageFileFolder,"Gescom-"+System.currentTimeMillis()+".jpg");
							imageUri = Uri.fromFile(imageFile);

							Log.e("TAG", imageUri.getPath());
							Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
							startActivityForResult(cameraIntent, PICTURE_RESULT);
						}else{
							Toast.makeText(getApplicationContext(), "Please select caption from drop down", Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please select caption from drop down", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(this, "Please select Estimation type", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this, "Please select project", Toast.LENGTH_SHORT).show();
			}
			break;*/
		case R.id.btnSaveCoordinates:
			if(workDescription.getSelectedItemId()>0){
				if(spinEatimationType.getSelectedItemId()>0)
				{
					if(poleType.getSelectedItemPosition()>0){
						edit = Caption.getText().toString();
						if(edit.length()>0 ){
							if(!lstCaption.contains(edit)){

								if(baseService == null){
									baseService = new BaseService(CameraActivity.this);
								}
								//							captureImage.setEnabled(true);
								tvDisplayCoordinates.setText(String.valueOf(currentLocation.getLatitude())+","+String.valueOf(currentLocation.getLongitude()));
								approvedcordinates.setLoginId(Appuser.getUserName());

								int estimationId = baseService.getEstimationIdFromDesc(String.valueOf(workRowId),spinEatimationType.getSelectedItem().toString());
								approvedcordinates.setEstimationid(estimationId);

								int selectedTypeOfline = baseService.getTypeOfLine(String.valueOf(workRowId),spinEatimationType.getSelectedItem().toString());

								approvedcordinates.setLatitude(String.valueOf(currentLocation.getLatitude()));
								approvedcordinates.setLongitude(String.valueOf(currentLocation.getLongitude()));
								countForCoor = baseService.getlastcoordinateid(workRowId,est1,String.valueOf(estimationId),String.valueOf(selectedTypeOfline));
								approvedcordinates.setCoordinatesId(++countForCoor);
								approvedcordinates.setLtEstId(est1);
								lstCaption.add(edit);

								//Caption.setText(null);
								call();
								SimpleDateFormat datefarmate = new SimpleDateFormat(
										"dd-MM-yyyy hh:mm:ss");
								Calendar calender = Calendar.getInstance();
								String date = datefarmate.format(calender.getTime());
								approvedcordinates.setCapturedOn(date);

								//		setSpinner.add(edit);
								approvedcordinates.setsCaption(edit);
								approvedcordinates.setiPoleType(baseService
										.getpoleId(strSelectedPole));
								approvedcordinates.setiPoleId(approvedcordinates.getCoordinatesId());
								approvedcordinates.setProjectId(workRowId);
								approvedcordinates.setTypeOfLine(selectedTypeOfline);
								lstapprovedcoordinates.add(approvedcordinates);

								baseService.insertintoapprovecoordinates(approvedcordinates);
								copyDatabaseToSdcard();
								Caption.setText("");
							}
							else{
								Toast.makeText(getApplicationContext(), "Please Provide different caption", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Please Provide  Caption", Toast.LENGTH_LONG).show();
						}
					}
					else{
						Toast.makeText(this, "Please select pole type", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(this, "Please select Estimation type", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(this, "Please select project", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnSend:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Alert Dialog");
			builder.setMessage("Final send")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finalSendStr = "Y";
					
					UpdateImage updateimage = new UpdateImage();
					updateimage.execute();
					copyDatabaseToSdcard();

				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finalSendStr = "N";
					UpdateImage updateimage = new UpdateImage();
					updateimage.execute();
					copyDatabaseToSdcard();
				}
			});
			AlertDialog alertDialog =  builder.create();
			alertDialog.show();

			/*UpdateImage updateimage = new UpdateImage();
				updateimage.execute();
				copyDatabaseToSdcard();*/
			break;


		default:
			break;
		}

	}

	private void call() {
		adp=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,lstCaption);
		adp.setDropDownViewResource(android.R.layout.simple_list_item_1);
		captionText.setAdapter(adp);
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
	public String getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o.toString();
			}
		}
		return null;
	}

	private  void fetchPriviousData()
	{
		hmGPEMarker.clear();
		Marker marker;
		if(baseService == null){
			baseService = new BaseService(CameraActivity.this);
		}
		htlineList = new ArrayList<Htline>();
		htlineList=baseService.getHtlines(Integer.toString(workRowId));
		htline = new Htline();
		ltlineList=new ArrayList<Ltline>();
		ltlineList=baseService.getLtines(Integer.toString(workRowId));
		transformerList=new ArrayList<Transformer>();
		transformerList=baseService.getTransformers(Integer.toString(workRowId));
		boolean reasult = baseService.isProjectdataAvailable(Integer.toString(workRowId));
		if (reasult) {
			Projectestimates preData = null;
			List<Projectestimates> preValues = baseService
					.getSurveyDetails(Integer.toString(workRowId));
			Iterator<Projectestimates> itPPreValues = preValues.iterator();
			while (itPPreValues.hasNext()) {
				preData = itPPreValues.next();
				if(preData.getTypeOfLine()==1)
				{
					htline=getHtLineByID(preData.getEstimationId());
					htline.setStartLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					htline.setEndLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet(Double.toString(preData.getDistance())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
					hmGPEMarker.put(marker, preData);
				}
				else if(preData.getTypeOfLine()==2)
				{
					ltline=getLtLineById(preData.getEstimationId(), preData.getLtEstId());
					ltline.setStartLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					ltline.setEndLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet(Double.toString(preData.getDistance())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
					hmGPEMarker.put(marker, preData);
				}
				else if(preData.getTypeOfLine()==3)
				{
					transformer=getTransformerById(preData.getEstimationId(), preData.getLtEstId());
					transformer.setLocation(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude())));
					marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(preData.getStartLattitude()),Double.parseDouble(preData.getStartLangtitude()))).title(preData.getCoordinatesCaption()).snippet("0.0").icon(BitmapDescriptorFactory.fromResource(R.drawable.transformer)).draggable(true));
					hmGPEMarker.put(marker, preData);
				}

			}
			if(htline!=null)
				if(!htline.getLocationList().isEmpty()&& htline!=null)
				{
					for(Htline preHt:htlineList)
						if(!preHt.getLocationList().isEmpty())
							map.addPolyline((new PolylineOptions()).addAll(preHt.getLocationList()).width(3).color(Color.BLUE).geodesic(true));

				}
			if(ltline!=null)
				if(!ltline.getLocationList().isEmpty())
				{
					for(Ltline preLt:ltlineList)
						if(!preLt.getLocationList().isEmpty())
							map.addPolyline((new PolylineOptions()).addAll(preLt.getLocationList()).width(3).color(Color.RED).geodesic(true));

				}
			if(preValues.size()>0){
				Projectestimates projectestimates = preValues.get(preValues.size()-1);
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLngFromEstimates(projectestimates), 15.5f));
			}
		}
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
	private Transformer getTransformerById(int htId,int tcId) {
		Transformer transObj = null;
		for (Transformer trans : transformerList) {
			if (trans.getHtId()==htId && trans.getTransId()==tcId) {
				return trans;

			}
		}
		return transObj;
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

	private LatLng getLatLngFromEstimates(Projectestimates projectestimates) {
		// TODO Auto-generated method stub

		return new LatLng(Double.parseDouble(projectestimates.getStartLattitude()), Double.parseDouble(projectestimates.getStartLangtitude()));
	}

	class UIInitialization extends AsyncTask<String, Void, String>{

		private final ProgressDialog dialog = new ProgressDialog(
				CameraActivity.this);

		@Override protected void onPreExecute() {
			this.dialog.setMessage("Loading...Please wait");
			dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					uiInitialIzation();
				}
			});
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
		}

	}
}
