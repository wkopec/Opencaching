package com.example.opencaching.events;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class SearchMapEvent {
    private String querry;

    public SearchMapEvent(String querry) {
        this.querry = querry;
    }

    public String getQuerry() {
        return querry;
    }
}
