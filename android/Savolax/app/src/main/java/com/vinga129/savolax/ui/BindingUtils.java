package com.vinga129.savolax.ui;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class BindingUtils {
    @BindingAdapter({"imageUrl", "error"})
    public static <T, C> void loadImage(ImageView view, T url, C error) {
        /*Picasso.get().load(url).error(error).into(view);
        if (url == null) {
            view.setImageDrawable(error);
        }*/
    }

}
