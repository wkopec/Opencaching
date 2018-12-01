package pl.opencaching.android.ui.main.drafts;

import java.util.List;

import javax.inject.Inject;

import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.LogDraftRepository;
import pl.opencaching.android.ui.base.BasePresenter;

public class DraftsPresenter extends BasePresenter implements DraftsContract.Presenter{

    @Inject
    LogDraftRepository logDraftRepository;

    private DraftsContract.View view;

    @Inject
    DraftsPresenter(DraftsContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        List<GeocacheLogDraft> geocacheLogDraftList = logDraftRepository.loadAllLogDraws();
        view.setGeocacheDraws(geocacheLogDraftList);
    }
}
