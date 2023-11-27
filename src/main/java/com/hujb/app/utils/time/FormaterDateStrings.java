package com.hujb.app.utils.time;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FormaterDateStrings {
    public static String parseTimeHour(String instantString){
        var hour =  Instant.parse(instantString).atZone(ZoneId.of("UTC-3")).getHour();
        if(hour < 10){
            return "0" + hour;
        }
        return Integer.toString(hour);
    }

    public static String parseTimeMinute(String instantString){
        var minute =  Instant.parse(instantString).atZone(ZoneId.of("UTC-3")).getMinute();
        if(minute < 10){
            return "0" + minute;
        }
        return Integer.toString(minute);
    }

    public static String parseTimeDuration(Duration duration){
        var durationSeconds = duration.getSeconds();

        var hours = durationSeconds / 3600;
        var minutes = (durationSeconds % 3600) / 60;
        var seconds = (durationSeconds % 3600) % 60;

       return formateLessThan2Numbers(hours) + ":" + formateLessThan2Numbers(minutes) + ":" + formateLessThan2Numbers(seconds);
    }
    public static String parseTimeToDate(String instantString){
      return  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.parse(instantString).atZone(ZoneId.of("UTC-3")));
    };


    public static String formateLessThan2Numbers(long number){
        if(number < 10){
            return "0" + number;
        }
        return Long.toString(number);
    }
}
