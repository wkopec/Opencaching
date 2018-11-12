package pl.opencaching.android.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BaseDialog;
import pl.opencaching.android.utils.events.ChangeLogTypeEvent;
import pl.opencaching.android.utils.listeners.OnNewLogTypeClickListener;

import static pl.opencaching.android.ui.base.BaseFragmentActivity.NEW_LOG_FRAGMENT;
import static pl.opencaching.android.ui.base.BaseFragmentActivity.launchFragmentActivity;
import static pl.opencaching.android.ui.geocache.GeocacheActivity.GEOCACHE_CODE;
import static pl.opencaching.android.ui.geocache.new_log.NewLogFragment.NEW_LOG_TYPE;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_EVENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_ATTENDED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_NOT_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_WILL_ATTEND;

public class NewLogTypeDialog extends BaseDialog implements OnNewLogTypeClickListener {

    private static final String DIALOG_MODE = "dialog_mode:";

    public static final int NEW_LOG = 1;
    public static final int CHANGE_LOG_TYPE = 2;

    @Inject
    Context context;
    @Inject
    GeocacheRepository geocacheRepository;

    @BindView(R.id.logTypeRecyclerView)
    RecyclerView logTypeRecyclerView;

    private String geocacheCode;
    private int dialogMode;

    public static NewLogTypeDialog newInstance(String geocacheCode, int dialogMode) {
        Bundle args = new Bundle();
        args.putString(GEOCACHE_CODE, geocacheCode);
        args.putInt(DIALOG_MODE, dialogMode);
        NewLogTypeDialog dialog = new NewLogTypeDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            geocacheCode = getArguments().getString(GEOCACHE_CODE);
            dialogMode = getArguments().getInt(DIALOG_MODE);
        }
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_new_log_type;
    }

    @Override
    protected void setupView() {
        setIcon(R.drawable.ic_new_log);
        logTypeRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        NewLogTypeAdapter adapter = new NewLogTypeAdapter(getAvailableLogTypes(geocacheCode), context, this);
        logTypeRecyclerView.setAdapter(adapter);
    }

    private void startNewLogActivity(String logType) {
        Bundle bundle = new Bundle();
        bundle.putString(GEOCACHE_CODE, geocacheCode);
        bundle.putString(NEW_LOG_TYPE, logType);
        launchFragmentActivity(getActivity(), NEW_LOG_FRAGMENT, bundle);
    }

    private ArrayList<String> getAvailableLogTypes(String geocacheCode) {
        ArrayList<String> logTypes = new ArrayList<>();
        Geocache geocache = geocacheRepository.loadGeocacheByCode(geocacheCode);
        if(geocache.getType().equals(GEOCACHE_TYPE_EVENT)) {
            logTypes.add(LOG_TYPE_WILL_ATTEND);
            logTypes.add(LOG_TYPE_ATTENDED);
        } else {
            if(!geocache.isFound()) {
                logTypes.add(LOG_TYPE_FOUND);
                logTypes.add(LOG_TYPE_NOT_FOUND);
            }
        }
        logTypes.add(LOG_TYPE_COMMENT);
        return logTypes;
    }

    @Override
    public void OnNewLogTypeClick(String newLogType) {
        switch(dialogMode) {
            case NEW_LOG:
                startNewLogActivity(newLogType);
                break;
            case CHANGE_LOG_TYPE:
                EventBus.getDefault().postSticky(new ChangeLogTypeEvent(newLogType));
        }
        dismiss();
    }

}