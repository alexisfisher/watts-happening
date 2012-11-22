package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class BatteryInfo extends DBEntry{

	private int timesliceID;
	private double voltage;
	private double temp;
	private double percentage;
	
	
	public BatteryInfo(int timesliceID, double voltage, double temp, double percentage){
		this.timesliceID = timesliceID;
		this.voltage = voltage;
		this.temp = temp;
		this.percentage = percentage;
	}
	
	public int getTimesliceID(){
		return timesliceID;
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
