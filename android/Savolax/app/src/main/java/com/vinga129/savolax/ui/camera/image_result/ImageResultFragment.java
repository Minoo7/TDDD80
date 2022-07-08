package com.vinga129.savolax.ui.camera.image_result;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentImageResultBinding;
import com.vinga129.savolax.other.AddImageViewModel;
import com.vinga129.savolax.ui.add_post.AddPostViewModel;

@AnnotationContentId(contentId = R.layout.fragment_image_result)
public class ImageResultFragment extends BaseFragment<FragmentImageResultBinding> {

    //private boolean accepted = false;
    private ActionMode actionMode;

    @Override
    protected void initFragment() {
        AddImageViewModel addImageViewModel = new ViewModelProvider(requireActivity()).get(AddImageViewModel.class);

        binding.setViewmodel(addImageViewModel);

        ActionMode.Callback callback = new Callback() {
            @Override
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                requireActivity().getMenuInflater().inflate(R.menu.image_result, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.accept) {
                    addImageViewModel.showImageResult();
                    navController.popBackStack();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(final ActionMode actionMode) {
                navController.popBackStack();
            }
        };
        actionMode = requireActivity().startActionMode(callback);
        actionMode.setTitle("Selected photo");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        actionMode.finish();
    }
}