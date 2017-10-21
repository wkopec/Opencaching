package com.example.opencaching.utils;

import android.content.Context;

import com.example.opencaching.R;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Volfram on 16.07.2017.
 */

public class StringUtils {

    public static String getApiFormatedFields(ArrayList<String> fields) {
        String apiFormatedWaypoints = fields.get(0);
        if (fields.size() > 1) {
            for (int i = 1; i < fields.size(); i++) {
                apiFormatedWaypoints += "|" + fields.get(i);
            }
        }
        return apiFormatedWaypoints;
    }

    public static String getDateString(DateTime date, Context context) {
        DateTime today = new DateTime();
        String displayDate = "";
        if (date.toLocalDate().equals(today.toLocalDate()))
            displayDate += context.getString(R.string.today);
        else if(date.toLocalDate().equals(today.minusDays(1).toLocalDate()))
            displayDate += context.getString(R.string.yesterday);
        else {
            String[] monhs = context.getResources().getStringArray(R.array.months_abbreviations);
            displayDate += date.getDayOfMonth() + " " + monhs[date.getMonthOfYear() -1] + " " + date.getYear();
        }
        return displayDate;
    }



}
