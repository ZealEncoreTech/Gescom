package com.zeal.gov.kar.gescom.proxy;

public class WebserviceConstants {


	//Live_testing_server
	//  public static final String HOST_URL = "http://124.153.106.183:62/";
	//Live_Server(In Production)
    public static final String HOST_URL = "http://124.153.106.183:85/";

	//Local_Testing
	//public static final String HOST_URL = "http://192.168.1.118:50/";
	public static final String NAMESPACE="http://tempuri.org/";
	public static final String HOST_URL_FOR_WORKITEMSBO = HOST_URL+"WorkItemsBO.asmx";
	
	public static final String LOGIN_VALIDATE_PORT = "UserBO.asmx";
	public static final String GESCOM_SERVICES_PORT = "GESCOM.asmx";
    
	public static final String LOGIN_VALIDATE_METHOD="LoginValidate";
	public static final String LOGIN_VALIDATE_RESULT="LoginValidateResult";
	
	public static final String GET_ESTIMATION_FROM_MOBILE_METHOD="GetEstimationFromMobile";
	public static final String GET_ESTIMATION_FROM_MOBILE_RESULT="GetEstimationFromMobileResult";
	
	public static final String GET_ESTIMATE_ITEMS_METHOD="GetEstimateItems";
	public static final String GET_ESTIMATE_ITEMS_RESULT="GetEstimateItemsResult";
	
	public static final String GET_ESTIMATION_FOR_MESUREMENT_METHOD="GetEstimationForMeasurment";
	public static final String GET_ESTIMATION_FOR_MESUREMENT_RESULT="GetEstimationForMeasurmentResult";
	
	public static final String GET_WORK_STATUS_METHOD="GetStatusWorks";
	public static final String GET_WORK_STATUS_METHOD_RESULT="GetStatusWorksResult";
	
	public static final String MOBILE_REGISTER_METHOD="MobileRegister";
	public static final String MOBILE_REGISTER_METHOD_RESULT="MobileRegisterResult";
	
	public static final String GET_PROJECT_FOR_MOBILE="ProjectsGetForMobile";
	public static final String GET_PROJECT_FOR_MOBILE_RESULT="ProjectsGetForMobileResult";
	
	public static final String UPLOAD_PROJECT_METHOD="UploadProjectData";
	public static final String UPLOAD_PROJECT_RESULT="UploadProjectDataResult";
	
	public static final String VERSION_UPDATE="VersionUpdation ";
	public static final String VERSION_UPDATE_RESULT="VersionUpdationResult";
	
	public static final String GET_WORK_FINANCIAL_METHOD="GetWorkFinancialforanorid";
	public static final String GET_WORK_FINANCIAL_RESULT="GetWorkFinancialforanoridResponse";
	
	public static final String GET_TASK_DETAILS_METHOD="GetTaskDetailsforanorid";
	public static final String GET_TASK_DETAILS_RESULT="GetTaskDetailsforanoridResponse";
	
	public static final String UPLOAD_IMAGE_TOSERVER="UpdateImagestoServer";
	public static final String UPLOAD_IMAGE_TOSERVER_RESPONSE="UpdateImagestoServerResponse";
	
	public static final String UPLOAD_COORDINATES_TOSERVER = "UpdateCoordinatestoServer";
	public static final String UPLOAD_COORDINATES_TOSERVER_RESPONSE = "UpdateCoordinatestoServerResponse";
	
	public static final String ADD_WORK_STATUS = "AddWorkStatus";
	public static final String ADD_WORK_STATUS_RESPONSE = "AddWorkStatusResponse";
	
	public static final String UPLOAD_FINANCIAL_DETAILS_METHOD="UpdateWorkFinancial";

	public static final String UPDATE_TASK_DETAILS = "UpdateTaskDetails";
	public static final String UPDATE_TASK_DETAILS_RESULT = "UpdateTaskDetailsResponse";
	
	public static final String GET_SURVEYWORKS = "GetSurveyWorks";
	public static final String GET_SURVEYWORKS_RESPONSE = "GetSurveyWorksResult";
	
	public static final String GET_SURVEYCOORDINATES = "GetSurveyCoordinates";
	public static final String GET_SURVEYCOORDINATES_RESPONSE = "GetSurveyCoordinatesResult";
}
