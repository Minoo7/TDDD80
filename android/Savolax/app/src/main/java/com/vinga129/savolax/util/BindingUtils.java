package com.vinga129.savolax.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.paris.Paris;
import com.squareup.picasso.Picasso;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BindingUtils {

    public static SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @BindingAdapter("android:src")
    public static void setImageViewSrc(ImageView view, Bitmap img) {
        view.setImageBitmap(img);
    }

    @BindingAdapter({"android:checked", "uncheckedColor", "checkedColor"})
    public static void setToggleButtonColor(ToggleButton button, boolean checked, int uncheckedColor,
            int checkedColor) {
        int color = (checked) ? checkedColor : uncheckedColor;
        changeBackgroundTint(button, color);
        button.setChecked(checked);
    }

    @BindingAdapter({"android:checked", "styleOff", "styleOn"})
    public static void setToggleButtonStyle(ToggleButton toggleButton, boolean checked, int styleOff, int styleOn) {
        int style = (checked) ? styleOn : styleOff;
        Paris.style(toggleButton).apply(style);
        toggleButton.setChecked(checked);
    }

    @BindingAdapter({"customAdapter", "extractor"})
    public static <T> void setAdapter(View view, T[] values, Function<T, String> extractor) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(
                    new ArrayAdapter<>(view.getContext(), R.layout.default_dropdown_menu_popup_item,
                            com.vinga129.savolax.retrofit.rest_objects.groups.enumToStrings(values, extractor)));
        }
    }

    @BindingAdapter("dropDownItems")
    public static void setDropDownItems(View view, String[] strings) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(
                    new ArrayAdapter<>(view.getContext(), R.layout.default_dropdown_menu_popup_item, strings));
        }
    }

    public static void changeBackgroundTint(View view, int color) {
        if (color == 0)
            return;
        Paris.styleBuilder(view)
                .backgroundTint(color)
                .apply();
    }

    public static void changeBackgroundTint(Context context, View view, int color) {
        changeBackgroundTint(view, ContextCompat.getColor(context, color));
    }

    public static String convertDateToFriendlyFormat(String text) {
        if (text == null)
            return null;
        text = text.replace("T", " ");
        SimpleDateFormat fromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        try {
            date = fromServer.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return myFormat.format(Objects.requireNonNull(date));
    }

    public static int booleanToVisibility(boolean bool) {
        return (bool) ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("android:visibility")
    public static void booleanToVisibility(View view, boolean value) {
        view.setVisibility(booleanToVisibility(value));
    }

    // Used in profileFragment

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.get().load(url).error(error).into(view);
        if (url == null) {
            view.setImageDrawable(error);
        }
    }

    @SuppressWarnings("rawtypes")
    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @BindingAdapter("submitList")
    public static <T> void submitList(RecyclerView recyclerView, List<T> items) {
        if (recyclerView.getAdapter() instanceof BaseRecyclerAdapter && recyclerView.getAdapter() != null)
            ((BaseRecyclerAdapter) recyclerView.getAdapter()).updateData(items);
    }

    @BindingAdapter("loadBackgroundUrl")
    public static void setBackground(LinearLayout linearLayout, String imageUrl) {
        if (imageUrl != null) {
            ImageView img = new ImageView(linearLayout.getContext());
            Picasso.get().load(imageUrl).into(img, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    linearLayout.setBackground(img.getDrawable());
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
}
