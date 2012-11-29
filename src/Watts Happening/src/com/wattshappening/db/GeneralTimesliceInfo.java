package com.wattshappening.db;

public class GeneralTimesliceInfo extends DBEntry {
	
	private long timestamp;
	private int timesliceID;
	private int isCharging;
	private int ticksUser;
	private int ticksSystem;
	private int ticksIdle;
	private int ticksTotal;

	public GeneralTimesliceInfo(int timesliceID, long timestamp, int isCharging, int ticksUser, int ticksSystem, int ticksIdle, int ticksTotal) {
		this.timesliceID = timesliceID;
		this.timestamp = timestamp;
		this.isCharging = isCharging;
		this.ticksUser = ticksUser;
		this.ticksSystem = ticksSystem;
		this.ticksIdle = ticksIdle;
		this.ticksTotal = ticksTotal;
	}
	
	public int getIsCharging() {
		return isCharging;
	}
	
	public int getTimesliceID() {
		return timesliceID;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public int getTicksUser() {
		return ticksUser;
	}
	
	public int getTicksSystem() {
		return ticksSystem;
	}
	
	public int getTicksIdle() {
		return ticksIdle;
	}
	
	public int getTicksTotal() {
		return ticksTotal;
	}

}
