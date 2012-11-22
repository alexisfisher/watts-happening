package com.wattshappening.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GeneralInfoTable extends DBTable {
	
	public static final String TABLE_GENINFO = "gen_info";
	public static final String COLUMN_GEN_TIME = "timestamp";
	public static final String COLUMN_TIMESLICE_ID = "timeslice_id";
	public static final String COLUMN_IS_CHARGING = "is_charging";
	public static final String COLUMN_ID = "id";
	public static final String CREATE_GEN_TABLE = "create table " + 
			TABLE_GENINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TIMESLICE_ID + " integer not null, " +
			COLUMN_GEN_TIME + " text not null, " +
			COLUMN_IS_CHARGING + " integer " +
			"CONSTRAINT chk_charging " +
			"CHECK ("+ COLUMN_IS_CHARGING + "= 0 OR " + COLUMN_IS_CHARGING + " = 1));";

	public GeneralInfoTable(Context context) {
		super(context);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_GEN_TABLE;
	}

	@Override
	public void addEntry(DBEntry dbE) throws Exception {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		
		if (!(dbE instanceof GeneralTimesliceInfo))
			throw new Exception("Wrong type of entry, should be of type GeneralTimesliceInfo");
		GeneralTimesliceInfo genTimeInfo = (GeneralTimesliceInfo)dbE;
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_GEN_TIME, genTimeInfo.getTimestamp());
		values.put(COLUMN_TIMESLICE_ID, genTimeInfo.getTimesliceID());
		values.put(COLUMN_IS_CHARGING, genTimeInfo.getIsCharging());
		
		db.insert(TABLE_GENINFO, null, values);
		

	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		return TABLE_GENINFO;
	}
	
	public int getNextTimesliceID()
	{
		SQLiteDatabase db = DBManager.getInstance(context).getReadableDatabase();
		String selectQuery = "SELECT MAX(" + COLUMN_TIMESLICE_ID + ") FROM " + TABLE_GENINFO;
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (!cursor.moveToFirst()) //there are no entries yet
			return 0; 
		else
			return cursor.getInt(0) + 1;
		
	}

}
