package com.example.opencaching.utils.events;

public class MapFilterChangeEvent {

    private boolean isAvailabilityChanged;

    public MapFilterChangeEvent(boolean isAvailabilityChanged) {
        this.isAvailabilityChanged = isAvailabilityChanged;
    }

    public boolean isAvailabilityChanged() {
        return isAvailabilityChanged;
    }
}
