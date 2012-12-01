package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppInfoTable extends DBTable {
	
	public static final String TABLE_APPINFO = "app_info";
	public static final String COLUMN_APP_TIMESLICE = "timeslice_id";
	public static final String COLUMN_APP_NAME = "name";
	public static final String COLUMN_APP_ID = "app_id";
	public static final String COLUMN_APP_CPU = "cpu";
	public static final String COLUMN_RX_BYTES = "rx_bytes";
	public static final String COLUMN_TX_BYTES = "tx_bytes";
	public static final String COLUMN_ID = "id";
	
	
	public static final String CREATE_APP_TABLE = "create table " + 
			TABLE_APPINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_APP_TIMESLICE + " integer not null, " +
			COLUMN_APP_NAME + " text not null, " +
			COLUMN_APP_ID + " integer, " +
			COLUMN_APP_CPU + " real, " + 
			COLUMN_RX_BYTES + " real, " +
			COLUMN_TX_BYTES + " real);";
	
	@Override
	public void addEntry(DBEntry dbE) throws Exception{
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		if (!(dbE instanceof AppInfo))
			throw new Exception("Wrong type of entry, should be of type AppInfo");
		AppInfo appInfo = (AppInfo)dbE;
		
		values.put(COLUMN_APP_TIMESLICE, appInfo.getTimesliceID());
		values.put(COLUMN_APP_NAME, appInfo.getName());
		values.put(COLUMN_APP_ID, appInfo.getAppId());
		values.put(COLUMN_APP_CPU, Double.toString(appInfo.getCPU()));
		values.put(COLUMN_RX_BYTES, Double.toString(appInfo.getRXBytes()));
		values.put(COLUMN_TX_BYTES, Double.toString(appInfo.getTXBytes()));
		
		Log.i("AppTable", 
				"Timeslice: " + appInfo.getTimesliceID() + 
				" Name: " + appInfo.getName() +
				" ID: " + appInfo.getAppId() + 
				" CPU: " + appInfo.getCPU() + 
				" RX: " + appInfo.getRXBytes() + 
				" TX: " + appInfo.getTXBytes());
		
		db.insert(TABLE_APPINFO, null, values);
	}
	
	public AppInfoTable(Context context) {
		super(context, DBTable.FLUSH_ALL | DBTable.FLUSH_ONLY_NONPERSISTANT);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_APP_TABLE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_APPINFO;
	}
	
	@Override
	public List<DBEntry> fetchAllEntries(){
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		List<DBEntry> appInfo = new ArrayList<DBEntry>();
		String selectQuery = "SELECT " + 
									COLUMN_APP_TIMESLICE  + ", " + //0
									COLUMN_APP_NAME  + ", " +  //1
									COLUMN_APP_ID  + ", " +  //2
									COLUMN_APP_CPU  + ", " + //3
									COLUMN_RX_BYTES  + ", " + //4
									COLUMN_TX_BYTES  + //5
							 " FROM " + TABLE_APPINFO + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				AppInfo info = new AppInfo(cursor.getInt(0), cursor.getString(1), 
						cursor.getInt(2), cursor.getLong(3),  cursor.getLong(4),
						cursor.getLong(5));
				appInfo.add(info);
			}while(cursor.moveToNext());
		}
		return appInfo;
	}
	//TODO add fetchAppEntries() that fetches all entries of a single app
	

	
}
