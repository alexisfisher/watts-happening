/**
 * 
 */
package com.wattshappening;


import java.util.Vector;

import android.R.string;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.wattshappening.db.DBManager;
import com.wattshappening.db.Gps;
import com.wattshappening.logevents.*;

/**
 *
 * This service is used to start the GPS monitoring thread. 
 * 
 */
public class GPSService extends Service {

	public GPSService() {
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
	    LocationListener locationListener = new LocationListener() { 
	        public void onLocationChanged(Location location) {
	        	DBManager.addGPSCoordinate(location);
	        }

	        public void onStatusChanged(String provider, int status, Bundle extras) {}

	        public void onProviderEnabled(String provider) {}

	        public void onProviderDisabled(String provider) {}
	    };
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
	    // Register the listener with the Location Manager to receive location updates
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	@Override
    public void onCreate() {
		Log.i("LocalService", "Service Created");
		super.onCreate();
		
		//add any needed log processes to the listOfLogs Vector here
		
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
        	listOfLogs.get(i).startLogging();
    	
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
