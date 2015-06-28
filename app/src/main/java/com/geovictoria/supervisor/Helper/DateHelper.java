package com.geovictoria.supervisor.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maximiliano on 6/25/2015.
 */
public class DateHelper {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date parseFromServer(String dateString) throws ParseException {
        return format.parse(dateString);
    }
    public static String formatDate(Date date){
        String dateString = format.format(date);
        return dateString;
    }
    public static String formatCalendar(Calendar calendar){
        return formatDate(calendar.getTime());
    }
}
