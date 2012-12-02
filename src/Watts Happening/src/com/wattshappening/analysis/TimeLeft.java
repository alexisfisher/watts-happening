package com.wattshappening.analysis;

import java.util.Vector;

import android.content.Context;
import android.util.Log;

import com.wattshappening.db.BatteryTime;
import com.wattshappening.db.DBManager;

public class TimeLeft {
	
	/**
	 * 
	 * @param context
	 * @return On success, the minutes left. When phone is charging, Double.MAX_VALUE (my way of trying
	 *     to say infinity). On error, -1 since there wasn't enough data to make the determination.
	 */
	public static double getTimeLeft(Context context){
		
		// get long term average usage per minute
		// get short term average usage per minute (over the last 10 minutes)
		// weight them 80/20 to get usage/time
		
		double weightLong = .8;
		double weightShort = .2;
		
		int shortTermMinutes = 5; // number of minutes to consider for short term info
		int msInMin = 60000; // milliseconds in a minute
		DBManager db = DBManager.getInstance(context);		
		Vector<BatteryTime> results = db.getBatteryInformation(); 
		
		if(results.size() < 3){
			Log.e("TimeLeft", "No data to make time left determination");
			return -1;
		}
		
		// Only look at data from the last time the device was plugged in
		
		
		int startIndex = 0;
		for(int i = results.size() - 1; i >= 0; i--){
			if(results.get(i).getIsCharging() == 1){
				if(i == results.size() -1){
					startIndex = i;
					Log.i("TimeLeft", "Phone is charging as of last timestamp");
					return Double.MAX_VALUE;
				}
				else{	
					startIndex = i + 1;
				}
				break;
			}
		}
		
		Log.i("TimeLeft", "StartIndex: " + startIndex);
		
		double longTermTime = results.get(results.size() - 1).getTimestamp() - 
				results.get(startIndex).getTimestamp();
		
		double longTermUsage = results.get(startIndex).getPercentage() - 
				results.get(results.size() - 1).getPercentage();
		
		startIndex = -1;
		for(int i = results.size() - 2; i >= 0; i--){
			if((results.get(results.size() -1).getTimestamp() - results.get(i).getTimestamp()) > 
					(shortTermMinutes * msInMin)){
				startIndex = i + 1;
				break;
			}
		}
		
		if(startIndex == -1){
			Log.e("TimeLeft", "Not " + shortTermMinutes + " minutes worth of data yet!");
			Log.e("TimeLeft", "Using all available data instead");
			startIndex = 0;
		}
		else if(startIndex == results.size() - 1){
			// the second to last result is more than the short term threshold
			// before the last result. Therefore, we should just
			// use the second to last result anyway
			startIndex = results.size() - 2;
		}
		
		double shortTermTime = results.get(results.size() - 1).getTimestamp() - 
				results.get(startIndex).getTimestamp();
		
		double shortTermUsage = results.get(startIndex).getPercentage() - 
				results.get(results.size() - 1).getPercentage();
		
		double percentPerMillisecond = (weightLong * (longTermUsage / longTermTime)) + 
				(weightShort * (shortTermUsage / shortTermTime));
			
		Log.i("TimeLeft", "longTermUsage / longTermTime = " + longTermUsage + " / " + longTermTime + " = " + longTermUsage / longTermTime);
		Log.i("TimeLeft", "shortTermUsage / shortTermTime = " + shortTermUsage + " / " + shortTermTime + " = " + shortTermUsage / shortTermTime);
		
		// battery percentage / percentPerMillisecond		
		double millisecLeft = results.get(results.size() - 1).getPercentage() / percentPerMillisecond;
		
		Log.i("TimeLeft" , "Milliseconds Left: " + millisecLeft);
		// flip to minutes and return
		return millisecLeft / msInMin;
	}

}
