package com.luminous.pdi.completed.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.fragment.AddProductLightDetailFragment;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.adapter.CompletedVisitAdapter;
import com.luminous.pdi.completed.dto.CompleteBaseResponse;
import com.luminous.pdi.completed.dto.CompletedDetail;
import com.luminous.pdi.completed.dto.GetVisitDatum;
import com.luminous.pdi.completed.interfaces.OnRecyclerViewCompletedItemClickListener;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentCompletedBinding;
import com.luminous.pdi.draft.fragments.DraftFragment;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.clicklistener.OnRecyclerViewHomeItemClickListener;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.search.activities.SearchVisitFragment;
import com.luminous.pdi.visit_detail.VisitDetailFragment;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.home.activities.HomePageActivity.binding;


public class CompletedFragment extends Fragment implements OnRecyclerViewCompletedItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private FragmentCompletedBinding completedFragmentBinding;
    String fragmentTag = "",Imei;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public CompletedFragment() {
        // Required empty public constructor
    }


    public static CompletedFragment newInstance(String param1, String param2) {
        CompletedFragment fragment = new CompletedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        completedFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false);
        // Inflate the layout for this fragment

        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        toolBarHeader();
        getCompleteData();
        Menu menu = binding.bottomNavigation.getMenu();
        menu.setGroupEnabled(0, true);
        binding.bottomNavigation.setClickable(true);
        AddProductLightDetailFragment.isHome=true;
        if (AddProductLightDetailFragment.isHome){

            CompleClickOnFragment();


        }
        return completedFragmentBinding.getRoot();

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

    private void getCompleteData() {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {

            Imei = CommonUtility.getUniqueIMEIId(mContext);
            if (Imei == null)
                Imei = CommonUtility.getDeviceId(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(mContext);
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }

        completedFragmentBinding.pbCompletedLoading.setVisibility(View.VISIBLE);
        String url = String.format(ServerConfig.getVisitDetailPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),"complete" ,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getVisitDetailPageData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompleteBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CompleteBaseResponse baseEntity) {
                        completedFragmentBinding.pbCompletedLoading.setVisibility(View.GONE);
                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {

                            List<CompletedDetail> data = baseEntity.getGetVisitData().get(0).getCompletedDetails();
                            if(data!=null) {
                                    setVisitDetailAdapter(data);
                            }



                        } else {
                            completedFragmentBinding.pbCompletedLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        completedFragmentBinding.pbCompletedLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void setVisitDetailAdapter(List<CompletedDetail> data) {

        completedFragmentBinding.completeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        completedFragmentBinding.completeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            completedFragmentBinding.completeRecyclerView.setNestedScrollingEnabled(true);
        CompletedVisitAdapter adapter = new CompletedVisitAdapter(mContext,data,this);
        completedFragmentBinding.completeRecyclerView.setAdapter(adapter);
    }

    private void toolBarHeader() {
            TextView toolbarTextViews  = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
            toolbarTextViews.setText("Completed");
            View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
            view1.setVisibility(View.VISIBLE);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




    @Override
    public void onItemCLicked(View v, int adapterPosition, CompletedDetail cardEntity) {
        try {
            Bundle args = new Bundle();
            args.putSerializable("SearchVisitDetail",  cardEntity);
            Fragment fragment = SearchVisitFragment.newInstance("", "");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "SearchVisitFragment").commit();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }
}
