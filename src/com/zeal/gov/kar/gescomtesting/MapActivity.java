package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.map.MultiValueMap;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import com.zeal.gov.kar.gescom.calculation.ProjectEstimation;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.Htline;
import com.zeal.gov.kar.gescom.model.Ltline;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.Transformer;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import com.zeal.gov.kar.gescom.xmlmodel.Estimation;

public class MapActivity extends FragmentActivity implements
		OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener,
		OnInfoWindowClickListener,OnItemSelectedListener,LocationListener,OnMarkerDragListener{
	
	public static final double MARKER_DRAG_RESTRICTION_DISTANCE = 50.0;
	private GoogleMap mMap;	
	private Point p;
	private TableRow tblRowBend,trPhaseSelection,trTapBend,trLayingMethod;
	private List<Htline> htlineList;
	private List<Ltline> ltlineList;
	private List<Transformer> transformerList;
	private Htline htline;
	private int isBend;
	private Ltline ltline;
	private Transformer transformer;
	private BaseService baseService;
	private MultiValueMap discpMap;
	private String projectName,isTap,strLayingMethod,str,str1;
	private CheckBox chkIsBend,chKIsTap;
	private RadioGroup rbBendPart,rbTapPart;
	private ImageView addtoLayout;
	private LinearLayout linLayForImage;
	private ArrayList<ApprovedPhotoCoordinates> mainImageList;
	private Spinner spinItem, spinMaterial,spinEstDestcription,spinLtPhase,spinLayingMethod;
	private List<String> itemList, materialList, projectList,descriptionList,ltPhaseList,layingMethodList;
	private ArrayAdapter<String> projectAdapter, itemAdapter, materialAdapter,descriptionAdapter,ltPhaseAdapter,layingMethodAdapter;
	private EditText edtxtDescription;
	private HashMap<String, Integer> projectData;
	private HashMap<String,String> lineMaterial,tcMaterial;
	private List<String> lineMaterialList,tcMaterialList;
	private static final int IMAGECODE = 100;
	private Marker currMarker;
	private Location currLocation;
	private boolean isLocationLoaded =true;
	private LocationManager service;
	private TextView tvCurrCoor,tvMethodlaying,tvPhase;
	private Map<Marker,Projectestimates> hmMarker;
	private Projectestimates pEst;
	private Circle currCircle;
	private boolean isHTLine = false,isLTLine = false,isTC = false,isShowAlertShown = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		/*	copyDatabaseToApplication();
		copyDatabaseToSdcard();
	*/
		discpMap = new MultiValueMap();
		mainImageList = new ArrayList<ApprovedPhotoCoordinates>();
		baseService = new BaseService(MapActivity.this);
		Intent input = getIntent();
		projectName = input.getStringExtra("projectname");
		setMaterials();
		
		hmMarker=new HashMap<Marker, Projectestimates>();
		tvCurrCoor = (TextView) findViewById(R.id.tvCurrCoor);
		//projectName="Cat-4 work1 for Fartabad section as on 21/05/2013";
		
		String status = "NEW";
		projectData = (HashMap<String, Integer>) baseService
				.getProjectforSavecordinates(status,Appuser.getUserName());
		projectList = new ArrayList<String>();
		projectList.add("Select Project");
		Iterator<Entry<String, Integer>> projectIterator = projectData
				.entrySet().iterator();
		while (projectIterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) projectIterator.next();
			projectList.add(pairs.getKey().toString());
		}
	      
		
		
		descriptionList = new LinkedList<String>();
		descriptionList.add("Select Description");
		descriptionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, descriptionList);
		
		
		materialList=new LinkedList<String>();
		materialList.add("--Select--");
		materialList.add("Rabbit Conductor");
		materialList.add("Plan Conductor");
		materialList.add("High conductivity");
		materialAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materialList);
		
		htlineList = new ArrayList<Htline>();
		htlineList=baseService.getHtlines(Integer.toString(projectData.get(projectName)));
		htline = new Htline();
		ltlineList=new ArrayList<Ltline>();
		ltlineList=baseService.getLtines(Integer.toString(projectData.get(projectName)));
		for(Ltline lt : ltlineList)
		{
			System.out.println(lt.getHtId() +" and "+lt.getLtId());
		}
		
		if(ltlineList==null)
		{
			ltline=new Ltline();
			ltline.setHtId(1);
			ltline.setDescription("Single Estimation");
			ltline.setLltId(1);
			ltlineList.add(ltline);
		}
		transformerList=new ArrayList<Transformer>();
		transformerList=baseService.getTransformers(Integer.toString(projectData.get(projectName)));
		if (mMap == null) {

			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			if (mMap != null) {
				setUpMap();
				fetchPriviousData();
			}
		}
		service = (LocationManager) getSystemService(LOCATION_SERVICE);
		service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, this);
		
	}

	private void setUpMap() {

		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//mMap.setMyLocationEnabled(true);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);
		
		itemList = new LinkedList<String>();
		
	boolean CategoryOne = baseService.isProjectACableEstimation(projectName, 1);
	/*if(CategoryOne){
		itemList.add("Select Line Type");
		itemList.add("Transformer");
	}else{*/
		itemList.add("Select Line Type");
		itemList.add("HT Line");
		itemList.add("LT Line");
		itemList.add("Transformer");
//	}
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemList);
          
		ltPhaseList=new ArrayList<String>();
		
	//	ltPhaseList.add(0, "Select phase");
		//ltPhaseList.add("1 ph, 2 wire");
		//ltPhaseList.add("1 ph, 3 wire");
		//ltPhaseList.add("3 ph, 4 wire");
		//ltPhaseList.add("3 ph, 5 wire");
		ltPhaseAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ltPhaseList);
        layingMethodList = new ArrayList<String>();
        layingMethodList.add("Select Method");
        layingMethodList.add("Manual");
        layingMethodList.add("Horizantal Drill");
        layingMethodAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,layingMethodList);
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
  private void setMaterials()
  {
	  lineMaterial=new HashMap<String, String>();
		lineMaterialList=new LinkedList<String>();
		lineMaterial.put("ACSR conductor", "4043");
		lineMaterial.put("Rabbit Conductor", "4007");
		lineMaterial.put("Plan Conductor","4044");
		lineMaterial.put("High conductivity","4045");
		lineMaterialList.add("ACSR conductor");
		lineMaterialList.add("Rabbit Conductor");
		boolean cableEst = baseService.isProjectACableEstimation(projectName,8);
	    boolean AbcCable = baseService.isProjectACableEstimation(projectName,10);
	    boolean Category9 = baseService.isProjectACableEstimation(projectName, 9);
		if(cableEst){
			lineMaterialList.clear();			
			lineMaterial.put("11 kV Class 3X 95 Sq.mm XLPE UG Cable", "4120");
			lineMaterial.put("11 kV Class 3X 240 Sq.mm XLPE UG Cable", "4112");
			lineMaterial.put("11 kV Class 3X 400 Sq.mm XLPE UG Cable", "4093");
			lineMaterialList.add("11 kV Class 3X 95 Sq.mm XLPE UG Cable");
			lineMaterialList.add("11 kV Class 3X 240 Sq.mm XLPE UG Cable");
			lineMaterialList.add("11 kV Class 3X 400 Sq.mm XLPE UG Cable");
		
		}else if(AbcCable){
			lineMaterialList.add("11 kv 3X 95 Sq.mm ABC Cable");
			// Need to Change the item id based on Xml
			lineMaterial.put("11 kv 3X 95 Sq.mm ABC Cable", "5400");
			lineMaterial.put("1.1 KV 3X 95 Sq mm ABC Cable", "5401");
					}
		else if(Category9){
			lineMaterialList.add("11 kV Class 3X 95 Sq.mm XLPE UG Cable");
			lineMaterialList.add("11 kV Class 3X 240 Sq.mm XLPE UG Cable");
			lineMaterialList.add("11 kV Class 3X 400 Sq.mm XLPE UG Cable");
			lineMaterial.put("11 kV Class 3X 95 Sq.mm XLPE UG Cable", "4120");
			lineMaterial.put("11 kV Class 3X 240 Sq.mm XLPE UG Cable", "4112");
			lineMaterial.put("11 kV Class 3X 400 Sq.mm XLPE UG Cable", "4093");
		}
		
		lineMaterialList.add(0, "Select material");
		tcMaterial=new HashMap<String, String>();
		tcMaterialList=new LinkedList<String>();
		tcMaterial.put("25 KVA", "4031");
		tcMaterial.put("63 KVA", "4075");
		tcMaterial.put("100 KVA", "4076");
		tcMaterial.put("250 KVA", "4077");
		tcMaterialList.add("25 KVA");
		tcMaterialList.add("63 KVA");
		tcMaterialList.add("100 KVA");
		tcMaterialList.add("250 KVA");
		tcMaterialList.add(0, "Select material");
  }
  
  @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, this);
}
  
  public void onPause()
  {
	  super.onPause();
	  service.removeUpdates(this);
  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		//	showAlert(point,false);
	}

	@Override
	public void onMapClick(LatLng pLatLng) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
