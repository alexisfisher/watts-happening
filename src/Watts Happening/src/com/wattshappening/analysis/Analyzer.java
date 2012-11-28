package com.wattshappening.analysis;


import java.util.List;

import android.app.Service;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.util.Log;

import com.wattshappening.analysis.AppAnalyzer;
import com.wattshappening.db.DBManager;
/* 
 * @author Alexis
 */
//public class Analyzer extends Service{
public class Analyzer{
	private int timeslices = 30;
	protected Service parent;
	
	public Analyzer(Service parent){
		onCreate(parent);
	}
	
	public void onCreate(Service parent) {
		super.onCreate();
		this.parent = parent;
		this.runAnalysis();
	}
	
	
	/*
	 * When called, should cycle through all apps in the currently running list
	 */
	public void runAnalysis(){
		ActivityManager am = (ActivityManager)parent.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = parent.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		DBManager db =  DBManager.getInstance(parent.getBaseContext());
		if(procs != null){
			
			//for every running app
			for(ActivityManager.RunningAppProcessInfo proc : procs){
				
				//get the apps info
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
				String name = info.processName;
				int uid = info.uid;

				try {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					name = c.toString();
					//Log.i("AppLogging", c.toString());

				} catch (NameNotFoundException e) {

				}

				//TODO: getAppInfo should probably be based on UID instead of name - Nick
				//timeslices should be configurable but assume 30 is good for now
				double out = AppAnalyzer.analyzeApp(db.getAppInfo(uid, timeslices));
				Log.i("LocalService", "App "+ name + " has usage value " +out);
			}

		}
	}
		
}