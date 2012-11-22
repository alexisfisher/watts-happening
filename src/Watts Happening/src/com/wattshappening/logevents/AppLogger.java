package com.wattshappening.logevents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.util.Log;

import com.wattshappening.db.AppInfo;
import com.wattshappening.db.AppInfoTable;

public class AppLogger extends LogProcess {

	AppInfoTable ait = null;
	
	public AppLogger(Service parent){
		super(parent);
		ait = new AppInfoTable(parent);
	}
	
	@Override
	public void startLoggingEvents() {
		
	}

	@Override
	public void stopLoggingEvents() {

	}
	
	private long getPIDTicks(int pid){
		
		String filename = "/proc/" + pid + "/stat";
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			
			String[] contents = bufferedReader.readLine().trim().split(" ");
			
			// Be careful taking a unsigned long and making it into a long!
			
			long utime = Long.parseLong(contents[13]);
			long stime = Long.parseLong(contents[14]);	
			bufferedReader.close();
			return utime + stime;
		}catch(FileNotFoundException e){
			Log.e("AppLogging", e.getMessage());
		}catch(IOException e){
			Log.e("AppLogging", e.getMessage());
		}
		return -1;
	}

	@Override
	public void logInformation(int timesliceID) {
		ActivityManager am = (ActivityManager)parent.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = parent.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		
		
		if(procs != null){
			for(ActivityManager.RunningAppProcessInfo proc : procs){
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
				String name = info.processName;
				
				try {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					name = c.toString();
					//Log.i("AppLogging", c.toString());
				} catch (NameNotFoundException e) {
					// used for testing initially, doesn't really matter to see this now
					//Log.e("AppLogging: ", e.getMessage());
				}				
				int pid = info.pid;
				int uid = info.uid;
				
				long cpu = getPIDTicks(pid);
				long rxBytes = getRXBytes(uid);
				long txBytes = getTXBytes(uid);
				
				if(cpu == -1){
					cpu = 0;
				}
				
				try {
					ait.addEntry(new AppInfo(timesliceID,name, pid, cpu, rxBytes, txBytes));
				} catch (Exception e) {
					Log.e("AppLogging: ", e.getMessage());
				}
			}
		}
	}

	private long getTXBytes(int uid) {
		long data = TrafficStats.getUidTxBytes(uid);
		return (data==TrafficStats.UNSUPPORTED)?0:data;
	}

	private long getRXBytes(int uid) {
		long data = TrafficStats.getUidRxBytes(uid);
		return (data==TrafficStats.UNSUPPORTED)?0:data;
	}

}