//		Toast.makeText(MapActivity.this, "You can proceed-"+marker.getTitle(), Toast.LENGTH_LONG).show();
		LatLng thisMarkerLocation = marker.getPosition();
		Location markerLocation = service.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(markerLocation!=null){
			if(marker.getTitle().equalsIgnoreCase("My Current Location")){
		markerLocation.setLatitude(thisMarkerLocation.latitude);
		markerLocation.setLongitude(thisMarkerLocation.longitude);
		LatLng currLocation = new LatLng(this.currLocation.getLatitude(), this.currLocation.getLongitude());
//		Toast.makeText(MapActivity.this, markerLocation.getLatitude()+":"+markerLocation.getLongitude(), Toast.LENGTH_LONG).show();
//		Toast.makeText(MapActivity.this, "marker location:"+thisMarkerLocation.latitude+"::"+thisMarkerLocation.longitude+"\ncurr Location:"+currLocation.latitude+"::"+currLocation.longitude, Toast.LENGTH_LONG).show();
		float dist = this.currLocation.distanceTo(markerLocation);
//		Toast.makeText(MapActivity.this, "distance-::"+dist, Toast.LENGTH_LONG).show();
		if(dist < 1){
			showAlert(thisMarkerLocation, false);
		}
		else{
//			Toast.makeText(MapActivity.this, "marker location:"+thisMarkerLocation.latitude+"::"+thisMarkerLocation.longitude+"\ncurr Location:"+currLocation.latitude+"::"+currLocation.longitude, Toast.LENGTH_LONG).show();
			Toast.makeText(MapActivity.this, "Its not your current location, Do you want to proceed anyway..?", Toast.LENGTH_LONG).show();
		}
			}
		}
		return false;
	}
	private void popUpwindow() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View input = inflater.inflate(R.layout.popup, null);

		final PopupWindow pw = new PopupWindow(input,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		linLayForImage = (LinearLayout) input.findViewById(R.id.linLayForImage);
		Iterator<ApprovedPhotoCoordinates> itImage = mainImageList.iterator();
		while (itImage.hasNext()) {

			ApprovedPhotoCoordinates imageDetails = itImage.next();
			byte[] decodedString = Base64.decode(imageDetails.getImageData(),
					Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);

			addtoLayout = new ImageView(getApplicationContext());
			addtoLayout.setPadding(02, 05, 02, 05);
			addtoLayout.setImageBitmap(decodedByte);
			linLayForImage.addView(addtoLayout);
		}

		pw.showAtLocation(input, Gravity.CENTER, 20, 20);

		Button cancelButton = (Button) input.findViewById(R.id.dismiss);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IMAGECODE && resultCode == RESULT_OK) {
			ArrayList<ApprovedPhotoCoordinates> datas = (ArrayList<ApprovedPhotoCoordinates>) data.getSerializableExtra("ImageList");
			Iterator<ApprovedPhotoCoordinates> itImage = datas.iterator();
			while (itImage.hasNext()) {
				mainImageList.add(itImage.next());
			}

			System.out.println(datas.size());
		}
	}
	private void showAlert(LatLng points,boolean formHt) {
		isBend=0;isTap="N";
		isShowAlertShown = true;
		final LatLng point = points;
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		LayoutInflater li = getLayoutInflater();
		View v = li.inflate(R.layout.alertlayout, null);
		alertbox.setView(v);
		spinEstDestcription=(Spinner) v.findViewById(R.id.materialDescription);
		spinLtPhase=(Spinner) v.findViewById(R.id.phasespinner);
		spinLtPhase.setAdapter(ltPhaseAdapter);
		spinLtPhase.setOnItemSelectedListener(this);
		spinEstDestcription.setAdapter(descriptionAdapter);
		spinItem = (Spinner) v.findViewById(R.id.asicccodespinner);
		spinItem.setAdapter(itemAdapter);
		
		tblRowBend=(TableRow) v.findViewById(R.id.trRGs);
		spinItem.setOnItemSelectedListener(this);
		tvPhase = (TextView)v.findViewById(R.id.textView8);
		
		final boolean cableEst = baseService.isProjectACableEstimation(projectName,8);
		boolean category9 = baseService.isProjectACableEstimation(projectName,9);
		spinLayingMethod = (Spinner)v.findViewById(R.id.layingMethodspinner);
		tvMethodlaying = (TextView)v.findViewById(R.id.textviewLaying);
		if(cableEst||category9){
		spinLayingMethod.setAdapter(layingMethodAdapter);
		spinLayingMethod.setOnItemSelectedListener(this);
		if(spinLayingMethod.getSelectedItemId()> 0){
			
		}
		}else{
			spinLayingMethod.setVisibility(View.INVISIBLE);
			tvMethodlaying.setVisibility(View.INVISIBLE);
		}
		rbBendPart=(RadioGroup) v.findViewById(R.id.bendpart);
		rbTapPart = (RadioGroup) v.findViewById(R.id.tappart);
		spinMaterial = (Spinner) v.findViewById(R.id.umitqtyspinner);
		spinMaterial.setAdapter(materialAdapter);
		spinMaterial.setOnItemSelectedListener(this);
		chkIsBend=(CheckBox) v.findViewById(R.id.isBend);
		chKIsTap=(CheckBox) v.findViewById(R.id.isTap);
		trPhaseSelection = (TableRow) v.findViewById(R.id.trPhaseSelection);
		trTapBend = (TableRow) v.findViewById(R.id.trTapBend);
		trLayingMethod = (TableRow) v.findViewById(R.id.trLayingMethod);
		rbTapPart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rbtnNormalTap:
					isTap = "Y";
					break;

				case R.id.rbtnIntermediateTap:
					isTap = "I";
					break;
				default:
					isTap = "N";
					break;
				}
			}
		});
		chKIsTap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(chkIsBend.isChecked()){
					chkIsBend.setChecked(false);
				}
				if(((CheckBox)v).isChecked())
				{
					rbTapPart.setVisibility(View.VISIBLE);
				    tblRowBend.setVisibility(View.VISIBLE);
				    rbBendPart.setVisibility(View.INVISIBLE);
				}
				else
				{
					rbTapPart.setVisibility(View.INVISIBLE);
				    tblRowBend.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		rbBendPart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId) {
	            case R.id.guyset:
	            	isBend=1;
	                break;
	            case R.id.studpole:
	                isBend=2;
	                break;
	            
	            }
			}
		});
		chkIsBend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(chKIsTap.isChecked()){
					chKIsTap.setChecked(false);
				}
			if(((CheckBox)v).isChecked())
				{
					rbBendPart.setVisibility(View.VISIBLE);
				    tblRowBend.setVisibility(View.VISIBLE);
				    rbTapPart.setVisibility(View.INVISIBLE);
				}
				else
				{
					rbBendPart.setVisibility(View.INVISIBLE);
				    tblRowBend.setVisibility(View.INVISIBLE);
				}
			}
		});
		edtxtDescription = (EditText) v.findViewById(R.id.QuantityInput);
		alertbox.setMessage("Location"); // Message to be displayed
		
		alertbox.setPositiveButton("Add",
				new DialogInterface.OnClickListener() {
			
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method
				isShowAlertShown = false;
 if(spinEstDestcription.getSelectedItemId()>0){
	
		 if(!edtxtDescription.getText().toString().isEmpty()){
			
				if(!chkIsBend.isChecked())
				{
					isBend=0;
				}

				if(spinItem.getSelectedItem().equals("HT Line"))
				{
					 if(spinMaterial.getSelectedItemId()>0){
					boolean Category9 = baseService.isProjectACableEstimation(projectName, 9);
					if(cableEst){
						if(spinLayingMethod.getSelectedItemId()>0){

							strLayingMethod = spinLayingMethod.getSelectedItem().toString();
							str = strLayingMethod.substring(0,1);
							System.out.println(str);
							if(descriptionList.isEmpty())
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
							else
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						}	
						else{
							//Toast.makeText(MapActivity.this,"Please Select Laying Method",Toast.LENGTH_LONG).show();
							str1  = "Manual";
							str = "M";
							if(descriptionList.isEmpty())
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
							else
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						}
					}	else if(Category9){
                         if(spinMaterial.getSelectedItemId()>2){
						if(spinLayingMethod.getSelectedItemId()>0){

							strLayingMethod = spinLayingMethod.getSelectedItem().toString();
							str = strLayingMethod.substring(0,1);
							System.out.println(str);
							if(descriptionList.isEmpty())
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
							else
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						}	
						else{
							//Toast.makeText(MapActivity.this,"Please Select Laying Method",Toast.LENGTH_LONG).show();
							
							str1  = "Manual";
							str = "M";
							if(descriptionList.isEmpty())
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
							else
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						}}else{
							str = "";
							if(descriptionList.isEmpty())
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
							else
								addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						
						}
					
						
					}
					
					else  {
						if(descriptionList.isEmpty())
							addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
						else
							addHtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap);
					}
					
					 }else{
						 Toast.makeText(MapActivity.this, "Please select Material", Toast.LENGTH_LONG).show();
					 }
					 }
					 





				else if(spinItem.getSelectedItem().equals("LT Line"))
				{
					if(spinMaterial.getSelectedItemId()>0){
					if(spinLtPhase.getSelectedItemId()>0){
					
						//if(spinLayingMethod.getSelectedItemId()>0){
							
								if(ltlineList.isEmpty())
									;
								//addHtlineCoordinates(edtxtDescription.getText().toString(),(int)spinItem.getSelectedItemId(),point);
								else
									addLtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap,(int)spinLtPhase.getSelectedItemId());
					}
					else{
						if(ltlineList.isEmpty())
							;
						//addHtlineCoordinates(edtxtDescription.getText().toString(),(int)spinItem.getSelectedItemId(),point);
						else
							addLtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap,3);
					}
							//}
						
					/*else{
						if(ltlineList.isEmpty())
							;
						//addHtlineCoordinates(edtxtDescription.getText().toString(),(int)spinItem.getSelectedItemId(),point);
						else
							addLtlineCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),isBend,spinMaterial.getSelectedItem().toString(),isTap,(int)spinLtPhase.getSelectedItemId());
					}*/
					}else{
						 Toast.makeText(MapActivity.this, "Please select Material", Toast.LENGTH_LONG).show();
					 }
					}
			 
 
				else if(spinItem.getSelectedItem().equals("Transformer"))
				{
					if(spinMaterial.getSelectedItemId()>0){
						int workRowId = baseService.getWorkRowId(projectName);
						
						boolean CategoryOne = baseService.isProjectACableEstimation(projectName, 1);
						if(CategoryOne){
							int count = baseService.getEstimationCoordinates(workRowId);
							if(count>0){
							Toast.makeText(MapActivity.this,"Coordinates already Taken", Toast.LENGTH_LONG).show();	
							}else{
							addTransformerCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),spinMaterial.getSelectedItem().toString(),isBend);
							}
							}else{
							addTransformerCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),spinMaterial.getSelectedItem().toString(),isBend);
						}
					
				}else{
					int workRowId = baseService.getWorkRowId(projectName);
					
					boolean CategoryOne = baseService.isProjectACableEstimation(projectName, 1);
					if(CategoryOne){
						int count = baseService.getEstimationCoordinates(workRowId);
						if(count>0){
						Toast.makeText(MapActivity.this,"Coordinates already Taken", Toast.LENGTH_LONG).show();	
						}else{
					addTransformerCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),"25 KVA",isBend);
				}
						}else{
							addTransformerCoordinates(edtxtDescription.getText().toString(),point,spinEstDestcription.getSelectedItem().toString(),"25 KVA",isBend);
						}
					}
				}

