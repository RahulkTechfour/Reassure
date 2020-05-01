package com.luminous.pdi.draft.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.luminous.pdi.R;
import com.luminous.pdi.addproduct_fan.dto.ApiBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.FaultBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.FaultList;
import com.luminous.pdi.addproduct_fan.dto.InvoiceNoDto;
import com.luminous.pdi.addproduct_fan.dto.MaterialBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.MaterialListDto;
import com.luminous.pdi.addproduct_fan.dto.SaveStatusBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.StatusListDto;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentDraftFanDetailBinding;
import com.luminous.pdi.draft.dto.DraftVisitDto;
import com.luminous.pdi.draft.dto.WarrantyBaseResponse;
import com.luminous.pdi.draft.dto.WarrantyDto;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.fragments.HomePageFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.core.CommonUtility.getCurrentDate;


public class DraftFanDetailFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    //
    String fragmentTag = "",Imei;
    private FragmentDraftFanDetailBinding draftFanBinding;
    private String secretCode = "", barCode = "";
    int quantyTotal =0, startQnty = 0;
    List<InvoiceNoDto> nextInvoiceList=new ArrayList<>();
    public static final int REQUEST_CAMERA_PERMISSION = 225;
    private DraftVisitDto visitDetailEntity;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String[] fName ;
    private String[] fId ;
    List<FaultList> faultDataList;
    String selectFaultName ,selectFaultId;
    String selectedMaterialName , selectedMaterialId, selectedSerialNo;
    String[] serialNo ;
    String[] invoiveNoArr;
    String productStatus;
    String claimStatus;
    String finalJsonData;
    String clickFlag="";
    ArrayAdapter<CharSequence> faultItemSpinnerAdapter;
    ArrayAdapter<CharSequence> serialNoItemSpinnerAdapter;
    String prevFaultId, prevProductStatus, prevClaimStatus, prevDistributorAddress, prevDistributorName, prevDivisionID, prevDivisionName, prevInvoiceID;
    //creating json object to put json array
    JSONObject MainObject = new JSONObject();
    //creating json array
    JSONArray MainArray; //= new JSONArray();

    JsonObject gsonObject = new JsonObject();
    int jsnCount=0;
    String fanWarrantyStatus,fanWarrantyInputSerialNo;
    int status_pending_id, status_confirm_id, status_reject_id, status_completed_id,status_short_closed_id,status_draft_id, status_approval_id, status_delete_id;
    List<StatusListDto> statusList;
    private String tag="TAG_FRAGMENT";
    String draftFanPurchageDate;
    //
    int distributerId;

    public DraftFanDetailFragment() {
        // Required empty public constructor
    }

    public static DraftFanDetailFragment newInstance(String param1, String param2) {
        DraftFanDetailFragment fragment = new DraftFanDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        draftFanBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_draft_fan_detail, container, false);
        View viewDraft=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        viewDraft.setVisibility(View.GONE);
        Bundle args = getArguments();
        if (args != null) {
            try {
                visitDetailEntity = (DraftVisitDto) args.getSerializable("VisitDetail");
                distributerId= visitDetailEntity.getDistributorid();
                String data =  args.getString("SaveFanDraftDetailData");
                JSONObject   jsonObject = new JSONObject(data);
                MainArray = jsonObject.getJSONArray("SaveFanDetails");
                JSONObject jsnObj = MainArray.getJSONObject(0);
             //   quantyTotal = jsnObj.getInt("TotalQuantity");
                quantyTotal=5;
                startQnty = (MainArray.length()-1);
                MainObject.put("SaveFanDetails", MainArray);
                Log.e("DraftDataJson", ""+jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.w("VisitDetail", "Arguments expected, but missing");
        }


        TextView toolbarTextView  = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
        toolbarTextView.setText("Add Product");
        // Inflate the layout for this fragment
        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        setHeaderTitle();

        if(startQnty==0){
            draftFanBinding.btnDraftFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
            //  addProductFanDetailBinding.btnFanPrev.setFocusable(false);
            draftFanBinding.fanDraftDeleteImage.setVisibility(View.GONE);
        }
        getFaultList();
        setFanData();
        getStatusList();
        setOnItemClickListener();

        // Change by Rahul
        int showQnty1 = startQnty;
        showQnty1 = showQnty1+1;
        draftFanBinding.etDraftFanQnty.setText(String.valueOf(showQnty1)+"/"+String.valueOf(quantyTotal));

      //  draftFanBinding.etDraftFanQnty.setText(String.valueOf(startQnty)+"/"+String.valueOf(quantyTotal));
        // fault dropdown
        draftFanBinding.spDraftFanFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (faultDataList != null) {
                        if (position > 0) {
                            selectFaultName = fName[position];
                            selectFaultId = fId[position];
                        } else {
                            selectFaultId = fId[position];
                        }
                    }
                }catch (Exception ex){
                    ex.getMessage();
                }


            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        draftFanBinding.spDraftFanInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                try {
                    if (serialNo != null) {


                        if (position > 0) {
                            selectedSerialNo = serialNo[position];


                        } else {
                            selectedSerialNo = serialNo[position];
                            Log.e("selectedSerialNo+++","SerialNo"+selectedSerialNo);

                        }
                    }
                }catch (Exception ex){
                    ex.getMessage();
                }


            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//
        draftFanBinding.etDraftFanSerialNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   // getMaterialList();
                    String currentCode = v.getText().toString().trim();
                    if(getUniqueSerialNo(currentCode)) {
                        getMaterialList("");
                    }else{
                        Toast.makeText(getActivity(),"You can't scan same serial no", Toast.LENGTH_SHORT).show();
                    }


                    handled = true;
                }
                return handled;
            }
        });

        //
        draftFanBinding.etDraftFanWarrantyStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {


                fanWarrantyStatus = ""+s.toString();
                Log.e("TextWatcherTest", "" +fanWarrantyStatus);
                //draftFanBinding.etFanWarrantyStatus.setText();
            }
        });

        draftFanBinding.etDraftFanSerialNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" +s.toString());
                fanWarrantyInputSerialNo = s.toString();
            }
        });

        // purchage of date
        draftFanBinding.etDraftFanPurchaseDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable str) {


                draftFanPurchageDate = ""+str.toString();
                Log.e("TextWatcherTest", "" +fanWarrantyStatus);
              //  getproductWarrantyStatus();
            }
        });


        draftFanBinding.fanDraftBackimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("VisitDetail",  visitDetailEntity);


                Fragment fragment = DraftFragment.newInstance("", "");
              //  fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();

            }
        });


        draftFanBinding.fanDraftDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_dialog();
            }
        });


        //
        //
      /*  View view= draftFanBinding.getRoot();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction()== KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!");
                    Fragment fragment = new AddProductVisitDetailFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    // fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });*/

        showlastData();


        return draftFanBinding.getRoot();
    }

    //
    private void getproductWarrantyStatus() {

        String fanSerialNo = fanWarrantyInputSerialNo; //draftFanBinding.etFanSerialNo.getText().toString().trim();
        //  fanSerialNo = "CCIHD9A9030433";
        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            //  draftFanBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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
  //
        String url = String.format(ServerConfig.getproductWarrantyStatusUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),draftFanBinding.etDraftFanSerialNo.getText().toString().trim(),draftFanBinding.etDraftFanInspectionDate.getText().toString(),draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim(),sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getProductWarrantyStatusData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WarrantyBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(WarrantyBaseResponse entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus()!=null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                List<WarrantyDto> warrantyStatusList = entity.getDataResult();
                                if(warrantyStatusList!=null && warrantyStatusList.size()>0){
                                    fanWarrantyStatus = warrantyStatusList.get(0).getWarranty();
                                    if(fanWarrantyStatus!=null && !fanWarrantyStatus.isEmpty())
                                        draftFanBinding.etDraftFanWarrantyStatus.setText(fanWarrantyStatus);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });

    }


    private void getMaterialList(String mInvoiceID) {

        String draftSerialNo = draftFanBinding.etDraftFanSerialNo.getText().toString().trim();
        if(!draftSerialNo.isEmpty() && draftSerialNo!=null){
            fanWarrantyInputSerialNo = draftSerialNo;
        }

        String fanSerialNo = fanWarrantyInputSerialNo; //draftFanBinding.etFanSerialNo.getText().toString().trim();
        //  fanSerialNo = "CCIHD9A9030433";
        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            //  draftFanBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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
//
        String url = String.format(ServerConfig.getMaterialUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),fanSerialNo,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getMaterialData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MaterialBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(MaterialBaseResponse entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus()!=null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                List<MaterialListDto> materialList = entity.getProduct_details();
                                if(materialList!=null && materialList.size()>0){
                                    if(materialList.get(0).getProductdescription()!=null && materialList.get(0).getProductdescription().equalsIgnoreCase(""))
                                        selectedMaterialName = materialList.get(0).getProductdescription();
                                    selectedMaterialId = materialList.get(0).getPRODUCT_ID();
                                    draftFanBinding.etDraftMaterialName.setText(""+materialList.get(0).getProductdescription());
                                    draftFanBinding.etDraftMaterialName.setTag(""+selectedMaterialId);
                                    List<InvoiceNoDto> invoiceList =  materialList.get(0).getInvoiceNo();
                                    nextInvoiceList=  materialList.get(0).getInvoiceNo();
                                    serialNo = new String[invoiceList.size()+1];
                                    invoiveNoArr = new String[invoiceList.size()+1];
                                    serialNo[0] = "Select Invoice No.";
                                    invoiveNoArr[0]= "Select Invoice No.";
                                    if(invoiceList!=null && invoiceList.size()>0){
                                        for(int kCount=0; kCount< invoiceList.size(); kCount++){
                                            serialNo[kCount+1] = invoiceList.get(kCount).getInvoiceNo();
                                            invoiveNoArr[kCount+1]= invoiceList.get(kCount).getInvoiceNo();
                                        }
                                    }
                                    if(serialNo!=null && serialNo.length>0)
                                        setSerialNoListUI(mInvoiceID);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }

    private void setSerialNoListUI(String mInvoiceID) {
        serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, serialNo);
        serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        draftFanBinding.spDraftFanInvoice.setAdapter(serialNoItemSpinnerAdapter);

        try {
            if (mInvoiceID != null && !mInvoiceID.isEmpty()) {
                int invoicePosition =0;
                for(int j=0; j< invoiveNoArr.length; j++){
                    String invoiceNo = invoiveNoArr[j];
                    if(invoiceNo.equalsIgnoreCase(mInvoiceID)){
                        invoicePosition = j;
                    }
                }
                //     int spinnerSerialPosition = serialNoItemSpinnerAdapter.getPosition(prevInvoiceID);
                draftFanBinding.spDraftFanInvoice.setSelection(invoicePosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeaderTitle() {
        // Toolbar mToolbar = ( getActivity()).getSupportActionBar();

    }

    private void getFaultList() {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            //  draftFanBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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

        String url = String.format(ServerConfig.getFaultUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),visitDetailEntity.getDivisionid(),distributerId,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getFaultData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaultBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaultBaseResponse entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus()!=null)
                        if (entity.getStatus().equalsIgnoreCase("200")) {
                            faultDataList=  entity.getData().get(0).getFaultList();
                            fName = new String[faultDataList.size()+1];
                            fId = new String[faultDataList.size()+1];
                            fName[0] = "Select Fault";
                            fId[0] = "0";

                            if(faultDataList!=null && faultDataList.size()>0) {

                                for(int iCount = 0; iCount<faultDataList.size(); iCount++){
                                    fName[iCount+1] = faultDataList.get(iCount).getFaultName();
                                    fId[iCount+1] = faultDataList.get(iCount).getId();
                                }
                                setFaultSearchUI(faultDataList);
                            }

                            showlastData();

                        } else {
                            //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }

    private void setFaultSearchUI(List<FaultList> faultDataList) {

        faultItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, fName);
        faultItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        draftFanBinding.spDraftFanFault.setAdapter(faultItemSpinnerAdapter);

    }



    private void setOnItemClickListener() {
        draftFanBinding.ivDraftScan.setOnClickListener(this);
        draftFanBinding.tvDraftFanManualLink.setOnClickListener(this);
        draftFanBinding.btnDraftFanPrev.setOnClickListener(this);
        draftFanBinding.btnDraftFanSubmit.setOnClickListener(this);
        draftFanBinding.btnDraftFanNext.setOnClickListener(this);
        draftFanBinding.etDraftFanPurchaseDate.setOnClickListener(this);
        draftFanBinding.ivDraftPDate.setOnClickListener(this);
    }

    private void setFanData() {
        draftFanBinding.etDraftFanInspectionDate.setText(getCurrentDate());
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.ivDraftScan:
                openScanner();
                break;

            case R.id.tv_DraftFanManualLink:
                draftFanBinding.etDraftFanSerialNo.setEnabled(true);
                draftFanBinding.etDraftFanSerialNo.setFocusable(true);
                draftFanBinding.etDraftFanSerialNo.setClickable(true);
                draftFanBinding.etDraftFanSerialNo.setText("");
                break;

            case R.id.ivDraftPDate:
                selectPurchageDate();
                break;

            case R.id.btn_DraftFanPrev:

                draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
                draftFanBinding.btnDraftFanNext.setEnabled(true);
                draftFanBinding.btnDraftFanNext.setFocusable(true);
                draftFanBinding.btnDraftFanNext.setClickable(true);


                if(startQnty==0){
                    draftFanBinding.btnDraftFanNext.setBackgroundColor(getResources().getColor(R.color.next));
                    draftFanBinding.btnDraftFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));

                }else{
                  //  showPreviousData();
                    try {
                        JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                        if(jsonArray.length()==startQnty){
                            //  Toast.makeText(mContext, "Please press NEXT to save this record first",Toast.LENGTH_LONG).show();
                            //   bindDataIntoJson();
                            if(isValidate()) {
                                preservedData_dialog();
                            }else{
                                showPreviousData();
                            }
                            //    addProductFanDetailBinding.btnFanNext.setVisibility(View.GONE);

                        }else{
                            showPreviousData();
                            //    addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.btn_DraftFanSubmit:
                clickFlag = "Sub";
                if(isValidate()){
                    // addProductFanDetailBinding.radFanSold.isChecked() || addProductFanDetailBinding.radFanUnSold.isChecked()
                    bindDataIntoJson(clickFlag);
                    int submitStartQun=startQnty;
                    submitStartQun=submitStartQun+1;
                    submit_dialog();

                /*    if (quantyTotal==submitStartQun){

                      //  submitBindJson();
                    }else {
                        if (quantyTotal==startQnty){
                            submitBindJson();
                        }
                        submit_dialog();

                    }*/

                }
                break;

            case R.id.btn_DraftFanNext:
                if(isValidate()){
                    draftFanBinding.fanDraftDeleteImage.setVisibility(View.GONE);
                    if(startQnty<quantyTotal){

                        startQnty = startQnty+1;
                        // Change by Rahul
                        int showQnty3 = startQnty;
                       // showQnty3 = showQnty3+1;


                        if(startQnty==quantyTotal){
                            showQnty3 = showQnty3;
                            draftFanBinding.btnDraftFanNext.setEnabled(false);
                            draftFanBinding.btnDraftFanNext.setFocusable(false);
                            draftFanBinding.btnDraftFanNext.setClickable(false);
                        }else {
                            showQnty3 = showQnty3+1;
                            if (quantyTotal==showQnty3) {
                                draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
                                draftFanBinding.btnDraftFanNext.setEnabled(false);
                                draftFanBinding.btnDraftFanNext.setFocusable(false);
                                draftFanBinding.btnDraftFanNext.setClickable(false);
                            }else{
                                draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
                                draftFanBinding.btnDraftFanNext.setEnabled(true);
                                draftFanBinding.btnDraftFanNext.setFocusable(true);
                                draftFanBinding.btnDraftFanNext.setClickable(true);
                            }
                        }
                        draftFanBinding.etDraftFanQnty.setText(String.valueOf(showQnty3)+"/"+String.valueOf(quantyTotal));
                        bindDataIntoJson(clickFlag);

                     //   draftFanBinding.etDraftFanQnty.setText(String.valueOf(startQnty)+"/"+String.valueOf(quantyTotal));
                        draftFanBinding.btnDraftFanPrev.setBackgroundColor(getResources().getColor((R.color.color_yellow)));
                        draftFanBinding.btnDraftFanPrev.setFocusable(true);

                        if (startQnty==quantyTotal){
                            draftFanBinding.btnDraftFanNext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                        }else {
                            draftFanBinding.btnDraftFanNext.setBackgroundColor(getResources().getColor(R.color.next));
                        }
                        int jsnCount = MainArray.length();
                        if(jsnCount==startQnty) {
                            resetFanUI();
                            if (compareJsonLenth()){
                                resetInvoiceFaultFanUI();
                            }
                        }else if(jsnCount>startQnty){

                        }
                    }
                }
                break;
        }
    }

    private void submitBindJson() {
        if (draftFanBinding.radDraftFanSold.isChecked()) {
            productStatus = "Sold";
        } else if (draftFanBinding.radDraftFanUnSold.isChecked()) {
            productStatus = "Unsold";
        }
        if (draftFanBinding.radioDraftFanClaimRejected.isChecked()) {
            claimStatus = "Rejected";
        } else if (draftFanBinding.radioDraftFanClaimAccepted.isChecked()) {
            claimStatus = "Accepted";
        }
        JSONObject person = new JSONObject();
        try {
            person.put("TotalQuantity", "" + quantyTotal);
            person.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
            //MaterialID
            person.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
            person.put("FaultId", "" + selectFaultId);
            person.put("ProductStatus", "" + productStatus);
            person.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
            //  person.put("WarrantyStatus", "" + draftFanBinding.etDraftFanWarrantyStatus.getText().toString());
            person.put("WarrantyStatus", "" +fanWarrantyStatus);
            person.put("ClaimStatus", "" + claimStatus);
            person.put("SerialNo", "" + draftFanBinding.etDraftFanSerialNo.getText().toString().trim());
            person.put("DistributorAddress", "" + visitDetailEntity.getAddress());
            person.put("DistributorName", "" + visitDetailEntity.getDistributorname());
            person.put("DivisionID", "" + visitDetailEntity.getDivisionid());
            person.put("DivisionName", "" + visitDetailEntity.getDivisionname());
            person.put("InvoiceID", "" + selectedSerialNo);
            MainArray.put(person);
            MainObject.put("SaveFanDetails", MainArray);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());
            finalJsonData = MainObject.toString();
            Toast.makeText(mContext,"Data saved",Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        submit_dialog();
    }

    private void showPreviousData() {
        startQnty = startQnty-1;
        if (startQnty==0){
            draftFanBinding.btnDraftFanNext.setBackgroundColor(getResources().getColor(R.color.next));
            draftFanBinding.btnDraftFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
        }else {
            draftFanBinding.btnDraftFanNext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
            draftFanBinding.btnDraftFanPrev.setBackgroundColor(getResources().getColor(R.color.previous));
        }

        // Change by Rahul
        int showQnty2 = startQnty;
        showQnty2 = showQnty2+1;
        draftFanBinding.etDraftFanQnty.setText(String.valueOf(showQnty2)+"/"+String.valueOf(quantyTotal));


        // draftFanBinding.etDraftFanQnty.setText(String.valueOf(startQnty)+"/"+String.valueOf(quantyTotal));
        resetFanUI();
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");

            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            String totalQnty = job.getString("TotalQuantity");
            String visitDate = job.getString("VisitDate");
            draftFanBinding.etDraftFanInspectionDate.setText(visitDate);
            String inspDate = job.getString("InspectionDate");
            draftFanBinding.etDraftFanInspectionDate.setText(""+inspDate);


            prevFaultId = job.getString("FaultId");
            if (prevFaultId != null) {
                int spinnerfaultPosition =0;
                for(int kCount =0; kCount<fId.length; kCount++){
                    if(prevFaultId.equalsIgnoreCase(fId[kCount]) ){
                        spinnerfaultPosition = kCount;

                    }
                }
                int spinnerSerialPosition =  faultItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                draftFanBinding.spDraftFanFault.setSelection(spinnerSerialPosition);
            }
            prevProductStatus = job.getString("ProductStatus");
            if(prevProductStatus.equalsIgnoreCase("Sold")){
                draftFanBinding.radDraftFanSold.setChecked(true);
                draftFanBinding.radDraftFanUnSold.setChecked(false);
            }else if(prevProductStatus.equalsIgnoreCase("Unsold")){
                draftFanBinding.radDraftFanSold.setChecked(false);
                draftFanBinding.radDraftFanUnSold.setChecked(true);
            }

            String DatePurchage = job.getString("DateOfPurchase");
            draftFanBinding.etDraftFanPurchaseDate.setText(""+DatePurchage);
            String warrantyStatus = job.getString("WarrantyStatus");
            draftFanBinding.etDraftFanWarrantyStatus.setText(""+warrantyStatus);
          /*  if (warrantyStatus.equalsIgnoreCase("IN")){
                draftFanBinding.etDraftFanWarrantyStatus.setText("In Warranty");

            }else if (warrantyStatus.equalsIgnoreCase("OUT")){
                draftFanBinding.etDraftFanWarrantyStatus.setText("Out of Warranty");

            }*/

            prevClaimStatus = job.getString("ClaimStatus");
            if(prevClaimStatus.equalsIgnoreCase("Accepted")){
                draftFanBinding.radioDraftFanClaimAccepted.setChecked(true);
                draftFanBinding.radioDraftFanClaimRejected.setChecked(false);
            }else if(prevClaimStatus.equalsIgnoreCase("Rejected")){
                draftFanBinding.radioDraftFanClaimAccepted.setChecked(false);
                draftFanBinding.radioDraftFanClaimRejected.setChecked(true);
            }

            String serialNo  = job.getString("SerialNo");
            draftFanBinding.etDraftFanSerialNo.setText(""+serialNo);

            if(draftFanBinding.etDraftFanSerialNo.getText().toString().trim().length()>0){

                draftFanBinding.etDraftFanSerialNo.setError(null);
            }else{

                draftFanBinding.etDraftFanSerialNo.setError("Scan serial no");
            }


            prevDistributorAddress = job.getString("DistributorAddress");

            prevDistributorName = job.getString("DistributorName");
            prevDivisionID = job.getString("DivisionID");
            prevDivisionName = job.getString("DivisionName");
            prevInvoiceID = job.getString("InvoiceID");
            String lastInvoice[]=new String[1];
            lastInvoice[0]=prevInvoiceID;
           // selectedSerialNo =prevInvoiceID;

            serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, lastInvoice);
            serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            draftFanBinding.spDraftFanInvoice.setAdapter(serialNoItemSpinnerAdapter);

         /*   try {
                if (invoiveNoArr!=null)
                if (prevInvoiceID != null) {
                    int invoicePosition =0;
                    for(int j=0; j< invoiveNoArr.length; j++){
                        String invoiceNo = invoiveNoArr[j];
                        if(invoiceNo.equalsIgnoreCase(prevInvoiceID)){
                            invoicePosition = j;
                        }

                    }

                    //     int spinnerSerialPosition = serialNoItemSpinnerAdapter.getPosition(prevInvoiceID);
                    draftFanBinding.spDraftFanInvoice.setSelection(invoicePosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            String materialName = job.getString("MaterialName");
            draftFanBinding.etDraftMaterialName.setText(""+materialName);
        } catch (JSONException e) {
            e.printStackTrace();
           // JSONObject job = (JSONObject) jsonArray.get(startQnty);
            getMaterialList(""); /*try catch */
        }
        //


      /*  try{
            JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            job.remove("TotalQuantity");
            job.put("TotalQuantity", "" + quantyTotal);
            job.remove("VisitDate");
            job.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            job.remove("InspectionDate");
            job.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            job.remove("MaterialName");
            job.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
            job.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
            job.remove("FaultId");
            job.put("FaultId", "" + selectFaultId);
            job.remove("ProductStatus");
            job.put("ProductStatus", "" + productStatus);
            job.remove("DateOfPurchase");
            job.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
            job.remove("WarrantyStatus");
            job.put("WarrantyStatus", "" + fanWarrantyStatus.trim());
            job.remove("ClaimStatus");
            job.put("ClaimStatus", "" + claimStatus);
            job.remove("SerialNo");
            job.put("SerialNo", "" + fanWarrantyInputSerialNo.trim());
            job.remove("DistributorAddress");
            job.put("DistributorAddress", "" + visitDetailEntity.getAddress());
            job.remove("DistributorName");
            job.put("DistributorName", "" + visitDetailEntity.getDistributorname());
            job.remove("DivisionID");
            job.put("DivisionID", "" + visitDetailEntity.getDivisionid());
            job.remove("DivisionName");
            job.put("DivisionName", "" + visitDetailEntity.getDivisionname());
            job.remove("InvoiceID");
            job.put("InvoiceID", "" + selectedSerialNo);
            //adding json objects to json array
            //  MainArray.put(job);
            //adding json array to json object
            //  MainObject.put("SaveFanDetails", MainArray);

        }catch(Exception ex){
            ex.getMessage();
        }

        JSONObject person = new JSONObject();
        try {
            person.put("TotalQuantity", "" + quantyTotal);
            person.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
            //MaterialID
            person.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
            person.put("FaultId", "" + selectFaultId);
            person.put("ProductStatus", "" + productStatus);
            person.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
            //  person.put("WarrantyStatus", "" + draftFanBinding.etDraftFanWarrantyStatus.getText().toString());
            person.put("WarrantyStatus", "" +fanWarrantyStatus);
            person.put("ClaimStatus", "" + claimStatus);
            person.put("SerialNo", "" + draftFanBinding.etDraftFanSerialNo.getText().toString().trim());
            person.put("DistributorAddress", "" + visitDetailEntity.getAddress());
            person.put("DistributorName", "" + visitDetailEntity.getDistributorname());
            person.put("DivisionID", "" + visitDetailEntity.getDivisionid());
            person.put("DivisionName", "" + visitDetailEntity.getDivisionname());
            person.put("InvoiceID", "" + selectedSerialNo);

            //adding json objects to json array
            MainArray.put(person);
            //adding json array to json object
            MainObject.put("SaveFanDetails", MainArray);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());

            //  liveStreamData.add("live_stream",gsonObject);
            //final json string
            finalJsonData = MainObject.toString();
            //  System.out.println("jsonString: " + finalJsonData);
            Log.d("page json ", finalJsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/





        //
    }

    private void bindDataIntoJson(String mClickFlag) {

        if (draftFanBinding.radDraftFanSold.isChecked()) {
            productStatus = "Sold";
        } else if (draftFanBinding.radDraftFanUnSold.isChecked()) {
            productStatus = "Unsold";
        }
        if (draftFanBinding.radioDraftFanClaimRejected.isChecked()) {
            claimStatus = "Rejected";
        } else if (draftFanBinding.radioDraftFanClaimAccepted.isChecked()) {
            claimStatus = "Accepted";
        }

        int jsnCount = MainArray.length();
        if(jsnCount>startQnty){ // read
            try {
                JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");

                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                Log.e("getobjectValue","getObject"+job);

                String totalQnty = job.getString("TotalQuantity");
                String visitDate = job.getString("VisitDate");
                draftFanBinding.etDraftFanInspectionDate.setText(visitDate);
                String inspDate = job.getString("InspectionDate");
                draftFanBinding.etDraftFanInspectionDate.setText(""+inspDate);
                String materialName = job.getString("MaterialName");
                draftFanBinding.etDraftMaterialName.setText(""+materialName);
                prevFaultId = job.getString("FaultId");
                if (prevFaultId != null) {
                    int spinnerfaultPosition =0;
                    for(int kCount =0; kCount<fId.length; kCount++){
                        if(prevFaultId.equalsIgnoreCase(fId[kCount]) ){
                            spinnerfaultPosition = kCount;

                        }
                    }
                    int spinnerSerialPosition =  faultItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                    draftFanBinding.spDraftFanFault.setSelection(spinnerSerialPosition);
                }
                prevProductStatus = job.getString("ProductStatus");
                if(prevProductStatus.equalsIgnoreCase("Sold")){
                    draftFanBinding.radDraftFanSold.setChecked(true);
                    draftFanBinding.radDraftFanUnSold.setChecked(false);
                }else if(prevProductStatus.equalsIgnoreCase("Unsold")){
                    draftFanBinding.radDraftFanSold.setChecked(false);
                    draftFanBinding.radDraftFanUnSold.setChecked(true);
                }

                String DatePurchage = job.getString("DateOfPurchase");
                draftFanBinding.etDraftFanPurchaseDate.setText(""+DatePurchage);
                String warrantyStatus = job.getString("WarrantyStatus");
                draftFanBinding.etDraftFanWarrantyStatus.setText(""+warrantyStatus);
                prevClaimStatus = job.getString("ClaimStatus");
                if(prevClaimStatus.equalsIgnoreCase("Accepted")){
                    draftFanBinding.radioDraftFanClaimAccepted.setChecked(true);
                    draftFanBinding.radioDraftFanClaimRejected.setChecked(false);
                }else if(prevClaimStatus.equalsIgnoreCase("Rejected")){
                    draftFanBinding.radioDraftFanClaimAccepted.setChecked(false);
                    draftFanBinding.radioDraftFanClaimRejected.setChecked(true);
                }

                String serialNo  = job.getString("SerialNo");
                draftFanBinding.etDraftFanSerialNo.setText(""+serialNo);
                prevDistributorAddress = job.getString("DistributorAddress");

                prevDistributorName = job.getString("DistributorName");
                prevDivisionID = job.getString("DivisionID");
                prevDivisionName = job.getString("DivisionName");
                prevInvoiceID = job.getString("InvoiceID");
           //     selectedSerialNo =prevInvoiceID;

                if (prevInvoiceID!=null){
                    String lastInvoice[]=new String[1];
                    lastInvoice[0]=prevInvoiceID;
                    serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, lastInvoice);
                    serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    draftFanBinding.spDraftFanInvoice.setAdapter(serialNoItemSpinnerAdapter);

                }

              /*  try {
                    Log.e("getArray","getArray"+invoiveNoArr);
                    if (invoiveNoArr!=null)
                    if (prevInvoiceID != null) {
                        int invoicePosition =0;
                        for(int j=0; j< invoiveNoArr.length; j++){

                            String invoiceNo = invoiveNoArr[j];
                            if(invoiceNo.equalsIgnoreCase(prevInvoiceID)){
                                invoicePosition = j;
                            }

                        }

                        //     int spinnerSerialPosition = serialNoItemSpinnerAdapter.getPosition(prevInvoiceID);
                        draftFanBinding.spDraftFanInvoice.setSelection(invoicePosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

 // Last changes
            try{
                JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                job.remove("TotalQuantity");
                job.put("TotalQuantity", "" + quantyTotal);
                job.remove("VisitDate");
                job.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                job.remove("InspectionDate");
                job.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                job.remove("MaterialName");
                job.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
                job.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
                job.remove("FaultId");
                job.put("FaultId", "" + selectFaultId);
                job.remove("ProductStatus");
                job.put("ProductStatus", "" + productStatus);
                job.remove("DateOfPurchase");
                job.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
                job.remove("WarrantyStatus");
                job.put("WarrantyStatus", "" + fanWarrantyStatus.trim());
               // job.remove("ClaimStatus");
                //job.put("ClaimStatus", "" + claimStatus);
                job.remove("SerialNo");
                job.put("SerialNo", "" + fanWarrantyInputSerialNo.trim());
                job.remove("DistributorAddress");
                job.put("DistributorAddress", "" + visitDetailEntity.getAddress());
                job.remove("DistributorName");
                job.put("DistributorName", "" + visitDetailEntity.getDistributorname());
                job.remove("DivisionID");
                job.put("DivisionID", "" + visitDetailEntity.getDivisionid());
                job.remove("DivisionName");
                job.put("DivisionName", "" + visitDetailEntity.getDivisionname());
              //  job.remove("InvoiceID");
                //job.put("InvoiceID", "" + selectedSerialNo);
                //adding json objects to json array
                //  MainArray.put(job);
                //adding json array to json object
                //  MainObject.put("SaveFanDetails", MainArray);

            }catch(Exception ex){
                ex.getMessage();
            }
        }else if(jsnCount==startQnty){
            // write
            if(mClickFlag.equalsIgnoreCase("Sub")){
                int lastIndex = startQnty;
                lastIndex = lastIndex+1;
                if(lastIndex==quantyTotal){
                lastItemSubmit();
                }else {
                    lastItemSubmit();
                }
            }else{
                resetInvoiceFaultFanUI();
                resetFanUI();
            }

        }else{ // write
            JSONObject person = new JSONObject();
            try {
                person.put("TotalQuantity", "" + quantyTotal);
                person.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                person.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                person.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
                //MaterialID
                person.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
                person.put("FaultId", "" + selectFaultId);
                person.put("ProductStatus", "" + productStatus);
                person.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
              //  person.put("WarrantyStatus", "" + draftFanBinding.etDraftFanWarrantyStatus.getText().toString());
                person.put("WarrantyStatus", "" +fanWarrantyStatus);
                person.put("ClaimStatus", "" + claimStatus);
                person.put("SerialNo", "" + draftFanBinding.etDraftFanSerialNo.getText().toString().trim());
                person.put("DistributorAddress", "" + visitDetailEntity.getAddress());
                person.put("DistributorName", "" + visitDetailEntity.getDistributorname());
                person.put("DivisionID", "" + visitDetailEntity.getDivisionid());
                person.put("DivisionName", "" + visitDetailEntity.getDivisionname());
                person.put("InvoiceID", "" + selectedSerialNo);

                //adding json objects to json array
                MainArray.put(person);
                //adding json array to json object
                MainObject.put("SaveFanDetails", MainArray);

                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());

                //  liveStreamData.add("live_stream",gsonObject);
                //final json string
                finalJsonData = MainObject.toString();
                //  System.out.println("jsonString: " + finalJsonData);
                Log.d("page json ", finalJsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetInvoiceFaultFanUI() {
        draftFanBinding.spDraftFanFault.setSelection(0);
        Log.e("nextInvoiceList","nextInvoiceList"+nextInvoiceList.size());

        String defaultSerialInvoice[]=new String[1];
        defaultSerialInvoice[0]="Select Invoice No.";
        serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, defaultSerialInvoice);
        serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        draftFanBinding.spDraftFanInvoice.setAdapter(serialNoItemSpinnerAdapter);



    }

    private void resetFanUI() {
        // getFaultList();
        // setFanData();
        draftFanBinding.etDraftFanSerialNo.setText("");
        draftFanBinding.etDraftMaterialName.setText("");
        draftFanBinding.etDraftFanPurchaseDate.setText("");
        draftFanBinding.etDraftFanWarrantyStatus.setText("");
        //  draftFanBinding.radFanSold.setChecked(false);
        // draftFanBinding.radFanUnSold.setChecked(false);
        //  draftFanBinding.radioFanClaimAccepted.setChecked(false);
        //  draftFanBinding.radioFanClaimRejected.setChecked(false);
    }

    private boolean isValidate() {
        boolean isChecked = false;
        if(draftFanBinding.etDraftFanSerialNo.getText().toString().trim().length()>0){
            isChecked = true;
            draftFanBinding.etDraftFanSerialNo.setError(null);
        }else{
            isChecked = false;
            draftFanBinding.etDraftFanSerialNo.setError("Scan serial no");
        }

        if(isChecked) {
            if (draftFanBinding.etDraftMaterialName.getText().toString().trim().length() > 0) {
                isChecked = true;
                draftFanBinding.etDraftMaterialName.setError(null);
            } else {
                isChecked = false;
                draftFanBinding.etDraftMaterialName.setError("Enter Material Name");
            }
        }

        if(isChecked) {
            if (!selectFaultId.equalsIgnoreCase("0")) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Select Fault", Toast.LENGTH_LONG).show();
            }
        }

        if(isChecked) {
            if (draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim().length() > 0) {
                isChecked = true;
                draftFanBinding.etDraftFanPurchaseDate.setError(null);
            } else {
                isChecked = false;
                draftFanBinding.etDraftFanPurchaseDate.setError("Enter purchage date");
            }
        }

        if(isChecked) {
            if (draftFanBinding.etDraftFanWarrantyStatus.getText().toString().trim().length() > 0) {
                isChecked = true;
                draftFanBinding.etDraftFanWarrantyStatus.setError(null);
            } else {
                isChecked = false;
                draftFanBinding.etDraftFanWarrantyStatus.setError("Enter warranty status");
            }
        }

        if(isChecked) {
            if (draftFanBinding.radDraftFanSold.isChecked() || draftFanBinding.radDraftFanUnSold.isChecked()) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Select product status", Toast.LENGTH_LONG).show();
            }
        }

        if(isChecked) {
            if (draftFanBinding.radioDraftFanClaimAccepted.isChecked() || draftFanBinding.radioDraftFanClaimRejected.isChecked()) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Select claim status", Toast.LENGTH_LONG).show();
            }
        }
        Log.e("GetInvoice","GetInvoice"+selectedSerialNo);
        if (compareJsonLenth()){
            if(isChecked) {
                if (selectedSerialNo!=null){
                    if (selectedSerialNo.equalsIgnoreCase("Select Invoice No.")) {

                        isChecked = false;
                        Toast.makeText(mContext, "Select Invoice No.", Toast.LENGTH_LONG).show();
                    } else {
                        isChecked = true;
                    }
                }

            }

        }

        return isChecked;
    }

    private void selectPurchageDate() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd MMMM yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                StringBuilder purchageDate = new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear + 1).append("-").append(year);
                String DateTimeStamp = sdf.format(myCalendar.getTime());
                draftFanBinding.etDraftFanPurchaseDate.setText(purchageDate);
                getproductWarrantyStatus();
            }

        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        final Calendar minCalendar = Calendar.getInstance();
        int minDay = minCalendar.get(Calendar.DAY_OF_MONTH),
                minMonth = minCalendar.get(Calendar.MONTH),
                minYear = minCalendar.get(Calendar.YEAR)-2;
        minCalendar.set(Calendar.YEAR, minYear);
        minCalendar.set(Calendar.MONTH, minMonth);
        minCalendar.set(Calendar.DAY_OF_MONTH, minDay);
        datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());        datePickerDialog.show();

        datePickerDialog.show();
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    public boolean isCameraAvailable() {
        PackageManager pm = getActivity().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    public void openScanner() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (isCameraAvailable()) {

                IntentIntegrator.forSupportFragment(DraftFanDetailFragment.this).initiateScan();

            } else {
                Toast.makeText(getActivity(), "Rear Facing Camera Unavailable",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.d("MainActivity", "cancelled scan");
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
            } else {

                barCode = Result.getContents();


                if(getUniqueSerialNo(barCode)){
                    draftFanBinding.etDraftFanSerialNo.setText(barCode);
                    draftFanBinding.etDraftFanSerialNo.setEnabled(false);
                    draftFanBinding.etDraftFanSerialNo.setFocusable(false);
                    getMaterialList("");
                }else{
                    Toast.makeText(getActivity(), "You can't scan same serial no", Toast.LENGTH_SHORT).show();
                }


              /*  draftFanBinding.etDraftFanSerialNo.setText(barCode);
                draftFanBinding.etDraftFanSerialNo.setEnabled(false);
                draftFanBinding.etDraftFanSerialNo.setFocusable(false);
                getMaterialList();
                Log.d("MainActivity", "Scanned");
                Toast.makeText(getActivity(), "Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // submit json
    private void saveData(int StatusId) {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            //  draftFanBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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

        String url = String.format(ServerConfig.saveFanDetailUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),visitDetailEntity.getDistributorid(),visitDetailEntity.getRequestno(),StatusId,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M");
        apiInterface.saveFanDetailData(url,gsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ApiBaseResponse entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus().equalsIgnoreCase("200")) {
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                       /* startQnty = startQnty+1;
                        resetFanUI();*/
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, HomePageFragment.newInstance("", ""), fragmentTag).addToBackStack(null).commit();

                        } else {
                            //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }

    // Status list

    private void getStatusList() {


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
//
        String url = String.format(ServerConfig.getSaveStatusListUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
        apiInterface.getSaveStatusData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaveStatusBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SaveStatusBaseResponse entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus()!=null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                statusList = entity.getStatusList();
                                if(statusList!=null && statusList.size()>0){

                                    for(int iCount =0; iCount<statusList.size(); iCount++ ){
                                        String statusName = statusList.get(iCount).getName();
                                        if(statusName.equalsIgnoreCase("PENDING")){
                                            status_pending_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("CONFIRM")){
                                            status_confirm_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("REJECT")){
                                            status_reject_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("COMPLETED")){
                                            status_completed_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("SHORT CLOSE")){
                                            status_short_closed_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("DRAFT")){
                                            status_draft_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("APPROVAL")){
                                            status_approval_id = statusList.get(iCount).getID();
                                        }else if(statusName.equalsIgnoreCase("DELETE")){
                                            status_delete_id = statusList.get(iCount).getID();
                                        }

                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }

    //
    private void submit_dialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_status_fan_layout);
        TextView tvDraft = dialog.findViewById(R.id.tvDraft);
        TextView tvShortClosed = dialog.findViewById(R.id.tvShortClosed);
        TextView tvCompleted = dialog.findViewById(R.id.tvCompleted);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        int showQnty21 = startQnty;
        showQnty21 = showQnty21+1;

        if (showQnty21 < quantyTotal) {
            tvShortClosed.setFocusable(true);
            tvShortClosed.setClickable(true);
            tvShortClosed.setTextColor(getResources().getColor(R.color.shortClose));
            Log.e("getData","getData"+quantyTotal);
        }else {
            tvShortClosed.setFocusable(false);
            tvShortClosed.setClickable(false);
            tvShortClosed.setTextColor(Color.GRAY);
            Log.e("getData","getData"+startQnty);

        }

        tvShortClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int showQnty21 = startQnty;
                showQnty21 = showQnty21+1;
                if (showQnty21 < quantyTotal) {
                    int selected_item;
                    if(statusList!=null && statusList.size()>0) {
                        if (statusList != null && statusList.size() > 0) {
                            for (int kCount = 0; kCount < statusList.size(); kCount++) {
                                if (statusList.get(kCount).getName().equalsIgnoreCase("SHORT CLOSE")) {
                                    selected_item = statusList.get(kCount).getID();
                                    saveData(status_short_closed_id);
                                }
                            }
                        }
                    }
                }else {
                    Toast.makeText(getActivity(),"You can short-close this visit only if  your inspected quantity is less than PDI quantity.",Toast.LENGTH_SHORT).show();
                }


            }
        });

        int showQnty211= startQnty;
        showQnty211 = showQnty211+1;
        if (quantyTotal==showQnty211) {
            tvCompleted.setFocusable(true);
            tvCompleted.setClickable(true);
            tvCompleted.setTextColor(getResources().getColor(R.color.completed));
            Log.e("quantyTotal","quantyTotal"+quantyTotal);

        }
        else {
            tvCompleted.setFocusable(false);
            tvCompleted.setClickable(false);
            tvCompleted.setTextColor(Color.GRAY);
            Log.e("getData","getData"+quantyTotal);

        }

        tvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int showQnty21 = startQnty;
                showQnty21 = showQnty21+1;
                if (quantyTotal==showQnty21) {
                    int selected_item;
                    if(statusList!=null && statusList.size()>0) {
                        if (statusList != null && statusList.size() > 0) {
                            for (int kCount = 0; kCount < statusList.size(); kCount++) {
                                if (statusList.get(kCount).getName().equalsIgnoreCase("COMPLETED")) {
                                    selected_item = statusList.get(kCount).getID();
                                    Log.e("FinalJsonObject","Final"+gsonObject);

                                     saveData(status_completed_id);
                                }
                            }
                        }
                    }
                }else {
                    Toast.makeText(getActivity(),"You can complete this visit only if your inspected quantity is same as the PDI quantity.",Toast.LENGTH_SHORT).show();

                }



            }
        });

        tvDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int selected_item ;

                if(statusList!=null && statusList.size()>0) {
                    for (int kCount = 0; kCount < statusList.size(); kCount++) {
                        if (statusList.get(kCount).getName().equalsIgnoreCase("DRAFT")) {
                            selected_item = statusList.get(kCount).getID();
                            Log.e("FinalJsonObject","Draft"+gsonObject);

                             saveData(status_draft_id);
                        }
                    }
                }
            }
        });



    }


    //show last fan data
    private void showlastData() {
        // Change by Rahul
        int showQnty = startQnty;
        showQnty = showQnty+1;
        draftFanBinding.etDraftFanQnty.setText(String.valueOf(showQnty)+"/"+String.valueOf(quantyTotal));

        if (quantyTotal==showQnty) {
            draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
            draftFanBinding.btnDraftFanNext.setEnabled(false);
            draftFanBinding.btnDraftFanNext.setFocusable(false);
            draftFanBinding.btnDraftFanNext.setClickable(false);
        }else{
            draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
            draftFanBinding.btnDraftFanNext.setEnabled(true);
            draftFanBinding.btnDraftFanNext.setFocusable(true);
            draftFanBinding.btnDraftFanNext.setClickable(true);
        }

     //   draftFanBinding.etDraftFanQnty.setText(String.valueOf(startQnty)+"/"+String.valueOf(quantyTotal));
        resetFanUI();
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");

            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            String totalQnty = job.getString("TotalQuantity");
            String visitDate = job.getString("VisitDate");
            draftFanBinding.etDraftFanInspectionDate.setText(visitDate);
            String inspDate = job.getString("InspectionDate");
            draftFanBinding.etDraftFanInspectionDate.setText(""+inspDate);
            String materialName = job.getString("MaterialName");
            draftFanBinding.etDraftMaterialName.setText(""+materialName);

            prevFaultId = job.getString("FaultId");
            if (prevFaultId != null && fId!=null) {
                int spinnerfaultPosition =0;
                for(int kCount =0; kCount<fId.length; kCount++){
                    if(prevFaultId.equalsIgnoreCase(fId[kCount]) ){
                        spinnerfaultPosition = kCount;

                    }
                }
                int spinnerSerialPosition =  faultItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                draftFanBinding.spDraftFanFault.setSelection(spinnerSerialPosition);
            }
            prevProductStatus = job.getString("ProductStatus");
            if(prevProductStatus.equalsIgnoreCase("Sold")){
                draftFanBinding.radDraftFanSold.setChecked(true);
                draftFanBinding.radDraftFanUnSold.setChecked(false);
            }else if(prevProductStatus.equalsIgnoreCase("Unsold")){
                draftFanBinding.radDraftFanSold.setChecked(false);
                draftFanBinding.radDraftFanUnSold.setChecked(true);
            }


            String warrantyStatus = job.getString("WarrantyStatus");
            draftFanBinding.etDraftFanWarrantyStatus.setText(""+warrantyStatus);

           /* if (warrantyStatus.equalsIgnoreCase("IN")){
                draftFanBinding.etDraftFanWarrantyStatus.setText("In Warranty");
            }else if (warrantyStatus.equalsIgnoreCase("OUT")){
                draftFanBinding.etDraftFanWarrantyStatus.setText("Out of Warranty");
            }*/


            prevClaimStatus = job.getString("ClaimStatus");
            if(prevClaimStatus.equalsIgnoreCase("Accepted")){
                draftFanBinding.radioDraftFanClaimAccepted.setChecked(true);
                draftFanBinding.radioDraftFanClaimRejected.setChecked(false);
            }else if(prevClaimStatus.equalsIgnoreCase("Rejected")){
                draftFanBinding.radioDraftFanClaimAccepted.setChecked(false);
                draftFanBinding.radioDraftFanClaimRejected.setChecked(true);
            }

            String serialNo  = job.getString("SerialNo");
            draftFanBinding.etDraftFanSerialNo.setText(""+serialNo);
         //   getMaterialList(job.getString("InvoiceID"));  /*get last data Invoice Update*/
            prevDistributorAddress = job.getString("DistributorAddress");
            prevDistributorName = job.getString("DistributorName");
            prevDivisionID = job.getString("DivisionID");
            prevDivisionName = job.getString("DivisionName");
            String DatePurchage = job.getString("DateOfPurchase");
            draftFanBinding.etDraftFanPurchaseDate.setText(""+DatePurchage);
            prevInvoiceID = job.getString("InvoiceID");
            String lastInvoice[]=new String[1];
            lastInvoice[0]=prevInvoiceID;
           // selectedSerialNo =prevInvoiceID;

            serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, lastInvoice);
            serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            draftFanBinding.spDraftFanInvoice.setAdapter(serialNoItemSpinnerAdapter);
           /* try {
                if (prevInvoiceID != null) {
                    int invoicePosition =0;
                    for(int j=0; j< invoiveNoArr.length; j++){

                        String invoiceNo = invoiveNoArr[j];
                        if(invoiceNo.equalsIgnoreCase(prevInvoiceID)){
                            invoicePosition = j;
                        }

                    }

               //     int spinnerSerialPosition = serialNoItemSpinnerAdapter.getPosition(prevInvoiceID);
                    draftFanBinding.spDraftFanInvoice.setSelection(invoicePosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //
    private void delete_dialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_delete_dialog);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        TextView tvOK = dialog.findViewById(R.id.tvOK);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONArray jsonArray = null;
                try {
                    jsonArray = MainObject.getJSONArray("SaveFanDetails");
                    if (jsonArray!= null && jsonArray.length()>0){
                        JSONObject job = (JSONObject) jsonArray.get(startQnty);
                        String totalQnty = job.getString("TotalQuantity");
                        jsonArray.remove(startQnty);
                        if(startQnty>0) {
                            showPreviousData();
                        }else{
                            draftFanBinding.fanDraftDeleteImage.setVisibility(View.GONE);
                            resetFanUI();
                        }
                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }



//

    public boolean getUniqueSerialNo(String currentSno){
        boolean isUnique = true;
        try{
            JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
            if (jsonArray!= null && jsonArray.length()>0) {
                for(int jKount =0; jKount< jsonArray.length(); jKount++){
                    JSONObject jobS = (JSONObject) jsonArray.get(jKount);
                    String serialNo  = jobS.getString("SerialNo");
                    if(currentSno.equalsIgnoreCase(serialNo)){
                        isUnique = false;

                    }else if(isUnique){
                        isUnique = true;
                    }
                }
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        return isUnique;
    }

    //

    //

    private void preservedData_dialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_preserve_data_layout);
        TextView tvCancel = dialog.findViewById(R.id.tvPreserveCancel);
        TextView tvOK = dialog.findViewById(R.id.tvPreserveOK);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (draftFanBinding.radDraftFanSold.isChecked()) {
                    productStatus = "Sold";
                } else if (draftFanBinding.radDraftFanUnSold.isChecked()) {
                    productStatus = "Unsold";
                }
                if (draftFanBinding.radioDraftFanClaimRejected.isChecked()) {
                    claimStatus = "Rejected";
                } else if (draftFanBinding.radioDraftFanClaimAccepted.isChecked()) {
                    claimStatus = "Accepted";
                }
                JSONObject person = new JSONObject();
                try {
                    person.put("TotalQuantity", "" + quantyTotal);
                    person.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                    person.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
                    person.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
                    //MaterialID
                    person.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
                    person.put("FaultId", "" + selectFaultId);
                    person.put("ProductStatus", "" + productStatus);
                    person.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
                    //  person.put("WarrantyStatus", "" + draftFanBinding.etDraftFanWarrantyStatus.getText().toString());
                    person.put("WarrantyStatus", "" +fanWarrantyStatus);
                    person.put("ClaimStatus", "" + claimStatus);
                    person.put("SerialNo", "" + draftFanBinding.etDraftFanSerialNo.getText().toString().trim());
                    person.put("DistributorAddress", "" + visitDetailEntity.getAddress());
                    person.put("DistributorName", "" + visitDetailEntity.getDistributorname());
                    person.put("DivisionID", "" + visitDetailEntity.getDivisionid());
                    person.put("DivisionName", "" + visitDetailEntity.getDivisionname());
                    person.put("InvoiceID", "" + selectedSerialNo);

                    //adding json objects to json array
                    MainArray.put(person);
                    //adding json array to json object
                    MainObject.put("SaveFanDetails", MainArray);

                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());

                    //  liveStreamData.add("live_stream",gsonObject);
                    //final json string
                    finalJsonData = MainObject.toString();
                    //  System.out.println("jsonString: " + finalJsonData);
                    Log.d("page json ", finalJsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int showQnty21 = startQnty;
                showQnty21 = showQnty21+1;
                if (quantyTotal==showQnty21) {
                    draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
                    draftFanBinding.btnDraftFanNext.setEnabled(false);
                    draftFanBinding.btnDraftFanNext.setFocusable(false);
                    draftFanBinding.btnDraftFanNext.setClickable(false);
                }else{
                    draftFanBinding.btnDraftFanNext.setVisibility(View.VISIBLE);
                    draftFanBinding.btnDraftFanNext.setEnabled(true);
                    draftFanBinding.btnDraftFanNext.setFocusable(true);
                    draftFanBinding.btnDraftFanNext.setClickable(true);
                }
                //
                dialog.dismiss();
            }
        });
    }


    //
    public void lastItemSubmit(){

        JSONObject person = new JSONObject();
        try {
            person.put("TotalQuantity", "" + quantyTotal);
            person.put("VisitDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("InspectionDate", "" + draftFanBinding.etDraftFanInspectionDate.getText().toString().trim());
            person.put("MaterialName", "" + draftFanBinding.etDraftMaterialName.getText().toString().trim());
            //MaterialID
            person.put("MaterialID",draftFanBinding.etDraftMaterialName.getTag());
            person.put("FaultId", "" + selectFaultId);
            person.put("ProductStatus", "" + productStatus);
            person.put("DateOfPurchase", "" + draftFanBinding.etDraftFanPurchaseDate.getText().toString().trim());
            //  person.put("WarrantyStatus", "" + draftFanBinding.etDraftFanWarrantyStatus.getText().toString());
            person.put("WarrantyStatus", "" +fanWarrantyStatus);
            person.put("ClaimStatus", "" + claimStatus);
            person.put("SerialNo", "" + draftFanBinding.etDraftFanSerialNo.getText().toString().trim());
            person.put("DistributorAddress", "" + visitDetailEntity.getAddress());
            person.put("DistributorName", "" + visitDetailEntity.getDistributorname());
            person.put("DivisionID", "" + visitDetailEntity.getDivisionid());
            person.put("DivisionName", "" + visitDetailEntity.getDivisionname());
            person.put("InvoiceID", "" + selectedSerialNo);

            //adding json objects to json array
            MainArray.put(person);
            //adding json array to json object
            MainObject.put("SaveFanDetails", MainArray);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());

            //  liveStreamData.add("live_stream",gsonObject);
            //final json string
            finalJsonData = MainObject.toString();
            //  System.out.println("jsonString: " + finalJsonData);
            Log.d("page json ", finalJsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean compareJsonLenth() {
        boolean countMainArray = false;
        int jsnCount = MainArray.length();
        int startSecondQnty = startQnty;
        startSecondQnty = startSecondQnty + 1;
        if (jsnCount >= startSecondQnty) {
            countMainArray = false;
        } else {
            countMainArray = true;

        }
        return countMainArray;
    }
    //
}

