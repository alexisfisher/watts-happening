package com.wattshappening.db;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBManager extends SQLiteOpenHelper {
	
	private static DBManager instance = null;

	private static final String DB_NAME = "wh_log.db";
	private static final int DB_VERSION = 1;
	
	protected DBManager(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static DBManager getInstance(Context context){
		if (instance == null)
			instance = new DBManager(context);
		
		return instance;
	}
	
	@Override 
	public void onCreate(SQLiteDatabase database){
		database.execSQL(GPSTable.CREATE_GPS_TABLE);
		database.execSQL(HardwareTable.CREATE_HARDWARE_TABLE);
		database.execSQL(AppInfoTable.CREATE_APP_TABLE);
		database.execSQL(BatteryTable.CREATE_BATTERY_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println("Upgrading database from version " + oldVersion +
				" to " + newVersion + ", ALL CURRENT DATA WILL BE LOST!");
		db.execSQL("DROP TABLE IF EXISTS " + GPSTable.TABLE_GPS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + HardwareTable.TABLE_HARDWARE + ";");
		db.execSQL("DROP TABLE IF EXISTS " + AppInfoTable.TABLE_APPINFO + ";");
		db.execSQL("DROP TABLE IF EXISTS " + BatteryTable.TABLE_BATTERY + ";");
		onCreate(db);
	}
	
	public void addGPSCoordinate(Gps coordinate){
		SQLiteDatabase db = this.getReadableDatabase();
		GPSTable.addGPSCoordinate(db, coordinate);
		db.close();
	}
	
	public List<Gps> getAllGPSCoordinates(){
		SQLiteDatabase db = this.getReadableDatabase();
		List<Gps> coords = GPSTable.getAllGPSCoordinates(db);
		db.close();
		return coords;
	}
	
	public void addHardware(Hardware hardware){
		SQLiteDatabase db = this.getWritableDatabase();
		HardwareTable.addHardware(db, hardware);
		db.close();
	}
	
	public List<Hardware> getAllHardware(){		
		SQLiteDatabase db = this.getReadableDatabase();
		List<Hardware> hardware = HardwareTable.getAllHardware(db);
		db.close();
		return hardware;
	}
	
	public void addAppInfo(AppInfo appInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		AppInfoTable.addAppInfo(db, appInfo);
		db.close();
	}
	
	public List<AppInfo> getAllAppInfo(){		
		SQLiteDatabase db = this.getReadableDatabase();
		List<AppInfo> appInfo = AppInfoTable.getAllAppInfo(db);
		db.close();
		return appInfo;
	}
	
	public void addBattery(BatteryInfo batInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		BatteryTable.addBattery(db, batInfo);
		db.close();
	}
	
	public List<BatteryInfo> getAllBatteryInfo(){	
		SQLiteDatabase db = this.getReadableDatabase();
		List<BatteryInfo> batInfo = BatteryTable.getAllBatteryInfo(db);
		db.close();
		return batInfo;
	}
}

