package com.wattshappening.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBTable {
	protected static SQLiteDatabase db;
	protected Context context;
	
	public DBTable(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public abstract String getCreationQuerry();
	
	public abstract void addEntry(DBEntry dbE) throws Exception ;
	
	public abstract List<DBEntry> fetchAllEntries();
	
	public abstract String getTableName();

}
