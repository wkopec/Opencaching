package pl.opencaching.android.utils.events;

public class MapTypeChangeEvent {

    private int mapType;

    public MapTypeChangeEvent(int mapType) {
        this.mapType = mapType;
    }

    public int getMapType() {
        return mapType;
    }
}
