package com.zeal.gov.kar.gescom.proxy;


import java.util.ArrayList;
import java.util.List;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.zeal.gov.kar.gescom.model.ApprovedCoordinates;
import com.zeal.gov.kar.gescom.model.ApprovedPhotoCoordinates;
import com.zeal.gov.kar.gescom.model.MeasurementEstimation;
import com.zeal.gov.kar.gescom.model.Projectdata;
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.model.Uploadestimation;
import com.zeal.gov.kar.gescom.model.User;

import com.zeal.gov.kar.gescom.parser.Parser;

@SuppressLint("NewApi")
public class Soapproxy {

	@SuppressWarnings("unused")
	private Context context;

	public Soapproxy(Context context) {
		this.context = context;
	}
	

	// Validate users.
	public String validateUser(String logId, String pasword) {
		String response = null;
	try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.LOGIN_VALIDATE_METHOD);
			request.addProperty("LoginId", logId);
			request.addProperty("Password", pasword);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.LOGIN_VALIDATE_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.LOGIN_VALIDATE_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.LOGIN_VALIDATE_RESULT).toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return response;
	}
	

	// Upload Estimation information to Server.
	public String uploadEstimation(Uploadestimation estimation) {
		String response = "";
		try {
			
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_ESTIMATION_FROM_MOBILE_METHOD);
			request.addProperty("sXml", estimation.getsXml());
			request.addProperty("strStatus", estimation.getStrStatus());
			request.addProperty("iWorkRowid", estimation.getiWorkRowid());
			request.addProperty("Signature", estimation.getSignature());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_ESTIMATION_FROM_MOBILE_METHOD,
					envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.GET_ESTIMATION_FROM_MOBILE_RESULT)
						.toString();
				if(response.contains("Send Sucessfully")){
					response= "success";
				}
//				StrictMode.setThreadPolicy(old);
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
			response = "Failure";
		}
		return response;
	}

	// Get Estimation Item.
	public String getEstimateItems(String estimateid) {
		String response = null;
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_ESTIMATE_ITEMS_METHOD);
			request.addProperty("estimateid", estimateid);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_ESTIMATE_ITEMS_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.GET_ESTIMATE_ITEMS_RESULT)
						.toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return response;
	}
	
	public List<MeasurementEstimation> getEstimateForMeasurements(String estimatationid,
			String projectid) {
		String response = null;
		List<MeasurementEstimation> lstmeasureest = new ArrayList<MeasurementEstimation>();
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_METHOD);
			request.addProperty("projectid", projectid);
			request.addProperty("Estimationid", estimatationid);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_METHOD,
					envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject
						.getProperty(
								WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_RESULT)
						.toString();
				System.out.println("------"+response);
				Parser parser = new Parser(context);
				lstmeasureest = parser.getmeasurementestimationlist(response);
				
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return lstmeasureest;
	}

	// Get Estimation for Measurement.


	public List<TaskDetails> getWorksStatus(String userid, String status) {
		 String response = null;
		 List<TaskDetails> lstint = new ArrayList<TaskDetails>();
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_WORK_STATUS_METHOD);
			request.addProperty("Userid", userid);
			request.addProperty("status", status);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_WORK_STATUS_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.GET_WORK_STATUS_METHOD_RESULT)
						.toString();
				Parser parser = new Parser(context);
				lstint = parser.getworkstatus(response);
				StrictMode.setThreadPolicy(old);
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return lstint;
	}
	// Get work Status.
