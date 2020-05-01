package com.luminous.pdi.calendar.CalenderActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.fragment.AddProductLightDetailFragment;
import com.luminous.pdi.calendar.CalenderInterface;
import com.luminous.pdi.calendar.adapter.CalendarDataAdapter;
import com.luminous.pdi.calendar.fragment.BaseFragment;
import com.luminous.pdi.calendar.responce.CalenderData.Data;
import com.luminous.pdi.calendar.responce.CalenderData.GetCalenderData;
import com.luminous.pdi.calendar.responce.CalenderData.PendingvisitDetail;
import com.luminous.pdi.calendar.responce.DistyDetail;
import com.luminous.pdi.calendar.responce.newDto.PendingCalnderResponse;
import com.luminous.pdi.calendar.responce.newDto.PendingCountDetail;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.draft.fragments.DraftFragment;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.visit_detail.VisitDetailFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.home.activities.HomePageActivity.binding;

/**
 * Created by Mateusz Kornakiewicz on 26.05.2017.
 */

public class AttendenceFragment extends BaseFragment implements CalenderInterface {
    private SharedPrefsManager sharedPrefsManager;
    String Imei;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ArrayList<DistyDetail> pendiingDataNum = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    public RecyclerView mRvCalendar;
    CardView mllCardView;
    String SelectionDate;
    String selectedDate;
    String currentdate_;
    CalendarView calendarView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<EventDay> events = new ArrayList<>();
    TextView CalenderSelectedDate;
    ArrayList<String> valueone;
    String fragmentTag = "";


    public static AttendenceFragment newInstance(String param1, String param2) {
        AttendenceFragment fragment = new AttendenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_activity, container, false);
        sharedPrefsManager = new SharedPrefsManager(getActivity());
        mRvCalendar = (RecyclerView) view.findViewById(R.id.rvCalendar);
        mllCardView = (CardView) view.findViewById(R.id.llCardView);
        mllCardView.setVisibility(View.GONE);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRvCalendar.setLayoutManager(mLinearLayoutManager);

        toolBarHeader();
        Menu menu = binding.bottomNavigation.getMenu();
        menu.setGroupEnabled(0, true);
        binding.bottomNavigation.setClickable(true);
        AddProductLightDetailFragment.isHome=true;
        if (AddProductLightDetailFragment.isHome){

            CompleClickOnFragment();


        }

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        CalenderSelectedDate = (TextView) view.findViewById(R.id.CalenderSelectedDate);

        //  Calendar min = Calendar.getInstance();
        //  min.add(Calendar.MONTH, -2);

        //  Calendar max = Calendar.getInstance();
        //  max.add(Calendar.MONTH, 2);

        //  calendarView.setMinimumDate(min);
        //  calendarView.setMaximumDate(max);

