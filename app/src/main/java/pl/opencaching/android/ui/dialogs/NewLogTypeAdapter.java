package pl.opencaching.android.ui.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.utils.GeocacheUtils;

import static pl.opencaching.android.utils.GeocacheUtils.getLogType;

public class NewLogTypeAdapter extends RecyclerView.Adapter<NewLogTypeAdapter.ViewHolder>{

    Context context;
    private ArrayList<String> logTypes;
    private View.OnClickListener listener;

    public NewLogTypeAdapter(ArrayList<String> logTypes, Context context, View.OnClickListener listener) {
        this.logTypes = logTypes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_log_type, parent, false);
        return new NewLogTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.logType.setText(getLogType(logTypes.get(position)));
        holder.logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(logTypes.get(position)));
        holder.logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(logTypes.get(position))));
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return logTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logTypeIcon)
        ImageView logTypeIcon;
        @BindView(R.id.logType)
        TextView logType;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
