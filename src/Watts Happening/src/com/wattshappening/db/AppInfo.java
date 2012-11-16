package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class AppInfo extends DBEntry{
	
	private String timestamp;
	private int timestampID;
	private String name;
	private int appId;
	private double cpu;

	/* Use this constructor when creating new entries to be inserted into the database */
	public AppInfo(int timestampID ,String name, int appId, double cpu){
		this.timestampID = timestampID;
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.name = name;
		this.appId = appId;
		if(cpu >= 0 && cpu <= 100){
			this.cpu = cpu;
		}
		else{
			// TODO throw exception instead of just exiting
			System.err.println("CPU usage must be between 0 - 100");
			System.exit(1);
		}
	}
	
	/* Use this constructor when returning rows from the database */
	public AppInfo(int timestampID, String timestamp, String name, int appId, double cpu){
		this.timestampID = timestampID;
		this.timestamp = timestamp;
		this.name = name;
		this.appId = appId;
		if(cpu >= 0 && cpu <= 100){
			this.cpu = cpu;
		}
		else{
			// TODO throw exception instead of just exiting
			System.err.println("CPU usage must be between 0 - 100");
			System.exit(1);
		}
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
	
	public double getCPU(){
		return cpu;
	}

	public int getTimestampID() {
		return timestampID;
	}

}
