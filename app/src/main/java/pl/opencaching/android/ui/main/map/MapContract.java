package pl.opencaching.android.ui.main.map;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseContract;
import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmResults;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapContract{

    public interface View extends BaseContract.View {

        void showProgress();

        void hideProgress();

        void showMapInfo(int message);

        void hideMapInfo();

        void moveMapCamera(LatLng latLng, float zoom, int duration);

        void addGeocaches(RealmResults<Geocache> geocaches);

        void removeGeocaches(RealmResults<Geocache> geocaches);

        void hideGeocacheInfo();

        void clearMap();

        void downloadGeocaches();

    }

    public interface Presenter extends BaseContract.Presenter {

        void downloadGeocaches(LatLng center, int radius);

        void getLocation(String address);

        void getUserData();

        void refreshMap(boolean isAvailabilityChanged);

        //Geocache getGeocache(Marker marker);

    }


}
