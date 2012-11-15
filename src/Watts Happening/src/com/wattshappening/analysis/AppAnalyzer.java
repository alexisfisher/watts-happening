package com.wattshappening.analysis;


/* @author Alexis
 * 
 */
import java.util.List;
//import java.util.Iterator;
import java.util.ListIterator;

import com.wattshappening.db.AppInfo;
//import com.wattshappening.db.AppInfoTable;
import com.wattshappening.db.BatteryInfo;
//import com.wattshappening.db.BatteryTable;

public class AppAnalyzer extends Analyzer {

	/*
	 * in: list of appinfo objects db entries <ts,cpu%> for a single app; battery <ts, %>
	 * 
	 */
	private double getBattery(double currtime, List<BatteryInfo> battinfo){
		boolean go = true;
		ListIterator<BatteryInfo> bi = battinfo.listIterator();
		BatteryInfo currb = bi.next();
		double prevtimediff = 100.0;
		double timediff = 0.0;
		while (go){
			Double batttime = Double.valueOf(currb.getTimestamp());
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
	public double analyzeApp(List<AppInfo> appinfo, List<BatteryInfo> battinfo){
		Double use = 0.0;
		//keep a running time diff
		Double prevtime;
		Double currtime;
		ListIterator<AppInfo> ai = appinfo.listIterator();
		ListIterator<BatteryInfo> bi = battinfo.listIterator();
		AppInfo curra;
		
		//initialize by grabbing first appinfo
		curra = (AppInfo) ai.next();
		prevtime =  Double.valueOf(curra.getTimestamp());
		currtime = prevtime;
		double cpuPercent = curra.getCPU();
		Double prevbattery = getBattery(currtime, battinfo);
		Double currbattery = prevbattery;
		
		while(ai.hasNext()){
			//get next appinfo
			curra = ai.next(); 
			//now we grab the cpu %, weights our claim. 
			cpuPercent = curra.getCPU();
			currtime = Double.valueOf(curra.getTimestamp());
			currbattery = getBattery(currtime, battinfo);
			battDelta = prevbattery - currbattery; 
			use += battDelta * cpuPercent;
			
			prevbattery = currbattery;
			
		}
		
		return use;
	}
}
