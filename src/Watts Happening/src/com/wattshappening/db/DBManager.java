package com.wattshappening.db;
import java.util.Vector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	
	private static DBManager instance = null;

	private static final String DB_NAME = "wh_log.db";
	private static final int DB_VERSION = 1;
	
	Vector<DBTable> tables = new Vector<DBTable>();
	
	protected DBManager(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		
		tables.add(new HardwareTable(context));
		tables.add(new GPSTable(context));
		tables.add(new AppInfoTable(context));
		tables.add(new BatteryTable(context));
		tables.add(new NetworkTable(context));
	}
	
	public static DBManager getInstance(Context context){
		if(context == null && instance == null){
			System.err.println("CAN'T MAKE DB WITH NULL INSTANCE");
		}
		if (instance == null)
			instance = new DBManager(context);
		
		return instance;
	}
	
	@Override 
	public void onCreate(SQLiteDatabase database){
		for (int i = 0; i<tables.size(); ++i)
			database.execSQL(tables.get(i).getCreationQuerry());
	}
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println("Upgrading database from version " + oldVersion +
				" to " + newVersion + ", ALL CURRENT DATA WILL BE LOST!");
		
		for (int i = 0; i<tables.size(); ++i)
			db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName() + ";");
		
		onCreate(db);
	}	
	
	public void dropTables(SQLiteDatabase db){
		for (int i = 0; i< tables.size(); ++i){
			db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName() + ";");
		}
	}
}

