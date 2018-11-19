package pl.opencaching.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import pl.opencaching.android.R;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Volfram on 16.07.2017.
 */

public class StringUtils {

    public static String OKAPI_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

    public static String getApiFormatedFields(ArrayList<String> fields) {
        StringBuilder apiFormatedWaypoints = new StringBuilder(fields.get(0));
        if (fields.size() > 1) {
            for (int i = 1; i < fields.size(); i++) {
                apiFormatedWaypoints.append("|").append(fields.get(i));
            }
        }
        return apiFormatedWaypoints.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getApiFormatedDate(Date date) {
         DateFormat dateformat = new SimpleDateFormat(OKAPI_DATE_FORMAT);
        return dateformat.format(date);
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

    public static String getTimeString(int hour, int minute, Context context) {
        DecimalFormat df = new DecimalFormat("00");
        return String.format(context.getString(R.string.separated_time), df.format(hour), df.format(minute));
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

    public static String getFormatedHtmlString(String httpString) {
        return httpString.replaceAll("<img", "<img style=\"max-width:100%; height: auto; width: auto;\"");
    }

    public static String getFormatedCoordinates(LatLng coordinates) {

        StringBuilder builder = new StringBuilder();
        DecimalFormat degreeFormat = new DecimalFormat("00");
        DecimalFormat minutesFormat = new DecimalFormat("00.000");

        if (coordinates.latitude < 0) {
            builder.append("S ");
        } else {
            builder.append("N ");
        }

        String latitudeDegrees = Location.convert(Math.abs(coordinates.latitude), Location.FORMAT_MINUTES);
        String[] latitudeSplit = latitudeDegrees.split(":");

        builder.append(degreeFormat.format(Integer.parseInt(latitudeSplit[0])));
        builder.append("° ");
        builder.append(minutesFormat.format(Float.parseFloat(latitudeSplit[1].replaceAll(",", "."))));
        builder.append("'");

        builder.append("  ");

        if (coordinates.longitude < 0) {
            builder.append("W ");
        } else {
            builder.append("E ");
        }

        String longitudeDegrees = Location.convert(Math.abs(coordinates.longitude), Location.FORMAT_MINUTES);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(degreeFormat.format(Integer.parseInt(longitudeSplit[0])));
        builder.append("° ");
        builder.append(minutesFormat.format(Float.parseFloat(longitudeSplit[1].replaceAll(",", "."))));
        builder.append("'");

        return builder.toString();
    }

    public static LatLng getLatLngFromString(String stringLatLng) {
        String[] location = stringLatLng.split("\\|");
        return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
    }

}
