package pl.opencaching.android.ui.geocache.new_log;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.NewGeocacheLog;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.ui.dialogs.NewLogTypeDialog;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.StringUtils;
import pl.opencaching.android.utils.events.ChangeLogTypeEvent;
import pl.opencaching.android.utils.views.CustomSmileRating;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.GEOCACHE_CODE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_MAINTENANCE_PERFORMED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_NEEDS_MAINTENANCE;
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
    @BindView(R.id.logDateTextView)
    TextView logDateTextView;
    @BindView(R.id.logTimeTextView)
    TextView logTimeTextView;
    @BindView(R.id.rateLabel)
    ConstraintLayout rateLabel;
    @BindView(R.id.smileRating)
    CustomSmileRating smileRating;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.passwordLabel)
    ConstraintLayout passwordLabel;
    @BindView(R.id.password)
    EditText password;

    private Geocache geocache;
    private NewGeocacheLog newGeocacheLog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        unbinder = ButterKnife.bind(this, view);
        newGeocacheLog = new NewGeocacheLog(getArguments().getString(GEOCACHE_CODE));
        newGeocacheLog.setLogType(getArguments().getString(NEW_LOG_TYPE));


        setupView();
        setPresenter(presenter);
        presenter.start(newGeocacheLog.getGeocacheCode());
        return view;
    }

    @Override
    public void setupGeocache(Geocache geocache) {
        this.geocache = geocache;
        geocacheName.setText(geocache.getName());
        setupPasswordLabel(geocache.isPasswordRequired());
    }

    @Override
    public void onGeocacheSubmited() {
        Toast.makeText(requireActivity(), R.string.geocache_submited, Toast.LENGTH_SHORT).show();
        requireActivity().finish();
    }

    private void setupView() {
        setupActionBar();
        setupSmileRating();
        setupLogType(newGeocacheLog.getLogType());
        setupLogDate(newGeocacheLog.getLogDate());
    }

    private void setupActionBar() {
        BaseActivity activity = (BaseActivity) requireActivity();
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

    private void setupLogDate(DateTime logDate) {
        logDateTextView.setText(StringUtils.getDateString(logDate, context));
        logTimeTextView.setText(String.format(context.getString(R.string.separated_time), logDate.getHourOfDay(), logDate.getMinuteOfHour()));
    }

    private void setupLogType(String newLogType) {
        logTypeTv.setText(getLogType(newLogType));
        logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(newLogType));
        logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(newLogType)));
        if (newLogType.equals(LOG_TYPE_FOUND)) {
            rateLabel.setVisibility(View.VISIBLE);
        } else {
            rateLabel.setVisibility(View.GONE);
        }
    }

    private void setupPasswordLabel(boolean isPasswordRequires) {
        passwordLabel.setVisibility(isPasswordRequires && newGeocacheLog.getLogType().equals(LOG_TYPE_FOUND) ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.submitButton)
    public void onNewLogClick() {
        newGeocacheLog.setComment(comment.getText().toString());
        if (geocache.isPasswordRequired()) {
            newGeocacheLog.setPassword(password.getText().toString());
        }

        switch (newGeocacheLog.getLogType()) {
            case LOG_TYPE_NEEDS_MAINTENANCE:
                newGeocacheLog.setLogType(LOG_TYPE_COMMENT);
                newGeocacheLog.setNeedMaintenance(true);
                break;
            case LOG_TYPE_MAINTENANCE_PERFORMED:
                newGeocacheLog.setLogType(LOG_TYPE_COMMENT);
                newGeocacheLog.setNeedMaintenance(false);
                break;
            default:
                newGeocacheLog.setNeedMaintenance(null);
                break;
        }

        presenter.submitNewLog(newGeocacheLog);
    }

    @OnClick({R.id.logType, R.id.logTypeIcon})
    public void onChangeLogTypeClick() {
        NewLogTypeDialog messageDialog = NewLogTypeDialog.newInstance(newGeocacheLog.getGeocacheCode(), NewLogTypeDialog.CHANGE_LOG_TYPE);
        messageDialog.show(requireActivity().getSupportFragmentManager(), NewLogTypeDialog.class.getName());
    }

    @OnClick(R.id.usePattern)
    public void onUsePatternClick() {

    }

    @OnClick({R.id.logDateTextView, R.id.logTimeTextView})
    public void onChangeDateClick() {
        DateTime date = newGeocacheLog.getLogDate();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    newGeocacheLog.setLogDate(newGeocacheLog.getLogDate()
                            .withYear(year)
                            .withMonthOfYear(monthOfYear)
                            .withDayOfMonth(dayOfMonth).toDate());
                    setupLogDate(newGeocacheLog.getLogDate());

                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),
                            (view1, hourOfDay, minute) -> {
                                newGeocacheLog.setLogDate(newGeocacheLog.getLogDate()
                                        .withHourOfDay(hourOfDay)
                                        .withMinuteOfHour(minute).toDate());
                                setupLogDate(newGeocacheLog.getLogDate());
                            }, date.getHourOfDay(), date.getMinuteOfHour(), true);
                    timePickerDialog.show();

                }, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onChangeLogType(ChangeLogTypeEvent event) {
        newGeocacheLog.setLogType(event.getLogType());
        setupLogType(newGeocacheLog.getLogType());

        setupPasswordLabel(geocache.isPasswordRequired());
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
