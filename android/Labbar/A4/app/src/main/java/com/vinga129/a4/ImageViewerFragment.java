package com.vinga129.a4;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewerFragment extends BaseFragment {

    private ImageView closeBtn;
    private ImageView imageView;
    private TextView locationInfoText;

    @Override
    public int getLayout() {
        return R.layout.fragment_image_viewer;
    }

    @Override
    public void init() {
        closeBtn = view.findViewById(R.id.closeBtn);
        imageView = view.findViewById(R.id.imageView);
        locationInfoText = view.findViewById(R.id.locationInfoText);

        closeBtn.setOnClickListener((__) -> navController.navigate(ImageViewerFragmentDirections.backCameraFragment()));
        model.getCapturedImage().observe(getViewLifecycleOwner(), image -> imageView.setImageBitmap(image));
        model.getLocation().observe(getViewLifecycleOwner(), this::setLocationText);
    }

    private void setLocationText(Location location) {
        Address address = main.getLocationAsAddress(location);
        String text = "";
        if (address != null)
            text = address.getAddressLine(0);
        else
            text = String.format("Latitude: %s\nLongitude: %s", location.getLatitude(), location.getLongitude());
        locationInfoText.setText(text);
    }
}