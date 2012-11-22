/**
 * 
 */
package com.wattshappening.logevents;

import android.app.Service;

/**
 * @author Nick
 *
 */
public abstract class LogProcess {
	
    protected Service parent;
    
	
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
	 * Called from the constructors to setup the Runnable class.
	 * 
	 * @author Nick
	 * 
	 */
	private void onCreate(Service parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Adds the data logging event handlers to the events they 
	 * should be watching.
	 */
	public abstract void startLoggingEvents();
	
	/**
	 * Removes the data logging event handlers to the events 
	 * they should be watching.
	 */
	public abstract void stopLoggingEvents();
	
	/**
	 * Logs any information that should be checked at a set time
	 * interval.
	 */
	public abstract void logInformation(int timeslice);
	
}
