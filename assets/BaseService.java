package com.zeal.gov.kar.gescom.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.MultiValueMap;

import com.zeal.gov.kar.gescom.model.ApprovedCoordinates;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.Htline;
import com.zeal.gov.kar.gescom.model.Logininfo;
import com.zeal.gov.kar.gescom.model.Ltline;
import com.zeal.gov.kar.gescom.model.Masteruserroles;
import com.zeal.gov.kar.gescom.model.MeasurementEstimation;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.Transformer;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.model.Workmain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class BaseService {
	@SuppressWarnings("unused")
	private BaseDao baseDao;
	@SuppressWarnings("unused")
	private Context context;
	ContentValues contentValues;

	public BaseService(Context context) {
		baseDao = new BaseDao(context);
		this.context = context;
	}
	public void updateworkItemsForNewMeasurements(MeasurementEstimation measureEstimation, int workItemTypeid,int workRowID) {
		String[] selargs ={String.valueOf(workItemTypeid),String.valueOf(workRowID)};
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_WORKITEMS_COSTPERITEM,measureEstimation.getCostPerItem());
		values.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID, measureEstimation.getMeasurementId());
		values.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS,measureEstimation.getTotalUnits());
		values.put(DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT, measureEstimation.getTotalAmount());
		values.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_QUANTITY,measureEstimation.getMeasurementQuantity());
		values.put(DBConstants.COLUMN_WORKITEMS_MTOTALAMOUNT, measureEstimation.getmTotalAmount());
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS, values, DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID+ " =? and "+DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID+"=?", selargs);

		}finally{
			baseDao.close();
		}	
	}
	public List<Projectestimates> getSurveySketchDetails(String workRowId) {
		List<Projectestimates> preValues=new ArrayList<Projectestimates>();
		String[] selAArgs = { workRowId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES_TEMP,
							DBConstants.COLUMN_TEMPGPE_WORK_ROW_ID + "=?",selAArgs);
			if (cursor != null) {
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {
					Projectestimates values=new Projectestimates();
					values.setTypeOfLine(cursor.getInt(0));
					int data=cursor.getInt(0);
					values.setStartLattitude(cursor.getString(1));
					values.setStartLangtitude(cursor.getString(3));
					values.setDistance(cursor.getDouble(5));
					values.setNoofCurves(cursor.getInt(6));
					values.setEstimationId(cursor.getInt(8));
					values.setMaterialId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_TEMPGPE_MATERIALID)));
					values.setLtPhase(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_TEMPGPE_LTPHASE)));
					values.setLtEstId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_LTESTID)));
					values.setCoordinatesCaption(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_CAPTION)));
					values.setCoordinatesId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_ID)));
					values.setLtEstId(cursor.getInt(cursor
							.getColumnIndex(DBConstants.COLUMN_TEMPGPE_LTESTID)));
					values.setCoordinatesCaption(cursor.getString(cursor
							.getColumnIndex(DBConstants.COLUMN_TEMPGPE_COORDINATES_CAPTION)));
					values.setCoordinatesId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_TEMPGPE_COORDINATES_ID)));
					preValues.add(values);
					cursor.moveToNext();

				}
			}

			return preValues;
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
	}
	public List<Projectestimates> getSurveyDetails(String workRowId) {
		List<Projectestimates> preValues=new ArrayList<Projectestimates>();
		String[] selAArgs = { workRowId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
							DBConstants.COLUMN_GPE_WORK_ROW_ID + "=?",selAArgs);
			if (cursor != null) {
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {
					Projectestimates values=new Projectestimates();
					values.setTypeOfLine(cursor.getInt(0));
					int data=cursor.getInt(0);
					values.setStartLattitude(cursor.getString(1));
					values.setStartLangtitude(cursor.getString(3));
					values.setDistance(cursor.getDouble(5));
					values.setNoofCurves(cursor.getInt(6));
					values.setEstimationId(cursor.getInt(8));
					values.setMaterialId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_MATERIALID)));
					values.setLtPhase(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_LTPHASE)));
					values.setLtEstId(cursor.getInt(cursor
							.getColumnIndex(DBConstants.COLUMN_GPE_LTESTID)));
					values.setCoordinatesCaption(cursor.getString(cursor
							.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_CAPTION)));
					values.setCoordinatesId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_ID)));
					preValues.add(values);
					cursor.moveToNext();

				}
			}

			return preValues;
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
	}
	public HashMap<String,Integer> getMaterialInformation(String workRowId,String lineType)
	{
		String[] selAArgs = { workRowId, lineType };
		Cursor cursor = null;
		String tcMaterial;
		int tcCount;
		HashMap<String,Integer> tcDetails=new HashMap<String,Integer>();
		baseDao.open();
		cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
				DBConstants.COLUMN_GPE_WORK_ROW_ID
				+ "=?"
				+ " and "
				+ DBConstants.COLUMN_GPE_TYPEOFLINE
				+ "=?", selAArgs);
		if(cursor!=null){
			while(!cursor.isAfterLast()){

				String material=cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GPE_MATERIALID));
				if(tcDetails.containsKey(material))
				{
					tcDetails.put(material, tcDetails.get(material)+1);
				}
				else
				{
					tcDetails.put(material, 1);	
				}
				cursor.moveToNext();
			}
		}
		return tcDetails;
	}
	public double getConductorLength(String workRowId,String Typeofline,String materialId)
	{
		double conductorLegth=0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " = "
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + Typeofline+" and "+DBConstants.COLUMN_GPE_MATERIALID+" = "+materialId;
		Cursor cursor=baseDao.RawQuery(Query,null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getInt(0);
				conductorLegth = conductorLegth + distance;
				cursor.moveToNext();
			}
		}
		return conductorLegth;
	}
	public double getConductorLengthWithPhase(String workRowId,String Typeofline,String materialId,String phase)
	{
		double conductorLegth=0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " = "
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + Typeofline+" and "+DBConstants.COLUMN_GPE_MATERIALID+" = "+materialId+" and "+DBConstants.COLUMN_GPE_LTPHASE+" = "+phase;
		Cursor cursor=baseDao.RawQuery(Query,null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getInt(0);
				conductorLegth = Math.round(conductorLegth + distance);
				cursor.moveToNext();
			}
		}
		return conductorLegth;
	}
	public List<ApprovedPhotoCoordinates> getProgressImages(String workRowId,String workStatus){
		List<ApprovedPhotoCoordinates> lstProgressImages = new ArrayList<ApprovedPhotoCoordinates>();
		String[] selArgs = { workRowId,workStatus };
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROGRESS_IMAGES, DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKROWID +"=? and "+DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKSTATUS +"=?", selArgs);
		if(cursor!=null){
			while(!cursor.isAfterLast()){
				ApprovedPhotoCoordinates  images = new ApprovedPhotoCoordinates();
				images.setWorkRowId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKROWID)));
				images.setCapturedBy(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDBY)));
				images.setWorkStarus(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKSTATUS)));
				images.setFileName(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_FILENAME)));
				images.setPhotoCaption(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_PHOTOCAPTION)));
				images.setLatitude(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_LATITUDE)));
				images.setLongitude(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_LONGITUDE)));
				images.setCapturedOn(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDON)));
				lstProgressImages.add(images);
				cursor.moveToNext();
			}
		}
		return lstProgressImages;
	}

	public long addToProgressImages(ApprovedPhotoCoordinates approvephoto) {
		long cursor;
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKROWID, approvephoto.getWorkRowId());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDBY, approvephoto.getCapturedBy());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKSTATUS, approvephoto.getWorkStarus());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_FILENAME, approvephoto.getFileName());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_PHOTOCAPTION, approvephoto.getPhotoCaption());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_LATITUDE, approvephoto.getLatitude());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_LONGITUDE, approvephoto.getLongitude());
		values.put(DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDON, approvephoto.getCapturedOn());


		try{
			baseDao.open();
			cursor = baseDao.Insert(DBConstants.TABLE_GESCOM_PROGRESS_IMAGES, values);
		}finally{
			baseDao.close();
		}

		return cursor;
	}
	public long addToProjectEstimatesTemp(Projectestimates projectestimates) {
		long cursor;
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_TEMPGPE_WORK_ROW_ID, projectestimates.getWorkRowId());
		values.put(DBConstants.COLUMN_TEMPGPE_TYPEOFLINE, projectestimates.getTypeOfLine());
		values.put(DBConstants.COLUMN_TEMPGPE_START_LATI, projectestimates.getStartLattitude());
		values.put(DBConstants.COLUMN_TEMPGPE_START_LANGI, projectestimates.getStartLangtitude());
		values.put(DBConstants.COLUMN_TEMPGPE_RECEIVED_FROM_MOBILE_DATA, projectestimates.getReceivedFromMobileDate());
		values.put(DBConstants.COLUMN_TEMPGPE_NOOFCURVES, projectestimates.getNoofCurves());
		values.put(DBConstants.COLUMN_TEMPGPE_LTESTID, projectestimates.getLtEstId());
		values.put(DBConstants.COLUMN_TEMPGPE_ESTIMATION_ID, projectestimates.getEstimationId());
		values.put(DBConstants.COLUMN_TEMPGPE_COORDINATES_ID, projectestimates.getCoordinatesId());
		values.put(DBConstants.COLUMN_TEMPGPE_COORDINATES_CAPTION, projectestimates.getCoordinatesCaption());
		values.put(DBConstants.COLUMN_TEMPGPE_DISTANCE, projectestimates.getDistance());

		try{
			baseDao.open();
			cursor = baseDao.Insert(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES_TEMP, values);
		}finally{
			baseDao.close();
		}

		return cursor;
	}

	public long addToProjectEstimatesType(ProjectEstimationType estimationType) {
		long cursor;
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp, estimationType.getProjectId());
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID_Temp, estimationType.getEstimationId());
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION_Temp, estimationType.getEstDescription());
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID_Temp, estimationType.getLineId());
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID_Temp, estimationType.getLtEstId());


		try{
			baseDao.open();
			cursor = baseDao.Insert(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp, values);
		}finally{
			baseDao.close();
		}

		return cursor;
	}
	public List<String> getPoleType() {
		List<String> pole;
		pole = new ArrayList<String>();
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao.Query(DBConstants.TABLE_POLE);
			if (cursor != null) {
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {
					pole.add(cursor.getString(1));
					cursor.moveToNext();

				}
			}

			return pole;
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
	}

	public List<String> getWorkDescription(String workStatus1,
			String workstatus2, String username) {
		List<String> items;
		items = new ArrayList<String>();

		Cursor cursor = null;
		try {
			baseDao.open();
			// String Sql = "Select * from "+
			// DBConstants.TABLE_WORKMAIN+" where WorkStatusid IN('Ame','Smv')"
			// ;

			// cursor = baseDao.RawQuery(Sql, null);
			String[] whereArgs = { workStatus1, workstatus2, username };
			cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
					DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + " IN(?,?) and "
							+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + "=? ORDER BY " +DBConstants.COLUMN_WORKMAIN_DATES +" desc ",
							whereArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {
					System.out.println(cursor.getString(1));
					items.add(cursor.getString(1));
					cursor.moveToNext();
				}
			}

			return items;

		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}

	}


	public List<String> getWorkDescriptionforCategoryChange(String workStatus,
			String username) {
		List<String> items;
		items = new ArrayList<String>();

		Cursor cursor = null;
		try {
			baseDao.open();
			// String Sql = "Select * from "+
			// DBConstants.TABLE_WORKMAIN+" where WorkStatusid IN('Ame','Smv')"
			// ;

			// cursor = baseDao.RawQuery(Sql, null);
			String[] whereArgs = { workStatus, username };
			cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
					DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + " =? and "
							+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + "=? ORDER BY " +DBConstants.COLUMN_WORKMAIN_DATES +" desc ",
							whereArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {
					System.out.println(cursor.getString(1));
					items.add(cursor.getString(1));
					cursor.moveToNext();
				}
			}

			return items;

		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}

	}


	public HashMap<String,Object> getWorkDescriptionforCategoryChangehm(String workStatus,
			String username) {

		HashMap<String,Object> hm = new HashMap<String, Object>();
		List<String> lst = new ArrayList<String>();

		HashMap<String, Integer> description=new  HashMap<String, Integer>();
		Cursor cursor = null;

		// String Sql = "Select * from "+
		// DBConstants.TABLE_WORKMAIN+" where WorkStatusid IN('Ame','Smv')"
		// ;

		// cursor = baseDao.RawQuery(Sql, null);
		String[] whereArgs = { workStatus, username };
		/*	cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
					DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + " =? and "
							+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + "=? ORDER BY " +DBConstants.COLUMN_WORKMAIN_DATES +" desc ",
					whereArgs);
		 */
		cursor = baseDao.RawQuery("SELECT * FROM Workmain WHERE WorkStatusid  = ? and workrowid  not in (select projectid from Gescom_project_EstimationType ) and AssignedUser =? ",whereArgs);
		if (cursor != null) {

			while (!cursor.isAfterLast()) {
				System.out.println(cursor.getString(1));
				lst.add(cursor.getString(1));
				description.put(cursor.getString(1),cursor.getInt(0));
				cursor.moveToNext();
			}
		}
		hm.put("Multivalue",description);
		hm.put("lst",lst);
		return hm;


	}
	public HashMap<String,String> getMultiDescription(String workRowId)
	{
		String[] selargs = { String.valueOf(workRowId) };
		HashMap<String,String> multiDescription=new HashMap<String, String>();
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + "=?",
				selargs);
		if(cursor!=null)
		{
			while (!cursor.isAfterLast()) {
				multiDescription.put(cursor.getString(2), cursor.getString(4));
				cursor.moveToNext();

			}
		}
		return multiDescription;
	}
	public int getlatestphotoid(int workRowId) {
		String[] selargs = { String.valueOf(workRowId) };
		int photoid = 0;
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS,
				DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId + "=?",
				selargs);
		if (cursor != null) {

			while (!cursor.isAfterLast()) {
				photoid = cursor
						.getInt(cursor
								.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoId));
				cursor.moveToNext();

			}
		}

		return photoid;
	}
	public int getEstimationId(int workRowId){
		String[] args = {String.valueOf(workRowId)};
		int id = 0;
		int count = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + " =? ",args);
		if(cursor != null){
			while(!cursor.isAfterLast()){
				count = cursor.getCount();
				id = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID));
				cursor.moveToNext();
			}
		}
		return count;
	}
	public int getEstimationCoordinates(int workRowId){
		String[] args = {String.valueOf(workRowId)};
		int id = 0;
		int count = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,DBConstants.COLUMN_GPE_WORK_ROW_ID + " =? ",args);
		if(cursor != null){
			while(!cursor.isAfterLast()){
				count = cursor.getCount();
				id = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GPE_WORK_ROW_ID));
				cursor.moveToNext();
			}
		}
		return count;
	}
	public List<ApprovedCoordinates> getapprovecoordinates(int workrowid, String isUploaded) {
		String[] selargs = { String.valueOf(workrowid) ,isUploaded};
		List<ApprovedCoordinates> lsttablecontents = new ArrayList<ApprovedCoordinates>();
		ApprovedCoordinates contents;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR,
				DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID + " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_ISUPLOADED+"=?",
				selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				contents = new ApprovedCoordinates();
				contents.setCapturedOn(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDON)));
				contents.setCoordinatesId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID)));
				contents.setEstimationid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID)));
				contents.setiPoleType(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POLETYPE)));
				contents.setCapturedBy(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDBY)));
				contents.setLatitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LATITUDE)));
				contents.setLongitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LONGITUDE)));
				contents.setsCaption(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION)));
				contents.setWorkRowId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID)));
				contents.setLtEstId(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LTESTID)));
				contents.setTypeOfLine(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE)));
				lsttablecontents.add(contents);
				cursor.moveToNext();

			}
		}
		return lsttablecontents;
	}
	
	public List<ApprovedCoordinates> getapprovecoordinates(int workrowid) {
		String[] selargs = { String.valueOf(workrowid) };
		List<ApprovedCoordinates> lsttablecontents = new ArrayList<ApprovedCoordinates>();
		ApprovedCoordinates contents;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR,
				DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID + " =?",
				selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				contents = new ApprovedCoordinates();
				contents.setCapturedOn(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDON)));
				contents.setCoordinatesId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID)));
				contents.setEstimationid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID)));
				contents.setiPoleType(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POLETYPE)));
				contents.setCapturedBy(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDBY)));
				contents.setLatitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LATITUDE)));
				contents.setLongitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LONGITUDE)));
				contents.setsCaption(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION)));
				contents.setWorkRowId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID)));
				contents.setLtEstId(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LTESTID)));
				contents.setTypeOfLine(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE)));
				lsttablecontents.add(contents);
				cursor.moveToNext();

			}
		}
		return lsttablecontents;
	}

	public List<ApprovedPhotoCoordinates> gettableapprovephotocoor(int workrowid) {
		String[] selargs = { String.valueOf(workrowid) };
		List<ApprovedPhotoCoordinates> lstapprovephoto = new ArrayList<ApprovedPhotoCoordinates>();
		ApprovedPhotoCoordinates contents;
		Cursor cursor = baseDao
				.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS,
						DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId
						+ " =?", selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				contents = new ApprovedPhotoCoordinates();
				contents.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_EstimationId)));
				contents.setCoordinatesId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CoordinateId)));
				contents.setPhotoId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoId)));
				contents.setPhotoCaption(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption)));
				contents.setFileName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_FileName)));
				contents.setCapturedOn(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedOn)));
				contents.setCapturedBy(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedBy)));
				contents.setCoordinates(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_Coordinates)));
				contents.setWorkRowId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId)));
				contents.setLtEstId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_LTESTID)));
				contents.setTypeOfLine(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_TYPEOFLINE)));
				lstapprovephoto.add(contents);
				cursor.moveToNext();
			}
		}
		return lstapprovephoto;
	}
	
	public List<ApprovedPhotoCoordinates> gettableapprovephotocoor(int workrowid,String isUploaded) {
		String[] selargs = { String.valueOf(workrowid),isUploaded };
		List<ApprovedPhotoCoordinates> lstapprovephoto = new ArrayList<ApprovedPhotoCoordinates>();
		ApprovedPhotoCoordinates contents;
		Cursor cursor = baseDao
				.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS,
						DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId
						+ " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_ISUPLOADED+"=?", selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				contents = new ApprovedPhotoCoordinates();
				contents.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_EstimationId)));
				contents.setCoordinatesId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CoordinateId)));
				contents.setPhotoId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoId)));
				contents.setPhotoCaption(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption)));
				contents.setFileName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_FileName)));
				contents.setCapturedOn(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedOn)));
				contents.setCapturedBy(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedBy)));
				contents.setCoordinates(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_Coordinates)));
				contents.setWorkRowId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId)));
				contents.setLtEstId(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_LTESTID)));
				contents.setTypeOfLine(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_TYPEOFLINE)));
				lstapprovephoto.add(contents);
				cursor.moveToNext();
			}
		}
		return lstapprovephoto;
	}
	public List<String> getEstimationDescription(String projrctId,String lineType)
	{
		String[] selargs = { projrctId,lineType };
		List<String> description=new ArrayList<String>();

		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID+"=?", selargs);
		if(cursor !=null)
		{

			while(!cursor.isAfterLast())
			{
				description.add(cursor.getString(2));
				cursor.moveToNext();
			}

		}
		return description;
	}
	public Map<String,String> getEstimationDescriptionMap(String projrctId,String lineType)
	{
		String[] selargs = { projrctId,lineType,"Single Estimation" };
		Map<String,String> description=new HashMap<String,String>();

		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"!=?", selargs);
		if(cursor !=null)
		{

			while(!cursor.isAfterLast())
			{
				//description.add(cursor.getString(2));
				description.put(cursor.getString(1),cursor.getString(2));
				System.out.println(cursor.getString(1)+","+cursor.getString(2));
				cursor.moveToNext();
			}
			System.out.println(description);
		}
		return description;
	}
	public boolean isEstimationDescriptionAvailable(String projrctId,String description)
	{
		String[] selargs = { projrctId,description };


		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"=?", selargs);

		if (cursor != null) {
			int count = cursor.getCount();
			if (count >= 1) {
				cursor.close();
				return true;
			}
		}
		return false;	
	}
	public boolean isWorkRowIdPresent(String projrctId)
	{
		String[] selargs = { projrctId};


		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? ", selargs);

		if (cursor != null) {
			int count = cursor.getCount();
			if (count >= 1) {
				cursor.close();
				return true;
			}
		}
		return false;	
	}
	public long insertintoapprovephotocoordinates(ApprovedPhotoCoordinates approvephoto){
		long cursor;
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedBy, approvephoto.getLoginId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedOn, approvephoto.getCaptionDate());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_Coordinates,approvephoto.getCoordinates() );
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_FileName,approvephoto.getFileName());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption,approvephoto.getPhotoCaption());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoId,approvephoto.getPhotoId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId, approvephoto.getProjectId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID, approvephoto.getEstimationId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CoordinateId, approvephoto.getPhotoId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_LTESTID, approvephoto.getLtEstId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_TYPEOFLINE, approvephoto.getTypeOfLine());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_ISUPLOADED, "N");
		try{
			baseDao.open();
			cursor = baseDao.Insert(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, values);
		}finally{
			baseDao.close();
		}

		return cursor;
	}

	public int getlastcoordinateid(int workRowId) {
		String[] selargs = { String.valueOf(workRowId) };
		int coordinateid = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR,
				DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID + " =?",
				selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				coordinateid = cursor
						.getInt(cursor
								.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID));
				cursor.moveToNext();
			}
		}
		return coordinateid;
	}

	public int getpoleId(String pSelectedDesc) {
		// TODO Auto-generated method stub
		String[] selArgs = { pSelectedDesc };
		int workRowId = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_POLE,
				DBConstants.COLUMN_POLE_DESCRIPTION + " =? ", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				workRowId = cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_POLE_ID));
				System.out.println("@@@@@@@@@@@@" + workRowId);
				cursor.moveToNext();
			}
		}
		return workRowId;
	}

	public boolean insertintoapprovecoordinates(ApprovedCoordinates approve) {
		long cursor;

		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDBY,
				approve.getLoginId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_CAPTUREDON,
				approve.getCapturedOn());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID,
				approve.getEstimationid());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LATITUDE,
				approve.getLatitude());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LONGITUDE,
				approve.getLongitude());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID,
				approve.getProjectId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POLETYPE,
				approve.getiPoleType());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION,
				approve.getsCaption());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID,
				approve.getCoordinatesId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_LTESTID, approve.getLtEstId());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE, approve.getTypeOfLine());
		values.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ISUPLOADED, "N");

		try {
			baseDao.open();

			cursor = baseDao.Insert(DBConstants.TABLE_GESCOM_APPROVE_COOR,
					values);
		} finally {

			baseDao.close();

		}
		return true;
	}

	public boolean isUserAvailable(String username, String password) {
		String[] selAArgs = { username, password };
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_AUTH,
							DBConstants.COLUMN_AUTH_USERNAME + "=?" + " and "
									+ DBConstants.COLUMN_AUTH_PASSWORD + "=?",
									selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public boolean isProjectdataAvailable(String workRowId) {
		String[] selAArgs = { workRowId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
							DBConstants.COLUMN_GPE_WORK_ROW_ID + "=?",selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public boolean isProjectdataAvailableTemp(String workRowId) {
		String[] selAArgs = { workRowId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES_TEMP,
							DBConstants.COLUMN_TEMPGPE_WORK_ROW_ID + "=?",selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public boolean isProjectEstimationTypeDatavailable(String workRowId,String estimationId,String estDescription,String lineId,String ltEstId ) {
		String[] selAArgs = { workRowId,estimationId,estDescription,lineId,ltEstId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp,
							DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp + " =? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID_Temp + " =? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION_Temp + " =? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID_Temp + " =? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID_Temp + " =? ",selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public boolean isProjectEstimationDatavailable(String workRowId,String estimationId,String estDescription,String lineId,String ltEstId ) {
		String[] selAArgs = { workRowId,estimationId,estDescription,lineId,ltEstId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES_TEMP,
							DBConstants.COLUMN_TEMPGPE_WORK_ROW_ID + " =? and " +DBConstants.COLUMN_TEMPGPE_ESTIMATION_ID + " =? and " +DBConstants.COLUMN_TEMPGPE_COORDINATES_CAPTION + " =? and " +DBConstants.COLUMN_TEMPGPE_TYPEOFLINE + " =? and "+DBConstants.COLUMN_TEMPGPE_LTESTID + " =? ",selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public boolean iscoordinatesavailable(String workRowId,String ltEstid, String typeofline,String estId){
		String[] args = {workRowId,ltEstid,typeofline,estId};
		Cursor cursor = null;
		boolean result = false;
		try{
			baseDao.open();
			cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID + " =? and " + DBConstants.COLUMN_GPE_LTESTID + " =? and " + DBConstants.COLUMN_GPE_TYPEOFLINE +" =? and " + DBConstants.COLUMN_GPE_ESTIMATION_ID +" =? ",args);
			if(cursor != null){
				int count = cursor.getCount();
				if(count >= 1){
					cursor.close();
					result = true;

				}
			}
		}finally{
			if(cursor != null){
				cursor.close();
				baseDao.close();
			}

		}
		return result;

	}

	public boolean isProjectphotosAvailable(String workRowId) {
		String[] selAArgs = { workRowId};
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROGRESS_IMAGES,
							DBConstants.COLUMN_GESCOM_PROGRESS_IMAGES_WORKROWID + "=?",selAArgs);

			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}
	public Map<String, Integer> getProjectforSavecordinates(String staus,String user) {
		String[] selAArgs = { staus,user };
		Map<String, Integer> workInfo = new HashMap<String, Integer>();
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN, DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + "=? and "+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?  ORDER BY " +DBConstants.COLUMN_WORKMAIN_DATES +" desc ",selAArgs);
		/*Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + "=? and "+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?", selAArgs);*/
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				int eoid = cursor.getInt(0);
				String eoid1 = cursor.getString(1);
				workInfo.put(cursor.getString(1), (Integer) cursor.getInt(0));
				cursor.moveToNext();
			}

		}
		cursor.close();
		return workInfo;
	}

	public List<String> getProjectforSavecordinate(String staus,String user) {
		String[] selArgs = { staus,user };
		ArrayList<String> workInfo = new ArrayList<String>();

		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN, DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + "=? and "+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?  ORDER BY " +DBConstants.COLUMN_WORKMAIN_DATES +" desc ",selArgs);
		/*Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + "=? and "+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?", selAArgs);*/

		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				int eoid = cursor.getInt(0);
				String eoid1 = cursor.getString(1);
				workInfo.add(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION)));
				//workInfo.add(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_WORKMAIN_WORKROWID)));
				cursor.moveToNext();
			}

		}
		cursor.close();
		return workInfo;
	}

	public long updateWorkMain(String workRowID, String status) {
		// TODO Auto-generated method stub
		long insertId;
		String[] whereArgs = { workRowID };
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_WORKMAIN_WORKSTATUSID, status);
		try {
			baseDao.open();
			insertId = baseDao.Update(DBConstants.TABLE_WORKMAIN,
					contentValues, DBConstants.COLUMN_WORKMAIN_WORKROWID
					+ "=? ", whereArgs);
			return insertId;
		} finally {
			baseDao.close();
		}
	}
	public long updateWorkmainCategory(String workrowId,int categoryId){
		long insertid;
		String[] args = {workrowId};
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_WORKMAIN_PROJECT_CATEGORY, categoryId);
		try{
			baseDao.open();
			insertid = baseDao.Update(DBConstants.TABLE_WORKMAIN, contentValues, DBConstants.COLUMN_WORKMAIN_WORKROWID + " =? ", args);
			return insertid;
		}finally{
			baseDao.close();
		}
	}
	public long deleteProjectDataFromEstimates(String workRowID) {
		// TODO Auto-generated method stub
		long insertId;
		String[] whereArgs = { workRowID };

		try {
			baseDao.open();
			insertId = baseDao.Delete(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS, DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID
					+ "=? ", whereArgs);
			System.out.println(insertId);
			return insertId;
		} finally {
			baseDao.close();
		}
	}
	public long addTologininfo(Logininfo user) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_AUTH_USERNAME, user.getLoginId());
		contentValues.put(DBConstants.COLUMN_AUTH_PASSWORD, user.getPassWord());
		contentValues.put(DBConstants.COLUMN_AUTH_OFFICER_NAME,
				user.getOfficeName());
		contentValues.put(DBConstants.COLUMN_AUTH_ACTIVATED,
				user.getActivated());
		try {
			baseDao.open();
			long insertId = baseDao.Insert(DBConstants.TABLE_AUTH,
					contentValues);
			Log.d("BaseService.addTologininfo", insertId + "");
			return insertId;
		} finally {
			baseDao.close();
		}

	}

	public boolean ismasteruserrolesAvailable(String lognId) {
		String[] selAArgs = { lognId };
		Cursor cursor = null;
		try {
			baseDao.open();
			cursor = baseDao.Query(DBConstants.TABLE_MASTER_USERROLES,
					DBConstants.COLUMN_MASTTER_USERROLES_LOGINID + "=?",
					selAArgs);
			if (cursor != null) {
				int count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}

	public double getLineLength(String workRowId, String lineType) {
		double totalDistance = 0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " ="
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + lineType;
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getDouble(0);
				totalDistance = Math.round(totalDistance + distance);
				cursor.moveToNext();
			}

		}
		cursor.close();
		return totalDistance;
	}
	public double getLineLengthofconductor(String workRowId, String lineType) {
		double totalDistance = 0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " ="
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + lineType + " and " + DBConstants.COLUMN_GPE_LAYINGMETHOD+" = ''";
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getDouble(0);
				totalDistance = Math.round(totalDistance + distance);
				cursor.moveToNext();
			}

		}
		cursor.close();
		return totalDistance;
	}
	public double getMultiLineLength(String workRowId, String lineType,String estimationId) {
		double totalDistance = 0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " ="
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + lineType+ " and " + DBConstants.COLUMN_GPE_ESTIMATION_ID
				+ " = " +estimationId ;
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getInt(0);
				totalDistance = totalDistance + distance;
				cursor.moveToNext();
			}

		}
		cursor.close();
		return totalDistance;
	}

	public int getNumberOfBends(String workRowId, String lineType) {
		int totalNumber = 0;
		String Query = "select *"
				+ " from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " ="
				+ workRowId + " and " + DBConstants.COLUMN_GPE_NOOFCURVES
				+ " != 0 and " + DBConstants.COLUMN_GPE_TYPEOFLINE +" = "+lineType;
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			/*while (!cursor.isAfterLast()) {
				totalNumber = cursor.getCount();
				cursor.moveToNext();
			}*/
			totalNumber = cursor.getCount();
		}

		cursor.close();
		return totalNumber;
	}

	public long addTomasteruserroles(Masteruserroles role) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_MASTTER_USERROLES_LOGINID,
				role.getLoginId());
		contentValues.put(DBConstants.COLUMN_MASTTER_USERROLES_ROLEID,
				role.getRoleId());
		try {
			baseDao.open();
			long insertId = baseDao.Insert(DBConstants.TABLE_MASTER_USERROLES,
					contentValues);
			return insertId;
		} finally {
			baseDao.close();
		}
	}

	public long addToworkmain(Workmain wmRow) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_WORKMAIN_WORKROWID,
				wmRow.getWorkRowid());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION,
				wmRow.getWorkDescription());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_PROJECT_CATEGORY,
				wmRow.getProjectCategory());
		contentValues
		.put(DBConstants.COLUMN_WORKMAIN_NOOFLT, wmRow.getNoofLT());
		contentValues
		.put(DBConstants.COLUMN_WORKMAIN_REASON, wmRow.getReason());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_WORKROWID,
				wmRow.getWorkRowid());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_WORKSTATUSID,
				wmRow.getWorkStatusid());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_DATES, wmRow.getDates());
		contentValues
		.put(DBConstants.COLUMN_WORKMAIN_NOOFHT, wmRow.getNoofHT());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_SIGNNATURE,
				wmRow.getSignature());
		contentValues.put(DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER,
				wmRow.getAssignedUser());

		try {
			baseDao.open();
			long insertId = baseDao.Insert(DBConstants.TABLE_WORKMAIN,
					contentValues);
			return insertId;
		} finally {
			baseDao.close();
		}
	}

	public long addToprojectestimation(Projectestimates peRow) {
		ContentValues contentValues = new ContentValues();

		contentValues.put(DBConstants.COLUMN_GPE_TYPEOFLINE,peRow.getTypeOfLine());
		contentValues.put(DBConstants.COLUMN_GPE_START_LATI,peRow.getStartLattitude());
		contentValues.put(DBConstants.COLUMN_GPE_START_LANGI,peRow.getStartLangtitude());
		contentValues.put(DBConstants.COLUMN_GPE_DISTANCE, peRow.getDistance());
		contentValues.put(DBConstants.COLUMN_GPE_NOOFCURVES,peRow.getNoofCurves());
		contentValues.put(DBConstants.COLUMN_GPE_COORDINATES_ID,peRow.getCoordinatesId());
		contentValues.put(DBConstants.COLUMN_GPE_COORDINATES_CAPTION,peRow.getCoordinatesCaption());
		contentValues.put(DBConstants.COLUMN_GPE_WORK_ROW_ID,peRow.getWorkRowId());
		contentValues.put(DBConstants.COLUMN_GPE_LTPHASE,peRow.getLtPhase());
		contentValues.put(DBConstants.COLUMN_GPE_LTESTID,peRow.getLtEstId());
		contentValues.put(DBConstants.COLUMN_GPE_MATERIALID,peRow.getMaterialId());
		contentValues.put(DBConstants.COLUMN_GPE_ISTAPING, peRow.getIsTaping());
		contentValues.put(DBConstants.COLUMN_GPE_ESTIMATION_ID, peRow.getEstimationId());

		contentValues.put(DBConstants.COLUMN_GPE_LAYINGMETHOD, peRow.getLayingMethod());


		try {
			baseDao.open();
			long insertId = baseDao.Insert(
					DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, contentValues);
			return insertId;
		} finally {
			baseDao.close();
		}
	}

	public int getCoordinateid(String workrowId, String lineType) {
		int coordinateId = 0;
		String[] selAArgs = { workrowId, lineType };
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
				DBConstants.COLUMN_GPE_WORK_ROW_ID + "=? and "
						+ DBConstants.COLUMN_GPE_TYPEOFLINE + "=?", selAArgs);
		if (cursor != null) {
			coordinateId = cursor.getCount();
			// while(!cursor.isAfterLast()) {
			// coordinateId=cursor.getInt(10);
			// cursor.moveToNext();
			// }

		}
		cursor.close();
		return coordinateId;
	}
	public int getLTEstId(String workRowId,String estimationId,int lineId,String Description){
		int ltEstID = 0;
		String[] selArgs = {workRowId,estimationId,String.valueOf(lineId),Description};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + "=? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID+ " =? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID + "=? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+ " =? ", selArgs);
		if(cursor != null){

			while(!cursor.isAfterLast()) {
				ltEstID =cursor.getInt(4);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return ltEstID;
	}
	public int getCoordinateidforMultiple(String workrowId, String lineType,String estimationId) {
		int coordinateId = 0;
		String[] selAArgs = { workrowId, lineType ,estimationId};
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
				DBConstants.COLUMN_GPE_WORK_ROW_ID + "=? and "
						+ DBConstants.COLUMN_GPE_TYPEOFLINE + "=? and "+ DBConstants.COLUMN_GPE_ESTIMATION_ID + "=?", selAArgs);
		if (cursor != null) {
			coordinateId = cursor.getCount();
			// while(!cursor.isAfterLast()) {
			// coordinateId=cursor.getInt(10);
			// cursor.moveToNext();
			// }

		}
		cursor.close();
		return coordinateId;
	}
	public int getEstimationId(String projectId, String lineType) {
		int EstimationId = 0;
		String[] selAArgs = { projectId, lineType,"Single Estimation" };
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + "=? and "
						+ DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID + "=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"!=?", selAArgs);
		if (cursor != null) {

			EstimationId = cursor.getCount();

		}
		cursor.close();
		return EstimationId;
	}
	public int isEstimationTypeAvailable(String projectID, String lineId) {
		String[] selAArgs = { projectID, lineId };
		Cursor cursor = null;
		int count=0;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
							DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID
							+ "=?"
							+ " and "
							+ DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID
							+ "=?", selAArgs);

			if (cursor != null) {
				count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return count;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return count;
	}
	public boolean isEstimationAvailable(String projectID, String lineId) {
		String[] selAArgs = { projectID, lineId };
		Cursor cursor = null;
		int count=0;
		try {
			baseDao.open();
			cursor = baseDao
					.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
							DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID
							+ "=?"
							+ " and "
							+ DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID
							+ "=?", selAArgs);

			if (cursor != null) {
				count = cursor.getCount();
				if (count >= 1) {
					cursor.close();
					return true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			baseDao.close();
		}
		return false;
	}

	public long addToProjectEstimationType(ProjectEstimationType petRow) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID,
				petRow.getProjectId());
		contentValues.put(
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID,
				petRow.getLineId());
		contentValues.put(
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID,
				petRow.getEstimationId());
		contentValues
		.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION,
				petRow.getEstDescription());
		contentValues
		.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID,
				petRow.getLtEstId());
		try {
			baseDao.open();
			long insertId = baseDao.Insert(
					DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
					contentValues);
			return insertId;
		} finally {
			baseDao.close();
		}

	}

	public Map<String, Integer> getProjectesForEstimation(String workSatus,String user) {
		String[] selectArgs = { workSatus,"EST",user };
		Map<String, Integer> workDetails = new HashMap<String, Integer>();
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + " in(?,?) and "+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?", selectArgs);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				workDetails.put(cursor.getString(1), cursor.getInt(0));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return workDetails;
	}

	public int getProjectCategory(int workId) {
		int projectCategory = 0;
		String[] selectArgs = { Integer.toString(workId) };
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKROWID + "=?", selectArgs);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				projectCategory = cursor.getInt(2);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return projectCategory;
	}

	public Double getBaseRate(String workItemTypeId) {
		double baseRate = 0.0;
		String[] selAArgs = { workItemTypeId };
		String[] colAArgs = { "BASERATE,Description" };
		// Cursor cursor =
		// baseDao.Query(DBConstants.MASTER_WORKITEM_TYPE,colAArgs,DBConstants.COLUMN_MASTER_WORKITEM_TYPE+"=?",
		// selAArgs);
		Cursor cursor = baseDao.RawQuery(
				"select BaseRate,Description from master_workitemtype where WorkItemTypeId="
						+ workItemTypeId, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					baseRate = Double.parseDouble(cursor.getString(0));
					// Log.d("BaseRate", "workItemTypeId : " + workItemTypeId
					// +" And BaseRate = " + baseRate);
				} while (cursor.moveToNext());
				cursor.close();
			}
		}
		return baseRate;
	}

	public String getDescription(String workItemTypeId) {
		String description = null;
		String[] selAArgs = { workItemTypeId };
		String[] colAArgs = { "Description" };
		// Cursor cursor =
		// baseDao.Query(DBConstants.MASTER_WORKITEM_TYPE,colAArgs,DBConstants.COLUMN_MASTER_WORKITEM_TYPE+"=?",
		// selAArgs);
		Cursor cursor = baseDao.RawQuery(
				"select Description from master_workitemtype where WorkItemTypeId="
						+ workItemTypeId, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					description = cursor.getString(0);
					// Log.d("BaseRate", "workItemTypeId : " + workItemTypeId
					// +" And BaseRate = " + baseRate);
				} while (cursor.moveToNext());
				cursor.close();
			}
		}
		return description;
	}

	public int getWorkRowId(String pSelectedDesc) {
		// TODO Auto-generated method stub
		String[] selArgs = { pSelectedDesc };
		int workRowId = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION + " =? ", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				workRowId = cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKMAIN_WORKROWID));
				System.out.println("@@@@@@@@@@@@" + workRowId);
				cursor.moveToNext();
			}
		}
		return workRowId;
	}
	public int getMeasurementIds(String workRowId)
	{	
		String[] selArgs = { workRowId };
		int measurementId = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_MASTER_WORK_ITEM_TYPE,
				DBConstants.COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_WORKITEMTYPEID + " =? ", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				measurementId = cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_MEASUREMENTID));
				System.out.println("@@@@@@@@@@@@" + workRowId);
				cursor.moveToNext();
			}
		}
		return measurementId;

	}
	public long addToworkItems(WorkItems item) {
		System.out.println(item.getWorkRowid());
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID,
				item.getWorkRowid());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_ESTIMATION_ID, 1);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_CATEGARY, 0);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_SUBCATEGARY, 0);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_COMPONEANT_ID, 0);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID,
				item.getWorkItemTypeId());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_WORKITEM_DESCRIPTION,
				item.getWorkItemDescription());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID, item.getMeasurementid());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_ESTIMATION_ID, 1);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_COSTPERITEM,
				item.getBaseRate());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS,
				item.getTotalUnits());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT,
				item.getTotalAmount());
		contentValues
		.put(DBConstants.COLUMN_WORKITEMS_SRRATE, item.getSrRate());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_GPSQUANTITY, item.getGpsQuantity());
		contentValues
		.put(DBConstants.COLUMN_WORKITEMS_SRYEAR, item.getSrYear());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_BLOCK_ID,
				item.getBlockId());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_GROUP_ID,
				item.getGroupId());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_BASE_RATE,
				item.getBaseRate());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_BLOCK_NAME,
				item.getBlockName());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_FIXED, item.getFixed());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_CONSTANT,
				item.getConstant());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_CONSTANT_VALUE,
				item.getConstantValue());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_FORMULA,
				item.getFormula());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_GROUPNAME,
				item.getGroupName());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_SUBWORK_ID, 0);
		contentValues.put(DBConstants.COLUMN_WORKITEMS_AMOUNT_QUANTITY,
				item.getAmountQuantity());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_ITEM_CODE,
				item.getItemCode());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_QUANTITY,
				item.getMeasurmentQuantity());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_MTOTALAMOUNT,
				item.getmTotalAmount());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_DECROUND,
				item.getDecRound());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_ISINTEGER,
				item.getIsInteger());
		try {
			baseDao.open();
			long insertId = baseDao.Insert(
					DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS, contentValues);
			return insertId;
		} finally {
			baseDao.close();
		}
	}

	public List<WorkItems> getTableContents(int workRowId) {
		// TODO Auto-generated method stub
		String[] selArgs = { String.valueOf(workRowId) };
		List<WorkItems> lstTableContents = new ArrayList<WorkItems>();
		WorkItems content;
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS,
				DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID + " =?", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				content = new WorkItems();
				content.setAmountQuantity(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_AMOUNT_QUANTITY)));
				content.setBaseRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_COSTPERITEM)));
				content.setBlockId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_BLOCK_ID)));
				content.setBlockName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_BLOCK_NAME)));
				content.setCategory(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CATEGARY)));
				content.setComponentId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBCATEGARY)));
				content.setConstant(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CONSTANT)));
				content.setConstantValue(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CONSTANT_VALUE)));
				content.setCostPerItem(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_COSTPERITEM)));
				content.setDecRound(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_DECROUND)));
				content.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ESTIMATION_ID)));
				content.setSrRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRRATE)));
				content.setSrYear(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRYEAR));
				content.setFixed(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_FIXED)));
				content.setFormula(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_FORMULA)));
				content.setGroupId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_GROUP_ID)));
				content.setGroupName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_GROUPNAME)));
				content.setGpsQuantity(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_GPSQUANTITY)));
				content.setIsInteger(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ISINTEGER)));
				content.setItemCode(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ITEM_CODE)));
				content.setMeasurementid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID)));
				content.setMeasurmentQuantity(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_QUANTITY)));
				content.setmTotalAmount(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MTOTALAMOUNT)));
				content.setSrRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRRATE)));
				content.setSrYear(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRYEAR)));
				content.setSubCategory(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBCATEGARY)));
				content.setSubWorkid(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBWORK_ID)));
				content.setTotalAmount(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT)));
				content.setTotalUnits(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS)));
				content.setWorkItemDescription(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORKITEM_DESCRIPTION)));
				content.setWorkItemTypeId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID)));
				content.setWorkRowid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID)));
				lstTableContents.add(content);
				cursor.moveToNext();
			}
		}
		return lstTableContents;
	}

	public int getmeasurementid(String measurementdescription){
		Cursor cursor;
		int measurementid = 0;

		try{
			String[] whereArgs = {measurementdescription};
			baseDao.open();
			cursor = baseDao.Query(DBConstants.TABLE_MASTER_MEASUREMENT,DBConstants.COLUMN_MASTER_MEASUREMENT_MEASUREMENTDESCRIPTION+ "=?", whereArgs);
			if(cursor != null)
			{
				int count = cursor.getCount();
				while (!cursor.isAfterLast()) {

					measurementid = cursor.getInt(0);
					cursor.moveToNext();

				}
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return measurementid;
	}
	public void updateworkstatus(int workrowid,String status){
		String[] selargs = {String.valueOf(workrowid)};

		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_WORKMAIN_WORKSTATUSID, status);

		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_WORKMAIN,values,DBConstants.COLUMN_WORKMAIN_WORKROWID +" =? ",selargs);
		}finally{
			baseDao.close();
		}
	}
	public void updateDescription(int workRowId,String desc,String description){
		String[] args = {String.valueOf(workRowId),desc};
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION, description);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,values,DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+" =? and " +DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION + " =?" ,args);
		}finally{
			baseDao.close();
		}
	}
	public List<WorkItems> getTableContent(int workrowid){
		String[] selargs = {String.valueOf(workrowid)};
		List<WorkItems> lstworkitems = new ArrayList<WorkItems>();
		WorkItems content;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS, DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID + "=?", selargs);
		if(cursor!=null){
			while(!cursor.isAfterLast()){
				content = new WorkItems();
				content.setAmountQuantity(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_AMOUNT_QUANTITY)));
				content.setBaseRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_COSTPERITEM)));
				content.setBlockId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_BLOCK_ID)));
				content.setBlockName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_BLOCK_NAME)));
				content.setCategory(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CATEGARY)));
				content.setComponentId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBCATEGARY)));
				content.setConstant(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CONSTANT)));
				content.setConstantValue(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_CONSTANT_VALUE)));
				content.setCostPerItem(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_COSTPERITEM)));
				content.setDecRound(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_DECROUND)));
				content.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ESTIMATION_ID)));
				content.setSrRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRRATE)));
				content.setSrYear(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRYEAR));
				content.setFixed(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_FIXED)));
				content.setFormula(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_FORMULA)));
				content.setGroupId(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_GROUP_ID)));
				content.setGroupName(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_GROUPNAME)));
				content.setIsInteger(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ISINTEGER)));
				content.setItemCode(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_ITEM_CODE)));
				content.setMeasurementid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID)));
				content.setMeasurmentQuantity(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_QUANTITY)));
				content.setmTotalAmount(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MTOTALAMOUNT)));
				content.setSrRate(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRRATE)));
				content.setSrYear(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SRYEAR)));
				content.setSubCategory(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBCATEGARY)));
				content.setSubWorkid(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_SUBWORK_ID)));
				content.setTotalAmount(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT)));
				content.setTotalUnits(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS)));
				content.setWorkItemDescription(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORKITEM_DESCRIPTION)));
				content.setWorkItemTypeId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID)));
				content.setWorkRowid(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID)));
				lstworkitems.add(content);
				cursor.moveToNext();
			}
		}
		return lstworkitems;
	}
	public List<String> getWorkDescForCreateEst(String string, String username) {
		String[] selArgs = { string, username };
		List<String> lstWorkDesc = new ArrayList<String>();
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKSTATUSID + " =? and "
						+ DBConstants.COLUMN_WORKMAIN_ASSIGNED_USER + " =?",
						selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				lstWorkDesc
				.add(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION)));
				cursor.moveToNext();
			}
		}
		return lstWorkDesc;
	}

	public List<String> getMeasurementUnit() {
		// TODO Auto-generated method stub
		List<String> lstOfMeasurementUnits = new ArrayList<String>();
		Cursor cursor = baseDao.Query(DBConstants.TABLE_MASTER_MEASUREMENT);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				lstOfMeasurementUnits
				.add(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_MASTER_MEASUREMENT_MEASUREMENTDESCRIPTION)));
				cursor.moveToNext();
			}
		}
		return lstOfMeasurementUnits;
	}

	public List<ProjectEstimationType> getProjectEstimationTypeList(
			int workRowID) {
		String[] selArg = { Integer.toString(workRowID) };

		List<ProjectEstimationType> lstProjEstType = new ArrayList<ProjectEstimationType>();
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID
				+ "=?", selArg);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				ProjectEstimationType projectest = new ProjectEstimationType();
				projectest
				.setEstDescription(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION)));
				projectest
				.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID)));
				projectest
				.setLineId(String.valueOf(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID))));
				projectest
				.setProjectId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID)));
				projectest
				.setLtEstId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID)));
				lstProjEstType.add(projectest);
				cursor.moveToNext();
			}
		}
		return lstProjEstType;
	}

	public long updateWorkmainWithSignature(String signature, int workRowId) {
		// TODO Auto-generated method stub
		long value;
		String[] selArgs = { String.valueOf(workRowId) };
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_WORKMAIN_SIGNNATURE, signature);

		try {
			baseDao.open();
			value = baseDao.Update(DBConstants.TABLE_WORKMAIN, contentValues,
					DBConstants.COLUMN_WORKMAIN_WORKROWID + "=?", selArgs);
			return value;
		} finally {
			baseDao.close();
		}
	}
	public String getSignaturePathFromWorkmain(int workRowId) {
		// TODO Auto-generated method stub
		String[] selArgs = { String.valueOf(workRowId) };
		String path = "";
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN,
				DBConstants.COLUMN_WORKMAIN_WORKROWID + "=?", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				path = cursor
						.getString(cursor
								.getColumnIndex(DBConstants.COLUMN_WORKMAIN_SIGNNATURE));
				cursor.moveToNext();
			}
		}
		return path;
	}

	public long updateWorkItemsTable(WorkItems items) {

		long value;
		String[] selArgs = { String.valueOf(items.getWorkItemTypeId()) };

		contentValues = new ContentValues();
		/*if (items.getAmountQuantity().equalsIgnoreCase("A")) {
			contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS, "0");
			contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT,
					items.getTotalUnits());
			contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID,
					items.getMeasurementid());
		} else if (items.getAmountQuantity().equalsIgnoreCase("Q")) {
			contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS,
					items.getTotalUnits());
			contentValues.put(
					DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT,
					items.getBaseRate()*items.getTotalUnits());
			contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID,
					items.getMeasurementid());
		} else {
			contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS,items.getTotalUnits());
			contentValues.put(
					DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT,
					items.getBaseRate()* items.getTotalUnits());
			contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID,
					items.getMeasurementid());
		}*/
		contentValues.put(DBConstants.COLUMN_WORKITEMS_TOTAL_UNITS,items.getTotalUnits());
		contentValues.put(
				DBConstants.COLUMN_WORKITEMS_TOTAL_AMOUNT,items.getTotalAmount());
		contentValues.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID,
				items.getMeasurementid());

		try {
			baseDao.open();
			value = baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS,
					contentValues,
					DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID + "=?",
					selArgs);
			return value;
		} finally {
			baseDao.close();
		}

	}

	public List<Projectestimates> getProjectEstimationList(int workRowId) {
		String[] selArg = { Integer.toString(workRowId) };
		List<Projectestimates> lstProjEst = new ArrayList<Projectestimates>();
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
				DBConstants.COLUMN_GPE_WORK_ROW_ID + "=?", selArg);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				Projectestimates projectest = new Projectestimates();
				projectest
				.setCoordinatesCaption(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_CAPTION)));
				projectest
				.setCoordinatesId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_COORDINATES_ID)));
				projectest.setDistance(cursor.getDouble(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_DISTANCE)));
				projectest.setEndLangtitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_END_LANGI)));
				projectest.setEndLattitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_END_LATI)));
				projectest.setEstimationId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_ESTIMATION_ID)));
				projectest.setIsTaping(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_ISTAPING)));
				projectest.setNoofCurves(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_NOOFCURVES)));
				projectest
				.setReceivedFromMobileDate(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_RECEIVED_FROM_MOBILE_DATA)));
				projectest.setStartLangtitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_START_LANGI)));
				projectest.setStartLattitude(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_START_LATI)));
				projectest.setTypeOfLine(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_TYPEOFLINE)));
				projectest.setWorkRowId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_WORK_ROW_ID)));
				projectest.setLtEstId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_LTESTID)));
				projectest.setMaterialId(cursor.getInt(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_MATERIALID)));
				projectest.setLayingMethod(cursor.getString(cursor
						.getColumnIndex(DBConstants.COLUMN_GPE_LAYINGMETHOD)));
				lstProjEst.add(projectest);
				cursor.moveToNext();
			}
		}
		return lstProjEst;
	}

	public int getmeasurementID(String workitemtypeid,String workRowID){
		Cursor cursor;
		int measurementid = 0;

		try{
			String[] whereArgs = {workitemtypeid,workRowID};
			baseDao.open();
			cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS,DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID+ "=? and "+DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID+"=?", whereArgs);
			if(cursor != null)
			{
				int count = cursor.getCount();
				while (count!=0) {

					measurementid = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID));
					cursor.moveToNext();
					count--;	
				}
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return measurementid;
	}
	public int getMeasurementId(int workItemTypeId){
		int measurementId = 0;
		String[] selArgs = { String.valueOf(workItemTypeId) };

		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS,
				DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID + "=?", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				measurementId = cursor
						.getInt(cursor
								.getColumnIndex(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID));
				cursor.moveToNext();
			}
		}

		return measurementId;
	}
	public long updateworkitemsformeasurement(WorkItems items,String workItemId){
		long insertid = 0 ;
		String[] selargs={String.valueOf(items.getWorkItemTypeId()),workItemId};
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_WORKITEMS_MTOTALAMOUNT,items.getmTotalAmount());
		values.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_QUANTITY,items.getMeasurmentQuantity());
		//		values.put(DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID, items.getWorkItemTypeId());
		values.put(DBConstants.COLUMN_WORKITEMS_MEASUREMENT_ID,items.getMeasurementid());
		try{
			baseDao.open();
			insertid = baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_WORKITEMS, values, DBConstants.COLUMN_WORKITEMS_WORKITEM_TYPE_ID + "=? and "+DBConstants.COLUMN_WORKITEMS_WORK_ROW_ID+"=?" , selargs);

		}catch (Exception e) {
			// TODO: handle exception
		}
		return insertid;

	}
	public HashMap<String, Integer> getMaterialData(String lineTypeId1,int lineTypeId2) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hmMaterialData = new HashMap<String, Integer>();
		String[] selArgs = {lineTypeId1,String.valueOf(lineTypeId2)};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_MATERIAL_DETAILS, DBConstants.COLUMN_GESCOM_MATERIAL_DETAILS_TYPEOFLINE+" IN(?,?)", selArgs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) { 
				hmMaterialData.put(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALDESCRIPTION)), cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALID)));
				cursor.moveToNext();
			}
		}
		return hmMaterialData;
	}
	public HashMap<String, Integer> getCompleteMaterialData() {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hmMaterialData = new HashMap<String, Integer>();
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_MATERIAL_DETAILS);
		if (cursor != null) {
			while (!cursor.isAfterLast()) { 
				hmMaterialData.put(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALDESCRIPTION)), cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALID)));
				cursor.moveToNext();
			}
		}
		return hmMaterialData;
	}
	public List<Ltline> getLtines(String projectId)
	{
		String[] selargs = {projectId};
		List<Ltline> ltlist=new ArrayList<Ltline>();  
		Ltline ltline;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				ltline=new Ltline();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("2"))
				{
					ltline.setHtId(Integer.parseInt(cursor.getString(1)));
					ltline.setDescription(cursor.getString(2));
					ltline.setLltId(Integer.parseInt(cursor.getString(4)));
					ltlist.add(ltline);
				}
				cursor.moveToNext();
			}

		}
		return ltlist;
	}
	public List<Ltline> getLtinesTemp(String projectId)
	{
		String[] selargs = {projectId};
		List<Ltline> ltlist=new ArrayList<Ltline>();  
		Ltline ltline;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				ltline=new Ltline();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("2"))
				{
					ltline.setHtId(Integer.parseInt(cursor.getString(1)));
					ltline.setDescription(cursor.getString(2));
					ltline.setLltId(Integer.parseInt(cursor.getString(4)));
					ltlist.add(ltline);
				}
				cursor.moveToNext();
			}

		}
		return ltlist;
	}
	public List<Transformer> getTransformers(String projectId)
	{
		String[] selargs = {projectId};
		List<Transformer> transfromerList=new ArrayList<Transformer>();  
		Transformer transformer;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				transformer=new Transformer();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("3"))
				{
					transformer.setHtId(Integer.parseInt(cursor.getString(1)));
					transformer.setDescription(cursor.getString(2));
					transformer.setTransId(Integer.parseInt(cursor.getString(4)));
					transfromerList.add(transformer);
				}
				cursor.moveToNext();
			}

		}
		return transfromerList;
	}
	public List<Transformer> getTransformersTemp(String projectId)
	{
		String[] selargs = {projectId};
		List<Transformer> transfromerList=new ArrayList<Transformer>();  
		Transformer transformer;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				transformer=new Transformer();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("3"))
				{
					transformer.setHtId(Integer.parseInt(cursor.getString(1)));
					transformer.setDescription(cursor.getString(2));
					transformer.setTransId(Integer.parseInt(cursor.getString(4)));
					transfromerList.add(transformer);
				}
				cursor.moveToNext();
			}

		}
		return transfromerList;
	}
	public List<Htline> getHtlines(String projectId)
	{
		String[] selargs = {projectId};
		List<Htline> HTList=new ArrayList<Htline>();  
		Htline htline;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				htline=new Htline();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("1"))
				{
					htline.setHtId(Integer.parseInt(cursor.getString(1)));
					htline.setDescription(cursor.getString(2));
					HTList.add(htline);
				}
				cursor.moveToNext();
			}

		}
		return HTList;
	}	
	public List<Htline> getHtlinesTemp(String projectId)
	{
		String[] selargs = {projectId};
		List<Htline> HTList=new ArrayList<Htline>();  
		Htline htline;
		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				htline=new Htline();
				System.out.println(cursor.getString(3));
				if(cursor.getString(3).equalsIgnoreCase("1"))
				{
					htline.setHtId(Integer.parseInt(cursor.getString(1)));
					htline.setDescription(cursor.getString(2));
					HTList.add(htline);
				}
				cursor.moveToNext();
			}

		}
		return HTList;
	}	
	public int getCoordinateidforMultiples(String workrowId, String lineType,String estimationId,String ltEstId) {
		int coordinateId = 0;
		String[] selAArgs = { workrowId, lineType ,estimationId,ltEstId};
		String query="select CoordinatesId from GESCOM_Project_Estimates where WorkRowId="+workrowId+" and TypeOfLine ="+lineType+" and Estimationid="+estimationId+" and ltEstId="+ltEstId;

		/*Cursor cursor = baseDao.Query(
					DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
					DBConstants.COLUMN_GPE_WORK_ROW_ID + "=? and "
							+ DBConstants.COLUMN_GPE_TYPEOFLINE + "=? and "+ DBConstants.COLUMN_GPE_ESTIMATION_ID + "=? and "+ DBConstants.COLUMN_GPE_LTESTID + "=?", selAArgs);*/

		Cursor cursor=baseDao.RawQuery(query, null);

		if (cursor != null) {
			coordinateId = cursor.getCount();

		}
		cursor.close();
		return coordinateId;
	}
	public HashMap<String,Object> getEstimationDescriptionMultiValueMap(String projrctId,String lineType)
	{
		String[] selargs = { projrctId,lineType,"Single Estimation" };
		HashMap<String,Object>  hm = new HashMap<String, Object>();
		List<String> lst = new ArrayList<String>();

		MultiValueMap description=new  MultiValueMap();

		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"!=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				lst.add(cursor.getString(2));
				description.put(cursor.getString(1),cursor.getString(2));
				System.out.println(cursor.getString(1)+","+cursor.getString(2));
				cursor.moveToNext();
			}
			System.out.println(description);
		}
		hm.put("MultiValueMap",description);
		hm.put("List",lst);
		return hm;
	}
	public HashMap<String, Object> getprojectsforsurvey(String Username){
		String[] args ={Username};
		HashMap<String,Object> hm = new HashMap<String, Object>();
		List<String> lst = new ArrayList<String>();
		HashMap<String,Integer> description = new HashMap<String, Integer>();
		Cursor cursor;

		cursor = baseDao.RawQuery("select * from workmain where workrowid in (select distinct(workrowid) from GESCOM_Project_Estimates) and AssignedUser =? ORDER BY Dates",args);
		if(cursor != null){

			while (!cursor.isAfterLast()) {
				lst.add(cursor.getString(1));
				description.put(cursor.getString(1),cursor.getInt(0));

				cursor.moveToNext();
			}

		}
		hm.put("MultiValueMap", description);
		hm.put("List", lst);
		return hm;

	}

	public HashMap<String, Object> getCategorynamesfrommasterCategory(){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		List<String> lst = new ArrayList<String>();
		HashMap<String, Integer>description=new  HashMap<String, Integer>();
		Cursor cursor;
		cursor = baseDao.RawQuery("select categoryID,category  from GESCOM_Master_Category", null);
		if(cursor!=null){
			while(!cursor.isAfterLast()){
				lst.add(cursor.getString(1));
				description.put(cursor.getString(1), cursor.getInt(0));
				cursor.moveToNext();
			}
		}
		hm.put("list", lst);
		hm.put("valuemap", description);
		return hm;
	}

	//for future use like it will be used in mapactivity like if user taps on ht line then to show lt and tc belonging to this ht
	public MultiValueMap getEstimationDescriptionMultiValueMap(String projrctId,String lineType,String estimationId)
	{
		String[] selargs = { projrctId,lineType,"Single Estimation",estimationId };
		MultiValueMap description=new  MultiValueMap();

		Cursor cursor=baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"!=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID+"=?", selargs);
		if(cursor !=null)
		{
			while(!cursor.isAfterLast())
			{
				description.put(cursor.getString(1),cursor.getString(2));
				System.out.println(cursor.getString(1)+","+cursor.getString(2));
				cursor.moveToNext();
			}
			System.out.println(description);
		}
		return description;
	}

	public int getLtEstCount(String string, int htcount,String linetype) {
		// TODO Auto-generated method stub
		int ltEstCount = 0;
		String[] selAArgs = { string, String.valueOf(htcount),linetype};
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + "=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID + "=? and "
						+ DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID + "=? ",selAArgs);
		if(cursor!=null){
			ltEstCount = cursor.getCount();
		}
		return ltEstCount;
	}
	public HashMap<String, Integer> getNoofCurvesCount(String workrowId) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String[] selArgs = {workrowId};
		Cursor htCursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_NOOFCURVES+"=2 and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=1", selArgs);
		if(htCursor != null){
			map.put("HtCount", htCursor.getCount());
		}
		Cursor ltCursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_NOOFCURVES+"=2 and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=2", selArgs);
		if(ltCursor != null){
			map.put("LtCount", ltCursor.getCount());
		}
		return map;
	}

	public long updateProjectEstimates(Projectestimates projectestimates,
			String string,double distance) {
		// TODO Auto-generated method stub
		long value;
		String[] selArgs = {string,String.valueOf(projectestimates.getCoordinatesId()),String.valueOf(projectestimates.getEstimationId()),String.valueOf(projectestimates.getLtEstId()),String.valueOf(projectestimates.getTypeOfLine())};
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GPE_START_LATI, projectestimates.getStartLattitude());
		contentValues.put(DBConstants.COLUMN_GPE_START_LANGI, projectestimates.getStartLangtitude());
		contentValues.put(DBConstants.COLUMN_GPE_LTPHASE, projectestimates.getLtPhase());
		contentValues.put(DBConstants.COLUMN_GPE_MATERIALID, projectestimates.getMaterialId());
		contentValues.put(DBConstants.COLUMN_GPE_DISTANCE, distance);

		try {
			baseDao.open();
			value = baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,
					contentValues,
					DBConstants.COLUMN_GPE_WORK_ROW_ID + "=? and "+DBConstants.COLUMN_GPE_COORDINATES_ID + "=? and "+DBConstants.COLUMN_GPE_ESTIMATION_ID + "=? and "+DBConstants.COLUMN_GPE_LTESTID +"=? and "+DBConstants.COLUMN_GPE_TYPEOFLINE +"=?",
					selArgs);
			return value;
		} finally {
			baseDao.close();
		}
	}

	public HashMap<String, String> getPrevEstList(String projectId, String string){
		HashMap<String, String> hm = new HashMap<String, String>();
		String[] selArgs = {projectId,string};
		Cursor cursor = baseDao.RawQuery("select s.estDescription as sdesc, e.estDescription as edesc from Gescom_project_EstimationType as s left outer join Gescom_project_EstimationType as e on (s.Estimationid = e.Estimationid and e.LineId = 1) where s.ProjectId = e.ProjectId and s.ProjectId = ? and s.LineId = ? order by s.Estimationid desc", selArgs);

		if(cursor !=null){
			while(!cursor.isAfterLast()){
				hm.put(cursor.getString(0), cursor.getString(1));
				cursor.moveToNext();
			}
		}
		return hm;
	}

	public HashMap<String, String> getEstID(String WorkRowId, String estDescription,String lineID){
		HashMap<String, String> hm = new HashMap<String, String>();
		String[] selArgs = {WorkRowId,estDescription,lineID};

		Cursor cursor = baseDao.RawQuery("Select  Estimationid from Gescom_project_EstimationType where ProjectId =? and estDescription =?  and LineId =?",selArgs);
		return hm;

	}
	public void deleteEst(String estID,String projectID){

		String[] selArgs = {estID,projectID};

		Cursor cursor = baseDao.RawQuery("Delete  from Gescom_project_EstimationType where Estimationid =? and ProjectId =? ",selArgs);


	}
	public void deleteTemp(){
		try{
			baseDao.open();
			baseDao.RawQuery("Delete  from GESCOM_Project_Estimates_Temp",null);


		}finally{
			baseDao.close();
		}



	}
	public void deleteTempEstimateType(){
		try{
			baseDao.open();
			baseDao.RawQuery("Delete  from  Gescom_project_EstimationType_Temp",null);
		}
		finally{
			baseDao.close();
		}
	}
	/*public HashMap<String, String> updateestId(String estId,String projectId){
			HashMap<String, String> hm = new HashMap<String, String>();
			String[] selArgs = {estId,projectId};
			Cursor cursor = baseDao.RawQuery("Update Gescom_project_EstimationType set Estimationid =?  where ProjectId =? ", selArgs);
			return null;





		}*/
	public void deleteEst(String estID,String projectID,String lineId, String ltest){

		String[] selArgs = {estID,projectID,lineId,ltest};

		Cursor cursor = baseDao.RawQuery("Delete  from Gescom_project_EstimationType where Estimationid =? and ProjectId =? and LineId =? and ltEstId=? ",selArgs);


	}
	public void upDateltEstimationID(String projectId, int previousEstId,int currentEstId){
		String[] args = {projectId,String.valueOf(previousEstId)};
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID, currentEstId);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, values,DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+ "=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID+"=?",args);
		}finally{
			baseDao.close();
		}
	}

	public void upDateEstimationID(String projectId, int previousEstId,int currentEstId){
		String[] args = {projectId,String.valueOf(previousEstId)};
		ContentValues values = new ContentValues();
		values.put(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID, currentEstId);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, values,DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+ "=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID+"=?",args);
		}finally{
			baseDao.close();
		}
	}



	public int getIntermediatePoleCount(String workrowId,String linetype) {
		// TODO Auto-generated method stub
		int IntermediatePoleCount = 0;
		String[] selAArgs = { workrowId, linetype,"I"};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES,DBConstants.COLUMN_GPE_WORK_ROW_ID + "=? and "+DBConstants.COLUMN_GPE_TYPEOFLINE + "=? and "+DBConstants.COLUMN_GPE_ISTAPING + "=?",selAArgs);
		if(cursor!=null){
			IntermediatePoleCount = cursor.getCount();
		}
		return IntermediatePoleCount;
	}

	public boolean isProjectACableEstimation(String projectName,int pCategory) {
		// TODO Auto-generated method stub
		boolean result = false;
		String[] selArgs = {projectName};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_WORKMAIN, DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION +"=?", selArgs);
		if(cursor!=null){
			cursor.moveToFirst();
			if(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_WORKMAIN_PROJECT_CATEGORY))==pCategory)
			{
				result =  true;
			}
		}
		return result;
	}
	public int deleteProjectEstimatesParticularRows(String workrowId,
			String typeOfLine, String estimationId, String ltEstId,
			String coordinateId) {
		// TODO Auto-generated method stub
		String[] selArgs = {workrowId,typeOfLine,estimationId,ltEstId,coordinateId};
		return baseDao.Delete(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=? and "+DBConstants.COLUMN_GPE_ESTIMATION_ID+"=? and "+DBConstants.COLUMN_GPE_LTESTID+"=? and "+DBConstants.COLUMN_GPE_COORDINATES_ID+"=?", selArgs);
	}
	public boolean isProjectEstimatesAvailable(String workRowId,
			String typeOfLine, String estimationId) {
		// TODO Auto-generated method stub
		String[] selArgs = {workRowId,typeOfLine,estimationId};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=? and "+DBConstants.COLUMN_GPE_ESTIMATION_ID+"=?",selArgs);
		if(cursor != null){
			if(cursor.getCount()>=1){
				return true;
			}
		}
		return false;
	}
	public int deleteProjectEstimatesParticularRows(String workrowId,
			String TypeOfLine, String estimationId) {
		// TODO Auto-generated method stub
		String[] selArgs = {workrowId,TypeOfLine,estimationId};
		return baseDao.Delete(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=? and "+DBConstants.COLUMN_GPE_ESTIMATION_ID+"=?",selArgs);
	}

	public HashMap<String, Integer> getStudePoleCount(String workrowId) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String[] selArgs = {workrowId};
		Cursor htCursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_NOOFCURVES+"=2 and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=1", selArgs);
		if(htCursor != null){
			map.put("HtCount", htCursor.getCount());
		}
		Cursor ltCursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES, DBConstants.COLUMN_GPE_WORK_ROW_ID+"=? and "+DBConstants.COLUMN_GPE_NOOFCURVES+"=2 and "+DBConstants.COLUMN_GPE_TYPEOFLINE+"=2", selArgs);
		if(ltCursor != null){
			map.put("LtCount", ltCursor.getCount());
		}
		return map;
	}

	public int findHtCount(String workRowId)
	{
		int HtEstCount = 0;
		Cursor cursor = baseDao.RawQuery("Select DISTINCT  Estimationid from GESCOM_Project_Estimates where Workrowid= "+workRowId+" and TypeOfLine=1", null);
		if(cursor!=null)
		{
			HtEstCount=cursor.getCount();
		}
		return HtEstCount;

	}
	public int findLtCount(String workRowId)
	{
		int HtEstCount = 0;
		Cursor cursor = baseDao.RawQuery("Select DISTINCT  Estimationid,ltEstId from GESCOM_Project_Estimates where Workrowid= "+workRowId+" and TypeOfLine=2", null);
		if(cursor!=null)
		{
			HtEstCount=cursor.getCount();
		}
		return HtEstCount;

	}
	public int findTcCount(String workRowId)
	{
		int HtEstCount = 0;
		Cursor cursor = baseDao.RawQuery("Select DISTINCT  Estimationid,ltEstId from GESCOM_Project_Estimates where Workrowid= "+workRowId+" and TypeOfLine=3", null);
		if(cursor!=null)
		{
			HtEstCount=cursor.getCount();
		}
		return HtEstCount;

	}
	public int findBendCount(String workRowId,String tpyeofline)
	{
		int HtEstCount = 0;
		Cursor cursor = baseDao.RawQuery("Select  NoofCurves from GESCOM_Project_Estimates where Workrowid= "+workRowId+" and TypeOfLine= "+tpyeofline+ " and  NoofCurves!=0;", null);
		if(cursor!=null)
		{
			HtEstCount=cursor.getCount();
		}
		return HtEstCount;

	}

	public void deleteEst(String estimationId, String projectId, int lineId) {
		String[] args = {estimationId,projectId,String.valueOf(lineId)};
		Cursor cursor = baseDao.RawQuery("Delete  from Gescom_project_EstimationType where Estimationid =? and ProjectId =? and LineId=? ",args);

	}

	public int getEstimationIdFromDesc(String workrowid, String workDesc) {
		// TODO Auto-generated method stub
		int estimationId = 0;
		String[] selArgs = {workrowid,workDesc};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION +"=?", selArgs);
		if(cursor != null){
			while (!cursor.isAfterLast()) {
				estimationId = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID));
				cursor.moveToNext();
			}
		}
		return estimationId;
	}

	public double getLayingLength(String workRowId, String lineType,String layingMthod) {
		double totalDistance = 0.0;
		String Query = "select sum(" + DBConstants.COLUMN_GPE_DISTANCE
				+ ") from " + DBConstants.TABLE_GESCOM_PROJECT_ESTIMATES
				+ " where " + DBConstants.COLUMN_GPE_WORK_ROW_ID + " ="
				+ workRowId + " and " + DBConstants.COLUMN_GPE_TYPEOFLINE
				+ " = " + lineType +" and " + DBConstants.COLUMN_GPE_LAYINGMETHOD+" = '"+layingMthod+"'";
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				double distance = cursor.getDouble(0);
				totalDistance = Math.round(totalDistance + distance);
				cursor.moveToNext();
			}

		}
		cursor.close();
		return totalDistance;
	}
	public List<String> findLayingMethod(String workRowId)
	{
		List<String> layingMethods=new ArrayList<String>();
		String Query="select  DISTINCT layingMethod from GESCOM_Project_Estimates where WorkRowId = "+workRowId;
		Cursor cursor = baseDao.RawQuery(Query, null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				String method = cursor.getString(0);
				layingMethods.add(method);
				cursor.moveToNext();
			}

		}
		return layingMethods;
	}
	public String findRccPoleCount(String workRowId,int spanlegth)
	{
		StringBuilder formula=new StringBuilder();
		Cursor cursor = baseDao.RawQuery("Select DISTINCT Estimationid,ltEstId from GESCOM_Project_Estimates where Workrowid="+workRowId+" and TypeOfLine=2", null);
		if(cursor!=null)
		{
			while (!cursor.isAfterLast()) {
				Cursor cursor2 = baseDao.RawQuery("Select sum(Distance) from GESCOM_Project_Estimates where Estimationid= "+cursor.getInt(0)+" and ltEstId="+cursor.getInt(1)+" and TypeOfLine=2", null);
				String ting;
				if (formula.length()==0)
					formula.append(String.valueOf(Math.round(cursor2.getDouble(0)/spanlegth)));
				else
					formula.append("+"+String.valueOf(Math.round(cursor2.getDouble(0)/spanlegth)));
				cursor.moveToNext();
			}
		}

		return formula.toString();
	}	



	public List<String> getCaptionListFromApprCoor(
			int workRowId) {
		// TODO Auto-generated method stub
		List<String> lstCaption = new ArrayList<String>();
		String[] selArgs = {String.valueOf(workRowId)};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+"=?", selArgs);
		if(cursor != null){
			while (!cursor.isAfterLast()) {
				lstCaption.add(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION)));
				cursor.moveToNext();
			}
		}
		return lstCaption;
	}

	public HashMap<String, Object> getMultiList(String workRowId)
	{
		String[] selargs = { String.valueOf(workRowId) };
		HashMap<String,String> multiDescription=new HashMap<String, String>();
		HashMap<String, Object> hm = new HashMap<String, Object>();
		List<String> lst = new ArrayList<String>();
		Cursor cursor = baseDao.Query(
				DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE,
				DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID + "=?",
				selargs);
		if(cursor!=null)
		{
			while (!cursor.isAfterLast()) {
				multiDescription.put(cursor.getString(2), cursor.getString(4));
				lst.add(cursor.getString(2));
				cursor.moveToNext();

			}
		}
		hm.put("HashMap", multiDescription);
		hm.put("List", lst);
		return hm;
	}

	public void updateAppCoor(String workRowId, String updatedCaption, int updatedPoleId,
			String prevCaption) {
		// TODO Auto-generated method stub
		String[] selArgs = {workRowId,prevCaption};
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION, updatedCaption);
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_POLETYPE, updatedPoleId);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+ " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION+"=?", selArgs);

		}finally{
			baseDao.close();
		}

	}
	public void updateApprPhotoCoor(String workRowId, String updatedCaption,
			String prevCaption) {
		// TODO Auto-generated method stub
		String[] selArgs = {workRowId,prevCaption};
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption, updatedCaption);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId+ " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption+"=?", selArgs);

		}finally{
			baseDao.close();
		}
	}
	public int deleteApprCoorFromTable(String workRowId,
			ApprovedCoordinates coor) {
		// TODO Auto-generated method stub
		int res = 0;
		String[] selArgs = {workRowId,coor.getsCaption()};
		try{
			baseDao.open();
			res = baseDao.Delete(DBConstants.TABLE_GESCOM_APPROVE_COOR, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION +"=?", selArgs);
		}
		finally{
			baseDao.close();
		}
		return res;
	}
	public int getTypeOfLine(String workRowId, String estDesc) {
		// TODO Auto-generated method stub
		int typeOfLine = 0;
		String[] selArgs = {workRowId,estDesc};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION+"=?", selArgs);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				typeOfLine = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID));
				cursor.moveToNext();
			}

		}
		cursor.close();
		return typeOfLine;
	}
	public boolean isApprCoorAvailable(String workRowId,
			ApprovedCoordinates coor) {
		// TODO Auto-generated method stub
		boolean isPresent = false;
		String[] selArgs = {workRowId,String.valueOf(coor.getEstimationid()),String.valueOf(coor.getCoordinatesId()),String.valueOf(coor.getLtEstId()),String.valueOf(coor.getTypeOfLine())};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID +"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_LTESTID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE+"=?", selArgs);
		if (cursor != null) {
			if(cursor.getCount()>=1){
				return true;
			}
		}
		cursor.close();
		return false;
	}
	public boolean isApprPhotoCoorAvailable(String workRowId,
			String strCaption) {
		// TODO Auto-generated method stub
		String[] selArgs = {workRowId,strCaption};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption+"=?", selArgs);
		if (cursor != null) {
			if(cursor.getCount()>=1){
				return true;
			}
		}
		return false;
	}
	public int deleteApprPhotoCoor(String workRowId, String strCaption) {
		// TODO Auto-generated method stub
		int res = 0;
		String[] selArgs = {workRowId,strCaption};
		try{
			baseDao.open();
			res = baseDao.Delete(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption +"=?", selArgs);
		}
		finally{
			baseDao.close();
		}
		return res;
	}

	public void updateAppCoorForID(ApprovedCoordinates currApCo) {
		// TODO Auto-generated method stub
		String[] selArgs = {String.valueOf(currApCo.getWorkRowId()),currApCo.getsCaption()};
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID, currApCo.getCoordinatesId());
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+ " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION+"=?", selArgs);

		}finally{
			baseDao.close();
		}

	}
	public int updateApprPhCoorId(String workRowId, String cap,
			String coorId) {
		// TODO Auto-generated method stub
		String[] selArgs = {workRowId,cap};
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CoordinateId, coorId);
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId+ " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption+"=?", selArgs);

		}finally{
			baseDao.close();
		}

		return 0;
	}
	public String getEstTypeDesc(ApprovedCoordinates ac) {
		// TODO Auto-generated method stub
		String estTypeDesc = "";
		String[] selArgs = {ac.getWorkRowId()+"",ac.Estimationid+"",ac.getLtEstId(),ac.getTypeOfLine()+""};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_PROJECT_ESTIMATION_TYPE, DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID+"=? and "+DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID+"=?", selArgs);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				estTypeDesc = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION));
				cursor.moveToNext();
			}

		}
		return estTypeDesc;
	}
	public List<String> getProjectForUser(String username) {
		// TODO Auto-generated method stub
		List<String> lst = new ArrayList<String>();
		String[] selArgs = {username};
		Cursor cursor = baseDao.RawQuery(" select * from Workmain where WorkRowid in (select distinct(WorkRowId) from GESCOM_Project_Estimates) and AssignedUser = '"+username+"'", null);
		if (cursor != null) {
			int count = cursor.getCount();
			while (!cursor.isAfterLast()) {
				lst.add(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_WORKMAIN_WORK_DESCRIPTION)));
				cursor.moveToNext();
			}

		}
		return lst;
	}

	public int getlastcoordinateid(int workRowId,String ltestid,String estimationId, String typeOfLine) {
		String[] selargs = { String.valueOf(workRowId),ltestid,estimationId,typeOfLine};
		int coordinateid = 0;
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR,
				DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID + " =? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_LTESTID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE+"=?",
				selargs);
		if (cursor != null) {
			while (!cursor.isAfterLast()) {
				coordinateid = cursor
						.getInt(cursor
								.getColumnIndex(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ID));
				cursor.moveToNext();
			}
		}
		return coordinateid;
	}
	public void updateMeasurementQuantityTozeroForFirstTime(String userName,
			String status, int workRowId) {
		// TODO Auto-generated method stub
		try{
			baseDao.open();
			baseDao.RawQuery("update WorkItems set MeasurmentQuantity = 0 where WorkRowid in (select WorkRowid from Workmain where WorkStatusid = '"+status+"' and AssignedUser = '"+userName+"')", null);
		}finally{
			baseDao.close();
		}
	}
	public void updateApprovedphotocoordinatesSyncyes(String workRowId) {
		// TODO Auto-generated method stub
		
		String[] selargs = {workRowId};
		
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_ISUPLOADED, "Y");
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR_PHOTOS, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId+"=?", selargs);
		}finally
		{
			baseDao.close();
		}
		
	}
	
	public void updateApprovedcoordinatesSyncyes(String workRowId) {
		// TODO Auto-generated method stub
		
		String[] selargs = {workRowId};
		
		contentValues = new ContentValues();
		contentValues.put(DBConstants.COLUMN_GESCOM_APPROVE_COOR_ISUPLOADED, "Y");
		try{
			baseDao.open();
			baseDao.Update(DBConstants.TABLE_GESCOM_APPROVE_COOR, contentValues, DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID+"=?", selargs);
		}finally
		{
			baseDao.close();
		}
		
	}
	public boolean isThisCaptionFromApprovedCoorUploaded(int workRowId, String strCaption) {
		// TODO Auto-generated method stub
		String selArgs[] = {strCaption,String.valueOf(workRowId)};
		Cursor cursor = baseDao.Query(DBConstants.TABLE_GESCOM_APPROVE_COOR, DBConstants.COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION+"=? and "+DBConstants.COLUMN_GESCOM_APPROVE_COOR_WORKROWID, selArgs);
		if(cursor.getCount()>0){
			return true;
		}
		return false;
	}
}

