package com.vinga129.savolax.ui.later;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.vinga129.savolax.R;

public class TempForLaterAuth extends Fragment {
    public void test() {
        Context context = getActivity();
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences("API", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("JWT_KEY", "dwdwdwd");
            editor.apply();
        }
    }
}
