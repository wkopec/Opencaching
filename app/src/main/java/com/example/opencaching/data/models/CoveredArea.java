package com.example.opencaching.data.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static com.example.opencaching.utils.IntegerUtils.getDistance;

/**
 * Created by Volfram on 22.07.2017.
 */

public class CoveredArea {
    private ArrayList<Area> coveredArea;

    public CoveredArea() {
        coveredArea = new ArrayList<>();
    }

    public boolean isWithin(LatLng point) {
        for(Area area : coveredArea)
            if(getDistance(area.getCenter(), point) < area.getRadius())
                return true;
        return false;
    }

    public void addArea(LatLng center, int radius){
        coveredArea.add(new Area(center, radius));
    }

    public void clear() {
        coveredArea.clear();
    }

    private class Area {
        private LatLng center;
        private int radius;

        public Area(LatLng center, int radius) {
            this.center = center;
            this.radius = radius;
        }

        public LatLng getCenter() {
            return center;
        }

        public int getRadius() {
            return radius;
        }
    }

}
