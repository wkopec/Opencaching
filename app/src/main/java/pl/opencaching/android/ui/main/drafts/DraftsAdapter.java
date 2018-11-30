package pl.opencaching.android.ui.main.drafts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.utils.GeocacheUtils;

import static pl.opencaching.android.utils.Constants.LOG_TYPE_ATTENDED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.StringUtils.getDateString;
import static pl.opencaching.android.utils.StringUtils.getTimeString;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.ViewHolder> {

    private GeocacheRepository geocacheRepository;
    private Context context;
    private Completable completable;

    private List<GeocacheLogDraw> geocacheLogDrawList;
    private List<ViewHolder> views = new ArrayList<>();
    private Set<GeocacheLogDraw> selectedGeocacheDraws = new HashSet<>();


    public DraftsAdapter(Context context, GeocacheRepository geocacheRepository) {
        this.geocacheRepository = geocacheRepository;
        this.context = context;
    }

    public void setGeocacheLogDrawList(List<GeocacheLogDraw> geocacheLogDrawList) {
        this.geocacheLogDrawList = geocacheLogDrawList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_geocache_draw, parent, false);
        return new DraftsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeocacheLogDraw geocacheLogDraw = geocacheLogDrawList.get(position);
        Geocache geocache = geocacheRepository.loadGeocacheByCode(geocacheLogDraw.getGeocacheCode());

        holder.geocacheName.setText(geocache.getName());

        holder.logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(geocacheLogDraw.getType()));
        holder.logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(geocacheLogDraw.getType())));

        DateTime logDate = geocacheLogDraw.getDateTime();
        holder.logDate.setText(getDateString(logDate, context));
        holder.logTime.setText(getTimeString(logDate.getHourOfDay(), logDate.getMinuteOfHour(), context));

        holder.recommendationIcon.setVisibility(geocacheLogDraw.isRecommended() ? View.VISIBLE : View.GONE);

        if (geocacheLogDraw.getComment() != null && !geocacheLogDraw.getComment().isEmpty()) {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } else if (geocacheLogDraw.getType().equals(LOG_TYPE_COMMENT)) {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray_hint));
        }

        if (geocacheLogDraw.getType().equals(LOG_TYPE_FOUND) || geocacheLogDraw.getType().equals(LOG_TYPE_ATTENDED)) {
            if (geocacheLogDraw.getRate() > 0) {
                holder.rateIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            } else {
                holder.rateIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray_hint));
            }
            holder.rateIcon.setVisibility(View.VISIBLE);
        } else {
            holder.rateIcon.setVisibility(View.GONE);
        }

        if (geocache.isPasswordRequired() && (geocacheLogDraw.getType().equals(LOG_TYPE_FOUND) || geocacheLogDraw.getType().equals(LOG_TYPE_ATTENDED))) {
            if (geocacheLogDraw.getPassword() == null) {
                holder.passwordIcon.setColorFilter(ContextCompat.getColor(context, R.color.red));
            } else {
                holder.passwordIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
            holder.passwordIcon.setVisibility(View.VISIBLE);
        } else {
            holder.passwordIcon.setVisibility(View.GONE);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedGeocacheDraws.add(geocacheLogDrawList.get(position));
                } else {
                    selectedGeocacheDraws.remove(geocacheLogDrawList.get(position));
                }
                if(completable != null) {
                    completable.subscribe();
                }
            }
        });

        //TODO: create photo indicator icon

        views.add(holder);
    }

    @Override
    public int getItemCount() {
        return geocacheLogDrawList.size();
    }

    public void setSelectedGeocacheDrawsChangeCompletable(Completable completable) {
        this.completable = completable;
    }

    public Set<GeocacheLogDraw> getSelectedGeocacheDraws() {
        return selectedGeocacheDraws;
    }

    void setMultipleChoiceMode(boolean isMultipleChoiceMode) {
        for (ViewHolder holder : views) {
            if (isMultipleChoiceMode) {
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.moreIcon.setVisibility(View.GONE);

            } else {
                holder.checkbox.setVisibility(View.GONE);
                holder.moreIcon.setVisibility(View.VISIBLE);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.moreIcon)
        ImageView moreIcon;

        @BindView(R.id.geocacheName)
        TextView geocacheName;
        @BindView(R.id.logTypeIcon)
        ImageView logTypeIcon;
        @BindView(R.id.logDate)
        TextView logDate;
        @BindView(R.id.logTime)
        TextView logTime;
        @BindView(R.id.recommendationIcon)
        ImageView recommendationIcon;
        @BindView(R.id.commentIcon)
        ImageView commentIcon;
        @BindView(R.id.rateIcon)
        ImageView rateIcon;
        @BindView(R.id.passwordIcon)
        ImageView passwordIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}