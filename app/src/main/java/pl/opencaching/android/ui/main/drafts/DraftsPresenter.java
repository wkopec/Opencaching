package pl.opencaching.android.ui.main.drafts;

import java.util.List;

import javax.inject.Inject;

import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.ui.base.BasePresenter;

public class DraftsPresenter extends BasePresenter implements DraftsContract.Presenter{

    @Inject
    LogDrawRepository logDrawRepository;

    private DraftsContract.View view;

    @Inject
    DraftsPresenter(DraftsContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        List<GeocacheLogDraw> geocacheLogDrawList = logDrawRepository.loadAllLogDraws();
        view.setGeocacheDraws(geocacheLogDrawList);
    }
}
