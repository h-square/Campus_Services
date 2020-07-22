package com.example.campus_services;

import android.util.Log;

import java.util.Calendar;

public class CalendarUtils {
    public static String getTomorrowsDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        String tomorrow = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-" +
                String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" +
                String.valueOf(calendar.get(Calendar.YEAR));
        return tomorrow;
    }

    public static long getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        long time = calendar.get(Calendar.HOUR_OF_DAY)*3600 + calendar.get(Calendar.MINUTE)*60 +calendar.get(Calendar.SECOND);
        return time;
    }

    public static String convertTimeFromSectoHHMM(long sec){
        long hour = sec/3600;
        long min = (sec%3600)/60;
        String ans = String.valueOf(hour) + ":" + String.valueOf(min);
        return ans;
    }

    public static boolean isTomorrow(String date){
        String tomorrowDate = getTomorrowsDate();
        if(date.equals(tomorrowDate)) return true;
        return false;
    }

}
