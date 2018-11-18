package pl.opencaching.android.sync;

public class SyncStatusEvent {

    public enum Status {
        WAITING, PROGRESS, COMPLETE, ERROR
    }

    public Status mStatus;

    public String msg;


    @Override
    public String toString() {
        return "SyncStatusEvent{" +
                "mStatus=" + mStatus +
                ", msg='" + msg + '\'' +
                '}';
    }
}