package com.operatorsapp.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.operatorsapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ShowImageDialog extends DialogFragment {


    private static boolean isFromFile;
    private String mImageUrl;
    private ImageView mImageIv;
    private File mImageFile;

    public static ShowImageDialog newInstance(String imageUrl) {


        ShowImageDialog fragment = new ShowImageDialog();
        fragment.setImage(imageUrl);
//        fragment.setLister(listener);
        return fragment;
    }

    public static ShowImageDialog newInstance(File file) {
        ShowImageDialog fragment = new ShowImageDialog();
        fragment.setImageFile(file);
        return fragment;
    }

    private void setImageFile(File file) {
        mImageFile = file;
        isFromFile = true;
    }

    private void setImage(String imageUrl) {
        mImageUrl = imageUrl;
        isFromFile = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_image_dialog, container, false);
        mImageIv = view.findViewById(R.id.show_image_dialog_iv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setupImage(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Callback picassoCallback = new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        if (getView() != null) {
                            Snackbar.make(getView(), getString(R.string.failed_to_load_image), Snackbar.LENGTH_SHORT);
                        }
                    }
                };

                if (!isFromFile){
                    Picasso.with(context).load(mImageUrl).fit().into(mImageIv, picassoCallback);
                }else {
                    Picasso.with(context).load(mImageFile).fit().into(mImageIv, picassoCallback);
                }
            }
        }, 500);


    }
}

