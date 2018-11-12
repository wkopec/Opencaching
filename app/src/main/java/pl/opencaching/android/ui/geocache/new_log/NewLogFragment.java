package pl.opencaching.android.ui.geocache.new_log;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hsalf.smilerating.BaseRating;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.ui.dialogs.NewLogTypeDialog;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.events.ChangeLogTypeEvent;
import pl.opencaching.android.utils.views.CustomSmileRating;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.GEOCACHE_CODE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.GeocacheUtils.getLogType;

public class NewLogFragment extends BaseFragment implements NewLogContract.View {

    public static final String NEW_LOG_TYPE = "new_log_type:";

    private Unbinder unbinder;

    @Inject
    Context context;
    @Inject
    NewLogContract.Presenter presenter;

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.logType)
    TextView logTypeTv;
    @BindView(R.id.logTypeIcon)
    ImageView logTypeIcon;
    @BindView(R.id.logDate)
    TextView logDate;
    @BindView(R.id.rateLabel)
    ConstraintLayout rateLabel;
    @BindView(R.id.smileRating)
    CustomSmileRating smileRating;
    @BindView(R.id.comment)
    EditText comment;

    private String logType;
    private String geocacheCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        unbinder = ButterKnife.bind(this, view);
        logType = getArguments().getString(NEW_LOG_TYPE);
        geocacheCode = getArguments().getString(GEOCACHE_CODE);
        setupView(logType);
        setPresenter(presenter);
        presenter.start(geocacheCode);
        return view;
    }

    @Override
    public void setupGeocache(Geocache geocache) {
        geocacheName.setText(geocache.getName());
    }

    private void setupView(String logType) {
        setupActionBar();
        setupSmileRating();
        setupLogType(logType);
        logDate.setText("21/11/2018 21:21");
    }

    private void setupActionBar() {
        BaseActivity activity = (BaseActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.new_log));
        }
    }

    private void setupSmileRating() {
        smileRating.setNameForSmile(BaseRating.GREAT, R.string.rate_perfect);
        smileRating.setNameForSmile(BaseRating.GOOD, R.string.rate_good);
        smileRating.setNameForSmile(BaseRating.OKAY, R.string.rate_normal);
        smileRating.setNameForSmile(BaseRating.BAD, R.string.rate_below_average_two_lines);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, R.string.rate_bad);
    }

    private void setupLogType(String newLogType) {
        logTypeTv.setText(getLogType(newLogType));
        logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(newLogType));
        logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(newLogType)));
        if(newLogType.equals(LOG_TYPE_FOUND)) {
            rateLabel.setVisibility(View.VISIBLE);
        } else {
            rateLabel.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.logType, R.id.logTypeIcon})
    public void onChangeLogTypeClick() {
        Activity activity = getActivity();
        if(activity != null) {
            NewLogTypeDialog messageDialog = NewLogTypeDialog.newInstance(geocacheCode, NewLogTypeDialog.CHANGE_LOG_TYPE);
            messageDialog.show(getActivity().getSupportFragmentManager(), NewLogTypeDialog.class.getName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onChangeLogType(ChangeLogTypeEvent event) {
        setupLogType(event.getLogType());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
