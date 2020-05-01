package com.luminous.pdi.search.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.luminous.pdi.R;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.dto.CompletedDetail;
import com.luminous.pdi.completed.dto.GetVisitDatum;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentSearchVisitBinding;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.search.adapter.SearchVisitAdapter;
import com.luminous.pdi.search.dto.ProductSearchBaseResponse;
import com.luminous.pdi.search.dto.SearchRecordByEngineerVisit;
import com.luminous.pdi.search.dto.SearchRecordWithstatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchVisitFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private String fragmentTag = "",Imei,  selectClaimStatus;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentSearchVisitBinding bindingSrearch;
    CompletedDetail visitDetailEntity;
    //lin_search_content
    ArrayList MultipleData=new ArrayList<>();


    public SearchVisitFragment() {
        // Required empty public constructor
    }


    public static SearchVisitFragment newInstance(String param1, String param2) {
        SearchVisitFragment fragment = new SearchVisitFragment();
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
        bindingSrearch = DataBindingUtil.inflate(inflater, R.layout.fragment_search_visit, container, false);
        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        Bundle args = getArguments();
        if (args != null) {
             visitDetailEntity = (CompletedDetail) args.getSerializable("SearchVisitDetail");

             Log.e("RequesNO","RequesNO"+visitDetailEntity.getDivisionname());
            setVisitDetailUI(visitDetailEntity);
        } else {
            Log.w("VisitDetail", "Arguments expected, but missing");
        }




        bindingSrearch.etSearchSerialNo.addTextChangedListener(watch);
        bindingSrearch.etSearchMaterialNo.addTextChangedListener(watch2);
        if (visitDetailEntity.getRequestno()!=null)
        bindingSrearch.tvStatusvaluesearchRequest.setText(visitDetailEntity.getRequestno());

        setOnItemClickListener();
        return bindingSrearch.getRoot();
    }

    private void setVisitDetailUI(CompletedDetail visitDetailEntity) {

        if(visitDetailEntity!=null){

            if(visitDetailEntity.getDistributorname()!=null && !visitDetailEntity.getDistributorname().equalsIgnoreCase("")){
                bindingSrearch.idivisitlistNamesearchproduct.setText(""+visitDetailEntity.getDistributorname());
            }

            if(visitDetailEntity.getVisitdate()!=null && !visitDetailEntity.getVisitdate().equalsIgnoreCase("")){
                String Date=visitDetailEntity.getVisitdate();
                String DateSplitOne[]=Date.split(" ");
                bindingSrearch.tvScheduledatevaluesearchproduct.setText(""+DateSplitOne[0]);
            }

            if(visitDetailEntity.getDivisionname()!=null && !visitDetailEntity.getDivisionname().equalsIgnoreCase("")){
                bindingSrearch.tvDivisionvaluesearchproduct.setText(visitDetailEntity.getDivisionname());
            }

            if(visitDetailEntity.getStatus()!=null && !visitDetailEntity.getStatus().equalsIgnoreCase("")){
                bindingSrearch.tvStatusvaluesearchproduct.setText(visitDetailEntity.getStatus());
            }


            if(visitDetailEntity.getAddress()!=null && !visitDetailEntity.getAddress().equalsIgnoreCase("")){
                bindingSrearch.tvSearchAddress.setText(visitDetailEntity.getAddress());
            }
        }

    }


    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {


        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {

            if(a == 1){
                setUIVisibilty(true,false,false, false,false);
            }
        }};

    TextWatcher watch2 = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {


        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {

            if(a == 1){
                setUIVisibilty(false,true,false, false,false);
            }
        }};

    private void setOnItemClickListener() {

        bindingSrearch.btnSearchProduct.setOnClickListener(this);
        bindingSrearch.btnSearch.setOnClickListener(this);
        bindingSrearch.tvSearchReset.setOnClickListener(this);
    }

    //
    private void searchProductDetail() {
        bindingSrearch.searchRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

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
        String url;
        bindingSrearch.pbSearchLoading.setVisibility(View.VISIBLE);
        if(bindingSrearch.rdSearchShowAll.isChecked()){
           //  url = String.format(ServerConfig.getSearchPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),"","","","","",sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
            url = String.format(ServerConfig.getSearchPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),"","","",visitDetailEntity.getRequestno(),sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");

        }else{
             url = String.format(ServerConfig.getSearchPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),bindingSrearch.etSearchSerialNo.getText().toString().trim(),bindingSrearch.etSearchMaterialNo.getText().toString().trim(),selectClaimStatus,visitDetailEntity.getRequestno(),sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        }

       // String url = String.format(ServerConfig.getSearchPageUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),bindingSrearch.etSearchSerialNo.getText().toString().trim(),bindingSrearch.etSearchMaterialNo.getText().toString().trim(),selectClaimStatus,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getSearchPageData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProductSearchBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ProductSearchBaseResponse baseEntity) {
                        bindingSrearch.pbSearchLoading.setVisibility(View.GONE);
                        if (baseEntity.getStatus().equalsIgnoreCase("200")) {

                            List<SearchRecordWithstatus> data = baseEntity.getSearchRecordWithStatus();
                                for (int i=0;i<data.size();i++){
                                    List<SearchRecordByEngineerVisit> engVisitList = data.get(i).getSearchRecordByEngineerVisit();

                                        MultipleData.addAll(engVisitList);
                                        bindingSrearch.searchRecyclerView.setVisibility(View.VISIBLE);
                                        SearchVisitAdapter adapter = new SearchVisitAdapter(mContext,MultipleData);
                                        bindingSrearch.searchRecyclerView.setAdapter(adapter);


                                }







                        } else {
                            bindingSrearch.pbSearchLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, baseEntity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        bindingSrearch.pbSearchLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }

    private void setEngVisitUI(List<SearchRecordByEngineerVisit> engVisitList) {

        bindingSrearch.searchRecyclerView.setVisibility(View.VISIBLE);
        bindingSrearch.searchRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        bindingSrearch.searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            bindingSrearch.searchRecyclerView.setNestedScrollingEnabled(true);
        SearchVisitAdapter adapter = new SearchVisitAdapter(mContext,engVisitList);
        bindingSrearch.searchRecyclerView.setAdapter(adapter);
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
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btnSearchProduct:
                bindingSrearch.linSearchContent.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_search:
                if(isValidate()){

                    if(bindingSrearch.rdAccepted.isChecked()){
                        selectClaimStatus ="Accepted";
                    }else if(bindingSrearch.rdRejected.isChecked()){
                        selectClaimStatus ="Rejected";
                    }


                    searchProductDetail();
                }else{
                    Toast.makeText(mContext,"Plz Select only one field at a time.", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.tvSearchReset:
                 reSetUI();
                break;

        }
    }

    private void reSetUI() {

        bindingSrearch.etSearchSerialNo.setVisibility(View.VISIBLE);
        bindingSrearch.tvSearchbyserialno.setVisibility(View.VISIBLE);
        bindingSrearch.etSearchMaterialNo.setVisibility(View.VISIBLE);
        bindingSrearch.tvSearchbymaterialno.setVisibility(View.VISIBLE);
        bindingSrearch.rdAccepted.setVisibility(View.VISIBLE);
        bindingSrearch.rdAccepted.setChecked(false);
        bindingSrearch.rdRejected.setChecked(false);
        bindingSrearch.rdSearchShowAll.setChecked(false);
        bindingSrearch.rdRejected.setVisibility(View.VISIBLE);
        bindingSrearch.rdSearchShowAll.setVisibility(View.VISIBLE);

        bindingSrearch.tvOrproductlist.setVisibility(View.VISIBLE);
        bindingSrearch.tvOrsecond.setVisibility(View.VISIBLE);
        bindingSrearch.tvOrthird.setVisibility(View.VISIBLE);
        bindingSrearch.searchRecyclerView.setVisibility(View.GONE);
        bindingSrearch.searchRecyclerView.setAdapter(null);

    }

    private boolean isValidate() {
      boolean isChecked = false;
      boolean serial =false;
        boolean material =false;
        boolean accepted =false;
        boolean rejected =false;
        boolean showAll =false;

        boolean serailheader=false;
        boolean materialheader=false;




      if(bindingSrearch.etSearchSerialNo.getText().toString().trim().length()>0){
          isChecked = true;
          setUIVisibilty(true,material,accepted, rejected,showAll);
      }else{
          isChecked = false;
          setUIVisibilty(false,false,false, false,false);
      }
      if(bindingSrearch.etSearchMaterialNo.getText().toString().trim().length()>0){
          isChecked = true;
          setUIVisibilty(serial, true, accepted, rejected, showAll);
      }else{
          isChecked = false;
          setUIVisibilty(false,false,false, false,false);
      }
       if(bindingSrearch.rdAccepted.isChecked()){
           isChecked = true;
           setUIVisibilty(serial, material, true, rejected, showAll);
       } else{
           isChecked = false;
           setUIVisibilty(false,false,false, false,false);
       }
        if(bindingSrearch.rdRejected.isChecked()){
            isChecked = true;
            setUIVisibilty(serial, material, accepted, true, showAll);
        } else{
            isChecked = false;
            setUIVisibilty(false,false,false, false,false);
        }
        if(bindingSrearch.rdSearchShowAll.isChecked()){
            isChecked = true;
            setUIVisibilty(serial, material, accepted, rejected, true);
        } else{
            isChecked = false;
            setUIVisibilty(false,false,false, false,false);
        }
       return isChecked;
    }

    private void setUIVisibilty(boolean serial, boolean material, boolean accepted, boolean rejected, boolean showAll) {
        if(serial){
            bindingSrearch.etSearchSerialNo.setVisibility(View.VISIBLE);
            bindingSrearch.tvSearchbyserialno.setVisibility(View.VISIBLE);
            bindingSrearch.etSearchMaterialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbymaterialno.setVisibility(View.GONE);
            bindingSrearch.rdAccepted.setVisibility(View.GONE);
            bindingSrearch.rdRejected.setVisibility(View.GONE);
            bindingSrearch.rdSearchShowAll.setVisibility(View.GONE);

            bindingSrearch.tvOrproductlist.setVisibility(View.GONE);
            bindingSrearch.tvOrsecond.setVisibility(View.GONE);
            bindingSrearch.tvOrthird.setVisibility(View.GONE);

            searchProductDetail();


        }
        if(material){
            bindingSrearch.etSearchSerialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbyserialno.setVisibility(View.GONE);
            bindingSrearch.etSearchMaterialNo.setVisibility(View.VISIBLE);
            bindingSrearch.tvSearchbymaterialno.setVisibility(View.VISIBLE);
            // bindingSrearch.rdAccepted.isChecked()
            bindingSrearch.rdAccepted.setVisibility(View.GONE);
            bindingSrearch.rdRejected.setVisibility(View.GONE);
            bindingSrearch.rdSearchShowAll.setVisibility(View.GONE);
            bindingSrearch.tvOrproductlist.setVisibility(View.GONE);
            bindingSrearch.tvOrsecond.setVisibility(View.GONE);
            bindingSrearch.tvOrthird.setVisibility(View.GONE);
            bindingSrearch.tvClaimstatus.setVisibility(View.GONE);

            searchProductDetail();

        }

        if(showAll){
            bindingSrearch.etSearchSerialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbyserialno.setVisibility(View.GONE);
            bindingSrearch.etSearchMaterialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbymaterialno.setVisibility(View.GONE);
            // bindingSrearch.rdAccepted.isChecked()
            bindingSrearch.rdAccepted.setVisibility(View.GONE);
            bindingSrearch.rdRejected.setVisibility(View.GONE);
            bindingSrearch.rdSearchShowAll.setVisibility(View.VISIBLE);

            bindingSrearch.tvOrproductlist.setVisibility(View.GONE);
            bindingSrearch.tvOrsecond.setVisibility(View.GONE);
            bindingSrearch.tvOrthird.setVisibility(View.GONE);
            bindingSrearch.tvClaimstatus.setVisibility(View.GONE);
          //  searchProductDetail();

        }


        if(accepted){

            bindingSrearch.etSearchSerialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbyserialno.setVisibility(View.GONE);
            bindingSrearch.etSearchMaterialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbymaterialno.setVisibility(View.GONE);
            // bindingSrearch.rdAccepted.isChecked()
            bindingSrearch.tvClaimstatus.setVisibility(View.VISIBLE);
            bindingSrearch.rdAccepted.setVisibility(View.VISIBLE);
            bindingSrearch.rdRejected.setVisibility(View.VISIBLE);
            bindingSrearch.rdSearchShowAll.setVisibility(View.GONE);

            bindingSrearch.tvOrproductlist.setVisibility(View.GONE);
            bindingSrearch.tvOrsecond.setVisibility(View.GONE);
            bindingSrearch.tvOrthird.setVisibility(View.GONE);


            if(bindingSrearch.rdAccepted.isChecked()){
                selectClaimStatus ="Accepted";
            }else if(bindingSrearch.rdRejected.isChecked()){
                selectClaimStatus ="Rejected";
            }
            searchProductDetail();
        }

        if(rejected){
            bindingSrearch.etSearchSerialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbyserialno.setVisibility(View.GONE);
            bindingSrearch.etSearchMaterialNo.setVisibility(View.GONE);
            bindingSrearch.tvSearchbymaterialno.setVisibility(View.GONE);
            // bindingSrearch.rdAccepted.isChecked()
            bindingSrearch.tvClaimstatus.setVisibility(View.VISIBLE);
            bindingSrearch.rdAccepted.setVisibility(View.VISIBLE);
            bindingSrearch.rdRejected.setVisibility(View.VISIBLE);
            bindingSrearch.rdSearchShowAll.setVisibility(View.GONE);

            bindingSrearch.tvOrproductlist.setVisibility(View.GONE);
            bindingSrearch.tvOrsecond.setVisibility(View.GONE);
            bindingSrearch.tvOrthird.setVisibility(View.GONE);

            if(bindingSrearch.rdAccepted.isChecked()){
                selectClaimStatus ="Accepted";
            }else if(bindingSrearch.rdRejected.isChecked()){
                selectClaimStatus ="Rejected";
            }
            searchProductDetail();
        }
    }
}
