package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.GescomUtility;
import com.zeal.gov.kar.gescom.model.WorkItems;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Editmeasurement extends Activity implements OnClickListener{
	Spinner spnForWorkDesc,spnMeasurementUnit;
	TextView tvWorkDesc;
	EditText etQuantity,etCostPerItem,etAmount;
	ArrayAdapter<String> adapForWorkDesc,adapForMeasurementUnit;
	List<String> lstForWorkDesc,lstOfMeasurement;
	String strSpnDesc ;
	Button btnCancel,btnSave;
	HashMap<String, Double> hmForCheckedFormula;
	String strSelectedmeasurement;
	int measurementid;
	String strworkitemtypeid,strWorkRowId;
	WorkItems items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editmeasurement);
		spnForWorkDesc = (Spinner) findViewById(R.id.spnForWorkDesc);
		spnMeasurementUnit = (Spinner) findViewById(R.id.spnMeasurementUnits);
		tvWorkDesc = (TextView) findViewById(R.id.tvWorkDesc);


		etQuantity = (EditText) findViewById(R.id.etQuantity);
		etCostPerItem = (EditText) findViewById(R.id.etCostPerItem);
		etAmount = (EditText) findViewById(R.id.etAmount);

		Intent intentEditEst = getIntent();
		items = (WorkItems) intentEditEst.getSerializableExtra("workItems");
		strSpnDesc = intentEditEst.getStringExtra("spnDesc");
		strworkitemtypeid = String.valueOf(items.getWorkItemTypeId());

		String strWorkDesc = items.getWorkItemDescription();
		double strQuantity = items.getMeasurmentQuantity();
		double strestimatedQuantity = items.getTotalUnits();
		double  strRemainingQuantity = items.getTotalUnits()-items.getMeasurmentQuantity();

		strWorkRowId = intentEditEst.getStringExtra("workRowId");
		BaseService baseService = new BaseService(getApplicationContext());
		int measurementdescription = baseService.getmeasurementID(strworkitemtypeid,String.valueOf(baseService.getWorkRowId(strSpnDesc)));

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		tvWorkDesc.setText(strWorkDesc);
		etQuantity.setText(String.valueOf(strQuantity));
		etCostPerItem.setText(String.valueOf(strestimatedQuantity));
		etAmount.setText(String.valueOf((strRemainingQuantity)));
		spinnerInitianlization();
		spnMeasurementUnit.setSelection(measurementdescription);
		etQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(count>=1 ){
					etAmount.setText(String.valueOf(Double.valueOf(etCostPerItem.getText().toString()) - Double.valueOf(s.toString())));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				etAmount.setFocusable(false);
			}
		});
		spnMeasurementUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {
				if(pos>0){
					BaseService baseService = new BaseService(getApplicationContext());

					strSelectedmeasurement= element.getItemAtPosition(pos).toString();
					measurementid = baseService.getmeasurementid((strSelectedmeasurement));

				}

			}




			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});


	}

	private void spinnerInitianlization() {
		// TODO Auto-generated method stub
		lstForWorkDesc = new ArrayList<String>();
		lstForWorkDesc.add(strSpnDesc);
		adapForWorkDesc = new ArrayAdapter<String>(Editmeasurement.this, android.R.layout.simple_list_item_1,lstForWorkDesc);
		spnForWorkDesc.setAdapter(adapForWorkDesc);

		lstOfMeasurement = new ArrayList<String>();
		lstOfMeasurement.add("-select-");
		BaseService baseService = new BaseService(Editmeasurement.this);
//		lstOfMeasurement.addAll(baseService.getMeasurementUnit());
		HashMap<String, Object> hmForMUnits = baseService.getMeasurementUnit();
		
		lstOfMeasurement.addAll((List<String>) hmForMUnits.get("List"));
		final HashMap<Integer, String> hmMUnits = (HashMap<Integer, String>) hmForMUnits.get("hmMUnits");
		adapForMeasurementUnit = new ArrayAdapter<String>(Editmeasurement.this, android.R.layout.simple_list_item_1,lstOfMeasurement);
		spnMeasurementUnit.setAdapter(adapForMeasurementUnit);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.btnSave:
			if(Double.valueOf(etQuantity.getText().toString())<= items.getTotalUnits()){
				updateMeasurement();
			}else{
				Toast.makeText(getApplicationContext(), "Quantity should be less than or equal to EstimatedQuantity",Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}

	private void updateMeasurement() {
		// TODO Auto-generated method stub
		HashMap<Integer, Double> hm = GescomUtility.getHmQuantityOver();
		if(hm!=null){
		if(!hm.containsKey(Integer.valueOf(strworkitemtypeid))){
		hm.put(Integer.valueOf(strworkitemtypeid), Double.valueOf(etQuantity.getText().toString()));
		}
		else{
			hm.remove(Integer.valueOf(strworkitemtypeid));
			hm.put(Integer.valueOf(strworkitemtypeid), Double.valueOf(etQuantity.getText().toString()));
		}}
		else{
			hm = new HashMap<Integer, Double>();
			hm.put(Integer.valueOf(strworkitemtypeid), Double.valueOf(etQuantity.getText().toString()));
		}
		if(GescomUtility.getHmQuantityOver()!=null){
			if(!GescomUtility.getHmQuantityOver().containsKey(Integer.parseInt(strworkitemtypeid))){
				GescomUtility.setHmQuantityOver(hm);
			}
		}
		else{
			GescomUtility.setHmQuantityOver(hm);
		}
		Log.e("mTotalAmout", ""+Double.parseDouble(etQuantity.getText().toString())*items.getCostPerItem());
		double mTotalAmount = Double.parseDouble(etQuantity.getText().toString())*items.getCostPerItem();
		Log.e("mTotalAmout", ""+mTotalAmount);
		WorkItems items = new WorkItems();
		items.setMeasurementid(measurementid);	
		items.setMeasurmentQuantity(Double.parseDouble(etQuantity.getText().toString()));
		items.setmTotalAmount(mTotalAmount);
		items.setWorkItemTypeId(Integer.parseInt(strworkitemtypeid));
		BaseService baseService = new BaseService(getApplicationContext());
		long value = baseService.updateworkitemsformeasurement(items,strWorkRowId);
		String response;
		if(value>0){
			response = "success";
		}
		else{
			response = "failed";
		}
		Intent intent = new Intent(Editmeasurement.this,
				MeasurementActivity.class);
		intent.putExtra("responsefrommeasurement", response);
		intent.putExtra("workAItemTypeId", strworkitemtypeid);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
		finish();		
	}


}






