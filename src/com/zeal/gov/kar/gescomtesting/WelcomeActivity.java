package com.zeal.gov.kar.gescomtesting;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class WelcomeActivity extends Activity implements  OnClickListener{
	Button saveCoordinates,createEstn,editEstn,measurement,taskProcess,itemUpdate,imageCapture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_welcome);
		saveCoordinates = (Button)findViewById(R.id.savecoordinates);
		createEstn = (Button) findViewById(R.id.createestimation);
		editEstn = (Button) findViewById(R.id.editestimation);
		measurement = (Button) findViewById(R.id.measurement);
		taskProcess = (Button) findViewById(R.id.task);
		itemUpdate = (Button) findViewById(R.id.update);
		imageCapture = (Button) findViewById(R.id.image);
		
		saveCoordinates.setOnClickListener(this);
		createEstn.setOnClickListener(this);
		editEstn.setOnClickListener(this);
		measurement.setOnClickListener(this);
		taskProcess.setOnClickListener(this);
		itemUpdate.setOnClickListener(this);
		imageCapture.setOnClickListener(this);

	}
	public void onClick(View v) {
		int viewid = v.getId();
		Intent intent;
		switch (viewid) {
		case R.id.savecoordinates:
			
			break;
		case R.id.createestimation:
			intent = new Intent(WelcomeActivity.this, CreateEstimationActivity.class);
			startActivity(intent);
			
			break;
		case R.id.editestimation:
			intent = new Intent(WelcomeActivity.this,EditEstimationActivity.class);
			
			startActivity(intent);
			break;
		case R.id.measurement:
			
			
			break;
		case R.id.task:
			
			
			break;
		case R.id.update:
			
			
			break;
		case R.id.image:
			
			
			break;

		default:
			break;
		}
	
		
	}
	
	
}
