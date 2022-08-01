package com.vinga129.savolax.ui.more;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vinga129.savolax.LoginActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.data.LoginRepository;
import com.vinga129.savolax.databinding.FragmentMoreBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AnnotationContentId(contentId = R.layout.fragment_more)
public class MoreFragment extends BaseFragment<FragmentMoreBinding> {

    @Override
    protected void initFragment() {
        binding.setFragment(this);
    }

    public void logout() {
        LoginRepository.getInstance().logout().doOnComplete(() -> {
            // Save JWT Token
            SharedPreferences sharedPref = requireContext().getSharedPreferences("API", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("JWT_KEY");
            editor.apply();

            // Go to LoginActivity
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            requireActivity().finish();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}