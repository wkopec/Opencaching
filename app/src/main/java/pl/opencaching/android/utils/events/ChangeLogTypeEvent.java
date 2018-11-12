package pl.opencaching.android.utils.events;

public class ChangeLogTypeEvent {

    private String logType;

    public ChangeLogTypeEvent(String logType) {
        this.logType = logType;
    }

    public String getLogType() {
        return logType;
    }
}
