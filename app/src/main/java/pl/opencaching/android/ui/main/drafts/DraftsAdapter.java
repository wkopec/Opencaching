package pl.opencaching.android.ui.main.drafts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.utils.GeocacheUtils;

import static pl.opencaching.android.ui.base.BaseFragmentActivity.NEW_LOG_FRAGMENT;
import static pl.opencaching.android.ui.base.BaseFragmentActivity.launchFragmentActivity;
import static pl.opencaching.android.ui.geocache.GeocacheActivity.launchGeocacheActivity;
import static pl.opencaching.android.ui.geocache.new_log.NewLogFragment.EXISTING_DRAFT_UUID;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_ATTENDED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.StringUtils.getDateString;
import static pl.opencaching.android.utils.StringUtils.getTimeString;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.ViewHolder> {

    private GeocacheRepository geocacheRepository;
    private Activity context;
    private Realm realm;
    private DraftsAdapretEventListener listener;
    private List<GeocacheLogDraft> geocacheLogDraftList;
    private List<ViewHolder> views = new ArrayList<>();
    private Set<GeocacheLogDraft> selectedGeocacheDraws = new HashSet<>();


    DraftsAdapter(Activity context, GeocacheRepository geocacheRepository, Realm realm, DraftsAdapretEventListener listener) {
        this.geocacheRepository = geocacheRepository;
        this.context = context;
        this.realm = realm;
        this.listener = listener;
    }

    void setGeocacheLogDraftList(List<GeocacheLogDraft> geocacheLogDraftList) {
        this.geocacheLogDraftList = geocacheLogDraftList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_geocache_draw, parent, false);
        return new DraftsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeocacheLogDraft geocacheLogDraft = geocacheLogDraftList.get(position);
        Geocache geocache = geocacheRepository.loadGeocacheByCode(geocacheLogDraft.getGeocacheCode());

        holder.geocacheName.setText(geocache.getName());

        holder.logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(geocacheLogDraft.getType()));
        holder.logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(geocacheLogDraft.getType())));

        DateTime logDate = geocacheLogDraft.getDateTime();
        holder.logDate.setText(getDateString(logDate, context));
        holder.logTime.setText(getTimeString(logDate.getHourOfDay(), logDate.getMinuteOfHour(), context));

        holder.recommendationIcon.setVisibility(geocacheLogDraft.isRecommended() ? View.VISIBLE : View.GONE);

        if (geocacheLogDraft.getComment() != null && !geocacheLogDraft.getComment().isEmpty()) {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } else if (geocacheLogDraft.getType().equals(LOG_TYPE_COMMENT)) {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.commentIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray_hint));
        }

        if (geocacheLogDraft.getType().equals(LOG_TYPE_FOUND) || geocacheLogDraft.getType().equals(LOG_TYPE_ATTENDED)) {
            if (geocacheLogDraft.getRate() > 0) {
                holder.rateIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            } else {
                holder.rateIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray_hint));
            }
            holder.rateIcon.setVisibility(View.VISIBLE);
        } else {
            holder.rateIcon.setVisibility(View.GONE);
        }

        if (geocache.isPasswordRequired() && (geocacheLogDraft.getType().equals(LOG_TYPE_FOUND) || geocacheLogDraft.getType().equals(LOG_TYPE_ATTENDED))) {
            if (geocacheLogDraft.getPassword() == null) {
                holder.passwordIcon.setColorFilter(ContextCompat.getColor(context, R.color.red));
            } else {
                holder.passwordIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
            holder.passwordIcon.setVisibility(View.VISIBLE);
        } else {
            holder.passwordIcon.setVisibility(View.GONE);
        }

        if(geocacheLogDraft.getUploadErrorMessage() != null) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_red));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongItemClick();
            holder.checkbox.setChecked(true);
            return true;
        });

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedGeocacheDraws.add(geocacheLogDraftList.get(position));
            } else {
                selectedGeocacheDraws.remove(geocacheLogDraftList.get(position));
            }
            listener.onDraftSelectionChange();
        });

        holder.moreIcon.setOnClickListener(v -> openDraftItemMenu(geocacheLogDraftList.get(position), holder.moreIcon));

        holder.itemView.setOnClickListener(v -> startEditDraftActivity(geocacheLogDraft));

        //TODO: create photo indicator icon

        views.add(holder);
    }

    @Override
    public int getItemCount() {
        return geocacheLogDraftList.size();
    }

    void setAllItemsChecked(boolean isChecked) {
        for (ViewHolder holder : views) {
            holder.checkbox.setChecked(isChecked);
        }
        if (isChecked) {
            selectedGeocacheDraws.addAll(geocacheLogDraftList);
        } else {
            selectedGeocacheDraws.clear();
        }
    }

    private void openDraftItemMenu(GeocacheLogDraft logDraw, View view) {
        PopupMenu draftItemMenu = new PopupMenu(context, view);
        draftItemMenu.inflate(R.menu.draft_item_menu);

        draftItemMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_show_geocache:
                    launchGeocacheActivity(context, logDraw.getGeocacheCode());
                    break;
                case R.id.action_post:
                    listener.onPostDraft(logDraw);
                    break;
                case R.id.action_delete:
                    realm.beginTransaction();
                    logDraw.deleteFromRealm();
                    realm.commitTransaction();
                    notifyDataSetChanged();
                    break;
            }
            return true;
        });

        draftItemMenu.show();
    }


    private void startEditDraftActivity(GeocacheLogDraft logDraw) {
        Bundle bundle = new Bundle();
        bundle.putString(EXISTING_DRAFT_UUID, logDraw.getUuid());
        launchFragmentActivity(context, NEW_LOG_FRAGMENT, bundle);
    }

    Set<GeocacheLogDraft> getSelectedGeocacheDraws() {
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