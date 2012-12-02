package com.wattshappening.analysis;


import java.util.List;

//import android.app.Service;
import android.app.ActivityManager;
//import android.app.Service;
import android.content.Context;
//import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
//import android.os.IBinder;
import android.util.Log;

import com.wattshappening.analysis.AppAnalyzer;
import com.wattshappening.db.DBManager;
import com.wattshappening.db.AggregateAppInfoTable;
import com.wattshappening.db.AggregateAppInfo;
/* 
 * @author Alexis
 */

public class Analyzer{
	private int timeslices = 30;
	private AggregateAppInfoTable aggTable = null;
	
	public Analyzer(){
		//aggTable = new AggregateAppInfoTable();
		onCreate();
	}
	
	public void onCreate() {

	}
	
	
	/*
	 * When called, should cycle through all apps in the currently running list
	 */
	public void runAnalysis(Context con){
		ActivityManager am = (ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = con.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		DBManager db =  DBManager.getInstance(con);
		aggTable = new AggregateAppInfoTable(con);
		
		
		if(procs != null){
			
			//for every running app
			for(ActivityManager.RunningAppProcessInfo proc : procs){
				
				//get the apps info
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
				String name = info.processName;
				int uid = info.uid;

//				try {
//					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
//							info.processName, PackageManager.GET_META_DATA));
//					name = c.toString();
//					//Log.i("AppLogging", c.toString());
//
//				} catch (NameNotFoundException e) {
//
//				}

				//TODO: getAppInfo should probably be based on UID instead of name - Nick
				//timeslices should be configurable but assume 30 is good for now
				long cpuout = AppAnalyzer.analyzeApp(db.getAppInfo(uid, timeslices));
				AggregateAppInfo aggInfo = new AggregateAppInfo(uid, cpuout, 0,0,0);

				//aggTable.addEntry(aggInfo);
				Log.i("LocalService", "App "+ name + " has usage value " +cpuout);
			}

		}
		double time = TimeLeft.getTimeLeft(con);
		Log.i("TimeLeft" , "Minutes: " + time);
	}
		
}