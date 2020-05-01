package com.luminous.pdi.calendar.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.luminous.pdi.core.SharedPrefsManager;

public abstract class BaseFragment  extends Fragment {

    public SharedPrefsManager mPref;
    private ProgressDialog progressDialog;

    public Drawable camera;
    public PermissionChecker permissionChecker;
    public Drawable layoutBackground;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPref = new SharedPrefsManager(getContext());


     /*  permissionChecker = new PermissionChecker().initPermissionChecker(getActivity(), new String[]{"android.permission.CAMERA",
                "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"});*/

          }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (onSaveInstance(outState) != null)
                outState.putAll(onSaveInstance(outState));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            onViewRestore(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract Bundle onSaveInstance(Bundle outState);

    public abstract void onViewRestore(Bundle savedInstance);

    public void showProgressDialog(Context mContxt) {
        progressDialog = new ProgressDialog(mContxt);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }


    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
