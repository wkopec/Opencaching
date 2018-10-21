package pl.opencaching.android.ui.geocache.info;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import pl.opencaching.android.R;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Attribute;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.repository.AttributeRepository;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

import static pl.opencaching.android.utils.GeocacheUtils.getAttributeIcon;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoPresenter extends BasePresenter implements GeocacheInfoContract.Presenter {

    @Inject
    OkapiService okapiService;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    AttributeRepository attributeRepository;

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
        getAllAttributes();
    }

    @Override
    public void onHintClick() {
        if(!geocache.getHint().isEmpty()) {
            view.showHint();
        } else {
            view.showMessage(context.getResources().getString(R.string.geocache_hint_unavailable_message));
        }
    }

    public void getAllAttributes() {
        okapiService.getAllAttributes("acode|name|description", "pl", true).enqueue(new Callback<Map<String, Attribute>>() {
            @Override
            public void onResponse(Call<Map<String, Attribute>> call, Response<Map<String, Attribute>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Map<String, Attribute> attributeMap = response.body();
                    ArrayList<Attribute> attributeList = new ArrayList<>();
                    Iterator iterator = attributeMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry pair = (Map.Entry) iterator.next();
                        Attribute attribute = (Attribute) pair.getValue();
                        attribute.setIcon(getAttributeIcon(attribute.getCode()));
                        attributeList.add(attribute);
                        iterator.remove();
                    }
                    attributeRepository.addOrUpdate(attributeList);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Attribute>> call, Throwable t) {

            }
        });
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
