package com.example.campus_services;

import android.util.Log;

import java.util.Calendar;

public class CalendarUtils {

    private static final long SLOT1_START = 12*3600 + 20*60;
    private static final long SLOT2_START = 16*3600 + 20*60;
    private static final long SLOT1_END = 13*3600 + 30*60;
    private static final long SLOT2_END = 17*3600 + 30*60;

    public static String getTomorrowsDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        String tomorrow = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-" +
                String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" +
                String.valueOf(calendar.get(Calendar.YEAR));
        return tomorrow;
    }

    public static String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "-" +
                String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-" +
                String.valueOf(calendar.get(Calendar.YEAR));
        return today;
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

    public static String getDate(int slot){
        long ct = getCurrentTime();
        String date = null;
        if(slot == 1){
            if(ct < SLOT1_START){
                date = getTodayDate();
            }else{
                date = getTomorrowsDate();
            }
        }
        if(slot == 2){
            if(ct < SLOT2_START){
                date = getTodayDate();
            }else{
                date = getTomorrowsDate();
            }
        }
        return date;
    }

    public static boolean isPastAppointment(String date,int slot){
        String today = getTodayDate();
        String tomorrow = getTomorrowsDate();
        long ct = getCurrentTime();
        boolean ans = false;
        if(date.equals(today)){
            if(slot == 1 && ct >= SLOT1_END){
                ans = true;
            }
            if(slot == 2 && ct >= SLOT2_END){
                ans = true;
            }
        }
        if(!date.equals(today) && !date.equals(tomorrow))
            ans = true;
        return ans;
    }
}
