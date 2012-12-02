package com.wattshappening.analysis;


/* @author Alexis
 * 
 */
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

import com.wattshappening.db.AppDetailedInfo;

public class AppAnalyzer {
	
	/**
	 * @author 
	 * @param aib A list of AppInfoBat for a single application
	 * @return A double that's related to the usage of the app
	 * 
	 */
	public static long analyzeAppCPU(List<AppDetailedInfo> aib){
		//we're given a list of appinfobat's about a single application
		
		long use = 0;
		int prevtime = 0;
		ListIterator<AppDetailedInfo> aibIter = aib.listIterator();

		if (true == aibIter.hasNext()){
			AppDetailedInfo current = aibIter.next();
			prevtime = current.getTimesliceID();
			long prevCPUPercent = current.getCPU();
			double prevBattLevel = current.getBatteryPercentage(); //the battery level during the last slice
			double battDelta = 0.0; //The change in battery level between slices
			double cpuDelta = 0.0; //How many CPU ticks were used during the last timeslice

			while (aibIter.hasNext()){
				current = aibIter.next();
				if (current.getTimesliceID() == prevtime + 1){

					battDelta = prevBattLevel - current.getBatteryPercentage();
					cpuDelta = current.getCPU() - prevCPUPercent;
					//Log.i("LocalService", "battDelta:"+battDelta + " cpuDelta: "+cpuDelta);
					//TODO: @NBUREK look at this
					use += battDelta * cpuDelta; 	// CPU needs to be a delta 
													// need ticks CPU performed
					
				}
				prevtime = current.getTimesliceID();
				prevCPUPercent = current.getCPU();
				prevBattLevel = current.getBatteryPercentage();

			}
		}
		
		return use;
	}

}
