package com.wattshappening;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.wattshappening.R;
import com.wattshappening.analysis.Analyzer;
import com.wattshappening.db.AggregateAppInfo;
import com.wattshappening.db.AggregateAppInfoTable;
import com.wattshappening.db.DBManager;
import com.wattshappening.db.DBTable;
/**
 * @author Nick
 *
 */
public class WattsHappening extends Activity {

	Button startButton;
	Button stopButton;
	Button dbFlushButton;
	Button dbExportButton;
	Button analysisButton;
	Button refreshButton;
	TextView tvCPU;
	TextView tvNet;
	/**
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {

		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watts_happening);
        
        final Intent monitorIntent = new Intent(this,MonitorService.class);
        
        //stupid tab stuff now
        TabHost tabs=(TabHost)findViewById(R.id.tabhost);
	    tabs.setup();
	    TabHost.TabSpec spec=tabs.newTabSpec("tag1");
	    spec.setContent(R.id.tab1);
	    spec.setIndicator("Actions");
	    tabs.addTab(spec);
	    spec=tabs.newTabSpec("tag2");
	    spec.setContent(R.id.tab2);
	    spec.setIndicator("Analysis");
	    tabs.addTab(spec);
        
        startButton = (Button) findViewById(R.id.button1);
        startButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		Log.i("LocalService", "Start Button Clicked");
        		startService(monitorIntent);
        	}
        });

        stopButton = (Button) findViewById(R.id.button2);
        stopButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		Log.i("LocalService", "Stop Button Clicked");
        		stopService(monitorIntent);
        	}
        });
        dbFlushButton = (Button) findViewById(R.id.button3);
        dbFlushButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("LocalService", "Flush Button Clicked");
				stopService(monitorIntent);
				DBManager dbMan =  DBManager.getInstance(v.getContext());
				SQLiteDatabase db = dbMan.getWritableDatabase(); 
				dbMan.flushTables(db, DBTable.FLUSH_ONLY_NONPERSISTANT);
				
			}
		});
        
        dbExportButton = (Button) findViewById(R.id.button4);
        dbExportButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("LocalService", "Export Button Clicked");
				try {
			        File sd = Environment.getExternalStorageDirectory();
			        File data = Environment.getDataDirectory();

			        if (sd.canWrite()) {
			            
			        	String currentDBPath = "//data//com.wattshappening//databases//wh_log.db";
			            String backupDBPath = "wh_log_pulled.db";
			            File currentDB = new File(data, currentDBPath);
			            File backupDB = new File(sd, backupDBPath);

			            if (currentDB.exists()) {
			                FileChannel src = new FileInputStream(currentDB).getChannel();
			                FileChannel dst = new FileOutputStream(backupDB).getChannel();
			                dst.transferFrom(src, 0, src.size());
			                src.close();
			                dst.close();
			                Toast.makeText(getBaseContext(), backupDB.toString(), Toast.LENGTH_LONG).show();

			                 Log.i("LocalService", "DB backed up to "+backupDBPath);
			            }else{
			            	Log.i("LocalService", "DB doesn't exist??");
			            }
			        }else {
			        	Log.i("LocalService", "Can't write to SD!");
			        }
			    } catch (Exception e) {
			    }
			}
		});
        
        analysisButton = (Button) findViewById(R.id.button5);
        analysisButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		Log.i("LocalService", "Analysis Button Clicked");
        		//start the analyzer
        		
        		Analyzer a = new Analyzer();
        		a.runAnalysis(v.getContext());
        	}
        });
        
                
        refreshButton = (Button) findViewById(R.id.button6);
        refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
				tvCPU = (TextView) findViewById(R.id.tvcpu);
		        tvNet = (TextView) findViewById(R.id.tvnet);
		        String cpuOut = "cpu:\n";
		        String netOut = "net\ntest";
		        AggregateAppInfoTable aggTable = new AggregateAppInfoTable(getBaseContext());
		        
		        //get list of currently running apps   
		        // 
		        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		        PackageManager pm = getPackageManager();
		        List<ActivityManager.RunningAppProcessInfo> procs = am.getRunningAppProcesses();
		        HashMap<String, Double> cpuUse = new HashMap<String, Double>();
		        HashMap<String, Double> netUse = new HashMap<String, Double>();
		        
		        if(procs != null){
		        	for(ActivityManager.RunningAppProcessInfo proc : procs){
		        		ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(proc);
		        		String name = info.processName;
		        		int uid = info.uid;
		        		//get usage by uid
		        		AggregateAppInfo aggappinfo = aggTable.fetchMostRecent(uid);
		        		double cpu = aggappinfo.getHistoricCPU();
		        		double net = aggappinfo.getHistoricNetwork();
		        		cpuUse.put(name, cpu);
		        		//cpuOut += name + " : "+cpu+"\n";  // temporary
		        		netUse.put(name, net);
		        		//netOut += name + " : " + net + "\n";  // temporary
		        	}
		        }
		        //sort & display
		        Map<String, Double> sortedCpu = sortByValue(cpuUse);
		        Map<String, Double> sortedNet = sortByValue(netUse);
		        
		       for (Map.Entry entry: sortedCpu.entrySet())
		    	   cpuOut += entry.getKey() + " : " + entry.getValue() + "\n";
		       for (Map.Entry entry: sortedNet.entrySet())
		    	   netOut += entry.getKey() + " : " + entry.getValue() + "\n";
		       
		        tvCPU.setText(cpuOut);
		        tvNet.setText(netOut);

				tvCPU.setText(cpuOut);
				tvNet.setText(netOut);
			}
		});
    }
    
    public static Map<String, Double> sortByValue(Map<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {

            public int compare(Map.Entry<String, Double> m1, Map.Entry<String, Double> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });

        Map<String, Double> result = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

	/**
	 * 
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_watts_happening, menu);
        return true;
    }
}
