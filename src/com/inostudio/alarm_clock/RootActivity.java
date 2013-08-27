package com.inostudio.alarm_clock;

import java.util.Date;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;

/*
 * Main application class
 * */
public class RootActivity extends Activity {
	public static final String DIALOG_ON = "dialog";
	
	private TimePicker timePicker;
	private TextView leftToSleepView;
	private final AlarmClockTools alarmClockTools = new AlarmClockTools();;
	private final AlarmManagerBroadcastReceiver alarmReciver = new  AlarmManagerBroadcastReceiver();
	private Prefference pref;
	private Intent screenService;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_root);
		
		pref = new Prefference(this);
		
		leftToSleepView = (TextView)findViewById(R.id.leftToSleepView);
		
		timePicker = (TimePicker)findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);	
		
		//get current date
		Date date = new Date();
		setCurrentTime(date.getHours(), date.getMinutes());
								
		if(pref.isSetTime()){
			int hour = pref.getHour();
			int minutes = pref.getMinutes();
			setCurrentTime(hour, minutes);
		}
										
		int leftToSleep = alarmClockTools.getLeftTimeSleep();
		leftToSleepView.setText((leftToSleep / 60)+" "+(leftToSleep % 60));
		
		leftToSleepView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent alarmActivityIntent = new Intent(RootActivity.this, AlarmActivity.class);
				alarmActivityIntent.putExtra(AlarmClockTools.LEFT_TIME, alarmClockTools.getLeftTimeSleep());
				startActivity(alarmActivityIntent);
			}
			
		});
		
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){
			
			@Override
			public void onTimeChanged(TimePicker view, int hour, int minute) {	
				
				//if changed TimePicker, set new values of alarm
				alarmClockTools.setHour(hour);
				alarmClockTools.setMinute(minute);
				
				//get the left time to sleep
				int leftToSleep = alarmClockTools.getLeftTimeSleep();
				leftToSleepView.setText((leftToSleep / 60)+" "+(leftToSleep % 60));
			}
			
		});
		
		final Button flahLightButton = (Button)findViewById(R.id.flashLightButton);
		flahLightButton.setOnClickListener(new FlashLightListener(getApplicationContext()));
		
		final Button exitButton = (Button)findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				//if click button exit, canceled AlarmMAnager
				alarmReciver.cancelAlarm(getApplicationContext());
				
				
	    		Intent screenService = new Intent(getApplicationContext(), StartScreenBroadcastReceiverService.class);
	    		stopService(screenService);
	    		pref.removeAccelerometerService();
	    		
				pref.removeAlarmFlag();
				Toast.makeText(getApplicationContext(), getString(R.string.alarm_off), Toast.LENGTH_SHORT).show();
				killActivity();
			}
			
		});
		
		final Button createAlarmButton = (Button) findViewById(R.id.createAlarmButton);
		createAlarmButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				//if click button createAlarm then create a new Alarm Manager
				int hour = timePicker.getCurrentHour();
				int minutes = timePicker.getCurrentMinute();
				alarmClockTools.setHour(hour);
				alarmClockTools.setMinute(minutes);
				
				long time = alarmClockTools.getLeftTimeSleep();
	    		alarmReciver.setAlarm(getApplicationContext(), time);
	    		pref.setLeftTime((int)time);
	    		pref.savePreferences(hour, minutes);
	    		
	    		Toast.makeText(getApplicationContext(), getString(R.string.alarm_is_set), Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	protected void onResume(){
		super.onResume();
		
		if(pref.isSetLeftTime()){
			int alarmRingsIn = AlarmClockTools.alarmRingsIn(pref.getLeftTime());
			int hour = alarmRingsIn / 60;
			int minutes = alarmRingsIn % 60;	
			setCurrentTime(hour, minutes);
		}
				
		//start accelerometer service
		if(pref.isSetAlarm()){
			screenService = new Intent(getApplicationContext(), StartScreenBroadcastReceiverService.class);
			startService(screenService);
		}		
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		if(pref.isSetNotification()){
			this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);
			createDialog();
		}
		
		if (this.getWindowManager().getDefaultDisplay().getOrientation() == 1 || 
		        this.getWindowManager().getDefaultDisplay().getOrientation() == 3) {
			if(pref.isSetAlarm()){
	    		//Start a NightActivity
	    		Intent nightActivity = new Intent(this, NightActivity.class);
		    	this.startActivity(nightActivity);	    	
	    	}
		}
	}
			
	private void killActivity(){
		finish();
		System.exit(0);
	}
    
    private void setCurrentTime(int hour, int minutes){
    	alarmClockTools.setHour(hour);
		alarmClockTools.setMinute(minutes);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minutes);
    }
    
    public void createDialog(){
    	//create a new LinearLayout
	    LinearLayout layout = new LinearLayout(this);
	    layout.setOrientation(LinearLayout.HORIZONTAL);
	    
	    //create a new TextView
	    final TextView task = new TextView(this);
	    	
	    //
	    int firstTerm = (int) ((Math.random()*10));
	    int secondTerm = (int) ((Math.random()*10));
	    final int result = firstTerm + secondTerm;
	    	
	    task.setText(getString(R.string.solve) + " " + firstTerm+ " + " +secondTerm + " = ");
	    task.setTextColor(Color.WHITE);
	    layout.addView(task);

	    //set an EditText view to get user input   
	    final EditText input = new EditText(this); 
	    input.setMinEms(10);
	    input.setInputType(InputType.TYPE_CLASS_NUMBER);
	    
	    layout.addView(input);
	    	
	    //create dialog
	    AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	    alert.setTitle(this.getString(R.string.alarm_clock)); 
	    alert.setView(layout); 
	    alert.setCancelable(false);
	    
	    alert.setPositiveButton(this.getString(R.string.off_alarm_clock), new DialogInterface.OnClickListener(){
	    	@Override
	    	public void onClick(DialogInterface dialog, int id) {
	    		try { 
	    			int value = Integer.parseInt(input.getText().toString());
	    			if(value == result){
	    				alarmReciver.cancelAlarm(getApplicationContext());
	    				Toast.makeText(getApplicationContext(), getString(R.string.alarm_off), Toast.LENGTH_SHORT).show();
	    				pref.removeAlarmFlag();
	    				
	    				//stop screen service, and off accelerometer service 
	    				if(pref.isAccelerometerStart()){
	    					stopService(screenService);
	    					pref.removeAccelerometerService();
	    					
	    				}	
	    				killActivity();
	    			}else{
	    				alarmReciver.cancelAlarm(getApplicationContext());
	    				alarmReciver.setAlarm(getApplicationContext(), 0);
	    				Toast.makeText(getApplicationContext(), getString(R.string.error_alarm_repeat), Toast.LENGTH_SHORT).show();
	    				pref.removeAlarmFlag();
	    			}	
	    		}catch(NumberFormatException e){
	    			Toast.makeText(getApplicationContext(), getString(R.string.input_error), Toast.LENGTH_SHORT).show();
	    			createDialog();
	    		}
	    	}
	    		
	    });
	    alert.create().show();
	
	 }
}
