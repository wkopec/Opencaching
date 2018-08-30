package com.example.opencaching.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.opencaching.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Wojtek on 22.07.2017.
 */

public class ResourceUtils {
    public static int getGeocacheSize(String size){
        switch (size) {
            case "none": return R.string.size_none;
            case "nano": return R.string.size_nano;
            case "micro": return R.string.size_micro;
            case "small": return R.string.size_small;
            case "regular": return R.string.size_regular;
            case "large": return R.string.size_large;
            case "xlarge": return R.string.size_xlarge;
            default: return 0;
        }
    }

    public static int getGeocacheType(String type){
        switch (type) {
            case "Traditional": return R.string.type_traditional;
            case "Other": return R.string.type_unknown;
            case "Quiz": return R.string.type_quiz;
            case "Multi": return R.string.type_multi;
            case "Virtual": return R.string.type_virtual;
            case "Own": return R.string.type_own;
            case "Moving": return R.string.type_moving;
            case "Event": return R.string.type_event;
            case "Webcam": return R.string.type_webcam;
            default: return 0;
        }
    }

    public static int getLogIcon(String type) {
        switch (type) {
            case "Found it": return R.drawable.ic_found;
            case "Didn't find it": return R.drawable.ic_not_found;
            case "Comment": return R.drawable.ic_comment;
            case "Ready to search": return R.drawable.ic_power;
            case "Archived": return R.drawable.ic_archive;
            case "Temporarily unavailable": return R.drawable.ic_warning;
            case "Needs maintenance": return R.drawable.ic_need_maintenance;
            case "Maintenance performed": return R.drawable.ic_maintenance_performed;
            case "Moved": return R.drawable.ic_moved;
            case "OC Team comment": return R.drawable.ic_cog;
            case "Attended": return R.drawable.ic_attended;
            case "Will attend": return R.drawable.ic_will_attend;
            default: return 0;
        }
    }

    public static int getLogIconColor(String type) {
        switch (type) {
            case "Found it": return R.color.green;
            case "Didn't find it": return R.color.red;
            case "Ready to search": return R.color.colorPrimaryDark;
            case "Temporarily unavailable": return R.color.colorAccent;
            default: return R.color.black;
        }
    }

    public static int getGeocacheIcon(String type){
        switch (type) {
            case "Traditional": return R.drawable.cache_traditional;
            case "Other": return R.drawable.cache_unknown;
            case "Quiz": return R.drawable.cache_quiz;
            case "Multi": return R.drawable.cache_multi;
            case "Virtual": return R.drawable.cache_virtual;
            case "Own": return R.drawable.cache_own;
            case "Moving": return R.drawable.cache_moving;
            case "Event": return R.drawable.cache_event;
            case "Webcam": return R.drawable.cache_webcam;
            default: return 0;
        }
    }

    public static int getGeocacheSelectedIcon(String type){
        switch (type) {
            case "Traditional": return R.drawable.cache_traditional_selected;
            case "Other": return R.drawable.cache_unknown_selected;
            case "Quiz": return R.drawable.cache_quiz_selected;
            case "Multi": return R.drawable.cache_multi_selected;
            case "Virtual": return R.drawable.cache_virtual_selected;
            case "Own": return R.drawable.cache_own_selected;
            case "Moving": return R.drawable.cache_moving_selected;
            case "Event": return R.drawable.cache_event_selected;
            case "Webcam": return R.drawable.cache_webcam_selected;
            default: return 0;
        }
    }

}
