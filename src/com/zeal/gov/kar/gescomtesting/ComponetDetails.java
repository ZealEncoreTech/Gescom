package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ComponetDetail;
import com.zeal.gov.kar.gescom.model.SavecoordDetails;
import com.zeal.gov.kar.gescom.session.Appuser;

public class ComponetDetails extends Activity implements OnItemSelectedListener{
	private TableLayout tblComponet;
	private Spinner spinProject,spinLineType,spinMaterialType;
	private List<String> projectList, lineTypeList, materialList;
	private ArrayAdapter<String> projectAdapter, lineTypeAdapter,materialTypeAdapter;
	private Button btnAddToList;
	private EditText edtTxtCaption;

	private HashMap<String, Integer> projectData;
	private HashMap<String,Integer> materialData,lineData;
	private ComponetDetail compDetails;
	private BaseService baseService;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_componet_details);
		baseService = new BaseService(ComponetDetails.this);
		tblComponet = (TableLayout) findViewById(R.id.componetTable);
		componetsTable();
		projectData=new HashMap<String, Integer>();
		materialData=new HashMap<String, Integer>();
		String status = "NEW";
		projectData = (HashMap<String, Integer>) baseService.getProjectforSavecordinates(status,Appuser.getUserName());

		edtTxtCaption=(EditText) findViewById(R.id.editTextCaption);
		edtTxtCaption.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){
				if(s != null && s.length() > 0 && edtTxtCaption.getError() != null) {
					edtTxtCaption.setError(null);
				}
			}
		});
//
//		materialData.put("Material-1", 1);
//		materialData.put("Material-2", 2);
//		materialData.put("Material-3", 3);
//		materialData.put("Material-4", 4);
		spinProject=(Spinner) findViewById(R.id.spinnerProject);
		spinLineType=(Spinner) findViewById(R.id.spinnerLineType);
		spinMaterialType=(Spinner) findViewById(R.id.spinnerMaterialType);

		btnAddToList=(Button) findViewById(R.id.addToList);
		btnAddToList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!(edtTxtCaption.getText().toString().isEmpty()) || spinLineType.getSelectedItemPosition()>0 || spinMaterialType.getSelectedItemPosition()>0)
				{
					compDetails=new ComponetDetail();
					compDetails.setCaption(edtTxtCaption.getText().toString());
					compDetails.setLineType(spinLineType.getSelectedItem().toString());
					compDetails.setMaterialUsed(spinMaterialType.getSelectedItem().toString());
					addTotable(compDetails);
				}
				else
				{
					Toast.makeText(ComponetDetails.this, "Please add Caption", Toast.LENGTH_SHORT).show();
				}

			}
		});

		projectList = new ArrayList<String>();
		projectList.add("Select Project");
		if(!projectData.isEmpty())
		{
			Iterator<Entry<String, Integer>> projectIterator = projectData.entrySet().iterator();
			while (projectIterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) projectIterator.next();
				projectList.add(pairs.getKey().toString());
			}
		}
		projectAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, projectList);
		spinProject.setAdapter(projectAdapter);
		spinProject.setOnItemSelectedListener(this);

		lineData = new HashMap<String, Integer>();
		lineTypeList = new ArrayList<String>();
		lineTypeList.add("Select Item");
		lineTypeList.add("HT-Line");
		lineTypeList.add("LT-Line");
		lineTypeList.add("Transformer");
		lineTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lineTypeList);
		spinLineType.setAdapter(lineTypeAdapter);
		spinLineType.setOnItemSelectedListener(this);
		lineData.put("Select Item", 0);
		lineData.put("HT-Line", 1);
		lineData.put("LT-Line", 2);
		lineData.put("Transformer", 3);

		materialList=new ArrayList<String>();
		materialList.add("Select Material");
	
		materialTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, materialList);
		spinMaterialType.setAdapter(materialTypeAdapter);
		spinMaterialType.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_componet_details, menu);
		return true;
	}

	private void componetsTable() {
		TableRow Header = new TableRow(this);
		for (int j = 0; j <= 3; j++) {
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
				cell.setText("Name");
			}
			if (j == 1) {
				cell.setText("Line Type");
			}
			if (j == 2) {
				cell.setText("Material Used");
			}

			if (j == 3) {
				cell.setText("Distance");
			}
			cell.setPadding(6, 4, 6, 4);
			cell.setTextColor(Color.WHITE);
			cell.setBackgroundColor(Color.rgb(100, 100, 100));
			Header.addView(cell);
		}
		tblComponet.addView(Header);
	}
	private void addTotable(ComponetDetail compDetails) {
		TableRow row = new TableRow(ComponetDetails.this);
		for (int j = 0; j <= 3; j++) {

			TextView cell = new TextView(ComponetDetails.this) {
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
				cell.setText(compDetails.getCaption());
			}
			if (j == 1) {
				cell.setText(compDetails.getLineType());
			}
			if (j == 2) {
				cell.setText(compDetails.getMaterialUsed());
			}
			if (j == 3) {
				cell.setText("0");
			}

			cell.setPadding(6, 4, 6, 4);
			row.addView(cell);
		}
		tblComponet.addView(row);
		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TableRow rowData = (TableRow) v;
				TextView discription = (TextView) rowData.getChildAt(0);
				String data=discription.getText().toString();
				TextView lineType = (TextView) rowData.getChildAt(1);
				TextView material = (TextView) rowData.getChildAt(2);
				String projectId = Integer.toString(projectData.get(spinProject.getSelectedItem().toString()));
				Intent captureCoordinates = new Intent(ComponetDetails.this, CaptureCoordinates.class);
				captureCoordinates.putExtra("projectId", projectId);
				captureCoordinates.putExtra("discription", data);
				captureCoordinates.putExtra("lineType", lineType.getText().toString());
				captureCoordinates.putExtra("material", Integer.toString(materialData.get(material.getText().toString())));
				startActivity(captureCoordinates);
			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> spnView, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		switch (spnView.getId()) {
		case R.id.spinnerLineType:
			if(pos>0){
				baseService = new BaseService(ComponetDetails.this);
				materialData.clear();
				int i = lineData.get(spnView.getItemAtPosition(pos));
				if(lineData.get(spnView.getItemAtPosition(pos))==3){
					materialData = baseService.getMaterialData("",lineData.get(spnView.getItemAtPosition(pos).toString()));
					Log.e("materialData = ", ""+materialData);
					updateMaterialDataSpinner();
				}
				else{
					materialData = 	baseService.getMaterialData("0",lineData.get(spnView.getItemAtPosition(pos).toString()));
					Log.e("materialData = ", ""+materialData);
					updateMaterialDataSpinner();
				}
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
	
	private void updateMaterialDataSpinner(){
		if(!materialData.isEmpty())
		{
			materialList.clear(); 
			materialList.add("Select Material");
			Iterator<Entry<String, Integer>> materialIterator = materialData.entrySet().iterator();
			while (materialIterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) materialIterator.next();
				materialList.add(pairs.getKey().toString());
			}
		}
	}
}
