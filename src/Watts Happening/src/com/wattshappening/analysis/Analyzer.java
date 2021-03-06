package com.wattshappening.analysis;


import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.wattshappening.db.AppDetailedInfo;
import com.wattshappening.db.DBManager;
import com.wattshappening.db.AggregateAppInfoTable;
import com.wattshappening.db.AggregateAppInfo;
import com.wattshappening.db.GeneralInfoTable;
/* 
 * @author Alexis
 */
import com.wattshappening.db.GeneralTimesliceInfo;

public class Analyzer{
	private AggregateAppInfoTable aggTable = null;
	private GeneralInfoTable genInfoTable = null;
	
	public Analyzer(){
		
	}
	
	
	/*
	 * When called, should cycle through all apps in the currently running list
	 */
	public void runAnalysis(Context con){
		DBManager db =  DBManager.getInstance(con);
		
		String uidList[] = new String[0];
		uidList = db.getFreshUIDList().toArray(uidList);
		
		if (aggTable == null)
			aggTable = new AggregateAppInfoTable(con);
		if (genInfoTable == null)
			genInfoTable = new GeneralInfoTable(con);
		
		
		if(uidList.length>0){
			
			//for every running app
			for(String uidStr : uidList){
				
				//get the apps info
				int uid = Integer.parseInt(uidStr);
				

				double cpuUsage = 0.0;
				int numUpdates = 0;
				double networkUsage = 0;

				//fetch the old information for this application
				AggregateAppInfo oldInfo = aggTable.fetchMostRecent(uid);
				if (oldInfo != null) //if there was a previous entry for this app
				{
					numUpdates = oldInfo.getNumUpdates();
					cpuUsage = oldInfo.getHistoricCPU() * numUpdates;
					networkUsage = oldInfo.getHistoricNetwork() * numUpdates;
				}


				
				Vector<AppDetailedInfo> appHist = db.getAppInfo(uid); //fetch the appHistory that hasn't been analyzed yet
				HashMap<Integer,GeneralTimesliceInfo> timesliceInfo = genInfoTable.fetchAllNewEntries();
				
				//ignore the last entry for now
				for (int i = 0; i<appHist.size()-1; ++i)
				{
					//only include this one if the app has a data point for the timeslice right after it too
					if ((appHist.get(i).getTimesliceID()+1) == appHist.get(i+1).getTimesliceID())
					{
						//we should always have timeslice data for both of these, but just in case
						if (timesliceInfo.containsKey(appHist.get(i).getTimesliceID()) && timesliceInfo.containsKey(appHist.get(i+1).getTimesliceID()))
						{
							GeneralTimesliceInfo t1 = timesliceInfo.get(Integer.valueOf(appHist.get(i).getTimesliceID()));
							AppDetailedInfo a1 = appHist.get(i);
							AppDetailedInfo a2 = appHist.get(i+1);
							GeneralTimesliceInfo t2 = timesliceInfo.get(Integer.valueOf(appHist.get(i+1).getTimesliceID()));
							
							//This will update the CPU usage information to include this timeslice
							cpuUsage += (a2.getCPU() - a1.getCPU())/(t2.getTicksTotal()-t1.getTicksTotal());
							
							//This will update the network usage/time information to include this timeslice
							networkUsage += ((a2.getTXBytes()+a2.getRXBytes()) - (a1.getTXBytes()+a1.getRXBytes()) / (t2.getTimestamp()-t1.getTimestamp())*1000);
							
							++numUpdates;
						}
					}
				}
				
				AggregateAppInfo aggInfo = new AggregateAppInfo(uid, (cpuUsage/numUpdates), (networkUsage/numUpdates),0, numUpdates);

				try {
					aggTable.addEntry(aggInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.i("LocalService", "App "+ name + " has usage value " +cpuUsage/numUpdates);
			}
			
			
			genInfoTable.markAllEntriesRead();

		}
		
		double time = TimeLeft.getTimeLeft(con);		
		Toast.makeText(con, "Minutes left: " + time, Toast.LENGTH_LONG).show();
		Log.i("TimeLeft" , "Minutes: " + time);
	}
		
}
