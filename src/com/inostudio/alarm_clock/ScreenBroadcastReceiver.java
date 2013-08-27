package com.inostudio.alarm_clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ScreenBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context ctx, Intent intent) {		
		Intent accelerometer = new Intent(ctx, AccelerometerService.class);
		String action = intent.getAction();
		
		if(action.equals(Intent.ACTION_SCREEN_OFF)){
			ctx.startService(accelerometer);
		}else{
			if(action.equals(Intent.ACTION_SCREEN_ON)){
				ctx.stopService(accelerometer);
			}
		}
	}	
}
