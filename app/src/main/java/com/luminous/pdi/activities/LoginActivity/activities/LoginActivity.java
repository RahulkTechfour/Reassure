package com.luminous.pdi.activities.LoginActivity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luminous.pdi.R;
import com.luminous.pdi.activities.LoginActivity.CreateOTP.ReassureCreateOTP;
import com.luminous.pdi.activities.LoginActivity.VerifyOTP.ReassureVerifyOTP;
import com.luminous.pdi.activities.LoginActivity.VerifyOTP.User;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.ActivityLoginBinding;
import com.luminous.pdi.home.activities.HomePageActivity;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    String Imei;
    private ActivityLoginBinding loginBinding;
    private String mUserId, otpPin;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    String deviceIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(LoginActivity.this,R.layout.activity_login);
        sharedPrefsManager = new SharedPrefsManager(this);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

        if (sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN).trim().length() > 0 && (sharedPrefsManager.getString(SharedPreferenceKeys.IS_LOGIN).trim().equalsIgnoreCase("yes"))) {
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
            finish();
        }

        setOnClickListener();
        setBackgoundImage();
        CheckPermissionAndStartIntent();
    }

    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //SEY SOMTHING LIKE YOU CANT ACCESS WITHOUT PERMISSION
        } else {
            doSomthing();
        }
    }

    private void doSomthing() {
        deviceIMEI = getDeviceIMEI(LoginActivity.this);
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Activity activity) {

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            else
                try {
                    deviceUniqueIdentifier = tm.getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length())
                deviceUniqueIdentifier = "0";
        }
        return deviceUniqueIdentifier;
    }

    private void setBackgoundImage() {

        Glide.with(LoginActivity.this).load(R.drawable.reassurelogoone4)
                .apply(new RequestOptions().placeholder(R.drawable.reassurelogoone4)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(loginBinding.mainImage);
    }

    private void setOnClickListener() {
        loginBinding.btnlogin.setOnClickListener(this);
        loginBinding.btnOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnlogin:
                attemptLogin();
                break;

            case R.id.btnOTP:

                if(isPermissionGranted()){
                    createOTP();
                }

                break;
        }

    }




    public void createOTP() {
        mUserId = loginBinding.username.getText().toString().trim();
        mUserId = mUserId.replaceFirst("^0+(?!$)", "");
        if (mUserId.trim().length() > 0) {
            loginBinding.username.setError(null);
            getDataAndLoginUser();
        } else {
            loginBinding.username.setError("Enter Distributer/Dealer Id");
        }
    }

    public void attemptLogin() {

        otpPin = loginBinding.password.getText().toString().trim();
        if (otpPin.trim().length() > 0) {
            loginBinding.password.setError(null);
            getDataAndValidateOtp();
        } else {
            loginBinding.password.setError("Password field cannot be empty");
            //  Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get device informations and send to user login.
     */
    private void getDataAndLoginUser() {
        if (!CommonUtility.isNetworkAvailable(this)) {
            return;
        }
        loginBinding.pbLoading.setVisibility(View.VISIBLE);

        try {
            Imei = CommonUtility.getUniqueIMEIId(LoginActivity.this);
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(LoginActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(LoginActivity.this);
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }
        ReassureCreateOTP sendUserMobileNo=new ReassureCreateOTP(mUserId);
        apiInterface.createReassureOtp(ServerConfig.reassurecreateOTPUrl(),sendUserMobileNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReassureCreateOTP>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ReassureCreateOTP createOtpEntity) {
                        loginBinding.pbLoading.setVisibility(View.GONE);
                        if (createOtpEntity.getResponseCode()==200) {
                            loginBinding.password.setText(createOtpEntity.getResponseData().getOtp());
                            loginBinding.username.setText(mUserId);
                            loginBinding.username.setEnabled(false);
                            loginBinding.relPass.setVisibility(View.VISIBLE);
                            loginBinding.password.setEnabled(true);
                            loginBinding.username.setAlpha(0.5f);
                            loginBinding.btnOTP.setVisibility(View.GONE);
                            loginBinding.btnlogin.setVisibility(View.VISIBLE);
                            //  loginBinding.tvResend.setVisibility(View.VISIBLE);

                        } else {
                            loginBinding.pbLoading.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, createOtpEntity.getResponseMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginBinding.pbLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void getDataAndValidateOtp() {
        if (!CommonUtility.isNetworkAvailable(this)) {
            return;
        }

        loginBinding.pbLoading.setVisibility(View.VISIBLE);
        try {
            Imei = CommonUtility.getUniqueIMEIId(LoginActivity.this);
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(LoginActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(LoginActivity.this);
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }

        ReassureVerifyOTP verifyOtp=new ReassureVerifyOTP(mUserId,otpPin);
        String url = String.format(ServerConfig.ReassureVerifyOTPUrl());
        apiInterface.reassureVerifyOtp(url,verifyOtp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReassureVerifyOTP>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ReassureVerifyOTP verifyOtpEntity) {

                        loginBinding.pbLoading.setVisibility(View.GONE);
                        if (verifyOtpEntity.getResponseCode()==200) {
                           User userProfileData = verifyOtpEntity.getUser();
                            if(userProfileData!=null) {
                                User profileData = userProfileData;
                                if(profileData!=null) {

                                    try {
                                        sharedPrefsManager.putString(SharedPreferenceKeys.USER_ID, mUserId);
                                        sharedPrefsManager.putString(SharedPreferenceKeys.TOKEN, verifyOtpEntity.getToken());
                                        sharedPrefsManager.putString(SharedPreferenceKeys.FIRST_NAME, profileData.getFirstName());
                                        if(profileData.getMiddleName()!=null){
                                            sharedPrefsManager.putString(SharedPreferenceKeys.USER_MIDDLE_NAME, profileData.getMiddleName());

                                        }
                                        sharedPrefsManager.putString(SharedPreferenceKeys.USER_LAST_NAME, profileData.getLastName());
                                        sharedPrefsManager.putString(SharedPreferenceKeys.IS_LOGIN, "yes");
                                        startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                        } else {
                            loginBinding.pbLoading.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, verifyOtpEntity.getResponseMessage(), Toast.LENGTH_LONG).show();
                            sharedPrefsManager.putString(SharedPreferenceKeys.IS_LOGIN, "no");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginBinding.pbLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

   /* public void verifyUser() {

        if (!CommonUtility.isNetworkAvailable(this)) {
            return;
        }



        // Log.e("Garvit", token);
        String url = ServerConfig.newUrl(String.format(ServerConfig.userverificationURL()), getApplicationContext(), LoginActivity.class.getSimpleName());

        apiInterface.getToken(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody response) {


                        try {
                            String data = response.string();
                            JSONObject jsonArray = new JSONObject(data);
                            SubmitResponse submitResponse = new Gson().fromJson(jsonArray.toString(), SubmitResponse.class);
                            if (submitResponse != null && submitResponse.getStatus().equalsIgnoreCase("200")) {
                                sharedPrefsManager.putString(SharedPreferenceKeys.TOKEN, submitResponse.getToken());
                            } else {
                                Toast.makeText(getApplicationContext(), submitResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }*/


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 2: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    // do your specific task after read phone state granted

                } else {
                    //          Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
