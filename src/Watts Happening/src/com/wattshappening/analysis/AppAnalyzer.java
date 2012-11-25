package com.wattshappening.analysis;


/* @author Alexis
 * 
 */
import java.util.List;
import java.util.ListIterator;

import com.wattshappening.db.AppInfo;
import com.wattshappening.db.BatteryInfo;


public class AppAnalyzer extends Analyzer {

	private double getBattery(double currtime, List<BatteryInfo> battinfo){
		boolean go = true;
		ListIterator<BatteryInfo> bi = battinfo.listIterator();
		BatteryInfo currb = bi.next();
		double prevtimediff = 100.0;
		double timediff = 0.0;
		while (go){
			Double batttime = Double.valueOf(currb.getTimesliceID());
			timediff = currtime - batttime;
			if (prevtimediff < timediff) {
				go = false;
				currb = bi.previous(); //rewinds iterator
			}else{
				prevtimediff = timediff;
				currb = bi.next();
			}
		}
		
		return currb.getPercentage(); 
	}
	
	/*
	 * in: list of appinfo objects db entries <ts,cpu%> for a single app; battery <ts, %>
	 * 
	 */

	public double analyzeApp(List<AppInfo> appinfo, List<BatteryInfo> battinfo){
		Double use = 0.0;
		//keep a running time diff
		Double prevtime;
		Double currtime;
		ListIterator<AppInfo> ai = appinfo.listIterator();
		//ListIterator<BatteryInfo> bi = battinfo.listIterator();
		AppInfo curra;
		
		//initialize by grabbing first appinfo
		curra = (AppInfo) ai.next();
		prevtime =  Double.valueOf(curra.getTimesliceID());
		currtime = prevtime;
		double cpuPercent = curra.getCPU();
		Double prevbattery = getBattery(currtime, battinfo);
		Double currbattery = prevbattery;
		Double battDelta = 0.0;
		
		while(ai.hasNext()){
			// get next appinfo
			// need to check if time slices are adjacent ! as currently written,
			// if app is started only @ beginning & end it will still look like app
			// is battery-heavy. 
			curra = ai.next(); 
			//now we grab the cpu %, weights our claim. 
			cpuPercent = curra.getCPU();
			currtime = Double.valueOf(curra.getTimesliceID());
			currbattery = getBattery(currtime, battinfo);
			battDelta = prevbattery - currbattery; 
			use += battDelta * cpuPercent;
			
			prevbattery = currbattery;
			
		}
		
		return use;
	}
}
