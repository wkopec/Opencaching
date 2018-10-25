package pl.opencaching.android.ui.gallery.image;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Image;

import static pl.opencaching.android.ui.gallery.GalleryActivity.KEY_IMAGE_ITEM;

public class ImageFragment extends Fragment {

    @BindView(R.id.photo)
    PhotoView photo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null);
        ButterKnife.bind(this, view);

        Image image = getArguments().getParcelable(KEY_IMAGE_ITEM);

        Picasso.with(getActivity())
                .load(image.getImageUrl())
                .placeholder(getResources().getDrawable(R.drawable.ic_image_placeholder))
                .into(photo);



        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && photo != null) {
            photo.setScale(1);
        }
    }


}
