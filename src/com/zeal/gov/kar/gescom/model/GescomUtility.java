package com.zeal.gov.kar.gescom.model;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GescomUtility {
	private static String imeiNo;
	private static String username;
	private static String pasword;
	private static int countForSpnSelected;
	private static int countForSpn;
	private static int countForSpnInEditEst;
	private static int tableCount;
	private static boolean isMeasurementUpdateToZero;
	private static HashMap<Integer, Double> hmQuantityOver;
	private static HashMap<Integer, HashMap<Integer, Double>> hmForMeasurementQuantityOver;
	

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		GescomUtility.username = username;
	}

	public static String getPasword() {
		return pasword;
	}

/*	public static String getHashPassword() {
		String hashPass = null;
		try {
			hashPass = generateHashPass(pasword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashPass;
	}*/

	public static void setPasword(String pasword) {

		GescomUtility.pasword = pasword;
	}

	public static String getImeiNo() {
		return imeiNo;
	}

	public static void setImeiNo(String imeiNo) {
		GescomUtility.imeiNo = imeiNo;
	}

/*
	private static String generateHashPass(String val)
			throws NoSuchAlgorithmException {

		byte[] strBytesData = new byte[val.length() + 2];
		byte[] strBytesHash = null;
		String strHash = null;

		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			strBytesData = stringToBytesASCII(val);// sha1.digest(input)
			strBytesHash = sha1.digest(strBytesData);
			strHash = new String(org.apache.commons.codec.binary.Base64.encodeBase64(strBytesHash));
			System.out.println(strHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strHash;

	}

	private static byte[] stringToBytesASCII(String str) {

		byte[] b = new byte[str.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) str.charAt(i);
		}
		return b;

	}

	private static String convert(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length);
		for (int i = 0; i < data.length; ++i) {
			if (data[i] < 0)
				throw new IllegalArgumentException();
			sb.append((char) data[i]);
		}
		return sb.toString();
	}
	
	
	public static String getBase64String(byte[] data) {
		String result = new String();
		if(data != null) {
			result = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
		}

		return result;
	}
*/
	public static Bitmap getBitmapFromByteArray(byte[] data) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = true;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory
				.decodeByteArray(data, 0, data.length, opt);
		return bitmap;
	}


	public static int getCountForSpn() {
		return countForSpn;
	}

	public static void setCountForSpn(int countForSpn) {
		GescomUtility.countForSpn = countForSpn;
	}

	public static int getCountForSpnSelected() {
		return countForSpnSelected;
	}

	public static void setCountForSpnSelected(int countForSpnSelected) {
		GescomUtility.countForSpnSelected = countForSpnSelected;
	}

	public static int getCountForSpnInEditEst() {
		return countForSpnInEditEst;
	}

	public static void setCountForSpnInEditEst(int countForSpnInEditEst) {
		GescomUtility.countForSpnInEditEst = countForSpnInEditEst;
	}

	public static int getTableCount() {
		return tableCount;
	}

	public static void setTableCount(int tableCount) {
		GescomUtility.tableCount = tableCount;
	}

	public static HashMap<Integer, Double> getHmQuantityOver() {
		return hmQuantityOver;
	}

	public static void setHmQuantityOver(HashMap<Integer, Double> hmQuantityOver) {
		GescomUtility.hmQuantityOver = hmQuantityOver;
	}

	public static boolean isMeasurementUpdateToZero() {
		return isMeasurementUpdateToZero;
	}

	public static void setMeasurementUpdateToZero(boolean isMeasurementUpdateToZero) {
		GescomUtility.isMeasurementUpdateToZero = isMeasurementUpdateToZero;
	}

	public static HashMap<Integer, HashMap<Integer, Double>> getHmForMeasurementQuantityOver() {
		return hmForMeasurementQuantityOver;
	}

	public static void setHmForMeasurementQuantityOver(
			HashMap<Integer, HashMap<Integer, Double>> hmForMeasurementQuantityOver) {
		GescomUtility.hmForMeasurementQuantityOver = hmForMeasurementQuantityOver;
	}

	
	
}
