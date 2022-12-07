package com.example.khulazy.ui.Alarm;

public class TimeActivity2 {
    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;

    public int getStartHour() {
        return startHour;
    }
    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }
    public int getEndMinute() {
        return endMinute;
    }


    public TimeActivity2(int startHour, int startMinute, int endHour, int endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("start hour = ").append(startHour);
        sb.append(", start minute = ").append(startMinute);
        sb.append(", end hour = ").append(endHour);
        sb.append(", end minute = ").append(endMinute);
        return sb.toString();
    }
}