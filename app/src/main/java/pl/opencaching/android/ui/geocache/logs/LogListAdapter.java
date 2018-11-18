package pl.opencaching.android.ui.geocache.logs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import pl.opencaching.android.R;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.ui.gallery.PhotosAdapter;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.StringUtils;

import static pl.opencaching.android.utils.StringUtils.getFormatedHtmlString;

/**
 * Created by Wojtek on 27.07.2017.
 */

@SuppressLint("SetJavaScriptEnabled")
public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.ViewHolder> {

    private List<GeocacheLogInterface> geocacheLogs;
    private Activity context;

    LogListAdapter(List<GeocacheLogInterface> geocacheLogs, Activity context) {
        this.geocacheLogs = geocacheLogs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logs_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        GeocacheLogInterface geocacheLog = geocacheLogs.get(position);

        String description = getFormatedHtmlString(geocacheLog.getComment());
        holder.logCommentWebView.getSettings().setJavaScriptEnabled(true);
        holder.logCommentWebView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        holder.logCommentWebView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);

//        User user = geocacheLogs.get(position).getUser();
//        holder.logAuthorTextView.setText(user.getUsername() + " (" + user.getFoundCaches() + ")");

        holder.logAuthorTextView.setText(geocacheLog.getUser().getUsername());
        DateTime logDate = geocacheLog.getDateTime();
        holder.logDateTextView.setText(StringUtils.getDateString(logDate, context));
        holder.logTimeTextView.setText(String.format(context.getString(R.string.separated_time), logDate.getHourOfDay(), logDate.getMinuteOfHour()));
        holder.logTypeImageView.setImageResource(GeocacheUtils.getLogIcon(geocacheLog.getType()));
        holder.logTypeImageView.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(geocacheLog.getType())));

        holder.recommendationImageView.setVisibility(geocacheLog.isRecommended() ? View.VISIBLE : View.GONE);

        if(geocacheLog.getImages().size() > 0) {
            holder.photoRecycleView.setFocusable(false);
            ViewCompat.setNestedScrollingEnabled(holder.photoRecycleView, false);
            holder.photoRecycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            PhotosAdapter adapter = new PhotosAdapter(geocacheLog.getImages(), context);
            holder.photoRecycleView.setAdapter(adapter);
            holder.photoRecycleView.setVisibility(View.VISIBLE);
        } else {
            holder.photoRecycleView.setVisibility(View.GONE);
        }

        if(geocacheLog.isReadyToSync() == null) {
            //GeocacheLog
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.notSyncedInfo.setVisibility(View.GONE);
        } else {
            //GeocacheLogDraw
            if(geocacheLog.isReadyToSync()) {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.accent_gray));
                holder.notSyncedInfo.setText(context.getResources().getString(R.string.log_not_synchronized_yet));
                holder.notSyncedInfo.setVisibility(View.VISIBLE);
            } else {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.accent_gray));
                holder.notSyncedInfo.setText(context.getResources().getString(R.string.draw_of_log));
                holder.notSyncedInfo.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return geocacheLogs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logTypeImageView)
        ImageView logTypeImageView;
        @BindView(R.id.recommendationImageView)
        ImageView recommendationImageView;
        @BindView(R.id.logAuthorTextView)
        TextView logAuthorTextView;
        @BindView(R.id.logDateTextView)
        TextView logDateTextView;
        @BindView(R.id.logTimeTextView)
        TextView logTimeTextView;
        @BindView(R.id.logCommentWebView)
        WebView logCommentWebView;
        @BindView(R.id.photoList)
        RecyclerView photoRecycleView;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.notSyncedInfo)
        TextView notSyncedInfo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}