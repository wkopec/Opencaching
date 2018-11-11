package pl.opencaching.android.utils;

import pl.opencaching.android.R;

import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_EVENT;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_EWBCAM;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_MOVING;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_MULTI;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_OTHER;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_OWN;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_QUIZ;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_TRADITIONAL;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_VIRTUAL;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_ARCHIVED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_ATTENDED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_AVAILABLE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_MAINTENANCE_PERFORMED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_MOVED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_NEEDS_MAINTENANCE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_NOT_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_OC_TEAM_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_READY_TO_SEARCH;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_TEMP_UNAVAILABLE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_WILL_ATTEND;

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
            case GEOCACHE_TYPE_TRADITIONAL: return R.string.type_traditional;
            case GEOCACHE_TYPE_OTHER: return R.string.type_unknown;
            case GEOCACHE_TYPE_QUIZ: return R.string.type_quiz;
            case GEOCACHE_TYPE_MULTI: return R.string.type_multi;
            case GEOCACHE_TYPE_VIRTUAL: return R.string.type_virtual;
            case GEOCACHE_TYPE_OWN: return R.string.type_own;
            case GEOCACHE_TYPE_MOVING: return R.string.type_moving;
            case GEOCACHE_TYPE_EVENT: return R.string.type_event;
            case GEOCACHE_TYPE_EWBCAM: return R.string.type_webcam;
            default: return 0;
        }
    }

    public static int getLogIconColor(String type) {
        switch (type) {
            case LOG_TYPE_FOUND: return R.color.green;
            case LOG_TYPE_NOT_FOUND: return R.color.red;
            case LOG_TYPE_READY_TO_SEARCH: return R.color.colorPrimaryDark;
            case LOG_TYPE_AVAILABLE: return R.color.colorPrimaryDark;
            case LOG_TYPE_TEMP_UNAVAILABLE: return R.color.colorAccent;
            default: return R.color.black;
        }
    }

    public static int getGeocacheIcon(String type){
        switch (type) {
            case GEOCACHE_TYPE_TRADITIONAL: return R.drawable.cache_traditional;
            case GEOCACHE_TYPE_OTHER: return R.drawable.cache_unknown;
            case GEOCACHE_TYPE_QUIZ: return R.drawable.cache_quiz;
            case GEOCACHE_TYPE_MULTI: return R.drawable.cache_multi;
            case GEOCACHE_TYPE_VIRTUAL: return R.drawable.cache_virtual;
            case GEOCACHE_TYPE_OWN: return R.drawable.cache_own;
            case GEOCACHE_TYPE_MOVING: return R.drawable.cache_moving;
            case GEOCACHE_TYPE_EVENT: return R.drawable.cache_event;
            case GEOCACHE_TYPE_EWBCAM: return R.drawable.cache_webcam;
            default: return 0;
        }
    }

    public static int getGeocacheSelectedIcon(String type){
        switch (type) {
            case GEOCACHE_TYPE_TRADITIONAL: return R.drawable.cache_traditional_selected;
            case GEOCACHE_TYPE_OTHER: return R.drawable.cache_unknown_selected;
            case GEOCACHE_TYPE_QUIZ: return R.drawable.cache_quiz_selected;
            case GEOCACHE_TYPE_MULTI: return R.drawable.cache_multi_selected;
            case GEOCACHE_TYPE_VIRTUAL: return R.drawable.cache_virtual_selected;
            case GEOCACHE_TYPE_OWN: return R.drawable.cache_own_selected;
            case GEOCACHE_TYPE_MOVING: return R.drawable.cache_moving_selected;
            case GEOCACHE_TYPE_EVENT: return R.drawable.cache_event_selected;
            case GEOCACHE_TYPE_EWBCAM: return R.drawable.cache_webcam_selected;
            default: return 0;
        }
    }

    public static int getLogType(String type) {
        switch (type) {
            case LOG_TYPE_FOUND: return R.string.log_found_it;
            case LOG_TYPE_NOT_FOUND: return R.string.log_not_found;
            case LOG_TYPE_COMMENT: return R.string.log_comment;
            case LOG_TYPE_AVAILABLE: return R.string.log_available;
            case LOG_TYPE_ARCHIVED: return R.string.log_archived;
            case LOG_TYPE_TEMP_UNAVAILABLE: return R.string.log_temporarily_unavailable;
            //case "Needs maintenance": return R.drawable.ic_need_maintenance;
            //case "Maintenance performed": return R.drawable.ic_maintenance_performed;
            //case "Moved": return R.drawable.ic_moved;
            //case "OC Team comment": return R.drawable.ic_cog;
            case LOG_TYPE_ATTENDED: return R.string.log_attended;
            case LOG_TYPE_WILL_ATTEND: return R.string.log_will_attend;
            default: return 0;
        }
    }

    public static int getLogIcon(String type) {
        switch (type) {
            case LOG_TYPE_FOUND: return R.drawable.ic_found;
            case LOG_TYPE_NOT_FOUND: return R.drawable.ic_not_found;
            case LOG_TYPE_COMMENT: return R.drawable.ic_comment;
            case LOG_TYPE_READY_TO_SEARCH: return R.drawable.ic_power;
            case LOG_TYPE_AVAILABLE: return R.drawable.ic_power;
            case LOG_TYPE_ARCHIVED: return R.drawable.ic_archive;
            case LOG_TYPE_TEMP_UNAVAILABLE: return R.drawable.ic_warning;
            case LOG_TYPE_NEEDS_MAINTENANCE: return R.drawable.ic_need_maintenance;
            case LOG_TYPE_MAINTENANCE_PERFORMED: return R.drawable.ic_maintenance_performed;
            case LOG_TYPE_MOVED: return R.drawable.ic_moved;
            case LOG_TYPE_OC_TEAM_COMMENT: return R.drawable.ic_cog;
            case LOG_TYPE_ATTENDED: return R.drawable.ic_attended;
            case LOG_TYPE_WILL_ATTEND: return R.drawable.ic_will_attend;
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
