package com.zeal.gov.kar.gescom.database;

public class DBConstants {

	public static final String DATABASE_NAME = "gescom.db";
	public static final int DATABASE_VERSION = 1;
	// Login table
	public static final String TABLE_AUTH = "LoginInfo";
	public static final String COLUMN_AUTH_USERNAME = "LoginId";
	public static final String COLUMN_AUTH_PASSWORD = "Password";
	public static final String COLUMN_AUTH_OFFICER_NAME = "OfficerName";
	public static final String COLUMN_AUTH_ACTIVATED = "Activated";
	public static final String COLUMN_AUTH_ACTIVATED_ON = "ActivatedOn";
	
	public static final String TABLE_GESCOM_COMPONET_DETAILS="GESCOM_Componet_Details";
	public static final String COLUMN_GESCOM_COMPONET_DETAILS_WORKROWID="WorkRowId";
	public static final String COLUMN_GESCOM_COMPONET_DETAILS_CAPTION="Caption";
	public static final String COLUMN_GESCOM_COMPONET_DETAILS_TYPEOFLINE="TypeOfLine";
	public static final String COLUMN_GESCOM_COMPONET_DETAILS_MATERIALID="MaterialId";
	public static final String COLUMN_GESCOM_COMPONET_DETAILS_DISTANCE="Distance";
	
	public static final String TABLE_MASTER_WORK_ITEM_TYPE = "Master_WorkitemType";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_WORKITEMTYPEID = "WorkItemTypeID";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_WORKITEMDESCRIPTION = "WorkItemDescription";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_MEASUREMENTID = "MeasurementId";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_DESCRIPTION = "Description";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_BASERATE = "BaseRate";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_ISITPROCESS = "IsitProcess";
	public static final String COLUMN_TABLE_MASTER_WORK_ITEM_TYPE_APPLIID = "AppliId";

	
	public static final String TABLE_MASTER_USERROLES="Master_UserRoles";
	public static final String COLUMN_MASTTER_USERROLES_LOGINID="LoginId";
	public static final String COLUMN_MASTTER_USERROLES_ROLEID="RoleId";
	
	public static final String TABLE_GESCOM_PROJECT_ESTIMATION_TYPE = "Gescom_project_EstimationType";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID = "ProjectId";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID = "Estimationid";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION = "estDescription";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID = "LineId";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID = "ltEstId";
	
	public static final String TABLE_GESCOM_PROJECT_ESTIMATION_TYPE_Temp = "Gescom_project_EstimationType_Temp";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_PROJECT_ID_Temp = "ProjectId_Temp";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_ID_Temp = "Estimationid_Temp";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_ESTIMATION_DESCRIPTION_Temp = "estDescription_Temp";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LINE_ID_Temp = "LineId_Temp";
	public static final String COLUMN_GESCOM_PROJECT_ESTIMATIONTYPE_LTEST_ID_Temp = "ltEstId_Temp";

	public static final String TABLE_WORKMAIN = "Workmain";
	public static final String COLUMN_WORKMAIN_WORKROWID = "WorkRowid";
	public static final String COLUMN_WORKMAIN_WORK_DESCRIPTION = "WorkDescription";
	public static final String COLUMN_WORKMAIN_PROJECT_CATEGORY = "ProjectCategory";
	public static final String COLUMN_WORKMAIN_NOOFLT = "NoofLT";
	public static final String COLUMN_WORKMAIN_REASON = "Reason";
	public static final String COLUMN_WORKMAIN_WORKSTATUSID = "WorkStatusid";
	public static final String COLUMN_WORKMAIN_DATES = "Dates";
	public static final String COLUMN_WORKMAIN_NOOFHT = "NoofHT";
	public static final String COLUMN_WORKMAIN_SIGNNATURE = "Signature";
	public static final String COLUMN_WORKMAIN_ASSIGNED_USER = "AssignedUser";
	public static final String COLUMN_WORKMAIN_SPAN_LENGTH = "SpanLength";

