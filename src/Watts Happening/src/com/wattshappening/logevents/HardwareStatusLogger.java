package com.wattshappening.logevents;

import com.wattshappening.db.*;

import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiManager;

public class HardwareStatusLogger extends LogProcess {
	
	DBManager dbMan = null;
	
	public HardwareStatusLogger(Service parent)
	{
		super(parent,10000);
		dbMan = new DBManager(parent);
	}

	@Override
	protected void startLoggingEvents() {
		//If you want to log hardware events, insert them here
		
	}

	@Override
	protected void stopLoggingEvents() {
		//if logging hardware events then remove the handlers here

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
	}

}
