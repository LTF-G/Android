package com.example.khulazy.ui.notifications;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeActivity {
    public int hour;
    public int minute;

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

    public TimeActivity(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("hour = ").append(hour);
        sb.append(", minute = ").append(minute);
        return sb.toString();
    }
}
