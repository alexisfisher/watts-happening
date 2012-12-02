package com.wattshappening.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBTable {
	protected static SQLiteDatabase db;
	protected Context context;
	
	public static final int FLUSH_ALL = 1;
	public static final int FLUSH_ONLY_NONPERSISTANT = 2;
	//public final int FLUSH_ = 4
	//public final int FLUSH_ = 8
	//public final int FLUSH_ = 16
	//public final int FLUSH_ = 32
	
	protected int flushLevel = 1;
	
	public DBTable(Context context, int flushLevel) {
		this.context = context;
		this.flushLevel = flushLevel;
		// TODO Auto-generated constructor stub
	}
	
	public abstract String getCreationQuerry();
	
	public abstract void addEntry(DBEntry dbE) throws Exception ;
	
	public abstract List<DBEntry> fetchAllEntries();
	
	public abstract String getTableName();
	
	public int getFlushLevel()
	{
		return flushLevel;
	}
	
	public boolean shouldIBeFlushed(int flushLevel)
	{
		return ((this.flushLevel & flushLevel)>0);
	}

}
