package com.jasonbutwell.popularmovies.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J on 25/02/2017.
 */

public class DateTimeUtils {

    // Helper function to convert mins to hours and mins
    public static String convertToHoursMins( String duration ) {
        int MINUTES_IN_HOUR = 60;
        int SECONDS_IN_MINUTE = 60;

        String timeString = "";
        String hoursString = "hour";

        if ( duration != null && !duration.equals("")) {
            int hours = Integer.parseInt(duration) / MINUTES_IN_HOUR;
            int mins = Integer.parseInt(duration) % SECONDS_IN_MINUTE;

            // Hours or Hour?
            if ( hours == 0 )
                timeString = String.format(Locale.ENGLISH,"%d mins", mins);
            else {
                if (hours > 1)
                    hoursString = "hours";

                timeString = String.format(Locale.ENGLISH, "%d %s %d mins", hours, hoursString, mins);
            }

            //timeString = String.format(Locale.ENGLISH,"%d %s %d mins", hours, hoursString, mins);
        }

        return timeString;
    }

    // Formats the US date to a UK one
    public static String USDateToUKDate( String dateToParse ) {
        return formatDate("yyyy-MM-dd", dateToParse, "dd.MM.yyyy" );
    }

    // Helper routine to help reformat dates
    public static String formatDate(String dateFormat, String dateToParse, String dateOutputFormat ) {
        Date date = null;
        String formattedDate = null;

        if ( dateFormat != null && dateToParse != null && dateOutputFormat != null ) {
            try {
                date = new SimpleDateFormat(dateFormat,Locale.getDefault()).parse(dateToParse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedDate = new SimpleDateFormat(dateOutputFormat, Locale.getDefault()).format(date);
        }

        return formattedDate;
    }

}
