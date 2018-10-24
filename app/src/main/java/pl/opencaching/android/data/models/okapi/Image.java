package pl.opencaching.android.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Image extends RealmObject {

    @SerializedName("uuid")
    @Expose
    private String uuid;            //UUID of the image
    @SerializedName("url")
    @Expose
    private String imageUrl;        //URL of the image
    @SerializedName("thumb_url")
    @Expose
    private String thumbImageUrl;   //URL of a small (thumb) version of the image
    @SerializedName("caption")
    @Expose
    private String caption;         //caption of the image
    @SerializedName("unique_caption")
    @Expose
    private String uniqueCaption;   //unique caption of the image
    @SerializedName("is_spoiler")
    @Expose
    private boolean isSpoiler;        //if true then the image is a spoiler image

    public Image() {
    }

    public String getUuid() {
        return uuid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public String getUniqueCaption() {
        return uniqueCaption;
    }

    public boolean isSpoiler() {
        return isSpoiler;
    }
}