//				dialog.dismiss();
			}else{
				Toast.makeText(MapActivity.this,"Please enter description",Toast.LENGTH_LONG).show();
				}
			
 }else{
	 Toast.makeText(MapActivity.this,"Please select description",Toast.LENGTH_LONG).show(); 
 }
			}
			});
				alertbox.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						isShowAlertShown = false;
					}

				});
				
		// show the alert box will be swapped by other code later
	    
		alertbox.show();
		
	}
	private Htline getHtLine(String description) {
		Htline htObj = null;
		for (Htline ht : htlineList) {
			if (ht.getDescription().equalsIgnoreCase(description)) {
				return ht;
				
			}
		}
		return htObj;
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
	private Transformer getTransformer(String description) {
		Transformer transObj = null;
		for (Transformer trans : transformerList) {
			if (trans.getDescription().equalsIgnoreCase(description)) {
				return trans;
				
			}
		}
		return transObj;
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
	
	private Ltline getLtLine(String description) {
		Ltline ltObj = null;
		for (Ltline lt : ltlineList) {
			if (lt.getDescription().equalsIgnoreCase(description)) {
				return lt;
				
			}
		}
		return ltObj;
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
	private void addTransformerCoordinates(String description,LatLng point,String transDescription,String material,int isBend)
	{
		transformer=getTransformer(transDescription);
	    transformer.setLocation(point);
	    Projectestimates peRow = new Projectestimates();
		peRow.setCoordinatesCaption(description);
		peRow.setDistance(0.0);
		peRow.setNoofCurves(isBend);
		peRow.setStartLangtitude(Double.toString(point.longitude));
		peRow.setStartLattitude(Double.toString(point.latitude));
		peRow.setTypeOfLine(3);
		peRow.setWorkRowId(projectData.get(projectName));
		peRow.setIsTaping("N");
		peRow.setLtEstId(transformer.getTransId());
		peRow.setMaterialId(Integer.parseInt(tcMaterial.get(material)));
		int count=baseService.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),"3",Integer.toString(transformer.getHtId()),Integer.toString(transformer.getTransId()));
		peRow.setCoordinatesId(count+1);
		peRow.setEstimationId(transformer.getHtId());
		//peRow.setLayingMethod(str);
		updateDatabase(peRow);
		transformer.setMarkerOptions(new MarkerOptions().position(point).title(edtxtDescription.getText().toString()).snippet("0.0").icon(BitmapDescriptorFactory.fromResource(R.drawable.transformer)).draggable(true));
		Marker marker = mMap.addMarker(transformer.getMarkerOptions());
		hmMarker.put(marker, peRow);
	}
	private void addHtlineCoordinates(String description,LatLng point,String htDescription,int isBend,String material,String isTap)
	{//String description,LatLng point,String transDescription
		double distance=0.0;
		if(getHtLine(htDescription)==null)
		{
		htline = new Htline();
		htline.setStartLocation(point);
		htline.setEndLocation(point);
		Projectestimates peRow = new Projectestimates();
		peRow.setCoordinatesCaption(description);
		peRow.setDistance(0.0);
		peRow.setNoofCurves(isBend);
		peRow.setStartLangtitude(Double.toString(point.longitude));
		peRow.setStartLattitude(Double.toString(point.latitude));
		peRow.setTypeOfLine(1);
		peRow.setWorkRowId(projectData.get(projectName));
		peRow.setIsTaping(isTap);
		peRow.setMaterialId(Integer.parseInt(lineMaterial.get(material)));
		peRow.setCoordinatesId(1);
		peRow.setLtEstId(0);
		peRow.setLtPhase(0);
		peRow.setEstimationId(htline.getHtId());
		peRow.setLayingMethod(str);
		updateDatabase(peRow);
		htline.setMarkerOptions(new MarkerOptions().position(point).snippet("0.0").title(edtxtDescription.getText().toString()).draggable(true));
		mMap.addPolyline((new PolylineOptions()).addAll(htline.getLocationList()).width(5).color(Color.BLUE).geodesic(true));

		Marker marker = mMap.addMarker(htline.getMarkerOptions()); 
		hmMarker.put(marker, peRow);
		htlineList.add(htline);
		
		}
		else
		{
			
			htline=getHtLine(htDescription);
			htline.setStartLocation(point);
			Projectestimates peRow = new Projectestimates();
			peRow.setCoordinatesCaption(description);
			if (htline.getEndLocation()!=null) {
			peRow.setDistance(calculateDistance(htline.getEndLocation(), point));
		    distance=calculateDistance(htline.getEndLocation(), point);
			htline.setEndLocation(point);
			}
			else
			{peRow.setDistance(0.0);}
			htline.setEndLocation(point);
			peRow.setNoofCurves(isBend);
			peRow.setStartLangtitude(Double.toString(point.longitude));
			peRow.setStartLattitude(Double.toString(point.latitude));
			peRow.setTypeOfLine(1);
			peRow.setWorkRowId(projectData.get(projectName));
			peRow.setIsTaping(isTap);
			peRow.setLtEstId(0);
			peRow.setLtPhase(0);
			peRow.setMaterialId(Integer.parseInt(lineMaterial.get(material)));
			int count=baseService.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),"1",Integer.toString(htline.getHtId()),"0");
			peRow.setCoordinatesId(count+1);
			peRow.setEstimationId(htline.getHtId());
			peRow.setLayingMethod(str);
			updateDatabase(peRow);
			htline.setMarkerOptions(new MarkerOptions().position(point).title(edtxtDescription.getText().toString()).snippet(Double.toString(distance)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
			if(!htline.getLocationList().isEmpty())
				{
					mMap.addPolyline((new PolylineOptions()).addAll(htline.getLocationList()).width(5).color(Color.BLUE).geodesic(true));
					Marker marker = mMap.addMarker(htline.getMarkerOptions());
					hmMarker.put(marker, peRow);
				}
					 
		}
		
	}
	
	private void addLtlineCoordinates(String description,LatLng point,String ltDescription,int isBend,String material,String isTap,int ltPhase)
	{
		ltline=getLtLine(ltDescription);
		double distance=0.0;
		ltline.setStartLocation(point);
		Projectestimates peRow = new Projectestimates();
		peRow.setCoordinatesCaption(description);
		if (ltline.getEndLocation()!=null) {
			peRow.setDistance(calculateDistance(ltline.getEndLocation(), point));
			distance=calculateDistance(ltline.getEndLocation(), point);
			ltline.setEndLocation(point);
		}
		else
		{peRow.setDistance(0.0);}
		ltline.setEndLocation(point);
		peRow.setNoofCurves(isBend);
		peRow.setStartLangtitude(Double.toString(point.longitude));
		peRow.setStartLattitude(Double.toString(point.latitude));
		peRow.setTypeOfLine(2);
		peRow.setWorkRowId(projectData.get(projectName));
		peRow.setIsTaping(isTap);
		peRow.setLtPhase(ltPhase);
		peRow.setLtEstId(ltline.getLtId());
		peRow.setMaterialId(Integer.parseInt(lineMaterial.get(material)));
		int count=baseService.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),"2",Integer.toString(ltline.getHtId()),Integer.toString(ltline.getLtId()));
		peRow.setCoordinatesId(count+1);
		peRow.setEstimationId(ltline.getHtId());
	//	peRow.setLayingMethod(str);
		updateDatabase(peRow);
		
		
		ltline.setMarkerOptions(new MarkerOptions().position(point).title(edtxtDescription.getText().toString()).snippet(Double.toString(distance)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pole)).draggable(true));
		if(!ltline.getLocationList().isEmpty())
		{
			mMap.addPolyline((new PolylineOptions()).addAll(ltline.getLocationList()).width(5).color(Color.RED).geodesic(true));
			Marker marker = mMap.addMarker(ltline.getMarkerOptions());
			hmMarker.put(marker, peRow);
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private void updateMultiDescriptionSpinner(int pos) {
		descriptionList.clear();
		List list;
		descriptionList.add("Select");
		if(pos>0)
		{
			if(baseService == null){
				baseService = new  BaseService(MapActivity.this);
			}
				HashMap<String,Object> hm = baseService.getEstimationDescriptionMultiValueMap(Integer.toString(projectData.get(projectName)),Integer.toString(pos));
				discpMap = (MultiValueMap) hm.get("MultiValueMap");
				descriptionList.addAll((List<String>) hm.get("List"));
			System.out.println(discpMap);
		/*	Iterator<Entry<String, String>> discMapIt = discpMap.entrySet().iterator();
			while (discMapIt.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) discMapIt.next();

				list = (List) discpMap.get(pairs.getKey());
				for (int j = 0; j < list.size(); j++) {
					descriptionList.add(list.get(j).toString());
				}			
			}*/
			descriptionAdapter.notifyDataSetChanged();
			System.out.println(descriptionList);
			if (!discpMap.isEmpty()) {
				spinEstDestcription.setEnabled(true);
			}
			
		}
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.menu_settings:
        	if (mainImageList.size() > 0) {
				UploadImage imageUploader = new UploadImage();
				imageUploader.execute("");
			}
        	else
        	{
        		Toast.makeText(MapActivity.this, "Please take photos", Toast.LENGTH_LONG).show();
        	}
        	
            break;
        case R.id.menu_ImageView:
        	if (mainImageList.size() > 0) {
        	popUpwindow();
        	}
        	else
        	{
        		Toast.makeText(MapActivity.this, "Please take photos", Toast.LENGTH_LONG).show();
        	}
        	 break;
        case R.id.menu_CapturePhotos:
        	captureImage();
           break;
        }
 
        return true;
    }
	@Override
	public void onInfoWindowClick(final Marker marker) {
		// TODO Auto-generated method stub
		if(marker.isInfoWindowShown()){
			marker.hideInfoWindow();
		}
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.listview_popup, null); 
		final PopupWindow window = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
		ListView listView = (ListView) view.findViewById(R.id.lvPopup);
		List<String> lst = new ArrayList<String>();
