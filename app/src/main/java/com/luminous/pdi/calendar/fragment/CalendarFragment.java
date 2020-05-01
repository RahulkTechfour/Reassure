package com.luminous.pdi.calendar.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luminous.pdi.R;
import com.luminous.pdi.calendar.CustomCalendar;
import com.luminous.pdi.calendar.Singleton;
import com.luminous.pdi.calendar.adapter.CalendarGridviewAdapter;
import com.luminous.pdi.calendar.adapter.CalenderGrid.CalenderGrid;
import com.luminous.pdi.calendar.dao.CalendarDecoratorDao;
import com.luminous.pdi.calendar.dao.CalendarResponse;
import com.luminous.pdi.calendar.dao.Event;
import com.luminous.pdi.calendar.dao.EventData;
import com.luminous.pdi.calendar.responce.PendingVisitDatum;
import com.luminous.pdi.calendar.responce.PenidngResponse;
import com.luminous.pdi.calendar.utils.CalendarUtils;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.dateview.OneDayDecorator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dhaval Soneji on 31/3/16.
 */
public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private GridView mGridview;
    private LinearLayout mLlDayList;
    private LinearLayout mRlHeader;
    private RelativeLayout mprevious;
    private RelativeLayout mnext;
    String Imei;
    String pendiingDataNumS;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GregorianCalendar month;
    private CalenderGrid mCalendarGridviewAdapter;
    private boolean flagMaxMin = false;
    public static String currentDateSelected;
    private Calendar mCalendar;
    private DateFormat mDateFormat;
    private GregorianCalendar mPMonth;
    private int mMonthLength;
    private GregorianCalendar mPMonthMaxSet;
    private ArrayList<CalendarDecoratorDao> mEventList = new ArrayList<>();
    private ViewGroup rootView;
    private static CustomCalendar mCustomCalendar;
    private SharedPrefsManager sharedPrefsManager;
    ArrayList<PendingVisitDatum> pendiingDataNum=new ArrayList<>();
    private SimpleDateFormat dateFormatter, dateFormatter_invoice;
    Date currentDatenew, currentDatenew_invoice;
    Calendar calendar, mVisitDate, calendar_invioice, mVisitDate_invoice;

    /**
     * create CalendarFragment object and call setCalendar().
     *
     * @param calendar
     * @return
     */
    public static CalendarFragment newInstance(CustomCalendar calendar) {
        CalendarFragment fragment = new CalendarFragment();
        fragment.setCalendar(calendar);
        return fragment;
    }

    /**
     * initialize CustomCalendar to access method of it.
     *
     * @param calendar
     */
    private void setCalendar(CustomCalendar calendar) {
        mCustomCalendar = calendar;
    }

    /**
     * initialize Calendar and for the first time load Current Month data.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragement, container, false);
        sharedPrefsManager = new SharedPrefsManager(getActivity());
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

        initCurrentMonthInGridview();


        if (Singleton.getInstance().getIsSwipeViewPager() == 2)
            refreshDays();

        return rootView;
    }

    /**
     * initialize DaysName(Sun,Mon,...) Layout,
     * initialize Current MonthHeader(June 2016) Layout,
     * initialize Gridview(Current month) With click event
     */
    private void initCurrentMonthInGridview() {

        mLlDayList = (LinearLayout) rootView.findViewById(R.id.llDayList);
        mRlHeader = (LinearLayout) rootView.findViewById(R.id.rlMonthTitle);
        mprevious= (RelativeLayout) rootView.findViewById(R.id.previous);
        mnext= (RelativeLayout) rootView.findViewById(R.id.next);

        month = Singleton.getInstance().getMonth();

      //  hitPendingAPI();

       // hitPendingAPI();



        mCalendarGridviewAdapter = new CalenderGrid(getActivity(), mEventList,pendiingDataNum, month);

        mCalendar = month;
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);

        mDateFormat = CalendarUtils.getCalendarDBFormat();

        mGridview = (GridView) rootView.findViewById(R.id.gvCurrentMonthDayList);
        mGridview.setAdapter(mCalendarGridviewAdapter);



        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format(CalendarUtils.getCalendarMonthTitleFormat(), month));
        mprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
                CustomCalendar.mAdapter.notifyDataSetChanged();
            }
        });
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
                CustomCalendar.mAdapter.notifyDataSetChanged();

            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String selectedDate = mEventList.get(position).getDate();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");


                Date date1 = null;
                try {
                    date1 = inputFormat.parse(mEventList.get(position).getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date1);
                try {
                    currentDatenew = outputFormat.parse(outputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar = Calendar.getInstance();
                calendar.setTime(currentDatenew);
                mVisitDate = Calendar.getInstance();
                mVisitDate.setTime(currentDatenew);
                Date maxDate = new Date();
                maxDate.setDate(currentDatenew.getDate());


                Log.e("dateFormatter","dateFormatter"+dateFormatter.format(mVisitDate
                        .getTime()));

                if (mCustomCalendar != null)
                  //  hitPendingAPI();
// change
                if (pendiingDataNumS!=null && pendiingDataNumS.equalsIgnoreCase(dateFormatter.format(mVisitDate.getTime()))){

                    ((CalenderGrid) parent.getAdapter()).setSelected(v, dateFormatter.format(mVisitDate.getTime()));
                    fetchEvents(dateFormatter.format(mVisitDate.getTime()));



                }else {
                    ((CalenderGrid) parent.getAdapter()).setSelected(v, selectedDate);
                    fetchEvents(selectedDate);

                    CustomCalendar.mRvCalendar.setVisibility(View.GONE);

                }


            }
        });
    }

    /**
     * check if date has event or not,
     * then
     *
     * @param date
     */
    public void fetchEvents(String date) {
        boolean flag = false;
        int pos = 0;
        for (int i = 0; i < Singleton.getInstance().getEventManager().size(); i++) {
            if (Singleton.getInstance().getEventManager().get(i).getDate().equalsIgnoreCase(date)) {
                flag = true;
                pos = i;
            }
        }
        ArrayList<EventData> eventDataArrayList = new ArrayList();
        if (flag) {
            if (Singleton.getInstance().getEventManager().get(pos).getEventData() != null) {
                eventDataArrayList = Singleton.getInstance().getEventManager().get(pos).getEventData();
            }
        }






    }

