package com.wattshappening.logevents;

import com.wattshappening.db.*;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

public class HardwareStatusLogger extends LogProcess {
	
	DBManager dbMan = null;

	BroadcastReceiver btBR = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context c, Intent i)
		{
			//TODO: Log the bluetooth event
		}
	};
	
	public HardwareStatusLogger(Service parent)
	{
		super(parent,10000);
		dbMan = DBManager.getInstance(parent);
	}

	@Override
	protected void startLoggingEvents() {
		//If you want to log hardware events, insert them here
		
		//look for bluetooth ACTION_DISCOVERY_STARTED
		parent.registerReceiver(btBR, 
								new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
		);
	}

	@Override
	protected void stopLoggingEvents() {
		//if logging hardware events then remove the handlers here
		
		parent.unregisterReceiver(btBR);//remove bluetooth
	}

	@Override
	protected void logInformation() {
		
		//TODO: Add bluetooth logging
		//TODO: Add gps hardware logging
		//TODO: Add screen state logging
		
		//Start the WiFi stuff here
		WifiManager wifiManager = (WifiManager) parent.getSystemService(Context.WIFI_SERVICE);
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
		
		dbMan.addHardware(new Hardware("WIFI", isEnabled?1:0, state));
		
		
		//Start the Bluetooth stuff here
		BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
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
		
		dbMan.addHardware(new Hardware("BLUETOOTH", isEnabled?1:0, state));
		
	}

}