//		lst.add("Edit This Point");
		final Projectestimates projectestimates = hmMarker.get(marker);
		if(projectestimates.getTypeOfLine() == 1){
			lst.add("Delete This Point");
			lst.add("Add HT/LT/TC Point");
			lst.add("Cancel");
		}
		else if(projectestimates.getTypeOfLine() == 2){
			if(projectestimates.getNoofCurves() == 1 || projectestimates.getNoofCurves() == 2){
				lst.add("Delete This Point");
				lst.add("Add LT Point");
				lst.add("Cancel");
			}
			else {
				lst.add("Delete This Point");
				lst.add("Cancel");
			}
		}
		else if(projectestimates.getTypeOfLine() == 3){
			lst.add("Delete This Point");
			lst.add("Cancel");
		}
		pEst = projectestimates;
		ArrayAdapter<String> options = new ArrayAdapter<String>(MapActivity.this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, lst);
		listView.setAdapter(options);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selectedView = arg0.getItemAtPosition(arg2).toString();
				if(selectedView.equalsIgnoreCase("Edit This Point")){
					editThisPoint(projectestimates);
				}
				else if(selectedView.equalsIgnoreCase("Delete This Point")){
					deleteThisPoint(projectestimates);
				}
				else if(selectedView.contains("Add")){
					saveToThisPoint(projectestimates,marker);
				}
				else if(selectedView.equalsIgnoreCase("Cancel")){
					
				}
//				Toast.makeText(getApplicationContext(), "You have selected "+selectedView, Toast.LENGTH_LONG).show();
				window.dismiss();
			}
		});
		window.showAtLocation(view, Gravity.CENTER, 20, 20);
	
		
		/*for (Htline ht : htlineList) {
			for (LatLng point : ht.getLocationList()) {
				if(point.equals(marker.getPosition()))
						{
					       
					        System.out.println(">>>"+ht.getHtId());
					        showAlert(marker.getPosition(),true);
						}
			}
		}*/
		/*for(Ltline lt: ltlineList)
		{
			for(LatLng point : lt.getLocationList())
			{
				if(point.equals(marker.getPosition()))
				{
					 System.out.println(">>>"+lt.getHtId() +"and" +lt.getLtId());
					 
				}
			}
		}*/
		
		if(marker.getTitle().equalsIgnoreCase("My Current Location")){

			/*//TODO Auto-generated method stub
//			Toast.makeText(MapActivity.this, "You can proceed-"+marker.getTitle(), Toast.LENGTH_LONG).show();
			LatLng thisMarkerLocation = marker.getPosition();
			Location markerLocation = service.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			markerLocation.setLatitude(thisMarkerLocation.latitude);
			markerLocation.setLongitude(thisMarkerLocation.longitude);
			LatLng currLocation = new LatLng(this.currLocation.getLatitude(), this.currLocation.getLongitude());
//			Toast.makeText(MapActivity.this, markerLocation.getLatitude()+":"+markerLocation.getLongitude(), Toast.LENGTH_LONG).show();
//			Toast.makeText(MapActivity.this, "marker location:"+thisMarkerLocation.latitude+"::"+thisMarkerLocation.longitude+"\ncurr Location:"+currLocation.latitude+"::"+currLocation.longitude, Toast.LENGTH_LONG).show();
			float dist = this.currLocation.distanceTo(markerLocation);
//			Toast.makeText(MapActivity.this, "distance-::"+dist, Toast.LENGTH_LONG).show();
			if(dist < 1){
				showAlert(thisMarkerLocation, false);
			}
			else{
//				Toast.makeText(MapActivity.this, "marker location:"+thisMarkerLocation.latitude+"::"+thisMarkerLocation.longitude+"\ncurr Location:"+currLocation.latitude+"::"+currLocation.longitude, Toast.LENGTH_LONG).show();
				Toast.makeText(MapActivity.this, "Its not your current location, Do you want to proceed anyway..?", Toast.LENGTH_LONG).show();
			}*/
		}	
	}

	protected void saveToThisPoint(Projectestimates projectestimates, Marker marker) {
		// TODO Auto-generated method stub
		if(projectestimates.getTypeOfLine()== 1){
			Toast.makeText(MapActivity.this, "Add LT/TC Point", Toast.LENGTH_LONG).show();
			for (Htline ht : htlineList) {
				for (LatLng point : ht.getLocationList()) {
					if(point.equals(marker.getPosition()))
							{
						       
						        System.out.println(">>>"+ht.getHtId());
						        showAlert(marker.getPosition(),true);
							}
				}
			}
		}
		else if(projectestimates.getTypeOfLine() == 2){
			for(Ltline lt: ltlineList)
			{
				for(LatLng point : lt.getLocationList())
				{
					if(point.equals(marker.getPosition()))
					{
						if(pEst.getNoofCurves()== 1 || pEst.getNoofCurves()==2){
							showAlert(marker.getPosition(),true);
						}
						else{
							Toast.makeText(MapActivity.this, "Sorry, you can take on cut points only", Toast.LENGTH_LONG).show();
						}
						 System.out.println(">>>"+lt.getHtId() +"and" +lt.getLtId());
						 
					}
				}
			}
		}
		/*else if(projectestimates.getTypeOfLine() == 3){
			showAlert(marker.getPosition(), true);
		}*/
		else{
			Toast.makeText(MapActivity.this, "Sorry you cannot add ", Toast.LENGTH_LONG).show();
		}
	}

	protected void deleteThisPoint(final Projectestimates projectestimates) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
		pEst = projectestimates;
		alert.setTitle("Warning");
		alert.setIcon(R.drawable.dialog_warning);
