/**
 * 
 */
package com.wattshappening;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.wattshappening.db.GeneralInfoTable;
import com.wattshappening.db.GeneralTimesliceInfo;
import com.wattshappening.logevents.*;

/**
 * @author Nick
 *
 * This service is used to start the main monitoring thread. 
 * It will provide several types of monitoring. The first type 
 * will be periodic checks at scheduled intervals and the 
 * second will be event handlers for when status updates occur.
 */
public class MonitorService extends Service {


    private final long logTimeout = 5000; //1 minute
    private AlarmManager am = null;
    
    //may God have mercy on my soul for making this static so I can access it in the Alarm class
    public static Vector<LogProcess> listOfLogs = new Vector<LogProcess>();
    
	/**
	 * 
	 */
	public MonitorService() {

		//add any needed log processes to the listOfLogs Vector here
		listOfLogs.add((LogProcess)(new HardwareStatusLogger(this)));
		listOfLogs.add((LogProcess)(new BatteryStatusLogger(this)));
		listOfLogs.add((LogProcess)(new GPSLocationLogger(this)));
		listOfLogs.add((LogProcess)(new NetworkStatusLogger(this)));
		listOfLogs.add((LogProcess)(new AppLogger(this)));
	}
	
	/**
	 * 
	 */
	@Override
    public void onCreate() {
		super.onCreate();
		
		
		
		am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
    }

	/**
	 * 
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        super.onStartCommand(intent, flags, startId);

        for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).startLoggingEvents();
        
		//start the timer
        Intent i = new Intent(this, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), logTimeout, pi); // Millisec * Second * Minute
        
        
        return START_STICKY;
    }

    /**
	 * 
	 */
    @Override
    public void onDestroy() {
    	Log.i("LocalService","Received destroy command.");

    	for (int i = 0; i<listOfLogs.size();++i)
        	listOfLogs.get(i).stopLoggingEvents();

    	//stop the timer
    	Intent intent = new Intent(this, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(sender);
    	
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

class Alarm extends BroadcastReceiver
{

    private GeneralInfoTable genInfoTable = null;
    
    boolean isInitialized = false;
    
	public Alarm()
	{
		super();
	}
	
    
	@Override
	public void onReceive(Context context, Intent intent) {
		if (isInitialized == false)
		{
			genInfoTable = new GeneralInfoTable(context);
			isInitialized = true;
		}
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        logInformation(context);

        wl.release();
		
	}
	
	public void logInformation(Context context)
    {
		
    	int timesliceID = genInfoTable.getNextTimesliceID();
    	long timestamp = Calendar.getInstance().getTimeInMillis();
    	
    	//determine if the device is charging
    	Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int isCharging = (plugged== BatteryManager.BATTERY_PLUGGED_AC || 
        				plugged == BatteryManager.BATTERY_PLUGGED_USB)?1:0;

        int ticksUser = 0;
        int ticksSystem = 0;
        int ticksIdle = 0;
        int ticksTotal = 0;
        
        try{
        	String filename = "/proc/stat";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			
			String[] contents = bufferedReader.readLine().trim().split("[ ]+");
			
			// Be careful taking a unsigned long and making it into a long!
			
			ticksUser = Integer.parseInt(contents[1]);
			ticksSystem = Integer.parseInt(contents[3]);
			ticksIdle = Integer.parseInt(contents[4]);
			ticksTotal = ticksUser + ticksSystem + ticksIdle +
						Integer.parseInt(contents[2]) + Integer.parseInt(contents[5]) + 
						Integer.parseInt(contents[6]) + Integer.parseInt(contents[7]);
			
			bufferedReader.close();

		}catch(FileNotFoundException e){
			Log.e("AppLogging", e.getMessage());
		}catch(IOException e){
			Log.e("AppLogging", e.getMessage());
		}
        
    	
    	try {
			genInfoTable.addEntry(new GeneralTimesliceInfo(	timesliceID,
															timestamp,
															isCharging,
															ticksUser,
															ticksSystem,
															ticksIdle,
															ticksTotal));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	for (int i = 0; i<MonitorService.listOfLogs.size(); ++i)
    		MonitorService.listOfLogs.get(i).logInformation(timesliceID);
    }
	
}
