package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.cordova.api.LOG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.model.WorkFinancialDetails;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import com.zeal.gov.kar.gescomtesting.MeasurementActivity.ExpandableListAdapter;


public class ItemwiseUpdateActivity extends Activity implements OnClickListener {

	Spinner spnWorkTaskDetails;
	TableLayout tlWorkFinanceTable,tlHt,tlLt,tlTc;
	EditText etDate, etQuatityover;
	private ImageView addtoLayout;
	private LinearLayout htLinearlayout,ltLinearlayout,tcLinearlayout;
	Button btnCancel, btnSend,btnViewImages,btnImageCapture;
	private static final int IMAGECODE =100;
	static final int DATE_DIALOG_ID = 1;
	private Point p;
	String StrDateSelected;
	int WorkRowId;
	 String strWorkDescription;
	double remainingQuantity;
	private LinearLayout  linLayForImage;
	List<TaskDetails> lstTaskDetails;
	List<String> lstWorkDescription;
	List<WorkFinancialDetails> lstFinancialDetails;
	ArrayAdapter<String> arrayAdapterWorkDesc;
	private List<ApprovedPhotoCoordinates> mainImageList;
	Soapproxy soapproxy;
	Parser parser;
	TableRow trForFinanceDetailsTable = null;
	TextView tvUpdate, textView,textviewHt,textViewLt,textViewTc;
	TextView tvQuantityover,tvRemainingQuantity;
	View rowView;
	String sDetails = "";
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	TableRow row;
	ImageView imageViewarrow;
	HashMap<Integer, Integer> hmForSpinner;
	HashMap<TableRow, WorkFinancialDetails> hmForItemwiseUpdateTable;
	final List<Integer> measurementIdsForDecimal =  new ArrayList<Integer>(Arrays.asList(2,5,6,7,8,9,10,12,13,15,16,24));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemwiseupdate);

		spnWorkTaskDetails = (Spinner) findViewById(R.id.spnWorkTask);
		//tlWorkFinanceTable = (TableLayout) findViewById(R.id.tlFinanceTable);

		etDate = (EditText) findViewById(R.id.etDate);
		textviewHt = (TextView)findViewById(R.id.textviewht);
		textViewLt = (TextView)findViewById(R.id.textviewlt);
		textViewTc = (TextView)findViewById(R.id.textviewtc);
		btnSend = (Button) findViewById(R.id.btnUpdate);
		btnSend.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		imageViewarrow = (ImageView)findViewById(R.id.imageviewarrow);
		mainImageList=new ArrayList<ApprovedPhotoCoordinates>();
		btnImageCapture = (Button)findViewById(R.id.btnImageCapture);
		tlHt = (TableLayout)findViewById(R.id.HtTablelayout);
		tlLt = (TableLayout)findViewById(R.id.LtTablelayout);
		tlTc = (TableLayout)findViewById(R.id.TCTablelayout);
		btnViewImages = (Button)findViewById(R.id.btnImages);
		htLinearlayout = (LinearLayout)findViewById(R.id.HTlinearlayout);
		ltLinearlayout = (LinearLayout)findViewById(R.id.Ltlinearlayout);
		tcLinearlayout = (LinearLayout)findViewById(R.id.Tclinearlayout);
		btnImageCapture.setOnClickListener(this);
		btnViewImages.setOnClickListener(this);
		btnImageCapture = (Button)findViewById(R.id.btnImageCapture);
        btnImageCapture.setOnClickListener(this);
        /*textviewHt.setOnClickListener(this);
        textViewLt.setOnClickListener(this);
        textViewTc.setOnClickListener(this);*/
        htLinearlayout.setOnClickListener(this);
        ltLinearlayout.setOnClickListener(this);
        tcLinearlayout.setOnClickListener(this);
		etDate.setEnabled(false);
		btnSend.setEnabled(false);
		soapproxy = new Soapproxy(ItemwiseUpdateActivity.this);
