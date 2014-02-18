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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Base64;
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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.SavecoordDetails;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;

public class Savecoordinates extends Activity implements LocationListener,
		OnItemSelectedListener {
	private Spinner spinProject, spinItem, spinEstDestcription;
	private Point p;
	private Button btnSavecord, savetodatabase, btnImageCapture, btnViewImages,back;
	private EditText edtTxtCaption;
	private CheckBox checkBends;
	private int previousEstId;
	private TextView txtTotalDistance, projectNames;
	private List<SavecoordDetails> details = null;
	private List<String> projectList, itemList, descriptionList;
	private ArrayAdapter<String> projectAdapter, itemAdapter,
			descriptionAdapter;
	private TableLayout tableLayout;
	private Location previousLocation = null;
	private LocationManager locationManager = null;
	private Location loc = null;
	private double totalDistance;
	private Location currentLoc;
	private LinearLayout linLayForImage;
	private static final int IMAGECODE = 100;
	private static final int PREFERENCECODE = 200;
	private LinearLayout layoutChildren;
	private SavecoordDetails rowData;
	private TextView currentCord;
	private ImageView addtoLayout;
	private BaseService baseService;
	private Map<String, String> discpMap;
	private int count,htConductor,ltConductor,transformerType,spanLength,materialId;
	private HashMap<String, Integer> indexMaps;
	private String projectName;
	private int prePos,preDiscpos;
	private ArrayList<ApprovedPhotoCoordinates> mainImageList;
	private HashMap<String, Integer> projectData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savecoordinates);
		baseService = new BaseService(Savecoordinates.this);
		currentCord = (TextView) findViewById(R.id.currentCord);
		mainImageList = new ArrayList<ApprovedPhotoCoordinates>();
		discpMap = new HashMap<String, String>();
		indexMaps = new HashMap<String, Integer>();
		String status = "NEW";
		projectData = (HashMap<String, Integer>) baseService
				.getProjectforSavecordinates(status,Appuser.getUserName());
		count = 0;
		Iterator<Entry<String, Integer>> projectIndex = projectData.entrySet()
				.iterator();
		int index = 1;
		while (projectIndex.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) projectIndex.next();
			indexMaps.put(pairs.getKey().toString(), index);
			index++;
		}
		// remove this
		/*
		 * Parser p =new Parser(Savecoordinates.this); p.parseXml();
		 */
		materialSelected();
		// Location data
		projectNames = (TextView) findViewById(R.id.projectNAME);
		Intent input = getIntent();
		projectName = input.getStringExtra("projectname");
		int data = indexMaps.get(projectName);
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10, 0, this);// 10-second interval,1 meters.

		currentLoc = locationManager
				.getLastKnownLocation(locationManager.GPS_PROVIDER);
		// UI initialization
		spinProject = (Spinner) findViewById(R.id.proceject_spinner);
		btnSavecord = (Button) findViewById(R.id.savecod);
		spinItem = (Spinner) findViewById(R.id.item_spinner);
		edtTxtCaption = (EditText) findViewById(R.id.location);
		checkBends = (CheckBox) findViewById(R.id.bends);
		txtTotalDistance = (TextView) findViewById(R.id.totalDistance);
		spinEstDestcription = (Spinner) findViewById(R.id.spinSavedEstDescriptions);
		back=(Button) findViewById(R.id.LocalBack);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		layoutChildren = (LinearLayout) findViewById(R.id.child);
		tableLayout = (TableLayout) findViewById(R.id.display);
		createTableHeader();
		rowData = new SavecoordDetails();

		btnViewImages = (Button) findViewById(R.id.viewImage);
		btnViewImages.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popUpwindow();
			}
		});
		btnImageCapture = (Button) findViewById(R.id.Imagecapture);
		btnImageCapture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent imageIntent = new Intent(Savecoordinates.this,
						CameraView.class);
				/*
				 * String projectID =
				 * Integer.toString(projectData.get(spinProject
				 * .getSelectedItem().toString()));
				 */
				String projectID = Integer.toString(projectData
						.get(projectName));
				imageIntent.putExtra("Projectid", projectID);
				startActivityForResult(imageIntent, IMAGECODE);
			}
		});
		savetodatabase = (Button) findViewById(R.id.savetoLocal);
		savetodatabase.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						Savecoordinates.this);
				alertDialog.setTitle("Project Details");
		
				alertDialog.setMessage("Do want to this details to Database?");
				alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (mainImageList.size() > 0) {
									UploadImage imageUploader = new UploadImage();
									imageUploader.execute("");
								}

							}
						});
				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
			}
		});

		btnSavecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (spinItem.getSelectedItem().toString().equals("LT-Line")||spinItem.getSelectedItem().toString().equals("HT-Line")||spinItem.getSelectedItem().toString().equals("Transformer")) {
					if (spinEstDestcription.getSelectedItemId() > 0) {

						if(spinItem.getSelectedItem().toString().equalsIgnoreCase("HT-Line")||spinItem.getSelectedItem().toString().equalsIgnoreCase("LT-Line")||spinItem.getSelectedItem().toString().equalsIgnoreCase("Transformer")){
							if(baseService.isEstimationTypeAvailable(Integer.toString(projectData.get(projectName)), "1")>0)
							{
								if(!spinItem.getSelectedItem().toString().equalsIgnoreCase("HT-Line")) htSelectionAlert();
								else showMultipleEstimationAlert(0);
							}
							 else{
								 showMultipleEstimationAlert(0);
						    }
						   
						}
					} else {
						showSettingsAlert();
					}
				} else {
					showSettingsAlert();
				}
			}
		});

		// Spinner initialization
		projectList = new ArrayList<String>();
		projectList.add("Select Project");
		Iterator<Entry<String, Integer>> projectIterator = projectData
				.entrySet().iterator();
		while (projectIterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) projectIterator.next();
			projectList.add(pairs.getKey().toString());
		}
		projectAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, projectList);
		spinProject.setAdapter(projectAdapter);
		spinProject.setOnItemSelectedListener(this);
		itemList = new ArrayList<String>();
		itemList.add("Select Item");
		itemList.add("HT-Line");
		itemList.add("LT-Line");
		itemList.add("Transformer");
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemList);
		spinItem.setAdapter(itemAdapter);
		spinItem.setOnItemSelectedListener(this);

		// Multiple Estimation Descriptions.
		descriptionList = new ArrayList<String>();
		descriptionList.add("Select Description");
		descriptionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, descriptionList);
		spinEstDestcription.setAdapter(descriptionAdapter);
		spinEstDestcription.setOnItemSelectedListener(this);
		spinProject.setSelection(indexMaps.get(projectName));

		spinProject.setVisibility(View.GONE);
		projectNames.setText("Project Name: " + projectName);
		projectNames.setGravity(2);
 		fectchPreviousdata();
		spinEstDestcription.setEnabled(false);
	}
