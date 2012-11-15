package com.wattshappening.logevents;

import com.wattshappening.db.GPSTable;
import com.wattshappening.db.Gps;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

public class GPSLocationLogger extends LogProcess {

	private LocationManager locationManager;
	private GPSTable gpsT = null;
	
	// Define a listener that responds to location updates
    private LocationListener locationListener = new LocationListener() { 
        public void onLocationChanged(Location location) {
        	
        	try {
				gpsT.addEntry(new Gps(location.getLatitude(),location.getLongitude()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };
    
	public GPSLocationLogger(Service parent) {
		super(parent,60000);
		locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
		gpsT = new GPSTable(parent);
	}

	public GPSLocationLogger(Service parent, long timeout) {
		super(parent, timeout);
		locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
		gpsT = new GPSTable(parent);
	}

	@Override
	protected void startLoggingEvents() {
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);

	}

	@Override
	protected void stopLoggingEvents() {
		locationManager.removeUpdates(locationListener);
		
	}

	@Override
	protected void logInformation() {
		// TODO Auto-generated method stub

	}

}
