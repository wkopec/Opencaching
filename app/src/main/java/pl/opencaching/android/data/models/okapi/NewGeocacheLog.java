package pl.opencaching.android.data.models.okapi;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static dagger.internal.Preconditions.checkNotNull;

public class NewGeocacheLog  extends RealmObject {

    @SerializedName("log_uuid")
    @Expose
    @PrimaryKey
    private String uuid;            //created log ID
    @SerializedName("cache_code")
    @Expose
    private String geocacheCode;    //code of the geocache
    @SerializedName("logtype")
    @Expose
    private String logType;         //type of the log entry
    @SerializedName("comment")
    @Expose
    private String comment;         //text to be submitted with the log entry
    @SerializedName("comment_format")
    @Expose
    private String commentFormat;   //indicates the format of your comment. Values: auto, html, plaintext
    @SerializedName("when")
    @Expose
    private Date logDate;           //date and time of new log
    @SerializedName("password")
    @Expose
    private String password;        //password for "Found it" or "Attended" logs if needed
    @SerializedName("langpref")
    @Expose
    private String langPref;        //ISO 639-1 language code. Indicates the chosen language for success or error messages
    @SerializedName("on_duplicate")
    @Expose
    private String onDuplicate;     //action on duplicate log entry. Values: silent_success, user_error, continue
    @SerializedName("rating")
    @Expose
    private int rate;               //rate between 1 and 5 of "Found it" or "Attended" logs.
    @SerializedName("recommend")
    @Expose
    private boolean isRecommend;    //recommendation for "Found it" or "Attended" logs
    @SerializedName("needs_maintenance2")
    @Expose
    private boolean isNeedMaintenance;  //indicate if the cache needs some special attention of its owner

    public NewGeocacheLog() {
    }

    public NewGeocacheLog(String geocacheCode) {
        this.geocacheCode = checkNotNull(geocacheCode);
        this.uuid = UUID.randomUUID().toString();
        this.logDate = new DateTime().toDate();
        this.commentFormat = "plaintext";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGeocacheCode() {
        return geocacheCode;
    }

    public void setGeocacheCode(String geocacheCode) {
        this.geocacheCode = geocacheCode;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentFormat() {
        return commentFormat;
    }

    public void setCommentFormat(String commentFormat) {
        this.commentFormat = commentFormat;
    }

    public DateTime getLogDate() {
        return new DateTime(logDate);
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLangPref() {
        return langPref;
    }

    public void setLangPref(String langPref) {
        this.langPref = langPref;
    }

    public String getOnDuplicate() {
        return onDuplicate;
    }

    public void setOnDuplicate(String onDuplicate) {
        this.onDuplicate = onDuplicate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public boolean isNeedMaintenance() {
        return isNeedMaintenance;
    }

    public void setNeedMaintenance(boolean needMaintenance) {
        isNeedMaintenance = needMaintenance;
    }
}
