package com.luminous.pdi.home.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.luminous.pdi.PDIApplication;
import com.luminous.pdi.R;
import com.luminous.pdi.activities.LoginActivity.activities.LoginActivity;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.ActivityHomePageBinding;
import com.luminous.pdi.draft.fragments.DraftFragment;
import com.luminous.pdi.home.adapter.NavDrawerRecyclerViewAdapter;
import com.luminous.pdi.home.dto.BottomMenu;
import com.luminous.pdi.home.dto.Datum;
import com.luminous.pdi.home.dto.NavigationBaseResponse;
import com.luminous.pdi.home.dto.NavigationMenuDatum;
import com.luminous.pdi.home.fragments.CameraPermissionFragment;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.profile.fragments.MyProfileFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePageActivity extends AppCompatActivity  implements View.OnClickListener, CameraPermissionFragment.CameraFilePermissionCallback, OnRecyclerViewItemClickListener {

    public static ActivityHomePageBinding binding;
    private CameraPermissionFragment cameraPermissionFragment;
    private Fragment fragment;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private String fragmentTag = "",Imei;
    private List<BottomMenu> bottomDataList;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    public  static boolean isLightScreen=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomePageActivity.this,R.layout.activity_home_page);

        sharedPrefsManager = new SharedPrefsManager(this);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        if(checkPermission(Manifest.permission.CALL_PHONE)){

        }else{

        }
        isTelephonyEnabled();
        fragmentTag = "HomePageFragment";
        isLightScreen=true;

        binding.bottomNavigation.getMenu().setGroupCheckable(0, false, false);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                binding.bottomNavigation.getMenu().setGroupCheckable(0, true, true);
                String bottomTabNavName = menuItem.getTitle().toString();
                if(bottomTabNavName.equalsIgnoreCase("Dashboard")){

                    fragment = HomePageFragment.newInstance("", "");
                    fragmentTag = "HomePageFragment";
                }else if(bottomTabNavName.equalsIgnoreCase("Pending")){

                    fragment = AttendenceFragment.newInstance("", "");
                    fragmentTag = "PendingFragment";
                }else if(bottomTabNavName.equalsIgnoreCase("Draft")){

                    fragment = DraftFragment.newInstance("", "");
                    fragmentTag = "DraftFragment";
                }else if(bottomTabNavName.equalsIgnoreCase("Completed")){

                    fragment = CompletedFragment.newInstance("", "");
                    fragmentTag = "CompletedFragment";
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, fragmentTag).addToBackStack(null).commit();
                return true;
            }
        });        initToolbar();
        setDrawer();
        openDashBoardPage();
        getNavigationMenuData();

        if (!CommonUtility.isNetworkAvailable(this)) {

            Menu menu = binding.bottomNavigation.getMenu();
            binding.bottomNavigation.getMenu().setGroupCheckable(0, false, true);

            binding.bottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
            menu.add(Menu.NONE, 0, Menu.NONE, "Dashboard").setIcon(R.drawable.ic_dashbottom);
            menu.add(Menu.NONE, 1, Menu.NONE, "Pending").setIcon(R.drawable.b_pending);
            menu.add(Menu.NONE, 2, Menu.NONE, "Draft").setIcon(R.drawable.b_draft);
            menu.add(Menu.NONE, 3, Menu.NONE, "Completed").setIcon(R.drawable.b_completed);

        } else {

            setBottomNavigationData();
        }

        //add headless fragment for camera permission check
        cameraPermissionFragment = (CameraPermissionFragment) getSupportFragmentManager().findFragmentByTag("CAMERA");
        if (cameraPermissionFragment == null) {
            cameraPermissionFragment = CameraPermissionFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(cameraPermissionFragment, "CAMERA")
                    .commit();
        }
    }

    private void setBottomNavigationData() {
        Menu menu = binding.bottomNavigation.getMenu();
        if(bottomDataList!=null && bottomDataList.size()>0){
            for(int iCount =0; iCount <bottomDataList.size(); iCount++){
                String modelName = bottomDataList.get(iCount).getModuleName();
                String moduleImage = bottomDataList.get(iCount).getModuleImage();
                if(moduleImage!=null && !moduleImage.isEmpty()){
                    int finalICount = iCount;

                    new AsyncTask<String, Integer, Drawable>(){

                        @Override
                        protected Drawable doInBackground(String... strings) {
                            Bitmap bmp = null;
                            try {
                                HttpURLConnection connection = (HttpURLConnection) new URL(moduleImage).openConnection();
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                bmp = BitmapFactory.decodeStream(input);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return new BitmapDrawable(bmp);
                        }

                        protected void onPostExecute(Drawable result) {

                            //Add image to ImageView

                            try {
                                if (result!=null){

                                    menu.add(Menu.NONE, finalICount, Menu.NONE, modelName).setIcon(result);
                                    menu.setGroupCheckable(0, false, true);

                                }else {
                                    binding.bottomNavigation.getMenu().setGroupCheckable(0, false, true);
                                    binding.bottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
                                    menu.add(Menu.NONE, 0, Menu.NONE, "Dashboard").setIcon(R.drawable.ic_dashbottom);
                                    menu.add(Menu.NONE, 1, Menu.NONE, "Pending").setIcon(R.drawable.b_pending);
                                    menu.add(Menu.NONE, 2, Menu.NONE, "Draft").setIcon(R.drawable.b_draft);
                                    menu.add(Menu.NONE, 3, Menu.NONE, "Completed").setIcon(R.drawable.b_completed);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }.execute();

                }

            }

        }

    }



    private void getNavigationMenuData() {
        if (!CommonUtility.isNetworkAvailable(this)) {
            return;
        }

        try {

            Imei = CommonUtility.getUniqueIMEIId(HomePageActivity.this);
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(HomePageActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(HomePageActivity.this);
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }

        String url = String.format(ServerConfig.getSideBottomNavigationUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(HomePageActivity.this),"EN","LoginActivity",CommonUtility.getNetworkType(HomePageActivity.this),CommonUtility.getNetworkOperator(HomePageActivity.this),""+System.currentTimeMillis(),"M","");
        apiInterface.navigationMenuData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NavigationBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(NavigationBaseResponse baseEntity) {

                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                Datum data = (Datum) baseEntity.getData().get(0);
                                if (data != null) {
                                    List<NavigationMenuDatum> DrawerDataList = data.getNavigationMenuData();
                                    bottomDataList = data.getBottomMenu();
                                    setDrawerNavigationList(DrawerDataList);
                                    // setBottomNavigationList(bottomDataList);
                                    setBottomNavigationData();
                                }
                            }catch(Exception ex){
                                ex.getMessage();
                            }


                        } else {
                            List<Integer> mDrawerDataList_static = new ArrayList<>();
                             List<String> mDrawerDataList=new ArrayList<>();

                            mDrawerDataList.add("DashBoard");
                            mDrawerDataList.add("My Profile");
                            mDrawerDataList.add("Pending");
                            mDrawerDataList.add("Completed");
                            mDrawerDataList.add("Change Password");
                            mDrawerDataList.add("Logout");
                            mDrawerDataList_static.add(R.drawable.dasboardimage);
                            mDrawerDataList_static.add(R.drawable.reprofile);
                            mDrawerDataList_static.add(R.drawable.repending);
                            mDrawerDataList_static.add(R.drawable.pending);
                            mDrawerDataList_static.add(R.drawable.rechangpass);
                            mDrawerDataList_static.add(R.drawable.relogout);

                            Toast.makeText(HomePageActivity.this, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                            setDrawerNavigationListStatic(mDrawerDataList,mDrawerDataList_static);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void setDrawerNavigationList(List<NavigationMenuDatum> drawerDataList) {

        binding.navDrawer.navDrawerList.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        binding.navDrawer.navDrawerList.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            binding.navDrawer.navDrawerList.setNestedScrollingEnabled(true);
        //NavDrawerRecyclerViewAdapter adapter = new NavDrawerRecyclerViewAdapter(HomePageActivity.this,drawerDataList,this);
       // binding.navDrawer.navDrawerList.setAdapter(adapter);
    }

    private void setDrawerNavigationListStatic(List<String> drawerDataList, List<Integer> mDrawerDataList_static) {

        binding.navDrawer.navDrawerList.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        binding.navDrawer.navDrawerList.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            binding.navDrawer.navDrawerList.setNestedScrollingEnabled(true);
        NavDrawerRecyclerViewAdapter adapter = new NavDrawerRecyclerViewAdapter(HomePageActivity.this,drawerDataList,mDrawerDataList_static,this);
        binding.navDrawer.navDrawerList.setAdapter(adapter);
    }

    private void openDashBoardPage() {

        fragment = HomePageFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    // hit bottom api


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!PDIApplication.isPermissionDialogShow) {
            cameraPermissionFragment.checkCameraAndFilePermissions();
            PDIApplication.isPermissionDialogShow = true;
        }
    }





    private void setBottomNavigation() {


    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarLayout.toolbar);


        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);


        binding.toolbarLayout.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        binding.toolbarLayout.ivHeaderCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* uncheckNavigationItem(binding.bottomNavigation);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, HomePageFragment.newInstance("", ""), "HomePageFragment").commit();
*/                Fragment fragment;

                try {
                    fragment = new AttendenceFragment();
                    FragmentManager fragmentManager;
                    fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }catch(Exception ex){
                    ex.getStackTrace();
                }

            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void uncheckNavigationItem(BottomNavigationView navigation) {
        try {
            navigation.getMenu().clear();
            // hitBottomNavigationApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < navigation.getMenu().size(); i++) {
                navigation.getMenu().getItem(i).setCheckable(false);
                BottomNavigationItemView tem = (BottomNavigationItemView) navigation.getMenu().getItem(i);
                ((BottomNavigationItemView) navigation.getMenu().getItem(i)).setShifting(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbarLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //  getCustomerPermissionData();
                invalidateOptionsMenu();
            }
        };

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
           /* Drawable drawable = getResources().getDrawable(R.drawable.ic_nav_icon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(drawable);*/

            getSupportActionBar().setHomeButtonEnabled(true);
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        }

        String UserName=sharedPrefsManager.getString(SharedPreferenceKeys.FIRST_NAME);
        binding.navDrawer.navProfileName.setText(UserName);
        binding.navDrawer.navRelProfile.setOnClickListener(this);
        binding.navDrawer.navImgProfile.setOnClickListener(this);


    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.nav_img_profile:
                binding.drawerLayout.closeDrawers();
                fragment = MyProfileFragment.newInstance("", "");
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                binding.drawerLayout.closeDrawer(Gravity.START, true);
                //   startActivity(new Intent(HomePageActivity.this, ProfileActivity.class));
                break;

            default:
                fragment = HomePageFragment.newInstance("", "");
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            binding.drawerLayout.closeDrawer(Gravity.START, true);
        }
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onCameraFilePermissionGranted() {
    }

    @Override
    public void onCameraFilePermissionDenied() {
        Toast.makeText(this, getString(R.string.permission_denied_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getApplicationContext(),"External storage permission",Toast.LENGTH_SHORT).show();

               /* ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        ContactUsFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);*/


            }
            Uri selectedFileUri = data.getData();
            if (selectedFileUri == null) {
                return;
            } else {
               /* MyApplication.getApplication()
                        .bus()
                        .send(new ContactUsUriEvent(selectedFileUri));*/
            }
        }

    }




    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
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
    public void onItemCLicked(View view, int position) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onItemCLicked(View view, int position, String source) {
        Fragment fragment = null;

        if(source.equalsIgnoreCase("DashBoard")){
            fragment = HomePageFragment.newInstance("", "");
            fragmentTag = "HomePageFragment";
        }else if(source.equalsIgnoreCase("My Profile")){
            fragment = MyProfileFragment.newInstance("", "");
            fragmentTag = "ProfileFragment";
        }else if(source.equalsIgnoreCase("Pending")){
            fragment = AttendenceFragment.newInstance("", "");
            fragmentTag = "PendingFragment";
        }else if(source.equalsIgnoreCase("Completed")){
            fragment = DraftFragment.newInstance("", "");
            fragmentTag = "DraftFragment";
        }else if(source.equalsIgnoreCase("Change Password")){
            fragment = CompletedFragment.newInstance("", "");
            fragmentTag = "CompletedFragment";
        }else if(source.equalsIgnoreCase("Logout")){
            logOutApp();
        }
        binding.drawerLayout.closeDrawer(Gravity.START, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, fragmentTag).addToBackStack(null).commit();



    }

    private void logOutApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomePageActivity.this.finish();

                        sharedPrefsManager.putString(SharedPreferenceKeys.USER_ID, "");
                        sharedPrefsManager.putString(SharedPreferenceKeys.TOKEN, "");
                        navigateToLoginActivity();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void navigateToLoginActivity() {

        startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
        finish();
    }


    //
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;

            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //do your specific task after read phone state granted

                } else {
                    //   Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private boolean isTelephonyEnabled(){
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState()== TelephonyManager.SIM_STATE_READY;
    }

}