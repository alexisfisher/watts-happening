package com.wattshappening;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.wattshappening.db.DBManager;
/**
 * @author Nick
 *
 */
public class WattsHappening extends Activity {

	Button startButton;
	Button stopButton;
	Button dbFlushButton;
	Button dbExportButton;
	/**
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {

		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watts_happening);
        
        final Intent monitorIntent = new Intent(this,MonitorService.class);

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
				DBManager db =  DBManager.getInstance(v.getContext());
				SQLiteDatabase s = db.getWritableDatabase(); 
				db.dropTables(s);
				db.onCreate(s);
				
				
			}
		});
        
        dbExportButton = (Button) findViewById(R.id.button4);
        dbExportButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("LocalService", "Export Button Clicked");
				//
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
