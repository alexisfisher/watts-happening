package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HardwareTable extends DBTable{

	/* Hardware Table Info */
	public static final String TABLE_HARDWARE = "hardware";
	public static final String COLUMN_TIMESLICE_ID = "timeslice_id";
	public static final String COLUMN_HARDWARE_NAME = "name";
	public static final String COLUMN_HARDWARE_ENABLED = "enabled";
	public static final String COLUMN_HARDWARE_STATUS = "status";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_HARDWARE_TABLE = "create table " + 
			TABLE_HARDWARE + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TIMESLICE_ID + " integer, " + 
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
		
		values.put(COLUMN_TIMESLICE_ID, hardware.getTimesliceID());
		values.put(COLUMN_HARDWARE_NAME, hardware.getName());
		values.put(COLUMN_HARDWARE_ENABLED, hardware.getEnabled());
		values.put(COLUMN_HARDWARE_STATUS, hardware.getStatus());
		
		db.insert(TABLE_HARDWARE, null, values);
		
		Log.i("HardwareTable","Timeslice: " + hardware.getTimesliceID() + 
				", Name: " + hardware.getName() + 
				", Enabled: " + hardware.getEnabled() + 
				", Status: " + hardware.getStatus());
		
	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		List<DBEntry> hardware = new ArrayList<DBEntry>();
		String selectQuery = 	"SELECT " + 
									COLUMN_TIMESLICE_ID + ", " +
									COLUMN_HARDWARE_NAME + ", " +
									COLUMN_HARDWARE_ENABLED + ", " +
									COLUMN_HARDWARE_STATUS + ", " +
								" FROM " + TABLE_HARDWARE + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Hardware hw = new Hardware(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), 
						cursor.getString(3));
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
