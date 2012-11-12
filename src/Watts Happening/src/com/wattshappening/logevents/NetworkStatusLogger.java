package com.wattshappening.logevents;

import com.wattshappening.db.DBManager;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusLogger extends LogProcess {

	private DBManager dbMan = null;
	private ConnectivityManager cM = null;
	
	public NetworkStatusLogger(Service parent) {
		super(parent,30000);
		cM = ((ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE));
		dbMan = new DBManager(parent);
	}

	@Override
	protected void startLoggingEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void stopLoggingEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void logInformation() {
		// TODO Auto-generated method stub
		NetworkInfo[] nI = cM.getAllNetworkInfo();
		
		for (int i = 0; i<nI.length; ++i)
		{
			String state = nI[i].getDetailedState().toString();
			String name = nI[i].getTypeName();
			String connection = "isAvailable: " + (nI[i].isAvailable()?1:0) + ", isConnected: " + (nI[i].isConnected()?1:0);
			
			//TODO: Adding in a call to log the info to the DB
		}
	}

}
