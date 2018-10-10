package pl.opencaching.android.data.models.geocoding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }
}
