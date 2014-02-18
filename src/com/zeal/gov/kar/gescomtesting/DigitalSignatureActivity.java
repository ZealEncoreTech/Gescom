package com.zeal.gov.kar.gescomtesting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.session.Appuser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
 
public class DigitalSignatureActivity extends Activity {
    signature mSignature;
    Paint paint;
    LinearLayout mContent;
    Button btnClear, btnSave, btnCancel;
    int workRowId;
    EditText etName;
    Intent intent;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
 
        btnSave = (Button) findViewById(R.id.btnGetsign);
        btnSave.setEnabled(false);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        mContent = (LinearLayout) findViewById(R.id.linearLayout);
 
        etName = (EditText) findViewById(R.id.etYourName);
        etName.setText(Appuser.getUserName());
        etName.setFocusable(false);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature);
 
        btnSave.setOnClickListener(onButtonClick);
        btnClear.setOnClickListener(onButtonClick);
        btnCancel.setOnClickListener(onButtonClick);
        intent= getIntent();
        workRowId = Integer.parseInt(intent.getStringExtra("workRowId"));
    }
 
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == btnClear) {
                mSignature.clear();
            } else if (v == btnSave) {
                mSignature.save();
            }
            else if (v == btnCancel) {
				mSignature.cancel();
			}
        }
    };
 
    public class signature extends View {
        static final float STROKE_WIDTH = 5f;
        static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        Paint paint = new Paint();
        Path path = new Path();
 
        float lastTouchX;
        float lastTouchY;
        final RectF dirtyRect = new RectF();
 
        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }
 
        public void clear() {
            path.reset();
            invalidate();
            btnSave.setEnabled(false);
        }
        
        public void cancel(){
        	finish();
        }
 
        public void save() {
            Bitmap returnedBitmap = Bitmap.createBitmap(mContent.getWidth(),
                    mContent.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable = mContent.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(canvas);
            else
                canvas.drawColor(Color.WHITE);
            mContent.draw(canvas);
 
            String signRoot = Environment.getExternalStorageDirectory()
					.getAbsolutePath().toString();
			File signFile = new File(signRoot, "/GescomSignatureImage");
			if (!signFile.exists()) {
				signFile.mkdirs();
			}
			File comSignFile = new File(signFile, "GescomSign-"+etName.getText().toString()+System.currentTimeMillis() + ".jpeg");
			FileOutputStream outputStream = null;
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
            try{
            	comSignFile.createNewFile();
				outputStream = new FileOutputStream(comSignFile);
				outputStream.write(bs.toByteArray());
				outputStream.close();
            }
            catch (Exception e) {
				// TODO: handle exception
			}
            BaseService baseService = new BaseService(DigitalSignatureActivity.this);
            long value = baseService.updateWorkmainWithSignature(comSignFile.toString(),workRowId);
            String response;
            if(value>0){
            	response = "success";
            	intent.putExtra("responsefromDigSign", response);
            	setResult(1,intent);
            	finish();
            }
            else{
            	response = "failed";
            	intent.putExtra("responsefromDigSign", response);
            	setResult(0,intent);
            	finish();
            }
            /*Intent intent = new Intent(DigitalSignatureActivity.this,
					EditEstimationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();*/
        }
 
        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            canvas.drawPath(path, paint);
        }
 
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            btnSave.setEnabled(true);
 
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;
 
            case MotionEvent.ACTION_MOVE:
 
            case MotionEvent.ACTION_UP:
 
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;
            }
 
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
 
            lastTouchX = eventX;
            lastTouchY = eventY;
 
            return true;
        }
 
        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}