package pl.opencaching.android.data.models.okapi;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogInterface;

/**
 * Created by Volfram on 16.07.2017.
 */

public class GeocacheLog extends RealmObject implements GeocacheLogInterface {

    @SerializedName("uuid")
    @Expose
    @PrimaryKey
    private String uuid;            //log ID
    @SerializedName("date")
    @Expose
    private Date date;              //date and time when the log was added
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

    public GeocacheLog(String uuid, String geocacheCode, Date date, User user, String type, String comment) {
        this.uuid = uuid;
        this.date = date;
        this.user = user;
        this.type = type;
        this.comment = comment;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public DateTime getDateTime() {
        return new DateTime(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    @Override
    public RealmList<Image> getImages() {
        return images;
    }

    @Override
    public Boolean isReadyToSync() {
        return null;        // GeocacheLog is always synced, so it can not be ready to sync
    }

    @Override
    public int compareTo(@NonNull GeocacheLogInterface geocacheLog) {
        //return getDateTime().compareTo(geocacheLog.getDateTime());
        return geocacheLog.getDateTime().compareTo(getDateTime());
    }
}
