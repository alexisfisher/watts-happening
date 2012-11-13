package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HardwareTable extends DBTable{

	/* Hardware Table Info */
	public static final String TABLE_HARDWARE = "hardware";
	public static final String COLUMN_HARDWARE_TIME = "timestamp";
	public static final String COLUMN_HARDWARE_NAME = "name";
	public static final String COLUMN_HARDWARE_ENABLED = "enabled";
	public static final String COLUMN_HARDWARE_STATUS = "status";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_HARDWARE_TABLE = "create table " + 
			TABLE_HARDWARE + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_HARDWARE_TIME + " text not null, " + 
			COLUMN_HARDWARE_NAME + " text not null, " + 
			COLUMN_HARDWARE_ENABLED + " integer, " +
			COLUMN_HARDWARE_STATUS + " integer, " +
			"CONSTRAINT chk_enabled " +
			"CHECK ("+ COLUMN_HARDWARE_ENABLED + "= 0 OR " + COLUMN_HARDWARE_ENABLED + " = 1));";
	
	public HardwareTable(Context context) {
		super(context);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_HARDWARE_TABLE;
	}

	@Override
	public void addEntry(DBEntry dbE) throws Exception {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		if (!(dbE instanceof Hardware))
			throw new Exception("Wrong type of entry, should be of type Hardware");
		Hardware hardware = (Hardware)dbE;
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_HARDWARE_TIME, hardware.getTimestamp());
		values.put(COLUMN_HARDWARE_NAME, hardware.getName());
		values.put(COLUMN_HARDWARE_ENABLED, hardware.getEnabled());
		values.put(COLUMN_HARDWARE_STATUS, hardware.getStatus());
		
		db.insert(TABLE_HARDWARE, null, values);
		
	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		List<DBEntry> hardware = new ArrayList<DBEntry>();
		String selectQuery = "SELECT * FROM " + TABLE_HARDWARE + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Hardware hw = new Hardware(cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getString(4));
				hardware.add(hw);
			}while(cursor.moveToNext());
		}
		return hardware;
	}

	@Override
	public String getTableName() {
		return TABLE_HARDWARE;
	}
	
}
