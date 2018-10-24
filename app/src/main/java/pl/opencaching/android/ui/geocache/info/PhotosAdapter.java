package pl.opencaching.android.ui.geocache.info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Image;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private RealmList<Image> images;
    private Context context;

    public PhotosAdapter(RealmList<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_small, parent, false);
        return new PhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = images.get(position);
        if (image != null) {
            Picasso.with(context)
                    .load(image.getImageUrl())
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_archive))
                    .into(holder.photo);
            holder.photoDescription.setText(image.getCaption());
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.photoDescription)
        TextView photoDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
