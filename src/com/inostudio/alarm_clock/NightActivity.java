package com.inostudio.alarm_clock;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

public class NightActivity extends Activity {

	private final AlarmClockTools alarmTools = new AlarmClockTools();
	private Prefference pref; 
	private PowerConnectionBroadcastReceiver powerConnReceiver = new PowerConnectionBroadcastReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_night);
				
		pref = new Prefference(this);
				
		final TextView currentTimeTV = (TextView) findViewById(R.id.currentTime);
		currentTimeTV.setTextColor(Color.GREEN);
		
		final TextView leftToSleepTV = (TextView) findViewById(R.id.leftToSleep);
		leftToSleepTV.setTextColor(Color.GREEN);
		
		alarmTools.setHour(pref.getHour());
		alarmTools.setMinute(pref.getMinutes());
		
		leftToSleepTV.setText(AlarmClockTools.intToTimeString(alarmTools.getHour())+":"+AlarmClockTools.intToTimeString(alarmTools.getMinute()));
		
		//Create timer to update time in activity
		Timer updateTime = new Timer();
		updateTime.schedule(new TimerTask(){
		      public void run() {
		    	runOnUiThread(new Runnable(){

					@Override
					public void run() {
						int hour = AlarmClockTools.getCurrentTime() / 60;
						int minutes = AlarmClockTools.getCurrentTime() % 60;
						
						currentTimeTV.setText(AlarmClockTools.intToTimeString(hour)+
								":"+AlarmClockTools.intToTimeString(minutes));
						
						int leftToSleep = alarmTools.getLeftTimeSleep();

						if(leftToSleep/60 < 1){
							leftToSleepTV.setTextColor(Color.RED);
						}
						
						hour = leftToSleep / 60;
						minutes = leftToSleep % 60;
						
						leftToSleepTV.setText(getString(R.string.alarm_rings_in)+
								AlarmClockTools.intToTimeString(hour)+":"+
								AlarmClockTools.intToTimeString(minutes));
					}
		    		
		    	});
		      }
		}, 0, 3*1000);
		
	}
	
	  @Override
	  protected void onResume(){
		  super.onResume();
		  
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
					WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		  
		  //Register PowerConnectionReceiver
		  IntentFilter intFilt = new IntentFilter();
		  intFilt.addAction(Intent.ACTION_POWER_CONNECTED);
		  intFilt.addAction(Intent.ACTION_POWER_DISCONNECTED);
		  intFilt.addAction(Intent.ACTION_BATTERY_CHANGED);
		  registerReceiver(powerConnReceiver, intFilt);
		  
		  if (this.getWindowManager().getDefaultDisplay().getOrientation() == 0 || 
			        this.getWindowManager().getDefaultDisplay().getOrientation() == 2) {
			Intent rootActivity = new Intent();
	    	rootActivity.setClassName("com.inostudio.alarm_clock", "com.inostudio.alarm_clock.RootActivity");
		    this.startActivity(rootActivity);
		  }
		  
	  }
	  
	  @Override
	  protected void onPause() {
	    super.onPause();
	    //Unregister PowerConnectionReceiver
	    unregisterReceiver(powerConnReceiver);
	  }	  	  
}
