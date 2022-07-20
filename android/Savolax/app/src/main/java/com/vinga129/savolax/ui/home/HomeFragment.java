package com.vinga129.savolax.ui.home;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.ui.NavigationUI;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.FragmentHomeBinding;
import com.vinga129.savolax.databinding.PostItemBinding;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import com.vinga129.savolax.ui.post.PostViewModel;
import com.vinga129.savolax.ui.profile.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.ui.profile.ProfileFragmentDirections;

@AnnotationContentId(contentId = R.layout.fragment_home)
public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements BaseRecyclerAdapter.OnItemListener {

    @Override
    protected void initFragment() {
        binding.setViewmodel(new HomeViewModel(user.getId()));
        PostItemRecyclerAdapter adapter = new PostItemRecyclerAdapter(this);
        binding.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int pos, Object data) {
        if (navController == null)
            navController = ((MainActivity) requireActivity()).getNavController();
        navController.navigate(MobileNavigationDirections.moveToSpecificPost((Post) data));

        //((MainActivity) requireActivity()).getBinding().navView.getMenu().findItem(R.id.)
        //NavigationUI.onNavDestinationSelected()
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}