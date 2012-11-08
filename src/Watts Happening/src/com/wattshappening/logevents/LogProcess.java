/**
 * 
 */
package com.wattshappening.logevents;

import android.os.Handler;

/**
 * @author Nick
 *
 */
public abstract class LogProcess {
	
    private Handler h = new Handler();
    private Runnable runMonitor = null;
    
	protected long logTimeout = 5000;
	
	public LogProcess()
	{
		onCreate();
	}
	
	public LogProcess(long timeout)
	{
		logTimeout = timeout;
		onCreate();
	}
	
	private void onCreate()
	{
		runMonitor = new Runnable(){
			public void run() {
				logInformation();
				h.postDelayed(runMonitor, logTimeout);
			}
		};
	}
	
	
	public void startLogging()
	{
		startLoggingEvents();
		h.postDelayed(runMonitor, logTimeout);
	}
	
	public void stopLogging()
	{
		stopLoggingEvents();
		h.removeCallbacks(runMonitor);
	}
	
	protected abstract void startLoggingEvents();
	
	protected abstract void stopLoggingEvents();
	
	protected abstract void logInformation();
	
}
