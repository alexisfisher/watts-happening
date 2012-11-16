package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class BatteryInfo extends DBEntry{

	private String timestamp;
	private double voltage;
	private double temp;
	private double percentage;
	
	public BatteryInfo(double voltage, double temp, double percentage){
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.voltage = voltage;
		this.temp = temp;
		this.percentage = percentage;
	}
	
	public BatteryInfo(String timestamp, double voltage, double temp, double percentage){
		this.timestamp = timestamp;
		this.voltage = voltage;
		this.temp = temp;
		this.percentage = percentage;
	}
	
	public String getTimestamp(){
		return timestamp;
	}
	
	public double getVoltage(){
		return voltage;
	}
	
	public double getTemp(){
		return temp;
	}
	
	public double getPercentage(){
		return percentage;
	}
	
}
