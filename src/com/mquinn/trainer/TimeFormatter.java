package com.mquinn.trainer;

public class TimeFormatter {

    public static String millisToTime(long millis){

        long milliseconds = millis % 1000;
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d.%d", hour, minute, second, milliseconds);

    }

}
