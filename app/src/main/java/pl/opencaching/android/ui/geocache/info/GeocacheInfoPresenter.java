package pl.opencaching.android.ui.geocache.info;

import android.content.Context;

import pl.opencaching.android.R;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;

import javax.inject.Inject;


/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoPresenter extends BasePresenter implements GeocacheInfoContract.Presenter {

    @Inject
    OkapiService okapiService;
    @Inject
    GeocacheRepository geocacheRepository;

    private GeocacheInfoContract.View view;
    private Geocache geocache;
    private Context context;

    @Inject
    public GeocacheInfoPresenter(GeocacheInfoContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getGeocacheInfo(String code) {
        geocache = geocacheRepository.loadGeocacheByCode(code);
        view.setGeocacheData(geocache);
    }

    @Override
    public void onHintClick() {
        if(!geocache.getHint().isEmpty()) {
            view.showHint();
        } else {
            view.showMessage(R.drawable.ic_archive, R.string.geocache_hint_unavailable_message);
        }
    }

//    public void getGeocacheInfo(String code) {
//
//        Call<Geocache> loginCall = okapiService.getGeocacheInfo(code, GEOCACHE_INFO_FIELDS);
//        loginCall.enqueue(new Callback<Geocache>() {
//            @Override
//            public void onResponse(@NonNull Call<Geocache> call, @NonNull Response<Geocache> response) {
//                if (response.body() != null){
//                    setGeocacheInfo(response.body());
//                } else {
//                    if (response.errorBody() != null){
//                        view.showError(new Error(response.message()));
//                    }
//                }
//                view.hideProgress();
//            }
//            @Override
//            public void onFailure(@NonNull Call<Geocache> call, @NonNull Throwable t) {
//                view.hideProgress();
//                view.showError(ApiUtils.getErrorSingle(t));
//            }
//        });
//    }
//

}
