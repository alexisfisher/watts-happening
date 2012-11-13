/**
 * 
 */
package com.wattshappening;


import java.util.Vector;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
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
		Log.i("LocalService", "Service Created");
		super.onCreate();
		
		//add any needed log processes to the listOfLogs Vector here
		listOfLogs.add((LogProcess)(new HardwareStatusLogger(this)));
		listOfLogs.add((LogProcess)(new BatteryStatusLogger(this)));
		listOfLogs.add((LogProcess)(new GPSLocationLogger(this)));
		listOfLogs.add((LogProcess)(new NetworkStatusLogger(this)));
    }

	/**
	 * 
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        super.onStartCommand(intent, flags, startId);

        for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).startLogging();
        
        return START_STICKY;
    }

    /**
	 * 
	 */
    @Override
    public void onDestroy() {
    	Log.i("LocalService","Received destroy command.");

    	for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).stopLogging();
    	
    	super.onDestroy();

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
