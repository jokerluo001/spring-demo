package com.example.weather.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {

    private static final String  FORMAT_DAY_BEGIN = "yyyyMMdd";
    public static final String  FORMAT_MONTH = "yyyyMM";
    public static final String  FORMAT_DEFAULT= "yyyy-MM-dd HH:mm:ss";
    public static final String  FORMAT_DATE_YYYY_MM_DD= "yyyy-MM-dd";
    public static final String  FORMAT_DATE_EXCEL= "yyyy/M/d";

    public static String convertToStr(long milliSecond) {
        Date date = new Date(milliSecond);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DEFAULT);
        return simpleDateFormat.format(date);
    }

    public static String convertToStr(String str) {
        if (str.length() != 14) {
            throw new RuntimeException("格式错误");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 4));
        sb.append("-");
        sb.append(str.substring(4, 6));
        sb.append("-");
        sb.append(str.substring(6, 8));
        sb.append(" ");
        sb.append(str.substring(8, 10));
        sb.append(":");
        sb.append(str.substring(10, 12));
        sb.append(":");
        sb.append(str.substring(12, 14));
        return sb.toString();
    }

    public static Date getData(Date date,int type,int increase){
        Calendar time=Calendar.getInstance();
        time.setTime(date);
        time.add(type,increase);
        return time.getTime();
    }

    public static String getDateStr(Date date,String format) {
        if(null==date){
            return null;
        }
        SimpleDateFormat formatB =  new SimpleDateFormat(format);
        return formatB.format(date);
    }

    public static Date getDate(String dateStr,String format) {
        SimpleDateFormat formatB =  new SimpleDateFormat(format);
        try {
            return formatB.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return  null;
        }
    }

    //获取当前时间到晚上0点时间差
    public static long getNowToNextDaySeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
