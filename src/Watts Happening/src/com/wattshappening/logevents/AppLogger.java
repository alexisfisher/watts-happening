package com.wattshappening.logevents;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.wattshappening.db.AppInfo;
import com.wattshappening.db.AppInfoTable;

public class AppLogger extends LogProcess {

	AppInfoTable ait = null;
	
	public AppLogger(Service parent){
		super(parent, 10000);
		ait = new AppInfoTable(parent);
	}
	
	@Override
	protected void startLoggingEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void stopLoggingEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void logInformation() {
		ActivityManager am = (ActivityManager)parent.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = parent.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		if(procs != null){
			for(ActivityManager.RunningAppProcessInfo proc : procs){
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
				String name = null;
				try {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					name = c.toString();
				} catch (NameNotFoundException e) {
					Log.e("App Logging: ", e.getMessage());
				}				
				int pid = info.pid;
				try {
					// for now don't put cpu info in, decide if we want to parse
					// top to figure this out, might be too heavy for what we're doing
					ait.addEntry(new AppInfo(name, pid, 0));
				} catch (Exception e) {
					Log.e("App Logging: ", e.getMessage());
				}
			}
		}
/*		if(procs != null){
			Iterator<ActivityManager.RunningAppProcessInfo> i = procs.iterator();
			while(i.hasNext()){
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
				String name = null;
				try {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					name = c.toString();
				} catch (NameNotFoundException e) {
					Log.e("App Logging: ", e.getMessage());
				}				
				int pid = info.pid;
				try {
					// for now don't put cpu info in, decide if we want to parse
					// top to figure this out, might be too heavy for what we're doing
					ait.addEntry(new AppInfo(name, pid, 0));
				} catch (Exception e) {
					Log.e("App Logging: ", e.getMessage());
				}
			}
		}*/
	}

}
