package com.flavourfit.Helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelpers {

    /**
     * Method to get the current date in yyyy-mm-dd format
     *
     * @return -- String current date
     */
    public static String getCurrentDateString() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        return date;
    }

}