/*	public List<Integer> getWorkStatus(String userid, String status) {
		 String response = null;
		 List<Integer> lstint = new ArrayList<Integer>();
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_WORK_STATUS_METHOD);
			request.addProperty("Userid", userid);
			request.addProperty("status", status);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_WORK_STATUS_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.GET_WORK_STATUS_METHOD_RESULT)
						.toString();
				Parser parser = new Parser(context);
				lstint = parser.getworkstatus(response);
				StrictMode.setThreadPolicy(old);
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return lstint;
	}*/


	// Register new Mobile.
	public String registerMobile(User mobileInfo) {
		String response = null;
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.MOBILE_REGISTER_METHOD);
			request.addProperty("sIMEIno", mobileInfo.getImeiNo());
			request.addProperty("sSimNo", mobileInfo.getSimNo());
			request.addProperty("sMobileNo", mobileInfo.getMobileNo());
			request.addProperty("sVerifyUsername", mobileInfo.getUserName());
			request.addProperty("sVerifyPassword", mobileInfo.getHashPassword());
			request.addProperty("sRegAs", mobileInfo.getRegisterAs());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.MOBILE_REGISTER_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.MOBILE_REGISTER_METHOD_RESULT)
						.toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return response;
	}

	// Get projects for mobile.
	public String getProjectsForMobile(User user) {
		String response = null;
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_PROJECT_FOR_MOBILE);

			request.addProperty("sIMEINo", user.getImeiNo());
			request.addProperty("sSimMobileNo", user.getMobileNo());
			request.addProperty("sUserName", user.getUserName());
			request.addProperty("sHashPasswd", user.getHashPassword());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
				androidHttpTransport.call(WebserviceConstants.NAMESPACE
						+ WebserviceConstants.GET_PROJECT_FOR_MOBILE, envelope);
		
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.GET_PROJECT_FOR_MOBILE_RESULT)
						.toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "Internetproblem";
			Toast.makeText(context, "Hellooooooo", Toast.LENGTH_LONG).show();
			Log.e("<<Exception>>", e.getMessage());
		}

		return response;
	}
	//GET STATUS WORKS
	
	public String getSurveyWorks(String Username){
		String response = "null";
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_SURVEYWORKS);
			request.addProperty("Userid", Username);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
				androidHttpTransport.call(WebserviceConstants.NAMESPACE
						+ WebserviceConstants.GET_SURVEYWORKS, envelope);
				if (envelope.bodyIn instanceof SoapObject) {
					SoapObject soapObject = (SoapObject) envelope.bodyIn;
					response = soapObject.getProperty(
							WebserviceConstants.GET_SURVEYWORKS_RESPONSE)
							.toString();
					
				}else if (envelope.bodyIn instanceof SoapFault) {
					@SuppressWarnings("unused")
					SoapFault soapFault = (SoapFault) envelope.bodyIn;
					response = "Failure";
				}
			} catch (Exception e) {
				e.printStackTrace();
				response = "Internetproblem";
				Toast.makeText(context, "Hellooooooo", Toast.LENGTH_LONG).show();
				Log.e("<<Exception>>", e.getMessage());
			}

			return response;
		}

	
			

	

	// Upload projects to server.
	public String uploadProjectData(Projectdata project) {
		String response = null;
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.UPLOAD_PROJECT_METHOD);

			request.addProperty("ProjectId", project.getProjectId());
			request.addProperty("ProjectName", project.getProjectName());
			request.addProperty("ProjectCategory", project.getProjectCategory());
			request.addProperty("HTStartPoint", project.getHTEndPoint());
			request.addProperty("HTEndPoint", project.getHTEndPoint());
			request.addProperty("LTStartPoint", project.getLTStartPoint()
					.toString());
			request.addProperty("LTEndPoint", project.getLTEndPoint());
			request.addProperty("NoOFCurves", project.getNoOFCurves());
			request.addProperty("Distance", project.getDistance());
			request.addProperty("EstimateId", project.getEstimateId());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.UPLOAD_PROJECT_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.UPLOAD_PROJECT_RESULT).toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return response;
	}

	// Update application
	public String updateApplication(int versionNo) {
		String response = null;
		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.VERSION_UPDATE);
			request.addProperty("iVersionId", versionNo);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.VERSION_UPDATE, envelope);
			if (envelope.bodyIn instanceof SoapObject) {
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				response = soapObject.getProperty(
						WebserviceConstants.VERSION_UPDATE_RESULT).toString();
			} else if (envelope.bodyIn instanceof SoapFault) {
				@SuppressWarnings("unused")
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				response = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("<<Exception>>", e.getMessage());
		}
		return response;
	}

	public String getWorkFinancialDetails(String pWorkRowId) {
		String strResponse = null;
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
			.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_WORK_FINANCIAL_METHOD);
			request.addProperty("WorkRowid", pWorkRowId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransport.debug = true;
			// doCorrectStuffThatWritesToDisk();
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_WORK_FINANCIAL_METHOD, envelope);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS

				strResponse = envelope.getResponse().toString();
				StrictMode.setThreadPolicy(old);
				System.out.println("-------"+strResponse);


			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
				// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				throw new XmlPullParserException(soapFault.getMessage());
				

			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			strResponse = "ServerTimeOut";
		}catch (Exception e) {
			strResponse = "noInternet";
			e.printStackTrace();

			// Log.e("<<Exception>>",e.getMessage());
		}
		return strResponse;

	}
	
	public String getSurveyCoordinates(int pWorkRowId) {
		String strResponse = null;
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
			.permitAll().build());SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_SURVEYCOORDINATES);
			request.addProperty("projectid", pWorkRowId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL
							+ WebserviceConstants.GESCOM_SERVICES_PORT);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_SURVEYCOORDINATES, envelope);
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS

				strResponse = envelope.getResponse().toString();
				StrictMode.setThreadPolicy(old);
				System.out.println("-------"+strResponse);
			}else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
				// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				throw new XmlPullParserException(soapFault.getMessage());
				

			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			strResponse = "ServerTimeOut";
		}catch (Exception e) {
			strResponse = "noInternet";
			e.printStackTrace();

			// Log.e("<<Exception>>",e.getMessage());
		}
		return strResponse;

	}
	public String getTaskDetails(String pLoginId){
		String strResponse = null;

		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
			.permitAll().build());

			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.GET_TASK_DETAILS_METHOD);
			request.addProperty("sUserId",pLoginId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.GET_TASK_DETAILS_METHOD, envelope);


			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				strResponse = envelope.getResponse().toString();
				StrictMode.setThreadPolicy(old);
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
				// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				strResponse = "ServerTimeOut";
				throw new Exception(soapFault.getMessage());

			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			strResponse = "ServerTimeOut";
		}catch (Exception e) {
			strResponse = "noInternet";
			e.printStackTrace();

			// Log.e("<<Exception>>",e.getMessage());
		}

		return strResponse;
	}
	public String Imagestoserver(ApprovedPhotoCoordinates approvedphotocoordinates){
		String strresponse = null;
		try{
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			String imageData = "";
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,WebserviceConstants.UPLOAD_IMAGE_TOSERVER);
			request.addProperty("ImageData",approvedphotocoordinates.getImageData());
			request.addProperty("Projectid",approvedphotocoordinates.getWorkRowId());
			request.addProperty("Estimationid",approvedphotocoordinates.getEstimationId());
			request.addProperty("Loginid",approvedphotocoordinates.getCapturedBy());
			request.addProperty("poleid",approvedphotocoordinates.getCoordinatesId());
			request.addProperty("PhotoCaption",approvedphotocoordinates.getPhotoCaption());
			request.addProperty("Latitude",approvedphotocoordinates.getLatitude());
			request.addProperty("Longitude",approvedphotocoordinates.getLongitude());
			request.addProperty("sCaptionDate",approvedphotocoordinates.getCapturedOn());
			request.addProperty("ltEstId",approvedphotocoordinates.getLtEstId());
			request.addProperty("TypeOfLine",approvedphotocoordinates.getTypeOfLine());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransportSE = new HttpTransportSE(WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransportSE.debug=true;
			androidHttpTransportSE.call(WebserviceConstants.NAMESPACE +WebserviceConstants.UPLOAD_IMAGE_TOSERVER, envelope);
			if(envelope.bodyIn instanceof SoapObject){
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				strresponse = androidHttpTransportSE.responseDump;
				if(strresponse.contains("Sucess")){
					strresponse = "success";
				}
 				StrictMode.setThreadPolicy(old);
				}else if (envelope.bodyIn instanceof SoapFault){
					SoapFault soapFault = (SoapFault) envelope.bodyIn;
					strresponse = "failure";
					throw new Exception(soapFault.getMessage());
				}
		}catch (Exception e) {
			strresponse = "failure";
			e.printStackTrace();
		}
		
		return strresponse;
		
		
		
		
	} 
	public String uploadWorkImages(ApprovedPhotoCoordinates approvedphotocoordinates){
		String strresponse = null;
		try{
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			String imageData = "";
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,WebserviceConstants.ADD_WORK_STATUS);
			request.addProperty("ImageData",approvedphotocoordinates.getFileName()+"@"+approvedphotocoordinates.getLatitude()+"@"+approvedphotocoordinates.getLongitude()+"@"+approvedphotocoordinates.getImageData()+"!");
			request.addProperty("WorkItem",approvedphotocoordinates.getWorkRowId());
			request.addProperty("SubWorkItem",1);
			request.addProperty("WorkStatus",approvedphotocoordinates.getWorkStarus());
			request.addProperty("PhotoCaption",approvedphotocoordinates.getPhotoCaption());
			request.addProperty("Latitude",approvedphotocoordinates.getLatitude());
			request.addProperty("Longitude",approvedphotocoordinates.getLongitude());
			request.addProperty("StartPoint","");
			request.addProperty("EndPoint","");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransportSE = new HttpTransportSE(WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransportSE.debug=true;
			androidHttpTransportSE.call(WebserviceConstants.NAMESPACE +WebserviceConstants.ADD_WORK_STATUS, envelope);
			if(envelope.bodyIn instanceof SoapObject){
				SoapObject soapobject = (SoapObject) envelope.bodyIn;
				strresponse = androidHttpTransportSE.responseDump;
				if(strresponse.contains("Sucess")){
					strresponse = "success";
				}
				StrictMode.setThreadPolicy(old);
				}else if (envelope.bodyIn instanceof SoapFault){
					SoapFault soapFault = (SoapFault) envelope.bodyIn;
					throw new Exception(soapFault.getMessage());
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return strresponse;
		
		
		
		
	} 
	// upload coordinates to server
	public String coordinatestoserver(ApprovedCoordinates approvedcoordinates){
		String strresponse = null;
		try{
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,WebserviceConstants.UPLOAD_COORDINATES_TOSERVER);
			request.addProperty("Projectid",approvedcoordinates.getWorkRowId());
			request.addProperty("Estimationid",approvedcoordinates.getEstimationid());
			request.addProperty("Latitude",approvedcoordinates.getLatitude());
			request.addProperty("Longitude",approvedcoordinates.getLongitude());
			request.addProperty("Loginid",approvedcoordinates.getCapturedBy());
			request.addProperty("ipoleid",approvedcoordinates.getCoordinatesId());
			request.addProperty("sCaption",approvedcoordinates.getsCaption());
			request.addProperty("iPoleType",approvedcoordinates.getiPoleType());
			request.addProperty("ltEstId",approvedcoordinates.getLtEstId());
			request.addProperty("TypeOfLine",approvedcoordinates.getTypeOfLine());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransportSE = new HttpTransportSE(WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransportSE.debug=true;
			androidHttpTransportSE.call(WebserviceConstants.NAMESPACE+WebserviceConstants.UPLOAD_COORDINATES_TOSERVER, envelope);
			if(envelope.bodyIn instanceof SoapObject){
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				strresponse = androidHttpTransportSE.responseDump;
				if(strresponse.contains("Sucess")){
					strresponse = "success";
				}
				/*strresponse = soapObject
						.getProperty(
								WebserviceConstants.UPLOAD_COORDINATES_TOSERVER_RESPONSE)
						.toString();*/
				StrictMode.setThreadPolicy(old);
				}else if (envelope.bodyIn instanceof SoapFault){
					SoapFault soapFault = (SoapFault) envelope.bodyIn;
					strresponse = "failure";
					throw new Exception(soapFault.getMessage());
				}
		}catch (Exception e) {
			strresponse = "failure";
			e.printStackTrace();
		}
		
		return strresponse;
		
	}
	

		// Get Estimation for Measurement.
			public List<MeasurementEstimation> getEstimateForMeasurement(String estimatationid,
					String projectid) {
				String response = null;
				List<MeasurementEstimation> lstmeasureest = new ArrayList<MeasurementEstimation>();
				try {
					StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
					StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
							.permitAll().build());
					SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
							WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_METHOD);
					request.addProperty("projectid", projectid);
					request.addProperty("Estimationid", estimatationid);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							WebserviceConstants.HOST_URL
									+ WebserviceConstants.GESCOM_SERVICES_PORT);
					androidHttpTransport.call(WebserviceConstants.NAMESPACE
							+ WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_METHOD,
							envelope);
					if (envelope.bodyIn instanceof SoapObject) {
						SoapObject soapObject = (SoapObject) envelope.bodyIn;
						response = soapObject
								.getProperty(
										WebserviceConstants.GET_ESTIMATION_FOR_MESUREMENT_RESULT)
								.toString();
						System.out.println("------"+response);
						Parser parser = new Parser(context);
						lstmeasureest = parser.getmeasurementestimationlist(response);
						
					} else if (envelope.bodyIn instanceof SoapFault) {
						@SuppressWarnings("unused")
						SoapFault soapFault = (SoapFault) envelope.bodyIn;
						response = "Failure";
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					Log.e("<<Exception>>", e.getMessage());
				}
				return lstmeasureest;
			}

	public String updateWorkFinance(int workRowId, String sDetails) {
		String strResponse = "";
		
		try {
			StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
					.permitAll().build());
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.UPLOAD_FINANCIAL_DETAILS_METHOD);
			request.addProperty("WorkRowid", workRowId);
			request.addProperty("sDetails",sDetails);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			androidHttpTransport.debug = true;
			
			androidHttpTransport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.UPLOAD_FINANCIAL_DETAILS_METHOD, envelope);
			
			
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
//				SoapObject soapObject = (SoapObject) envelope.bodyIn;
 
