package com.dpc.workbench.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WorkbenchUtils {

	public WorkbenchUtils() {
	}
	
	public static String convertToHhMmSs() {
	    Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	return (String)sdf.format(cal.getTime());
	}
	
	public static void writeToLog(String msg) {
		String hms = null;
		hms = WorkbenchUtils.convertToHhMmSs();
		System.out.println(msg + ": " + hms);
	}
	
}
