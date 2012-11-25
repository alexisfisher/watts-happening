package com.wattshappening.db;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {
	
	private static DBManager instance = null;

	private static final String DB_NAME = "wh_log.db";
	private static final int DB_VERSION = 1;
	
	Vector<DBTable> tables = new Vector<DBTable>();
	
	protected DBManager(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		
		tables.add(new HardwareTable(context));
		tables.add(new GPSTable(context));
		tables.add(new AppInfoTable(context));
		tables.add(new BatteryTable(context));
		tables.add(new NetworkTable(context));
	}
	
	public static DBManager getInstance(Context context){
		if(context == null && instance == null){
			System.err.println("CAN'T MAKE DB WITH NULL CONTEXT");
		}
		if (instance == null)
			instance = new DBManager(context);
		
		return instance;
	}
	
	@Override 
	public void onCreate(SQLiteDatabase database){
		for (int i = 0; i<tables.size(); ++i)
			database.execSQL(tables.get(i).getCreationQuerry());
	}
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println("Upgrading database from version " + oldVersion +
				" to " + newVersion + ", ALL CURRENT DATA WILL BE LOST!");
		
		for (int i = 0; i<tables.size(); ++i)
			db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName() + ";");
		
		onCreate(db);
	}	
	
	public void dropTables(SQLiteDatabase db){
		for (int i = 0; i< tables.size(); ++i){
			db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName() + ";");
		}
	}
	
	public Vector<AppInfoBat> getAppInfo(String name, int slices){
		Vector<AppInfoBat> results = new Vector<AppInfoBat>();
		String getTimestampID = "SELECT MAX(timestamp_id) FROM " + AppInfoTable.TABLE_APPINFO +
				" where name='" + name + "'";
		int maxTimestamp = -1;
		Cursor cursor = instance.getReadableDatabase().rawQuery(getTimestampID, null);
		
		if(cursor.moveToFirst()){
			maxTimestamp = cursor.getInt(0);
			Log.i("DBManager", "MaxTimestamp: " + maxTimestamp);
		}
		else {
			Log.e("DBManager", "COULD NOT GET MAX TIMESTAMP ID");
			return null;
		}
		
		String sqlQuery = "SELECT * FROM" + AppInfoTable.TABLE_APPINFO + " join " + 
				BatteryTable.TABLE_BATTERY + " on " + AppInfoTable.COLUMN_APP_TIMESLICE + 
				" where name='" + name + "' and " + AppInfoTable.COLUMN_APP_TIMESLICE + " > " +
				(maxTimestamp - slices) + ";";
		cursor = instance.getReadableDatabase().rawQuery(sqlQuery, null);
		
		if(cursor.moveToFirst()){
			// cycle through and make all the AppInfoBat objects
			for(int i = 0; i < cursor.getColumnCount(); i++){
				Log.i("DBManager", cursor.getColumnName(i));
			}
		}
		else {
			Log.e("DBManager", "NO ROWS RETURNED FOR getAppInfo");
			return null;
		}
		
		return results;
	}
}

