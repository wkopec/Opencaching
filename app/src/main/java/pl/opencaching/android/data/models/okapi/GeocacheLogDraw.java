package pl.opencaching.android.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogInterface;

import static dagger.internal.Preconditions.checkNotNull;
import static pl.opencaching.android.utils.StringUtils.getApiFormatedDate;

public class GeocacheLogDraw extends RealmObject implements GeocacheLogInterface {

    @SerializedName("log_uuid")
    @Expose
    @PrimaryKey
    private String uuid;
    @SerializedName("cache_code")
    @Expose
    private String geocacheCode;    //code of the geocache
    @SerializedName("logtype")
    @Expose
    private String type;            //type of the log entry
    @SerializedName("comment")
    @Expose
    private String comment;         //text to be submitted with the log entry
    @SerializedName("comment_format")
    @Expose
    private String commentFormat;   //indicates the format of your comment. Values: auto, html, plaintext
    @SerializedName("when")
    @Expose
    private Date date;              //date and time of new log
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
    private boolean isRecommended;    //recommendation for "Found it" or "Attended" logs
    @SerializedName("needs_maintenance2")
    @Expose
    private Boolean isNeedMaintenance;  //indicate if the cache needs some special attention of its owner

    private Boolean isReadyToSync;

    private User user;

    private RealmList<Image> images;

    public GeocacheLogDraw() {
    }

    public GeocacheLogDraw(String geocacheCode) {
        this.geocacheCode = checkNotNull(geocacheCode);
        this.uuid = UUID.randomUUID().toString();
        this.date = new DateTime().toDate();
        this.commentFormat = "plaintext";
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("cache_code", geocacheCode);
        map.put("logtype", type);
        map.put("comment", comment);
        map.put("when", getApiFormatedDate(date));
        map.put("recommend", String.valueOf(isRecommended));
        if(password != null) {
            map.put("password", password);
        }
        if(rate > 0 && rate < 6) {
            map.put("rating", String.valueOf(rate));
        }
        if(isNeedMaintenance != null) {
            map.put("needs_maintenance2", String.valueOf(isNeedMaintenance));
        }
        map.put("comment_format", "plaintext");
        map.put("langpref", "pl|en");
        map.put("on_duplicate", "user_error");
        return map;
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

    public String getCommentFormat() {
        return commentFormat;
    }

    public void setCommentFormat(String commentFormat) {
        this.commentFormat = commentFormat;
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

    @Override
    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public Boolean getNeedMaintenance() {
        return isNeedMaintenance;
    }

    public void setNeedMaintenance(Boolean needMaintenance) {
        isNeedMaintenance = needMaintenance;
    }

    @Override
    public Boolean isReadyToSync() {
        return isReadyToSync;
    }

    public void setReadyToSync(boolean readyToSync) {
        isReadyToSync = readyToSync;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }

}
