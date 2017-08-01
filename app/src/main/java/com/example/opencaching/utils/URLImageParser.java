package com.example.opencaching.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.example.opencaching.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Wojtek on 31.07.2017.
 */

public class URLImageParser implements Html.ImageGetter {

    private TextView textView = null;
    Context context;


    public URLImageParser(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;

    }

    @Override
    public Drawable getDrawable(String source) {
//        Bitmap downloadedImage = null;
//        int width = 50;
//        int height = 50;
//        try {
//            downloadedImage = Picasso.with(context).load(source).get();
//            width = downloadedImage.getWidth();
//            height = downloadedImage.getHeight();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        Picasso.with(context)
                .load(source)
                .placeholder(R.drawable.ic_archive)
                .into(drawable);

        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }
}