//		String string = Appuser.getUserName();
		if(Internet.isAvailable(ItemwiseUpdateActivity.this)){
			GetTaskDetails task = new GetTaskDetails();
			task.execute();
//		lstTaskDetails = soapproxy.getTaskDetails("je_engg");
//		spinnerInitialization();
		}
		else{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("No Internet");
			dialog.setMessage("Make sure Data Connection Available and Try again");
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivity();
				}
			});
			dialog.create();
			dialog.show();
			message("Data Connection Unavailable..Please enable connection and try again");
		}

		spnWorkTaskDetails
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element,
					View arg1, int pos, long arg3) {
				if(pos>0){
					etDate.setText("");
					ImageView htImageView = (ImageView)htLinearlayout.findViewById(R.id.imageviewarrow);
					htImageView.setImageResource(R.drawable.arrow_right);
					ImageView ltImageView = (ImageView)ltLinearlayout.findViewById(R.id.imageviewarrow);
					ltImageView.setImageResource(R.drawable.arrow_right);
					ImageView tcImageView = (ImageView)tcLinearlayout.findViewById(R.id.imageviewarrow);
					tcImageView.setImageResource(R.drawable.arrow_right);
					
					tlHt.removeAllViews();
					tlLt.removeAllViews();
					tlTc.removeAllViews();
					
					etDate.setText("");
					etDate.setEnabled(true);
					btnSend.setEnabled(true);
					 strWorkDescription = element.getItemAtPosition(
							pos).toString();
					 WorkRowId = hmForSpinner.get(pos);
					System.out.println("workrowId From HM----"+hmForSpinner.get(pos));
					Iterator<TaskDetails> itr = lstTaskDetails.iterator();
					while (itr.hasNext()) {
						TaskDetails taskDetails = itr.next();
						if (taskDetails.getWorkDescription()
								.equalsIgnoreCase(strWorkDescription)) {
							}
						}
						System.out
					.println("WorkRowId for selected description----"
							+ WorkRowId);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ItemwiseUpdateActivity.this);

					alertDialogBuilder.setTitle("Task Description");
					alertDialogBuilder.setMessage(strWorkDescription);
					alertDialogBuilder.setCancelable(false)
					.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							lstFinancialDetails = new ArrayList<WorkFinancialDetails>();
							//tlWorkFinanceTable.removeAllViewsInLayout();
							mainImageList.clear();
							projectPhotos();
							GenerateItemwiseUpdateTable details = new GenerateItemwiseUpdateTable();
							details.execute();
							
						}
					})
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {

							spnWorkTaskDetails.setSelection(0, true);
							arrayAdapterWorkDesc.notifyDataSetChanged();
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				else {
					etDate.setEnabled(false);
					btnSend.setEnabled(false);
					tlHt.removeAllViews();
					tlLt.removeAllViews();
					tlTc.removeAllViews();
					tlHt.setVisibility(View.GONE);
					tlLt.setVisibility(View.GONE);
					tlTc.setVisibility(View.GONE);
				//	tlWorkFinanceTable.removeAllViews();
					ImageView htImageView = (ImageView)htLinearlayout.findViewById(R.id.imageviewarrow);
					htImageView.setImageResource(R.drawable.arrow_right);
					ImageView ltImageView = (ImageView)ltLinearlayout.findViewById(R.id.imageviewarrow);
					ltImageView.setImageResource(R.drawable.arrow_right);
					ImageView tcImageView = (ImageView)tcLinearlayout.findViewById(R.id.imageviewarrow);
					tcImageView.setImageResource(R.drawable.arrow_right);
				}
				

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
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
	
	public void projectPhotos(){
		BaseService baseService = new BaseService(this);
		if (baseService.isProjectphotosAvailable(String.valueOf(WorkRowId))) {
			List<ApprovedPhotoCoordinates> prePhotos = baseService
					.getProgressImages(
							String.valueOf(WorkRowId),
							"IP");
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
						Toast.makeText(ItemwiseUpdateActivity.this,
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
	private void popUpwindow()
	 {
		 LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View input = inflater.inflate(R.layout.popup, null);
		 int OFFSET_X = 20;
		 int OFFSET_Y = 20;

		 final PopupWindow pw = new PopupWindow(input, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		 linLayForImage = (LinearLayout)input.findViewById(R.id.linLayForImage);
		/* mainImageList.clear();
			BaseService baseService = new  BaseService(ItemwiseUpdateActivity.this);
			mainImageList =  baseService.getProgressImages(String.valueOf(WorkRowId), "IP");
	*/
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
		   btnImageCapture.getLocationOnScreen(location);
		 
		   //Initialize the Point with x, and y positions
		   p = new Point();
		   p.x = location[0];
		   p.y = location[1];
		}
	private class GetTaskDetails extends AsyncTask<String, Void, String>{
		ProgressDialog dialog = new ProgressDialog(ItemwiseUpdateActivity.this);
		
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
				result = soapproxy.getTaskDetails(Appuser.getUserName());
					if(result.equalsIgnoreCase("ServerTimeOut")||result.equalsIgnoreCase("noInternet"))
					{
						Log.e("Error", ""+result);
						return result;
					}
					else{
						Parser parser = new Parser(ItemwiseUpdateActivity.this);
						
						lstTaskDetails = parser.getTaskDetailsList(result);
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
					message("Projects download successfull");
					spinnerInitialization();
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

	private TableRow createFinanceDetailsTableHeader() {
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
				cell.setText("	  ");
			}
			if (i == 1) {
				cell.setText("Work Description");
			}
			if (i == 2) {
				cell.setText("CostPerItem");
			}
			if (i == 3) {
				cell.setText("Estimated Quantity");
			}
			if (i == 4) {
				cell.setText("Quatity Over");
			}
			if (i == 5) {
				cell.setText("Remaining Quantity");
			}
			cell.setPadding(6, 4, 6, 4);
			cell.setBackgroundColor(Color.rgb(100, 100, 100));
			header.addView(cell);
		}
		//tlWorkFinanceTable.addView(header);
		return header;
	}

	private TableRow addRowtoFinanceDetailsTable(
			WorkFinancialDetails details) {
		if(details.getAsOnToday() == null || details.getAsOnToday().equals("null") || details.getAsOnToday().equals("")){
			etDate.setText("");
		}else{
			etDate.setText(details.getAsOnToday());
		}
	//	hmForItemwiseUpdateTable = new HashMap<TableRow, WorkFinancialDetails>();
		
		 
			
			trForFinanceDetailsTable = new TableRow(ItemwiseUpdateActivity.this);
//			trForFinanceDetailsTable.setClickable(true);

//			int id = 0;
			for (int i = 0; i <= 5; i++) {

//				trForFinanceDetailsTable.setId(100 + id);
				EditText editTextForQuantityOver = new EditText(this);
				textView = new TextView(ItemwiseUpdateActivity.this) {
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

					textView.setText("		");

				}

				if (i == 1) {
					textView.setText(details.getDescription());
				}

				if (i == 2) {
					textView.setText(String.valueOf(details.getCostPerItem()));
				}

				if (i == 3) {
					textView.setText(String.valueOf(details.getTotalUnits()));
				}

				if (i == 4) {
					textView.setText(String.valueOf(details
							.getQuantityOver()));
				}

				if (i == 5) {
					// editTextForQuantityOver.setText(String.valueOf(details.getQuantityOver()));
					textView.setText(String.valueOf(Double.valueOf(details.getTotalUnits() - Double.valueOf(details.getQuantityOver()))));
				}

//				editTextForQuantityOver.setPadding(6, 4, 4, 6);
				textView.setPadding(6, 4, 6, 4);
				textView.setTextColor(Color.WHITE);
				// trForFinanceDetailsTable.addView(editTextForQuantityOver);
				trForFinanceDetailsTable.addView(textView);

			}
			hmForItemwiseUpdateTable.put(trForFinanceDetailsTable, details);
			
			trForFinanceDetailsTable.setOnClickListener(new OnClickListener() {
				public void onClick(View pTableRowView) {
					if(etDate.getText().toString().length()!=0){
						WorkFinancialDetails details = hmForItemwiseUpdateTable.get((TableRow)pTableRowView);
						if(details.getAsOnToday() == null || details.getAsOnToday().equals("null") || details.getAsOnToday().equals("")){
							showUpdateDialog(pTableRowView);
						}
						else if(checkForLastModifiedDate(details,etDate.getText().toString())){
							showUpdateDialog(pTableRowView);
						}else{
							message("Please specify the date that should be greater than last modified date "+details.getAsOnToday());
						}
					}
					else
					{
						message("Please specify the date");
					}
				}
			});
		return  trForFinanceDetailsTable;
	}

	protected boolean checkForLastModifiedDate(WorkFinancialDetails details, String pEnteredDate) {
		// TODO Auto-generated method stub
		String strLastModified = details.getAsOnToday();
		try{

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date1 = sdf.parse(pEnteredDate);
			Date date2 = sdf.parse(strLastModified);

			System.out.println(sdf.format(date1));
			System.out.println(sdf.format(date2));

			if(date1.compareTo(date2)>0){
				System.out.println("Date1 is after Date2");
				return true;
			}else if(date1.compareTo(date2)<0){
				System.out.println("Date1 is before Date2");
				return false;
			}else if(date1.compareTo(date2)==0){
				System.out.println("Date1 is equal to Date2");
				return true;
			}else{
				System.out.println("How to get here?");
				return false;
			}

		}catch(ParseException ex){
			ex.printStackTrace();
			return false;
		}

	}
	private void showUpdateDialog(final View view) {

		AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View updateView = inflater.inflate(R.layout.updateview,
				(ViewGroup) findViewById(R.id.updateLayout), false);
		etQuatityover = (EditText) updateView.findViewById(R.id.etQuOver);
		row = (TableRow) view;
		tvQuantityover = (TextView) row.getChildAt(4);
		tvRemainingQuantity = (TextView) row.getChildAt(5);
		
		WorkFinancialDetails details = hmForItemwiseUpdateTable.get((TableRow)view);
		if(!(measurementIdsForDecimal.contains(details.getMeasurementId()))){
			etQuatityover.setInputType(InputType.TYPE_CLASS_NUMBER);
			etQuatityover.setText(""+Math.round(Double.valueOf(tvQuantityover.getText().toString())));
		}
		else{
			etQuatityover.setText(tvQuantityover.getText().toString());
			etQuatityover.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
		}
		updateDialog.setTitle("Update");
		updateDialog.setView(updateView);
		TextView button = (TextView) row.getChildAt(0);
		button.setBackgroundResource(R.drawable.control_right);

		updateDialog.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				TextView tvTotalUnits = (TextView) row.getChildAt(3);
				double dTotalUnits = Double.parseDouble(tvTotalUnits
						.getText().toString());
				if (Double.parseDouble(etQuatityover.getText()
						.toString()) <= dTotalUnits) {
					TextView tvDescNo = (TextView) row.getChildAt(1);
					String sDesc = tvDescNo.getText().toString();
					TextView tvCostPerItem = (TextView) row
							.getChildAt(2);
					double dCostPerItem = Double
							.parseDouble(tvCostPerItem.getText()
									.toString());
					TextView tvEstimatedQ = (TextView) row
							.getChildAt(3);
					double dEstimatedQ = Double
							.parseDouble(tvEstimatedQ.getText()
									.toString());
					tvQuantityover.setText(""+Double.valueOf(etQuatityover.getText()
							.toString()));
					tvRemainingQuantity.setText(String.valueOf((dTotalUnits - Double.valueOf(etQuatityover.getText().toString()))));
					int subWorkId = 0;
					Iterator<WorkFinancialDetails> itr = lstFinancialDetails
							.iterator();
					while (itr.hasNext()) {
						WorkFinancialDetails details = itr.next();
						if (details.getDescription().equalsIgnoreCase(
								sDesc)
								&& details.getTotalUnits() == dTotalUnits
								&& details.getCostPerItem() == dCostPerItem
								&& details.getEstimatedQuantity() == dEstimatedQ) {
							subWorkId = details.getSubworkId();
						}
					}
					message("Quantity Updated");
					generateSDetails(view,tvQuantityover);
				} else {
					message("QuantityOver should not be greater than totalUnits");
				}
				
			}

		});

		updateDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		updateDialog.create();
		updateDialog.show();

	}

	protected void generateSDetails(View pView,TextView row) {
		// TODO Auto-generated method stub
		sDetails = "";
		TableRow row2 = (TableRow) pView;
		WorkFinancialDetails details = hmForItemwiseUpdateTable.get(row2);
		Log.e("details", ""+details.getQuantityOver());
		hmForItemwiseUpdateTable.remove(row2);
		
		details.setQuantityOver(Double.parseDouble(row.getText().toString()));
		details.setAsOnToday(etDate.getText().toString());
		details.setUpdatedBy(Appuser.getUserName());
		hmForItemwiseUpdateTable.put(row2, details);
		
		WorkFinancialDetails details2 = hmForItemwiseUpdateTable.get(row2);
		Log.e("details", ""+details2.getQuantityOver());
	}

	private void spinnerInitialization() {
		lstWorkDescription = new ArrayList<String>();
		lstWorkDescription.add("Select Work");
		BaseService service = new BaseService(ItemwiseUpdateActivity.this);
		List<Integer> lstMeasurementWorks = new ArrayList<Integer>();
		lstMeasurementWorks = service.getProjectIdforSavecordinate("PRG",Appuser.getUserName());
		Iterator<TaskDetails> itr = lstTaskDetails.iterator();
		
		hmForSpinner = new HashMap<Integer, Integer>();
		int count = 1;
		while (itr.hasNext()) {
			TaskDetails taskDetails = itr.next();
					if(lstMeasurementWorks.contains(taskDetails.getWorkRowId())){
						hmForSpinner.put(count++, taskDetails.getWorkRowId());
						lstWorkDescription.add(taskDetails.getWorkDescription());
					}
		}

		arrayAdapterWorkDesc = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lstWorkDescription);
		spnWorkTaskDetails.setAdapter(arrayAdapterWorkDesc);
	}

	public void launchDate(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dairyRegDate, year, month, day);
		}

		return null;
	}

	OnDateSetListener dairyRegDate = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			StrDateSelected = String.valueOf(dayOfMonth) + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(year);
			etDate.setText(StrDateSelected);

		}

	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCancel:
			Intent menuIntent = new Intent(ItemwiseUpdateActivity.this,UsermenuActivity.class);
			this.startActivity(menuIntent);
			finish();
			break;

		case R.id.btnUpdate:
			UpdateWorkFinance updateWork = new UpdateWorkFinance();
			updateWork.execute();

			break;
		case R.id.btnImageCapture:
			Intent imageIntent = new Intent(ItemwiseUpdateActivity.this, CameraView.class);
