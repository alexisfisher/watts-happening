package com.wattshappening.analysis;


/* @author Alexis
 * 
 */
import java.util.List;
import java.util.ListIterator;

//import com.wattshappening.db.AppInfo;
import com.wattshappening.db.AppInfoBat;


public class AppAnalyzer {
	
	public double analyzeApp(List<AppInfoBat> aib){
		//we're given a list of appinfobat's about a single application
		
		double use = 0.0;
		int prevtime = 0;
		ListIterator<AppInfoBat> aibIter = aib.listIterator();
		AppInfoBat current = aibIter.next();
		prevtime = current.getTimesliceID();
		long prevCPUPercent = current.getCPU();
		double prevBattLevel = current.getBatteryPercentage();
		double battDelta = 0.0;
		double cpuDelta = 0.0;
		
		while (aibIter.hasNext()){
			current = aibIter.next();
			if (current.getTimesliceID() == prevtime + 1){
			
				battDelta = prevBattLevel - current.getBatteryPercentage();
				cpuDelta = current.getCPU() - prevCPUPercent;
				use += battDelta * cpuDelta; 	//does CPU need to be a delta, as well? 
														//... yes  
			}
			prevtime = current.getTimesliceID();
			prevCPUPercent = current.getCPU();
			prevBattLevel = current.getBatteryPercentage();
			
		}
		
		//store in DB
		return use;
	}

}
