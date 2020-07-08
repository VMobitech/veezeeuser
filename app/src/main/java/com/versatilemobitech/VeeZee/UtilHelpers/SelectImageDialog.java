package com.versatilemobitech.VeeZee.UtilHelpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.interfaces.ImageDialogInterface;

public class SelectImageDialog extends DialogFragment implements View.OnClickListener {

    TextView textViewCamera, textViewGallery;
    ImageDialogInterface imageDialogInterface;

/*
    public SelectImageDialog() {

    }
    public SelectImageDialog(Activity activity,) {

    }
*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_camera, container, false);
        textViewCamera =  view.findViewById(R.id.textViewCamera);
        textViewGallery =  view.findViewById(R.id.textViewGallery);
        textViewCamera.setOnClickListener(this);
        textViewGallery.setOnClickListener(this);
        return view;
    }

    public void setCallback(ImageDialogInterface imageDialogInterface) {
        this.imageDialogInterface = imageDialogInterface;
    }

    @Override
    public void onClick(View v) {
        if (v == textViewCamera) {
            imageDialogInterface.onCameraSelect();
              dismiss();
        } else if (v == textViewGallery) {
            imageDialogInterface.onGallerySelect();
            dismiss();
        }
    }
}
