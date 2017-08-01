package com.example.opencaching.fragments.geocache_logs;

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
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.models.okapi.Log;
import com.example.opencaching.utils.URLImageParser;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.opencaching.utils.ResourceUtils.getLogIcon;
import static com.example.opencaching.utils.ResourceUtils.getLogIconColor;
import static com.example.opencaching.utils.StringUtils.getDateString;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class LogsListAdapter extends RecyclerView.Adapter<LogsListAdapter.ViewHolder> {

    private List<Log> logs;
    private Context context;

    public LogsListAdapter(List<Log> logs, Context context) {
        this.logs = logs;
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
//            html = (Spannable) Html.fromHtml(logs.get(position).getComment(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//        } else {
//            html = (Spannable) Html.fromHtml(logs.get(position).getComment(), imageGetter, null);
//        }
//
//        holder.logCommentTextView.setText(html);



        holder.logCommentTextView.getSettings().setJavaScriptEnabled(true);
        holder.logCommentTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        holder.logCommentTextView.loadDataWithBaseURL(null, logs.get(position).getComment(), "text/html", "UTF-8", null);

        holder.logAuthorTextView.setText(logs.get(position).getUser().getUsername());
        DateTime logDate = new DateTime(logs.get(position).getDate());
        holder.logDateTextView.setText(getDateString(logDate, context));
        holder.logTimeTextView.setText(logDate.getHourOfDay() + ":" + logDate.getMinuteOfHour());
        holder.logTypeImageView.setImageResource(getLogIcon(logs.get(position).getType()));
        holder.logTypeImageView.setColorFilter(ContextCompat.getColor(context, getLogIconColor(logs.get(position).getType())));
        android.util.Log.d("Test", logs.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logTypeImageView)
        ImageView logTypeImageView;
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
                //Log.d(LOG_CAT, "Downloading the image from: " + source);
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
                //Log.d(LOG_CAT, "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.getWidth() * multiplier, bitmap.getHeight() * multiplier);
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }



}