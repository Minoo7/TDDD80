package com.vinga129.savolax.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.databinding.BindingAdapter;

import com.airbnb.paris.Paris;
import com.vinga129.savolax.R;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import com.vinga129.savolax.retrofit.rest_objects.groups.Genders;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
            //((ToggleButton) view).setChecked(true);
            changeBackgroundTint(view, checkedColor);
        }
    }

    @BindingAdapter({"customAdapter", "extractor"})
    public static <T> void setAdapter(View view, T[] values, Function<T, String> extractor) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(
                    new ArrayAdapter<String>(view.getContext(), R.layout.default_dropdown_menu_popup_item,
                            com.vinga129.savolax.retrofit.rest_objects.groups.enumToStrings(values, extractor)));
        }
    }

    @BindingAdapter("dropDownItems")
    public static void setDropDownItems(View view, String[] strings) {
        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setAdapter(
                    new ArrayAdapter<String>(view.getContext(), R.layout.default_dropdown_menu_popup_item, strings));
        }
    }

    @BindingAdapter({"customSpan", "customSpanWidth"})
    public static void customSpan(TextView view, String text, String widthText) {
        /*SpannableString s = new SpannableString("this is a test"+"\n"+"this is a test");
        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(30, 0), 0, s.length(), 0);
        textView .setText(s);*/

        //SpannableString s = new SpannableString(text);
        //s.setSpan(new LeadingMarginSpan.Standard(300, 0), 0, 1, 0);
        //view.setText(s);
        //String space10 = new String(new char[10]).replace('\0', ' ');
        //view.setText(widthText.length());
    }

    public static void changeBackgroundTint(View view, int color) {
        Paris.styleBuilder(view)
                .backgroundTint(color)
                .apply();
    }

    public static void changeBackgroundTint(Context context, View view, int color) {
        changeBackgroundTint(view, ContextCompat.getColor(context, color));
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDateToFriendlyFormat(String text) {
        if (text == null)
            return null;
        text = text.replace("T", " ");
        SimpleDateFormat fromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = null;
        try {
            date = fromServer.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String output = myFormat.format(date);
        return output;
    }
}
