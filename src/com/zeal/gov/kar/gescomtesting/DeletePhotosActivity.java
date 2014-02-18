package com.zeal.gov.kar.gescomtesting;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ItemsforDelete;
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.session.Appuser;

public class DeletePhotosActivity extends Activity{
	private ArrayList<String> lst, lstImages,lstphotos;
	
	
	ArrayList<ItemsforDelete> lstdel;
	
	
	HashMap<View, ItemsforDelete> hmOtherWorks;
	Dialog dialog;
	List<ItemsforDelete> selectedList;
	CheckBox headerCheckBox,checkBox;
	List<ItemsforDelete> lstWorksNoinDb,selectedDownloadList;
	LinearLayout layout;
	Button Back,Delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_table);
		
		 Back = (Button) findViewById(R.id.btnCancel);
		 Delete = (Button) findViewById(R.id.btnDownload);
		layout = (LinearLayout)findViewById(R.id.LtTablelayout);
		lst = new ArrayList<String>();
		lstImages = new ArrayList<String>();
		lstphotos = new ArrayList<String>();
		Delete.setEnabled(false);
		lstdel = new ArrayList<ItemsforDelete>();
		Back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent userMenuScreen = new Intent(DeletePhotosActivity.this, UsermenuActivity.class);
				userMenuScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				DeletePhotosActivity.this.startActivity(userMenuScreen);
			}
		});
		
		
		Delete.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 selectedList = getCheckedWorks();
				DeletePhoto deletePhoto = new DeletePhoto();
				deletePhoto.execute();
				
			}
		});
		Intent intent = getIntent();
		lstdel = (ArrayList<ItemsforDelete>) intent.getSerializableExtra("list");
	/*	final BaseService baseService = new BaseService(
				DeletePhotosActivity.this);
		lst.addAll(baseService.getProjectforDeletingImages("CMP",
				Appuser.getUserName()));

		Iterator<String> itrimages = lst.iterator();
		
		int count = 0;
		while (itrimages.hasNext()) {
			final String string = (String) itrimages.next();
			if (baseService.isWorkRowIdPresentinProgressImages(string)) {
				String str = baseService.getCaptureddate(string);

				SimpleDateFormat datefarmate = new SimpleDateFormat(
						"dd-MM-yyyy hh:mm:ss");
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
		}
		   */    
              
              showWorksNotInDb(lstdel);
		       
             
		
	}
	private class DeletePhoto extends AsyncTask<String, Void, String>{
		private ProgressDialog dialog = new ProgressDialog(DeletePhotosActivity.this);
		
     
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.setMessage("Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}
      
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			System.out.println(selectedList);
			BaseService baseService = new BaseService(DeletePhotosActivity.this);
			if(selectedList != null){
				if(selectedList.size()>0){
					selectedDownloadList = selectedList;
					System.out.println(selectedList);
					ArrayList<ItemsforDelete> lsdel;
					lsdel = new ArrayList<ItemsforDelete>();
					Iterator<ItemsforDelete> itrdel = selectedList.iterator();
					ItemsforDelete itemsdel = new ItemsforDelete();
					while(itrdel.hasNext()){
						ItemsforDelete itemsde = itrdel.next();
						System.out.println(itemsde.getWorkDescription());
						System.out.println(itemsde.getWorkRowId());
						lstphotos = (ArrayList<String>) baseService.getphotopathfromapprovecoordinates(itemsde.getWorkRowId());
						 lstImages = (ArrayList<String>) (baseService.getPhotoPath(itemsde.getWorkRowId()));
						 Iterator<String>itrsdcardpath = lstImages.iterator();
						  while(itrsdcardpath.hasNext()){ 
						String path =(String) itrsdcardpath.next(); 
						  String path1 =path.substring(15);
						  File file = new File(path);
						  if(file.exists()){
							  boolean deleted = file.delete();
						  
						  }
						 
						  }
						  Iterator<String> itrsdcardimagespath = lstphotos.iterator();
						  while(itrsdcardimagespath.hasNext()){
							  String imagepath = (String) itrsdcardimagespath.next(); 
							  String imgpath = imagepath.substring(4);
							  File file = new File(imgpath);
							  if(file.exists()){
								  boolean delete = file.delete();
							  }
						  }
						  boolean idinphotocoordinates = baseService.workidpresentinphotocoordinates(itemsde.getWorkRowId());
						  boolean idinCoordinates = baseService.workidpresentincoordinates(itemsde.getWorkRowId());
						  boolean idinworkmain = baseService.workidpresentinworkmain(itemsde.getWorkRowId());
						  boolean idinworkitems = baseService.workidpresentinworkItems(itemsde.getWorkRowId()); 
						  boolean idinEstimates = baseService.workidpresentinProjectestimates(itemsde.getWorkRowId());
						  boolean idinEstimateType = baseService.workidpresentinProjectestimateType (itemsde.getWorkRowId()); 
						  boolean idinprogressimages = baseService.workrowidinprogressimages(itemsde.getWorkRowId());
						  if(idinphotocoordinates){
						 baseService.deletefromApprovephotoCoordinates(itemsde.getWorkRowId()); 
						 } if(idinCoordinates){
						  baseService.deletefromApproveCoordinates(itemsde.getWorkRowId()); }
						  if(idinworkmain){
						  baseService.deletefromworkmain(itemsde.getWorkRowId()); 
						  }
						  if(idinworkitems){
						  baseService.deletefromworkItems(itemsde.getWorkRowId());
						  }
						  if(idinEstimates){
						  baseService.deletefromprojectEstimates(itemsde.getWorkRowId()); 
						  }
						  if(idinEstimateType){
						  baseService.deletefromprojectEstimateType(itemsde.getWorkRowId()); }
						  if(idinprogressimages){
								 baseService.deleteworkidfromprogreeimages(itemsde.getWorkRowId());
							 }
						 
					}
					
					//new DownloadDataNotPresentInDb().execute();
				}
				
			}
			
			return null;
		}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		this.dialog.dismiss();
		
		if(selectedList.size() > 0){
		getdeletecheckeditems();
		Toast.makeText(getApplicationContext(),
				  " Selected works deleted successfully",Toast.LENGTH_LONG).show(); 
		}else{
			Toast.makeText(getApplicationContext(),
					  "check the works before clicking on delete button",Toast.LENGTH_LONG).show(); 
		}
		selectedList = getCheckedWorks();
		if(selectedList.size() <= 0){
			Delete.setEnabled(false);
			headerCheckBox.setChecked(false);
		}
	}

	
		
	}
	
	@SuppressLint("NewApi")
	protected void showWorksNotInDb(List<ItemsforDelete> ls) {
		// TODO Auto-generated method stub
		hmOtherWorks = new HashMap<View, ItemsforDelete>();
		LayoutInflater inflater = getLayoutInflater();
		View headerView = inflater.inflate(R.layout.custom_table_row, null);
		headerCheckBox = (CheckBox) headerView.findViewById(R.id.checkBox1);
		
		TextView headerWorkId = (TextView) headerView.findViewById(R.id.tvWorkid);
		TextView headerDesc = (TextView) headerView.findViewById(R.id.tvDesc);
		
		TextView headerMoreInfo = (TextView) headerView.findViewById(R.id.tvmoreinfo);
		headerDesc.setBackgroundColor(Color.rgb(136, 54, 250));
		headerMoreInfo.setBackgroundColor(Color.rgb(136, 54, 250));
		headerWorkId.setBackgroundColor(Color.rgb(136, 54, 250));
		headerCheckBox.setBackgroundColor(Color.rgb(136, 54, 250));
		headerWorkId.setText("No");
		headerWorkId.setTextAppearance(DeletePhotosActivity.this, R.style.boldText);
		headerDesc.setText("Description");
		headerDesc.setTextAppearance(DeletePhotosActivity.this, R.style.boldText);
		
		headerMoreInfo.setText("");
		layout.addView(headerView,0);
		
		headerCheckBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(headerCheckBox.isChecked()){
					checkAllFromList();
					selectedList = getCheckedWorks();
					if(selectedList.size() <= 0){
						Delete.setEnabled(false);
						headerCheckBox.setChecked(false);
					}else{
						Delete.setEnabled(true);
					}
				}else{
					unCheckAllFromList();
					selectedList = getCheckedWorks();
					if(selectedList.size() <= 0){
						Delete.setEnabled(false);
						headerCheckBox.setChecked(false);
					}else{
						Delete.setEnabled(true);
					}
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
			workId.setText(""+ls.get(i).getSerialnumber());
			checkBox.setChecked(false);
			layout.addView(view);
			hmOtherWorks.put(view, ls.get(i));
			checkBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectedList = getCheckedWorks();
					if(selectedList.size() <= 0){
						Delete.setEnabled(false);
						headerCheckBox.setChecked(false);
					}else{
						Delete.setEnabled(true);
					}
				}
			});
			moreInfo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(DeletePhotosActivity.this, desc.getText().toString(), Toast.LENGTH_LONG).show();
				}
			});
		}//end of child table adding 
		
		
		
		
	}
	
	protected void checkAllFromList() {
		// TODO Auto-generated method stub
		Iterator<Entry<View, ItemsforDelete>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, ItemsforDelete> entry = iterator.next();
			View view = entry.getKey();
			ItemsforDelete items = entry.getValue();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			checkBox.setChecked(true);
		}
	}
	
	protected void unCheckAllFromList() {
		// TODO Auto-generated method stub
		Iterator<Entry<View, ItemsforDelete>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, ItemsforDelete> entry = iterator.next();
			View view = entry.getKey();
			ItemsforDelete items = entry.getValue();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			checkBox.setChecked(false);
		}
	}
	protected List<ItemsforDelete> getCheckedWorks() {
		// TODO Auto-generated method stub
		List<ItemsforDelete> checkedWorks = new ArrayList<ItemsforDelete>();
		Iterator<Entry<View, ItemsforDelete>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, ItemsforDelete> entry = iterator.next();
			
			View view = entry.getKey();
			ItemsforDelete itemstodelete = entry.getValue();
			
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			if(checkBox.isChecked()){
				checkedWorks.add(itemstodelete);
			}
			
		}
		return checkedWorks;
		
	}
    protected void getdeletecheckeditems(){
    	List<ItemsforDelete> checkedWorks = new ArrayList<ItemsforDelete>();
		Iterator<Entry<View, ItemsforDelete>> iterator = hmOtherWorks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<View, ItemsforDelete> entry = iterator.next();
			
			View view = entry.getKey();
			ItemsforDelete itemstodelete = entry.getValue();
			if(selectedDownloadList.contains(itemstodelete)){
				
				view.setVisibility(View.GONE);
				iterator.remove();
			}
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
			if(checkBox.isChecked()){
				checkedWorks.add(itemstodelete);
			}
			
    }
    }
}
