package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.collections.map.MultiValueMap;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.session.Appuser;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CategoryChange extends Activity implements OnItemSelectedListener{
private Spinner spnCategoryWorks,spnCategoryId;
private Button btnUpdate;
private HashMap<String, Integer> discpMap,categoryMap;
private List<String> lstworks,lstCategories;
private ArrayAdapter<String> worksAdapter,categoryAdapter;
String Status;
String value,value1;
int categoryNumber,CategoryChangeNumber;
HashMap<String, Object> hm,hm1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_change);
		final BaseService baseService = new BaseService(this);
		spnCategoryWorks  = (Spinner)findViewById(R.id.spnCategoryChangeWorks);
		spnCategoryId = (Spinner)findViewById(R.id.spnCategoryChange);
		btnUpdate = (Button)findViewById(R.id.btnUpdate);
		spnCategoryWorks.setOnItemSelectedListener(this);
		spnCategoryId.setOnItemSelectedListener(this);
		
		lstworks = new ArrayList<String>();
		lstCategories = new ArrayList<String>();
		hm = new HashMap<String, Object>();
		hm1 = new HashMap<String, Object>();
		discpMap = new HashMap<String, Integer>();
		categoryMap = new HashMap<String, Integer>();
		Status = "NEW";
		hm = baseService.getWorkDescriptionforCategoryChangehm(Status , Appuser.getUserName());
		hm1 = baseService.getCategorynamesfrommasterCategory();
	//	lstworks.addAll(baseService.getWorkDescriptionforCategoryChange(Status , Appuser.getUserName()));
		spinnerIntialization();
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	if(baseService.isWorkRowIdPresent(value)){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryChange.this);

					// Setting Dialog Title
					alertDialog.setTitle("Alert Dialog");

					// Setting Dialog Message
					alertDialog.setMessage("Do you want to change category");

					alertDialog.setPositiveButton("Yes", new OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(spnCategoryWorks.getSelectedItemId() >0){
							if(spnCategoryId.getSelectedItemId() > 0){
							 baseService.updateWorkmainCategory(value, CategoryChangeNumber);
							 Toast.makeText(CategoryChange.this,"Category updated", Toast.LENGTH_LONG).show();
							 spnCategoryId.setSelection(0);
							 spnCategoryWorks.setSelection(0);
							 dialog.dismiss();
						}else{
							Toast.makeText(CategoryChange.this,"Please select category", Toast.LENGTH_LONG).show();
						}
						}else{
							Toast.makeText(CategoryChange.this,"Please select description", Toast.LENGTH_LONG).show();
						}
						}
					});

					// Setting Positive "Yes" Button
					

					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int which) {
						// Write your code here to invoke NO event
						
						dialog.cancel();
						}
					});

					// Showing Alert Message
					alertDialog.show();
					

					 
				// }
			}
		});
	}

	public void spinnerIntialization(){
		lstworks.add("Select Work");
		discpMap =  (HashMap<String, Integer>) hm.get("Multivalue");
		lstworks.addAll((List<String>) hm.get("lst"));
		worksAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lstworks);
		spnCategoryWorks.setAdapter(worksAdapter);
		lstCategories.add("Select Category");
		categoryMap =  (HashMap<String, Integer>) hm1.get("valuemap");
		lstCategories.addAll((List<String>) hm1.get("list"));
		categoryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lstCategories);
		spnCategoryId.setAdapter(categoryAdapter);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> adp, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		int id = adp.getId();
		switch (id) {
		case R.id.spnCategoryChangeWorks:
			if(pos>0){
				String pos1 = spnCategoryWorks.getItemAtPosition(pos).toString();
				
				
				  value = String.valueOf(discpMap.get(pos1));
				 System.out.println(value);
				 int i = Integer.parseInt(value.trim());
BaseService baseService = new BaseService(this);
categoryNumber = baseService.getProjectCategory(i);
spnCategoryId.setSelection(categoryNumber);
				 
			}else{
				spnCategoryId.setSelection(0);
			}
			break;
		case R.id.spnCategoryChange:
			if(pos>0){
				String pos1 = spnCategoryId.getItemAtPosition(pos).toString();
				value1 = String.valueOf(categoryMap.get(pos1));
					 System.out.println(value1);
					 CategoryChangeNumber = Integer.parseInt(value1.trim());
			
			
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