        hitPendingAPI_api();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
             currentdate_ = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }


        getLoadDatawitCurrentDate(currentdate_);

        calendarView.setOnDayClickListener(eventDay ->


                // hitPendingAPI(eventDay));
                getLoadData(eventDay, ""));
               return view;

    }

    private void CompleClickOnFragment() {

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, fragmentTag).addToBackStack(null).commit();

                return true;
            }
        });


    }

    private void getLoadDatawitCurrentDate(String currentdate_) {


        /*get click Data*/
        if (!CommonUtility.isNetworkAvailable(getActivity())) {
            return;
        }
        try {
            Imei = CommonUtility.getUniqueIMEIId(getActivity());
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(getActivity());
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }


        String url = String.format(ServerConfig.getHitDataeUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), currentdate_, sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(getActivity()),
                "EN", "LoginActivity", CommonUtility.getNetworkType(getActivity()), CommonUtility.getNetworkOperator(getActivity()),
                "" + System.currentTimeMillis(), "M", "");
        apiInterface.getCalendereData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCalenderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(GetCalenderData baseEntity) {
                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {

                            Data data = baseEntity.getData();
                            if (data != null) {
                                List<PendingvisitDetail> pendingVisitList = data.getPendingvisitDetails();

                                if (pendingVisitList != null && pendingVisitList.size() > 0) {


                                    setAdapterData(pendingVisitList);


                                }else {
                                    mllCardView.setVisibility(View.GONE);

                                }
                            }


                        } else {
                            // homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                            mllCardView.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void getLoadData(EventDay eventDay, String s) {


        /*get click Data*/

        eventDay.getCalendar().getTime().toString();

        String dateStr = eventDay.getCalendar().getTime().toString();
        SimpleDateFormat curFormater = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            curFormater = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date dateObj = null;
            try {
                dateObj = curFormater.parse(dateStr);
                String newDateStr = curFormater.format(dateObj);
                String day = (String) DateFormat.format("dd", dateObj); // 20
                String monthString = (String) DateFormat.format("MMM", dateObj); // Jun
                String monthNumber = (String) DateFormat.format("MM", dateObj); // 06
                String year = (String) DateFormat.format("yyyy", dateObj);

                selectedDate = year + "-" + monthNumber + "-" + day;

                Log.e("getDate", "getDate" + day);

                SelectionDate = day + "-" + monthString + "-" + year;


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (!CommonUtility.isNetworkAvailable(getActivity())) {
            return;
        }
        try {
            Imei = CommonUtility.getUniqueIMEIId(getActivity());
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(getActivity());
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }


        String url = String.format(ServerConfig.getHitDataeUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), selectedDate, sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(getActivity()),
                "EN", "LoginActivity", CommonUtility.getNetworkType(getActivity()), CommonUtility.getNetworkOperator(getActivity()),
                "" + System.currentTimeMillis(), "M", "");
        apiInterface.getCalendereData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCalenderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(GetCalenderData baseEntity) {
                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {

                            Data data = baseEntity.getData();
                            if (data != null) {
                                List<PendingvisitDetail> pendingVisitList = data.getPendingvisitDetails();

                                if (pendingVisitList != null && pendingVisitList.size() > 0) {


                                    setAdapterData(pendingVisitList);


                                }else {
                                    mllCardView.setVisibility(View.GONE);

                                }
                            }


                        } else {
                            // homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                            mllCardView.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void hitPendingAPI_api() {

        if (!CommonUtility.isNetworkAvailable(getActivity())) {
            return;
        }

        try {
            Imei = CommonUtility.getUniqueIMEIId(getActivity());
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(getActivity());
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }

        String url = String.format(ServerConfig.calenderFragmentURL(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(getActivity()), "EN", "PendingFragment", CommonUtility.getNetworkType(getActivity()), CommonUtility.getNetworkOperator(getActivity()), "" + System.currentTimeMillis(), "M", "");
        apiInterface.getPendingResponceDataa(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PendingCalnderResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PendingCalnderResponse pendingResponce) {
                        mllCardView.setVisibility(View.VISIBLE);

                        if (pendingResponce.getStatus().equalsIgnoreCase("200")) {

                            List<PendingCountDetail> dateResponce = pendingResponce.getPendingVisitData().get(0).getPendingCountDetails();

                            for (int i = 0; i < dateResponce.size(); i++) {

                                String RequestDate = dateResponce.get(i).getRequestDate();
                                String RequestCount = dateResponce.get(i).getPendingCount();

                                String splitdate[] = RequestDate.split("-");
                                String Year = splitdate[0];
                                int YearParse = Integer.parseInt(Year);
                                String month = splitdate[1];
                                int MonthParse = Integer.parseInt(month) - 1;
                                String day = splitdate[2];
                                int dayParse = Integer.parseInt(day);

                                Calendar calendar5 = Calendar.getInstance();
                                calendar5.set(YearParse, MonthParse, dayParse);
                                events.add(new EventDay(calendar5, DrawableUtils.getCircleDrawableWithText(getActivity(), RequestCount)));
                                calendarView.setEvents(events);

                            }

                        } else {

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

    private void toolBarHeader() {
        TextView toolbarTextViews = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
        toolbarTextViews.setText("Pending Visits");
        View view1 = (View) ((HomePageActivity) this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.VISIBLE);
    }


    private void hitPendingAPI(EventDay eventDay) {

        if (!CommonUtility.isNetworkAvailable(getActivity())) {
            return;
        }

        try {
            Imei = CommonUtility.getUniqueIMEIId(getActivity());
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(getActivity());
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }

        String url = String.format(ServerConfig.calenderFragmentURL(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(getActivity()), "EN", "PendingFragment", CommonUtility.getNetworkType(getActivity()), CommonUtility.getNetworkOperator(getActivity()), "" + System.currentTimeMillis(), "M", "");
        apiInterface.getPendingResponceDataa(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PendingCalnderResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PendingCalnderResponse pendingResponce) {
                        mllCardView.setVisibility(View.VISIBLE);

                        if (pendingResponce.getStatus().equalsIgnoreCase("200")) {

                            Log.e("getData", "getdata" + eventDay.getCalendar().getTime().toString());

                            eventDay.getCalendar().getTime().toString();

                            String dateStr = eventDay.getCalendar().getTime().toString();
                            SimpleDateFormat curFormater = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                curFormater = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                                Date dateObj = null;
                                try {
                                    dateObj = curFormater.parse(dateStr);
                                    String newDateStr = curFormater.format(dateObj);
                                    String day = (String) DateFormat.format("dd", dateObj); // 20
                                    String monthString = (String) DateFormat.format("MMM", dateObj); // Jun
                                    String monthNumber = (String) DateFormat.format("MM", dateObj); // 06
                                    String year = (String) DateFormat.format("yyyy", dateObj);

                                    selectedDate = day + "-" + monthNumber + "-" + year;

                                    Log.e("getDate", "getDate" + day);

                                    SelectionDate = day + "-" + monthString + "-" + year;


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            List<PendingCountDetail> dateResponce = pendingResponce.getPendingVisitData().get(0).getPendingCountDetails();

                            for (int i = 0; i < dateResponce.size(); i++) {

                                String RequestDate = dateResponce.get(i).getRequestDate();
                                String RequestCount = dateResponce.get(i).getPendingCount();

                                String splitdate[] = RequestDate.split("-");
                                String Year = splitdate[0];
                                int YearParse = Integer.parseInt(Year);
                                String month = splitdate[1];
                                int MonthParse = Integer.parseInt(month) - 1;
                                String day = splitdate[2];
                                int dayParse = Integer.parseInt(day);

                              /*  if  (RequestDate.equalsIgnoreCase(selectedDate)){

                                  setAdapterData(dateResponce);



                                }else {
                                    Toast.makeText(getActivity(),"No Event Found",Toast.LENGTH_SHORT).show();
                                    mRvCalendar.setAdapter(null);
                                    mllCardView.setVisibility(View.GONE);
                                    CalenderSelectedDate.setText("Event Not Found");


                                }*/


                            }


                            // setData(pendingResponce.getPendingVisitData(),selectedDate,SelectionDate);


                        } else {

                            //Toast.makeText(this, pendingResponce.getMessage(), Toast.LENGTH_LONG).show();
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

    private void setAdapterData(List<PendingvisitDetail> dateResponce) {
        CalenderSelectedDate.setText(SelectionDate);
        mllCardView.setVisibility(View.VISIBLE);
        CalendarDataAdapter calnderAdapter = new CalendarDataAdapter(dateResponce, this);
        mRvCalendar.setAdapter(calnderAdapter);
    }







    @Override
    public Bundle onSaveInstance(Bundle outState) {
        return null;
    }

    @Override
    public void onViewRestore(Bundle savedInstance) {

    }


    @Override
    public void onItemCLicked(View v, int adapterPosition, com.luminous.pdi.home.dto.PendingvisitDetail cardEntity) {

        try {
            Bundle args = new Bundle();
            args.putSerializable("VisitDetail", cardEntity);
            Fragment fragment = VisitDetailFragment.newInstance("", "");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }

    }


    @Override
    public void onItemCLicked(View v, int adapterPosition, String call) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + call));
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            getActivity().startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



