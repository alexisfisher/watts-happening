package com.wattshappening.logevents;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	private HashMap<String, Double> parseTop(){
		BufferedReader in = null;
		HashMap<String, Double> cpuInfo = new HashMap<String, Double>();
		Process process = null;
		try{
			process = Runtime.getRuntime().exec("top -d 1 -n 1 -m 10");
			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while((line = in.readLine()) != null){
				String [] contents = line.trim().split(" ");
				String processName = contents[contents.length - 1];
				//Log.i("Top", line.trim());
				Double usage = 0.0;
				for(String s : contents){
					if(s.indexOf("%") != -1 && s.charAt(0) != 'C'){
						Log.i("top", s.substring(0, s.indexOf("%")));
						usage = Double.parseDouble(s.substring(0, s.indexOf("%")));
						break;
					}
				}
				cpuInfo.put(processName, usage);
			}
		}catch(IOException e){
			Log.e("AppLogging", e.getMessage());
		}finally {
			try{
				in.close();
				process.destroy();
			} catch(IOException e){
				Log.e("AppLogging", e.getMessage());
			}
		}
		return cpuInfo;
	}
	
	private long getPIDTicks(int pid){
		
		String filename = "/proc/" + pid + "/stat";
		try{
			InputStreamReader inputStreamReader = new InputStreamReader(parent.openFileInput(filename));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String[] contents = bufferedReader.readLine().trim().split(" ");
			
			Log.i("PIDTICKS", "Utime: " + contents[13]);
			Log.i("PIDTICKS", "Stime: " + contents[14]);
			
			// Be careful taking a unsigned long and making it into a long!
			
			long utime = Long.parseLong(contents[13]);
			long stime = Long.parseLong(contents[14]);		
			return utime + stime;
		}catch(FileNotFoundException e){
			Log.e("AppLogging", e.getMessage());
		}catch(IOException e){
			Log.e("AppLogging", e.getMessage());
		}
		return -1;
	}

	@Override
	protected void logInformation() {
		ActivityManager am = (ActivityManager)parent.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = parent.getPackageManager();
		List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		
		int timestampID = ait.getNextTimestampID();
		
		if(procs != null){
			HashMap<String, Double> usage = parseTop();
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
				
				long cpu = getPIDTicks(pid);
				if(cpu == -1){
					//Log.e("AppLogging", info.processName + " not found in dumpsys!");
					cpu = 0;
				}
				
				try {
					// for now don't put cpu info in, decide if we want to parse
					// top to figure this out, might be too heavy for what we're doing
					ait.addEntry(new AppInfo(timestampID,name, pid, cpu));
				} catch (Exception e) {
					Log.e("AppLogging: ", e.getMessage());
				}
			}
		}
	}

}
