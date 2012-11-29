package com.wattshappening.db;

public class BatteryTime {

	long timestamp = 0;
	double percentage = 0;
	
	public BatteryTime(double percentage, long timestamp){
		this.timestamp = timestamp;
		this.percentage = percentage;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public double getPercentage(){
		return percentage;
	}
	
	@Override
	public String toString(){
		return "Percentage: " + percentage + " Timestamp: " + timestamp;
	}
}
