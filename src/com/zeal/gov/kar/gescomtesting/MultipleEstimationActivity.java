package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.session.Appuser;




public class MultipleEstimationActivity extends Activity implements
		OnItemSelectedListener {

	private Spinner spinProject, spinItem;
	private List<String> projectList, itemList, descriptionList;
	private ArrayAdapter<String> projectAdapter, itemAdapter;
	private TableLayout tblEstDescription;
	private Button btnEstimation,btnBack,btnContiune;
	private EditText txtEstDescription;
	private BaseService baseService;
	private LayoutInflater layoutInflater = null;
	private HashMap<String, Integer> projectData;
	private int value=0,lineId, estimationId; 
	private String coordinatesAvailable;
	private int categoryId;
	ProjectEstimationType estimationType ;

	private  int previousPosition;

	private int previousposition,ltEstId;
	PopupWindow window;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiple_estimation);

		spinProject = (Spinner) findViewById(R.id.multiProjectSpinner);
		spinItem = (Spinner) findViewById(R.id.multiItemSpinner);
		tblEstDescription = (TableLayout) findViewById(R.id.discriptionTable);
		/*layoutInflater = LayoutInflater.from(this);
		View capturelayout = layoutInflater.inflate(R.layout.cameraoverlay, null);
		this.addContentView(capturelayout, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));*/
		txtEstDescription = (EditText) findViewById(R.id.multiEstDescription);
		txtEstDescription.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s != null && s.length() > 0
						&& txtEstDescription.getError() != null) {
					txtEstDescription.setError(null);
				}
			}
		});
		btnBack=(Button) findViewById(R.id.backMulti);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnContiune=(Button) findViewById(R.id.continueMulti);
		btnContiune.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(spinProject.getSelectedItemId()>0)
				{
				Intent startSavecord = new Intent(MultipleEstimationActivity.this, MapActivity.class);
				startSavecord.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startSavecord.putExtra("projectname", spinProject.getSelectedItem().toString());
				MultipleEstimationActivity.this.startActivity(startSavecord);
				finish();
				}
				else
				{
					Toast.makeText(MultipleEstimationActivity.this, "Please Select the Project", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		baseService = new BaseService(MultipleEstimationActivity.this);
      
		String status = "NEW";
		projectData = (HashMap<String, Integer>) baseService
				.getProjectforSavecordinates(status,Appuser.getUserName());
         
		
		projectList = new ArrayList<String>();
		projectList.add("Select Project");
		Iterator<Entry<String, Integer>> projectIterator = projectData
				.entrySet().iterator();
		
		/*while (projectIterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) projectIterator.next();
			
			projectList.add(pairs.getKey().toString());
			
		}*/
		
		projectList.addAll(baseService.getProjectforSavecordinate(status,Appuser.getUserName()));
		
		projectAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, projectList);
		
		spinProject.setAdapter(projectAdapter);
		spinProject.setOnItemSelectedListener(this);
		
		itemList = new ArrayList<String>();
		//itemList.add("Select Line Type");
		itemList.add("Select Line Type");
		itemList.add("HT-Line");
		itemList.add("LT-Line");
		itemList.add("Transformer");
	itemAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, itemList);
	 spinItem.setAdapter(itemAdapter);
		/*itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemList);
		spinItem.setAdapter(itemAdapter);*/
		
		/*if(categoryId == 1){
			itemList.add("Transformer");
		}else{
			
			itemList.add("HT-Line");
			itemList.add("LT-Line");
			itemList.add("Transformer");
		}
		itemAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemList);
		spinItem.setAdapter(itemAdapter);*/
		spinItem.setOnItemSelectedListener(this);
          
		descriptionList = new ArrayList<String>();

		btnEstimation = (Button) findViewById(R.id.saveEstDescription);
		btnEstimation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(categoryId == 1){
			int i = baseService.getEstimationId(projectData.get(spinProject
						.getSelectedItem().toString()));
			System.out.println(i);
			if(i > 0){
				Toast.makeText(MultipleEstimationActivity.this,"Estimation already added",Toast.LENGTH_LONG).show();
			}else{
				int count = 0;
				if(spinProject.getSelectedItemId()>0)
				{
					if(spinItem.getSelectedItemId()>0){
						

						/*if(spinItem.getSelectedItemId()==2)
				{*/

						if (!txtEstDescription.getText().toString().isEmpty()) {
							if (!baseService.isEstimationDescriptionAvailable(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),
											txtEstDescription.getText().toString()))


							{

								if(baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject.getSelectedItem()
												.toString())), Integer.toString(spinItem
														.getSelectedItemPosition()))<1)
								{
									createTableHeader();
								}
								count = baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject
												.getSelectedItem().toString())),
												Integer.toString(spinItem
														.getSelectedItemPosition()));
								int htCount = baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject
												.getSelectedItem().toString())),"1"); 
								if(spinItem.getSelectedItem().toString().equalsIgnoreCase("LT-Line") && htCount>0){
									showHTSelectionForLt(htCount);
								}
								else if(spinItem.getSelectedItem().toString().equalsIgnoreCase("Transformer") && htCount>0){
									showHTSelectionForTc(htCount);
								}
								else{
									ProjectEstimationType petRow = new ProjectEstimationType();
									petRow.setLineId(Integer.toString(spinItem
											.getSelectedItemPosition()));
									petRow.setProjectId(projectData.get(spinProject
											.getSelectedItem().toString()));
									petRow.setEstimationId(++count);
									petRow.setEstDescription(txtEstDescription.getText().toString());
									petRow.setLtEstId(0);
									baseService.addToProjectEstimationType(petRow);
									addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),"");
									txtEstDescription.setText("");
									Toast.makeText(MultipleEstimationActivity.this,
											"Estimation Saved", Toast.LENGTH_SHORT).show();
								}

							} else {
								txtEstDescription
								.setError("Please Enter different Description");
							}

						} else {
							txtEstDescription.setError("Please Enter Description");

						}
						/*	}
				else {
					Toast.makeText(MultipleEstimationActivity.this, "Multiple estimation is not allowed for "+spinItem.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

				}*/
					}
					else{
						Toast.makeText(MultipleEstimationActivity.this, "Please select Line Type", Toast.LENGTH_LONG).show();
					}
				}
			
			else {
				Toast.makeText(MultipleEstimationActivity.this, "Please select project", Toast.LENGTH_LONG).show();

			}
			
			}	
			}else if(categoryId == 8){
				boolean estidone = baseService.IsestIdone(Integer.toString(projectData.get(spinProject.getSelectedItem().toString())),1);
				if(spinItem.getSelectedItemPosition() >1){
				if(estidone){
					int count =0;

					if (!txtEstDescription.getText().toString().isEmpty()) {
						if (!baseService.isEstimationDescriptionAvailable(Integer
								.toString(projectData.get(spinProject
										.getSelectedItem().toString())),
										txtEstDescription.getText().toString()))


						{

							if(baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject.getSelectedItem()
											.toString())), Integer.toString(spinItem
													.getSelectedItemPosition()))<1)
							{
								createTableHeader();
							}
							count = baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),
											Integer.toString(spinItem
													.getSelectedItemPosition()));
							int htCount = baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),"1"); 
							if(spinItem.getSelectedItem().toString().equalsIgnoreCase("LT-Line") && htCount>0){
								showHTSelectionForLt(htCount);
							}
							else if(spinItem.getSelectedItem().toString().equalsIgnoreCase("Transformer") && htCount>0){
								showHTSelectionForTc(htCount);
							}
							else{
								ProjectEstimationType petRow = new ProjectEstimationType();
								petRow.setLineId(Integer.toString(spinItem
										.getSelectedItemPosition()));
								petRow.setProjectId(projectData.get(spinProject
										.getSelectedItem().toString()));
								petRow.setEstimationId(++count);
								petRow.setEstDescription(txtEstDescription.getText().toString());
								petRow.setLtEstId(0);
								baseService.addToProjectEstimationType(petRow);
								addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),"");
								txtEstDescription.setText("");
								Toast.makeText(MultipleEstimationActivity.this,
										"Estimation Saved", Toast.LENGTH_SHORT).show();
							}

						} else {
							txtEstDescription
							.setError("Please Enter different Description");
						}

					} else {
						txtEstDescription.setError("Please Enter Description");

					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Please enter Ht", Toast.LENGTH_LONG).show();
				}
				}else{
					int count =0;

					if (!txtEstDescription.getText().toString().isEmpty()) {
						if (!baseService.isEstimationDescriptionAvailable(Integer
								.toString(projectData.get(spinProject
										.getSelectedItem().toString())),
										txtEstDescription.getText().toString()))


						{

							if(baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject.getSelectedItem()
											.toString())), Integer.toString(spinItem
													.getSelectedItemPosition()))<1)
							{
								createTableHeader();
							}
							count = baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),
											Integer.toString(spinItem
													.getSelectedItemPosition()));
							int htCount = baseService.getEstimationId(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),"1"); 
							if(spinItem.getSelectedItem().toString().equalsIgnoreCase("LT-Line") && htCount>0){
								showHTSelectionForLt(htCount);
							}
							else if(spinItem.getSelectedItem().toString().equalsIgnoreCase("Transformer") && htCount>0){
								showHTSelectionForTc(htCount);
							}
							else{
								ProjectEstimationType petRow = new ProjectEstimationType();
								petRow.setLineId(Integer.toString(spinItem
										.getSelectedItemPosition()));
								petRow.setProjectId(projectData.get(spinProject
										.getSelectedItem().toString()));
								petRow.setEstimationId(++count);
								petRow.setEstDescription(txtEstDescription.getText().toString());
								petRow.setLtEstId(0);
								baseService.addToProjectEstimationType(petRow);
								addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),"");
								txtEstDescription.setText("");
								Toast.makeText(MultipleEstimationActivity.this,
										"Estimation Saved", Toast.LENGTH_SHORT).show();
							}

						} else {
							txtEstDescription
							.setError("Please Enter different Description");
						}
					}
				}
				
				
			}
				else{

				int count = 0;
				if(spinProject.getSelectedItemId()>0)
				{
					if(spinItem.getSelectedItemId()>0){
						

						/*if(spinItem.getSelectedItemId()==2)
				{*/

						if (!txtEstDescription.getText().toString().isEmpty()) {
							if (!baseService.isEstimationDescriptionAvailable(Integer
									.toString(projectData.get(spinProject
											.getSelectedItem().toString())),
											txtEstDescription.getText().toString()))


							{

								if(baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject.getSelectedItem()
												.toString())), Integer.toString(spinItem
														.getSelectedItemPosition()))<1)
								{
									createTableHeader();
								}
								count = baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject
												.getSelectedItem().toString())),
												Integer.toString(spinItem
														.getSelectedItemPosition()));
								int htCount = baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject
												.getSelectedItem().toString())),"1"); 
								if(spinItem.getSelectedItem().toString().equalsIgnoreCase("LT-Line") && htCount>0){
									showHTSelectionForLt(htCount);
								}
								else if(spinItem.getSelectedItem().toString().equalsIgnoreCase("Transformer") && htCount>0){
									showHTSelectionForTc(htCount);
								}
								else{
									ProjectEstimationType petRow = new ProjectEstimationType();
									petRow.setLineId(Integer.toString(spinItem
											.getSelectedItemPosition()));
									petRow.setProjectId(projectData.get(spinProject
											.getSelectedItem().toString()));
									petRow.setEstimationId(++count);
									petRow.setEstDescription(txtEstDescription.getText().toString());
									petRow.setLtEstId(0);
									baseService.addToProjectEstimationType(petRow);
									addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),"");
									txtEstDescription.setText("");
									Toast.makeText(MultipleEstimationActivity.this,
											"Estimation Saved", Toast.LENGTH_SHORT).show();
								}

							} else {
								txtEstDescription
								.setError("Please Enter different Description");
							}

						} else {
							txtEstDescription.setError("Please Enter Description");

						}
						/*	}
				else {
					Toast.makeText(MultipleEstimationActivity.this, "Multiple estimation is not allowed for "+spinItem.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

				}*/
					}
					else{
						Toast.makeText(MultipleEstimationActivity.this, "Please select Line Type", Toast.LENGTH_LONG).show();
					}
				}
			
			else {
				Toast.makeText(MultipleEstimationActivity.this, "Please select project", Toast.LENGTH_LONG).show();

			}
			
			
				
			}
				
				
			}
		});

	}

	private void showFullDetails(String detial)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				MultipleEstimationActivity.this).create();
		alertDialog.setMessage(detial);
		alertDialog.setButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(
					DialogInterface dialog,
					int which) {
			}
		});
		alertDialog.show();
	}
	protected void showHTSelectionForTc(int htCount) {

		// TODO Auto-generated method stub
		final Map<String, String>  htMap = baseService.getEstimationDescriptionMap(
				Integer.toString(projectData.get(spinProject
						.getSelectedItem().toString())),Integer.toString(1));
		/*final List<String> htList=new ArrayList<String>();
		Iterator<Entry<String, String>> htMapIt = htMap.entrySet()
				.iterator();
		while (htMapIt.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) htMapIt.next();
			htList.add(pairs.getValue().toString());
			
		}*/
		Object[]   key   =     htMap.keySet().toArray();  
		 Arrays.sort(key);
		 final List<String> htList=new ArrayList<String>();
		 
		 for (int i = 0; i < key.length; i++) {
		 System.out.println(htMap.get(key[i]));
		 htList.add(htMap.get(key[i]));
		 }
		ArrayAdapter<String> htAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, htList);
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setTitle("Select HT-Line for TC");
	    alt_bld.setSingleChoiceItems(htAdapter, -1, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	        	int data=Integer.parseInt(getKeyFromValue(htMap,htList.get(item)));
	        	ProjectEstimationType petRow = new ProjectEstimationType();
				petRow.setLineId(Integer.toString(spinItem
						.getSelectedItemPosition()));
				petRow.setProjectId(projectData.get(spinProject
						.getSelectedItem().toString()));
				petRow.setEstimationId(data);
				petRow.setEstDescription(txtEstDescription.getText()
						.toString());
				int tcCount = baseService.getLtEstCount(String.valueOf(projectData.get(spinProject
						.getSelectedItem().toString())),data,String.valueOf(spinItem.getSelectedItemPosition()));
				petRow.setLtEstId(++tcCount);
				baseService.addToProjectEstimationType(petRow);
				
				addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),htList.get(item));
				txtEstDescription.setText("");
				Toast.makeText(MultipleEstimationActivity.this,"Estimation Saved", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
	        
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.show();

	
		
	}

	protected void showHTSelectionForLt(final int htcount) {
		// TODO Auto-generated method stub
		final Map<String, String>  htMap = baseService.getEstimationDescriptionMap(
				Integer.toString(projectData.get(spinProject
						.getSelectedItem().toString())),Integer.toString(1));
		 Object[]   key   =     htMap.keySet().toArray();  
		 Arrays.sort(key);
		 final List<String> htList=new ArrayList<String>();
		 
		 for (int i = 0; i < key.length; i++) {
		 System.out.println(htMap.get(key[i]));
		 htList.add(htMap.get(key[i]));
		 }
		
		/*Iterator<Entry<String, String>> htMapIt = htMap.entrySet()
				.iterator();

		while (htMapIt.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) htMapIt.next();
			htList.add(pairs.getValue().toString());
			
		}*/
		ArrayAdapter<String> htAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, htList);
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setTitle("Select HT-Line for LT");
	    alt_bld.setSingleChoiceItems(htAdapter, -1, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	        	int data=Integer.parseInt(getKeyFromValue(htMap,htList.get(item)));
	        	ProjectEstimationType petRow = new ProjectEstimationType();
				petRow.setLineId(Integer.toString(spinItem
						.getSelectedItemPosition()));
				petRow.setProjectId(projectData.get(spinProject
						.getSelectedItem().toString()));
				petRow.setEstimationId(data);
				petRow.setEstDescription(txtEstDescription.getText()
						.toString());
				int ltCount = baseService.getLtEstCount(String.valueOf(projectData.get(spinProject
						.getSelectedItem().toString())),data,String.valueOf(spinItem.getSelectedItemPosition()));
				petRow.setLtEstId(++ltCount);
				baseService.addToProjectEstimationType(petRow);
				addTotable(txtEstDescription.getText().toString(),spinItem.getSelectedItem().toString(),htList.get(item));
				txtEstDescription.setText("");
				Toast.makeText(MultipleEstimationActivity.this,"Estimation Saved", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.multiProjectSpinner:
			if(spinProject.getSelectedItemId()>0)
			{
			showFullDetails(String.valueOf(projectData.get(spinProject
					.getSelectedItem().toString()))+" - "+spinProject.getSelectedItem().toString());
			 value = projectData.get(spinProject
						.getSelectedItem().toString());
			 spinItem.setSelection(0);
			 
			}
			
		break;
		
		case R.id.multiItemSpinner:
			if(spinProject.getSelectedItemId()>0)
			{

				if (arg2 == 2 || arg2 == 1 || arg2 == 3) {
					tblEstDescription.removeAllViews();
					if(baseService.getEstimationId(Integer
							.toString(projectData.get(spinProject.getSelectedItem()
									.toString())), Integer.toString(spinItem
											.getSelectedItemPosition()))>0)
					{
						categoryId = baseService.getProjectCategory(projectData.get(spinProject
								.getSelectedItem().toString()));
						if(categoryId == 1){
							if(arg2 !=3){
								createTableHeader();
								updateMultiDescriptionSpinner();
								spinItem.setAdapter(itemAdapter);
								spinItem.setSelection(3,true);
								Toast.makeText(MultipleEstimationActivity.this,"Category 1 supports only for transformer",Toast.LENGTH_LONG).show();
							}else{
								createTableHeader();
								updateMultiDescriptionSpinner();
							}

						}else{

							createTableHeader();
							updateMultiDescriptionSpinner();
						}
					}
					else
					{
						categoryId = baseService.getProjectCategory(projectData.get(spinProject
								.getSelectedItem().toString()));
						if(categoryId == 1){
							if(arg2 !=3){

								Toast.makeText(MultipleEstimationActivity.this,"Category 1 supports only for transformer",Toast.LENGTH_LONG).show();
								spinItem.setAdapter(itemAdapter);
								spinItem.setSelection(3,true);
							}else{
								if(!(baseService.getEstimationId(Integer
										.toString(projectData.get(spinProject.getSelectedItem()
												.toString())), Integer.toString(spinItem
														.getSelectedItemPosition()))>0))
								{
									Toast.makeText(MultipleEstimationActivity.this, "Please Add Estimations", Toast.LENGTH_LONG).show();
								}

							}
						}else{
							Toast.makeText(MultipleEstimationActivity.this, "Please Add Estimations", Toast.LENGTH_LONG).show();
						}
					}

				}
				else
				{
					tblEstDescription.removeAllViews();
				}
			}
			else
			{
				Toast.makeText(MultipleEstimationActivity.this, "Please Select the Project", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	private void updateMultiDescriptionSpinner() {
		
		
		HashMap<String, String> hm = baseService.getPrevEstList(Integer.toString(projectData.get(spinProject.getSelectedItem()
				.toString())),Integer.toString(spinItem
						.getSelectedItemPosition()));
		/*Set<Entry<String, String>> set = hm.entrySet();
		Iterator<Entry<String, String>> itr = set.iterator();
		while(itr.hasNext()){
			Entry<String, String> entry = itr.next();
			addTotable(entry.getKey().toString(),spinItem.getSelectedItem().toString(),entry.getValue().toString());
		}
		if(hm.size()==0){
			descriptionList.clear();
			List<String> discpList = new ArrayList<String>();
			discpList = baseService.getEstimationDescription(Integer
					.toString(projectData.get(spinProject.getSelectedItem()
							.toString())), Integer.toString(spinItem
					.getSelectedItemPosition()));
			Iterator<String> itdiscpList = discpList.iterator();
			while (itdiscpList.hasNext()) {
				//descriptionList.add(itdiscpList.next());
				addTotable(itdiscpList.next().toString(),spinItem.getSelectedItem().toString(),"");
			}
		}*/
		List<ProjectEstimationType> l = baseService.getProjectEstimationTypeList(projectData.get(spinProject.getSelectedItem()
				.toString()));
		Iterator<ProjectEstimationType> i = l.iterator();
		while(i.hasNext()){
			ProjectEstimationType type = i.next();
			if(spinItem.getSelectedItemPosition() == Integer.valueOf(type.getLineId())){
				addTotable(type.getEstDescription(),spinItem.getSelectedItem().toString(),hm.get(type.getEstDescription()));
			}
		}
	}
	

	private void createTableHeader() {
		TableRow Header = new TableRow(this);
        for(int i=0 ; i<=2;i++)
        	
        {
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
		cell.setTextColor(Color.WHITE);
		cell.setSingleLine(true);
	
		if(i==0)
		{
		cell.setText("Line Type");
		}
		if(i==1)
		{
		cell.setText("Descriptions");
		}
		if(i==2)
		{
			cell.setText("HT-Line Selected");		
		
		}
		cell.setPadding(6, 4, 6, 4);
		cell.setBackgroundColor(Color.rgb(100, 100, 100));
		Header.addView(cell);
        }
		tblEstDescription.addView(Header);
	}

	private void addTotable(String description,String lineType, String htType) {
		final TableRow row = new TableRow(this);
		 for(int i=0 ; i<=2;i++)
	        	
	        {
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
		cell.setSingleLine(true);
		if(i==0)
		{
		cell.setText(lineType);
		}
		if(i==1)
		{
		cell.setText(description);
		}
		if(i==2){
			cell.setText(htType);
		}
		
		cell.setPadding(6, 4, 6, 4);
		row.addView(cell);
	      }
		tblEstDescription.addView(row);
row.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableRow row = (TableRow) v;
				
				
				TextView lineType = (TextView) row.getChildAt(0);
				String strlineTpe = lineType.getText().toString();
				if(strlineTpe.equalsIgnoreCase("HT-Line")){
					lineId = 1;
				}else if(strlineTpe.equalsIgnoreCase("LT-Line")){
					lineId = 2;
				}
				else if(strlineTpe.equalsIgnoreCase("Transformer")){
					lineId = 3;
				}
				 estimationType = new ProjectEstimationType();
			TextView workDesc = (TextView) row.getChildAt(1);
			 final String desc = workDesc.getText().toString();
				baseService = new BaseService(MultipleEstimationActivity.this);
				List<ProjectEstimationType> lstestimate =  baseService.getProjectEstimationTypeList(value);
                 Iterator<ProjectEstimationType> itr = lstestimate.iterator();
                 while(itr.hasNext()){
                	 ProjectEstimationType element  = itr.next();
                	 if(element.getEstDescription().equalsIgnoreCase(workDesc.getText().toString())){
                		 estimationId = element.getEstimationId();
                		 estimationType = element;
                		 
                		 break;
                	 }
                	
                 }
                 
                
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.listview_popup, null); 
				 window = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
				 window.setBackgroundDrawable(new BitmapDrawable());
				 window.setFocusable(true);
				ListView listView = (ListView) view.findViewById(R.id.lvPopup);
				List<String> lst = new ArrayList<String>();
				lst.add("Edit");
				lst.add("Delete");
				lst.add("Cancel");
				ArrayAdapter<String> options = new ArrayAdapter<String>(MultipleEstimationActivity.this,
			              android.R.layout.simple_list_item_1, android.R.id.text1, lst);
				listView.setAdapter(options);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						String selectedView = arg0.getItemAtPosition(arg2).toString();
						ltEstId = baseService.getLTEstId(String.valueOf(value), String.valueOf(estimationId),lineId,desc);
						if(selectedView.equalsIgnoreCase("Delete")){
							if(!baseService.iscoordinatesavailable(String.valueOf(value),String.valueOf(ltEstId),String.valueOf(lineId),String.valueOf(estimationId))){
							AlertDialog.Builder alert = new AlertDialog.Builder(MultipleEstimationActivity.this);
				            alert.setTitle("Alert"); //Set Alert dialog title here
				            alert.setMessage("your data will be removed permanently"); //Message here
				 
				            // Set an EditText view to get user input 
				            
				           
				 
				            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int whichButton) {
				             //You will get as string input data in this variable.
				             // here we convert the input to a string and show in a toast.
				            	  
									
								
									/*spinItem.setSelection(previousposition);   */
									
									deleteEstimateType(estimationId,lineId,estimationType);

									 
									      

				            } // End of onClick(DialogInterface dialog, int whichButton)
				        }); //End of alert.setPositiveButton
				            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				              public void onClick(DialogInterface dialog, int whichButton) {
				                // Canceled.
				                  dialog.cancel();
				              }
				        }); //End of alert.setNegativeButton
				            AlertDialog alertDialog = alert.create();
				            alertDialog.show();
							
							
							}else{
								Toast.makeText(MultipleEstimationActivity.this, "Coordinates already taken for this work,so you can't delete",Toast.LENGTH_LONG).show();
							} }
						if(selectedView.equalsIgnoreCase("Edit")){
							/*if(!baseService.iscoordinatesavailable(String.valueOf(value))){
								
							}else{
								Toast.makeText(MultipleEstimationActivity.this, "Coordinates already taken for this work,so you can't Edit",Toast.LENGTH_LONG).show();
							}*/
							 AlertDialog.Builder alert = new AlertDialog.Builder(MultipleEstimationActivity.this);
					            alert.setTitle("Edit the description"); //Set Alert dialog title here
					            alert.setMessage("Enter the description"); //Message here
					 
					            // Set an EditText view to get user input 
					            final EditText input = new EditText(MultipleEstimationActivity.this);
					            alert.setView(input);
					 
					            alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int whichButton) {
					             //You will get as string input data in this variable.
					             // here we convert the input to a string and show in a toast.
//					             previousPosition=spinItem.getSelectedItemPosition();	
//					            
//
//					             baseService.updateDescription(value,desc,srt);
//					             Toast.makeText(MultipleEstimationActivity.this,srt,Toast.LENGTH_LONG).show();
//					             spinItem.setAdapter(itemAdapter);
//					             spinItem.setSelection(previousPosition,true);
					            	
					           String srt = input.getEditableText().toString();
					             if(!desc.equals(srt)){
					            	 if(!baseService.isEstimationDescriptionAvailable(String.valueOf(value),srt)){
							             baseService.updateDescription(value,desc,srt);
							             previousposition=spinItem.getSelectedItemPosition();	

								 			
								 			
							    		 spinItem.setAdapter(itemAdapter);
							    		 spinItem.setSelection(previousposition,true);
							    		 Toast.makeText(MultipleEstimationActivity.this,"Updated",Toast.LENGTH_LONG).show();
							             }else{
							            	 Toast.makeText(MultipleEstimationActivity.this,"Description Already exist Please enter different description",Toast.LENGTH_LONG).show();
							             } 
					            	
					             }else{
					            	 Toast.makeText(MultipleEstimationActivity.this,"You have entered same description. Please enter different description", Toast.LENGTH_LONG).show();
					             } 
					             

					            } // End of onClick(DialogInterface dialog, int whichButton)
					        }); //End of alert.setPositiveButton
					            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					              public void onClick(DialogInterface dialog, int whichButton) {
					                // Canceled.
					                  dialog.cancel();
					              }
					        }); //End of alert.setNegativeButton
					            AlertDialog alertDialog = alert.create();
					            alertDialog.show();

						/*spinItem.setSelection(0);
						itemAdapter.notifyDataSetChanged();*/
						
							

						//spinItem.setSelection(0);

						}
						if(selectedView.equalsIgnoreCase("Cancel")){
							
						}
						Toast.makeText(MultipleEstimationActivity.this, "You have selected "+selectedView, Toast.LENGTH_LONG).show();
						window.dismiss();
					}
				});

				window.showAtLocation(view, Gravity.CENTER, 20, 20);
			}
		});
	}
	
 public void deleteEstimateType(int estimationId,int lineId, ProjectEstimationType estimationType2){
	 BaseService baseService = new BaseService(MultipleEstimationActivity.this);
	 int count = baseService.isEstimationTypeAvailable(String.valueOf(value),String.valueOf(lineId));
	 if(lineId ==1){
		 baseService.deleteEst(String.valueOf(estimationId), String.valueOf(value));
		 for(int i=estimationId;i<count;i++){
			baseService.upDateEstimationID(String.valueOf(value),i+1,i); 
			
		 }
		 previousposition=spinItem.getSelectedItemPosition();	

		
		 			
		 spinItem.setAdapter(itemAdapter);
		 spinItem.setSelection(previousposition,true);
		
		
		 
		 
	 }
	 if(lineId == 2){
		 int ltcount = baseService.getLtEstCount(String.valueOf(value), estimationId, String.valueOf(lineId));
		 baseService.deleteEst(String.valueOf(estimationId), String.valueOf(value),String.valueOf(lineId),String.valueOf(estimationType2.getLtEstId()));
		 for(int i =estimationType2.getLtEstId();i<ltcount;i++){
			 baseService.upDateltEstimationID(String.valueOf(value),i+1,i);
			
		 }
		 
		 previousposition=spinItem.getSelectedItemPosition();	

			
			
		 spinItem.setAdapter(itemAdapter);
		 spinItem.setSelection(previousposition,true);
		
	 }
	 if(lineId == 3){
		 int lineId1 =2;
		 int ltcount = baseService.getLtEstCount(String.valueOf(value), estimationId, String.valueOf(lineId));
		 baseService.deleteEst(String.valueOf(estimationId), String.valueOf(value),String.valueOf(lineId),String.valueOf(estimationType2.getLtEstId()));
		 baseService.deleteEst(String.valueOf(estimationId), String.valueOf(value),lineId1);
		 for(int i =estimationType2.getLtEstId();i<=ltcount;i++){
			 baseService.upDateltEstimationID(String.valueOf(value),i+1,i);
		 }
		 
		 
		 previousposition=spinItem.getSelectedItemPosition();	

			
			
		 spinItem.setAdapter(itemAdapter);
		 spinItem.setSelection(previousposition,true);
	 }
 }
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	
		// TODO Auto-generated method stub

	}
	
	public String getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o.toString();
			}
		}
		return null;
	}
	
 
}
