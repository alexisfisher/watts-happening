package com.wattshappening.db;

import java.sql.Timestamp;
import java.util.Calendar;

public class NetworkEntry extends DBEntry {

	private int timesliceID;
	private String name;
	private String state;
	private String connection;
	
	public NetworkEntry(int timesliceID, String name, String state, String connection) {
		this.timesliceID = timesliceID;
		this.name = name;
		this.state = state;
		this.connection = connection;
	}
	
	public int getTimesliceID() {
		return timesliceID;
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
