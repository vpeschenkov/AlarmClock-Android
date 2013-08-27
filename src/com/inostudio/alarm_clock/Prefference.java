package com.inostudio.alarm_clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefference {
	public static final String SETTINGS_NAME = "com.inostudio.alarm_clock.settings";
	public static final String SETTINGS_HOUR = "com.inostudio.alarm_clock.hour";
	public static final String SETTINGS_MINUTES = "com.inostudio.alarm_clock.minutes";
	public static final String SETTINGS_ALARM_ON = "com.inostudio.alarm_clock.alarm_on";
	public static final String SETTINGS_LEFT_TIME = "com.inostudio.alarm_clock.left_time";
	public static final String SETTING_ACCELEROMETER_SERVICE = "com.inostudio.alarm_clock.accelerometer_service";
	public static final String SETTINGS_NOTIFICATION = "com.inostudio.alarm_clock.notification";
	public static final String SETTINGS_NOTIFICATION_SOUND = "com.inostudio.alarm_clock.notification_sound";
	
	private SharedPreferences mSettings;
	
	public Prefference(Context ctx){
		mSettings = ctx.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
	}
	
	public int getHour(){
		return mSettings.getInt(SETTINGS_HOUR, 0);
	}
	
	public String getNotificationSound(){
		return mSettings.getString(SETTINGS_NOTIFICATION_SOUND, "");
	}
	
	public int getMinutes(){
		return mSettings.getInt(SETTINGS_MINUTES, 0);
	}
	
	public Editor getEditor(){
		return mSettings.edit();
	}
	
	public int getLeftTime(){
		return mSettings.getInt(SETTINGS_LEFT_TIME, 0);
	}
		
	public void setAccelerometerService(){
		Editor editor = mSettings.edit();
		editor.putBoolean(SETTING_ACCELEROMETER_SERVICE, Boolean.TRUE);
		editor.commit();
	}
	
	public void setNotificationSound(String fileName){
		Editor editor = mSettings.edit();
		editor.putString(SETTINGS_NOTIFICATION_SOUND, fileName);
		editor.commit();
	}
	
	
	public void setLeftTime(int leftTime){
		Editor editor = mSettings.edit();
		editor.putInt(SETTINGS_LEFT_TIME, leftTime);
		editor.commit();
	}
	
	public void setNotification(boolean flag){
		Editor editor = mSettings.edit();
		editor.putBoolean(SETTINGS_NOTIFICATION, flag);
		editor.commit();
	}
	
	public void savePreferences(int hour, int minutes){
		Editor editor = mSettings.edit();
		editor.putInt(SETTINGS_HOUR, hour);
		editor.putInt(SETTINGS_MINUTES, minutes);
		editor.putBoolean(SETTINGS_ALARM_ON, Boolean.TRUE);
		editor.putBoolean(SETTING_ACCELEROMETER_SERVICE, Boolean.TRUE);
		editor.commit();
	}
	
	public boolean isAccelerometerStart(){
		return mSettings.contains(SETTING_ACCELEROMETER_SERVICE);
	}
	
	public boolean isSetNotification(){
		return mSettings.contains(SETTINGS_NOTIFICATION);
	}
	
	public boolean isSetTime(){
		return (mSettings.contains(SETTINGS_HOUR) && mSettings.contains(SETTINGS_MINUTES));
	}
	
	public boolean isSetAlarm(){
		return mSettings.contains(SETTINGS_ALARM_ON);
	}
	
	public boolean isSetLeftTime(){
		return mSettings.contains(SETTINGS_LEFT_TIME);
	}
	
	public boolean isSetNotificationSound(){
		return mSettings.contains(SETTINGS_NOTIFICATION_SOUND);
	}
	
	public void removeAccelerometerService(){
		mSettings.edit().remove(SETTINGS_ALARM_ON).commit();
	}
	
	public void removeAlarmFlag(){
		Editor editor = mSettings.edit();
		editor.remove(SETTINGS_ALARM_ON);
		editor.remove(SETTING_ACCELEROMETER_SERVICE);
		editor.remove(SETTINGS_NOTIFICATION);
		editor.commit();
	}
	
	public void clear(){
		mSettings.edit().clear().commit();
	}
	
}
