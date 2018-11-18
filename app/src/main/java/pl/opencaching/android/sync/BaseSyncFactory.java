package pl.opencaching.android.sync;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseSyncFactory {

    public abstract void execute();

    protected void sendEvent(SyncStatusEvent.Status status, String message) {
        SyncStatusEvent event = new SyncStatusEvent();
        event.mStatus = status;
        event.msg = message;
        EventBus.getDefault().postSticky(event);
    }
}
