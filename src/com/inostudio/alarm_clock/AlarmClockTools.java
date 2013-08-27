package com.inostudio.alarm_clock;

import java.util.Calendar;

public class AlarmClockTools {
	private int hour;
	private int minute;
	private boolean flag;
	public static final String ALARM_HOUR = "ALARM_HOUR";
	public static final String ALARM_MINUTE = "ALARM_MINUTE";
	public static final String LEFT_TIME = "LEFT_TIME";
	
	public AlarmClockTools(){
		flag = false;
	}
	//return value to left sleep in minute
	public int getLeftTimeSleep(){
		int currentTime = getCurrentTime();
		int alarmTime = hour * 60 + minute;
		
		if(currentTime <= alarmTime){
			
			return alarmTime - currentTime;
		}else{
			
			return alarmTime + (24*60 - currentTime);
		}
	}
	
	static public int alarmRingsIn(int leftTime){
		int tmpTime = leftTime + getCurrentTime();
		if(tmpTime > 24*60)
			tmpTime -= 24*60;
		return tmpTime;
		
	}
	
	/*Return the current time in minutes*/
	static public int getCurrentTime(){
		Calendar calendar = Calendar.getInstance();
		return (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));
	}
	
	static public String intToTimeString(int value){
		 return (value < 10)?"0"+value : value+"";
	 }
	
	public int getAlarmtHour(){
		return hour;
	}
	
	public int getAlarmtMinute(){
		return hour;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
		flag = true;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		flag = true;
		this.minute = minute;
	}
	
	public boolean isSet(){
		return flag;
	}
	
}
