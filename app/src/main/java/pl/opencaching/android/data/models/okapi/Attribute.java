package pl.opencaching.android.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Attribute extends RealmObject {

    @SerializedName("acode")
    @Expose
    @PrimaryKey
    private String code;                //Unique identifier of the attribute.
    @SerializedName("name")
    @Expose
    private String name;                //name of the attribute
    @SerializedName("description")
    @Expose
    private String description;         //HTML string, description of the attribute

    private int icon;

    public Attribute() {
    }

    public Attribute(String code, String name, String description, int icon) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }
}
