package com.wattshappening.db;

import java.util.Calendar;
import java.sql.Timestamp;

public class Hardware extends DBEntry{

	int _id;
	//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private Date timestamp;
	private int timesliceID;
	private String name;
	private int enabled;
	private String status;
	
	/* Use this constructor when returning all rows from a database */
	public Hardware(int timesliceID, String name, int enabled, String status){
		this.timesliceID = timesliceID;
		this.name = name;
		this.status = status;
		/* force enabled to be either 1 or 0 */
		if(enabled == 0 || enabled == 1){
			this.enabled = enabled;
		}
		else{
			System.err.println("Enabled must be either 1 or 0.");
			System.exit(1);
		}
	}
	
	public int getTimesliceID(){
		return timesliceID;
	}
	
	public String getName(){
		return name;
	}
	
	public int getEnabled(){
		return enabled;
	}
	
	public String getStatus(){
		return status;
	}
}
