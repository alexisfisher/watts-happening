package com.wattshappening.logevents;

import com.wattshappening.db.*;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

public class HardwareStatusLogger extends LogProcess {
	
	HardwareTable hTable = null;
	
	WifiManager wifiManager = null;
	LocationManager gpsManager = null;
	PowerManager powerManager = null;
	int lastTimesliceID = -1;

	BroadcastReceiver btBR = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context c, Intent i)
		{
			try {
				//TODO: Come up with a better way to handle this, ideally it would be in a different 
				//table, but I don't want to create a table just for it.
				
				//since this could get called at any time, we'll just associate it with the last timeslice
				hTable.addEntry(new Hardware(lastTimesliceID,"BLUETOOTH",1,"action: Started a Discovery Scan"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public HardwareStatusLogger(Service parent)
	{
		super(parent);
		hTable = new HardwareTable(parent);
		wifiManager = (WifiManager) parent.getSystemService(Context.WIFI_SERVICE);
		gpsManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
		powerManager = (PowerManager) parent.getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public void startLoggingEvents() {
		//If you want to log hardware events, insert them here
		
		//look for bluetooth ACTION_DISCOVERY_STARTED
		parent.registerReceiver(btBR, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
	}

	@Override
	public void stopLoggingEvents() {
		//if logging hardware events then remove the handlers here
		
		parent.unregisterReceiver(btBR);//remove bluetooth
	}

	@Override
	public void logInformation(int timesliceID) {
		lastTimesliceID = timesliceID;
		
		//TODO: Try to determine how to get the list of active wake locks
		
		//Start the WiFi stuff here
		boolean isEnabled = wifiManager.isWifiEnabled();
		String state = "";
		switch (wifiManager.getWifiState())
		{
			case WifiManager.WIFI_STATE_DISABLED:
				state = "Disabled";
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				state = "Disabling";
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				state = "Enabled: " + wifiManager.getConnectionInfo().getSupplicantState().toString();
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				state = "Enabling";
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				state = "Unknown";
				break;
		}
		
		//it would be nice to get all the WiFi locks here as well
		
		try {
			hTable.addEntry(new Hardware(timesliceID, "WIFI", isEnabled?1:0, state));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Start the Bluetooth stuff here
		BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bAdapter != null)
		{
			isEnabled = bAdapter.isEnabled();
			state = "State: ";
			switch (bAdapter.getScanMode())
			{
				case BluetoothAdapter.STATE_OFF:
					state += "off";
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					state += "turning on";
					break;
				case BluetoothAdapter.STATE_ON:
					state += "on";
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					state += "turning off";
					break;
			}
			
			state = ", ScanMode: ";
			switch (bAdapter.getScanMode())
			{
				case BluetoothAdapter.SCAN_MODE_NONE:
					state += "none";
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					state += "turning on";
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					state += "turning off";
					break;
				case BluetoothAdapter.STATE_ON:
					state += "on";
					break;
				case BluetoothAdapter.STATE_OFF:
					state += "none";
					break;
			}
			
			try {
				hTable.addEntry(new Hardware(timesliceID, "BLUETOOTH", isEnabled?1:0, state));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//Start the GPS stuff here
		try {
			hTable.addEntry(new Hardware(timesliceID, "GPS", gpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER)?1:0,	state));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//start the screen stuff here
		try {
			hTable.addEntry(new Hardware(timesliceID, "SCREEN", powerManager.isScreenOn()?1:0, ""));
		} catch (Exception e) {
			
		}
		
	}

}
