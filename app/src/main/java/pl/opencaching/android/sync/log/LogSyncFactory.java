package pl.opencaching.android.sync.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pl.opencaching.android.sync.BaseSyncFactory;
import pl.opencaching.android.sync.SyncStatusEvent;
import pl.opencaching.android.sync.log.tasks.UploadGeocacheLogsTask;
import timber.log.Timber;

public class LogSyncFactory extends BaseSyncFactory {

    private static final String LOG_TAG = LogSyncFactory.class.getSimpleName();


    private List<Runnable> fetchTasks;
    private ExecutorService executorService;

    public LogSyncFactory() {
        initTaskList();
        initThreadPool();
    }

    private void initTaskList() {
        fetchTasks = new ArrayList<>();
        fetchTasks.add(new UploadGeocacheLogsTask());
    }

    private void initThreadPool() {
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        final int KEEP_ALIVE_TIME = 30;

        executorService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }


    @Override
    public void execute() {
        sendEvent(SyncStatusEvent.Status.PROGRESS, "Executing log sync tasks.");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < fetchTasks.size(); i++) {
            Runnable task = fetchTasks.get(i);
            executorService.submit(task);
        }

        long millis = System.currentTimeMillis() - startTime;

        Timber.d(LOG_TAG, "<--- MERGE SYNC COMPLETE --->");
        Timber.d(LOG_TAG, "<-- EXECUTION TIME: " + TimeUnit.MILLISECONDS.toSeconds(millis) + " sec -->");
        sendEvent(SyncStatusEvent.Status.COMPLETE, "Log Sync Complete");

    }
}
