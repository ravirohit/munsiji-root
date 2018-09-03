package org.munsiji.commonUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
  public static Date convertStringToDate(String d){
	System.out.println("String Date:"+d);
	Date date = null;
    try{
    DateFormat c = new SimpleDateFormat("dd-MM-yyyy HH:MM:SS");
    date = c.parse(d);
    }
    catch(Exception e){
     System.out.println("exception while date conversion:"+e);
    }
    return date;
  }

}
