package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BatteryTable extends DBTable{

	/* Battery Table Info */
	public static final String TABLE_BATTERY = "battery";
	public static final String COLUMN_TIMESLICE_ID = "timeslice_id";
	public static final String COLUMN_BATTERY_VOLTAGE = "voltage";
	public static final String COLUMN_BATTERY_TEMP = "temperature";
	public static final String COLUMN_BATTERY_PERCENTAGE = "percentage";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_BATTERY_TABLE = "create table " +
			TABLE_BATTERY + "(" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TIMESLICE_ID + " integer, " +
			COLUMN_BATTERY_VOLTAGE + " real, " +
			COLUMN_BATTERY_TEMP + " real, " +
			COLUMN_BATTERY_PERCENTAGE + " real);";
	
	public BatteryTable(Context context) {
		super(context);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_BATTERY_TABLE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_BATTERY;
	}
	
	public void addEntry(DBEntry dbE) throws Exception{
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		if (!(dbE instanceof BatteryInfo))
			throw new Exception("Wrong type of entry, should be of type Hardware");
		BatteryInfo batInfo = (BatteryInfo)dbE;
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_TIMESLICE_ID, batInfo.getTimesliceID());
		values.put(COLUMN_BATTERY_VOLTAGE, batInfo.getVoltage());
		values.put(COLUMN_BATTERY_TEMP, batInfo.getTemp());
		values.put(COLUMN_BATTERY_PERCENTAGE, batInfo.getPercentage());
		
		Log.i("BatteryTable: ", 
				"TIME: " + batInfo.getTimesliceID() + 
				" VOLTAGE: " + batInfo.getVoltage() + 
				" TEMP: " + batInfo.getTemp() + 
				" PERCENTAGE: " + batInfo.getPercentage());
		
		db.insert(TABLE_BATTERY, null, values);
	}
	
	public List<DBEntry> fetchAllEntries(){
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		List<DBEntry> batInfo = new ArrayList<DBEntry>();
		String selectQuery = 	"SELECT " +
									COLUMN_TIMESLICE_ID + ", " + 
									COLUMN_BATTERY_VOLTAGE + ", " + 
									COLUMN_BATTERY_TEMP + ", " + 
									COLUMN_BATTERY_PERCENTAGE + 
								" FROM " + TABLE_BATTERY + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				BatteryInfo bi = new BatteryInfo(cursor.getInt(0), cursor.getDouble(1), 
												cursor.getDouble(4), cursor.getDouble(3));
				batInfo.add(bi);
			}while(cursor.moveToNext());
		}
		return batInfo;
	}
}
