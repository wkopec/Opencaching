package com.example.opencaching.utils;

import android.content.Context;

import com.example.opencaching.R;

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

}
