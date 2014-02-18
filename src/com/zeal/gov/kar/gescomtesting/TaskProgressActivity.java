package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.zeal.gov.kar.gescom.Internet.Internet;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.GescomUtility;
import com.zeal.gov.kar.gescom.model.MainTaskDetailsWork;
import com.zeal.gov.kar.gescom.model.SubTaskDetailsWork;
import com.zeal.gov.kar.gescom.model.Task;
import com.zeal.gov.kar.gescom.model.TaskDetailsLists;
import com.zeal.gov.kar.gescom.parser.Parser;
import com.zeal.gov.kar.gescom.proxy.Soapproxy;
import com.zeal.gov.kar.gescom.session.Appuser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class TaskProgressActivity extends Activity implements OnClickListener {

	private Spinner spnMainWorkDesc, spnSubWorkDesc;
	private ArrayAdapter<String> aadapMainWork, aadapSubWork;
	private Button btnBack, btnUpdate;
	private List<String> lstMainWorkDesc, lstSubWorkDesc;
	private int mainWorkRowId, subWorkRowId;
	private HashMap<Integer, Integer> hmMainWork;
	private HashMap<Integer, SubTaskDetailsWork> hmSubWork;
	private TableLayout tlForTask;
	private TableRow trForTask, trPercentageRow;
	private EditText etPercentageCompleted;
	private TextView tvPercentage;
	private HashMap<TableRow, Task> hmTask = new HashMap<TableRow, Task>();;
	private String sTaskDetails;
	private List<MainTaskDetailsWork> lstMainTaskDetailsWorks;
	private List<SubTaskDetailsWork> lstSubTaskDetailsWorks;
	private List<Task> lstTasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatetaskprogress);
		spnMainWorkDesc = (Spinner) findViewById(R.id.spnMainWork);
		spnSubWorkDesc = (Spinner) findViewById(R.id.spnSubWork);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnBack.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnUpdate.setEnabled(false);

		tlForTask = (TableLayout) findViewById(R.id.updatetask_table);

		if (Internet.isAvailable(TaskProgressActivity.this)) {
			GetTeskDetails details = new GetTeskDetails();
			details.execute();
		} else {
			toastMessage("Data Connection UnAvailable, Please enable connection and try again");
		}

		spnMainWorkDesc
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@SuppressWarnings("deprecation")
			public void onItemSelected(AdapterView<?> element,
					View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				if (pos > 0) {
						AlertDialog alertDialog = alertDialog("Work Description", element
								.getItemAtPosition(pos).toString());
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
							}
						});
						alertDialog.show();
					tlForTask.removeAllViews();
					String worDesc = element.getItemAtPosition(pos)
							.toString();
					hmSubWork = new HashMap<Integer, SubTaskDetailsWork>();
					Iterator<SubTaskDetailsWork> itrSubTask = lstSubTaskDetailsWorks
							.iterator();
					int count = 1;
					int id = hmMainWork.get(pos);
					lstSubWorkDesc = new ArrayList<String>();
					lstSubWorkDesc.add("Select work");
					while (itrSubTask.hasNext()) {
						SubTaskDetailsWork sub = itrSubTask.next();
						if (hmMainWork.get(pos) == sub.getWorkRowId()) {
							lstSubWorkDesc.add(sub.getDescription());
							hmSubWork.put(count++, sub);
						}
					}

					aadapSubWork = new ArrayAdapter<String>(
							TaskProgressActivity.this,
							android.R.layout.simple_spinner_item,
							lstSubWorkDesc);
					spnSubWorkDesc.setAdapter(aadapSubWork);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				tlForTask.removeAllViews();
			}

		});

		spnSubWorkDesc
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> element,
					View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos>0){
					btnUpdate.setEnabled(true);
					AlertDialog alertDialog =alertDialog("Work Description",element.getItemAtPosition(pos)
							.toString());
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					});
					alertDialog.show();
					headerForTaskDetailsTable();
					SubTaskDetailsWork subWork = hmSubWork.get(pos);
					Iterator<Task> itrTask = lstTasks.iterator();
					int count = 0;
					while (itrTask.hasNext()) {
						Task task = itrTask.next();
						if (task.getWorkRowId() == subWork.getWorkRowId()
								&& task.getSubWorkRowId() == subWork
								.getSubWorkRowId()) {
							addRowsTaskDetailsTable(task);
							count++;
						}
					}
					if (count == 0) {
						tlForTask.removeAllViews();
						btnUpdate.setEnabled(false);
						toastMessage("You have no task to update");
					} else {
						toastMessage("You have " + count + " task");
					}
				}
			}

			

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				tlForTask.removeAllViews();
			}
		});
	}

	private class GetTeskDetails extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				TaskProgressActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Projects...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = new String();
			try {
				Soapproxy proxy = new Soapproxy(TaskProgressActivity.this);
				result = proxy.getTaskDetails(Appuser.getUserName());
				System.out.println(result);
				if (result.equalsIgnoreCase("Failure")
						|| result.equalsIgnoreCase("ServerTimeout")
						|| result.equalsIgnoreCase("noInternet")) {
					return result;
				} else {
					Parser parser = new Parser(TaskProgressActivity.this);
					TaskDetailsLists lists = new TaskDetailsLists();
					lists = parser.parseGetTaskDetails(result);
					if (lists != null) {
						lstMainTaskDetailsWorks = lists
								.getLstMainTaskDetailsWorks();
						lstSubTaskDetailsWorks = lists
								.getLstSubTaskDetailsWorks();
						lstTasks = lists.getLstTasks();
					} else {
						result = "ErrorWhileParsing";
						return result;
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result = "exception";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			this.dialog.dismiss();
			if (result.equalsIgnoreCase("ServerTimeOut")) {
				toastMessage("Server is taking too long to respond, Please try again later");
			} else if (result.equalsIgnoreCase("noInternet")) {
				toastMessage("Data connection Lost, Please try again later");
			} else if (result.equalsIgnoreCase("responseFailed")) {
				toastMessage("Error while receiving response from service");
			} else if (result.equalsIgnoreCase("ErrorWhileParsing")) {
				toastMessage("Sorry!! Unable to process your request,Please try again later");
			} else if (result.equalsIgnoreCase("doubleID")) {
				toastMessage("Error while requesting the service");
			} else if (result.equalsIgnoreCase("exception")) {
				toastMessage("Unable to process, Please try again later");
			} else if (lstMainTaskDetailsWorks.size() > 0) {
				uiInitialization();
			} else {
				toastMessage("Currently You have No Works");
			}

		}

	}

	@SuppressLint("UseSparseArrays")
	public void uiInitialization() {
		// TODO Auto-generated method stub
		lstMainWorkDesc = new ArrayList<String>();
		lstMainWorkDesc.add("-Select Work-");
		hmMainWork = new HashMap<Integer, Integer>();
		// hmSubWork = new HashMap<Integer, Integer>();
		Iterator<MainTaskDetailsWork> itrMainWorkDesc = lstMainTaskDetailsWorks
				.iterator();
		int count = 1;
		List<Integer> lstMeasurementWorks = new ArrayList<Integer>();
		BaseService service = new BaseService(TaskProgressActivity.this);
		lstMeasurementWorks = service.getProjectIdforSavecordinate("PRG",Appuser.getUserName());
		int workid = 0;
		while (itrMainWorkDesc.hasNext()) {
			MainTaskDetailsWork main = itrMainWorkDesc.next();
			if(workid!=main.getWorkRowId()){
				if(lstMeasurementWorks.contains(main.getWorkRowId())){
					workid = main.getWorkRowId();
					lstMainWorkDesc.add(main.getWorkDescription());
					hmMainWork.put(count++, main.getWorkRowId());
				}
			}
		}

		aadapMainWork = new ArrayAdapter<String>(TaskProgressActivity.this,
				android.R.layout.simple_spinner_item, lstMainWorkDesc);
		spnMainWorkDesc.setAdapter(aadapMainWork);

	}

	private void headerForTaskDetailsTable() {

		TableRow header = new TableRow(this);
		for (int i = 0; i <= 2; i++) {
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
			if (i == 0) {
				cell.setText("TaskId");
			}
			if (i == 1) {
				cell.setText("TaskDesc");
			}
			if (i == 2) {
				cell.setText("Percentage Completed");
			}

			cell.setPadding(6, 4, 6, 4);
			cell.setBackgroundColor(Color.rgb(100, 100, 100));
			header.addView(cell);
		}
		tlForTask.addView(header);
	}

	private void addRowsTaskDetailsTable(Task task) {

		trForTask = new TableRow(this);
		for (int i = 0; i <= 2; i++) {
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
			if (i == 0) {
				cell.setText(String.valueOf(task.getTaskId()));
			}
			if (i == 1) {
				cell.setText(task.getTask());
			}
			if (i == 2) {
				cell.setText(String.valueOf(task.getPercentageCompleted()));
			}

			cell.setPadding(6, 4, 6, 4);
			// cell.setBackgroundColor(Color.rgb(100, 100, 100));
			trForTask.addView(cell);
		}
		hmTask.put(trForTask, task);
		tlForTask.addView(trForTask);
		trForTask.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				// TODO Auto-generated method stub
				TextView textview = new TextView(TaskProgressActivity.this);
				TableRow row = (TableRow) v;
				textview = (TextView) row.getChildAt(1);
//				alertDialogBuilder.setTitle("Task Description");
//				alertDialogBuilder.setMessage(textview.getText().toString());
				AlertDialog.Builder alertDialogBuilder = alertDialogBuilder("Task Description",textview.getText().toString());
				alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						showPercentageUpdateDialog(v);
					}
				})
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {

						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}

		});

	}

	protected void showPercentageUpdateDialog(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View updateView = inflater.inflate(R.layout.percentageupdatelayout,
				(ViewGroup) findViewById(R.id.percentageUpdateLayout), false);
		etPercentageCompleted = (EditText) updateView
				.findViewById(R.id.etPercentage);
		trPercentageRow = (TableRow) v;
		tvPercentage = (TextView) trPercentageRow.getChildAt(2);
		etPercentageCompleted.setText(tvPercentage.getText().toString());
		Button btnPlus = (Button) updateView.findViewById(R.id.plus);
		btnPlus.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				double val = Double.parseDouble(etPercentageCompleted.getText()
						.toString());
				val++;
				if (val <= 100.0) {
					etPercentageCompleted.setText(String.valueOf(val));
				}
			}
		});
		Button btnMinus = (Button) updateView.findViewById(R.id.minus);
		btnMinus.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				double val = Double.parseDouble(etPercentageCompleted.getText()
						.toString());
				val--;
				if (val >= 0.0) {
					etPercentageCompleted.setText(String.valueOf(val));
				}
			}
		});
		updateDialog.setTitle("Percentage Completed?");
		updateDialog.setView(updateView);
		updateDialog.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (Double.parseDouble(etPercentageCompleted.getText()
						.toString()) > 100.0) {
					toastMessage("Percentage Cannot be greaterthan 100");
				} else {
					tvPercentage.setText(etPercentageCompleted
							.getText().toString());
					generateSTaskDetails(trPercentageRow, tvPercentage);
				}
			}
		});
		updateDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		updateDialog.create();
		updateDialog.show();
	}

	protected void generateSTaskDetails(TableRow trPercentageRow2,
			TextView tvPercentage2) {
		// TODO Auto-generated method stub
		TableRow row = trPercentageRow2;
		Task task = hmTask.get(trPercentageRow2);
		hmTask.remove(tvPercentage2);

		task.setStatus("U");
		task.setPercentageCompleted((int)Double.parseDouble(tvPercentage2.getText()
				.toString()));
		hmTask.put(row, task);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
			Intent intent = new Intent(TaskProgressActivity.this,
					UsermenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			finish();
			break;
		case R.id.btnUpdate:
			UpdateTaskDetails taskDetails = new UpdateTaskDetails();
			taskDetails.execute();

			break;
		default:
			break;
		}
	}

	private class UpdateTaskDetails extends AsyncTask<String, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				TaskProgressActivity.this);

		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Projects...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = new String();
			try {
				sTaskDetails = "";
				Iterator<Entry<TableRow, Task>> iterator = hmTask.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<TableRow, Task> entry = iterator.next();
					TableRow view = entry.getKey();
					Task items = entry.getValue();
					if (items.getStatus().equalsIgnoreCase("U")) {
						sTaskDetails = sTaskDetails + items.getWorkRowId()
								+ "?" + items.getSubWorkRowId() + "*"
								+ items.getTaskId() + "/"
								+ (int)items.getPercentageCompleted() + "@";
					}
				}
				Log.e("sTaskDetails", sTaskDetails);
				if (sTaskDetails.length() != 0) {
					Soapproxy proxy = new Soapproxy(TaskProgressActivity.this);
					result = proxy.updateTaskDetails(sTaskDetails);
				} else {

					result = "dataNotChanged";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result = "exception";
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			this.dialog.dismiss();
			if (result.equalsIgnoreCase("ServerTimeOut")) {
				toastMessage("Server is taking too long to respond, Please try again later");
			} else if (result.equalsIgnoreCase("noInternet")) {
				toastMessage("Data connection Lost, Please try again later");
			} else if (result.equalsIgnoreCase("responseFailed")) {
				toastMessage("Error while receiving response from service");
			} else if (result.equalsIgnoreCase("doubleID")) {
				toastMessage("Error while requesting the service");
			} else if (result.equalsIgnoreCase("exception")) {
				toastMessage("Unable to process, Please try again later");
			}else if (result.equalsIgnoreCase("dataNotChanged")) {
				toastMessage("Data as not been changed to update");
			}
			else if (result.equalsIgnoreCase("success")) {
				toastMessage("Successfully Updated");
				Intent intent = new Intent(TaskProgressActivity.this,
						UsermenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else if (result.equalsIgnoreCase("Failure")) {
				toastMessage("Error while processing your request, Please try again later");
			}
			
			/*
			 * else{ Toast.makeText(TaskProgressActivity.this, "NO Projects",
			 * Toast.LENGTH_LONG).show(); }
			 */
		}


	}
	
	private Builder alertDialogBuilder(String Title, String MessageDescription) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TaskProgressActivity.this);
		alertDialogBuilder.setTitle(Title);
		alertDialogBuilder.setMessage(MessageDescription);
		return alertDialogBuilder;
	}
	
	private AlertDialog alertDialog(String Title, String Message) {
		// TODO Auto-generated method stub
		AlertDialog alertDialog = new AlertDialog.Builder(
				TaskProgressActivity.this).create();
		alertDialog.setTitle(Title);
		alertDialog.setMessage(Message);
		return alertDialog;
	}
	
	private void toastMessage(String Message) {
		// TODO Auto-generated method stub
		Toast.makeText(TaskProgressActivity.this, Message, Toast.LENGTH_LONG).show();
	}
}