	public static final String TABLE_GESCOM_PROJECT_ESTIMATES = "GESCOM_Project_Estimates";
	public static final String COLUMN_GPE_WORK_ROW_ID = "WorkRowId";
	public static final String COLUMN_GPE_COORDINATES_ID = "CoordinatesId";
	public static final String COLUMN_GPE_ESTIMATION_ID = "Estimationid";
	public static final String COLUMN_GPE_TYPEOFLINE = "TypeOfLine";
	public static final String COLUMN_GPE_START_LATI = "StartLattitude";
	public static final String COLUMN_GPE_START_LANGI = "StartLangtitude";
	public static final String COLUMN_GPE_END_LATI = "EndLattitude";
	public static final String COLUMN_GPE_END_LANGI = "EndLangtitude";
	public static final String COLUMN_GPE_DISTANCE = "Distance";
	public static final String COLUMN_GPE_NOOFCURVES = "NoofCurves";
	public static final String COLUMN_GPE_RECEIVED_FROM_MOBILE_DATA = "ReceivedFromMobileDate";
	public static final String COLUMN_GPE_COORDINATES_CAPTION = "CoordinatesCaption";
	public static final String COLUMN_GPE_ISTAPING = "IsTaping";
	public static final String COLUMN_GPE_MATERIALID = "MaterialId";
	public static final String COLUMN_GPE_LTESTID = "ltEstId";
	public static final String COLUMN_GPE_LTPHASE = "ltPhase";

	public static final String COLUMN_GPE_LAYINGMETHOD = "layingMethod";
	

	public static final String TABLE_GESCOM_PROJECT_ESTIMATES_TEMP = "GESCOM_Project_Estimates_Temp";
	public static final String COLUMN_TEMPGPE_WORK_ROW_ID = "WorkRowId_Temp";
	public static final String COLUMN_TEMPGPE_COORDINATES_ID = "CoordinatesId_Temp";
	public static final String COLUMN_TEMPGPE_ESTIMATION_ID = "Estimationid_Temp";
	public static final String COLUMN_TEMPGPE_TYPEOFLINE = "TypeOfLine_Temp";
	public static final String COLUMN_TEMPGPE_START_LATI = "StartLattitude_Temp";
	public static final String COLUMN_TEMPGPE_START_LANGI = "StartLangtitude_Temp";
	public static final String COLUMN_TEMPGPE_END_LATI = "EndLattitude_Temp";
	public static final String COLUMN_TEMPGPE_END_LANGI = "EndLangtitude_Temp";
	public static final String COLUMN_TEMPGPE_DISTANCE = "Distance_Temp";
	public static final String COLUMN_TEMPGPE_NOOFCURVES = "NoofCurves_Temp";
	public static final String COLUMN_TEMPGPE_RECEIVED_FROM_MOBILE_DATA = "ReceivedFromMobileDate_Temp";
	public static final String COLUMN_TEMPGPE_COORDINATES_CAPTION = "CoordinatesCaption_Temp";
	public static final String COLUMN_TEMPGPE_ISTAPING = "IsTaping_Temp";
	public static final String COLUMN_TEMPGPE_MATERIALID = "MaterialId_Temp";
	public static final String COLUMN_TEMPGPE_LTESTID = "ltEstId_Temp";
	public static final String COLUMN_TEMPGPE_LTPHASE = "ltPhase_Temp";

	public static final String COLUMN_TEMPGPE_LAYINGMETHOD = "layingMethod_Temp";
	
