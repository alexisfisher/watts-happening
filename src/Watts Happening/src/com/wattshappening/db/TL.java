package com.wattshappening.db;

public class TL extends DBEntry{
	
	int timeslice_id;
	double shortTermRemaining;
	double longTermRemaining;
	double percentage;
	
	public TL(int timeslice_id, double shortTermRemaining, double longTermRemaining, double percentage){
		this.timeslice_id = timeslice_id;
		this.shortTermRemaining = shortTermRemaining;
		this.longTermRemaining = longTermRemaining;
		this.percentage = percentage;
	}

	public int getTimesliceID(){
		return timeslice_id;
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
