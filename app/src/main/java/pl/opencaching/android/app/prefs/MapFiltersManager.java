package pl.opencaching.android.app.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class MapFiltersManager {

    private static final String KEY_MAP_FILTER_FOUND = "map_filter_found:";
    private static final String KEY_MAP_FILTER_NOT_FOUND = "map_filter_not_found:";
    private static final String KEY_MAP_FILTER_OWNED = "map_filter_owned:";
    private static final String KEY_MAP_FILTER_IGNORED = "map_filter_ignored:";
    private static final String KEY_MAP_FILTER_TRACKABLE = "map_filter_trackable:";
    private static final String KEY_MAP_FILTER_AVAILABLE = "map_filter_available:";
    private static final String KEY_MAP_FILTER_TEMP_UNAVAILABLE = "map_filter_temp_unavailable:";
    private static final String KEY_MAP_FILTER_ARCHIVED = "map_filter_archived:";
    private static final String KEY_MAP_FILTER_FTF = "map_filter_ftf:";
    private static final String KEY_MAP_FILTER_POWER_TRAIL = "map_filter_power_trail:";

    private static final String KEY_MAP_FILTER_TRADITIONAL = "map_filter_traditional:";
    private static final String KEY_MAP_FILTER_MULTICACHE = "map_filter_multicache:";
    private static final String KEY_MAP_QUIZ = "map_filter_quiz:";
    private static final String KEY_MAP_UNKNOWN = "map_filter_unknown:";
    private static final String KEY_MAP_VIRTUAL = "map_filter_virtual:";
    private static final String KEY_MAP_EVENT = "map_filter_event:";
    private static final String KEY_MAP_OWNCACHE = "map_filter_owncache:";
    private static final String KEY_MAP_MOVING = "map_filter_moving:";
    private static final String KEY_MAP_WEBCAM = "map_filter_webcam:";

    private final SharedPreferences prefs;

    @Inject
    public MapFiltersManager(SharedPreferences preferences) {
        this.prefs = preferences;
    }

    private SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

//    STANDARD FILTERS

    public void saveFoundFilter(boolean isFoundFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_FOUND, isFoundFilter).commit();
    }

    public boolean isFoundFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_FOUND, false);
    }

    public void saveNotFoundFilter(boolean isNotFoundFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_NOT_FOUND, isNotFoundFilter).commit();
    }

    public boolean isNotFoundFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_NOT_FOUND, true);
    }

    public void saveOwnedFilter(boolean isOwnedFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_OWNED, isOwnedFilter).commit();
    }

    public boolean isOwnedFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_OWNED, true);
    }

    public void saveIgnoredFilter(boolean isIgnoredFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_IGNORED, isIgnoredFilter).commit();
    }

    public boolean isIgnoredFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_IGNORED, false);
    }

    public void saveTrackableFilter(boolean isTrackableFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_TRACKABLE, isTrackableFilter).commit();
    }

    public boolean isTrackableFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_TRACKABLE, false);
    }

    public void saveAvailableFilter(boolean isAvailableFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_AVAILABLE, isAvailableFilter).commit();
    }

    public boolean isAvailableFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_AVAILABLE, true);
    }

    public void saveTempUnavailableFilter(boolean isTempUnavailableFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_TEMP_UNAVAILABLE, isTempUnavailableFilter).commit();
    }

    public boolean isTempUnavailableFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_TEMP_UNAVAILABLE, false);
    }

    public void saveArchivedFilter(boolean isArchivedFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_ARCHIVED, isArchivedFilter).commit();
    }

    public boolean isArchivedFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_ARCHIVED, false);
    }

    public void saveFTFFilter(boolean isFTFFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_FTF, isFTFFilter).commit();
    }

    public boolean isFTFFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_FTF, false);
    }

    public void savePowerTrailFilter(boolean isPowerTrailFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_POWER_TRAIL, isPowerTrailFilter).commit();
    }

    public boolean isPowerTrailFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_POWER_TRAIL, false);
    }

//    GEOCACHE FILTERS

    public void saveTraditionalFilter(boolean isTraditionalFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_TRADITIONAL, isTraditionalFilter).commit();
    }

    public boolean isTraditionalFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_TRADITIONAL, true);
    }
    public void saveMulticacheFilter(boolean isMulticacheFilter) {
        getEditor().putBoolean(KEY_MAP_FILTER_MULTICACHE, isMulticacheFilter).commit();
    }

    public boolean isMulticacheFilter() {
        return prefs.getBoolean(KEY_MAP_FILTER_MULTICACHE, true);
    }
    public void saveQuizFilter(boolean isQuizFilter) {
        getEditor().putBoolean(KEY_MAP_QUIZ, isQuizFilter).commit();
    }

    public boolean isQuizFilter() {
        return prefs.getBoolean(KEY_MAP_QUIZ, true);
    }
    public void saveUnknownFilter(boolean isUnknownFilter) {
        getEditor().putBoolean(KEY_MAP_UNKNOWN, isUnknownFilter).commit();
    }

    public boolean isUnknownFilter() {
        return prefs.getBoolean(KEY_MAP_UNKNOWN, true);
    }
    public void saveVirtualFilter(boolean isVirtualFilter) {
        getEditor().putBoolean(KEY_MAP_VIRTUAL, isVirtualFilter).commit();
    }

    public boolean isVirtualFilter() {
        return prefs.getBoolean(KEY_MAP_VIRTUAL, true);
    }
    public void saveEventFilter(boolean isEventFilter) {
        getEditor().putBoolean(KEY_MAP_EVENT, isEventFilter).commit();
    }

    public boolean isEventFilter() {
        return prefs.getBoolean(KEY_MAP_EVENT, true);
    }
    public void saveOwncacheFilter(boolean isOwnFilter) {
        getEditor().putBoolean(KEY_MAP_OWNCACHE, isOwnFilter).commit();
    }

    public boolean isOwncacheFilter() {
        return prefs.getBoolean(KEY_MAP_OWNCACHE, true);
    }
    public void saveMovingFilter(boolean isMovingFilter) {
        getEditor().putBoolean(KEY_MAP_MOVING, isMovingFilter).commit();
    }

    public boolean isMovingFilter() {
        return prefs.getBoolean(KEY_MAP_MOVING, true);
    }
    public void saveWebcamFilter(boolean isWebcamFilter) {
        getEditor().putBoolean(KEY_MAP_WEBCAM, isWebcamFilter).commit();
    }

    public boolean isWebcamFilter() {
        return prefs.getBoolean(KEY_MAP_WEBCAM, true);
    }

}
