package com.vinga129.savolax.ui.camera;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentImageResultBinding;
import com.vinga129.savolax.other.AddImageViewModel;

@AnnotationContentId(contentId = R.layout.fragment_image_result)
public class ImageResultFragment extends BaseFragment<FragmentImageResultBinding> {

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

            @SuppressWarnings("ConstantConditions")
            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.accept) {
                    addImageViewModel.showImageResult();
                    navController.navigate(addImageViewModel.getDestinationForResult().getValue());
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(final ActionMode actionMode) {
            }
        };

        ((MainActivity) requireActivity()).setCallBackWithTitle(callback, "Selected photo");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((MainActivity) requireActivity()).finishActionMode();
    }
}