package com.inostudio.alarm_clock;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class StartScreenBroadcastReceiverService extends Service{
	private final ScreenBroadcastReceiver mReceiver = new ScreenBroadcastReceiver();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
	super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    filter.addAction(Intent.ACTION_SCREEN_ON);
	    registerReceiver(mReceiver, filter);
	 }
	
	@Override
	public void onDestroy() {
	super.onCreate();
	    unregisterReceiver(mReceiver);
	 }

}
