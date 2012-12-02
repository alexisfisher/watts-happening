package com.wattshappening.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimeLeftTable extends DBTable{

	public static final String TABLE_TIMELEFT = "timeleft";
	public static final String COLUMN_TL_TIMESLICE = "timeslice";
	public static final String COLUMN_TL_SHORT = "short";
	public static final String COLUMN_TL_LONG = "long";
	public static final String COLUMN_TL_CPU = "cpu";
	public static final String COLUMN_ID = "id";
	
	
	public static final String CREATE_TL_TABLE = "create table " + 
			TABLE_TIMELEFT + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_TL_TIMESLICE + " real not null, " +
			COLUMN_TL_SHORT + " real, " +
			COLUMN_TL_LONG + " real, " +
			COLUMN_TL_CPU + " real); ";
	
	@Override
	public void addEntry(DBEntry dbE) throws Exception{
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		if (!(dbE instanceof TL))
			throw new Exception("Wrong type of entry, should be of type AppInfo");
		TL timeleft = (TL)dbE;
		
		values.put(COLUMN_TL_TIMESLICE, timeleft.getTimeslice());
		values.put(COLUMN_TL_SHORT, Double.toString(timeleft.getShortTermRemaining()));
		values.put(COLUMN_TL_LONG, Double.toString(timeleft.getLongTermRemaining()));
		values.put(COLUMN_TL_CPU, Double.toString(timeleft.getPercentage()));
		
		Log.i("TimeLeftTable", 
				"Timeslice: " + timeleft.getTimeslice() + 
				" Short: " + timeleft.getShortTermRemaining() +
				" Long: " + timeleft.getLongTermRemaining() + 
				" Percentage: " + timeleft.getPercentage());
		
		db.insert(TABLE_TIMELEFT, null, values);
	}
	
	public TimeLeftTable(Context context) {
		super(context, DBTable.FLUSH_ALL | DBTable.FLUSH_ONLY_NONPERSISTANT);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_TL_TABLE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_TIMELEFT;
	}
	
	@Override
	public List<DBEntry> fetchAllEntries(){
		return null;
	}
	
}
