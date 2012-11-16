package com.wattshappening.logevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
		super(parent, 5000);
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
	
	private HashMap<String, Double> parseDumpSys(){
		BufferedReader in = null;
		HashMap<String, Double> cpuInfo = new HashMap<String, Double>();
		Process process = null;
		try{
			process = Runtime.getRuntime().exec("dumpsys cpuinfo");
			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while((line = in.readLine()) != null){
				String [] contents = line.split(" ");
				String processName = contents[1].substring(contents[1].indexOf('/') + 1);
				for(String s : contents){
					Log.i("AppLogging", "Contents: " + s);
				}
				Double usage = Double.parseDouble(contents[0].substring(0, contents[0].indexOf('%')));
				cpuInfo.put(processName, usage);
			}
			
		}catch(IOException e){
			Log.e("AppLoggingDumpSys", e.getMessage());
		}finally {
			try{
				in.close();
				process.destroy();
			} catch(IOException e){
				Log.e("AppLoggingDumpSys", e.getMessage());
			}
		}
		
		return cpuInfo;
	}

	@Override
	protected void logInformation() {
		ActivityManager am = (ActivityManager)parent.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = parent.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		if(procs != null){
			HashMap<String, Double> usage = parseDumpSys();
			for(ActivityManager.RunningAppProcessInfo proc : procs){
				ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
				String name = info.processName;
				
				try {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					name = c.toString();
					Log.i("AppLogging", c.toString());
				} catch (NameNotFoundException e) {
					// used for testing initially, doesn't really matter to see this now
					//Log.e("AppLogging: ", e.getMessage());
				}				
				int pid = info.pid;
				
				Double cpu = usage.get(info.processName);
				if(cpu == null){
					Log.e("AppLogging", info.processName + " not found in dumpsys!");
					cpu = 0.0;
				}
				
				try {
					// for now don't put cpu info in, decide if we want to parse
					// top to figure this out, might be too heavy for what we're doing
					ait.addEntry(new AppInfo(name, pid, cpu));
				} catch (Exception e) {
					Log.e("AppLogging: ", e.getMessage());
				}
			}
		}
	}

}
