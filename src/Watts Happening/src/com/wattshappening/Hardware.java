package com.wattshappening;

import java.util.Calendar;
import java.sql.Timestamp;

public class Hardware {

	int _id;
	//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private Date timestamp;
	private String timestamp;
	private String name;
	private int enabled;
	private String status;
	
	/* Use this constructor when creating new entries to be put into the database */
	public Hardware(String name, int enabled, String status){
		this.timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		this.name = name;
		this.status = status;
		/* force enabled to be either 1 or 0 */
		// TODO throw exception instead of just exiting
		if(enabled == 0 || enabled == 1){
			this.enabled = enabled;
		}
		else{
			System.err.println("Enabled must be either 1 or 0.");
			System.exit(1);
		}
	}
	
	/* Use this constructor when returning all rows from a database */
	public Hardware(String timestamp, String name, int enabled, String status){
		this.timestamp = timestamp;
		this.name = name;
		this.status = status;
		/* force enabled to be either 1 or 0 */
		// TODO throw exception instead of just exiting
		if(enabled == 0 || enabled == 1){
			this.enabled = enabled;
		}
		else{
			System.err.println("Enabled must be either 1 or 0.");
			System.exit(1);
		}
	}
	
	public String getTimestamp(){
		return timestamp;
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
