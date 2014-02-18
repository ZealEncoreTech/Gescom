package com.zeal.gov.kar.gescomtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.cordova.DroidGap;

import com.google.android.gms.maps.model.LatLng;
import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.CoordinateList;
import com.zeal.gov.kar.gescom.model.Projectestimates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class IntermediatePoleActivity extends DroidGap{
	private int workRowId;
	private int spanLenght;
	private HashMap<List<LatLng>, Integer> hm = new HashMap<List<LatLng>, Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		GescomUtility.setTableCount(668);
		Intent intent = getIntent();
		workRowId = Integer.parseInt(intent.getStringExtra("workrowid"));
		spanLenght = Integer.parseInt(intent.getStringExtra("spanLength"));
		super.init();
	    super.appView.addJavascriptInterface(IntermediatePoleActivity.this, "CallDroidGap");
	    super.loadUrl("file:///android_asset/www/index.html");
	}
	
	public String getString(){
		return "success";
	}
	
	public String getSpanLength(){
		return ""+spanLenght;
	}
	
	public String getZoominCoor(){
		String count = "0";
		String which = "start";
		if(workRowId!=0){
			List<List<LatLng>> lst = getSurveyCoordinateList();
			CoordinateList cl = new CoordinateList();
			System.out.println(count);
			if(lst!=null){
				if(lst.size() != Integer.parseInt(count)){
					List<LatLng> l = lst.get(Integer.parseInt(count));
					if(l!=null){
						LatLng s = l.get(0);
						LatLng e = l.get(1);
						if(s != null || e !=null){
							cl.setStartCoor(s.latitude+","+s.longitude);
							cl.setEndCoor(e.latitude+","+e.longitude);
						}
					}
				}else{
					return null;
				}
			}
			else{
				return null;
			}

			if(which.equals("start")){
				System.out.println("Start:"+cl.getStartCoor());
				return cl.getStartCoor();
			}
			else if(which.equals("end")){
				System.out.println("End:"+cl.getEndCoor());
				return cl.getEndCoor();
			}
		}
		return null;
	}
	
	public List<List<LatLng>> getSurveyCoordinateList(){
		hm.clear();
		List<List<LatLng>> llst = new ArrayList<List<LatLng>>();
		List<LatLng> lst;
			BaseService baseService = new BaseService(IntermediatePoleActivity.this);
			List<Projectestimates> lstPE = baseService.getProjectEstimationList(workRowId);
			for(Projectestimates pe :lstPE){
				LatLng latLng = getLatLngFromPE(pe);
				Projectestimates pe1 = getNextPE(lstPE,pe);
				if(pe1 != null){
					lst = new ArrayList<LatLng>();
					LatLng latLng2 = getLatLngFromPE(pe1);
					lst.add(latLng);
					lst.add(latLng2);
					llst.add(lst);
					hm.put(lst, pe.getTypeOfLine());
				}
				
			}
		return llst;
	}
	
	public String getSurverCoor(String count,String which){
		List<List<LatLng>> lst = getSurveyCoordinateList();
		CoordinateList cl = new CoordinateList();
		System.out.println(count);
		if(lst!=null){
			if(lst.size() != Integer.parseInt(count)){
				List<LatLng> l = lst.get(Integer.parseInt(count));
				if(l!=null){
					LatLng s = l.get(0);
					LatLng e = l.get(1);
					if(s != null || e !=null){
						cl.setStartCoor(s.latitude+","+s.longitude);
						cl.setEndCoor(e.latitude+","+e.longitude);
					}
				}
			}else{
				Log.i("PhoneGapLog", "noval from first else in phonegapactivity.getSurverCoor method");
				return "noval";
			}
		}
		else{
			Log.i("PhoneGapLog", "noval from second else in phonegapactivity.getSurverCoor method");
			return "noval";
		}
		if(which.equals("start")){
			Log.i("PhoneGapLog", "Start coordinate:"+cl.getStartCoor());
			
			return cl.getStartCoor();
		}
		else if(which.equals("end")){
			Log.i("PhoneGapLog", "end coordinate:"+cl.getEndCoor());
			return cl.getEndCoor();
		}
		Log.i("PhoneGapLog", "noval from final return in phonegapactivity.getSurverCoor method");
		return "noval";
	}
	
		

	public Projectestimates getNextPE(List<Projectestimates> lstPE,
			Projectestimates pe) {
		// TODO Auto-generated method stub
		for(Projectestimates pe1:lstPE){
			if(pe1.getEstimationId() == pe.getEstimationId() && pe1.getTypeOfLine() == pe.getTypeOfLine() && pe1.getLtEstId() == pe.getLtEstId() && pe1.getCoordinatesId() == pe.getCoordinatesId()+1){
				return pe1;
			}
		}
		return null;
	}

	public LatLng getLatLngFromPE(Projectestimates pe) {
		// TODO Auto-generated method stub
		
		return new LatLng(Double.parseDouble(pe.getStartLattitude()), Double.parseDouble(pe.getStartLangtitude()));
	}
	
	public void getValueFromJS(Object object){
		System.out.println("fromJS:"+object);
	}
	
	public String getLineInfo(String count){
		List<List<LatLng>> lst = getSurveyCoordinateList();
//		CoordinateList cl = new CoordinateList();
		System.out.println(count);
		if(lst!=null){
			if(lst.size() != Integer.parseInt(count)){
				List<LatLng> l = lst.get(Integer.parseInt(count));
				if(l!=null){
					LatLng s = l.get(0);
					LatLng e = l.get(1);
					if(s != null || e !=null){
						return ""+hm.get(l);
//						cl.setStartCoor(s.latitude+","+s.longitude);
//						cl.setEndCoor(e.latitude+","+e.longitude);
					}
				}
			}
		}
		/*String[] latlng1 = start.split(",");
		String[] latlng2 = end.split(",");
		if(latlng1.length == 2){
			if(latlng2.length == 2){
//				LatLng s = new LatLng(Double.parseDouble(latlng1[0]), Double.parseDouble(latlng1[1]));
				String sLat = latlng1[0];
				String sLng = latlng1[1];
				String eLat = latlng2[0];
				String eLng = latlng2[1];
				
			}
		}*/
		return "";
	}
	public String getTransformer(String count){
		List<LatLng> tLatlng = getTrasnformerList();
		if(tLatlng!=null){
			if(tLatlng.size() != Integer.parseInt(count)){
				LatLng latLng = tLatlng.get(Integer.parseInt(count));
				if(latLng != null){
					return latLng.latitude+","+latLng.longitude;
				}
			}
		}
		return "end";
	}
	
	public List<LatLng> getTrasnformerList(){
		BaseService baseService = new BaseService(IntermediatePoleActivity.this);
		List<Projectestimates> lstPE = baseService.getProjectEstimationList(workRowId);
		List<LatLng> tLatlng = new ArrayList<LatLng>();
		for(Projectestimates pe:lstPE){
			if(pe.getTypeOfLine() == 3){
				LatLng latLng = getLatLngFromPE(pe);
				tLatlng.add(latLng);
			}
		}
		return tLatlng;
	}
	
	public String getTransformerCount(){
		List<LatLng> tLatlng = getTrasnformerList();
		return ""+tLatlng.size();
	}

}
