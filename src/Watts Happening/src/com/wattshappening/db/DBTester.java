package com.wattshappening.db;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class DBTester {

	DBManager db;
	
	public DBTester(Context context, boolean testAll){
		this.db = new DBManager(context);
        
		Log.d("Test: ", "Begin testing database functions...");
		
		if(testAll){
			testGPS();
			testHardware();
			testAppInfo();
		}
	}
	
	public void testGPS(){
		Log.d("Insert: ", "Inserting GPS..");
        db.addGPSCoordinate(new Gps("132,132"));
        db.addGPSCoordinate(new Gps("120, 120"));
        
        Log.d("Reading: ", "Reading GPS data ..");
        List<Gps> gps = db.getAllGPSCoordinates();
        
        for(Gps g : gps){
        	String log = "Timestamp: " + g.getTimestamp() + " Coordinate: " + g.getCoordinate();
        	Log.d("Name: ", log);
        }
	}
	
	public void testHardware(){
		Log.d("Insert: ", "Inserting Hardware..");
		db.addHardware(new Hardware("Test1", 1, "testing"));
        db.addHardware(new Hardware("Test2", 0, "testing"));
        
        Log.d("Reading: ", "Reading Hardware data ..");
        List<Hardware> hardware = db.getAllHardware();
        
        for(Hardware hw : hardware){
        	String log = "Timestamp: " + hw.getTimestamp() + " Name: " + hw.getName() + " Enabled: " + 
        			hw.getEnabled() + " Status: " + hw.getStatus();
        	Log.d("Name: ", log);
        }
	}
	
	public void testAppInfo(){
		Log.d("Insert: ", "Inserting AppInfo..");
        db.addAppInfo(new AppInfo("APP1", 1234, 20.4));
        db.addAppInfo(new AppInfo("App2", 4321, 50.92));
        
        Log.d("Reading: ", "Reading App Info data ..");
        List<AppInfo> appInfo = db.getAllAppInfo();
        
        for(AppInfo ai : appInfo){
        	String log = "Timestamp: " + ai.getTimestamp() + " Name: " + ai.getName() + " App ID: " + 
        			ai.getAppId() + " CPU: " + ai.getCPU();
        	Log.d("Name: ", log);
        }
	}
	
}
