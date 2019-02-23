package com.fuyou.play.util.HORUtils;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class BornInfo {

    private int year, month, day, hour, minute, daylight, timezone;
    private double lon,lat;

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

    public int getDaylight() {
        return daylight;
    }

    public void setDaylight(int daylight) {
        this.daylight = daylight;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "{" +
                "year=" + year +
                ", month=" + month +
                ", date=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", dayline=" + daylight +
                ", timezone=" + timezone +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
