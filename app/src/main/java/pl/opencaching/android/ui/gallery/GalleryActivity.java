package pl.opencaching.android.ui.gallery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Image;
import pl.opencaching.android.ui.base.BaseActivity;

public class GalleryActivity extends BaseActivity {

    public static String KEY_IMAGE_ITEM = "key_image_item:";

    @BindView(R.id.photo)
    ImageView photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        intent.setExtrasClassLoader(Image.class.getClassLoader());
        Image image = intent.getParcelableExtra(KEY_IMAGE_ITEM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photo.setTransitionName(image.getUniqueCaption());
        }

        Picasso.with(this)
                .load(image.getImageUrl())
                .placeholder(getResources().getDrawable(R.drawable.ic_image_placeholder))
                .into(photo);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
