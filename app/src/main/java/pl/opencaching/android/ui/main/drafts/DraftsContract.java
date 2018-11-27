package pl.opencaching.android.ui.main.drafts;

import pl.opencaching.android.ui.base.BaseContract;

public class DraftsContract {

    public interface View extends BaseContract.View {

    }

    public interface Presenter extends BaseContract.Presenter {

        void onStart();

    }
}
