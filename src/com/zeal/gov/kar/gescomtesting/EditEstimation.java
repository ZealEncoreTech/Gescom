package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.zeal.gov.kar.gescom.calculation.ProjectEstimation;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.WorkItems;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class EditEstimation extends Activity implements OnClickListener {
	Spinner spnForWorkDesc, spnMeasurementUnit;
	TextView tvWorkDesc;
	EditText etQuantity, etCostPerItem, etAmount;
	ArrayAdapter<String> adapForWorkDesc, adapForMeasurementUnit;
	List<String> lstForWorkDesc, lstOfMeasurement;
	String strSpnDesc;
	private double total;
	Button btnCancel, btnSave;
	HashMap<String, Double> hmForCheckedFormula,hmForDBItemvalues;
	HashMap<String, WorkItems> hmForWorkItems = new HashMap<String, WorkItems>();
	HashMap<String, WorkItems> hmForWork = new HashMap<String, WorkItems>();
	HashMap<String, WorkItems> unCheck = new HashMap<String, WorkItems>();
	String strWorkItemTypeId,strWorkRowId,strMeasurementId;
	int measurementId;
	String strItemCodeOfChangedQuan;//"2","5", "6","7","8","9","10","12","13","15","16","24"
	Intent intentEditEst;
	final List<Integer> measurementIdsForDecimal =  new ArrayList<Integer>(Arrays.asList(2,5,6,7,8,9,10,12,13,15,16,24));
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editingestimation);
		spnForWorkDesc = (Spinner) findViewById(R.id.spnForWorkDesc);
		spnMeasurementUnit = (Spinner) findViewById(R.id.spnMeasurementUnits);
		tvWorkDesc = (TextView) findViewById(R.id.tvWorkDesc);

		etQuantity = (EditText) findViewById(R.id.etQuantity);
		etCostPerItem = (EditText) findViewById(R.id.etCostPerItem);
		etAmount = (EditText) findViewById(R.id.etAmount);


		intentEditEst = getIntent();
		strWorkRowId = intentEditEst.getStringExtra("workRowId");
		strSpnDesc = intentEditEst.getStringExtra("spnDesc");
		String strWorkDesc = intentEditEst.getStringExtra("workDesc");
		String strQuantity = intentEditEst.getStringExtra("Quantity");
		String strCostPerItem = intentEditEst.getStringExtra("costperItem");
		String strAmount = intentEditEst.getStringExtra("Amount");
		strWorkItemTypeId = intentEditEst.getStringExtra("workItemTypeID");
		hmForWorkItems = (HashMap<String, WorkItems>) intentEditEst
				.getSerializableExtra("hmForWorkItems");
		

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		tvWorkDesc.setText(strWorkDesc);
		etQuantity.setText(strQuantity);
		etCostPerItem.setText(strCostPerItem);
		etAmount.setText(strAmount);
		spinnerInitianlization();
		if(strWorkItemTypeId!=null){
			BaseService baseService = new BaseService(EditEstimation.this);
			int pos = baseService.getMeasurementId(Integer.parseInt(strWorkItemTypeId));
			spnMeasurementUnit.setSelection(pos);
		}
		if(!(measurementIdsForDecimal.contains(spnMeasurementUnit.getSelectedItemPosition()))){
			etQuantity.setText(""+Math.round(Double.valueOf(strQuantity)));
			etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
//			etQuantity.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
		}else{
			etQuantity.setText(strQuantity);
			etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
		}
		
		etQuantity.setOnKeyListener(new OnKeyListener() {                 
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	        	//You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
	        	if(keyCode == KeyEvent.KEYCODE_DEL){  
	        		//this is for backspace
	        		if(!etQuantity.getText().toString().isEmpty()){
	        			if(!(etQuantity.getText().toString().startsWith(".")) || etQuantity.getText().toString().endsWith(".")){
	        				etAmount.setText(String.valueOf(Double
	        						.valueOf(etCostPerItem.getText().toString())
	        						* Double.valueOf(etQuantity.getText().toString())));
	        			}
	        		}
	        	}
	        	return false;       
	        }
		});
		
		etQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (count >= 1) {
					if(!etQuantity.getText().toString().startsWith(".")){
						etAmount.setText(String.valueOf(Double
								.valueOf(etCostPerItem.getText().toString())
								* Double.valueOf(s.toString())));
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				etAmount.setFocusable(false);
			}
		});

		spnMeasurementUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> element, View arg1,
					int pos, long arg3) {

				if(pos > 0){
					measurementId = pos;
				}
				if(!(measurementIdsForDecimal.contains(pos))){
					etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
				}else{
					etQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> element) {

			}
		});

	}

	@SuppressWarnings("unused")
	private void spinnerInitianlization() {

		lstForWorkDesc = new ArrayList<String>();
		lstForWorkDesc.add(strSpnDesc);
		adapForWorkDesc = new ArrayAdapter<String>(EditEstimation.this,
				android.R.layout.simple_list_item_1, lstForWorkDesc);
		spnForWorkDesc.setAdapter(adapForWorkDesc);

		lstOfMeasurement = new ArrayList<String>();
		lstOfMeasurement.add("-select-");
		BaseService baseService = new BaseService(EditEstimation.this);
//		lstOfMeasurement.addAll(baseService.getMeasurementUnit());
		HashMap<String, Object> hmForMUnits = baseService.getMeasurementUnit();
		
		lstOfMeasurement.addAll((List<String>) hmForMUnits.get("List"));
		final HashMap<Integer, String> hmMUnits = (HashMap<Integer, String>) hmForMUnits.get("hmMUnits");
		adapForMeasurementUnit = new ArrayAdapter<String>(EditEstimation.this,
				android.R.layout.simple_list_item_1, lstOfMeasurement);
		spnMeasurementUnit.setAdapter(adapForMeasurementUnit);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSave:

			if(etQuantity.getText().toString().equals("") || etQuantity.getText().toString().trim().length() == 0){
				Toast.makeText(this, "Quantity cannot be empty, please enter some value before saving.", Toast.LENGTH_LONG).show();
			}
			else{
				UpdateEstimation update = new UpdateEstimation();
				update.execute();
			}
			//			updateEstimate();


			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}

	private long updateEstimate() {

		BaseService baseService = new BaseService(EditEstimation.this);
		int projCat = baseService.getProjectCategory(Integer.parseInt(strWorkRowId));
		ProjectEstimation estimate = new ProjectEstimation(EditEstimation.this,projCat,false,strWorkRowId);
		
		
		
		
		Iterator<Entry<String, WorkItems>> iterator = hmForWorkItems.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, WorkItems> entry = iterator.next();
			String itemCode = entry.getKey();
			WorkItems items = entry.getValue();
			if (items.getWorkItemTypeId() == Integer.parseInt(strWorkItemTypeId)) {
				items.setTotalUnits(Double.parseDouble(etQuantity.getText().toString()));
				items.setBaseRate(Double.parseDouble(etCostPerItem.getText().toString()));
				items.setTotalAmount(Double.parseDouble(etAmount.getText().toString()));
				items.setChanged(true);
				hmForWorkItems.remove(items.getItemCode());
				hmForWorkItems.put(items.getItemCode(), items);
			
				break;
			}
		}
		hmForWork.clear();
		hmForWork=estimate.processEditEstimationRequest(hmForWorkItems);
		hmForWorkItems.clear();
		total=estimate.getTotalAmount();
		long value = 0;
		Iterator<Entry<String, WorkItems>> itr = hmForWork.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, WorkItems> entry = itr.next();
			WorkItems items =entry.getValue();
			if(items.getWorkItemTypeId()==Integer.parseInt(strWorkItemTypeId)){
				items.setMeasurementid(measurementId);
			}
			
			value = baseService.updateWorkItemsTable(items);
		}

		return value;
		
	}

	public void onFinish()
	{
		
	}

	private class UpdateEstimation extends AsyncTask<String, Void, String>{

		private final ProgressDialog dialog = new ProgressDialog(EditEstimation.this);
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Updating Estimation....");
			this.dialog.setTitle("Please wait");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			String Result = new String();
			long value = updateEstimate();
			if(value>0){
				Result = "success";
            }
            else{
            	Result = "failed";
            }
			return Result;
		}
		@Override
		protected void onPostExecute(String strResult) {
			this.dialog.dismiss();
//			Intent intent = new Intent(EditEstimation.this, EditEstimationActivity.class);
			intentEditEst.putExtra("spnDesc", strSpnDesc);
			intentEditEst.putExtra("responseFromEditEst", strResult);
			intentEditEst.putExtra("Total", String.valueOf(total));
			setResult(1, intentEditEst);
			finish();
		}

	}

}
