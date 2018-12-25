package com.munsiji.commonUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static void main(String[] args){
	//	System.out.println(convertStringToDate("18-07-2015"));
		//System.out.println(convertStringToString("18-07-2015"));
		//System.out.println(convertDBStringToViewString("2015-05-15 12:06:55")); // passing arg should be in Date format
	}
	
  public static Date convertStringToDate(String d){ // string date format is dd-MM-yyyy to util.date format to store the date in db
	Date date = null;
    try{
    DateFormat c = new SimpleDateFormat("dd-MM-yyyy");
    date = c.parse(d);
    }
    catch(Exception e){
     System.out.println("exception while date conversion:"+e);
    }
    return date;
  }
  public static String convertStringToString(String d){  //from  "dd-MM-yyyy" to "yyyy-MM-dd",  used for fetching data from db in between provided date
		Date date = null;
		String outDate = null;
	    try{
	    DateFormat indate = new SimpleDateFormat("dd-MM-yyyy");
	    date = indate.parse(d);
	    DateFormat outdate = new SimpleDateFormat("yyyy-MM-dd");
	    outDate = outdate.format(date);
	    }
	    catch(Exception e){
	     System.out.println("exception while date conversion:"+e);
	    }
	    return outDate;
	  }
  public static String convertDBStringToViewString(Date dbDate){  //from  "yyyy-MM-dd" to "dd-MM-yyyy", for showing db result to view
	    String d = String.valueOf(dbDate);
		Date date = null;
		String outDate = null;
	    try{
	    DateFormat indate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    date = indate.parse(d);
	    DateFormat outdate = new SimpleDateFormat("dd-MM-yyy");
	    outDate = outdate.format(date);
	    }
	    catch(Exception e){
	     System.out.println("exception while date conversion:"+e);
	    }
	    return outDate;
	  }
}
