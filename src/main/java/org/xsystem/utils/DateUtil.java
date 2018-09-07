/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author atimofeev
 */
public class DateUtil {

    public static String nowISODateAsString() {
        Date d = new Date();
        long date = d.getTime();
        String result = ISODateTimeFormat.dateTimeNoMillis().print(date);
        return result;
    }

    public static String plusISODateAsString(String sd, long mc) {
        DateTime dt = DateTime.parse(sd);
        DateTime dt2 = dt.plus(mc);
        long l = dt2.getMillis();
        String result = ISODateTimeFormat.dateTimeNoMillis().print(l);
        return result;
    }

    public static String printISODate(Date d) {
        String result = null;
        if (d != null) {
            long l = d.getTime();
            result = ISODateTimeFormat.dateTimeNoMillis().print(l);
        }
        return result;
    }
    
    public static Date isoDateFromString(String s){
     // DateTimeFormatter formater=(isms)?ISODateTimeFormat.
      long l=  ISODateTimeFormat.dateTimeNoMillis().parseMillis(s);
      Date ret=new Date(l);
      return ret;
    }
}
