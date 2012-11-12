package com.wattshappening.logevents;

public class HardwareStatusLogger extends LogProcess {
	
	public HardwareStatusLogger()
	{
		super(10000);
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
		//Any logging that occurs at a fixed interval goes here

	}

}
