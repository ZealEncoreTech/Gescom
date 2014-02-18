package com.zeal.gov.kar.gescom.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.zeal.gov.kar.gescom.database.BaseService;
import com.zeal.gov.kar.gescom.model.Logininfo;
import com.zeal.gov.kar.gescom.model.MainTaskDetailsWork;
import com.zeal.gov.kar.gescom.model.Masteruserroles;
import com.zeal.gov.kar.gescom.model.MeasurementEstimation;
import com.zeal.gov.kar.gescom.model.ProjectEstimationType;
import com.zeal.gov.kar.gescom.model.Projectestimates;
import com.zeal.gov.kar.gescom.model.SubTaskDetailsWork;
import com.zeal.gov.kar.gescom.model.Task;
import com.zeal.gov.kar.gescom.model.TaskDetails;
import com.zeal.gov.kar.gescom.model.TaskDetailsLists;
import com.zeal.gov.kar.gescom.model.WorkFinancialDetails;
import com.zeal.gov.kar.gescom.model.WorkItems;
import com.zeal.gov.kar.gescom.model.Workmain;
import com.zeal.gov.kar.gescom.session.Appuser;

public class Parser {
	public int newWorks;
	private Context context;
	public Parser(Context pContext){
		this.context = pContext;
	}
 
 public String parseResponse(String sResponse)
 {
	 String result=null;
	 try {
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(new StringReader(sResponse)));
		//	doc.getDocumentElement().normalize();
			BaseService baseService=new BaseService(context);
 
			NodeList nodeList = doc.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				Logininfo newUser= new Logininfo();
				Node node = nodeList.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);
					if (temp.getNodeName().equalsIgnoreCase("activated")) {
						System.out.println(temp.getTextContent());
						newUser.setActivated(temp.getTextContent());
						result="Activated";
					}else if (temp.getNodeName().equalsIgnoreCase("Loginid")) {
						System.out.println(temp.getTextContent());
						Appuser.setUserName(temp.getTextContent());
						newUser.setLoginId(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("password")) {
						System.out.println(temp.getTextContent());
						Appuser.setPassword(temp.getTextContent());
						newUser.setPassWord(temp.getTextContent());
					} else if (temp.getNodeName().equalsIgnoreCase("MSG")) {
						System.out.println(temp.getTextContent());
						result="NotActivated";
						return result;
					}

				}
				// Adding new user to loginInfo Table.
				if(!baseService.isUserAvailable(Appuser.getUserName(), Appuser.getPassword()))
				{
				baseService.addTologininfo(newUser);
				}
			}
			if(result.equalsIgnoreCase("Activated"))
			{
				NodeList table1NodeList = doc.getElementsByTagName("Table1");
				for (int k = 0; k < table1NodeList.getLength(); k++) {
				  Masteruserroles role=new Masteruserroles();
					Node node = table1NodeList.item(k);
					for (int i = 0; i < node.getChildNodes().getLength(); i++) {
						Node temp = node.getChildNodes().item(i);
						if (temp.getNodeName().equalsIgnoreCase("loginId")) {
							System.out.println(temp.getTextContent());
							role.setLoginId(temp.getTextContent());
						} else if (temp.getNodeName().equalsIgnoreCase("RoleId")) {
							System.out.println(temp.getTextContent());
							role.setRoleId(temp.getTextContent());
						}

					}
					// Adding new row to Master user roles.
					if(!baseService.ismasteruserrolesAvailable(role.getLoginId()))
					{
					baseService.addTomasteruserroles(role);
					}
				}
				
				NodeList table2NodeList = doc.getElementsByTagName("Table2");
				for (int k = 0; k < table2NodeList.getLength(); k++) {
					Workmain workmainRow= new Workmain();
					Node node = table2NodeList.item(k);
					for (int i = 0; i < node.getChildNodes().getLength(); i++) {
						Node temp = node.getChildNodes().item(i);
						if (temp.getNodeName().equalsIgnoreCase("WorkRowid")) {
							System.out.println(temp.getTextContent());
							newWorks++;
							workmainRow.setWorkRowid(Integer.parseInt(temp.getTextContent()));
						} else if (temp.getNodeName().equalsIgnoreCase("WorkDescription")) {
							System.out.println(temp.getTextContent());
							workmainRow.setWorkDescription(temp.getTextContent());
						}else if (temp.getNodeName().equalsIgnoreCase("NoofHT")) {
							System.out.println(temp.getTextContent());
							workmainRow.setNoofHT(Integer.parseInt(temp.getTextContent()));
						}else if (temp.getNodeName().equalsIgnoreCase("NoofLT")) {
							System.out.println(temp.getTextContent());
							workmainRow.setNoofLT(Integer.parseInt(temp.getTextContent()));
						}else if (temp.getNodeName().equalsIgnoreCase("Date")) {
							System.out.println(temp.getTextContent());
							workmainRow.setDates(temp.getTextContent());
						}else if (temp.getNodeName().equalsIgnoreCase("Reason")) {
							System.out.println(temp.getTextContent());
							workmainRow.setReason(temp.getTextContent());
						}else if (temp.getNodeName().equalsIgnoreCase("WorkStatusId")) {
							System.out.println(temp.getTextContent());
							workmainRow.setWorkStatusid(temp.getTextContent());
						}else if (temp.getNodeName().equalsIgnoreCase("ProjectCategory")) {
							System.out.println(temp.getTextContent());
							workmainRow.setProjectCategory(Integer.parseInt(temp.getTextContent()));
						}

					}
					    //Adding new row to workmain table.
						workmainRow.setSignature("not available");
					    workmainRow.setAssignedUser(Appuser.getUserName());
						baseService.addToworkmain(workmainRow);
					
				}
			}

		} catch (ParserConfigurationException e) {
			result="Error";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			result="Error";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			result="Error";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	return result;
 }
	
	public List<TaskDetails> getTaskDetailsList(String pResponse) {

		// TODO Auto-generated method stub
		List<TaskDetails> lstTaskDetails = new ArrayList<TaskDetails>();
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document document = docBuilder
					.parse(new InputSource(new StringReader(pResponse)));
//			Document document = docBuilder.parse(context.getAssets().open("taskdetails.xml"));
//			System.out.println(pResponse + "");
			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				TaskDetails taskDetails = new TaskDetails();
				Node node = nodeList.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("WORKROWID")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkRowId(Integer.parseInt(temp
								.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase("DESCRIPTION")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkDescription(temp.getTextContent());
					}

				}
				lstTaskDetails.add(taskDetails);

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstTaskDetails;
	}

	public List<TaskDetails> getSurveyWorks(String pResponse) {

		// TODO Auto-generated method stub
		List<TaskDetails> lstTaskDetails = new ArrayList<TaskDetails>();
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document document = docBuilder
					.parse(new InputSource(new StringReader(pResponse)));
//			Document document = docBuilder.parse(context.getAssets().open("taskdetails.xml"));
//			System.out.println(pResponse + "");
			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				TaskDetails taskDetails = new TaskDetails();
				Node node = nodeList.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("WorkRowId")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkRowId(Integer.parseInt(temp
								.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase("WorkDescription")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkDescription(temp.getTextContent());
					}

				}
				lstTaskDetails.add(taskDetails);

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstTaskDetails;
	}

	public List<WorkFinancialDetails> getWorkFinancialDetails(String pResponse) {
		// TODO Auto-generated method stub
		
		List<WorkFinancialDetails> lstWorkFinancialDetails = new ArrayList<WorkFinancialDetails>();
		try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder
				.parse(new InputSource(new StringReader(pResponse)));
		
		System.out.println(pResponse + "");
		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName("Table");
		for (int k = 0; k < nodeList.getLength(); k++) {
			WorkFinancialDetails financialDetails = new WorkFinancialDetails();
			Node node = nodeList.item(k);
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node temp = node.getChildNodes().item(i);

				if (temp.getNodeName().equalsIgnoreCase("DescNo")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDescNo(temp.getTextContent());

				} else if (temp.getNodeName().equalsIgnoreCase(
						"Description")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDescription(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"TotalUnits")) {
					System.out.println(temp.getTextContent());
					financialDetails.setTotalUnits(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"CostPerItem")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCostPerItem(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"EstimatedQuantity")) {
					System.out.println(temp.getTextContent());
					financialDetails.setEstimatedQuantity(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"SubWorkId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setSubworkId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"QuantityOver")) {
					System.out.println(temp.getTextContent());
					financialDetails.setQuantityOver(Double.parseDouble(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"UpdatedBy")) {
					System.out.println(temp.getTextContent());
					financialDetails.setUpdatedBy(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"AsOnToday")) {
					System.out.println(temp.getTextContent());
					financialDetails.setAsOnToday(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"TotalCost")) {
					System.out.println(temp.getTextContent());
					financialDetails.setTotalCost(Double.parseDouble(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CostOver")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCostOver(Double.parseDouble(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"TUnits")) {
					System.out.println(temp.getTextContent());
					financialDetails.settUnits(Double.parseDouble(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CUnits")) {
					System.out.println(temp.getTextContent());
					financialDetails.setcUnits(Double.parseDouble(temp.getTextContent()));
				}
				else if(temp.getNodeName().equalsIgnoreCase("BlockId")){
					financialDetails.setBlockId(temp.getTextContent());
				}
				else if(temp.getNodeName().equalsIgnoreCase("MeasurementId")){
					financialDetails.setMeasurementId(Integer.valueOf(temp.getTextContent()));
				}

			}
			lstWorkFinancialDetails.add(financialDetails);

		}

	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return lstWorkFinancialDetails;
	}

	public HashMap<String, Object> getSurveyCoordinatesData(String pResponse) {
		// TODO Auto-generated method stub
		HashMap<String,Object> hm = new HashMap<String, Object>();
		
		List<Projectestimates> lstWorkFinancialDetails = new ArrayList<Projectestimates>();
		List<ProjectEstimationType> lstProjectEstimationTypes = new ArrayList<ProjectEstimationType>();
		try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder
				.parse(new InputSource(new StringReader(pResponse)));
		
		System.out.println(pResponse + "");
		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName("Table");
		for (int k = 0; k < nodeList.getLength(); k++) {
			Projectestimates financialDetails = new Projectestimates();
			Node node = nodeList.item(k);
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node temp = node.getChildNodes().item(i);

				if (temp.getNodeName().equalsIgnoreCase("StartLattitude")) {
					System.out.println(temp.getTextContent());
					financialDetails.setStartLattitude(temp.getTextContent());

				} else if (temp.getNodeName().equalsIgnoreCase(
						"StartLangtitude")) {
					System.out.println(temp.getTextContent());
					financialDetails.setStartLangtitude(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"Distance")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDistance(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"NoofCurves")) {
					System.out.println(temp.getTextContent());
					financialDetails.setNoofCurves(Integer.parseInt(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"ReceivedFromMobileDate")) {
					System.out.println(temp.getTextContent());
					financialDetails.setReceivedFromMobileDate(temp.getTextContent());
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"WorkRowid")) {
					System.out.println(temp.getTextContent());
					financialDetails.setWorkRowId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"EstimationId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setEstimationId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"TypeofLine")) {
					System.out.println(temp.getTextContent());
					financialDetails.setTypeOfLine(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CoordinatesId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCoordinatesId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CoordinatesCaption")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCoordinatesCaption(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"ltEstId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setLtEstId(Integer.parseInt(temp.getTextContent()));
				}
				
				
				

			}
			lstWorkFinancialDetails.add(financialDetails);
		}
			NodeList nodeList1 = doc.getElementsByTagName("Table1");
			for (int k1 = 0; k1 < nodeList1.getLength(); k1++) {
				ProjectEstimationType estimationType = new ProjectEstimationType();
				Node node1 = nodeList1.item(k1);
				for (int i = 0; i < node1.getChildNodes().getLength(); i++) {
					Node temp = node1.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("ProjectId")) {
						System.out.println(temp.getTextContent());
						estimationType.setProjectId(Integer.parseInt(temp.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase(
							"EstimationId")) {
						System.out.println(temp.getTextContent());
						estimationType.setEstimationId(Integer.parseInt(temp.getTextContent()));
					}
					
					else if (temp.getNodeName().equalsIgnoreCase(
							"EstDescription")) {
						System.out.println(temp.getTextContent());
						estimationType.setEstDescription(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"LineTypeId")) {
						System.out.println(temp.getTextContent());
						estimationType.setLineId(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"ltEstId")) {
						System.out.println(temp.getTextContent());
						estimationType.setLtEstId(Integer.parseInt(temp.getTextContent()));
					}
					
					
					

				}
				lstProjectEstimationTypes.add(estimationType);
			}

		
		}catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		hm.put("lst1", lstWorkFinancialDetails);
		hm.put("lst2", lstProjectEstimationTypes);
	return hm;
	}
	public List<WorkFinancialDetails> getWorkFinancialDetails() {
		// TODO Auto-generated method stub
		
		List<WorkFinancialDetails> lstWorkFinancialDetails = new ArrayList<WorkFinancialDetails>();
		try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbf.newDocumentBuilder();
		/*Document doc = docBuilder
				.parse(new InputSource(new StringReader(pResponse)));
		
		System.out.println(pResponse + "");
		doc.getDocumentElement().normalize();*/
		Document document = docBuilder.parse(context.getAssets().open("workfinancial.xml"));
//		System.out.println(pResponse + "");
		document.getDocumentElement().normalize();


		NodeList nodeList = document.getElementsByTagName("Table");
		for (int k = 0; k < nodeList.getLength(); k++) {
			WorkFinancialDetails financialDetails = new WorkFinancialDetails();
			Node node = nodeList.item(k);
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node temp = node.getChildNodes().item(i);

				if (temp.getNodeName().equalsIgnoreCase("DescNo")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDescNo(temp.getTextContent());

				} else if (temp.getNodeName().equalsIgnoreCase(
						"Description")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDescription(temp.getTextContent());
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"TotalUnits")) {
					System.out.println(temp.getTextContent());
					financialDetails.setTotalUnits(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"CostPerItem")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCostPerItem(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"EstimatedQuantity")) {
					System.out.println(temp.getTextContent());
					financialDetails.setEstimatedQuantity(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"CostOver")) {
					System.out.println(temp.getTextContent());
					financialDetails.setQuantityOver(Double.parseDouble(temp.getTextContent()));
				}

			}
			lstWorkFinancialDetails.add(financialDetails);

		}

	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return lstWorkFinancialDetails;
	}
	public List<TaskDetails> getworkstatus(String pResponse) {
		// TODO Auto-generated method stub

		List<TaskDetails> lstTaskDetails = new ArrayList<TaskDetails>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder
					.parse(new InputSource(new StringReader(pResponse)));

			System.out.println(pResponse + "");
			doc.getDocumentElement().normalize();
			//			Document document = docBuilder.parse(context.getAssets().open("workfinancial.xml"));
			//		System.out.println(pResponse + "");
			doc.getDocumentElement().normalize();


			NodeList nodeList = doc.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				TaskDetails taskDetails = new TaskDetails();
				Node node = nodeList.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("WORKROWID")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkRowId(Integer.parseInt(temp
								.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase("WorkDescription")) {
						System.out.println(temp.getTextContent());
						taskDetails.setWorkDescription(temp.getTextContent());
					}

				}
				lstTaskDetails.add(taskDetails);

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstTaskDetails;
	}
	public List<MeasurementEstimation> getmeasurementestimationlist(String pResponse) {
		// TODO Auto-generated method stub
		MeasurementEstimation measureest ;
		List<MeasurementEstimation> lstMeasurementEstimation = new ArrayList<MeasurementEstimation>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder
					.parse(new InputSource(new StringReader(pResponse)));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				measureest = new MeasurementEstimation();

				Node node = nodeList.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("WorkRowId")) {
						System.out.println(temp.getTextContent());
						measureest.setWorkRowId(Integer.parseInt(temp.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase(
							"EstimationId")) {
						System.out.println(temp.getTextContent());
						measureest.setEstimationId(Integer.parseInt(temp.getTextContent()));
					}

					else if (temp.getNodeName().equalsIgnoreCase(
							"SubWorkId")) {
						System.out.println(temp.getTextContent());
						measureest.setSubWorkID(Integer.parseInt((temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"workitemtypeid")) {
						System.out.println(temp.getTextContent());
						measureest.setWorkItemTypeId(Integer.parseInt(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"CostPerItem")) {
						System.out.println(temp.getTextContent());
						measureest.setCostPerItem((Double.parseDouble(temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"MeasurementId")) {
						System.out.println(temp.getTextContent());
						measureest.setMeasurementId((Integer.parseInt(temp.getTextContent())));
					}

					else if (temp.getNodeName().equalsIgnoreCase(
							"TotalUnits")) {
						System.out.println(temp.getTextContent());
						measureest.setTotalUnits((Double.parseDouble(temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"TotalAmount")) {
						System.out.println(temp.getTextContent());
						measureest.setTotalAmount((Double.parseDouble(temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"measurmentquantity")) {
						System.out.println(temp.getTextContent());
						measureest.setMeasurementQuantity((Double.parseDouble(temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"TotalAmount")) {
						System.out.println(temp.getTextContent());
						measureest.setTotalAmount((Double.parseDouble(temp.getTextContent())));
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"mtotalamount")) {
						System.out.println(temp.getTextContent());
						measureest.setmTotalAmount((Double.parseDouble(temp.getTextContent())));
					}

				}
				lstMeasurementEstimation.add(measureest);

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstMeasurementEstimation;
	}
	public TaskDetailsLists parseGetTaskDetails(String result) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		List<MainTaskDetailsWork> lstMainTaskDetailsWorks = new ArrayList<MainTaskDetailsWork>();
		List<SubTaskDetailsWork> lstSubTaskDetailsWorks = new ArrayList<SubTaskDetailsWork>();
		List<Task> lstTasks = new ArrayList<Task>();
		TaskDetailsLists lists = new TaskDetailsLists();

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(result)));
//			AssetManager assetManager = context.getAssets();

//			Document doc = db.parse(assetManager.open("taskdetails.xml"));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("Table");
			for (int k = 0; k < nodeList.getLength(); k++) {
				MainTaskDetailsWork main = new MainTaskDetailsWork();
				Node node = nodeList.item(k);
				String string = node.getNodeName();
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);
					String string2 = temp.getNodeName();

					if (temp.getNodeName().equalsIgnoreCase("WORKROWID")) {
						Log.i("WORKROWID", temp.getTextContent());
						main.setWorkRowId(Integer.parseInt(temp
								.getTextContent().trim()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("DESCRIPTION")) {
						Log.i("DESCRIPTION", temp.getTextContent());
						main.setWorkDescription(temp.getTextContent());
					}

				}
				lstMainTaskDetailsWorks.add(main);
			}

			NodeList nodeList1 = doc.getElementsByTagName("Table1");
			for (int k = 0; k < nodeList1.getLength(); k++) {
				SubTaskDetailsWork subWork = new SubTaskDetailsWork();
				Node node = nodeList1.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("WORKROWID")) {
						Log.i("WORKROWID", temp.getTextContent());
						subWork.setWorkRowId(Integer.parseInt(temp
								.getTextContent().trim()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("SUBWORKROWID")) {
						Log.i("SUBWORKROWID", temp.getTextContent());
						subWork.setSubWorkRowId(Integer.parseInt(temp
								.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("DESCRIPTION")) {
						Log.i("DESCRIPTION", temp.getTextContent());
						subWork.setDescription(temp.getTextContent());
					}

				}
				lstSubTaskDetailsWorks.add(subWork);
			}

			NodeList nodeList2 = doc.getElementsByTagName("Table2");
			for (int k = 0; k < nodeList2.getLength(); k++) {
				Task task = new Task();
				Node node = nodeList2.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("TASKID")) {
						Log.i("WORKROWID", temp.getTextContent());
						task.setTaskId(Integer.parseInt(temp
								.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("WORKROWID")) {
						Log.i("WORKROWID", temp.getTextContent());
						task.setWorkRowId(Integer.parseInt(temp
								.getTextContent().trim()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("SUBWORKROWID")) {
						Log.i("SUBWORKROWID", temp.getTextContent());
						task.setSubWorkRowId(Integer.parseInt(temp
								.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("TASK")) {
						Log.i("TASK", temp.getTextContent());
						task.setTask(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("PERCENTAGECOMPLETE")) {
						Log.i("PERCENTAGECOMPLETE", temp.getTextContent());
						task.setPercentageCompleted(Double.parseDouble(temp.getTextContent()));
					}

				}
				task.setStatus("");
				lstTasks.add(task);
			}

			lists.setLstMainTaskDetailsWorks(lstMainTaskDetailsWorks);
			lists.setLstSubTaskDetailsWorks(lstSubTaskDetailsWorks);
			lists.setLstTasks(lstTasks);


		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return lists;
	
	}
	
	//method for parsing works that are not present in our localdatabase.
	public HashMap<String, Object> parseResponseForWorkNotPresentInDb(String pResponse) {
		// TODO Auto-generated method stub
		HashMap<String,Object> hm = new HashMap<String, Object>();
		BaseService baseService = new BaseService(context);
		try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder
				.parse(new InputSource(new StringReader(pResponse)));
		
		System.out.println(pResponse + "");
		doc.getDocumentElement().normalize();

		NodeList tablePE = doc.getElementsByTagName("Table");
		for (int k = 0; k < tablePE.getLength(); k++) {
			Projectestimates financialDetails = new Projectestimates();
			Node node = tablePE.item(k);
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node temp = node.getChildNodes().item(i);

				if (temp.getNodeName().equalsIgnoreCase("StartLattitude")) {
					System.out.println(temp.getTextContent());
					financialDetails.setStartLattitude(temp.getTextContent());

				} else if (temp.getNodeName().equalsIgnoreCase(
						"StartLangtitude")) {
					System.out.println(temp.getTextContent());
					financialDetails.setStartLangtitude(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"Distance")) {
					System.out.println(temp.getTextContent());
					financialDetails.setDistance(Double.parseDouble(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"NoofCurves")) {
					System.out.println(temp.getTextContent());
					financialDetails.setNoofCurves(Integer.parseInt(temp.getTextContent()));
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"ReceivedFromMobileDate")) {
					System.out.println(temp.getTextContent());
					financialDetails.setReceivedFromMobileDate(temp.getTextContent());
				}
				else if (temp.getNodeName().equalsIgnoreCase(
						"WorkRowid")) {
					System.out.println(temp.getTextContent());
					financialDetails.setWorkRowId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"EstimationId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setEstimationId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"TypeofLine")) {
					System.out.println(temp.getTextContent());
					financialDetails.setTypeOfLine(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CoordinatesId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCoordinatesId(Integer.parseInt(temp.getTextContent()));
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"CoordinatesCaption")) {
					System.out.println(temp.getTextContent());
					financialDetails.setCoordinatesCaption(temp.getTextContent());
				}
				
				else if (temp.getNodeName().equalsIgnoreCase(
						"ltEstId")) {
					System.out.println(temp.getTextContent());
					financialDetails.setLtEstId(Integer.parseInt(temp.getTextContent()));
				}
			}
			baseService.addToprojectestimation(financialDetails);
//			lstWorkFinancialDetails.add(financialDetails);
			
		}
			NodeList tablePET = doc.getElementsByTagName("Table1");
			for (int k1 = 0; k1 < tablePET.getLength(); k1++) {
				ProjectEstimationType estimationType = new ProjectEstimationType();
				Node node1 = tablePET.item(k1);
				for (int i = 0; i < node1.getChildNodes().getLength(); i++) {
					Node temp = node1.getChildNodes().item(i);

					if (temp.getNodeName().equalsIgnoreCase("ProjectId")) {
						System.out.println(temp.getTextContent());
						estimationType.setProjectId(Integer.parseInt(temp.getTextContent()));

					} else if (temp.getNodeName().equalsIgnoreCase(
							"EstimationId")) {
						System.out.println(temp.getTextContent());
						estimationType.setEstimationId(Integer.parseInt(temp.getTextContent()));
					}
					
					else if (temp.getNodeName().equalsIgnoreCase(
							"EstDescription")) {
						System.out.println(temp.getTextContent());
						estimationType.setEstDescription(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"LineTypeId")) {
						System.out.println(temp.getTextContent());
						estimationType.setLineId(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase(
							"ltEstId")) {
						System.out.println(temp.getTextContent());
						estimationType.setLtEstId(Integer.parseInt(temp.getTextContent()));
					}
				}
				baseService.addToProjectEstimationType(estimationType);
//				lstProjectEstimationTypes.add(estimationType);
			}
			
			NodeList tableForWorkmain = doc.getElementsByTagName("Table3");
			for (int k = 0; k < tableForWorkmain.getLength(); k++) {
				Workmain workmainRow= new Workmain();
				Node node = tableForWorkmain.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);
					if (temp.getNodeName().equalsIgnoreCase("WorkRowId")) {
						System.out.println(temp.getTextContent());
						workmainRow.setWorkRowid(Integer.parseInt(temp.getTextContent()));
					} else if (temp.getNodeName().equalsIgnoreCase("WorkDescription")) {
						System.out.println(temp.getTextContent());
						workmainRow.setWorkDescription(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("NoofHT")) {
						System.out.println(temp.getTextContent());
						workmainRow.setNoofHT(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("NoofLT")) {
						System.out.println(temp.getTextContent());
						workmainRow.setNoofLT(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("EnteredOn")) {
						System.out.println(temp.getTextContent());
						workmainRow.setDates(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("Reason")) {
						System.out.println(temp.getTextContent());
						workmainRow.setReason(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("WorkStatusId")) {
						System.out.println(temp.getTextContent());
						workmainRow.setWorkStatusid(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("ProjectCategory")) {
						System.out.println(temp.getTextContent());
						workmainRow.setProjectCategory(Integer.parseInt(temp.getTextContent()));
					}

				}
				    //Adding new row to workmain table.
					workmainRow.setSignature("not available");
				    workmainRow.setAssignedUser(Appuser.getUserName());
					baseService.addToworkmain(workmainRow);
				
			}

			NodeList tableForWorkitems = doc.getElementsByTagName("Table2");
			for (int k = 0; k < tableForWorkitems.getLength(); k++) {
				WorkItems wItems= new WorkItems();
				Node node = tableForWorkitems.item(k);
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					Node temp = node.getChildNodes().item(i);
					if (temp.getNodeName().equalsIgnoreCase("WorkRowId")) {
						wItems.setWorkRowid(Integer.parseInt(temp.getTextContent()));
					} else if (temp.getNodeName().equalsIgnoreCase("EstimationId")) {
						System.out.println(temp.getTextContent());
						wItems.setEstimationId(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("SubWorkId")) {
						System.out.println(temp.getTextContent());
						wItems.setSubWorkid(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("Category")) {
						System.out.println(temp.getTextContent());
						wItems.setCategory(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("SubCategory")) {
						System.out.println(temp.getTextContent());
						wItems.setSubCategory(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("ComponentId")) {
						System.out.println(temp.getTextContent());
						wItems.setComponentId(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("WorkItemTypeId")) {
						System.out.println(temp.getTextContent());
						wItems.setWorkItemTypeId(Integer.parseInt(temp.getTextContent()));
					}else if (temp.getNodeName().equalsIgnoreCase("WorkItemDescription")) {
						System.out.println(temp.getTextContent());
						wItems.setWorkItemDescription(temp.getTextContent());
					}else if (temp.getNodeName().equalsIgnoreCase("MeasurementId")) {
						System.out.println(temp.getTextContent());
						wItems.setMeasurementid(Integer.parseInt(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("TotalUnits")) {
						System.out.println(temp.getTextContent());
						wItems.setTotalUnits(Double.parseDouble(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("CostPerItem")) {
						System.out.println(temp.getTextContent());
						wItems.setCostPerItem(Double.parseDouble(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("SRRate")) {
						System.out.println(temp.getTextContent());
						wItems.setSrRate(Double.parseDouble(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("SRYear")) {
						System.out.println(temp.getTextContent());
						wItems.setSrYear(Integer.parseInt(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("BaseRate")) {
						System.out.println(temp.getTextContent());
						wItems.setBaseRate(Double.parseDouble(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("TotalAmount")) {
						System.out.println(temp.getTextContent());
						wItems.setTotalAmount(Double.parseDouble(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("BlockId")) {
						System.out.println(temp.getTextContent());
						wItems.setBlockId(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("BlockName")) {
						System.out.println(temp.getTextContent());
						wItems.setBlockName(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("GroupId")) {
						System.out.println(temp.getTextContent());
						wItems.setGroupId(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("GroupName")) {
						System.out.println(temp.getTextContent());
						wItems.setGroupName(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("Fixed")) {
						System.out.println(temp.getTextContent());
						wItems.setFixed(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("Constant")) {
						System.out.println(temp.getTextContent());
						wItems.setConstant(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("ConstantValue")) {
						System.out.println(temp.getTextContent());
						wItems.setConstantValue(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("Formula")) {
						System.out.println(temp.getTextContent());
						wItems.setFormula(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("AmountQuantity")) {
						System.out.println(temp.getTextContent());
						wItems.setAmountQuantity(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("ItemCode")) {
						System.out.println(temp.getTextContent());
						wItems.setItemCode(temp.getTextContent());
					}
					else if (temp.getNodeName().equalsIgnoreCase("DecRound")) {
						System.out.println(temp.getTextContent());
						wItems.setDecRound(Integer.parseInt(temp.getTextContent()));
					}
					else if (temp.getNodeName().equalsIgnoreCase("IsInteger")) {
						System.out.println(temp.getTextContent());
						wItems.setIsInteger(temp.getTextContent());
					}
					

				}
				    //Adding new row to workitems table.
					baseService.addToworkItems(wItems);
			}
		
		}catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return hm;
	}
	
}
