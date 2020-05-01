package com.luminous.pdi.draft.fragments;

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
import com.luminous.pdi.addproductLight.fragment.DraftLightDetailFragment;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.dto.CompleteBaseResponse;
import com.luminous.pdi.completed.dto.GetVisitDatum;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentDraftBinding;
import com.luminous.pdi.draft.adapters.DraftVisitAdapter;
import com.luminous.pdi.draft.dto.DraftBaseResponse;
import com.luminous.pdi.draft.dto.DraftVisitDetailDto;
import com.luminous.pdi.draft.dto.DraftVisitDto;
import com.luminous.pdi.draft.dto.VisitBaseResponse;
import com.luminous.pdi.draft.interfaces.onRecyclerViewDraftItemClickListener;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.profile.fragments.MyProfileFragment;
import com.luminous.pdi.visit_detail.AddProductVisitDetailFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.luminous.pdi.home.activities.HomePageActivity.binding;


public class DraftFragment extends Fragment implements OnRecyclerViewItemClickListener, onRecyclerViewDraftItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mContext;
    private FragmentDraftBinding draftBinding;
    String fragmentTag = "",Imei;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DraftFragment() {
        // Required empty public constructor
    }


    public static DraftFragment newInstance(String param1, String param2) {
        DraftFragment fragment = new DraftFragment();
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
        // Inflate the layout for this fragment
        draftBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_draft, container, false);
        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        getDraftData();

        TextView toolbarTextView  = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
        toolbarTextView.setText("Draft");

        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.VISIBLE);

        Menu menu = binding.bottomNavigation.getMenu();
        menu.setGroupEnabled(0, true);
        binding.bottomNavigation.setClickable(true);
        AddProductLightDetailFragment.isHome=true;
        if (AddProductLightDetailFragment.isHome){

            CompleClickOnFragment();


        }

        return draftBinding.getRoot();
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


    private void getDraftData() {

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

        draftBinding.pbDraftLoading.setVisibility(View.VISIBLE);
        String url = String.format(ServerConfig.getDraftVisitDetailPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),"Draft" ,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getDraftVisitDetailPageData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DraftBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DraftBaseResponse baseEntity) {
                        draftBinding.pbDraftLoading.setVisibility(View.GONE);
                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {

                            List<DraftVisitDetailDto> data = baseEntity.getGetVisitData();
                            if(data!=null && data.size()>0) {
                                setDraftVisitDetailAdapter(data);
                            }

                        } else {
                            draftBinding.pbDraftLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        draftBinding.pbDraftLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void setDraftVisitDetailAdapter(List<DraftVisitDetailDto> visitList) {

        if(visitList!=null && visitList.size()>0){
            List<DraftVisitDto> draftDetail = visitList.get(0).getDraft_details();
            if(draftDetail!=null && draftDetail.size()>0) {
                draftBinding.draftRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                draftBinding.draftRecyclerView.setItemAnimator(new DefaultItemAnimator());
                if (Build.VERSION.SDK_INT >= 21)
                    draftBinding.draftRecyclerView.setNestedScrollingEnabled(true);
                DraftVisitAdapter adapter = new DraftVisitAdapter(mContext, draftDetail, this, this);
                draftBinding.draftRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


        }
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
    public void onItemCLicked(View view, int position) {

    }

    @Override
    public void onItemCLicked(View view, int position, String distributorId) {

        try {
            removeDraftCard(distributorId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeDraftCard(String distributorId) {

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

        draftBinding.pbDraftLoading.setVisibility(View.VISIBLE);
        String url = String.format(ServerConfig.getDraftDeleteUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),distributorId ,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getDrafDeleteData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VisitBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(VisitBaseResponse baseEntity) {

                        if (baseEntity.getStatus()!=null  && baseEntity.getStatus().equalsIgnoreCase("200")) {

                            Toast.makeText(mContext, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                            DraftFragment fragment = DraftFragment.newInstance("", "");
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                            getFragmentManager().beginTransaction() .detach(fragment) .attach(fragment) .commit();

                         //   getDraftData();

                        } else {

                            Toast.makeText(mContext, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemDraftCardCLicked(View view, int position, DraftVisitDto draftDto) {
        if(draftDto.getDivisionname().equalsIgnoreCase("FAN")){

            try {
            String requestNo = draftDto.getRequestno();
            getSavedFanDetailApi(requestNo,draftDto);
            }catch(Exception ex){
                ex.getStackTrace();
            }
        }else{

            try {
                String requestNo = draftDto.getRequestno();
                getSavedLightDetailApi(requestNo,draftDto);
            }catch(Exception ex){
                ex.getStackTrace();
            }


        }


    }

    private void getSavedLightDetailApi(String requestNo, DraftVisitDto draftDto) {
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

        draftBinding.pbDraftLoading.setVisibility(View.VISIBLE);
        String url = String.format(ServerConfig.getSavedDraftLightDetailUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),requestNo ,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getDraftLightDetailData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        String data = null;
                        try {
                            data = response.string();
                            if(data!=null){
                                JSONObject jsonObject = new JSONObject(data);
                                String apiStaus = jsonObject.getString("Status");
                                if(apiStaus.equalsIgnoreCase("200")){
                                    JSONArray jsnArray = jsonObject.getJSONArray("SaveLightDetails");
                                    if(jsnArray!=null && jsnArray.length()>0){

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("VisitDetail",  draftDto);
                                        bundle.putString("SaveLightDraftDetailData", data);
                                        DraftLightDetailFragment fragment = DraftLightDetailFragment.newInstance("", "");
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

                                    }else{

                                        Toast.makeText(mContext, "Save data not found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }
    private void getSavedFanDetailApi(String requestNo, DraftVisitDto draftDto) {

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

        draftBinding.pbDraftLoading.setVisibility(View.VISIBLE);
        String url = String.format(ServerConfig.getSavedDraftFanDetailUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),requestNo ,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getDraftFanDetailData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        String data = null;
                        try {
                            data = response.string();
                            if(data!=null){
                                JSONObject jsonObject = new JSONObject(data);
                                String apiStaus = jsonObject.getString("Status");
                                if(apiStaus.equalsIgnoreCase("200")){
                                    JSONArray jsnArray = jsonObject.getJSONArray("SaveFanDetails");
                                    if(jsnArray!=null && jsnArray.length()>0){

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("VisitDetail",  draftDto);
                                        bundle.putString("SaveFanDraftDetailData", data);
                                        DraftFanDetailFragment fragment = DraftFanDetailFragment.newInstance("", "");
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

                                    }else{

                                        Toast.makeText(mContext, "Save data not found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }
}