//			String projectID=Integer.toString(projectData.get(spinProject.getSelectedItem().toString()));
			imageIntent.putExtra("Projectid",String.valueOf(WorkRowId));
			imageIntent.putExtra("workstatus","IP");
			startActivityForResult(imageIntent,IMAGECODE);
			 break;
		case R.id.btnImages:
			 popUpwindow();
			 break;
		case R.id.HTlinearlayout:
			ImageView ltImageView = (ImageView)ltLinearlayout.findViewById(R.id.imageviewarrow);
			ltImageView.setImageResource(R.drawable.arrow_right);
			ImageView tcImageView = (ImageView)tcLinearlayout.findViewById(R.id.imageviewarrow);
			tcImageView.setImageResource(R.drawable.arrow_right);
			imageViewarrow = (ImageView)v.findViewById(R.id.imageviewarrow);
			if(tlHt.getVisibility() == View.VISIBLE){
				tlHt.setVisibility(View.GONE);
				imageViewarrow.setImageResource(R.drawable.arrow_right);
			}else{
			tlHt.setVisibility(View.VISIBLE);
			tlLt.setVisibility(View.GONE);
			tlTc.setVisibility(View.GONE);
			imageViewarrow.setImageResource(R.drawable.arrow_down);
			}
			break;
		case R.id.Ltlinearlayout:
			ImageView htImageView = (ImageView)htLinearlayout.findViewById(R.id.imageviewarrow);
			htImageView.setImageResource(R.drawable.arrow_right);
			ImageView tcImageView1 = (ImageView)tcLinearlayout.findViewById(R.id.imageviewarrow);
			tcImageView1.setImageResource(R.drawable.arrow_right);
			imageViewarrow = (ImageView)v.findViewById(R.id.imageviewarrow);
			if(tlLt.getVisibility() == View.VISIBLE){
				tlLt.setVisibility(View.GONE);
				imageViewarrow.setImageResource(R.drawable.arrow_right);
			}else{
			tlHt.setVisibility(View.GONE);
			tlLt.setVisibility(View.VISIBLE);
			tlTc.setVisibility(View.GONE);
			imageViewarrow.setImageResource(R.drawable.arrow_down);
			}
		    break;
		case R.id.Tclinearlayout:
			ImageView ltImageView1= (ImageView)ltLinearlayout.findViewById(R.id.imageviewarrow);
			ltImageView1.setImageResource(R.drawable.arrow_right);
			ImageView htImageView1 = (ImageView)htLinearlayout.findViewById(R.id.imageviewarrow);
			htImageView1.setImageResource(R.drawable.arrow_right);
			imageViewarrow = (ImageView)v.findViewById(R.id.imageviewarrow);
			if(tlTc.getVisibility() == View.VISIBLE){
				tlTc.setVisibility(View.GONE);
				imageViewarrow.setImageResource(R.drawable.arrow_right);
			}else{
			tlHt.setVisibility(View.GONE);
			tlLt.setVisibility(View.GONE);
			tlTc.setVisibility(View.VISIBLE);
			imageViewarrow.setImageResource(R.drawable.arrow_down);
			}
			break;
		default:
			break;
		}
	}
	
	private class GenerateItemwiseUpdateTable extends AsyncTask<String, Void, String>{
		ProgressDialog dialog = new ProgressDialog(ItemwiseUpdateActivity.this);
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("Please wait");
			dialog.setMessage("Generating table");
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) { 
			// TODO Auto-generated method stub
			String result = new String();
					// TODO Auto-generated method stub
			try{
				LOG.e("eRRORRRRRRRRRRRRRR", ""+hmForSpinner.get(spnWorkTaskDetails.getSelectedItemPosition()));
					result = soapproxy
							.getWorkFinancialDetails(String
									.valueOf(hmForSpinner.get(spnWorkTaskDetails.getSelectedItemPosition())));
					if(result.equalsIgnoreCase("ServerTimeOut")||result.equalsIgnoreCase("noInternet")){
						Log.e("Error", ""+result);
						return result;
					}
					else{
						Parser parser = new Parser(ItemwiseUpdateActivity.this);
						lstFinancialDetails = parser.getWorkFinancialDetails(result);
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
			else if(lstFinancialDetails.size()>0){
				/*createFinanceDetailsTableHeader();
				addRowtoFinanceDetailsTable(lstFinancialDetails);*/
				/*ItemwiseUpdate itemwiseUpdate = new ItemwiseUpdate();
				itemwiseUpdate.execute();*/
				tlHt.setVisibility(View.GONE);
				tlLt.setVisibility(View.GONE);
				tlTc.setVisibility(View.GONE);
				tlHt.addView(createFinanceDetailsTableHeader(),0);
				tlLt.addView(createFinanceDetailsTableHeader(),0);
				tlTc.addView(createFinanceDetailsTableHeader(),0);
				hmForItemwiseUpdateTable = new HashMap<TableRow, WorkFinancialDetails>();
				for(WorkFinancialDetails item : lstFinancialDetails){
					if(item.getBlockId().contains("B1")){
						tlHt.addView(addRowtoFinanceDetailsTable(item));
						
					}
					if(item.getBlockId().contains("B3")){
						tlLt.addView(addRowtoFinanceDetailsTable(item));
						
					}
					if(item.getBlockId().contains("B2")){
						tlTc.addView(addRowtoFinanceDetailsTable(item));
						
					}
				}
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

	private class UpdateWorkFinance extends AsyncTask<String, Void, String> {
		ProgressDialog dialog = new ProgressDialog(ItemwiseUpdateActivity.this);
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("Upload To Server");
			dialog.setMessage("Uploading.. Please Wait!!");
			this.dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Soapproxy soapproxy = new Soapproxy(ItemwiseUpdateActivity.this);
			Iterator<Entry<TableRow, WorkFinancialDetails>> iterator = hmForItemwiseUpdateTable.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<TableRow, WorkFinancialDetails> entry = iterator.next();
				TableRow view = entry.getKey();
				WorkFinancialDetails items = entry.getValue();
				sDetails = sDetails +items.getSubworkId() + "@" + items.getQuantityOver() + "@" +items.getUpdatedBy() +"@"+items.getAsOnToday()+"*";
			}
			
			Log.e("sdetails", ""+sDetails);
			String result = new String();
			try{
			result = soapproxy.updateWorkFinance(WorkRowId, sDetails);
			mainImageList.clear();
			BaseService baseService = new  BaseService(ItemwiseUpdateActivity.this);
			mainImageList = (ArrayList<ApprovedPhotoCoordinates>) baseService.getProgressImages(String.valueOf(WorkRowId), "IP","N");
			Iterator<ApprovedPhotoCoordinates> itr = mainImageList.iterator();
			Iterator<ApprovedPhotoCoordinates> itImage = mainImageList.iterator();
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
						Toast.makeText(ItemwiseUpdateActivity.this,
								"Some images are missing!!!", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				mainImageList.add(preImage);
			/*}
			while(itr.hasNext()){
				ApprovedPhotoCoordinates photoCoordinates = itr.next();*/
				/*if(remainingQuantity == 0.0){
				photoCoordinates.setWorkStarus("AC");
			}
			else{
				photoCoordinates.setWorkStarus("IP");
			}*/
				String res = soapproxy.uploadWorkImages(preImage);
				if(res.equals("success")){
					baseService.updateProgressImagesSyncyes(String.valueOf(WorkRowId),preImage);
				}
			}
			}catch (Exception e) {
				result = "error";
			}
			return result; 
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result.equalsIgnoreCase("success")) {
				message("Update Successfull");
				startActivity();
			}
			else if (result.equalsIgnoreCase("error")) {
				message("Error while requesting the service, Please try again later");
				startActivity();
			}
			else{
				message("Error requesting service, please try again later");
				startActivity();
			}
			
		}

	}
	
	private void message(String msg){
		Toast.makeText(ItemwiseUpdateActivity.this,
				msg, Toast.LENGTH_LONG).show();

	}

	private void startActivity(){
		Intent userMenuIntent = new Intent(ItemwiseUpdateActivity.this,UsermenuActivity.class);
		userMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(userMenuIntent);
		finish();
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
			TableRow Header = new TableRow(ItemwiseUpdateActivity.this);
			for (int j = 0; j <= 5; j++) {
				TextView cell = new TextView(ItemwiseUpdateActivity.this) {
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
				if(j == 0){
					cell.setText(" ");
				}
				if (j == 1) {
					cell.setText("DescCd");
				}
				if (j == 2) {
					cell.setText("TotalUnits");
				}

				if(j == 3){
					cell.setText("CostPerItem");
				}

				if(j == 4){
					cell.setText("EstimatedQuantity");
				}

				if(j == 5){
					cell.setText("QuatityOver");

				}

			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
			
				cell.setBackgroundColor(Color.rgb(100, 100, 100));
				Header.addView(cell);
		}
			table.addView(Header);
			if(hmForItemwiseUpdateTable != null){
				hmForItemwiseUpdateTable = null;
			}
		//	hmForItemwiseUpdateTable  = new HashMap<TableRow,WorkFinancialDetails>();
					
					
			for (WorkFinancialDetails item : lstFinancialDetails) {
           if (item.getBlockId().equalsIgnoreCase("E4B2") && groupPosition==1) {
        	  
			table.addView( addRowtoFinanceDetailsTable(item));
            }
            if (item.getBlockId().equalsIgnoreCase("E4B3") && groupPosition==2) {
				
            	table.addView(addRowtoFinanceDetailsTable(item));
                }
            if (item.getBlockId().equalsIgnoreCase("E4B1") && groupPosition==0) {
				
            	table.addView(addRowtoFinanceDetailsTable(item));
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
	/* protected TableRow addRowsToEditEstimationTable(
				WorkFinancialDetails item) {
			// TODO Auto-generated method stub
//			hmMeasurementTable = new HashMap<TableRow, WorkItems>();
//			Iterator<WorkItems> itr = lstTableContents.iterator();
//			while (itr.hasNext()) {
//				WorkItems contents = itr.next();
		 trForFinanceDetailsTable = new TableRow(ItemwiseUpdateActivity.this);
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
					// double formulaValue = calculateFormulaValue(contents);

					if (i == 0) {
						cell.setText(item.getDescription());
					}
					if (i == 1) {
						cell.setText(String.valueOf(item.getTotalUnits()));
					}
					if (i == 2) {
						cell.setText(String.valueOf(item.getCostPerItem()));
					}

					if(i == 3){
						
					cell.setText(String.valueOf(item.getEstimatedQuantity()));
					
					}

					

					if (i == 4) {
						// editTextForQuantityOver.setText(String.valueOf(details.getQuantityOver()));
						cell.setText(String.valueOf(item.getQuantityOver()));
					}
                        
					
					cell.setPadding(6, 4, 6, 4);
					// trForFinanceDetailsTable.addView(editTextForQuantityOver);
					//trForFinanceDetailsTable.addView(textView);
					trForFinanceDetailsTable.addView(cell);
				}
				hmForItemwiseUpdateTable.put(trForFinanceDetailsTable, item);
				
			//	tlWorkFinanceTable.addView(trForFinanceDetailsTable);
				trForFinanceDetailsTable.setOnClickListener(new OnClickListener() {
					public void onClick(View pTableRowView) {
						if(etDate.getText().toString().length()!=0){
						showUpdateDialog(pTableRowView);
						}
						else
						{
							message("Please specify the date");
						}
					}
				});
//			}
				return trForFinanceDetailsTable;

		}*/
	 private class ItemwiseUpdate extends AsyncTask<String, Void, String> {
			private final ProgressDialog dialog = new ProgressDialog(
					ItemwiseUpdateActivity.this);
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
						listAdapter =  new ExpandableListAdapter(ItemwiseUpdateActivity.this, listDataHeader);
						expListView = (ExpandableListView) findViewById(R.id.lvExp);
						expListView.setAdapter(listAdapter);
						expListView.refreshDrawableState();
						
						
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
}
