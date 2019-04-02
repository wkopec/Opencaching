package pl.opencaching.android.ui.main.drafts;

import java.util.Set;

import io.realm.OrderedRealmCollection;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.ui.base.BaseContract;

public class DraftsContract {

    public interface View extends BaseContract.View {

        void setGeocacheDraws(OrderedRealmCollection<GeocacheLogDraft> geocacheLogDrafts);

        void setMultipleChoiceMode(boolean isMultipleChoiceMode);

        void updateData();

        void showProgress(boolean isShown);
    }

    public interface Presenter extends BaseContract.Presenter {

        void onStart();

        void postDraft(GeocacheLogDraft logDraft);

        void postDrafts(Set<GeocacheLogDraft> drafts);

        void deleteDrafts(Set<GeocacheLogDraft> drafts);

    }
}
