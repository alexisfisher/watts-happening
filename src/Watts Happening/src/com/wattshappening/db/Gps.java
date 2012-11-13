package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class Gps extends DBEntry {
	int id;
	private String timestamp;
	private double lat;
	private double longitude;

	/* Use the constructor when making new entries to be inserted into the database */
	public Gps(double lat, double longitude){
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.lat = lat;
		this.longitude = longitude;
	}
	
	/* Use this constructor when returning rows from the database */
	public Gps(String timestamp, double lat, double longitude){
		this.timestamp = timestamp;
		this.lat = lat;
		this.longitude = longitude;
	}

	public String getTimestamp(){
		return timestamp;
	}
	
	public double getLatitude(){
		return lat;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
