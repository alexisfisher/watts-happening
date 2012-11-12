package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppInfoTable {

	public static final String TABLE_APPINFO = "app_info";
	public static final String COLUMN_APP_TIME = "timestamp";
	public static final String COLUMN_APP_NAME = "name";
	public static final String COLUMN_APP_ID = "app_id";
	public static final String COLUMN_APP_CPU = "cpu";
	public static final String COLUMN_ID = "id";

	
	public static final String CREATE_APP_TABLE = "create table " + 
			TABLE_APPINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_APP_TIME + " text not null, " +
			COLUMN_APP_NAME + " text not null, " +
			COLUMN_APP_ID + " integer, " +
			COLUMN_APP_CPU + " real);";
	
	public static void addAppInfo(SQLiteDatabase db, AppInfo appInfo){
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_APP_TIME, appInfo.getTimestamp());
		values.put(COLUMN_APP_NAME, appInfo.getName());
		values.put(COLUMN_APP_ID, appInfo.getAppId());
		values.put(COLUMN_APP_CPU, Double.toString(appInfo.getCPU()));
		
		db.insert(TABLE_APPINFO, null, values);
	}
	
	public static List<AppInfo> getAllAppInfo(SQLiteDatabase db){
		List<AppInfo> appInfo = new ArrayList<AppInfo>();
		String selectQuery = "SELECT * FROM " + TABLE_APPINFO + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				AppInfo info = new AppInfo(cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getDouble(4));
				appInfo.add(info);
			}while(cursor.moveToNext());
		}
		return appInfo;
	}
	
}
