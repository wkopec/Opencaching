package pl.opencaching.android.ui.main.drafts;

import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;

public interface DraftsAdapterEventListener {
    void onDraftSelectionChange();
    void onLongItemClick();
    void onPostDraft(GeocacheLogDraft logDraft);
}
