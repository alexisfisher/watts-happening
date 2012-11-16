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
	public static final String COLUMN_APP_TIME = "timestamp";
	public static final String COLUMN_APP_NAME = "name";
	public static final String COLUMN_APP_ID = "app_id";
	public static final String COLUMN_APP_CPU = "cpu";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TIMESTAMP_ID = "time_stamp_id";
	
	
	public static final String CREATE_APP_TABLE = "create table " + 
			TABLE_APPINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TIMESTAMP_ID + " integer not null, " +
			COLUMN_APP_TIME + " text not null, " +
			COLUMN_APP_NAME + " text not null, " +
			COLUMN_APP_ID + " integer, " +
			COLUMN_APP_CPU + " real);";
	
	@Override
	public void addEntry(DBEntry dbE) throws Exception{
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		if (!(dbE instanceof AppInfo))
			throw new Exception("Wrong type of entry, should be of type Hardware");
		AppInfo appInfo = (AppInfo)dbE;
		
		values.put(COLUMN_TIMESTAMP_ID, appInfo.getTimestampID());
		values.put(COLUMN_APP_TIME, appInfo.getTimestamp());
		values.put(COLUMN_APP_NAME, appInfo.getName());
		values.put(COLUMN_APP_ID, appInfo.getAppId());
		values.put(COLUMN_APP_CPU, Double.toString(appInfo.getCPU()));
		
		Log.i("AppTable", 
				"Timestamp: " + appInfo.getTimestamp() + 
				" Name: " + appInfo.getName() +
				" ID: " + appInfo.getAppId() + 
				" CPU: " + appInfo.getCPU());
		
		db.insert(TABLE_APPINFO, null, values);
	}
	
	public AppInfoTable(Context context) {
		super(context);
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
		String selectQuery = "SELECT * FROM " + TABLE_APPINFO + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				AppInfo info = new AppInfo(cursor.getInt(1), cursor.getString(2), 
						cursor.getString(3), cursor.getInt(4),  cursor.getLong(5));
				appInfo.add(info);
			}while(cursor.moveToNext());
		}
		return appInfo;
	}
	
	public int getNextTimestampID()
	{
		SQLiteDatabase db = DBManager.getInstance(context).getReadableDatabase();
		String selectQuery = "SELECT MAX(" + COLUMN_TIMESTAMP_ID + ") FROM " + TABLE_APPINFO;
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (!cursor.moveToFirst()) //there are no entries yet
			return 0; 
		else
			return cursor.getInt(0) + 1;
		
	}
	
}
