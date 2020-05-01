package com.luminous.pdi.home.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luminous.pdi.R;
import com.luminous.pdi.RemarkFragment.RemarkFragment;
import com.luminous.pdi.addproductLight.fragment.AddProductLightDetailFragment;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentHomePageBinding;
import com.luminous.pdi.draft.fragments.DraftFragment;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.adapter.UpComingVisitAdapter;
import com.luminous.pdi.home.clicklistener.OnRecyclerOnItemClick;
import com.luminous.pdi.home.clicklistener.OnRecyclerViewHomeItemClickListener;
import com.luminous.pdi.home.dto.Data;
import com.luminous.pdi.home.dto.HomePageBaseResponse;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.dto.UpComingVisitDto;
import com.luminous.pdi.home.dto.Visitcount;
import com.luminous.pdi.home.fragments.CreateListDTO.CreateListDashBoard;
import com.luminous.pdi.home.fragments.CreateListDTO.ResponseDatum;
import com.luminous.pdi.visit_detail.VisitDetailFragment;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.luminous.pdi.home.activities.HomePageActivity.binding;


public class HomePageFragment extends Fragment implements OnRecyclerViewHomeItemClickListener , OnRecyclerOnItemClick {
    private static final String TAG = "HomePageFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;

    private String mParam2;
    private FragmentHomePageBinding homeFragmentBinding;
    private Context mContext;

    String fragmentTag = "",Imei;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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

         homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false);
        // Inflate the layout for this fragment
           homeFragmentBinding.linviewCompleted.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Fragment fragment=new RemarkFragment();
                   FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.content_frame,fragment);
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
               }
           });

        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        getLoadData();

        setHeaderTitle();
        Menu menu = binding.bottomNavigation.getMenu();

        menu.setGroupEnabled(0, true);
        binding.bottomNavigation.setClickable(true);
        AddProductLightDetailFragment.isHome=true;
        if (AddProductLightDetailFragment.isHome){

            CompleClickOnFragment();


        }


        String dashtoolbattext="Reassure";

        TextView toolbarTextViews  = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
        toolbarTextViews.setText(""+dashtoolbattext);

        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.VISIBLE);

        Click();


        return homeFragmentBinding.getRoot();

    }

    public void CompleClickOnFragment() {
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

    private void Click() {
        homeFragmentBinding.llPendingCLick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment;

                try {
                    fragment = new AttendenceFragment();
                    FragmentManager fragmentManager;
                    fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }catch(Exception ex){
                    ex.getStackTrace();
                }

            }
        });

        homeFragmentBinding.llCompletedCLick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;

                try {
                    fragment = new CompletedFragment();
                    FragmentManager fragmentManager;
                    fragmentManager = getFragmentManager();
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

    private void setHeaderTitle() {


    }

    private void getLoadData() {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            homeFragmentBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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

        String url = String.format(ServerConfig.getHomePageUrl());
        apiInterface.getHomePageData(sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CreateListDashBoard>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CreateListDashBoard baseEntity) {
                        homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (baseEntity.getResponseCode()==200) {

                            List<ResponseDatum> pendingVisitList = baseEntity.getResponseData();
                            if(pendingVisitList!=null) {
                               // List<ResponseDatum> pendingVisitList = data;
                                //List<Visitcount> visitCountList = data.getVisitcount();
                                /*if(visitCountList!=null && visitCountList.size()>0){
                                    setVisitCountUI(visitCountList);
                                }*/
                                if(pendingVisitList!=null && pendingVisitList.size()>0){
                                   setUpCommingAdapter(pendingVisitList);
                                }
                            }



                        } else {
                            homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, baseEntity.getResponseMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeFragmentBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void setVisitCountUI(List<Visitcount> pendingVisitList) {
        homeFragmentBinding.txtHomeUserName.setText(""+sharedPrefsManager.getString(SharedPreferenceKeys.FIRST_NAME));

        if(pendingVisitList.get(0).getPending()!=null) {
            homeFragmentBinding.txtHomePendingCount.setText("" +pendingVisitList.get(0).getPending());
        }else{
            homeFragmentBinding.txtHomePendingCount.setText("0");
        }
        if(pendingVisitList.get(0).getCompleted()!=null) {
            homeFragmentBinding.txtHomeCompletedCount.setText("" +pendingVisitList.get(0).getCompleted());
        }else{
            homeFragmentBinding.txtHomeCompletedCount.setText("0");
        }

    }

    private void setUpCommingAdapter(List<ResponseDatum> pendingVisitList) {
        homeFragmentBinding.txtHomeUserName.setText(""+sharedPrefsManager.getString(SharedPreferenceKeys.FIRST_NAME));

        homeFragmentBinding.dashRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        homeFragmentBinding.dashRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            homeFragmentBinding.dashRecyclerView.setNestedScrollingEnabled(true);
        UpComingVisitAdapter adapter = new UpComingVisitAdapter(mContext,pendingVisitList,this, this);
        homeFragmentBinding.dashRecyclerView.setAdapter(adapter);
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
    public void onItemCLicked(View v, int adapterPosition, ResponseDatum cardEntity) {
        try {
            Bundle args = new Bundle();
            args.putSerializable("VisitDetail",  cardEntity);
            Fragment fragment = VisitDetailFragment.newInstance("","");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onItemCLicked(View v, int adapterPosition, String call) {

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +call));
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mContext.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
