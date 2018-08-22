package com.example.opencaching.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Image {

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

}
