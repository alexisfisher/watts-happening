package com.wattshappening.analysis;

import java.util.Vector;

import android.content.Context;
import android.util.Log;

import com.wattshappening.db.BatteryTime;
import com.wattshappening.db.DBManager;

public class TimeLeft {
	
	public static int getTimeLeft(Context context){
		
		// join sql to get all the battery information
		// get long term average usage per minute
		// get short term average usage per minute (over the last 10 minutes)
		// weight them 90/10 at first (probably more like 80/20)
		
		DBManager db = DBManager.getInstance(context);		
		Vector<BatteryTime> results = db.getBatteryInformation(); 
		
		
		return 0;
	}

}
