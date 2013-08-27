package com.inostudio.alarm_clock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class WakeLockService extends Service {
	private PowerManager.WakeLock wakeLock;
	
	/*extends method (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock");
        wakeLock.acquire();
    }
 
    @Override
    public void onDestroy() {
    	wakeLock.release();
        super.onDestroy();
    }

}
