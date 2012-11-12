package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BatteryTable {

	/* Battery Table Info */
	public static final String TABLE_BATTERY = "battery";
	public static final String COLUMN_BATTERY_TIME = "timestamp";
	public static final String COLUMN_BATTERY_VOLTAGE = "voltage";
	public static final String COLUMN_BATTERY_TEMP = "temperature";
	public static final String COLUMN_BATTERY_PERCENTAGE = "percentage";
	public static final String COLUMN_BATTERY_SCALE = "scale";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_BATTERY_TABLE = "create table " +
			TABLE_BATTERY + "(" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_BATTERY_TIME + " text not null, " +
			COLUMN_BATTERY_VOLTAGE + " real, " +
			COLUMN_BATTERY_TEMP + " real, " +
			COLUMN_BATTERY_PERCENTAGE + " real, " +
			COLUMN_BATTERY_SCALE + " integer);";
	
	public static void addBattery(SQLiteDatabase db, BatteryInfo batInfo){
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_BATTERY_TIME, batInfo.getTimestamp());
		values.put(COLUMN_BATTERY_VOLTAGE, batInfo.getVoltage());
		values.put(COLUMN_BATTERY_TEMP, batInfo.getTemp());
		values.put(COLUMN_BATTERY_PERCENTAGE, batInfo.getPercentage());
		values.put(COLUMN_BATTERY_SCALE, batInfo.getScale());
		
		db.insert(TABLE_BATTERY, null, values);
	}
	
	public static List<BatteryInfo> getAllBatteryInfo(SQLiteDatabase db){
		List<BatteryInfo> batInfo = new ArrayList<BatteryInfo>();
		String selectQuery = "SELECT * FROM " + TABLE_BATTERY + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				BatteryInfo bi = new BatteryInfo(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),
						cursor.getDouble(4), cursor.getInt(5));
				batInfo.add(bi);
			}while(cursor.moveToNext());
		}
		return batInfo;
	}
}
