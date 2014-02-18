package com.zeal.gov.kar.gescom.database;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "GescomDBHelper";
	private static DBHelper mInstance;
	private static Context context;

	public static DBHelper getInstance(Context context) {
		DBHelper.context = context;
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}

	private DBHelper(Context context) {
		super(context, DBConstants.DATABASE_NAME, null,
				DBConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG,"Inside DataBase Creator") ;
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			//Document doc = docBuilder.parse(context.getAssets().open("GescomDatBaseFile.xml"));//Old base rates. 
			Document doc = docBuilder.parse(context.getAssets().open("GescomDatBaseFile(updated).xml"));//latest base rates.
			NodeList items = doc.getElementsByTagName("schema");
			for (int i = 0; i < items.getLength(); ++i) {
				Element node = (Element) items.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String Schema = node.getTextContent().trim();
					Log.d(TAG,Schema) ;
					db.execSQL(Schema);
				}
			}

			NodeList inserts = doc.getElementsByTagName("insert");
			for (int i = 0; i < inserts.getLength(); ++i) {
				Element node = (Element) inserts.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String insert = node.getTextContent().trim();
					Log.d(TAG,insert) ;
					db.execSQL(insert);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP Table IF EXISTS users");
		onCreate(db);

	}
}
