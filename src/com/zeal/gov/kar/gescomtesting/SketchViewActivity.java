package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.List;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.session.Appuser;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class SketchViewActivity extends Activity{
	private Spinner spnProject;
	private RadioGroup rgSpanlength;
	private CheckBox cbMapwithspanlength;
	private LinearLayout spanLengthlayout;
	private Button btnContinue,btnBack,btnMoveToNormalMap;
	private BaseService baseService;
	private int workRowid = 0;
	private int spanLength = 30;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sketchview);
		spnProject = (Spinner )findViewById(R.id.spnProjects);
		rgSpanlength = (RadioGroup) findViewById(R.id.rgSpanLength);
//		cbNormalMap = (CheckBox) findViewById(R.id.checkBoxnormal);
		cbMapwithspanlength = (CheckBox) findViewById(R.id.checkBoxspan);
		spanLengthlayout = (LinearLayout) findViewById(R.id.spanLengthSketchlayout);
		spanLengthlayout.setVisibility(View.INVISIBLE);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnContinue = (Button) findViewById(R.id.btnContinue);
		baseService = new BaseService(SketchViewActivity.this);
		List<String> lstWorkDesc = new ArrayList<String>();
		lstWorkDesc.add("Select project");
		lstWorkDesc.addAll(baseService.getProjectForUser(Appuser.getUserName()));
		ArrayAdapter<String> spnProjAdap = new ArrayAdapter<String>(SketchViewActivity.this, android.R.layout.simple_spinner_item,lstWorkDesc);
		spnProject.setAdapter(spnProjAdap);
		btnMoveToNormalMap = (Button) findViewById(R.id.btnNormalMap);
		
		btnMoveToNormalMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent skipToNormalMap = new Intent(SketchViewActivity.this,SurveySketchActivity.class);
				startActivity(skipToNormalMap);
			}
		});
		spnProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos>0){
					btnContinue.setEnabled(true);
					workRowid = baseService.getWorkRowId(element.getSelectedItem().toString());
				}
				else{
					btnContinue.setEnabled(false);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		
		
		btnContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	if(cbNormalMap.isChecked() == false && cbMapwithspanlength.isChecked() == false){
					Toast.makeText(SketchViewActivity.this, "Please select sketch view", Toast.LENGTH_LONG).show();
				}else if(cbNormalMap.isChecked()){
					Intent normalMap = new Intent(SketchViewActivity.this, IntermediatePoleActivity.class);
					normalMap.putExtra("workrowid", ""+workRowid);
					normalMap.putExtra("spanLength", ""+spanLength);
					startActivity(normalMap);
				}else*/
					if(cbMapwithspanlength.isChecked()){
					Intent intermediatePoleMap = new Intent(SketchViewActivity.this, IntermediatePoleActivity.class);
					intermediatePoleMap.putExtra("workrowid", ""+workRowid);
					intermediatePoleMap.putExtra("spanLength", ""+spanLength);
					startActivity(intermediatePoleMap);
				}else{
					Toast.makeText(SketchViewActivity.this, "Please select sketch view", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		rgSpanlength.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rbtnThirty:
					spanLength = 30;
					break;
				case R.id.rbtnFourty:
					spanLength = 40;
					break;
				case R.id.rbtnFifty:
					spanLength = 50;
					break;

				default:
					spanLength = 30;
					break;
				}
				
			}
		});
		
		/*cbNormalMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spanLengthlayout.setVisibility(View.INVISIBLE);
				spanLength = 30;
				cbMapwithspanlength.setChecked(false);
			}
		});*/
		
		cbMapwithspanlength.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cbMapwithspanlength.isChecked()){
				spanLengthlayout.setVisibility(View.VISIBLE);
				}else{
					spanLengthlayout.setVisibility(View.INVISIBLE);
				}
//				cbNormalMap.setChecked(false);
			}
		});
	}

}