//		AlertDialog alert = new AlertDialog.Builder(MapActivity.this).create();
		final List<Projectestimates> lstest = baseService.getSurveyDetails(Integer.toString(projectData.get(projectName)));
		List<Projectestimates> lstPrevNextEst = getPrevNextProjectEstimates(pEst,lstest);
		Log.i("List SIze", ""+lstPrevNextEst.size());
		if(lstPrevNextEst.size() == 1){
			//need to delete two estimates if it as child values
			//need to delete one estimates if it as no child values
			alert.setMessage("You are about to delete the point which cannot be reverted back, are you sure you want to delete?");
			if(pEst.getCoordinatesId() == 1){
				alert.setMessage("You are about to delete the point which is refered by other points for this line and cannot be reverted back, are you sure you want to delete?");
			}
			alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(pEst.getCoordinatesId() == 1){
						//need to delete two estimates if it as child values
						BaseService service = new BaseService(MapActivity.this);
						int count = service.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()));
						if(pEst.getTypeOfLine() == 1){
							for(int i= pEst.getCoordinatesId();i<=count;i++){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}else if(pEst.getTypeOfLine() == 2 ){
							for(int i= pEst.getCoordinatesId();i<=count;i++){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}else if(pEst.getTypeOfLine() == 3){
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}
						dialog.dismiss();
						restartActivity();
					}
					else{
						//need to delete one estimates if it as no child values
						BaseService service = new BaseService(MapActivity.this);
						int count = service.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()));
						if(pEst.getTypeOfLine() == 1){
							for(int i= pEst.getCoordinatesId();i<=count;i++){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}else if(pEst.getTypeOfLine() == 2 ){
							for(int i= pEst.getCoordinatesId();i<=count;i++){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}else if(pEst.getTypeOfLine() == 3){
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
							if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
								int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
								Log.i("Deleted Row", ""+effectedrow);
							}
						}
						dialog.dismiss();
						restartActivity();
					}
					
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
		}
		else if(lstPrevNextEst.size() == 2){
			alert.setMessage("You are about to delete the point which is refered by other points for this line and cannot be reverted back, are you sure you want to delete?");
			alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					BaseService service = new BaseService(MapActivity.this);
					int count = service.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),String.valueOf(projectestimates.getTypeOfLine()),String.valueOf(projectestimates.getEstimationId()),String.valueOf(projectestimates.getLtEstId()));
					if(pEst.getTypeOfLine() == 1){
						for(int i= pEst.getCoordinatesId();i<=count;i++){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}else if(pEst.getTypeOfLine() == 2 ){
						for(int i= pEst.getCoordinatesId();i<=count;i++){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}else if(pEst.getTypeOfLine() == 3){
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}
					restartActivity();
					dialog.dismiss();
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
		}
		else if(lstPrevNextEst.size()== 0){
			alert.setMessage("You are about to delete the point which cannot be reverted back, are you sure you want to delete?");
		alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					BaseService service = new BaseService(MapActivity.this);
					int count = service.getCoordinateidforMultiples(Integer.toString(projectData.get(projectName)),String.valueOf(projectestimates.getTypeOfLine()),String.valueOf(projectestimates.getEstimationId()),String.valueOf(projectestimates.getLtEstId()));
					if(pEst.getTypeOfLine() == 1){
						for(int i= pEst.getCoordinatesId();i<=count;i++){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}else if(pEst.getTypeOfLine() == 2 ){
						for(int i= pEst.getCoordinatesId();i<=count;i++){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),String.valueOf(pEst.getTypeOfLine()),String.valueOf(pEst.getEstimationId()),String.valueOf(pEst.getLtEstId()),String.valueOf(i));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}else if(pEst.getTypeOfLine() == 3){
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"2",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
						if(service.isProjectEstimatesAvailable(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()))){
							int effectedrow = service.deleteProjectEstimatesParticularRows(Integer.toString(projectData.get(projectName)),"3",String.valueOf(pEst.getEstimationId()));
							Log.i("Deleted Row", ""+effectedrow);
						}
					}
					restartActivity();
					dialog.dismiss();
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
		}
	}

	protected void editThisPoint(Projectestimates projectestimates) {
	
	}

	@Override
	public void onItemSelected(AdapterView<?> adp, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		int id = adp.getId();
		switch (id) {
			case R.id.asicccodespinner:
				/*if(isShowAlertShown){
					tblRowBend.setVisibility(View.INVISIBLE);
					if(pos == 1){
						trPhaseSelection.setVisibility(View.INVISIBLE);
						trTapBend.setVisibility(View.VISIBLE);
						if(chKIsTap.isChecked()){
							chKIsTap.setChecked(false);
						}
						if(chkIsBend.isChecked()){
							chkIsBend.setChecked(false);
						}
					}
					else if(pos == 2){
						trPhaseSelection.setVisibility(View.VISIBLE);
						trTapBend.setVisibility(View.VISIBLE);
						if(chKIsTap.isChecked()){
							chKIsTap.setChecked(false);
						}
						if(chkIsBend.isChecked()){
							chkIsBend.setChecked(false);
						}
					}
					else if(pos == 3){
						trPhaseSelection.setVisibility(View.INVISIBLE);
						trTapBend.setVisibility(View.INVISIBLE);
						tblRowBend.setVisibility(View.INVISIBLE);
					}
				}
				boolean cableEst = baseService.isProjectACableEstimation(projectName);
				if(cableEst){
					if(pos == 2){
						lineMaterialList.clear();
						lineMaterialList.add("Select material");
						lineMaterialList.add("ACSR conductor");
						lineMaterialList.add("Rabbit Conductor");

					}
					else{
						lineMaterialList.clear();
						lineMaterialList.add("Select material");
						for(String material: lineMaterial.keySet())
						{
							lineMaterialList.add(material);
						}
					}
				}*/
				updateMultiDescriptionSpinner(pos);
				if(pos == 0){
					spinLayingMethod.setSelection(0);
					spinLtPhase.setSelection(0);
					spinEstDestcription.setSelection(0);
					spinMaterial.setSelection(0);
					edtxtDescription.setText("");
				}
				if(pos == 1){
					boolean AbcCable = baseService.isProjectACableEstimation(projectName, 10);
					boolean category1 = baseService.isProjectACableEstimation(projectName, 1);
					
					if(category1){
						/*tcMaterialList.clear();
						tcMaterialList.add("25 KVA");
						tcMaterialList.add("63 KVA");
						tcMaterialList.add("100 KVA");
						tcMaterialList.add("250 KVA");
						tcMaterialList.add(0, "Select material");
						materialList.clear();
						for(String data : tcMaterialList)
						{
							materialList.add(data);
						}
						materialAdapter.notifyDataSetChanged();*/
						spinItem.setAdapter(itemAdapter);
						spinItem.setSelection(3,true);
						Toast.makeText(MapActivity.this,"Category 1 supports only for transformer",Toast.LENGTH_LONG).show();
						
					}else{
						if(AbcCable){
							lineMaterialList.clear();
							setMaterials();
							/*tvMethodlaying.setVisibility(View.VISIBLE);
							spinLayingMethod.setVisibility(View.VISIBLE);
							spinLtPhase.setVisibility(View.INVISIBLE);
							tvPhase.setVisibility(View.INVISIBLE);
							chkIsBend.setVisibility(View.VISIBLE);
							chKIsTap.setVisibility(View.VISIBLE);
							rbTapPart.setVisibility(View.VISIBLE);
							rbBendPart.setVisibility(View.VISIBLE);*/
							spinLtPhase.setEnabled(false);
							tvPhase.setEnabled(false);
							/*tvMethodlaying.setEnabled(false);
							spinLayingMethod.setEnabled(false);*/
							chKIsTap.setEnabled(true);
							chkIsBend.setEnabled(true);
							spinLayingMethod.setEnabled(false);
							tvMethodlaying.setEnabled(false);
							spinMaterial.setSelection(0);
							spinLtPhase.setSelection(0);
							spinLayingMethod.setSelection(0);
							tblRowBend.setVisibility(View.INVISIBLE);
							edtxtDescription.setText("");
							spinEstDestcription.setSelection(0);
							chkIsBend.setChecked(false);
							chKIsTap.setChecked(false);
						}else{
						lineMaterialList.clear();
						setMaterials();
						/*tvMethodlaying.setVisibility(View.VISIBLE);
						spinLayingMethod.setVisibility(View.VISIBLE);
						spinLtPhase.setVisibility(View.INVISIBLE);
						tvPhase.setVisibility(View.INVISIBLE);
						chkIsBend.setVisibility(View.VISIBLE);
						chKIsTap.setVisibility(View.VISIBLE);
						rbTapPart.setVisibility(View.VISIBLE);
						rbBendPart.setVisibility(View.VISIBLE);*/
						
						/*tvMethodlaying.setEnabled(false);
						spinLayingMethod.setEnabled(false);*/
						chKIsTap.setEnabled(true);
						chkIsBend.setEnabled(true);
						spinLayingMethod.setEnabled(true);
						tvMethodlaying.setEnabled(true);
						spinMaterial.setSelection(0);
						spinLtPhase.setSelection(0);
						spinLayingMethod.setSelection(0);
						spinLtPhase.setEnabled(false);
						tvPhase.setEnabled(false);
						tblRowBend.setVisibility(View.INVISIBLE);
						edtxtDescription.setText("");
						spinEstDestcription.setSelection(0);
						chkIsBend.setChecked(false);
						chKIsTap.setChecked(false);
						
					}
						
						materialList.clear();
						for(String data : lineMaterialList)
						{
							materialList.add(data);
						}
						materialAdapter.notifyDataSetChanged();
					}

				}
				if(pos == 2){
					boolean category1 = baseService.isProjectACableEstimation(projectName, 1);
					if(category1){
						Toast.makeText(MapActivity.this,"Category 1 supports only for transformer",Toast.LENGTH_LONG).show();
						spinItem.setAdapter(itemAdapter);
						spinItem.setSelection(3,true);
						
					}
					else{
						boolean AbcCable = baseService.isProjectACableEstimation(projectName, 10);
						boolean Category9 = baseService.isProjectACableEstimation(projectName, 9);
						if(AbcCable){
							lineMaterialList.clear();
							lineMaterialList.add("Select material");
							lineMaterialList.add("ACSR conductor");
							lineMaterialList.add("Rabbit Conductor");
							lineMaterialList.add("1.1 KV 3X 95 Sq mm ABC Cable");
							ltPhaseList.clear();
							ltPhaseList.add("Select phase");
							ltPhaseList.add("1 ph, 2 wire");
							ltPhaseList.add("1 ph, 3 wire");
							ltPhaseList.add("3 ph, 4 wire");
							ltPhaseList.add("3 ph, 5 wire");
						    ltPhaseAdapter.notifyDataSetChanged();
						    spinLtPhase.setEnabled(true);
							tvPhase.setEnabled(true);
							chKIsTap.setEnabled(true);
							chkIsBend.setEnabled(true);
							spinLayingMethod.setEnabled(false);
							tvMethodlaying.setEnabled(false);
							spinMaterial.setSelection(0);
							spinLtPhase.setSelection(0);
							spinLayingMethod.setSelection(0);
							tblRowBend.setVisibility(View.INVISIBLE);
							edtxtDescription.setText("");
							spinEstDestcription.setSelection(0);
							chkIsBend.setChecked(false);
							chKIsTap.setChecked(false);
						}
						else{
						lineMaterialList.clear();
						lineMaterialList.add("Select material");
						lineMaterialList.add("ACSR conductor");
						lineMaterialList.add("Rabbit Conductor");
						ltPhaseList.clear();
						ltPhaseList.add("Select phase");
						ltPhaseList.add("1 ph, 2 wire");
						ltPhaseList.add("1 ph, 3 wire");
						ltPhaseList.add("3 ph, 4 wire");
						ltPhaseList.add("3 ph, 5 wire");
					    ltPhaseAdapter.notifyDataSetChanged();
					    /*tvMethodlaying.setVisibility(View.VISIBLE);
						spinLayingMethod.setVisibility(View.VISIBLE);
						chkIsBend.setVisibility(View.VISIBLE);
						chKIsTap.setVisibility(View.VISIBLE);
						rbTapPart.setVisibility(View.VISIBLE);
						rbBendPart.setVisibility(View.VISIBLE);*/
					    spinLtPhase.setEnabled(true);
						tvPhase.setEnabled(true);
						chKIsTap.setEnabled(true);
						chkIsBend.setEnabled(true);
						spinLayingMethod.setEnabled(false);
						tvMethodlaying.setEnabled(false);
						spinMaterial.setSelection(0);
						spinLtPhase.setSelection(0);
						spinLayingMethod.setSelection(0);
						tblRowBend.setVisibility(View.INVISIBLE);
						edtxtDescription.setText("");
						spinEstDestcription.setSelection(0);
						chkIsBend.setChecked(false);
						chKIsTap.setChecked(false);
					}

						materialList.clear();
						for(String data : lineMaterialList)
						{
							materialList.add(data);
						}
					materialAdapter.notifyDataSetChanged();
					}
					/*tvPhase.setVisibility(View.VISIBLE);
					spinLtPhase.setVisibility(View.VISIBLE);*/
					
				}
				if(pos==3)
				{
					/*spinLtPhase.setVisibility(View.INVISIBLE);
					tvPhase.setVisibility(View.INVISIBLE);
					tvMethodlaying.setVisibility(View.INVISIBLE);
					spinLayingMethod.setVisibility(View.INVISIBLE);
					chkIsBend.setVisibility(View.INVISIBLE);
					chKIsTap.setVisibility(View.INVISIBLE);
					rbTapPart.setVisibility(View.INVISIBLE);
					rbBendPart.setVisibility(View.INVISIBLE);*/
					spinLtPhase.setEnabled(false);
					tvPhase.setEnabled(false);
					chKIsTap.setChecked(false);
					chkIsBend.setChecked(false);
					chKIsTap.setEnabled(false);
					chkIsBend.setEnabled(false);
					spinLayingMethod.setEnabled(false);
					tvMethodlaying.setEnabled(false);
					tblRowBend.setVisibility(View.INVISIBLE);
					spinMaterial.setSelection(0);
					spinLtPhase.setSelection(0);
					spinLayingMethod.setSelection(0);
					edtxtDescription.setText("");
					spinEstDestcription.setSelection(0);
					chkIsBend.setChecked(false);
					chKIsTap.setChecked(false);
					
				materialList.clear();
					for(String data : tcMaterialList)
					{
						materialList.add(data);
					}
				materialAdapter.notifyDataSetChanged();
				}
			/*	else 
				{
					
					materialList.clear();
					for(String data : lineMaterialList)
					{
						materialList.add(data);
					}
				materialAdapter.notifyDataSetChanged();
				}*/
				break;
			case R.id.umitqtyspinner:
				
				
					boolean AbcCable = baseService.isProjectACableEstimation(projectName, 10);
					if(AbcCable){
						if(pos == 3){
						spinLtPhase.setSelection(4);
						spinLtPhase.setEnabled(false);
						
					}
					
				}else {
					if(spinItem.getSelectedItemId() == 1 ||spinItem.getSelectedItemId() == 3){
					spinLtPhase.setSelection(0);
					spinLtPhase.setEnabled(false);
					}else{
						spinLtPhase.setSelection(0);
						spinLtPhase.setEnabled(true);
					}
					 boolean Category9 = baseService.isProjectACableEstimation(projectName, 9);
					if(Category9){
						if(pos>2){
							spinLayingMethod.setVisibility(View.VISIBLE);
							tvMethodlaying.setVisibility(View.VISIBLE);
						}
						else{
							spinLayingMethod.setVisibility(View.INVISIBLE);
							tvMethodlaying.setVisibility(View.INVISIBLE);
						
						}
					}
				} 
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	private double calculateDistance(LatLng StartPoint, LatLng EndPoint) {
	    double lat1 = StartPoint.latitude;
	    double lat2 = EndPoint.latitude;
	    double lon1 = StartPoint.longitude;
	    double lon2 = EndPoint.longitude;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    return  Math.round((6366000 * c )* 100.0)/ 100.0;
	    //  return 6366000 * c;
	}
	private  void fetchPriviousData()
	{
		boolean reasult = baseService.isProjectdataAvailable(Integer.toString(projectData.get(projectName)));
		if (reasult) {
			Projectestimates preData = null;
			List<Projectestimates> preValues = baseService
					.getSurveyDetails(Integer.toString(projectData
							.get(projectName)));
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
		if (baseService.isProjectphotosAvailable(Integer.toString(projectData
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
						Toast.makeText(MapActivity.this,
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
	private void updateDatabase(Projectestimates peRow ) {
	
		
	     long result = baseService.addToprojectestimation(peRow);

		if (result > 0) {
			Toast.makeText(MapActivity.this,
					"Location details saved successfully", Toast.LENGTH_SHORT)
					.show();
			copyDatabaseToSdcard();
			
		}
	}
	
	private void captureImage()
	{
		Intent imageIntent = new Intent(MapActivity.this,
				CameraView.class);
		String projectID = Integer.toString(projectData
				.get(projectName));
		imageIntent.putExtra("Projectid", projectID);
		imageIntent.putExtra("workstatus","BS");
		startActivityForResult(imageIntent, IMAGECODE);
	}
	private void copyDatabaseToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			Toast.makeText(MapActivity.this, "Latest Db Copied to sddcard", Toast.LENGTH_LONG).show();
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
	
	
	
	class UploadImage extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(MapActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Uploading Images...Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			Soapproxy proxy = new Soapproxy(MapActivity.this);
			Iterator<ApprovedPhotoCoordinates> imagarIterator = mainImageList
					.iterator();

			while (imagarIterator.hasNext()) {
				ApprovedPhotoCoordinates imageDetails = imagarIterator.next();

				Element estType = new Element("dtEstimationType");
				estType.addContent(new Element("WorkItem").setText(""
						+ imageDetails.getWorkRowId()));
				estType.addContent(new Element("SubWorkItem").setText("1"));
				estType.addContent(new Element("WorkStatus").setText(""
						+ imageDetails.getWorkStarus()));
				estType.addContent(new Element("PhotoCaption")
						.setText(imageDetails.getPhotoCaption()));
				estType.addContent(new Element("Latitude").setText(imageDetails
						.getLatitude()));
				estType.addContent(new Element("Longitude")
						.setText(imageDetails.getLongitude()));
				estType.addContent(new Element("StartPoint").setText(""));
				estType.addContent(new Element("EndPoint").setText(""));
				estType.addContent(new Element("ImageData").setText(""
						+ imageDetails.getImageData()));
				Document document = new Document();
				document.addContent(estType);
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				
				try {

					xmlOutput.output(
							document,
							openFileOutput("ImageDetails.xml",
									Context.MODE_PRIVATE));
			

				} catch (IOException e) {

					e.printStackTrace();

				}
			
				proxy.uploadWorkImages(imageDetails);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			Toast.makeText(MapActivity.this,
					"Images are uploaded to server.", Toast.LENGTH_SHORT)
					.show();
			baseService.updateWorkMain(
					Integer.toString(projectData.get(projectName)), "CCO");
			Intent moveBack = new Intent(MapActivity.this,
					UsermenuActivity.class);
			Toast.makeText(MapActivity.this,
					"Work Survey Done Successfully", Toast.LENGTH_LONG).show();
			moveBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(moveBack);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		currLocation = location;
		tvCurrCoor.setText("Current Coordinates:"+location.getLatitude()+","+location.getLongitude());
		tvCurrCoor.setBackgroundColor(Color.TRANSPARENT);
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		if(currMarker!=null){
			currMarker.remove();
		}
		if(currCircle != null){
			currCircle.remove();
		}
		
		currMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation)).title("My Current Location"));
		currCircle = mMap.addCircle(new CircleOptions().center(currMarker.getPosition()).radius(MARKER_DRAG_RESTRICTION_DISTANCE).visible(true).strokeColor(Color.YELLOW).strokeWidth(1f).fillColor(0x20FFEB00));//0x40FFEB00 20(%) FFEB00(colorcode) semi transparent
		mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
		mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
		if(isLocationLoaded){
			mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
			isLocationLoaded = false;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	//Marker dragging functionality starts from here
	
	@Override
	public void onMarkerDrag(Marker marker) {/*

		String title = marker.getTitle();
		String workrowId = Integer.toString(projectData.get(projectName));
		 baseService = new BaseService(MapActivity.this);
		 List<Projectestimates> lstest = baseService.getSurveyDetails(Integer.toString(projectData.get(projectName)));
		 Projectestimates est = getThisMarkerDetails(title,lstest);
		 List<Projectestimates> lstPrevNextEst = getPrevNextProjectEstimates(est,lstest);
		 if(lstPrevNextEst.size() == 1){
			 // here need to update distance in db for next coorid and also for marker
			 Projectestimates pe = lstPrevNextEst.get(0);
			 double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe.getStartLattitude()), Double.valueOf(pe.getStartLangtitude())));
			 drawPolyLine(pe, marker, est);
			 if(est.getCoordinatesId() == 1){//update 2 rows one for coordinates and other for distance
				 marker.setSnippet("Distance: 0.0");
				 est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
				 est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
				 baseService.updateProjectEstimates(est,workrowId,est.getDistance());
				 baseService.updateProjectEstimates(pe, workrowId, dist);
			 }
			 else{//update 1 rows
				 marker.setSnippet("Distance: "+dist);
				 est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
				 est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
				 baseService.updateProjectEstimates(est, workrowId, dist);
			 }
		 }else if(lstPrevNextEst.size() == 2){
			 // here need to update distance in db for this and next coorid and also for marker
			 Projectestimates pe1= lstPrevNextEst.get(0);
			 Projectestimates pe2 = lstPrevNextEst.get(1);
			 drawPolyLine(pe1, marker, est);
			 drawPolyLine(pe2, marker, est);
			 if((est.getCoordinatesId()+1) == pe1.getCoordinatesId()){
				 
				 double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe1.getStartLattitude()), Double.valueOf(pe1.getStartLangtitude())));
				 baseService.updateProjectEstimates(pe1, workrowId, dist);
				 double dist1 = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe2.getStartLattitude()), Double.valueOf(pe2.getStartLangtitude())));
				 est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
				 est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
				 baseService.updateProjectEstimates(est, workrowId, dist1);
			 }
			 else{
				 double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe1.getStartLattitude()), Double.valueOf(pe1.getStartLangtitude())));
				 double dist1 = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe2.getStartLattitude()), Double.valueOf(pe2.getStartLangtitude())));
				 est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
				 est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
				 baseService.updateProjectEstimates(est, workrowId, dist);
				 baseService.updateProjectEstimates(pe2, workrowId, dist1);
			 }
		 } 
	
		 if(est.getTypeOfLine() == 3){
			 est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
			 est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
			 baseService.updateProjectEstimates(est, Integer.toString(projectData.get(projectName)),est.getDistance());
		 }
	
		
	*/}

	private List<LatLng> getCoordinateList(
			Projectestimates projectestimates, Marker est) {
		// TODO Auto-generated method stub
		List<LatLng> latLngs = new ArrayList<LatLng>();
		latLngs.add(getLatLngFromEstimates(projectestimates));
		latLngs.add(est.getPosition());
		return latLngs;
	}

	private LatLng getLatLngFromEstimates(Projectestimates projectestimates) {
		// TODO Auto-generated method stub
		
		return new LatLng(Double.parseDouble(projectestimates.getStartLattitude()), Double.parseDouble(projectestimates.getStartLangtitude()));
	}

	private List<Projectestimates> getPrevNextProjectEstimates(
			Projectestimates est, List<Projectestimates> lstest) {
		// TODO Auto-generated method stub
		List<Projectestimates> lstPrevNextEst = new ArrayList<Projectestimates>();
		for (Projectestimates projectestimates : lstest) {
			if(projectestimates.getEstimationId() == est.getEstimationId() && projectestimates.getLtEstId() == est.getLtEstId() && projectestimates.getTypeOfLine() == est.getTypeOfLine())
			if(projectestimates.getCoordinatesId() == (est.getCoordinatesId()-1)){
				lstPrevNextEst.add(projectestimates);
			}
			else if(projectestimates.getCoordinatesId() == (est.getCoordinatesId()+1)){
				lstPrevNextEst.add(projectestimates);
			}
		}
		return lstPrevNextEst;
	}

	private Projectestimates getThisMarkerDetails(String title,List<Projectestimates> lstest) {
		// TODO Auto-generated method stub
		for(Projectestimates est:lstest){
			if(est.getCoordinatesCaption().equalsIgnoreCase(title)){
				return est;
			}
		}
		return null;
	}

	@Override
	public void onMarkerDragEnd(final Marker marker) {

		final String title = marker.getTitle();
		final String workrowId = Integer.toString(projectData.get(projectName));
		baseService = new BaseService(MapActivity.this);



		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		LayoutInflater li = getLayoutInflater();
		View v = li.inflate(R.layout.draglayout, null);
		TableRow phaseRow=(TableRow) v.findViewById(R.id.tableRow12);
		spinLtPhase=(Spinner) v.findViewById(R.id.phasespinner);
		spinMaterial = (Spinner) v.findViewById(R.id.umitqtyspinner);
		alertbox.setView(v);
		final List<Projectestimates> lstest = baseService.getSurveyDetails(Integer.toString(projectData.get(projectName)));
		//final Projectestimates est = getThisMarkerDetails(title,lstest);
		final Projectestimates est = hmMarker.get(marker);
		if(est.getTypeOfLine()==1)
		{
	/*		materialList.clear();
			for(String data : tcMaterialList)
			{
				materialList.add(data);
			}
			materialAdapter.notifyDataSetChanged();
		}
		else 
		{*/
			materialList.clear();
			for(String data : lineMaterialList)
			{
				materialList.add(data);
			}
			materialAdapter.notifyDataSetChanged();
			spinMaterial.setAdapter(materialAdapter);
			getPreviousMaterialIndex(est.getMaterialId());
		}

		
		if(est.getTypeOfLine()==2){
			materialList.clear();
			materialList.add("Select material");
			materialList.add("ACSR conductor");
			materialList.add("Rabbit Conductor");

			ltPhaseList.clear();
			ltPhaseList.add("Select phase");
			ltPhaseList.add("1 ph, 2 wire");
			ltPhaseList.add("1 ph, 3 wire");
			ltPhaseList.add("3 ph, 4 wire");
			ltPhaseList.add("3 ph, 5 wire");
			ltPhaseAdapter.notifyDataSetChanged();
			spinLtPhase.setAdapter(ltPhaseAdapter);
			spinLtPhase.setSelection(est.getLtPhase(),true);
			spinMaterial.setAdapter(materialAdapter);
			spinMaterial.setSelection(((est.getMaterialId() == 4043)?1:(((est.getMaterialId() == 4007)?2:0))), true);
//			getPreviousMaterialIndex(est.getMaterialId());
		}
		/*if(est.getTypeOfLine()==3)
		{
			spinLtPhase.setAdapter(materialAdapter);

		}*/
		if(est.getTypeOfLine()==3 || est.getTypeOfLine()==1)
		{
			phaseRow.setVisibility(View.INVISIBLE);
		}

		if(est.getTypeOfLine()==3)
		{
			materialList.clear();
			for(String data : tcMaterialList)
			{
				materialList.add(data);
			}
			materialAdapter.notifyDataSetChanged();
			spinMaterial.setAdapter(materialAdapter);
			getPreviousTcMaterialIndex(est.getMaterialId());
		}
//		spinMaterial.setAdapter(materialAdapter);

		alertbox.setMessage("Update"); // Message to be displayed
		alertbox.setPositiveButton("Change",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method
				if(spinMaterial.getSelectedItemPosition()>0){
					List<Projectestimates> lstPrevNextEst = getPrevNextProjectEstimates(est,lstest);
					if(est.getTypeOfLine()==1 || est.getTypeOfLine()==2){
						if(lstPrevNextEst.size() == 1){
							// here need to update distance in db for next coorid and also for marker
							Projectestimates pe = lstPrevNextEst.get(0);
							double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe.getStartLattitude()), Double.valueOf(pe.getStartLangtitude())));
							drawPolyLine(pe, marker, est);
							if(est.getCoordinatesId() == 1){//update 2 rows one for coordinates and other for distance
								marker.setSnippet("Distance: 0.0");
								est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
								est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
								est.setLtPhase(((est.getTypeOfLine() == 1)?0:((int)spinLtPhase.getSelectedItemPosition())));
								est.setMaterialId(Integer.parseInt(lineMaterial.get(spinMaterial.getSelectedItem().toString())));
								baseService.updateProjectEstimates(est,workrowId,est.getDistance());
								baseService.updateProjectEstimates(pe, workrowId, dist);
								restartActivity();
							}
							else{//update 1 rows
								marker.setSnippet("Distance: "+dist);
								est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
								est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
								est.setLtPhase(((est.getTypeOfLine() == 1)?0:((int)spinLtPhase.getSelectedItemPosition())));
								est.setMaterialId(Integer.parseInt(lineMaterial.get(spinMaterial.getSelectedItem().toString())));
								baseService.updateProjectEstimates(est, workrowId, dist);
								restartActivity();
							}
						}else if(lstPrevNextEst.size() == 2){
							// here need to update distance in db for this and next coorid and also for marker
							Projectestimates pe1= lstPrevNextEst.get(0);
							Projectestimates pe2 = lstPrevNextEst.get(1);
							drawPolyLine(pe1, marker, est);
							drawPolyLine(pe2, marker, est);
							if((est.getCoordinatesId()+1) == pe1.getCoordinatesId()){

								double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe1.getStartLattitude()), Double.valueOf(pe1.getStartLangtitude())));
								baseService.updateProjectEstimates(pe1, workrowId, dist);
								double dist1 = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe2.getStartLattitude()), Double.valueOf(pe2.getStartLangtitude())));
								est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
								est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
								est.setLtPhase(((est.getTypeOfLine() == 1)?0:((int)spinLtPhase.getSelectedItemPosition())));
								est.setMaterialId(Integer.parseInt(lineMaterial.get(spinMaterial.getSelectedItem().toString())));
								baseService.updateProjectEstimates(est, workrowId, dist1);
								restartActivity();
							}
							else{
								double dist = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe1.getStartLattitude()), Double.valueOf(pe1.getStartLangtitude())));
								double dist1 = calculateDistance(marker.getPosition(), new LatLng(Double.valueOf(pe2.getStartLattitude()), Double.valueOf(pe2.getStartLangtitude())));
								est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
								est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
								est.setLtPhase(((est.getTypeOfLine() == 1)?0:((int)spinLtPhase.getSelectedItemPosition())));
								est.setMaterialId(Integer.parseInt(lineMaterial.get(spinMaterial.getSelectedItem().toString())));
								baseService.updateProjectEstimates(est, workrowId, dist);
								baseService.updateProjectEstimates(pe2, workrowId, dist1);
								restartActivity();
							}
						} else if(lstPrevNextEst.size() == 0){
							est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
							est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
							est.setLtPhase(((est.getTypeOfLine() == 1)?0:((int)spinLtPhase.getSelectedItemPosition())));
							est.setMaterialId(Integer.parseInt(lineMaterial.get(spinMaterial.getSelectedItem().toString())));
							baseService.updateProjectEstimates(est, workrowId, 0);
							restartActivity();
						}

					}
					if(est.getTypeOfLine() == 3){
						est.setStartLangtitude(String.valueOf(marker.getPosition().longitude));
						est.setStartLattitude(String.valueOf(marker.getPosition().latitude));
						est.setMaterialId(Integer.parseInt(tcMaterial.get(spinMaterial.getSelectedItem().toString())));
						baseService.updateProjectEstimates(est, Integer.toString(projectData.get(projectName)),est.getDistance());
						restartActivity();
					}
				}else{
					Toast.makeText(MapActivity.this, "Please select material",Toast.LENGTH_LONG).show();
					restartActivity();
				}
			}
		});
		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();
				restartActivity();
			}
		});	

		alertbox.create();
		alertbox.show();
	}

	private void drawPolyLine(Projectestimates lstPrevNextEst,
			Marker marker, Projectestimates est) {
		// TODO Auto-generated method stub
		
		if(est.getTypeOfLine()==1){
			mMap.addPolyline(new PolylineOptions().addAll(getCoordinateList(lstPrevNextEst,marker)).width(2f).color(Color.BLUE));
		}
		else if(est.getTypeOfLine() == 2){
			mMap.addPolyline(new PolylineOptions().addAll(getCoordinateList(lstPrevNextEst,marker)).width(2f).color(Color.RED));
		}
		
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		if(currLocation != null){
			Projectestimates pest = hmMarker.get(marker);
			Marker draggingMarker = marker;
			double dragDistance = calculateDistance(new LatLng(currLocation.getLatitude(), currLocation.getLongitude()), new LatLng(Double.parseDouble(pest.getStartLattitude()), Double.parseDouble(pest.getStartLangtitude())));
			if(dragDistance > MARKER_DRAG_RESTRICTION_DISTANCE){
				Toast.makeText(MapActivity.this, "Sorry, You are not allowed to drag as the pole is not within the my location radius.", Toast.LENGTH_LONG).show();
//				restartActivity();
				BitmapDescriptor bd = (pest.getTypeOfLine() == 3?BitmapDescriptorFactory.fromResource(R.drawable.transformer):BitmapDescriptorFactory.fromResource(R.drawable.pole));
				Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(pest.getStartLattitude()),Double.parseDouble(pest.getStartLangtitude()))).title(marker.getTitle()).snippet(marker.getSnippet()).icon(bd).draggable(true));
				marker.remove();
				hmMarker.remove(marker);
				hmMarker.put(mark, pest);
				
			}
		}
	}
	
	private void restartActivity() {
	    Intent intent = getIntent();
	    finish();
	    startActivity(intent);
	}
	//Marker dragging functionality ends here
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
	 private void getPreviousTcMaterialIndex(int materialId)
	   {
		   Iterator<Entry<String, String>> material = tcMaterial.entrySet().iterator();
			while (material.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) material.next();
				String value=pairs.getValue().toString();
		    	String mat=Integer.toString(materialId);
				System.out.println("<<<<<<Material key>>>>>>"+pairs.getKey()+"<<<<<<Material Value>>>>>>"+pairs.getValue());
				if(value.equalsIgnoreCase(mat))	
				{	
					System.out.println(""+tcMaterialList.indexOf(pairs.getKey()));
					spinMaterial.setSelection(tcMaterialList.indexOf(pairs.getKey()));
				}
				
			}
		
	   }
	
   private void getPreviousMaterialIndex(int materialId)
   {
	   Iterator<Entry<String, String>> material = lineMaterial.entrySet().iterator();
		while (material.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) material.next();
			String value=pairs.getValue().toString();
	    	String mat=Integer.toString(materialId);
			System.out.println("<<<<<<Material key>>>>>>"+pairs.getKey()+"<<<<<<Material Value>>>>>>"+pairs.getValue());
			if(value.equalsIgnoreCase(mat))	
			{	
				System.out.println(""+lineMaterialList.indexOf(pairs.getKey()));
				spinMaterial.setSelection(lineMaterialList.indexOf(pairs.getKey()));
			}
			
		}
	
   }
}
