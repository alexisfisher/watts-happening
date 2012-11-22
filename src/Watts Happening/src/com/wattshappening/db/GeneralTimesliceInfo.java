package com.wattshappening.db;

public class GeneralTimesliceInfo extends DBEntry {
	
	private String timestamp;
	private int timesliceID;
	private int isCharging;

	public GeneralTimesliceInfo(int timesliceID, String timestamp, int isCharging) {
		this.timesliceID = timesliceID;
		this.timestamp = timestamp;
		this.isCharging = isCharging;
	}
	
	public int getIsCharging() {
		return isCharging;
	}
	
	public int getTimesliceID() {
		return timesliceID;
	}
	
	public String getTimestamp() {
		return timestamp;
	}

}
