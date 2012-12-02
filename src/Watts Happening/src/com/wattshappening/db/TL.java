package com.wattshappening.db;

public class TL extends DBEntry{
	
	long timeslice;
	double shortTermRemaining;
	double longTermRemaining;
	double percentage;
	
	public TL(long timeslice, double shortTermRemaining, double longTermRemaining, double percentage){
		this.timeslice = timeslice;
		this.shortTermRemaining = shortTermRemaining;
		this.longTermRemaining = longTermRemaining;
		this.percentage = percentage;
	}

	public long getTimeslice(){
		return timeslice;
	}
	
	public double getShortTermRemaining(){
		return shortTermRemaining;
	}
	
	public double getLongTermRemaining(){
		return longTermRemaining;
	}
	
	public double getPercentage(){
		return percentage;
	}
	
}
