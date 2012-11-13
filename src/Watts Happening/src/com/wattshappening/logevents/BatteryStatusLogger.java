package com.wattshappening.logevents;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.wattshappening.db.DBManager;

public class BatteryStatusLogger extends LogProcess {

	DBManager dbMan = null;
	Intent batteryStatus = null;

	public BatteryStatusLogger(Service parent){
		super(parent, 10000);
		dbMan = DBManager.getInstance(parent);
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		this.batteryStatus = parent.registerReceiver(null, ifilter);
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
	protected void logInformation(){	
		int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
		int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		double percentage = 0;
		if(level != -1 && scale != -1){
			percentage = level / scale;
		}
		else {
			Log.e("BatteryService: ", "Can't calculate percentage, don't have level or scale");
		}
		
		// Add the information to the database, not going to code it yet since who knows
		// how this is going to work at the moment.
	}
	
}
