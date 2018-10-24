package pl.opencaching.android.ui.geocache.info;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

import io.realm.RealmResults;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Attribute;
import pl.opencaching.android.ui.base.BaseActivity;

public class AttributesAdapter extends RecyclerView.Adapter<AttributesAdapter.ViewHolder> {

    private RealmResults<Attribute> attributes;
    private BaseActivity activity;

    AttributesAdapter(RealmResults<Attribute> attributes, BaseActivity activity) {
        this.attributes = attributes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attribute, parent, false);
        return new AttributesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.attibuteIcon.setImageResource(attributes.get(position).getIcon());
        holder.itemView.setOnClickListener(v -> {
            Attribute attribute = attributes.get(position);
            activity.showMessage(attribute.getIcon(), attribute.getName(), attribute.getDescription());
        });
    }

    @Override
    public int getItemCount() {
        return attributes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.attributeIcon)
        ImageView attibuteIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