//				strResponse = androidHttpTransport.responseDump;
//				strResponse = strResponse.replaceAll("&lt;", "<");
//				strResponse = strResponse.replaceAll("&gt;", ">");
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				strResponse = "Success";
				System.out.println("-------FromServer-----" + strResponse);

				StrictMode.setThreadPolicy(old);
				
			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
				// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				throw new Exception(soapFault.getMessage());

			}
		} catch (Exception e) {
			e.printStackTrace();
			// Log.e("<<Exception>>",e.getMessage());
		}
		return strResponse;
		
	}

	public String updateTaskDetails(String sTaskDetails) {
		// TODO Auto-generated method stub
		String response = new String();

		try {
			SoapObject request = new SoapObject(WebserviceConstants.NAMESPACE,
					WebserviceConstants.UPDATE_TASK_DETAILS);
			request.addProperty("sTaskDetails", sTaskDetails);
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			soapEnvelope.dotNet = true;
			soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
			soapEnvelope.setOutputSoapObject(request);

			// soapEnvelope.addMapping("http://tempuri.org/", "Pojo", new
			// Test().getClass());

			HttpTransportSE transport = new HttpTransportSE(
					WebserviceConstants.HOST_URL_FOR_WORKITEMSBO);
			transport.debug = true;
			transport.call(WebserviceConstants.NAMESPACE
					+ WebserviceConstants.UPDATE_TASK_DETAILS, soapEnvelope);

			if (soapEnvelope.bodyIn instanceof SoapObject) { // SoapObject =
																// SUCCESS
				SoapObject soapObject = (SoapObject) soapEnvelope.bodyIn;
				response = "Success";
				Log.i("response", response);
			} else if (soapEnvelope.bodyIn instanceof SoapFault) { // SoapFault
																	// =
				// FAILURE
				SoapFault soapFault = (SoapFault) soapEnvelope.bodyIn;
				response = "Failure";
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			response = "doubleID";
			return response;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			response = "ServerTimeOut";
			return response;
		} catch (Exception e) {

			e.printStackTrace();
			response = "noInternet";
//			Log.e("<<Exception>>", e.getMessage());
			return response;
		}
		return response;
	}
	
}
