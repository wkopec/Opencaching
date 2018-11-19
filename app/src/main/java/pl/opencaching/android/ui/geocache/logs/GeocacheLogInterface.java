package pl.opencaching.android.ui.geocache.logs;

import org.joda.time.DateTime;

import io.realm.RealmList;
import pl.opencaching.android.data.models.okapi.Image;
import pl.opencaching.android.data.models.okapi.User;

public interface GeocacheLogInterface extends Comparable<GeocacheLogInterface>{

    String getComment();
    User getUser();
    DateTime getDateTime();
    String getType();
    boolean isRecommended();
    RealmList<Image> getImages();
    Boolean isReadyToSync();

}
