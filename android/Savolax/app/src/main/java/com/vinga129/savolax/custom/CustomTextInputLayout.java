package com.vinga129.savolax.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.vinga129.savolax.R;

// Custom view class being the same as TextInputLayout, but now with the custom attribute "formRequired" added.
public class CustomTextInputLayout extends TextInputLayout {
    private boolean required;
    private String key;

    public CustomTextInputLayout(@NonNull final Context context) {
        super(context);
    }

    public CustomTextInputLayout(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextInputLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextInputLayout, 0, 0);
        try {
            required = a.getBoolean(R.styleable.CustomTextInputLayout_formRequired, false);
            key = a.getString(R.styleable.CustomTextInputLayout_jsonKey);
        } finally {
            a.recycle();
        }
    }

    public boolean isRequired() {
        return required;
    }

    public String getKey() {
        return key;
    }
}
