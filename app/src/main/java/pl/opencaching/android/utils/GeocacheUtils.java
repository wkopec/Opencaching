package pl.opencaching.android.utils;

import java.util.ArrayList;
import java.util.List;

import pl.opencaching.android.R;

/**
 * Created by Wojtek on 22.07.2017.
 */

public class GeocacheUtils {

    public static float getSizeIntValue(String size) {
        switch (size) {
            case "none": return 0;
            case "nano": return 0.5f;
            case "micro": return 1;
            case "small": return 2;
            case "regular": return 3;
            case "large": return 4;
            case "xlarge": return 5;
            default: return 0;
        }
    }

    public static String getGeocacheShortSize(String size) {
        switch (size) {
            case "none": return "?";
            case "nano": return "XXS";
            case "micro": return "XS";
            case "small": return "S";
            case "regular": return "M";
            case "large": return "L";
            case "xlarge": return "XL";
            default: return "?";
        }
    }

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

    public static int getLogIconColor(String type) {
        switch (type) {
            case "Found it": return R.color.green;
            case "Didn't find it": return R.color.red;
            case "Ready to search": return R.color.colorPrimaryDark;
            case "Available": return R.color.colorPrimaryDark;
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

    public static int getLogType(String type) {
        switch (type) {
            case "Found it": return R.string.log_found_it;
            case "Didn't find it": return R.string.log_not_found;
            case "Comment": return R.string.log_comment;
            case "Available": return R.string.log_available;
            case "Archived": return R.string.log_archived;
            case "Temporarily unavailable": return R.string.log_temporarily_unavailable;
            //case "Needs maintenance": return R.drawable.ic_need_maintenance;
            //case "Maintenance performed": return R.drawable.ic_maintenance_performed;
            //case "Moved": return R.drawable.ic_moved;
            //case "OC Team comment": return R.drawable.ic_cog;
            case "Attended": return R.string.log_attended;
            case "Will attend": return R.string.log_will_attend;
            default: return 0;
        }
    }

    public static int getLogIcon(String type) {
        switch (type) {
            case "Found it": return R.drawable.ic_found;
            case "Didn't find it": return R.drawable.ic_not_found;
            case "Comment": return R.drawable.ic_comment;
            case "Ready to search": return R.drawable.ic_power;
            case "Available": return R.drawable.ic_power;
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

    public static int getAttributeIcon(String acode) {
        switch (acode) {
            case "A2": return R.drawable.ic_attribute_benchmark;     //W pobliżu punktu geodezyjnego
            case "A3": return R.drawable.ic_attribute_wigo;     //Wherigo Cache
            case "A4": return R.drawable.ic_attribute_letterbox;     //Skrzynka typu Letterbox
            case "A5": return R.drawable.ic_attribute_geo_hotel;     //GeoHotel Cache
            case "A6": return R.drawable.ic_attribute_magnetic;     //Przyczepiona magnesem
            case "A7": return R.drawable.ic_attribute_audio;     //Opis zawiera plik audio
            case "A8": return R.drawable.ic_attribute_offset;     //Offset cache
            case "A9": return R.drawable.ic_attribute_beacon;     //Beacon - Garmin Chirp
            case "A10": return R.drawable.ic_attribute_dead_drop;    //Dead Drop USB skrzynka
            case "A18": return R.drawable.ic_attribute_disabled;    //Dostępna dla niepełnosprawnych
            case "A20": return R.drawable.ic_attribute_trekking;    //Dostępna tylko pieszo
            case "A27": return R.drawable.ic_attribute_bike;    //Dostępna rowerem
            case "A28": return R.drawable.ic_attribute_nature;    //Umiejscowiona na łonie natury (lasy, góry, itp.)
            case "A29": return R.drawable.ic_attribute_historical;    //Miejsce historyczne
            case "A42": return R.drawable.ic_attribute_night;    //Zalecane szukanie nocą
            case "A49": return R.drawable.ic_attribute_compass;    //Potrzebny kompas
            case "A50": return R.drawable.ic_attribute_pencil;    //Weź coś do pisania
            case "A51": return R.drawable.ic_attribute_spade;    //Potrzebna łopatka
            case "A52": return R.drawable.ic_attribute_lighter;    //Potrzebna latarka
            case "A56": return R.drawable.ic_attribute_tool;    //Wymagany dodatkowy sprzęt
            case "A57": return R.drawable.ic_attribute_boat;    //Wymaga sprzętu pływającego
            case "A59": return R.drawable.ic_attribute_danger;    //Skrzynka niebezpieczna
            case "A68": return R.drawable.ic_attribute_hourglass;    //Szybka skrzynka
            case "A70": return R.drawable.ic_attribute_kids;    //Można zabrać dzieci
            case "A73": return R.drawable.ic_attribute_clock;    //Dostępna w określonych godzinach lub płatna
            default: return R.drawable.ic_attribute_unknown;
        }
    }



}
