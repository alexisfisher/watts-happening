package com.wattshappening.db;


public class AppInfo extends DBEntry{
	
	private int timesliceID;
	private String name;
	private int appId;
	private long cpu;
	private long rxBytes;
	private long txBytes;

	/* Use this constructor when creating new entries to be inserted into the database */
	public AppInfo(int timesliceID ,String name, int appId, long cpu, long rxBytes, long txBytes){
		this.timesliceID = timesliceID;
		this.name = name;
		this.appId = appId;
		this.cpu = cpu;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
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

	public int getTimesliceID() {
		return timesliceID;
	}

	public long getRXBytes() {
		return rxBytes;
	}
	
	public long getTXBytes() {
		return txBytes;
	}

}
