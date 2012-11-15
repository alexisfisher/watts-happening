package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NetworkTable extends DBTable {
	
	public static final String TABLE_NETWORK = "network";
	public static final String COLUMN_NETWORK_TIME = "timestamp";
	public static final String COLUMN_NETWORK_NAME = "name";
	public static final String COLUMN_NETWORK_STATE = "state";
	public static final String COLUMN_NETWORK_CONNECTION = "connection";
	public static final String COLUMN_ID = "id";
	
	public static final String CREATE_HARDWARE_TABLE = "create table " + 
			TABLE_NETWORK + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_NETWORK_TIME + " text not null, " + 
			COLUMN_NETWORK_NAME + " text not null, " + 
			COLUMN_NETWORK_STATE + " text not null, " +
			COLUMN_NETWORK_CONNECTION + " text not null); ";

	public NetworkTable(Context context) {
		super(context);
	}

	@Override
	public String getCreationQuerry() {
		return CREATE_HARDWARE_TABLE;
	}

	@Override
	public void addEntry(DBEntry dbE) throws Exception {

		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		if (!(dbE instanceof NetworkEntry))
			throw new Exception("Wrong type of entry, should be of type Hardware");
		NetworkEntry network = (NetworkEntry)dbE;
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_NETWORK_TIME, network.getTimestamp());
		values.put(COLUMN_NETWORK_NAME, network.getName());
		values.put(COLUMN_NETWORK_STATE, network.getState());
		values.put(COLUMN_NETWORK_CONNECTION, network.getConnection());
		
		db.insert(TABLE_NETWORK, null, values);
		
		Log.i("HardwareTable","Timestamp: " + network.getTimestamp() + 
				", Name: " + network.getName() + 
				", Enabled: " + network.getState() + 
				", Status: " + network.getConnection());

	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		List<DBEntry> network = new ArrayList<DBEntry>();
		String selectQuery = "SELECT * FROM " + TABLE_NETWORK + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				NetworkEntry net = new NetworkEntry(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
				network.add(net);
			}while(cursor.moveToNext());
		}
		return network;
	}

	@Override
	public String getTableName() {
		return TABLE_NETWORK;
	}

}
