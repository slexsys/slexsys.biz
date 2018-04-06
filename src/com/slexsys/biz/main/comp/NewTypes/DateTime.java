package com.slexsys.biz.main.comp.NewTypes;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by slexsys on 1/25/17.
 */

public class DateTime implements Serializable{
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    //region constructors
    public DateTime() {
        this.year =
        this.month =
        this.day =
        this.hour =
        this.minute =
        this.second = 0;
    }

    public DateTime(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour =
        this.minute =
        this.second = 0;
    }

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    //endregion

    //region getters setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
    //endregion

    public static DateTime fromSQLFormat(String text) {
        DateTime result = new DateTime();
        String[] arr1 = text.split(" ");
        if (arr1.length > 0) {
            String[] arr2 = arr1[0].split("-");
            if (arr2.length > 0) {
                int i = 0;
                result.setYear(Integer.parseInt(arr2[i++]));
                result.setMonth(Integer.parseInt(arr2[i++]));
                result.setDay(Integer.parseInt(arr2[i++]));
            }
        }
        if (arr1.length > 1) {
            String[] arr2 = arr1[1].split(":");
            if (arr2.length > 0) {
                int i = 0;
                result.setHour(Integer.parseInt(arr2[i++]));
                result.setMinute(Integer.parseInt(arr2[i++]));
                result.setSecond(Integer.parseInt(arr2[i++]));
            }
        }
        return result;
    }

    public String toNormalDateFormat() {
        return normilize(day) + "-" +
                normilize(month) + "-" +
                year;
    }

    public String toNormalDateTimeFormat() {
        return normilize(day) + "-" +
                normilize(month) + "-" +
                year + " " +
                normilize(hour) + ":" +
                normilize(minute) + ":" +
                normilize(second);
    }

    public String toSQLFormatDate() {
        return year + "-" +
                normilize(month) + "-" +
                normilize(day);
    }

    public String toSQLFormatDateTime() {
        return year + "-" +
                normilize(month) + "-" +
                normilize(day) + " " +
                normilize(hour) + ":" +
                normilize(minute) + ":" +
                normilize(second);
    }

    public static DateTime now() {
        final Calendar calendar = Calendar.getInstance();
        return new DateTime(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String normilize(int value) {
        String result = Integer.toString(value);
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }
}
