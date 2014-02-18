package com.zeal.gov.kar.gescom.session;

public class Appuser {

	private static String imeiNo;
	private static String userName;
	private static String password;
	private static String spnDescForEditEst;

	public static String getImeiNo() {
		return imeiNo;
	}

	public static void setImeiNo(String imeiNo) {
		Appuser.imeiNo = imeiNo;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		Appuser.userName = userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Appuser.password = password;
	}

	public static String getSpnDescForEditEst() {
		return spnDescForEditEst;
	}

	public static void setSpnDescForEditEst(String spnDescForEditEst) {
		Appuser.spnDescForEditEst = spnDescForEditEst;
	}
}
