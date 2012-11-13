package com.wattshappening;
/*
 *  * @author afisher
 *   */

public class Battery {
	
	//	@Override
	//	public void onCreate() {
	//	//	    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
	//	//	        int scale = -1;
	//	//	        int level = -1;
	//	//	        int voltage = -1;
	//	//	        int temp = -1;
	//	//	        @Override
	//	//	        public void onReceive(Context context, Intent intent) {
	//	//	            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	//	//	            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	//	//	            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
	//	//	            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
	//	//	            Log.e("BatteryManager", "level is "+level+"/"+scale+", temp is "+temp+", voltage is "+voltage);
	//	//	        }
	//	//	    };
	//	//	    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	//	//	    registerReceiver(batteryReceiver, filter);
	//	//	}
	public int getLevel() {
		return level;
	}
	//					//	public void setLevel(int level) {
	//					//		this.level = level;
	//					//	}
	public int getTemp() {
		return temp;
	}
	//	public void setTemp(int temp) {
	//		this.temp = temp;
	//	}
	private int getVolt() {
		return volt;
	}
	//													//	private void setVolt(int volt) {
	//													//		this.volt = volt;
	//													//	}
	//													//	
}

