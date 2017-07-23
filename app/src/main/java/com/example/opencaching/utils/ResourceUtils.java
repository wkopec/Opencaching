package com.example.opencaching.utils;

import com.example.opencaching.R;

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
