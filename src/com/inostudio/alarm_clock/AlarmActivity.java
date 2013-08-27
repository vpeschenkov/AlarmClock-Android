package com.inostudio.alarm_clock;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
/*
 * Alternative mode to set wake up time;
 * */
public class AlarmActivity extends Activity implements OnSeekBarChangeListener{

	private SeekBar leftToSleepSeekBar;
	private TextView toSleepTextView;
	private TextView leftToSleepTextView;
	private int leftTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm);
		
		//Get the left time to sleep from previous activity
		Bundle extras = getIntent().getExtras();
		leftTime = extras.getInt(AlarmClockTools.LEFT_TIME);

		//Setup the SeekBar
		leftToSleepSeekBar = (SeekBar)findViewById(R.id.leftToSleeSeekBar);
		leftToSleepSeekBar.setMax(1440);
		leftToSleepSeekBar.setProgress(leftTime);
		leftToSleepSeekBar.setOnSeekBarChangeListener(this);
		
		toSleepTextView = (TextView)findViewById(R.id.toSleepTextView);
		
		toSleepTextView.setText(AlarmClockTools.intToTimeString(leftTime / 60)+
				":"+AlarmClockTools.intToTimeString(leftTime % 60));
		
		leftToSleepTextView = (TextView)findViewById(R.id.leftToSleepTextView);
		leftToSleepTextView.setText(getString(R.string.alarm_rings_in)+
				" "+(AlarmClockTools.alarmRingsIn(leftTime)/60)+
				":"+(AlarmClockTools.alarmRingsIn(leftTime)%60));
		
		//Return to RootActivity
		final TextView returnToViewActivity = (TextView)findViewById(R.id.leftToSleepTextView);
		returnToViewActivity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Prefference pref = new Prefference(getApplicationContext());
				pref.setLeftTime(leftTime);
				Intent rootActivityIntent = new Intent(AlarmActivity.this, RootActivity.class);
				startActivity(rootActivityIntent);
			}
			
		});
	}

	
	
	/*
	 * called if configuration changed
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		leftTime = progress;
		int hour = leftTime / 60;
		int minutes  = leftTime % 60;
		
		toSleepTextView.setText(AlarmClockTools.intToTimeString(hour)+
				":"+AlarmClockTools.intToTimeString(minutes));
		
		hour = AlarmClockTools.alarmRingsIn(leftTime) / 60;
		minutes = AlarmClockTools.alarmRingsIn(leftTime) % 60;
		
		leftToSleepTextView.setText(getString(R.string.alarm_rings_in)+
				AlarmClockTools.intToTimeString(hour)+":"+
				AlarmClockTools.intToTimeString(minutes));
	}

	/*
	 * implements method(non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	
	/*
	 * implements method(non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
}
