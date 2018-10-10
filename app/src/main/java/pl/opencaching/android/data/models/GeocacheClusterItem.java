package pl.opencaching.android.data.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class GeocacheClusterItem implements ClusterItem {

    private String name;
    private String code;
    private String type;
    private LatLng location;

    public GeocacheClusterItem(String name, String code, String type, LatLng location) {
        this.name = name;
        this.code = code;
        this.location = location;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public LatLng getPosition() {
        return location;
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
