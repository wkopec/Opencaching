package pl.opencaching.android.ui.main.drafts;

import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import pl.opencaching.android.R;
import pl.opencaching.android.app.App;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.LogDraftRepository;
import pl.opencaching.android.ui.base.BasePresenter;
import pl.opencaching.android.utils.events.LogSyncEvent;

import static pl.opencaching.android.utils.SyncUtils.startMergeService;
import static pl.opencaching.android.utils.events.LogSyncEvent.SyncStatus.FAILED;
import static pl.opencaching.android.utils.events.LogSyncEvent.SyncStatus.WITH_ERRORS;

public class DraftsPresenter extends BasePresenter implements DraftsContract.Presenter {

    @Inject
    App context;
    @Inject
    Realm realm;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    LogDraftRepository logDraftRepository;

    private int draftsForSyncCounter = 0;
    private int syncedDrafts = 0;
    private Set<LogSyncEvent.SyncStatus> syncStatuses = new HashSet<>();

    private DraftsContract.View view;

    @Inject
    DraftsPresenter(DraftsContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        OrderedRealmCollection<GeocacheLogDraft> geocacheLogDraftList = logDraftRepository.loadAllLogDraws();
        view.setGeocacheDraws(geocacheLogDraftList);
    }

    @Override
    public void postDraft(GeocacheLogDraft logDraft) {
        Set<GeocacheLogDraft> drafts = new HashSet<>();
        drafts.add(logDraft);
        postDrafts(drafts);
    }

    @Override
    public void postDrafts(Set<GeocacheLogDraft> drafts) {
        draftsForSyncCounter = drafts.size();
        syncedDrafts = 0;
        syncStatuses.clear();
        realm.beginTransaction();
        for (GeocacheLogDraft draft : drafts) {
            draft.setReadyToSync(true);
        }
        realm.commitTransaction();
        view.setMultipleChoiceMode(false);
        startMergeService(context, sharedPreferences);
        view.showProgress(true);
    }

    @Override
    public void deleteDrafts(Set<GeocacheLogDraft> drafts) {
        realm.beginTransaction();
        for(GeocacheLogDraft draft : drafts) {
            draft.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogSyncEvent(LogSyncEvent event) {
        syncedDrafts++;
        syncStatuses.add(event.getStatus());
        if (syncedDrafts == draftsForSyncCounter) {
            view.updateData();
            view.showProgress(false);
            if (syncStatuses.contains(FAILED)) {
                view.showMessage(context.getString(R.string.something_went_wrong));
            } else if (syncStatuses.contains(WITH_ERRORS)) {
                view.showMessage(context.getString(R.string.log_sync_with_errors_message));
            } else {
                if(draftsForSyncCounter == 1) {
                    view.showMessage(context.getString(R.string.log_sync_success_message));
                } else {
                    view.showMessage(context.getString(R.string.logs_sync_success_message));
                }
            }
        }
    }
}