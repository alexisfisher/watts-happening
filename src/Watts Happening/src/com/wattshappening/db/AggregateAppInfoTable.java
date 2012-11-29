package com.wattshappening.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AggregateAppInfoTable extends DBTable {

	public static final String TABLE_AGG_APP_INFO = "agg_app_info";
	public static final String COLUMN_APP_UID = "app_uid";
	public static final String COLUMN_APP_HISTORIC_CPU = "historic_cpu";
	public static final String COLUMN_APP_HISTORIC_NETWORK = "historic_net";
	public static final String COLUMN_APP_HISTORIC_HARDWARE = "historic_hardware";
	public static final String COLUMN_NUM_UPDATES = "number_of_updates";
	public static final String COLUMN_ID = "id";
	
	
	public static final String CREATE_AGG_APP_INFO_TABLE = "create table " + 
			TABLE_AGG_APP_INFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_APP_UID + " integer, " +
			COLUMN_APP_HISTORIC_CPU + " real, " + 
			COLUMN_APP_HISTORIC_NETWORK + " real, " +
			COLUMN_APP_HISTORIC_HARDWARE + " real, " +
			COLUMN_NUM_UPDATES + " integer);";
	
	public AggregateAppInfoTable(Context context) {
		super(context);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_AGG_APP_INFO_TABLE;
	}

	@Override
	public void addEntry(DBEntry dbE) throws Exception {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		
		if (!(dbE instanceof AggregateAppInfo))
			throw new Exception("Wrong type of entry, should be of type AggregateAppInfo");
		AggregateAppInfo appInfo = (AggregateAppInfo)dbE;
		
		values.put(COLUMN_APP_UID, appInfo.getAppUID());
		values.put(COLUMN_APP_HISTORIC_CPU, appInfo.getHistoricCPU());
		values.put(COLUMN_APP_HISTORIC_NETWORK, appInfo.getHistoricNetwork());
		values.put(COLUMN_APP_HISTORIC_HARDWARE, appInfo.getHistoricHardware());
		values.put(COLUMN_NUM_UPDATES, appInfo.getNumUpdates());
		
		
		db.insert(TABLE_AGG_APP_INFO, null, values);

	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		return TABLE_AGG_APP_INFO;
	}

}
