package com.inostudio.alarm_clock;

import java.util.Date;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/*
 * Called from post alarm
 * create and delete notification
 * */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver{	
	private NotificationManager mNotifyMgr;
	public static final int NOTIFICATION_ID = 4256;
	private KeyguardManager mKeyGuardManager;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {     	
    	//Unlock device screen
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
	    WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
	    wl.acquire(30*1000);
		
	    mKeyGuardManager = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("com.inostudio.alar_clock");
		mLock.disableKeyguard();
	    
		//Create notification
		creatNotification(ctx);
		
		//Start dialog
		Intent dialog = new Intent();
		dialog.setClassName("com.inostudio.alarm_clock", "com.inostudio.alarm_clock.RootActivity");
		dialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(dialog);
		
		//release screen
		wl.release();
	}
	/* Set alarm clock*/
	public void setAlarm(Context ctx, long time) {
		//Get Alarm Manager service
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Setting intent
        Intent intent = new Intent(ctx, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        
        //Create Alarm Manager notification, no't repeat
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(time * 60 * 1000), 0, pi);
    }
 
	/* Cancel alarm clock and cancel notification*/
    public void cancelAlarm(Context ctx) {
        Intent intent = new Intent(ctx, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        
        //Get NotificationManager
        mNotifyMgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        //Canceled notification
        mNotifyMgr.cancel(NOTIFICATION_ID);
    }
        
    /*Builder alarm clock notification*/
    private void creatNotification(Context ctx){
    	int icon = R.drawable.ic_launcher; 
    	
		mNotifyMgr = (NotificationManager)ctx.getSystemService( 
        		Context.NOTIFICATION_SERVICE); 
		
		CharSequence tickerText = ctx.getString(R.string.alarm_clock);
		
		//Get current time
		long when = System.currentTimeMillis();
		
		//Create notification
		Notification notification = new Notification(icon, tickerText, when);	
		notification.flags |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.FLAG_INSISTENT;
		notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		
		
		//A title notification
		CharSequence contentTitle = ctx.getString(R.string.wake_up)+"!"; 
		
		//Get current date
		Date currentTime = new Date();
        CharSequence contentText = currentTime.getHours()+":"+currentTime.getMinutes(); 
        
        //Set flag notification
        Prefference pref = new Prefference(ctx);
        pref.setNotification(Boolean.TRUE);
        
        //This intent is invoke when clicked icon notification
        Intent rootActivity = new Intent(ctx, RootActivity.class); 
	    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, rootActivity, 0);
	    notification.setLatestEventInfo(ctx, contentTitle,contentText, contentIntent); 
	    
	    //Register this notification
	    mNotifyMgr.notify(NOTIFICATION_ID, notification);
	  }
}
