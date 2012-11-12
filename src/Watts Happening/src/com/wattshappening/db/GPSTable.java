package com.wattshappening.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GPSTable {

	/* GPS Table info */
	public static final String TABLE_GPS = "gps";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_GPS_TIME = "timestamp";
	public static final String COLUMN_GPS_COORD = "coordinates";

	public static final String CREATE_GPS_TABLE = "create table " + 
			TABLE_GPS + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_GPS_TIME + " text not null, " + 
			COLUMN_GPS_COORD + " text not null);";

	public static void addGPSCoordinate(SQLiteDatabase db, Gps coordinate){
		//SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_GPS_TIME, coordinate.getTimestamp());
		values.put(COLUMN_GPS_COORD, coordinate.getCoordinate());
		
		db.insert(TABLE_GPS, null, values);
		//db.close();
	}
	
	public static List<Gps> getAllGPSCoordinates(SQLiteDatabase db){
		List<Gps> coords = new ArrayList<Gps>();
		String selectQuery = "SELECT * FROM " + TABLE_GPS + ";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Gps coord = new Gps(cursor.getString(1), cursor.getString(2));
				coords.add(coord);
			}while(cursor.moveToNext());
		}
		
		return coords;
	}
}