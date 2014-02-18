package com.zeal.gov.kar.gescomtesting;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.session.Appuser;

@SuppressLint("NewApi")
public class CameraView extends Activity implements SurfaceHolder.Callback,
		OnClickListener,LocationListener {
	Camera mCamera;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	static final int FOTO_MODE = 0;
	private Context mContext = this;
	private LayoutInflater controlInflater,gpsInflater;
	private LocationManager locationManager;
	private static final String TAG = "CameraTest";
	private Location imageLocation;
	private byte[] tempdata;
	private Calendar calender;
	private Button buttonTakePicture;
	private SimpleDateFormat datefarmate;
	private EditText imageCaption;
	private String projectId,imageBase64,today,photocaption,workStatus,Wstatus;
	private ApprovedPhotoCoordinates imageDetails ;
	boolean mPreviewRunning = false;
	private ArrayList<ApprovedPhotoCoordinates> imageofList;
	static long name = System.currentTimeMillis();
	private BaseService baseService;
    private TextView txtViewShowCoordinates;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_camera_view);
		baseService=new BaseService(this);
		
		imageofList=new ArrayList<ApprovedPhotoCoordinates>();
		datefarmate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		calender = Calendar.getInstance();
		today = datefarmate.format(calender.getTime());
		
		
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10, 0, this);// 10-second interval,1 meters.
		imageLocation=locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
		controlInflater = LayoutInflater.from(getBaseContext());
		
		Intent intentFromSave = getIntent(); 
		System.out.println(intentFromSave.getStringExtra("Projectid"));
		projectId=intentFromSave.getStringExtra("Projectid");
		workStatus = intentFromSave.getStringExtra("workstatus");
		Wstatus = intentFromSave.getStringExtra("workstatus");
		View viewControl = controlInflater.inflate(R.layout.control, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);
		 
		
		
		gpsInflater=LayoutInflater.from(getBaseContext());
		View viewGps = controlInflater.inflate(R.layout.gpsinfo, null);
		LayoutParams layoutgps = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewGps, layoutgps);
		txtViewShowCoordinates=(TextView) findViewById(R.id.cord);
		
	    buttonTakePicture = (Button) findViewById(R.id.takepicture);
		buttonTakePicture.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, mPictureCallback, mPictureCallback);
			}
		});
		Button back = (Button) findViewById(R.id.bacK);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent mIntent = new Intent();
					mIntent.putExtra("ImageList", imageofList);
					setResult(RESULT_OK, mIntent);
				    finish();
			}
		});
		
	    imageCaption=(EditText) findViewById(R.id.captionText);
	    imageCaption.addTextChangedListener(new TextWatcher() {
		    public void afterTextChanged(Editable s) {}
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){
		        if(s != null && s.length() > 0 && imageCaption.getError() != null) {
		        	imageCaption.setError(null);
		        }
		    }
		});
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceView.setOnClickListener(this);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		buttonTakePicture.setEnabled(false);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	  
		return true;
	}
	protected void onDestroy()
	{
		super.onDestroy();
		locationManager.removeUpdates(this);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			if (imageData != null) {
				
				  Intent mIntent = new Intent(); StoreByteImage(mContext,
				  imageData, 100,"ImageName"); mCamera.startPreview();
				  setResult(FOTO_MODE, mIntent);
				 
				/*tempdata = imageData;
				processImage();*/
			}
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// TODO Auto-generated method stub
		Log.e(TAG, "surfaceChanged");
		try {
			if (mPreviewRunning) {
				mCamera.stopPreview();
				mPreviewRunning = false;
			}
			Camera.Parameters p = mCamera.getParameters();
			List<Camera.Size> size=p.getSupportedPictureSizes();
			System.out.println(size);
			p.setPreviewSize(w, h);
			mCamera.setParameters(p);
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			mPreviewRunning = true;
		} catch (Exception e) {
			Log.d("", e.toString());
		}

	}
	public Bitmap timestampItAndSave(Bitmap toEdit,String caption){
		
		SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		String dateTime = sdate.format(Calendar.getInstance().getTime()); 
		Bitmap canvasBitmap =toEdit.copy(Bitmap.Config.RGB_565, true);
		Canvas imageCanvas = new Canvas(canvasBitmap);
		Paint imagePaint = new Paint();
		imagePaint.setTextAlign(Align.CENTER);
		imagePaint.setTextSize(16f);
		imagePaint.setColor(Color.YELLOW);
		imageCanvas.drawText(dateTime, canvasBitmap.getWidth()/2, (canvasBitmap.getHeight()-5), imagePaint);
		//imageCanvas.drawText(caption, canvasBitmap.getWidth()/2, 20, imagePaint); 
		
		//imageCanvas.drawText(currentLocation.getLatitude() + ","+ currentLocation.getLongitude(), canvasBitmap.getWidth()/2, (canvasBitmap.getHeight()-5), imagePaint); 
		return canvasBitmap;
	}
	
	private void processImage() {
		/*Bitmap bm = BitmapFactory.decodeByteArray(tempdata, 0, tempdata.length);
		String url = Images.Media.insertImage(getContentResolver(), bm, null,null);
		
		
		Bitmap capturedImage=timestampItAndSave(bm,imageCaption.getText().toString());
	
		System.out.println(capturedImage.isMutable());
		System.out.println(capturedImage.isRecycled());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		capturedImage.compress(Bitmap.CompressFormat.JPEG, 0, baos);
		byte[] b = baos.toByteArray();
		imageBase64=Base64.encodeToString(b, Base64.DEFAULT);
		
		byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		
	   imageDetails = new ApprovedPhotoCoordinates();
	   imageDetails.setWorkRowId(Integer.parseInt(projectId));
	   imageDetails.setCapturedBy(Appuser.getUserName());
	   imageDetails.setWorkStarus("BS");
	   imageDetails.setFileName(url);
	   imageDetails.setPhotoCaption(imageCaption.getText().toString());
	   imageDetails.setLatitude(String.valueOf(imageLocation.getLatitude()));
	   imageDetails.setLongitude(String.valueOf(imageLocation.getLongitude()));
	   imageDetails.setCapturedOn(today);
	   imageDetails.setImageData(imageBase64);
	   imageofList.add(imageDetails);
	   System.out.println(imageofList);*/
		/*Bundle bundle = new Bundle();
		if (url != null) {
			bundle.putString("url", url);
			Intent mIntent = new Intent();
			mIntent.putExtras(bundle);
			setResult(RESULT_OK, mIntent);
		} else {
			Toast.makeText(this, "Picture can not be saved", Toast.LENGTH_SHORT)
					.show();
		}
		mCamera.startPreview();
		Toast.makeText(CameraView.this, "Image Captured", Toast.LENGTH_LONG)
				.show();*/
	}

	public  boolean StoreByteImage(Context mContext, byte[] imageData,
			int quality, String expName) {
		photocaption=imageCaption.getText().toString();
		if(!photocaption.isEmpty())
		{
		FileOutputStream fileOutputStream = null;
		try {
			String root = Environment.getExternalStorageDirectory().toString();

			File imageFileFolder = new File(root + "/GESCOMP");
			if(!imageFileFolder.exists()){
				imageFileFolder.mkdirs();
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 5;
			Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,imageData.length, options);
			Bitmap capturedImage=timestampItAndSave(myImage,imageCaption.getText().toString());
			String url="/sdcard/GESCOMP/"+"Gescom-"+System.currentTimeMillis()+".JPEG";
			fileOutputStream = new FileOutputStream(url);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream, 8129);
			capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
		       imageBase64=Base64.encodeToString(b, Base64.DEFAULT);
			   imageDetails = new ApprovedPhotoCoordinates();
			   imageDetails.setWorkRowId(Integer.parseInt(projectId));
			   imageDetails.setCapturedBy(Appuser.getUserName());
			   imageDetails.setWorkStarus(workStatus);
			   imageDetails.setFileName(url);
			   imageDetails.setPhotoCaption(imageCaption.getText().toString());
			   imageDetails.setLatitude(String.valueOf(imageLocation.getLatitude()));
			   imageDetails.setLongitude(String.valueOf(imageLocation.getLongitude()));
			   imageDetails.setCapturedOn(today);
			   imageDetails.setImageData(imageBase64);
			   imageDetails.setIsImageuploaded("N");
			   baseService.addToProgressImages(imageDetails);
			   imageofList.add(imageDetails);
			
			bos.flush();
			bos.close();
			imageCaption.setText("");
			  Toast.makeText(CameraView.this, "Image Captured", Toast.LENGTH_LONG)
				.show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		else
		{
			imageCaption.setError("Please provide caption");
		}
 		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		mCamera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		mCamera.release();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if(imageLocation==null)
		{
		imageLocation=locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
		txtViewShowCoordinates.setText(location.getLatitude() + ","+ location.getLongitude());
		imageLocation.setLatitude(location.getLatitude());
		imageLocation.setLongitude(location.getLongitude());
		if(!buttonTakePicture.isEnabled())
		{
		buttonTakePicture.setEnabled(true);
		}
		}
		else
		{
		txtViewShowCoordinates.setText(location.getLatitude() + ","+ location.getLongitude());
		imageLocation.setLatitude(location.getLatitude());
		imageLocation.setLongitude(location.getLongitude());
		if(!buttonTakePicture.isEnabled())
		{
		buttonTakePicture.setEnabled(true);
		}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }

}
