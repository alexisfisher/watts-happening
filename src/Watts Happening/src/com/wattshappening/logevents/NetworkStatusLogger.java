package com.wattshappening.logevents;


import com.wattshappening.db.NetworkEntry;
import com.wattshappening.db.NetworkTable;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusLogger extends LogProcess {

	private ConnectivityManager cM = null;
	private NetworkTable nTable = null;
	
	public NetworkStatusLogger(Service parent) {
		super(parent,30000);
		cM = ((ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE));
		nTable = new NetworkTable(parent);
	}

	@Override
	protected void startLoggingEvents() {

	}

	@Override
	protected void stopLoggingEvents() {

	}

	@Override
	protected void logInformation() {
		NetworkInfo[] nI = cM.getAllNetworkInfo();
		
		for (int i = 0; i<nI.length; ++i)
		{
			String state = nI[i].getDetailedState().toString();
			String name = nI[i].getTypeName();
			String connection = "isAvailable: " + (nI[i].isAvailable()?1:0) + ", isConnected: " + (nI[i].isConnected()?1:0);
			
			try {
				nTable.addEntry(new NetworkEntry(name,state,connection));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
