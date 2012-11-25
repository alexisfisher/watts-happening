/**
 * 
 */
package com.wattshappening;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.wattshappening.db.GeneralInfoTable;
import com.wattshappening.db.GeneralTimesliceInfo;
import com.wattshappening.logevents.*;

/**
 * @author Nick
 *
 * This service is used to start the main monitoring thread. 
 * It will provide several types of monitoring. The first type 
 * will be periodic checks at scheduled intervals and the 
 * second will be event handlers for when status updates occur.
 */
public class MonitorService extends Service {

    Vector<LogProcess> listOfLogs = new Vector<LogProcess>();

    private final long logTimeout = 5000;
    private Handler h = new Handler();
    private Runnable runMonitor = null;
    private GeneralInfoTable genInfoTable = null;
    
	/**
	 * 
	 */
	public MonitorService() {
	}
	
	/**
	 * 
	 */
	@Override
    public void onCreate() {
		super.onCreate();
		
		//add any needed log processes to the listOfLogs Vector here
		listOfLogs.add((LogProcess)(new HardwareStatusLogger(this)));
		listOfLogs.add((LogProcess)(new BatteryStatusLogger(this)));
		listOfLogs.add((LogProcess)(new GPSLocationLogger(this)));
		listOfLogs.add((LogProcess)(new NetworkStatusLogger(this)));
		listOfLogs.add((LogProcess)(new AppLogger(this)));
		
		genInfoTable = new GeneralInfoTable(this);
		
		runMonitor = new Runnable(){
			public void run() {
				logInformation();
				h.postDelayed(runMonitor, logTimeout);
			}
		};
    }

	/**
	 * 
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        super.onStartCommand(intent, flags, startId);

        for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).startLoggingEvents();
        
		h.postDelayed(runMonitor, logTimeout);
        
        return START_STICKY;
    }

    /**
	 * 
	 */
    @Override
    public void onDestroy() {
    	Log.i("LocalService","Received destroy command.");

    	for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).stopLoggingEvents();
    	
    	h.removeCallbacks(runMonitor);
    	
    	super.onDestroy();

    }
    
    public void logInformation()
    {
    	int timesliceID = genInfoTable.getNextTimesliceID();
    	String timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
    	
    	//determine if the device is charging
    	Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int isCharging = (plugged== BatteryManager.BATTERY_PLUGGED_AC || 
        				plugged == BatteryManager.BATTERY_PLUGGED_USB)?1:0;
    	
    	try {
			genInfoTable.addEntry(new GeneralTimesliceInfo(timesliceID,timestamp,isCharging));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	for (int i = 0; i<listOfLogs.size(); ++i)
    		listOfLogs.get(i).logInformation(timesliceID);
    }

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
}
