package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class NetworkEntry extends DBEntry {

	private String timestamp;
	private String name;
	private String state;
	private String connection;
	
	public NetworkEntry(String timestamp, String name, String state, String connection) {
		this.timestamp = timestamp;
		this.name = name;
		this.state = state;
		this.connection = connection;
	}
	
	public NetworkEntry( String name, String state, String connection) {
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();;
		this.name = name;
		this.state = state;
		this.connection = connection;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public String getConnection() {
		return connection;
	}

}
