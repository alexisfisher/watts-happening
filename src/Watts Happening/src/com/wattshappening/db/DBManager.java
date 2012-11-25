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
		tables.add(new GeneralInfoTable(context));
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
		String getTimestampID = "SELECT MAX(" + AppInfoTable.COLUMN_APP_TIMESLICE + ") FROM "
				+ AppInfoTable.TABLE_APPINFO +
				" where name='" + name + "'";
		int maxTimeslice = -1;
		Cursor cursor = instance.getReadableDatabase().rawQuery(getTimestampID, null);
		
		if(cursor.moveToFirst()){
			maxTimeslice = cursor.getInt(0);
		}
		else {
			Log.e("DBManager", "COULD NOT GET MAX TIMESTAMP ID");
			return null;
		}
		
		int startTimeslice = maxTimeslice - slices;
		// If there isn't enough data for the request, then return all we have
		if(startTimeslice < 1){
			startTimeslice = 0;
		}
		
		String sqlQuery = "SELECT DISTINCT " + 
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_TIMESLICE + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_NAME + ", " + 
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_ID + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_CPU + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_RX_BYTES + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_TX_BYTES + ", " +
				BatteryTable.TABLE_BATTERY + "." + BatteryTable.COLUMN_BATTERY_PERCENTAGE +
				" FROM " + AppInfoTable.TABLE_APPINFO + ", " + 
				BatteryTable.TABLE_BATTERY + " where " + 
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_TIMESLICE + ">" + startTimeslice + 
				" and " + BatteryTable.TABLE_BATTERY + "." + BatteryTable.COLUMN_TIMESLICE_ID + ">" + startTimeslice +
				" and " + AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_NAME + "='" + name + "';";

		cursor = instance.getReadableDatabase().rawQuery(sqlQuery, null);
		
		if(cursor.moveToFirst()){
			do {
				results.add(new AppInfoBat(
						cursor.getInt(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_TIMESLICE)),
						cursor.getString(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_NAME)),
						cursor.getInt(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_ID)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_CPU)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_RX_BYTES)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_TX_BYTES)),
						cursor.getDouble(cursor.getColumnIndex(BatteryTable.COLUMN_BATTERY_PERCENTAGE))
						));
			}while(cursor.moveToNext());
		}
		else {
			Log.e("DBManager", "NO ROWS RETURNED FOR getAppInfo");
			return null;
		}
	
		// print out the result set as AppInfoBat objects
/*		for(int i = 0; i < results.size(); i++){
			Log.i("DBManager", results.get(i).toString());
		}
*/		
		return results;
	}
}

