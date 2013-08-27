package com.inostudio.alarm_clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class PowerConnectionBroadcastReceiver extends BroadcastReceiver {
	private boolean isWakeLock = false;
	@Override
	public void onReceive(Context ctx, Intent intent) {
		//If battery charging/connect/disconnect
		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		
		//Get battery status
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
		if(isCharging){
			if(!isWakeLock){
				isWakeLock = true;
				Intent wakeLock = new Intent(ctx.getApplicationContext(),WakeLockService.class);
				ctx.startService(wakeLock);
			}
		}else{
			if(isWakeLock){
			isWakeLock = false;
			Intent wakeLock = new Intent(ctx.getApplicationContext(),WakeLockService.class);
			ctx.startService(wakeLock);
			}
		}
	}
}