	public static final String TABLE_GESCOM_PROJECT_WORKITEMS = "WorkItems";
	public static final String COLUMN_WORKITEMS_WORK_ROW_ID = "WorkRowid";
	public static final String COLUMN_WORKITEMS_ESTIMATION_ID = "EstimationId";
	public static final String COLUMN_WORKITEMS_SUBWORK_ID = "SubWorkid";
	public static final String COLUMN_WORKITEMS_CATEGARY = "Category";
	public static final String COLUMN_WORKITEMS_SUBCATEGARY = "SubCategory";
	public static final String COLUMN_WORKITEMS_COMPONEANT_ID = "ComponentId";
	public static final String COLUMN_WORKITEMS_WORKITEM_TYPE_ID = "WorkItemTypeId";
	public static final String COLUMN_WORKITEMS_WORKITEM_DESCRIPTION = "WorkItemDescription";
	public static final String COLUMN_WORKITEMS_MEASUREMENT_ID = "Measurementid";
	public static final String COLUMN_WORKITEMS_COSTPERITEM = "CostPerItem";
	public static final String COLUMN_WORKITEMS_TOTAL_UNITS = "TotalUnits";
	public static final String COLUMN_WORKITEMS_TOTAL_AMOUNT = "TotalAmount";
	public static final String COLUMN_WORKITEMS_GPSQUANTITY = "GPSQuantity";
	public static final String COLUMN_WORKITEMS_SRRATE = "SRRate";
	public static final String COLUMN_WORKITEMS_SRYEAR = "SRYear";
	public static final String COLUMN_WORKITEMS_BLOCK_ID = "BlockId";
	public static final String COLUMN_WORKITEMS_GROUP_ID = "GroupId";
	public static final String COLUMN_WORKITEMS_BASE_RATE = "BaseRate";
	public static final String COLUMN_WORKITEMS_BLOCK_NAME = "BlockName";
	public static final String COLUMN_WORKITEMS_FIXED = "Fixed";
	public static final String COLUMN_WORKITEMS_CONSTANT = "Constant";
	public static final String COLUMN_WORKITEMS_CONSTANT_VALUE = "ConstantValue";
	public static final String COLUMN_WORKITEMS_FORMULA = "Formula";
	public static final String COLUMN_WORKITEMS_GROUPNAME = "GroupName";
	public static final String COLUMN_WORKITEMS_AMOUNT_QUANTITY = "AmountQuantity";
	public static final String COLUMN_WORKITEMS_ITEM_CODE = "ItemCode";
	public static final String COLUMN_WORKITEMS_MEASUREMENT_QUANTITY = "MeasurmentQuantity";
	public static final String COLUMN_WORKITEMS_MTOTALAMOUNT = "MTotalAmount";
	public static final String COLUMN_WORKITEMS_DECROUND = "DecRound";
	public static final String COLUMN_WORKITEMS_ISINTEGER = "IsInteger";

	public static final String TABLE_WORKITEM_TYPE = "workitemtype";
	public static final String COLUMN_WORKITEM_TYPE_ID = "woerkitemtypeid";
	public static final String COLUMN_WORKITEM_TYPE_COMPONENT_ID = "componentid";
	public static final String COLUMN_WORKITEM_TYPE_WORKITEMDESCRIPTON = "workitemdescription";
	public static final String COLUMN_WORKITEM_TYPE_MEASUREMENT_ID = "measurementId";
	public static final String COLUMN_WORKITEM_TYPE_DESCRIPTION = "Description";
	public static final String COLUMN_WORKITEM_TYPE_BASERATE = "Baserate";
	public static final String COLUMN_WORKITEM_TYPE_ISIT_PROCESS = "IsItProcess";

	public static final String TABLE_WORK_TASKPLAN_TRANSACTION = "worktaskplantransaction";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_WORKROWID = "workrowid";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_SUBWORKROWID = "subworkrowid";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_TASKID = "taskid";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_TRANSACTIONID = "transactionid";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_UPDATEDBY = "transactionupdatedby";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_UPDATED_DATE = "transactionUpdatedate";
	public static final String COLUMN_WORK_TASKPLAN_TRANSACTION_PERCENTAGE_COMPLETE = "percentagecomplete";

