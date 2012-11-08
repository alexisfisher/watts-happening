/**
 * 
 */
package com.wattshappening;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Nick
 *
 * This service is used to start the main monitoring thread. 
 * It will provide several types of monitoring. The first type 
 * will be periodic checks at scheduled intervals and the 
 * second will be event handlers for when status updates occur.
 */
public class MonitorService extends Service {

    Handler h = new Handler();
    Runnable runMonitor = null;
	/**
	 * 
	 */
	public MonitorService() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	@Override
    public void onCreate() {
		Log.i("LocalService", "Service Created");
		super.onCreate();
		
		if (runMonitor == null)
			runMonitor = new Runnable(){
				public void run() {
					// TODO Auto-generated method stub
					Log.i("LocalService","Received singal to Run the monitor logging thread.");
					h.postDelayed(runMonitor, 5000);
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
        
        h.postDelayed(runMonitor, 5000);
        
        return START_STICKY;
    }

    /**
	 * 
	 */
    @Override
    public void onDestroy() {
    	Log.i("LocalService","Received destroy command.");
    	h.removeCallbacks(runMonitor);
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
