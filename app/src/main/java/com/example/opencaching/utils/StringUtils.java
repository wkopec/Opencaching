package com.example.opencaching.utils;

import android.content.Context;
import android.location.Location;

import com.example.opencaching.R;
import com.google.android.gms.maps.model.LatLng;

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
            displayDate += date.getDayOfMonth() + " " + monhs[date.getMonthOfYear() - 1] + " " + date.getYear();
        }
        return displayDate;
    }

    public static String getOathToken(String url) {
        url = url.substring(url.indexOf("oauth_token"));
        String[] pairs = url.split("&");
        String token = pairs[0].replace("oauth_token=", "");
        return token;
    }

    public static String getOathTokenSecret(String url) {
        url = url.substring(url.indexOf("oauth_token_secret"));
        String[] pairs = url.split("&");
        String tokenSecret = pairs[0].replace("oauth_token_secret=", "");
        return tokenSecret;
    }

    public static String getOathVerifier(String url) {
        url = url.substring(url.indexOf("oauth_token"));
        String[] pairs = url.split("&");
        String verifier = pairs[1].replace("oauth_verifier=", "");
        return verifier;
    }

    public static String getFormatedCoordinates(LatLng coordinates) {

        StringBuilder builder = new StringBuilder();

        if (coordinates.latitude < 0) {
            builder.append("S ");
        } else {
            builder.append("N ");
        }

        String latitudeDegrees = Location.convert(Math.abs(coordinates.latitude), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");
        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(latitudeSplit[1]);
        builder.append("'");
        builder.append(latitudeSplit[2]);
        builder.append("\"");

        builder.append(" ");

        if (coordinates.longitude < 0) {
            builder.append("W ");
        } else {
            builder.append("E ");
        }

        String longitudeDegrees = Location.convert(Math.abs(coordinates.longitude), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(longitudeSplit[1]);
        builder.append("'");
        builder.append(longitudeSplit[2]);
        builder.append("\"");

        return builder.toString();
    }

}
