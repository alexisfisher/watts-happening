package com.wattshappening.logevents;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import com.wattshappening.db.*;

import com.wattshappening.db.DBManager;

public class BatteryStatusLogger extends LogProcess {

	DBManager dbMan = null;
	IntentFilter ifilter = null;
	BatteryTable batteryTable = null;

	public BatteryStatusLogger(Service parent){
		super(parent, 30000);
		dbMan = DBManager.getInstance(parent);
		batteryTable = new BatteryTable(parent);
		ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
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
		Intent batteryStatus = parent.registerReceiver(null, ifilter);
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
		
		try{
			batteryTable.addEntry(new BatteryInfo(voltage, temp, percentage, scale));
		}catch(Exception e){
			Log.e("BatteryService: ", "Must insert object of type BatteryInfo into the database");
		}
	}
	
}
