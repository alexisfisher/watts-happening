package com.wattshappening.db;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "wh_log.db";
	private static final int DB_VERSION = 1;
	
	/* GPS Table info */
	private static final String TABLE_GPS = "gps";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_GPS_TIME = "timestamp";
	private static final String COLUMN_GPS_COORD = "coordinates";

	private static final String CREATE_GPS_TABLE = "create table " + 
			TABLE_GPS + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_GPS_TIME + " text not null, " + 
			COLUMN_GPS_COORD + " text not null);";
	
	/* Hardware Table Info */
	private static final String TABLE_HARDWARE = "hardware";
	private static final String COLUMN_HARDWARE_TIME = "timestamp";
	private static final String COLUMN_HARDWARE_NAME = "name";
	private static final String COLUMN_HARDWARE_ENABLED = "enabled";
	private static final String COLUMN_HARDWARE_STATUS = "status";
	
	private static final String CREATE_HARDWARE_TABLE = "create table " + 
			TABLE_HARDWARE + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_HARDWARE_TIME + " text not null, " + 
			COLUMN_HARDWARE_NAME + " text not null, " + 
			COLUMN_HARDWARE_ENABLED + " integer, " +
			COLUMN_HARDWARE_STATUS + " integer, " +
			"CONSTRAINT chk_enabled " +
			"CHECK ("+ COLUMN_HARDWARE_ENABLED + "= 0 OR " + COLUMN_HARDWARE_ENABLED + " = 1));";
	
	/* Application Table Info */
	private static final String TABLE_APPINFO = "app_info";
	private static final String COLUMN_APP_TIME = "timestamp";
	private static final String COLUMN_APP_NAME = "name";
	private static final String COLUMN_APP_ID = "app_id";
	private static final String COLUMN_APP_CPU = "cpu";
	
	private static final String CREATE_APP_TABLE = "create table " + 
			TABLE_APPINFO + "(" +
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_APP_TIME + " text not null, " +
			COLUMN_APP_NAME + " text not null, " +
			COLUMN_APP_ID + " integer, " +
			COLUMN_APP_CPU + " real);";
	
	/* Battery Table Info */
	private static final String TABLE_BATTERY = "battery";
	private static final String COLUMN_BATTERY_TIME = "timestamp";
	private static final String COLUMN_BATTERY_VOLTAGE = "voltage";
	private static final String COLUMN_BATTERY_TEMP = "temperature";
	private static final String COLUMN_BATTERY_PERCENTAGE = "percentage";
	private static final String COLUMN_BATTERY_SCALE = "scale";
	
	private static final String CREATE_BATTERY_TABLE = "create table " +
			TABLE_BATTERY + "(" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_BATTERY_TIME + " text not null, " +
			COLUMN_BATTERY_VOLTAGE + " real, " +
			COLUMN_BATTERY_TEMP + " real, " +
			COLUMN_BATTERY_PERCENTAGE + " real, " +
			COLUMN_BATTERY_SCALE + " integer);";
	
	public DBManager(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override 
	public void onCreate(SQLiteDatabase database){
		database.execSQL(CREATE_GPS_TABLE);
		database.execSQL(CREATE_HARDWARE_TABLE);
		database.execSQL(CREATE_APP_TABLE);
		database.execSQL(CREATE_BATTERY_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println("Upgrading database from version " + oldVersion +
				" to " + newVersion + ", ALL CURRENT DATA WILL BE LOST!");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HARDWARE + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPINFO + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATTERY + ";");
		onCreate(db);
	}
	
	public void addGPSCoordinate(Gps coordinate){
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_GPS_TIME, coordinate.getTimestamp());
		values.put(COLUMN_GPS_COORD, coordinate.getCoordinate());
		
		db.insert(TABLE_GPS, null, values);
		db.close();
	}
	
	public Gps getGPSCoordinate(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_GPS, new String[] {COLUMN_ID, COLUMN_GPS_TIME,
				COLUMN_GPS_COORD}, COLUMN_ID + "=?", new String[]{String.valueOf(id)},
				null, null, null, null);
		if(cursor != null){
			cursor.moveToFirst();
			Gps gps =  new Gps(cursor.getString(1), cursor.getString(2));
			db.close();
			return gps;
		}
		
		db.close();
		return null;
	}
	
	public List<Gps> getAllGPSCoordinates(){
		List<Gps> coords = new ArrayList<Gps>();
		String selectQuery = "SELECT * FROM " + TABLE_GPS + ";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Gps coord = new Gps(cursor.getString(1), cursor.getString(2));
				coords.add(coord);
			}while(cursor.moveToNext());
		}
		db.close();
		return coords;
	}
	
	public void addHardware(Hardware hardware){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_HARDWARE_TIME, hardware.getTimestamp());
		values.put(COLUMN_HARDWARE_NAME, hardware.getName());
		values.put(COLUMN_HARDWARE_ENABLED, hardware.getEnabled());
		values.put(COLUMN_HARDWARE_STATUS, hardware.getStatus());
		
		db.insert(TABLE_HARDWARE, null, values);
		db.close();
	}
	
	public List<Hardware> getAllHardware(){
		List<Hardware> hardware = new ArrayList<Hardware>();
		String selectQuery = "SELECT * FROM " + TABLE_HARDWARE + ";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Hardware hw = new Hardware(cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getString(4));
				hardware.add(hw);
			}while(cursor.moveToNext());
		}
		db.close();
		return hardware;
	}
	
	public void addAppInfo(AppInfo appInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_APP_TIME, appInfo.getTimestamp());
		values.put(COLUMN_APP_NAME, appInfo.getName());
		values.put(COLUMN_APP_ID, appInfo.getAppId());
		values.put(COLUMN_APP_CPU, Double.toString(appInfo.getCPU()));
		
		db.insert(TABLE_APPINFO, null, values);
		db.close();
	}
	
	public List<AppInfo> getAllAppInfo(){
		List<AppInfo> appInfo = new ArrayList<AppInfo>();
		String selectQuery = "SELECT * FROM " + TABLE_APPINFO + ";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				AppInfo info = new AppInfo(cursor.getString(1), cursor.getString(2), cursor.getInt(3), 
						cursor.getDouble(4));
				appInfo.add(info);
			}while(cursor.moveToNext());
		}
		db.close();
		return appInfo;
	}
	
	public void addBattery(BatteryInfo batInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_BATTERY_TIME, batInfo.getTimestamp());
		values.put(COLUMN_BATTERY_VOLTAGE, batInfo.getVoltage());
		values.put(COLUMN_BATTERY_TEMP, batInfo.getTemp());
		values.put(COLUMN_BATTERY_PERCENTAGE, batInfo.getPercentage());
		values.put(COLUMN_BATTERY_SCALE, batInfo.getScale());
		
		db.insert(TABLE_BATTERY, null, values);
		db.close();
	}
	
	public List<BatteryInfo> getAllBatteryInfo(){
		List<BatteryInfo> batInfo = new ArrayList<BatteryInfo>();
		String selectQuery = "SELECT * FROM " + TABLE_BATTERY + ";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				BatteryInfo bi = new BatteryInfo(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),
						cursor.getDouble(4), cursor.getInt(5));
				batInfo.add(bi);
			}while(cursor.moveToNext());
		}
		return batInfo;
	}
}

