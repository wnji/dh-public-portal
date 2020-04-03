package com.blkchainsolutions.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public final class TimeSetup {

	public static Timestamp getTimestamp() {
		
		return new Timestamp(System.currentTimeMillis());
		
	}
	
	public static Timestamp getTimestampMinus2Mins() {
		
		return new Timestamp(System.currentTimeMillis()-2*60*1000);
		
	}
	
	public static Timestamp getTimestampMinus10Mins() {
		
		return new Timestamp(System.currentTimeMillis()-10*60*1000);
		
	}
	
	public static Calendar getTimezone() {
		
		return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		
	}
}