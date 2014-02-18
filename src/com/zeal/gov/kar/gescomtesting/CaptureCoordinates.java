package com.zeal.gov.kar.gescomtesting;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class CaptureCoordinates extends Activity {
    private Intent intentFromCapturecord;
    private TextView description;
    private String projectId,typeOfLine,material;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_coordinates);
		description=(TextView) findViewById(R.id.descriptionCapture);
		
		
		intentFromCapturecord = getIntent();
		String data=intentFromCapturecord.getStringExtra("discription");
		description.setText(data);
		
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_capture_coordinates, menu);
		return true;
	}

}
