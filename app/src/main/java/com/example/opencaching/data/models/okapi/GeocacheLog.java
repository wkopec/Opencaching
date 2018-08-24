package com.example.opencaching.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Volfram on 16.07.2017.
 */

public class GeocacheLog extends RealmObject {

    @SerializedName("uuid")
    @Expose
    private String uuid;            //log ID
    @SerializedName("date")
    @Expose
    private String date;            //date and time when the log was added
    @SerializedName("user")
    @Expose
    private User user;              //authot of the log
    @SerializedName("type")
    @Expose
    private String type;            //type of a log
    @SerializedName("comment")
    @Expose
    private String comment;         //full comment of a log
    @SerializedName("was_recommended")
    @Expose
    private boolean isRecommended;  //true if the author included his recommendation in this log entry
    @SerializedName("images")
    @Expose
    private RealmList<Image> images;     //list of dictionaries

    public GeocacheLog() {
    }

    public GeocacheLog(String uuid, String date, User user, String type, String comment) {
        this.uuid = uuid;
        this.date = date;
        this.user = user;
        this.type = type;
        this.comment = comment;
    }

    public String getUuid() {
        return uuid;
    }

    public DateTime getDate() {
        return new DateTime(date);
    }

    public User getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public RealmList<Image> getImages() {
        return images;
    }
}
