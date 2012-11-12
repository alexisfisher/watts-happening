package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class Gps extends DBEntry {
	int id;
	private String timestamp;
	private String coordinate;

	/* Use the constructor when making new entries to be inserted into the database */
	public Gps(String coordinates){
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.coordinate = coordinates;
	}
	
	/* Use this constructor when returning rows from the database */
	public Gps(String timestamp, String coordinates){
		this.timestamp = timestamp;
		this.coordinate = coordinates;
	}

	public String getTimestamp(){
		return timestamp;
	}
	
	public String getCoordinate(){
		return coordinate;
	}
}
