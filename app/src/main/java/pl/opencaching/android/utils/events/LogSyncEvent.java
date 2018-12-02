package pl.opencaching.android.utils.events;

public class LogSyncEvent {

    public enum SyncStatus {
        COMPLETED, WITH_ERRORS, FAILED
    }

    SyncStatus status;

    public LogSyncEvent(SyncStatus status) {
        this.status = status;
    }

    public SyncStatus getStatus() {
        return status;
    }
}
