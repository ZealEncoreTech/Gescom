package com.zeal.gov.kar.gescomtesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ItemsforDelete;
import com.zeal.gov.kar.gescom.model.User;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;


public class UsermenuActivity extends Activity implements  OnClickListener,Serializable{
	Button btnsaveCoordinates,btncreateEstn,btneditEstn,btnmeasurement,btntaskProcess,btnitemUpdate,btnimageCapture,btnSyncronize,btnSurveySketch,btnCategoryChange;
	private ArrayList<String> lst;
	ArrayList<ItemsforDelete> lstdel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usermenu);
		lst = new ArrayList<String>();
		lstdel = new ArrayList<ItemsforDelete>();
		
		btnsaveCoordinates=(Button)findViewById(R.id.savecoordinates);
		btncreateEstn=(Button)findViewById(R.id.createestimation);
		btneditEstn=(Button)findViewById(R.id.editestimation);
		btnmeasurement=(Button)findViewById(R.id.measurement);
		btnitemUpdate=(Button)findViewById(R.id.update);
		btntaskProcess=(Button)findViewById(R.id.task);
		btnimageCapture=(Button)findViewById(R.id.image);
		btnSyncronize=(Button)findViewById(R.id.sync);
		btnSurveySketch = (Button)findViewById(R.id.surveySketch);
		btnCategoryChange = (Button)findViewById(R.id.CategoryChange);
		
		btnsaveCoordinates.setOnClickListener(this);
		btncreateEstn.setOnClickListener(this);
		btneditEstn.setOnClickListener(this);
		btnmeasurement.setOnClickListener(this);
		btntaskProcess.setOnClickListener(this);
		btnitemUpdate.setOnClickListener(this);
		btnimageCapture.setOnClickListener(this);
		btnSyncronize.setOnClickListener(this);
		btnSurveySketch.setOnClickListener(this);
		btnCategoryChange.setOnClickListener(this);
		
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_usermenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.Delete_photos:
	       /*Intent intent = new Intent(UsermenuActivity.this,DeletePhotosActivity.class);
	       startActivity(intent);
	        return true;*/
	    	final BaseService baseService = new BaseService(
					UsermenuActivity.this);
			lst.addAll(baseService.getProjectforDeletingImages("CMP",
					Appuser.getUserName()));

			Iterator<String> itrimages = lst.iterator();
			
			int count = 0;
			while (itrimages.hasNext()) {
				final String string = (String) itrimages.next();
			//	if (baseService.isWorkRowIdPresentinProgressImages(string)) {
					String str = baseService.getCaptureddate(string);

					SimpleDateFormat datefarmate = new SimpleDateFormat(
							"yyyy-MM-dd");
					Calendar calender = Calendar.getInstance();
					String date = datefarmate.format(calender.getTime());

					Date d1 = null;
					Date d2 = null;
					try {
						d1 = datefarmate.parse(str);
						d2 = datefarmate.parse(date);

						// in milliseconds
						long diff = d2.getTime() - d1.getTime();

						long diffDays = diff / (24 * 60 * 60 * 1000);

						System.out.println(diffDays);
						
						if (diffDays > 30) {
							 ItemsforDelete delete = new ItemsforDelete();
			            	  count++;
			            	 
			            	  delete.setSerialnumber(String.valueOf(count));
			            	  delete.setWorkDescription(baseService.getProjectforSavecordinateforDelete("CMP",string));
			            	  delete.setWorkRowId(string);
			            	  lstdel.add(delete);
							
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				
			}
			       
	              if(lstdel.size() == 0){
	            	 Toast.makeText(getApplicationContext(), "No works available for deleting",Toast.LENGTH_LONG).show();
	              }else{
	            	  Intent intent = new Intent(UsermenuActivity.this,DeletePhotosActivity.class);
	            	  intent.putExtra("list",lstdel);
	       	       startActivity(intent);         
	             
	              }  
	    
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	public void onBackPressed()
	{
		super.onBackPressed();
		Appuser.setUserName("");
		Appuser.setPassword("");
		Intent loginScreen = new Intent(UsermenuActivity.this, LoginActivity.class);
		loginScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		UsermenuActivity.this.startActivity(loginScreen);
		
	}
	
	public void onClick(View v) {
		int viewid = v.getId();
		switch (viewid) {
		case R.id.savecoordinates:
			/*Intent startMultiple = new Intent(UsermenuActivity.this, ComponetDetails.class);
			startMultiple.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			UsermenuActivity.this.startActivity(startMultiple);
			//finish();
*/			Intent startSavecord = new Intent(UsermenuActivity.this, MultipleEstimationActivity.class);
			startSavecord.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			UsermenuActivity.this.startActivity(startSavecord);
			//finish();
			break;
		case R.id.createestimation:
			Intent startCreateEst = new Intent(UsermenuActivity.this, CreateEstimationActivity.class);
			startCreateEst.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			UsermenuActivity.this.startActivity(startCreateEst);
			//finish();
			break;
		case R.id.editestimation:
			Intent startEditEst = new Intent(UsermenuActivity.this,EditEstimationActivity.class);
			UsermenuActivity.this.startActivity(startEditEst);
			
			break;
		case R.id.measurement:
			Intent startMeasurement = new Intent(UsermenuActivity.this,MeasurementActivity.class);
			UsermenuActivity.this.startActivity(startMeasurement);
			
			
			break;
		case R.id.task:
			Intent startTaskProcess = new Intent(UsermenuActivity.this,TaskProgressActivity.class);
			UsermenuActivity.this.startActivity(startTaskProcess);
			
			
			break;
		case R.id.update:
			Intent startItemwiseUpdate = new Intent(UsermenuActivity.this,ItemwiseUpdateActivity.class);
			UsermenuActivity.this.startActivity(startItemwiseUpdate);
			
			
			break;
		case R.id.image:
			Intent imageCapture = new Intent(UsermenuActivity.this,CameraActivity.class);
			UsermenuActivity.this.startActivity(imageCapture);
			
			break;
		case R.id.sync:
			if(Internet.isAvailable(UsermenuActivity.this))
			{
			Sync st=new Sync();
			st.execute();
			}
			else
			{
				Toast.makeText(UsermenuActivity.this, "Internet currently unavailable", Toast.LENGTH_LONG).show();	
			}
			break;
		case R.id.surveySketch:
			Intent surveySketchIntent = new Intent(UsermenuActivity.this,SketchViewActivity.class);
			UsermenuActivity.this.startActivity(surveySketchIntent);
			break;
		case R.id.CategoryChange:
			Intent CategoryChangeIntent = new Intent(UsermenuActivity.this,CategoryChange.class);
			startActivity(CategoryChangeIntent);
			break;
		default:
			break;
		}
	
		
	}
	private class Sync extends AsyncTask<String, Void, String> {
		 BaseService baseService = new BaseService(getApplicationContext());
		private final ProgressDialog dialog = new ProgressDialog(UsermenuActivity.this);
		private int newProject;
		@Override
		protected void onPreExecute() {

			this.dialog.setMessage("synchronizing...");
			//this.dialog.setCanceledOnTouchOutside(false);
              this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String strResult = new String();
			try{
							User user = new User();	            
			                user.setImeiNo("351816057678553");	
			                //user.setImeiNo(Appuser.getImeiNo());
			                user.setUserName(Appuser.getUserName());
			                user.setHashPassword(Appuser.getPassword());			             
			                Soapproxy proxy=new Soapproxy(UsermenuActivity.this);
			                Parser parseResponse= new Parser(UsermenuActivity.this);			             
			                strResult=parseResponse.parseResponse(proxy.getProjectsForMobile(user));
			                System.out.println(strResult);
			                newProject=parseResponse.newWorks;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				strResult="internetconectionProblem";
				
			}
			return strResult;
			
		}
		
		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			  Toast.makeText(UsermenuActivity.this, newProject+" Project Downloaded.", Toast.LENGTH_LONG).show();
                 if(strResult.equals("Error")) {
				
				Toast.makeText(UsermenuActivity.this, "Internet Connection unavailable", Toast.LENGTH_LONG).show();
			}
                 else if(strResult.equals("internetconectionProblem")) {
     				
     				Toast.makeText(UsermenuActivity.this, "Internet Disconnected", Toast.LENGTH_LONG).show();
     			}
                
                 
		}
		
    }


}
