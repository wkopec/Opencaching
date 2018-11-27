package pl.opencaching.android.ui.main.drafts;

import javax.inject.Inject;

import pl.opencaching.android.ui.base.BasePresenter;

public class DraftsPresenter extends BasePresenter implements DraftsContract.Presenter{

    DraftsContract.View view;

    @Inject
    DraftsPresenter(DraftsContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {

    }
}
