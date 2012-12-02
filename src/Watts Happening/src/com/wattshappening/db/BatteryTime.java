package com.wattshappening.db;

public class BatteryTime {

	long timestamp = 0;
	double percentage = 0;
	int isCharging = -1;
	
	public BatteryTime(double percentage, long timestamp, int isCharging){
		this.timestamp = timestamp;
		this.percentage = percentage;
		this.isCharging = isCharging;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public double getPercentage(){
		return percentage;
	}
	
	public int getIsCharging(){
		return isCharging;
	}
	
	@Override
	public String toString(){
		return "Percentage: " + percentage + " Timestamp: " + timestamp;
	}
}
