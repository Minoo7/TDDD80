package com.vinga129.savolax.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class BaseFragment2<T extends ViewDataBinding> extends Fragment {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ActivityFragmentAnnoation {
        int contentId();
    }

    public static class ActivityFragmentAnnoationManager {

        public static int check(Object clazz) {
            boolean annotationPresent = clazz.getClass().isAnnotationPresent(ActivityFragmentAnnoation.class);
            if (!annotationPresent){
                throw new IllegalStateException("Activity必须有注解");
            }
            ActivityFragmentAnnoation annotation = clazz.getClass().getAnnotation(ActivityFragmentAnnoation.class);
            return annotation.contentId();
        }
    }

    private int contentId;
    protected Bundle bundle;
    protected Activity a;
    protected T binding;

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (contentId == 0){
            bundle = getArguments();
            contentId = ActivityFragmentAnnoationManager.check(this);
            a = activity;
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, contentId, container, false);
            initFragmentImpl();
        }
        return binding.getRoot();
    }

    protected abstract void initFragmentImpl();
}