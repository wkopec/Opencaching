package pl.opencaching.android.data.models.okapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Image extends RealmObject implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.caption);
        dest.writeString(this.uniqueCaption);
    }

    private Image (Parcel in) {
        this.imageUrl = in.readString();
        this.caption = in.readString();
        this.uniqueCaption = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Geocache[] newArray(int size) {
            return new Geocache[size];
        }
    };

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
