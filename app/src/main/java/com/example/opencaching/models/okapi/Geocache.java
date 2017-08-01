package com.example.opencaching.models.okapi;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Geocache implements ClusterItem {

    @SerializedName("code")
    @Expose
    private String code;                //unique Opencaching code of the geocache
    @SerializedName("name")
    @Expose
    private String name;                //name of the geocache
    @SerializedName("location")
    @Expose
    private String location;            //location of the cache in the "lat|lon" format
    @SerializedName("type")
    @Expose
    private String type;                //cache type (e.g. Traditional/Multi/Quiz...)
    @SerializedName("status")
    @Expose
    private String status;              //cache status (e.g. Available/Temporarily unavailable/Archived)
    @SerializedName("needs_maintenance")
    @Expose
    private boolean needsMaintenance;   //true if the geocache is known to be in poor condition, e.g. damaged
    @SerializedName("url")
    @Expose
    private String geocacheUrl;         //URL of the cache's web page
    @SerializedName("owner")
    @Expose
    private User owner;                 //Owner of the geocache
    @SerializedName("gc_code")
    @Expose
    private String gcCode;              //Geocaching.com code
    @SerializedName("distance")
    @Expose
    private float distance;             //the distance to a cache, in meters
    @SerializedName("is_found")
    @Expose
    private boolean found;              //true if the user already found this cache
    @SerializedName("is_not_found")
    @Expose
    private boolean notFound;           //true if the user has submitted "Didn't find it" log entry for this cache
    @SerializedName("is_watched")
    @Expose
    private boolean watched;            //true if the user is watching this cache.
    @SerializedName("is_ignored")
    @Expose
    private boolean ignored;            //true if the user is ignoring this cache
    @SerializedName("founds")
    @Expose
    private int founds;                 //number of times the geocache was successfully found
    @SerializedName("notfounds")
    @Expose
    private int notfounds;              //number of times the geocache was not found
    @SerializedName("willattends")
    @Expose
    private int willattends;            //n case of Event Caches, this is the number of "Will attend" log entries
    @SerializedName("size2")
    @Expose
    private String size;                //the size of the container (e.g. none/nano/micro/small/regular/large/xlarge/other)
    @SerializedName("difficulty")
    @Expose
    private float difficulty;           //difficulty rating of the cache (between 1 and 5)
    @SerializedName("terrain")
    @Expose
    private float terrain;              //terrain rating of the cache (between 1 and 5)
    @SerializedName("tripTime")
    @Expose
    private float trip_time;            //approximate total amount of time needed to find the cache, in hours
    @SerializedName("trip_distance")
    @Expose
    private float tripDistance;         //approximate total distance needed to find the cache, in kilometers
    @SerializedName("rating")
    @Expose
    private float rating;               //an overall rating of the cache (between 1 and 5)
    @SerializedName("rating_votes")
    @Expose
    private int ratingVotes;            //number of votes submitted for the rating of this cache
    @SerializedName("recommendations")
    @Expose
    private int recommendations;        //number of recommendations for this cache
    @SerializedName("req_passwd")
    @Expose
    private boolean passwordRequired;   //states if this cache requires a password in order to submit a "Found it" log entry
    @SerializedName("short_description")
    @Expose
    private String shortSescription;    //a plaintext string with a single line (very short) description of the cache
    @SerializedName("description")
    @Expose
    private String description;         //description of the cache (in HTML)
    @SerializedName("hint2")
    @Expose
    private String hint;                //cache hints/spoilers
    @SerializedName("images")
    @Expose
    private ArrayList<Image> images;    //list of dictionaries
    @SerializedName("attr_acodes")
    @Expose
    private ArrayList<String> attrAcodes;   //unordered list of OKAPI geocache-attribute IDs (A-codes) with which the cache was tagged
    @SerializedName("latest_logs")
    @Expose
    private ArrayList<Log> latestLogs;  //a couple of latest log entries in the cache
    @SerializedName("my_notes")
    @Expose
    private String notes;               //user's notes on the cache
    @SerializedName("trackables_count")
    @Expose
    private int trackablesCount;        //a total number of trackables hidden within the cache
    @SerializedName("trackables")
    @Expose
    private ArrayList<Trackable> trackables;    //list of dictionaries, each dictionary represents one trackable hidden within the cache container
    @SerializedName("alt_wpts")
    @Expose
    private ArrayList<AlternativeWaypoint> alternateAlternativeWaypoints;
    @SerializedName("state")
    @Expose
    private String state;               //name of the state the cache is placed in
    @SerializedName("last_found")
    @Expose
    private String lastFound;           //date and time when the geocache was last found (ISO 8601)
    @SerializedName("last_modified")
    @Expose
    private String lastModified;        //date and time when the geocache was last modified (ISO 8601)
    @SerializedName("date_created")
    @Expose
    private String createdDate;         //date and time when the geocache was listed at the Opencaching site (ISO 8601)
    @SerializedName("date_hidden")
    @Expose
    private String hiddenDate;          //date and time when (the geocache was first hidden / the geocache was first published / the event takes place) (ISO 8601)

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public boolean isNeedsMaintenance() {
        return needsMaintenance;
    }

    public String getGeocacheUrl() {
        return geocacheUrl;
    }

    public User getOwner() {
        return owner;
    }

    public String getGcCode() {
        return gcCode;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isFound() {
        return found;
    }

    public boolean isNotFound() {
        return notFound;
    }

    public boolean isWatched() {
        return watched;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public int getFounds() {
        return founds;
    }

    public int getNotfounds() {
        return notfounds;
    }

    public int getWillattends() {
        return willattends;
    }

    public String getSize() {
        return size;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public float getTerrain() {
        return terrain;
    }

    public float getTrip_time() {
        return trip_time;
    }

    public float getTripDistance() {
        return tripDistance;
    }

    public float getRating() {
        return rating;
    }

    public int getRatingVotes() {
        return ratingVotes;
    }

    public int getRecommendations() {
        return recommendations;
    }

    public boolean isPasswordRequired() {
        return passwordRequired;
    }

    public String getShortSescription() {
        return shortSescription;
    }

    public String getDescription() {
        return description;
    }

    public String getHint() {
        return hint;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<String> getAttrAcodes() {
        return attrAcodes;
    }

    public ArrayList<Log> getLatestLogs() {
        return latestLogs;
    }

    public String getNotes() {
        return notes;
    }

    public int getTrackablesCount() {
        return trackablesCount;
    }

    public ArrayList<Trackable> getTrackables() {
        return trackables;
    }

    public ArrayList<AlternativeWaypoint> getAlternateAlternativeWaypoints() {
        return alternateAlternativeWaypoints;
    }

    public String getState() {
        return state;
    }

    public String getLastFound() {
        return lastFound;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getHiddenDate() {
        return hiddenDate;
    }

    @Override
    public LatLng getPosition() {
        String[] location = getLocation().split("\\|");
        return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return code;
    }
}