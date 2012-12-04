package com.wattshappening;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
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
	Button toastButton;
	TextView tv;
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
        
        tv = (TextView) findViewById(R.id.tv1);
        tv.setText("Hello");
        
        toastButton = (Button) findViewById(R.id.button6);
        toastButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "button 6", Toast.LENGTH_SHORT).show();
			}
		});
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
