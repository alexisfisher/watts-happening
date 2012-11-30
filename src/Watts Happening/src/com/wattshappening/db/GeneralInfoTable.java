package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GeneralInfoTable extends DBTable {
	
	public static final String TABLE_GENINFO = "gen_info";
	public static final String COLUMN_GEN_TIME = "timestamp";
	public static final String COLUMN_TIMESLICE_ID = "timeslice_id";
	public static final String COLUMN_IS_CHARGING = "is_charging";
	public static final String COLUMN_TICKS_USER = "ticks_user";
	public static final String COLUMN_TICKS_SYSTEM = "ticks_system";
	public static final String COLUMN_TICKS_IDLE = "ticks_idle";
	public static final String COLUMN_TICKS_TOTAL = "ticks_total";
	public static final String COLUMN_HAS_BEEN_ANALYZED = "has_been_analyzed";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_GEN_TABLE = "create table " + 
			TABLE_GENINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TIMESLICE_ID + " integer not null, " +
			COLUMN_GEN_TIME + " real not null, " +
			COLUMN_IS_CHARGING + " integer, " +
			COLUMN_TICKS_USER + " integer, " +
			COLUMN_TICKS_SYSTEM + " integer, " +
			COLUMN_TICKS_IDLE + " integer, " +
			COLUMN_TICKS_TOTAL + " integer, " +
			COLUMN_HAS_BEEN_ANALYZED + " integer default 0" +
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
		values.put(COLUMN_TICKS_USER, genTimeInfo.getTicksUser());
		values.put(COLUMN_TICKS_SYSTEM, genTimeInfo.getTicksSystem());
		values.put(COLUMN_TICKS_IDLE, genTimeInfo.getTicksIdle());
		values.put(COLUMN_TICKS_TOTAL, genTimeInfo.getTicksTotal());
		
		db.insert(TABLE_GENINFO, null, values);
		

	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Will return the list of timeslices that have not yet been analyzed
	 * @author Nick
	 * @return A vector containing the list of GeneralTimesliceInfo objects that represent that
	 * 		the timeslices that have not yet been analyzed.
	 */
	public Vector<GeneralTimesliceInfo> fetchAllNewEntries(){
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		Vector<GeneralTimesliceInfo> timesliceInfo = new Vector<GeneralTimesliceInfo>();
		String selectQuery = 	"SELECT " + 
									COLUMN_TIMESLICE_ID + "	, " +
									COLUMN_GEN_TIME + "		, " +
									COLUMN_IS_CHARGING + "	, " +
									COLUMN_TICKS_USER + "	, " +
									COLUMN_TICKS_SYSTEM + "	, " +
									COLUMN_TICKS_IDLE + "	, " +
									COLUMN_TICKS_TOTAL +
								" FROM " + TABLE_GENINFO + 
								" WHERE " + COLUMN_HAS_BEEN_ANALYZED +"=0;";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				
				//GeneralTimesliceInfo(int timesliceID, long timestamp, int isCharging, int ticksUser, int ticksSystem, int ticksIdle, int ticksTotal)
				GeneralTimesliceInfo ts = new GeneralTimesliceInfo(	cursor.getInt(cursor.getColumnIndex(COLUMN_TIMESLICE_ID)), 
																	cursor.getLong(cursor.getColumnIndex(COLUMN_GEN_TIME)), 
																	cursor.getInt(cursor.getColumnIndex(COLUMN_IS_CHARGING)), 
																	cursor.getInt(cursor.getColumnIndex(COLUMN_TICKS_USER)), 
																	cursor.getInt(cursor.getColumnIndex(COLUMN_TICKS_SYSTEM)), 
																	cursor.getInt(cursor.getColumnIndex(COLUMN_TICKS_IDLE)), 
																	cursor.getInt(cursor.getColumnIndex(COLUMN_TICKS_TOTAL)));
				timesliceInfo.add(ts);
			}while(cursor.moveToNext());
		}
		
		return timesliceInfo;
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
	
	public void markAllEntriesRead(){
		SQLiteDatabase db = DBManager.getInstance(context).getReadableDatabase();
		
		ContentValues args = new ContentValues();
		args.put(GeneralInfoTable.COLUMN_HAS_BEEN_ANALYZED, "1");
		db.update(GeneralInfoTable.CREATE_GEN_TABLE, args, null, null);
	}

}
