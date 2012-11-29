package com.wattshappening.analysis;


/* @author Alexis
 * 
 */
import java.util.List;
import java.util.ListIterator;

//import com.wattshappening.db.AppInfo;
import android.util.Log;

import com.wattshappening.db.AppInfoBat;


public class AppAnalyzer {
	
	/**
	 * @author 
	 * @param aib A list of AppInfoBat for a single application
	 * @return A double that's related to the usage of the app
	 * 
	 */
	public static long analyzeApp(List<AppInfoBat> aib){
		//we're given a list of appinfobat's about a single application
		
		long use = 0;
		int prevtime = 0;
		ListIterator<AppInfoBat> aibIter = aib.listIterator();

		if (true == aibIter.hasNext()){
			AppInfoBat current = aibIter.next();
			prevtime = current.getTimesliceID();
			long prevCPUPercent = current.getCPU();
			double prevBattLevel = current.getBatteryPercentage(); //the battery level during the last slice
			double battDelta = 0.0; //The change in battery level between slices
			double cpuDelta = 0.0; //How many CPU ticks were used during the last tick

			while (aibIter.hasNext()){
				current = aibIter.next();
				if (current.getTimesliceID() == prevtime + 1){

					battDelta = prevBattLevel - current.getBatteryPercentage();
					cpuDelta = current.getCPU() - prevCPUPercent;
					//Log.i("LocalService", "battdelta:"+battDelta + " cpuDelta: "+cpuDelta);
					use += battDelta * cpuDelta; 	//does CPU need to be a delta, as well? 
					//... yes  
				}
				prevtime = current.getTimesliceID();
				prevCPUPercent = current.getCPU();
				prevBattLevel = current.getBatteryPercentage();

			}
		}
		//store in DB
		return use;
	}

}
