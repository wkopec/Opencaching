package pl.opencaching.android.ui.geocache.new_log;

import javax.inject.Inject;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BasePresenter;

public class NewLogPresenter extends BasePresenter implements NewLogContract.Presenter{

    private NewLogContract.View view;

    @Inject
    public NewLogPresenter(NewLogContract.View view) {
        this.view = view;
    }

    @Override
    public void start(Geocache geocache) {

    }
}
