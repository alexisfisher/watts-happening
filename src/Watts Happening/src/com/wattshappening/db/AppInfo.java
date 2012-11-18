package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class AppInfo extends DBEntry{
	
	private String timestamp;
	private int timestampID;
	private String name;
	private int appId;
	private long cpu;
	private long rxBytes;
	private long txBytes;

	/* Use this constructor when creating new entries to be inserted into the database */
	public AppInfo(int timestampID ,String name, int appId, long cpu, long rxBytes, long txBytes){
		this.timestampID = timestampID;
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.name = name;
		this.appId = appId;
		this.cpu = cpu;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
	}
	
	/* Use this constructor when returning rows from the database */
	public AppInfo(int timestampID, String timestamp, String name, int appId, long cpu, long rxBytes, long txBytes){
		this.timestampID = timestampID;
		this.timestamp = timestamp;
		this.name = name;
		this.appId = appId;
		this.cpu = cpu;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
	}
	
	public String getTimestamp(){
		return timestamp;
	}
	
	public String getName(){
		return name;
	}
	
	public int getAppId(){
		return appId;
	}
	
	public long getCPU(){
		return cpu;
	}

	public int getTimestampID() {
		return timestampID;
	}

	public long getRXBytes() {
		return rxBytes;
	}
	
	public long getTXBytes() {
		return txBytes;
	}

}
