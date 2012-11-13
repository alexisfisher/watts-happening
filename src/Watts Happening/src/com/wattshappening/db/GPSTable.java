package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GPSTable extends DBTable {

	public GPSTable(Context context) {
		super(context);
	}

	/* GPS Table info */
	public static final String TABLE_GPS = "gps";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_GPS_TIME = "timestamp";
	public static final String COLUMN_GPS_LAT = "latitude";
	public static final String COLUMN_GPS_LONG = "longitude";

	public static final String CREATE_GPS_TABLE = "create table " + 
			TABLE_GPS + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_GPS_TIME + " text not null, " + 
			COLUMN_GPS_LAT + " real not null, " + 
			COLUMN_GPS_LONG + " real not null);";

	public static void addGPSCoordinate(SQLiteDatabase db, Gps coordinate){
		
		//db.close();
	}
	
	@Override
	public String getCreationQuerry() {
		// TODO Auto-generated method stub
		return CREATE_GPS_TABLE;
	}

	@Override
	public void addEntry(DBEntry dbE) throws Exception {
		// TODO Auto-generated method stub
		if (!(dbE instanceof Gps))
			throw new Exception("Wrong type of entry, should be of type Gps");
		
		Gps coordinate = (Gps)dbE;

		SQLiteDatabase db = DBManager.getInstance(context).getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_GPS_TIME, coordinate.getTimestamp());
		values.put(COLUMN_GPS_LAT, coordinate.getLatitude());
		values.put(COLUMN_GPS_LONG, coordinate.getLongitude());

		Log.i("GPSTable: ", "TIME: " + coordinate.getTimestamp() + " Latitude: " +
				coordinate.getLatitude() + " Longitude: " + coordinate.getLongitude());
			
		db.insert(TABLE_GPS, null, values);
	}

	@Override
	public List<DBEntry> fetchAllEntries() {
		
		List<DBEntry> coords = new ArrayList<DBEntry>();
		String selectQuery = "SELECT * FROM " + TABLE_GPS + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Gps coord = new Gps(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
				coords.add(coord);
			}while(cursor.moveToNext());
		}
		
		return coords;
		
	}

	@Override
	public String getTableName() {
		return TABLE_GPS;
	}
}