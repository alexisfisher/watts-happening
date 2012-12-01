package com.wattshappening.db;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		tables.add(new GeneralInfoTable(context));
		tables.add(new AggregateAppInfoTable(context));
	}
	
	public static DBManager getInstance(Context context){
		if(context == null && instance == null){
			System.err.println("CAN'T MAKE DB WITH NULL CONTEXT");
		}
		if (instance == null)
			instance = new DBManager(context);
		
		return instance;
	}
	
	@Override 
	public void onCreate(SQLiteDatabase database){
		createDatabaseTables(database,DBTable.FLUSH_ALL);
	}
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println("Upgrading database from version " + oldVersion +
				" to " + newVersion + ", ALL CURRENT DATA WILL BE LOST!");
		
		flushTables(db, DBTable.FLUSH_ALL);
		
	}	
	
	/**
	 * 
	 * @param uid - the UID of the app needed
	 * @return Vector containing AppInfoBat objects for the uid specified. On error empty vector is returned.
	 */
	public Vector<AppDetailedInfo> getAppInfo(int uid){
		Vector<AppDetailedInfo> results = new Vector<AppDetailedInfo>();
		
		String sqlQuery = "SELECT DISTINCT " + 
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_TIMESLICE + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_NAME + ", " + 
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_ID + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_CPU + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_RX_BYTES + ", " +
				AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_TX_BYTES + ", " +
				BatteryTable.TABLE_BATTERY + "." + BatteryTable.COLUMN_BATTERY_PERCENTAGE +
				" FROM " + AppInfoTable.TABLE_APPINFO + 
				" LEFT JOIN " + BatteryTable.TABLE_BATTERY + 
				" ON " + AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_TIMESLICE + "=" + BatteryTable.TABLE_BATTERY + "." + BatteryTable.COLUMN_TIMESLICE_ID +
				" WHERE " + 
				AppInfoTable.COLUMN_APP_TIMESLICE + " IN (SELECT " + 
															GeneralInfoTable.COLUMN_TIMESLICE_ID + 
														" FROM " + GeneralInfoTable.TABLE_GENINFO + 
														" WHERE " + GeneralInfoTable.COLUMN_HAS_BEEN_ANALYZED + "=0)" +
				" and " + AppInfoTable.TABLE_APPINFO + "." + AppInfoTable.COLUMN_APP_ID + "=" + uid + ";";

		Cursor cursor = instance.getReadableDatabase().rawQuery(sqlQuery, null);
		
		if(cursor.moveToFirst()){
			do {
				results.add(new AppDetailedInfo(
						cursor.getInt(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_TIMESLICE)),
						cursor.getString(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_NAME)),
						cursor.getInt(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_ID)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_APP_CPU)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_RX_BYTES)),
						cursor.getLong(cursor.getColumnIndex(AppInfoTable.COLUMN_TX_BYTES)),
						cursor.getDouble(cursor.getColumnIndex(BatteryTable.COLUMN_BATTERY_PERCENTAGE))
						));
			}while(cursor.moveToNext());
		}
		else {
			Log.e("DBManager", "NO ROWS RETURNED FOR getAppInfo");
			/* Empty result Vector will be returned on error */
		}
	
		// print out the result set as AppInfoBat objects
/*		for(int i = 0; i < results.size(); i++){
			Log.i("DBManager", results.get(i).toString());
		}
	*/	
		return results;
	}
	
	/**
	 * Call this function to flush the data from the tables. You should pass it 
	 * a flush level and all tables that match that level will be flushed, 
	 * tables that do not match that flush level will not be flushed. To flush 
	 * the data this function will drop and recreate the tables that match the
	 * flush level, which also makes it useful if you just want to recreate the
	 * tables.
	 * 
	 * @author Nick
	 * @param flushLevel - The level of flush you want to perform (see DBTable 
	 * 		for the available flush levels)
	 */
	public void flushTables(SQLiteDatabase db,int flushLevel)
	{
		for (int i = 0; i<tables.size(); ++i)
			if (tables.get(i).shouldIBeFlushed(flushLevel))
				db.execSQL("DROP TABLE IF EXISTS " + tables.get(i).getTableName() + ";");
		
		createDatabaseTables(db,flushLevel);
	}
	
	/**
	 * This function will handle creating the tables for a given flush level. It should
	 * really only be called if you know that the tables don't exist (which would be from
	 * onCreate() or from flushTables.
	 * 
	 * @author Nick
	 * @param db - The database object to use when performing the queries 
	 * @param flushLevel
	 */
	private void createDatabaseTables(SQLiteDatabase db, int flushLevel)
	{
		for (int i = 0; i<tables.size(); ++i)
			if (tables.get(i).shouldIBeFlushed(flushLevel))
				db.execSQL(tables.get(i).getCreationQuerry());
	}
}

