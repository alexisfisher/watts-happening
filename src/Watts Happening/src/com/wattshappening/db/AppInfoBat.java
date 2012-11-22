package com.wattshappening.db;

public class AppInfoBat extends AppInfo {
	
	private double batteryPercentage = 0;
	
	public AppInfoBat(int timesliceID ,String name, int appId, long cpu, long rxBytes, long txBytes, double batteryPercentage){
		super(timesliceID, name, appId, cpu, txBytes, txBytes);
		this.batteryPercentage = batteryPercentage;
	}
	
	public double getBatteryPercentage(){
		return batteryPercentage;
	}
}