/*
    private void hitPendingAPI() {



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

        String url = String.format(ServerConfig.calenderFragmentURL(),sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+ CommonUtility.getIpAddress(getActivity()),"EN","PendingFragment", CommonUtility.getNetworkType(getActivity()), CommonUtility.getNetworkOperator(getActivity()),""+System.currentTimeMillis(),"M","");
        apiInterface.getPendingResponceDataa(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PenidngResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PenidngResponse pendingResponce) {

                        if (pendingResponce.getStatus().equalsIgnoreCase("200")) {


                            pendiingDataNum.addAll(pendingResponce.getPendingVisitData());
                            mCustomCalendar.setDateSelectionData(pendiingDataNum);
                            for (int i=0;i<=pendiingDataNum.size();i++){
                                 pendiingDataNumS=pendingResponce.getPendingVisitData().get(0).getPendingCountDetails().getRequestDate();
                            }



                             Log.e("getResponce","getResponce"+pendingResponce.getPendingVisitData());
                        } else {

                            Toast.makeText(getActivity(), pendingResponce.getMessage(), Toast.LENGTH_LONG).show();
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
*/

    /**
     * refresh current month
     */
    public void refreshCalendar() {
        TextView title = (TextView) rootView.findViewById(R.id.title);
        refreshDays();
        title.setText(android.text.format.DateFormat.format(CalendarUtils.getCalendarMonthTitleFormat(), month));
    }

    /**
     * refresh current month days
     */
    public void refreshDays() {

        //clear List
        mEventList.clear();
        //create clone
        mPMonth = (GregorianCalendar) mCalendar.clone();

        CalendarGridviewAdapter.firstDay = mCalendar.get(GregorianCalendar.DAY_OF_WEEK);




        int mMaxWeekNumber = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        mMonthLength = mMaxWeekNumber * 7;
        int mMaxP = getmMaxP();
        int mCalMaxP = mMaxP - (CalendarGridviewAdapter.firstDay - 1);

        mPMonthMaxSet = (GregorianCalendar) mPMonth.clone();

        mPMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, mCalMaxP + 1);

        setData(getCalendarData());

    }

    /**
     * @return
     */
    private CalendarResponse getCalendarData() {
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setStartmonth(Singleton.getInstance().getStartMonth());
        calendarResponse.setEndmonth(Singleton.getInstance().getEndMonth());
        calendarResponse.setMonthdata(Singleton.getInstance().getEventManager());
        return calendarResponse;
    }

    /**
     * @param calendarResponse
     */
    private void setData(CalendarResponse calendarResponse) {

        mLlDayList.setVisibility(View.VISIBLE);
        mRlHeader.setVisibility(View.VISIBLE);
        mGridview.setVisibility(View.VISIBLE);

        if (calendarResponse.getMonthdata() != null) {

            ArrayList<Event> monthDataList = calendarResponse.getMonthdata();
            int m = 0;

            for (int n = 0; n < mMonthLength; n++) {
                String mItemValue = mDateFormat.format(mPMonthMaxSet.getTime());
                mPMonthMaxSet.add(GregorianCalendar.DATE, 1);

                if (m < monthDataList.size()) {
                    if (mItemValue.equalsIgnoreCase(monthDataList.get(m).getDate())) {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(
                                monthDataList.get(m).getDate(),

                                Integer.parseInt(monthDataList.get(m).getCount()));
                        mEventList.add(eventDao);
                        m++;
                    } else {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                        mEventList.add(eventDao);
                    }
                } else {
                    CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                    mEventList.add(eventDao);

                }
            }

            mCalendarGridviewAdapter.notifyDataSetChanged();

            if (!flagMaxMin) {
                flagMaxMin = true;
            }
        }
    }

    /**
     * setup next month and check for calendar range
     */

    public void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
            Singleton.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
            Singleton.getInstance().setMonth(month);
        }
    }

    /**
     * setup previous month and check for calendar range
     */
    public void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            Singleton.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
            Singleton.getInstance().setMonth(month);
        }
    }

    /**
     * @return
     */
    private int getmMaxP() {
        int maxP;
        if (mCalendar.get(GregorianCalendar.MONTH) == mCalendar
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mPMonth.set((mCalendar.get(GregorianCalendar.YEAR) - 1),
                    mCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mPMonth.set(GregorianCalendar.MONTH,
                    mCalendar.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = mPMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}
