package pl.opencaching.android.utils;

/**
 * Created by Volfram on 21.07.2017.
 */

public class Constants {

    //TODO: Generate new keys and move to properties
    public static final String OPENCACHING_CONSUMER_KEY = "eSLCuVzRJJsYUUfUs6Su";
    public static final String OPENCACHING_CONSUMER_KEY_SECRET = "nGdfx64ksCvwk7XDSnmsVCgGpnsFh9ZwG4B5Yweg";

    public static final String USERNAME_FIELDS = "uuid|username|profile_url|is_admin|date_registered|caches_found|caches_notfound|caches_hidden|rcmds_given|home_location";
    public static final String GEOCACHES_STANDARD_FIELDS = "code|name|location|type|status|url|owner|gc_code|is_found|is_not_found|is_watched|founds|notfounds|willattends|size2|difficulty|terrain|trip_time|trip_distance|rating|rating_votes|recommendations|req_passwd|short_description|description|hint2|images|attr_acodes|my_notes|trackables_count|trackables|date_created|date_hidden|last_found|latest_logs";
    public static final String LOGS_STANDARD_FIELDS = "uuid|date|user|type|comment|images|was_recommended";

    // Log types
    public static final String LOG_TYPE_FOUND = "Found it";
    public static final String LOG_TYPE_NOT_FOUND = "Didn't find it";
    public static final String LOG_TYPE_COMMENT = "Comment";
    public static final String LOG_TYPE_AVAILABLE = "Available";
    public static final String LOG_TYPE_ARCHIVED = "Archived";
    public static final String LOG_TYPE_TEMP_UNAVAILABLE = "Temporarily unavailable";
    public static final String LOG_TYPE_ATTENDED = "Attended";
    public static final String LOG_TYPE_WILL_ATTEND = "Will attend";
    public static final String LOG_TYPE_READY_TO_SEARCH = "Ready to search";
    public static final String LOG_TYPE_NEEDS_MAINTENANCE = "Needs maintenance";
    public static final String LOG_TYPE_MAINTENANCE_PERFORMED = "Maintenance performed";
    public static final String LOG_TYPE_MOVED = "Moved";
    public static final String LOG_TYPE_OC_TEAM_COMMENT = "OC Team comment";

    // Geocache types
    public static final String GEOCACHE_TYPE_TRADITIONAL = "Traditional";
    public static final String GEOCACHE_TYPE_OTHER = "Other";
    public static final String GEOCACHE_TYPE_QUIZ = "Quiz";
    public static final String GEOCACHE_TYPE_MULTI = "Multi";
    public static final String GEOCACHE_TYPE_VIRTUAL = "Virtual";
    public static final String GEOCACHE_TYPE_OWN = "Own";
    public static final String GEOCACHE_TYPE_MOVING = "Moving";
    public static final String GEOCACHE_TYPE_EVENT = "Event";
    public static final String GEOCACHE_TYPE_EWBCAM = "Webcam";

    // Geocache statuses
    public static final String GEOCACHE_STATUS_AVAILABLE = "Available";
    public static final String GEOCACHE_STATUS_TEMP_UNAVAILABLE = "Temporarily unavailable";
    public static final String GEOCACHE_STATUS_ARCHIVED = "Archived";
}
