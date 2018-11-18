package pl.opencaching.android.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewGeocacheLogResponse {

    @SerializedName("success")
    @Expose
    private boolean isSuccess;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("logUuid")
    @Expose
    private String logUuid;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getLogUuid() {
        return logUuid;
    }
}
