package com.inostudio.alarm_clock;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AccelerometerService extends Service implements SensorEventListener{
	private SensorManager sensorManager;
	private boolean initializated;
	private static final float accuracy = 2;
	private float prev_x, prev_y, prev_z;
	private KeyguardManager mKeyGuardManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate(){	
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	@Override
	public void onDestroy(){
		sensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent evt) {
		if(evt.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			//assign directions
			float x = evt.values[0];
			float y = evt.values[1];
			float z = evt.values[2];
			
		    if(!initializated){
		    	prev_x = x;
		    	prev_y = y;
		    	prev_z = z;
		    	initializated = true;
		    }else{
		    	//The calculation of the error
		    	float dx = Math.abs(prev_x - x);
				float dy = Math.abs(prev_y - y);
				float dz = Math.abs(prev_z - z);
				
				
				//if error < accuracy set value 0
				if(dx < accuracy) dx = 0;
				if(dy < accuracy) dy = 0;
				if(dz < accuracy) dz = 0;
				
				prev_x = x;
		    	prev_y = y;
		    	prev_z = z;

				if(dx > 0 || dy > 0 || dz > 0){
					PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.inostudio.alar_clock");
					wl.acquire(30 * 1000);
					    		
					//unlock screen
					mKeyGuardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
					KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("com.inostudio.alar_clock");
					mLock.disableKeyguard();	
						
					//Start NightActivity
					Intent nightActivity = new Intent();
					nightActivity.setClassName("com.inostudio.alarm_clock", "com.inostudio.alarm_clock.RootActivity");
					nightActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				    startActivity(nightActivity);
				}
		    }
		}
	}
}