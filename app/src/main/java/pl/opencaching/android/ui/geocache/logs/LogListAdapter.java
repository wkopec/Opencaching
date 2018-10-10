package pl.opencaching.android.ui.geocache.logs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import pl.opencaching.android.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.StringUtils;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.ViewHolder> {

    private List<GeocacheLog> geocacheLogs;
    private Context context;

    public LogListAdapter(List<GeocacheLog> geocacheLogs, Context context) {
        this.geocacheLogs = geocacheLogs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logs_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

//        URLImageParser imageGetter = new URLImageParser(holder.logCommentTextView, context);
//        Spannable html;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            html = (Spannable) Html.fromHtml(geocacheLogs.get(position).getComment(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//        } else {
//            html = (Spannable) Html.fromHtml(geocacheLogs.get(position).getComment(), imageGetter, null);
//        }
//
//        holder.logCommentTextView.setText(html);

        holder.logCommentTextView.getSettings().setJavaScriptEnabled(true);
        holder.logCommentTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        holder.logCommentTextView.loadDataWithBaseURL(null, geocacheLogs.get(position).getComment(), "text/html", "UTF-8", null);

//        User user = geocacheLogs.get(position).getUser();
//        holder.logAuthorTextView.setText(user.getUsername() + " (" + user.getFoundCaches() + ")");

        holder.logAuthorTextView.setText(geocacheLogs.get(position).getUser().getUsername());
        DateTime logDate = geocacheLogs.get(position).getDate();
        holder.logDateTextView.setText(StringUtils.getDateString(geocacheLogs.get(position).getDate(), context));
        holder.logTimeTextView.setText(logDate.getHourOfDay() + ":" + logDate.getMinuteOfHour());
        holder.logTypeImageView.setImageResource(GeocacheUtils.getLogIcon(geocacheLogs.get(position).getType()));
        holder.logTypeImageView.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(geocacheLogs.get(position).getType())));
        if(geocacheLogs.get(position).isRecommended())
            holder.recommendationImageView.setVisibility(View.VISIBLE);
        else
            holder.recommendationImageView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return geocacheLogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.logCommentTextView)
        WebView logCommentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ImageGetterAsyncTask extends AsyncTask<TextView, Void, Bitmap> {


        private LevelListDrawable levelListDrawable;
        private Context context;
        private String source;
        private TextView t;

        public ImageGetterAsyncTask(Context context, String source, LevelListDrawable levelListDrawable) {
            this.context = context;
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextView... params) {
            t = params[0];
            try {
                //GeocacheLog.d(LOG_CAT, "Downloading the image from: " + source);
                return Picasso.with(context).load(source).get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                Point size = new Point();
                ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
                // Lets calculate the ratio according to the screen width in px
                int multiplier = size.x / bitmap.getWidth();
                //GeocacheLog.d(LOG_CAT, "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.getWidth() * multiplier, bitmap.getHeight() * multiplier);
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }



}