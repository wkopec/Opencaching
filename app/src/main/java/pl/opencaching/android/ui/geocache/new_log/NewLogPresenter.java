package pl.opencaching.android.ui.geocache.new_log;

import javax.inject.Inject;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;

public class NewLogPresenter extends BasePresenter implements NewLogContract.Presenter{

    @Inject
    GeocacheRepository geocacheRepository;

    private NewLogContract.View view;
    private Geocache geocache;

    @Inject
    NewLogPresenter(NewLogContract.View view) {
        this.view = view;
    }

    @Override
    public void start(String geocacheCode) {
        geocache = geocacheRepository.loadGeocacheByCode(geocacheCode);

        view.setupGeocache(geocache);
    }

}
