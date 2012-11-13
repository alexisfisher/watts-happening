package com.wattshappening.db;

public class BatteryInfo extends DBEntry{

	private String timestamp;
	private double voltage;
	private double temp;
	private double percentage;
	private int scale;
	
	public BatteryInfo(double voltage, double temp, double percentage, int scale){
		this.voltage = voltage;
		this.temp = temp;
		this.percentage = percentage;
		this.scale = scale;
	}
	
	public BatteryInfo(String timestamp, double voltage, double temp, double percentage, int scale){
		this.timestamp = timestamp;
		this.voltage = voltage;
		this.temp = temp;
		this.percentage = percentage;
		this.scale = scale;
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
	
	public int getScale(){
		return scale;
	}
}
