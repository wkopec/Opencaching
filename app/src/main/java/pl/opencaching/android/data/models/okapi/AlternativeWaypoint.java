package pl.opencaching.android.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Volfram on 16.07.2017.
 */

public class AlternativeWaypoint extends RealmObject {

    @SerializedName("name")
    @Expose
    private String name;        //short, unique "codename" for the waypoint
    @SerializedName("location")
    @Expose
    private String location;    //location of the waypoint in the "lat|lon" format
    @SerializedName("type")
    @Expose
    private String type;        //unique identifier for the type of waypoint
    @SerializedName("type_name")
    @Expose
    private String type_name;   //the human-readable name of the waypoint type
    @SerializedName("sym")
    @Expose
    private String sym;         //one of commonly recognized waypoint symbol names
    @SerializedName("description")
    @Expose
    private String description; //longer description of the waypoint

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getType_name() {
        return type_name;
    }

    public String getSym() {
        return sym;
    }

    public String getDescription() {
        return description;
    }

    public AlternativeWaypoint() {
    }
}
