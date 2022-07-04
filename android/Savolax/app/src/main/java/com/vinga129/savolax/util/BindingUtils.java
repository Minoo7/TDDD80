package com.vinga129.savolax.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.databinding.BindingAdapter;

import com.airbnb.paris.Paris;
import com.vinga129.savolax.R;
import com.vinga129.savolax.retrofit.rest_objects.groups.Genders;
import java.util.Arrays;
import java.util.function.Function;

public class BindingUtils {

    @BindingAdapter({"imageUrl", "error"})
    public static <T, C> void loadImage(ImageView view, T url, C error) {
        /*Picasso.get().load(url).error(error).into(view);
        if (url == null) {
            view.setImageDrawable(error);
        }*/
    }

    @BindingAdapter("android:src")
    public static void setImageViewSrc(ImageView view, Bitmap img) {
        view.setImageBitmap(img);
    }


    @BindingAdapter("customOnClick")
    public static void customOnClick(View view, Consumer<View> function) {
        view.setOnClickListener((x) -> function.accept(view));
    }

    @BindingAdapter({"customChecked", "checkedFunc", "notCheckedFunc"})
    public static void customChecked(View view, boolean checked, @Nullable Consumer<View> checkedFunc,
            @Nullable Runnable notCheckedFunc) {
        //Consumer d = (g) -> changeBackgroundTint(g, 1);
        if (checked && checkedFunc != null) {
            checkedFunc.accept(view);
        }
    }

    public static <T> String[] propsToStringArray(Genders[] values, Function<Genders, String> extractor) {
        return Arrays.stream(values).map(extractor).toArray(String[]::new);
    }

    @BindingAdapter({"myLikeButton", "checkedColor"})
    public static void myLikeButton(View view, boolean checked, int checkedColor) {
        if (checked) {
            ((ToggleButton) view).setChecked(true);
            changeBackgroundTint(view, checkedColor);
        }
    }

    @BindingAdapter({"customAdapter", "extractor"})
    public static <T> void setAdapter(View view, T[] values, Function<T, String> extractor) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(
                    new ArrayAdapter<String>(view.getContext(), R.layout.default_dropdown_menu_popup_item, com.vinga129.savolax.retrofit.rest_objects.groups.enumToStrings(values, extractor)));
        }
    }

    @BindingAdapter("dropDownItems")
    public static void setDropDownItems(View view, String[] strings) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.default_dropdown_menu_popup_item, strings));
        }
    }

    public static void changeBackgroundTint(View view, int color) {
        Paris.styleBuilder(view)
                .backgroundTint(color)
                .apply();
    }

    public static void changeBackgroundTint(Context context, View view, int color) {
        changeBackgroundTint(view, ContextCompat.getColor(context, color));
    }
}