private void htSelectionAlert()
{
	final Map<String, String>  htMap = baseService.getEstimationDescriptionMap(
			Integer.toString(projectData.get(projectName)),Integer.toString(1));
	final List<String> htList=new ArrayList<String>();
	Iterator<Entry<String, String>> htMapIt = htMap.entrySet()
			.iterator();
	while (htMapIt.hasNext()) {
		@SuppressWarnings("rawtypes")
		Map.Entry pairs = (Map.Entry) htMapIt.next();
		htList.add(pairs.getValue().toString());
		
	}
	ArrayAdapter<String> htAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, htList);
    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
    
    alt_bld.setTitle("Select HT-Line");
    alt_bld.setSingleChoiceItems(htAdapter, -1, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {
        	
        	int data=Integer.parseInt(getKeyFromValue(htMap,htList.get(item)));
        	
        	showMultipleEstimationAlert(data);
        	dialog.cancel();
        }
    });
 
    AlertDialog alert = alt_bld.create();
    alert.show();
  
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
		if(requestCode == PREFERENCECODE)
		{
			materialSelected();
			setMaterialId((int)spinItem.getSelectedItemId());
			
		}
 	}

	public void onBackPressed() {
		super.onBackPressed();

	}
private void materialSelected()
{ 
	 PreferenceManager.setDefaultValues(this,R.layout.materialdetails,false);
	 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	 String data=sharedPrefs.getString("Lt-Line","");
	 ltConductor=Integer.parseInt(sharedPrefs.getString("Lt-Line",""));
     htConductor=Integer.parseInt(sharedPrefs.getString("Ht-Line",""));
     transformerType=Integer.parseInt(sharedPrefs.getString("Transformer",""));
     spanLength=Integer.parseInt(sharedPrefs.getString("SpanLenght",""));
}
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(this);
	}

	private void createTableHeader() {
		TableRow Header = new TableRow(this);
		for (int j = 0; j <= 6; j++) {
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
			cell.setTextColor(Color.WHITE);
			if (j == 0) {
				cell.setText("Project Name");
			}
			if (j == 1) {
				cell.setText("Item");
			}
			if (j == 2) {
				cell.setText("Lognitude");
			}
			if (j == 3) {
				cell.setText("Lattitude");
			}
			if (j == 4) {
				cell.setText("Bends");
			}
			if (j == 5) {
				cell.setText("Location");
			}
			if (j == 6) {
				cell.setText("Distance");
			}
			cell.setPadding(6, 4, 6, 4);
			cell.setBackgroundColor(Color.rgb(100, 100, 100));
			Header.addView(cell);
		}
		tableLayout.addView(Header);
	}

	private void addTotable(SavecoordDetails project) {
		TableRow row = new TableRow(this);
		for (int j = 0; j <= 6; j++) {

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
			if (j == 0) {
				cell.setText(project.getProjectName());
			}
			if (j == 1) {
				cell.setText(project.getItemName());
			}
			if (j == 2) {
				cell.setText(Double.toString(project.getlongitute()));
			}
			if (j == 3) {
				cell.setText(Double.toString(project.getLatitute()));
			}
			if (j == 4) {
				if (project.getBends() == 1) {
					cell.setText("Yes");
				} else {
					cell.setText("No");
				}
			}
			if (j == 5) {
				cell.setText(project.getCaption());
			}
			if (j == 6) {
				cell.setText(Double.toString(project.getDistance()));
			}
			cell.setPadding(6, 4, 6, 4);
			row.addView(cell);
		}

		tableLayout.addView(row);
	}

	@SuppressWarnings("static-access")
	private void caculatedDistance() {
		loc = locationManager
				.getLastKnownLocation(locationManager.GPS_PROVIDER);

		Location currentLocation = null;
		float distance = 0;
		if (previousLocation != null) {
			if (loc != null) {
				currentLocation = loc;
				distance = currentLocation.distanceTo(previousLocation);
				rowData.setDistance(Math.round(distance * 100.0) / 100.0);
				rowData.setlongitute(currentLocation.getLongitude());
				rowData.setLatitute(currentLocation.getLatitude());
				previousLocation = currentLocation;
			} else {
				// gps.showSettingsAlert();
			}
		} else {
			if (loc != null) {
				distance = 0;
				currentLocation = loc;
				previousLocation = currentLocation;
				distance = currentLocation.distanceTo(previousLocation);
				rowData.setDistance(Math.round(distance * 100.0) / 100.0);
				rowData.setlongitute(currentLocation.getLongitude());
				rowData.setLatitute(currentLocation.getLatitude());
			} else {
				// gps.showSettingsAlert();
			}
		}
		totalDistance = totalDistance + distance;

  	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Project Details");
		alertDialog.setMessage("Do want to this details to Table?");
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (spinItem.getSelectedItemPosition() != 0) {
							int count = 0;
							if (!baseService.isEstimationAvailable(Integer.toString(projectData.get(projectName)),Integer.toString(spinItem.getSelectedItemPosition()))) {
								ProjectEstimationType petRow = new ProjectEstimationType();
								petRow.setLineId(Integer.toString(spinItem
										.getSelectedItemPosition()));
								petRow.setProjectId(projectData
										.get(projectName));
								petRow.setEstimationId(1);
								petRow.setEstDescription("Single Estimation");
								baseService.addToProjectEstimationType(petRow);
							}
							count = baseService.getCoordinateidforMultiple(
									Integer.toString(projectData
											.get(projectName)),
									Integer.toString(spinItem
											.getSelectedItemPosition()), "1");
							count++;
							caculatedDistance();
							rowData.setItemName(spinItem.getSelectedItem()
									.toString());
							rowData.setEstimationId(1);
							rowData.setCoordinateCount(count);
							if (checkBends.isChecked()) {
								rowData.setBends(1);
							} else {
								rowData.setBends(0);
							}
							rowData.setCaption(edtTxtCaption.getText()
									.toString());
							rowData.setProjectName(spinProject
									.getSelectedItem().toString());
							addTotable(rowData);
							updateDatabase();
							// details.add(rowData);

							txtTotalDistance.setText(Double.toString(Math
									.round(totalDistance * 100.0) / 100.0));

						} else {
							Toast.makeText(Savecoordinates.this,
									"Please Select Item type ",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	public void showMultipleEstimationAlert(final int estid) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Project Details");
		alertDialog.setMessage("Do want to this details to Table?");
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (spinItem.getSelectedItemPosition() != 0) {
							int count = 0;
							if(estid!=0)
							{
								count = baseService.getCoordinateidforMultiple(
										Integer.toString(projectData
												.get(projectName)),
										Integer.toString(spinItem
												.getSelectedItemPosition()),Integer.toString(estid));
								
								count++;
							}
							else{
							count = baseService.getCoordinateidforMultiple(
									Integer.toString(projectData
											.get(projectName)),
									Integer.toString(spinItem
											.getSelectedItemPosition()),
									getKeyFromValue(discpMap,
											spinEstDestcription
													.getSelectedItem()
													.toString()));
							
							count++;
							}
							caculatedDistance();
							rowData.setItemName(spinEstDestcription
									.getSelectedItem().toString());
							rowData.setCoordinateCount(count); 
							if (checkBends.isChecked()) {
								rowData.setBends(1);
							} else {
								rowData.setBends(0);
							}
						    
							rowData.setEstimationId(Integer
									.parseInt(getKeyFromValue(discpMap,
											spinEstDestcription
													.getSelectedItem()
													.toString())));
							if(estid!=0)
							{
								rowData.setEstimationId(estid);
							}
							rowData.setCaption(edtTxtCaption.getText()
									.toString());
							rowData.setProjectName(spinProject
									.getSelectedItem().toString());
							addTotable(rowData);
							updateDatabase();
							// details.add(rowData);
							txtTotalDistance.setText(Double.toString(Math
									.round(totalDistance * 100.0) / 100.0));

						} else {
							Toast.makeText(Savecoordinates.this,
									"Please Select Item type ",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	// To Enable/Disable Child views.
	public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			view.setEnabled(enabled);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_savecoordinates, menu);

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
	   
	   
	private void fectchPreviousdata() {
		boolean reasult = baseService.isProjectdataAvailable(Integer
				.toString(projectData.get(projectName)));
		if (reasult) {
			Projectestimates preData = null;
			List<Projectestimates> preValues = baseService
					.getSurveyDetails(Integer.toString(projectData
							.get(projectName)));
			Iterator<Projectestimates> itPPreValues = preValues.iterator();
			while (itPPreValues.hasNext()) {
				preData = itPPreValues.next();
				SavecoordDetails data = new SavecoordDetails();
				data.setProjectName(spinProject.getSelectedItem().toString());
				data.setCaption(preData.getCoordinatesCaption());
				data.setDistance(preData.getDistance());
				data.setItemName(itemList.get(preData.getTypeOfLine()));
				data.setLatitute(Double.parseDouble(preData.getStartLattitude()));
				data.setlongitute(Double.parseDouble(preData.getStartLangtitude()));
				data.setBends(preData.getNoofCurves());
				data.setEstimationId(preData.getEstimationId());
				previousEstId=preData.getEstimationId();
				addTotable(data);
				totalDistance=totalDistance+preData.getDistance();
			}
			prePos=preData.getTypeOfLine();
			updateMultiDescriptionSpinner(prePos);
			previousLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
			if(previousLocation==null)
			{
				previousLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			}
			spinItem.setSelection(preData.getTypeOfLine());
			preDiscpos=descriptionList.indexOf(discpMap.get(Integer.toString(previousEstId)));
			
			count = 1;
			if(previousLocation!=null)
			{
			previousLocation.setLatitude(Double.parseDouble(preData
					.getStartLattitude()));
			previousLocation.setLongitude(Double.parseDouble(preData
					.getStartLangtitude()));
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
						Toast.makeText(Savecoordinates.this,
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

	@Override
	public void onItemSelected(AdapterView<?> adp, View arg1, int pos, long arg3) {

		int id = adp.getId();
		switch (id) {
		case R.id.proceject_spinner:
			if (pos == 0) {
				enableDisableViewGroup(layoutChildren, false);
			} else {
				enableDisableViewGroup(layoutChildren, true);

			}
			break;
		case R.id.item_spinner:
			if (pos != 0) {
				
				if(prePos==pos)
				{
					updateMultiDescriptionSpinner(pos);
					if(!discpMap.isEmpty())
					{
						count++;
						spinEstDestcription.setSelection(descriptionList.indexOf(discpMap.get(Integer.toString(previousEstId))));
						spinEstDestcription.setEnabled(true);
					}
				}
				else
				{
					previousLocation = null;
					updateMultiDescriptionSpinner(pos);
					if(!discpMap.isEmpty())
					{
						count++;
						spinEstDestcription.setSelection(descriptionList.indexOf(discpMap.get(Integer.toString(previousEstId))));
						spinEstDestcription.setEnabled(true);
					}
				}
				prePos=pos;
				/* setMaterialId(pos);
				if (count == 1) {
					updateMultiDescriptionSpinner();
					if(!discpMap.isEmpty())
					{
						count++;
						spinEstDestcription.setSelection(descriptionList.indexOf(discpMap.get(Integer.toString(previousEstId))));
					}
					else
					{
						
							previousLocation = null;
						
					}
					count++;
				} else {
				
					previousLocation = null;
					
					if (pos == 2 || pos==1) {
						updateMultiDescriptionSpinner();
						previousLocation = null;
						// spinEstDestcription.setEnabled(true);
					} else if (pos != 2) {
						spinEstDestcription.setEnabled(false);
					}
				}
				count++;*/
			}

			break;

		case R.id.spinSavedEstDescriptions:
			if (pos > 0) {
			/*	System.out.println(spinEstDestcription.getSelectedItem()
						.toString());
				System.out.println(getKeyFromValue(discpMap,
						spinEstDestcription.getSelectedItem().toString()));
				if (count <=3) {count++;}
				else{
				previousLocation = null;
				}*/
				if(preDiscpos==pos)
				{
					
				}
				else
				{
					previousLocation = null;
				}
			}

			break;
		}
 	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		currentCord.setText(location.getLatitude() + ","
				+ location.getLongitude());
		if(currentLoc!=null){
		currentLoc.setLatitude(location.getLatitude());
		currentLoc.setLongitude(location.getLongitude());
		}
		else
		{
			currentLoc = locationManager
					.getLastKnownLocation(locationManager.GPS_PROVIDER);
				currentLoc.setLatitude(location.getLatitude());
				currentLoc.setLongitude(location.getLongitude());
				
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

	private void updateDatabase() {
		long result = 0;
		switch ((int)spinItem.getSelectedItemId()) {
		case 1:
			materialId=htConductor;
			break;
		case 2:
			materialId=ltConductor;
			break;
		case 3:
			materialId=transformerType;
			break;
		}
		Projectestimates peRow = new Projectestimates();

		peRow.setCoordinatesCaption(rowData.getCaption());
		peRow.setDistance(rowData.getDistance());
		peRow.setNoofCurves(rowData.getBends());
		peRow.setStartLangtitude(Double.toString(rowData.getlongitute()));
		peRow.setStartLattitude(Double.toString(rowData.getLatitute()));
		peRow.setTypeOfLine(spinItem.getSelectedItemPosition());
		peRow.setWorkRowId(projectData.get(projectName));
		peRow.setIsTaping("NO");
		peRow.setMaterialId(materialId);
		peRow.setCoordinatesId(rowData.getCoordinateCount());
		peRow.setEstimationId(rowData.getEstimationId());
		result = baseService.addToprojectestimation(peRow);

		if (result > 0) {
			Toast.makeText(Savecoordinates.this,
					"Location details saved successfully", Toast.LENGTH_SHORT)
					.show();
		}
	}

	class UploadImage extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				Savecoordinates.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Uploading Images...Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			Soapproxy proxy = new Soapproxy(Savecoordinates.this);
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

				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				// buffer.append("</NewDataSet>");
				// System.out.println("++++++" + buffer);
				try {

					xmlOutput.output(
							document,
							openFileOutput("ImageDetails.xml",
									Context.MODE_PRIVATE));
					/*
					 * FileOutputStream fos = openFileOutput("RAGHU",
					 * Context.MODE_PRIVATE);
					 * fos.write(buffer.toString().getBytes());
					 */

				} catch (IOException e) {

					e.printStackTrace();

				}
				copyXML();
				proxy.uploadWorkImages(imageDetails);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			Toast.makeText(Savecoordinates.this,
					"Images are uploaded to server.", Toast.LENGTH_SHORT)
					.show();
			baseService.updateWorkMain(
					Integer.toString(projectData.get(projectName)), "CCO");
			Intent moveBack = new Intent(Savecoordinates.this,
					UsermenuActivity.class);
			Toast.makeText(Savecoordinates.this,
					"Work Survey Done Successfully", Toast.LENGTH_LONG).show();
			moveBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(moveBack);
		}
	}
	private void setMaterialId(int pos)
	{
		switch (pos) {
		case 1:
			materialId=htConductor;
			break;
		case 2:
			materialId=ltConductor;
			break;
		case 3:
			materialId=transformerType;
			break;
		}
	}
	
	private void copyXML() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String xml = "//data//com.zeal.gov.kar.gescom//files//ImageDetails.xml";
				String backupDBPath = "ImageDetails.xml";
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

	private void popUpwindow() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View input = inflater.inflate(R.layout.popup, null);
		int OFFSET_X = 20;
		int OFFSET_Y = 20;

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

		pw.showAtLocation(input, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y
				+ OFFSET_Y);

		Button cancelButton = (Button) input.findViewById(R.id.dismiss);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}
		});

	}

	public void onWindowFocusChanged(boolean hasFocus) {

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		btnImageCapture.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = location[0];
		p.y = location[1];
	}

	private void updateMultiDescriptionSpinner(int pos) {
		descriptionList.clear();
		
		descriptionList.add("Select");
		discpMap = baseService.getEstimationDescriptionMap(
				Integer.toString(projectData.get(projectName)),
				Integer.toString(pos));
		System.out.println(discpMap);
		Iterator<Entry<String, String>> discMapIt = discpMap.entrySet()
				.iterator();
		while (discMapIt.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) discMapIt.next();
			descriptionList.add(pairs.getValue().toString());
			
		}
		descriptionAdapter.notifyDataSetChanged();
		System.out.println(descriptionList);
		if (!discpMap.isEmpty()) {
			spinEstDestcription.setEnabled(true);
		}
	}

	public String getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o.toString();
			}
		}
		return null;
	}
}
