package com.luminous.pdi.RemarkFragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.luminous.pdi.R;
import com.luminous.pdi.RemarkFragment.dto.DataMethod;
import com.luminous.pdi.RemarkFragment.dto.DefectPhoto;
import com.luminous.pdi.RemarkFragment.dto.ImageUpload;
import com.luminous.pdi.activities.LoginActivity.activities.LoginActivity;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentRemarkBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.core.CommonUtility.dismissProgressDialog;
import static com.luminous.pdi.core.CommonUtility.showProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemarkFragment extends Fragment {
    private static final String TAG = "RemarkFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext;
    FragmentRemarkBinding remarkPageBinding;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int PICK_FILE_REQUEST=101;
    String imageBase64String="";
    ImageView img_upload;
    String gallerybase="";
    private RetrofitInterface apiInterface;
    private SharedPrefsManager sharedPrefsManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    String Imei;
    public RemarkFragment() {
        // Required empty public constructor
    }
    public static RemarkFragment newInstance(String param1, String param2) {
        RemarkFragment fragment = new RemarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        remarkPageBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_remark, container, false);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        sharedPrefsManager = new SharedPrefsManager(mContext);

        Log.d(TAG, "yourtokeeen===="+sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN));
        remarkPageBinding.imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    chooseImage();

            }
        });


        return remarkPageBinding.getRoot();
    }

    private void chooseImage() {

        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
           /* Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_FILE_REQUEST);*/


            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"),3);

        }
    }

    private void uploadgalleryImageApi(String basrsixfour) {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }

      ImageUpload imageUpload = new ImageUpload();
        imageUpload.setDefect_image(basrsixfour);

          int caseid=258;
        imageUpload.setImagemethod("invoice");
        imageUpload.setCase_id("258");



        showProgressDialog(getContext());
        String url =String.format( ServerConfig.reAssureUploadImage(),mContext,RemarkFragment.class.getSimpleName() + ".class",imageUpload.getDefect_image(),imageUpload.getImagemethod(),imageUpload.getCase_id());
        apiInterface.getreasureUploadImage(sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),url,imageUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageUpload>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ImageUpload pdiDebitNote) {
                        dismissProgressDialog();

                        if (pdiDebitNote.getStatus().equalsIgnoreCase("200")) {

                            Toast.makeText(getActivity(), pdiDebitNote.getMessage(), Toast.LENGTH_SHORT).show();

                          //  showDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(mContext,""+e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                        CommonUtility.printLog("SaveContactUsSuggestion", "onComplete");
                    }
                });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {

            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        return;
                    } else {
                        Uri selectedFileUri = data.getData();
                        Bitmap bitmap;

                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),selectedFileUri);

                            new AsyncTask<Void,Void,String>(){

                                @Override
                                protected String doInBackground(Void... voids) {

                                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                    byte[]b=baos.toByteArray();

                                    String encodeimage= Base64.encodeToString(b,Base64.DEFAULT);

                                    return encodeimage;

                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    gallerybase=s;

                                }
                            }.execute();
                            uploadgalleryImageApi(gallerybase);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }


                }
                break;
        }
    }
}
