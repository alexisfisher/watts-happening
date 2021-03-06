package com.wattshappening.logevents;

import com.wattshappening.db.GPSTable;
import com.wattshappening.db.Gps;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
		super(parent);
		locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
		gpsT = new GPSTable(parent);
	}
	
	@Override
	public void startLoggingEvents() {
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);

	}

	@Override
	public void stopLoggingEvents() {
		locationManager.removeUpdates(locationListener);
		
	}

	@Override
	public void logInformation(int timesliceID) {
		// TODO Auto-generated method stub

	}

}
