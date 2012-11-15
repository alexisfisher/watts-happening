/**
 * 
 */
package com.wattshappening.logevents;

import android.app.Service;
import android.os.Handler;

/**
 * @author Nick
 *
 */
public abstract class LogProcess {
	
    private Handler h = new Handler();
    private Runnable runMonitor = null;
    protected Service parent;
    
	protected long logTimeout = 120000;
	
	/**
	 * Initializes the LogProcess class and sets the default timeout to
	 * 5000 milliseconds.
	 * @author Nick
	 * 
	 */
	public LogProcess(Service parent)
	{
		onCreate(parent);
	}
	
	/**
	 * Initializes the LogProcess class and sets the default timeout to
	 * the time provided.
	 * @author Nick
	 * @param timeout - The amount of time (in milliseconds) to wait between
	 * logging information. 
	 */
	public LogProcess(Service parent, long timeout)
	{
		logTimeout = timeout;
		onCreate(parent);
	}
	
	/**
	 * Called from the constructors to setup the Runnable class.
	 * 
	 * @author Nick
	 * 
	 */
	private void onCreate(Service parent)
	{
		this.parent = parent;
		runMonitor = new Runnable(){
			public void run() {
				logInformation();
				h.postDelayed(runMonitor, logTimeout);
			}
		};
	}
	
	/**
	 * Starts the logging of events and time based data logging.
	 * @author Nick
	 */
	public void startLogging()
	{
		startLoggingEvents();
		h.postDelayed(runMonitor, logTimeout);
	}
	
	/**
	 * Stops the logging of events and time based data logging.
	 * @author Nick
	 */
	public void stopLogging()
	{
		stopLoggingEvents();
		h.removeCallbacks(runMonitor);
	}
	
	/**
	 * Adds the data logging event handlers to the events they 
	 * should be watching.
	 */
	protected abstract void startLoggingEvents();
	
	/**
	 * Removes the data logging event handlers to the events 
	 * they should be watching.
	 */
	protected abstract void stopLoggingEvents();
	
	/**
	 * Logs any information that should be checked at a set time
	 * interval.
	 */
	protected abstract void logInformation();
	
}