	public static final String TABLE_GESCOM_APPROVE_COOR_PHOTOS = "GESCOM_ApproveCoor_Photos";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_EstimationId = "Estimationid";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CoordinateId = "Coordinatesid";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoId = "Photoid";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_PhotoCaption = "PhotoCaption";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_FileName = "FileName";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedOn = "CaptureOn";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_CapturedBy = "CaptureBy";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_Coordinates = "Coordinates";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_WorkrowId = "Workrowid";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_LTESTID = "ltEstId";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_TYPEOFLINE = "TypeOfLine";
	public static final String COLUMN_GESCOM_APPROVE_COOR_PHOTOS_ISUPLOADED = "isUploaded";
     
	public static final String TABLE_MASTER_MEASUREMENT ="Master_Measurement";
	public static final String COLUMN_MASTER_MEASUREMENT_MEASUREMENTID ="MeasurementId";
	public static final String COLUMN_MASTER_MEASUREMENT_MEASUREMENTDESCRIPTION ="MeasurementDescription";

	public static final String TABLE_GESCOM_APPROVE_COOR = "GESCOM_Approved_Coordinates";
		public static final String COLUMN_GESCOM_APPROVE_COOR_ESTIMATIONID  = "Estimationid";
		public static final String COLUMN_GESCOM_APPROVE_COOR_ID= "Coordinatesid";
		public static final String COLUMN_GESCOM_APPROVE_COOR_LATITUDE= "Latitude";
		public static final String COLUMN_GESCOM_APPROVE_COOR_LONGITUDE = "Longitude";
		public static final String COLUMN_GESCOM_APPROVE_COOR_CAPTUREDON = "CaptureOn";
		public static final String COLUMN_GESCOM_APPROVE_COOR_CAPTUREDBY="CaptureBy";
		public static final String COLUMN_GESCOM_APPROVE_COOR_WORKROWID = "WorkRowId";
		public static final String COLUMN_GESCOM_APPROVE_COOR_POINTCAPTION= "PointCaption";
		public static final String COLUMN_GESCOM_APPROVE_COOR_POLETYPE= "poletype";
		public static final String COLUMN_GESCOM_APPROVE_COOR_LTESTID = "ltEstId";
		public static final String COLUMN_GESCOM_APPROVE_COOR_TYPEOFLINE = "TypeOfLine";
		public static final String COLUMN_GESCOM_APPROVE_COOR_ISUPLOADED = "isUploaded";

		//Master_Pole_Type
		
		public static final String TABLE_POLE = "Master_Pole_Type";
		public static final String COLUMN_POLE_ID="PoleId";
		public static final String COLUMN_POLE_DESCRIPTION="PoleDescription";
	    public static final String COLUMN_POLE_ACTIVE="Active";
	    
	    //GESCOM_Progress_Images
	    public static final String TABLE_GESCOM_PROGRESS_IMAGES = "GESCOM_Progress_Images";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_WORKROWID = "WorkRowId";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_WORKSTATUS = "Workstatus";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_FILENAME = "FileName";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_PHOTOCAPTION = "PhotoCaption";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_LATITUDE = "Latitude";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_LONGITUDE = "Longitude";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDON = "CapturedOn";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_CAPTUREDBY = "capturedBy";
	    public static final String COLUMN_GESCOM_PROGRESS_IMAGES_ISIMAGEUPLOADED = "isImageUploaded";
	    
	    //TABLE_GESCOM_MATERIAL_DETAILS
	    public static final String TABLE_GESCOM_MATERIAL_DETAILS = "GESCOM_Material_Details";
	    public static final String COLUMN_GESCOM_MATERIAL_DETAILS_TYPEOFLINE = "TypeOfLine";
	    public static final String COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALID = "MaterialId";
	    public static final String COLUMN_GESCOM_MATERIAL_DETAILS_MATERIALDESCRIPTION = "MaterialDescription";
}
