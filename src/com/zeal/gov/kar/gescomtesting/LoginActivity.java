package com.zeal.gov.kar.gescomtesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseDao;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.User;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;

public class LoginActivity extends Activity {
	Button btnLogin,btnLogCancel,copyBackupDb;
	EditText edttxtUsername,edttxtpassword;
	String strStatusMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.activity_login);
		
		createDatabase createDb=new createDatabase();
		createDb.execute();
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Appuser.setImeiNo(tm.getDeviceId());
		//System.out.println(tm.getDeviceId());
		
		BaseService baseService = new BaseService(this);
		
		/*baseService.deleteTemp();
		baseService.deleteTempEstimateType();*/
		copyDatabaseToSdcard();
		copyBackupDb=(Button)findViewById(R.id.fromBackup);
		edttxtUsername = (EditText)findViewById(R.id.userName);
		edttxtpassword = (EditText)findViewById(R.id.password);
		edttxtUsername.addTextChangedListener(new TextWatcher() {
		    public void afterTextChanged(Editable s) {}
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){
		        if(s != null && s.length() > 0 && edttxtUsername.getError() != null) {
		        	edttxtUsername.setError(null);
		        }
		    }
		});
		copyBackupDb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showAlert();
			}
		});
		//copyBackupDb.setVisibility(View.INVISIBLE);
		edttxtpassword.addTextChangedListener(new TextWatcher() {
		    public void afterTextChanged(Editable s) {}
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){
		        if(s != null && s.length() > 0 && edttxtpassword.getError() != null) {
		        	edttxtpassword.setError(null);
		        }
		    }
		}); 
		//QL0AFWMIX8NRZTKeof9cXsvbvu8=
		/*edttxtUsername.setText("SO_South");
		edttxtpassword.setText("123");*/
		edttxtUsername.setText("aeshankar");
		edttxtpassword.setText("123");
		btnLogin = (Button) findViewById(R.id.login);
		btnLogCancel = (Button) findViewById(R.id.loginCancel);


		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AuthTask task = new AuthTask();
				task.execute(new String[] { "" });
				
			}
		});
		btnLogCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	private class AuthTask extends AsyncTask<String, Void, String> {
		BaseService baseService = new BaseService(getApplicationContext());
		private final ProgressDialog dialog = new ProgressDialog(
				LoginActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Authentication is in Process");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String strResult = new String();
			String strUsername = edttxtUsername.getText().toString().trim();
			String strPassword = getHashPass(edttxtpassword.getText().toString().trim());
			if (strUsername != null && !strUsername.equalsIgnoreCase("")) {
				if (strPassword != null && !strPassword.equals("")) {
					if (baseService.isUserAvailable(strUsername, strPassword)) {
						Appuser.setUserName(strUsername);
						Appuser.setPassword(strPassword);
						strResult="Activated";
					} 
					else {
						if(Internet.isAvailable(LoginActivity.this))
						{
						//String r = validateServer(strUsername, strPassword);
							try{
						   TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						    User user = new User();	
						    //"351816057678553"
						    user.setImeiNo("357953040205501");
//			                user.setImeiNo(tm.getDeviceId());
			                user.setSimNo(tm.getSimSerialNumber());
			                user.setUserName(strUsername);
			                user.setHashPassword(strPassword);			             
			                Soapproxy proxy=new Soapproxy(LoginActivity.this);
			                Parser parseResponse= new Parser(LoginActivity.this);			             
			                strResult=parseResponse.parseResponse(proxy.getProjectsForMobile(user));
			                System.out.println(strResult);
							}
							catch(Exception e)
							{
								e.printStackTrace();
								strResult = "Error";
							}
						}
						else
						{
							strResult = "Error";
						}
					}
				} else {
					
					strResult = "notActivated";
				}
			} else {
	
				strResult = "notActivated";
			}


			return strResult;
			
		}
		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			if (strResult.equals("Activated")) {
				Intent i = new Intent(LoginActivity.this, UsermenuActivity.class);
				LoginActivity.this.startActivity(i);
				finish();
			}
			else if(strResult.equals("Error")) {
				
				Toast.makeText(LoginActivity.this, "Internet currently unavailable", Toast.LENGTH_LONG).show();
			} 
           else  {
				
				edttxtUsername.setError("Please provide valide Username.");
				edttxtpassword.setError("Please provide valide Password.");
			} 
			
		}
		
		}
	
	Thread.UncaughtExceptionHandler handlers=
	        new Thread.UncaughtExceptionHandler() {
	        public void uncaughtException(Thread thread, Throwable ex) {
	            Log.e("TAG", "Uncaught exception", ex);
	            showDialog(ex);
	        }
	    };
	    
	  
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	 
	        case R.id.menu_settings:
	            Intent i = new Intent(this, MatreialDetails.class);
	            startActivityForResult(i,10);
	            break;
	 
	        }
	 
	        return true;
	    }
	void showDialog(Throwable t) {
	        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
	            builder
	                .setTitle("Exception")
	                .setMessage(t.toString())
	                .setPositiveButton("Okay", null)
	                .show();
	    }
	private class createDatabase extends AsyncTask<String, Void, String> {
		
		private final ProgressDialog dialog = new ProgressDialog(
				LoginActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Intializing");
			this.dialog.setCancelable(false);
			//this.dialog.setCanceledOnTouchOutside(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String strResult = new String();
			//Database Creation
			BaseDao baseDao = new BaseDao(LoginActivity.this);
			try {
				baseDao.open();
			} finally {
				baseDao.close();
			}
			return strResult;
			
		}
		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
			
		}
		
	}
	private String getHashPass(String userPassword) 
	{
		byte[] strBytesData = new byte[userPassword.length()];
		try 
		{
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			for (int i = 0; i < strBytesData.length; i++) {
				strBytesData[i] = (byte) userPassword.charAt(i);
			}
			strBytesData = mDigest.digest(strBytesData);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(strBytesData));
	}
	private void copyDatabaseToPhone() {
		try {
			File data = Environment.getExternalStorageDirectory();
			File sd = Environment.getDataDirectory();

			if (data.canWrite()) {
				String currentDBPath = "//backup//testing//gescom.db";
				String backupDBPath = "//data//com.zeal.gov.kar.gescomtesting//databases//gescom.db";
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
	private void copyDatabaseToSdcard() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			Toast.makeText(LoginActivity.this, "Latest Db Copied to sddcard", Toast.LENGTH_LONG).show();
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
	private void showAlert()
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		LayoutInflater li = getLayoutInflater();
		View v = li.inflate(R.layout.backupscreen, null);
		alertbox.setView(v);
		
		final EditText edtxtPassword=(EditText)v.findViewById(R.id.buckUpPassword);
		alertbox.setMessage("Enter Password"); // Message to be displayed
		
		alertbox.setPositiveButton("Add",new DialogInterface.OnClickListener() {
			
		public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method 
			
			if(edtxtPassword.getText().toString().equalsIgnoreCase("gescom321"))
			{
				copyDatabaseToPhone();
				Toast.makeText(LoginActivity.this, "Application Database changed.",Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(LoginActivity.this, "Access Denied !! ",Toast.LENGTH_LONG).show();
				
			}
			
		      }
		
		});

		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method 	
					
				}
				
          });
         
		alertbox.show();
     }
}
