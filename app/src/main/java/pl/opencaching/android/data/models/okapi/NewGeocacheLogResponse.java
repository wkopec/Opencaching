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
    @SerializedName("log_uuid")
    @Expose
    private String log_uuid;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getLog_uuid() {
        return log_uuid;
    }
}
