package com.luminous.pdi.addproduct_fan.fragments;

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
import android.view.Menu;
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
import com.luminous.pdi.databinding.FragmentAddProductFanDetailBinding;

import com.luminous.pdi.draft.dto.WarrantyBaseResponse;
import com.luminous.pdi.draft.dto.WarrantyDto;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.visit_detail.AddProductVisitDetailFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.core.CommonUtility.getCurrentDate;
import static com.luminous.pdi.home.activities.HomePageActivity.binding;


public class AddProductFanDetailFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    String fragmentTag = "",Imei;
    private FragmentAddProductFanDetailBinding addProductFanDetailBinding;
    private String secretCode = "", barCode = "";
    int quantyTotal =0, startQnty = 0;
    public static final int REQUEST_CAMERA_PERMISSION = 225;
    private PendingvisitDetail visitDetailEntity;
    private SharedPrefsManager sharedPrefsManager;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String[] fName ;
    private String[] fId ;
    List<FaultList> faultDataList;
    String selectFaultName ,selectFaultId;
    String selectedMaterialName , selectedMaterialId, selectedSerialNo;
    String[] serialNo ;
    String productStatus;
    String claimStatus;
    String SerailNoExist;
    int distributerId;
    String finalJsonData;
    ArrayAdapter<CharSequence> faultItemSpinnerAdapter;
    ArrayAdapter<CharSequence> serialNoItemSpinnerAdapter;
    String prevFaultId, prevProductStatus, prevClaimStatus, prevDistributorAddress, prevDistributorName, prevDivisionID, prevDivisionName, prevInvoiceID;
    //creating json object to put json array
    JSONObject MainObject = new JSONObject();
    //creating json array
    JSONArray MainArray = new JSONArray();

    JsonObject gsonObject = new JsonObject();
    int jsnCount=0;
    String fanWarrantyStatus,fanWarrantyInputSerialNo;
    int status_pending_id, status_confirm_id, status_reject_id, status_completed_id,status_short_closed_id,status_draft_id, status_approval_id, status_delete_id;
    List<StatusListDto> statusList;
    private String tag="TAG_FRAGMENT";
    String draftFanPurchageDate;
    String clickFlag = "";

    public AddProductFanDetailFragment() {
        // Required empty public constructor
    }


    public static AddProductFanDetailFragment newInstance(String param1, String param2) {
        AddProductFanDetailFragment fragment = new AddProductFanDetailFragment();
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
        addProductFanDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product_fan_detail, container, false);


        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        setHeaderTitle();
        Bundle args = getArguments();
        if (args != null) {
            visitDetailEntity = (PendingvisitDetail) args.getSerializable("VisitDetail");
            quantyTotal = Integer.parseInt(args.getString("Quantity"));
            distributerId= args.getInt("distributerId");

        } else {
            Log.w("FanVisitDetail", "Arguments expected, but missing");
        }

        if(startQnty==0){
            addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
          //  addProductFanDetailBinding.btnFanPrev.setFocusable(false);
            addProductFanDetailBinding.addproductfanvisitlistDeleteImage.setVisibility(View.GONE);
        }
        getFaultList();
        setFanData();
        getStatusList();
        setOnItemClickListener();
      /*  Menu menu = binding.bottomNavigation.getMenu();
        binding.bottomNavigation.getMenu().setGroupCheckable(0, false, false);
        binding.bottomNavigation.getMenu().setGroupEnabled(0, false);
        binding.bottomNavigation.getMenu().setGroupVisible(0,true);
        binding.bottomNavigation.getMenu().getItem(1).setCheckable(false);
        binding.bottomNavigation.getMenu().getItem(0).setCheckable(false);
        binding.bottomNavigation.setClickable(false);
*/
        // Change by Rahul
        int showQnty = startQnty;
        showQnty = showQnty+1;
        addProductFanDetailBinding.etFanQnty.setText(String.valueOf(showQnty)+"/"+String.valueOf(quantyTotal));
        // fault dropdown
        addProductFanDetailBinding.spFanFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("getData","getData"+selectFaultName+"selectFaultId"+selectFaultId);
                try {
                    if (faultDataList != null) {
                        if (position > 0) {

                            selectFaultName = fName[position];
                            selectFaultId = fId[position];
                            JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                            JSONObject job = (JSONObject) jsonArray.get(startQnty);
                            job.remove("FaultId");
                            job.put("FaultId", "" + selectFaultId);
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
  addProductFanDetailBinding.spFanInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          Log.e("getData","getData"+selectedSerialNo+"selectFaultId"+selectFaultId);



          try {
              if (serialNo != null) {
                  if (position > 0) {
                      selectedSerialNo = serialNo[position];

                      try {
                          JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                          JSONObject job = (JSONObject) jsonArray.get(startQnty);
                          job.remove("InvoiceID");
                          job.put("InvoiceID", "" + selectedSerialNo);
                      }catch (Exception e){

                      }

                  } else {
                      selectedSerialNo = serialNo[position];
                  //    Toast.makeText(mContext,""+selectedSerialNo ,Toast.LENGTH_LONG).show();
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
        addProductFanDetailBinding.etFanSerialNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   /* if (addProductFanDetailBinding.etFanSerialNo.getText().equals(v.getText().toString())){
                        Toast.makeText(getActivity(), "Scanned -> " + "You can't scan same serial no", Toast.LENGTH_SHORT).show();

                    }else {
                        getMaterialList();
                        Log.d("MainActivity", "Scanned");
                    }*/

                    String currentCode = v.getText().toString().trim();
                    if(getUniqueSerialNo(currentCode)) {
                        getMaterialList();
                    }else{
                        Toast.makeText(getActivity(),"You can't scan same serial no", Toast.LENGTH_SHORT).show();
                    }

                    handled = true;
                }
                return handled;
            }
        });

  //
        addProductFanDetailBinding.etFanWarrantyStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fanWarrantyStatus = ""+s.toString();

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                fanWarrantyStatus = ""+s.toString();

                Log.e("TextWatcherTest", "" +fanWarrantyStatus);
                //addProductFanDetailBinding.etFanWarrantyStatus.setText();
            }
        });

        addProductFanDetailBinding.etFanSerialNo.addTextChangedListener(new TextWatcher() {
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
        addProductFanDetailBinding.etFanFanPurchaseDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable str) {


                draftFanPurchageDate = ""+str.toString();
                Log.e("TextWatcherTest", "" +fanWarrantyStatus);
               // getproductWarrantyStatus();
            }
        });

  //
        View view= addProductFanDetailBinding.getRoot();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction()== KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!");
                    Bundle args = new Bundle();
                    args.putSerializable("VisitDetail",  visitDetailEntity);


                    Fragment fragment = AddProductVisitDetailFragment.newInstance("", "");
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });

        addProductFanDetailBinding.addproductfanvisitlistBackimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("VisitDetail",  visitDetailEntity);


                Fragment fragment = AddProductVisitDetailFragment.newInstance("", "");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();

            }
        });


        addProductFanDetailBinding.addproductfanvisitlistDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(startQnty==0){
                    addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.next));
                    addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                    Toast.makeText(mContext, "Please press NEXT to save this record first",Toast.LENGTH_LONG).show();

                }else{
                    try {
                        JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                        if(jsonArray.length()==startQnty && isValidate()){
                            Toast.makeText(mContext, "Please press NEXT to save this record first",Toast.LENGTH_LONG).show();

                        }else{
                            delete_dialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        return view;
    }
//
private void getproductWarrantyStatus() {

    if(draftFanPurchageDate!=null && !draftFanPurchageDate.isEmpty()){
        draftFanPurchageDate = draftFanPurchageDate;
    }else{
        draftFanPurchageDate = addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim();
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
    String serialNo= addProductFanDetailBinding.etFanSerialNo.getText().toString().trim();
    String inspectionDate= addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim();
    String pod= addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim();
    //
    String url = String.format(ServerConfig.getproductWarrantyStatusUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID),addProductFanDetailBinding.etFanSerialNo.getText().toString().trim(),addProductFanDetailBinding.etFanInspectionDate.getText().toString(),addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim(),sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN),appVersionName,appVersionCode,Imei, CommonUtility.getDeviceName(), "Android",""+ Build.VERSION.SDK_INT,""+Build.VERSION.RELEASE,""+CommonUtility.getIpAddress(mContext),"EN","LoginActivity",CommonUtility.getNetworkType(mContext),CommonUtility.getNetworkOperator(mContext),""+System.currentTimeMillis(),"M","");
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
                                    addProductFanDetailBinding.etFanWarrantyStatus.setText(""+fanWarrantyStatus);

                              /*  if (fanWarrantyStatus.equalsIgnoreCase("IN")){
                                    addProductFanDetailBinding.etFanWarrantyStatus.setText("In Warranty");

                                }else if (fanWarrantyStatus.equalsIgnoreCase("OUT")){
                                    addProductFanDetailBinding.etFanWarrantyStatus.setText("Out of Warranty");

                                }*/


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
    private void getMaterialList() {

        String fanSerialNo = fanWarrantyInputSerialNo; //addProductFanDetailBinding.etFanSerialNo.getText().toString().trim();
      //  fanSerialNo = "CCIHD9A9030433";
        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
            //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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
                        // addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus()!=null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                List<MaterialListDto> materialList = entity.getProduct_details();
                                if(materialList!=null && materialList.size()>0){
                                    if(materialList.get(0).getProductdescription()!=null && materialList.get(0).getProductdescription().equalsIgnoreCase(""))
                                    selectedMaterialName = materialList.get(0).getProductdescription();
                                    selectedMaterialId = materialList.get(0).getPRODUCT_ID();
                                    addProductFanDetailBinding.etMaterialName.setText(""+materialList.get(0).getProductdescription());
                                    addProductFanDetailBinding.etMaterialName.setTag(""+selectedMaterialId);
                                    List<InvoiceNoDto> invoiceList =  materialList.get(0).getInvoiceNo();
                                    serialNo = new String[invoiceList.size()+1];
                                    serialNo[0] = "Select Invoice No.";
                                    if(invoiceList!=null && invoiceList.size()>0){
                                        for(int kCount=0; kCount< invoiceList.size(); kCount++){
                                           serialNo[kCount+1] = invoiceList.get(kCount).getInvoiceNo();
                                        }
                                    }
                                   if(serialNo!=null && serialNo.length>0)
                                    setSerialNoListUI();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtility.printLog("ContactUsDetailData", "onComplete");
                    }
                });
    }

    private void setSerialNoListUI() {
        serialNoItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, serialNo);
        serialNoItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        addProductFanDetailBinding.spFanInvoice.setAdapter(serialNoItemSpinnerAdapter);
    }

    private void setHeaderTitle() {
     // Toolbar mToolbar = ( getActivity()).getSupportActionBar();

    }

    private void getFaultList() {

        if (!CommonUtility.isNetworkAvailable(mContext)) {
            return;
        }
        try {
          //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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
                       // addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
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



                        } else {
                          //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                      //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
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
        addProductFanDetailBinding.spFanFault.setAdapter(faultItemSpinnerAdapter);

    }



    private void setOnItemClickListener() {
        addProductFanDetailBinding.ivScan.setOnClickListener(this);
        addProductFanDetailBinding.tvFanManualLink.setOnClickListener(this);
        addProductFanDetailBinding.btnFanPrev.setOnClickListener(this);
        addProductFanDetailBinding.btnFanSubmit.setOnClickListener(this);
        addProductFanDetailBinding.btnFanNext.setOnClickListener(this);
        addProductFanDetailBinding.etFanFanPurchaseDate.setOnClickListener(this);
        addProductFanDetailBinding.ivPDate.setOnClickListener(this);
    }

    private void setFanData() {
       addProductFanDetailBinding.etFanInspectionDate.setText(getCurrentDate());
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

            case R.id.ivScan:
                openScanner();
                break;

            case R.id.tv_FanManualLink:
                addProductFanDetailBinding.etFanSerialNo.setEnabled(true);
                addProductFanDetailBinding.etFanSerialNo.setFocusable(true);
                addProductFanDetailBinding.etFanSerialNo.setClickable(true);
                addProductFanDetailBinding.etFanSerialNo.setText("");
                break;

            case R.id.ivPDate:
                selectPurchageDate();
                break;

            case R.id.btn_FanPrev:
                clickFlag = "Prev";
                addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                addProductFanDetailBinding.btnFanNext.setEnabled(true);
                addProductFanDetailBinding.btnFanNext.setFocusable(true);
                addProductFanDetailBinding.btnFanNext.setClickable(true);
                if(startQnty==0){
                        addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.next));
                        addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                      //  Toast.makeText(mContext, "Please press NEXT to save this record first",Toast.LENGTH_LONG).show();

                }else{
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

            case R.id.btn_FanSubmit:
                clickFlag = "Sub";
                if(isValidate()){
                   // addProductFanDetailBinding.radFanSold.isChecked() || addProductFanDetailBinding.radFanUnSold.isChecked()
                    bindDataIntoJson(clickFlag);
                    int submitStartQun=startQnty;
                    submitStartQun=submitStartQun+1;
                    if (quantyTotal==submitStartQun){
                        if (addProductFanDetailBinding.radFanSold.isChecked()) {
                            productStatus = "Sold";
                        } else if (addProductFanDetailBinding.radFanUnSold.isChecked()) {
                            productStatus = "Unsold";
                        }
                        if (addProductFanDetailBinding.radioFanClaimRejected.isChecked()) {
                            claimStatus = "Rejected";

                        } else if (addProductFanDetailBinding.radioFanClaimAccepted.isChecked()) {
                            claimStatus = "Accepted";

                        }
                        JSONObject person = new JSONObject();
                        try {
                            person.put("TotalQuantity", "" + quantyTotal);
                            person.put("VisitDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                            person.put("InspectionDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                            person.put("MaterialName", "" + addProductFanDetailBinding.etMaterialName.getText().toString().trim());
                            //MaterialID
                            person.put("MaterialID",addProductFanDetailBinding.etMaterialName.getTag());
                            person.put("FaultId", "" + selectFaultId);
                            person.put("ProductStatus", "" + productStatus);
                            person.put("DateOfPurchase", "" + addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim());
                            person.put("WarrantyStatus", "" + addProductFanDetailBinding.etFanWarrantyStatus.getText().toString());
                            person.put("ClaimStatus", "" + claimStatus);
                            person.put("SerialNo", "" + addProductFanDetailBinding.etFanSerialNo.getText().toString().trim());
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
                            Toast.makeText(mContext,"Data saved",Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        submit_dialog();

                    }else {
                        submit_dialog();

                    }

                }
                break;
                case R.id.btn_FanNext:
                  if(isValidate()){
                      clickFlag = "Next";
                      addProductFanDetailBinding.addproductfanvisitlistDeleteImage.setVisibility(View.VISIBLE);
                      if(startQnty<quantyTotal){

                          startQnty = startQnty+1;
                          // Changed by Rahul
                          int showQnty1 = startQnty;
                          if(startQnty==quantyTotal){
                              showQnty1 = showQnty1;
                              addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                              addProductFanDetailBinding.btnFanNext.setEnabled(false);
                              addProductFanDetailBinding.btnFanNext.setFocusable(false);
                              addProductFanDetailBinding.btnFanNext.setClickable(false);
                          }else {
                              showQnty1 = showQnty1+1;
                              if (quantyTotal==showQnty1) {
                                  addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                                  addProductFanDetailBinding.btnFanNext.setEnabled(false);
                                  addProductFanDetailBinding.btnFanNext.setFocusable(false);
                                  addProductFanDetailBinding.btnFanNext.setClickable(false);
                             }else{
                                  addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                                  addProductFanDetailBinding.btnFanNext.setEnabled(true);
                                  addProductFanDetailBinding.btnFanNext.setFocusable(true);
                                  addProductFanDetailBinding.btnFanNext.setClickable(true);
                              }
                          }

                          addProductFanDetailBinding.etFanQnty.setText(String.valueOf(showQnty1)+"/"+String.valueOf(quantyTotal));
                          bindDataIntoJson(clickFlag);
                          addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor((R.color.color_yellow)));
                          addProductFanDetailBinding.btnFanPrev.setFocusable(true);

                          if (startQnty==quantyTotal){
                              addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                          }else {
                              addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.next));

                          }
                          int jsnCount = MainArray.length();
                          if(jsnCount==startQnty) {
                              resetFanUI();
                          }else if(jsnCount>startQnty){

                          }
                      }else{
                          resetFanUI();
                         Toast.makeText(mContext,"Form is completed . Please check and save",Toast.LENGTH_LONG).show();
                      }
                  }
                break;
        }
    }

    private void showPreviousData() {
        startQnty = startQnty-1;
        if (startQnty==0){
            addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.next));
            addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
        }else {
            addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
            addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.previous));
        }
       // Change by Rahul
        int showQnty2 = startQnty;
        showQnty2 = showQnty2+1;
        addProductFanDetailBinding.etFanQnty.setText(String.valueOf(showQnty2)+"/"+String.valueOf(quantyTotal));
        Log.e("getstartQuantity","getstartQuantity"+startQnty);

        /*  if (startQnty==1){
            addProductFanDetailBinding.btnFanNext.setBackgroundColor(getResources().getColor(R.color.next));
        }else {
            addProductFanDetailBinding.btnFanPrev.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));

        }*/
        resetFanUI();
        try {
               JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");

                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                String totalQnty = job.getString("TotalQuantity");
                String visitDate = job.getString("VisitDate");
                addProductFanDetailBinding.etFanInspectionDate.setText(visitDate);
                String inspDate = job.getString("InspectionDate");
                addProductFanDetailBinding.etFanInspectionDate.setText(""+inspDate);
                String materialName = job.getString("MaterialName");
                addProductFanDetailBinding.etMaterialName.setText(""+materialName);

                 prevFaultId = job.getString("FaultId");
            if (prevFaultId != null) {
                int spinnerfaultPosition =0;
                for(int kCount =0; kCount<fId.length; kCount++){
                    if(prevFaultId.equalsIgnoreCase(fId[kCount]) ){
                         spinnerfaultPosition = kCount;

                    }
                }
                int spinnerSerialPosition =  faultItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                addProductFanDetailBinding.spFanFault.setSelection(spinnerSerialPosition);
            }
                 prevProductStatus = job.getString("ProductStatus");
                 if(prevProductStatus.equalsIgnoreCase("Sold")){
                     addProductFanDetailBinding.radFanSold.setChecked(true);
                     addProductFanDetailBinding.radFanUnSold.setChecked(false);
                 }else if(prevProductStatus.equalsIgnoreCase("Unsold")){
                     addProductFanDetailBinding.radFanSold.setChecked(false);
                     addProductFanDetailBinding.radFanUnSold.setChecked(true);
                 }

                String DatePurchage = job.getString("DateOfPurchase");
                addProductFanDetailBinding.etFanFanPurchaseDate.setText(""+DatePurchage);
                String warrantyStatus = job.getString("WarrantyStatus");
               addProductFanDetailBinding.etFanWarrantyStatus.setText(""+warrantyStatus);
               /* if (warrantyStatus.equalsIgnoreCase("IN")){
                    addProductFanDetailBinding.etFanWarrantyStatus.setText("In Warranty");

                }else if (warrantyStatus.equalsIgnoreCase("OUT")){
                    addProductFanDetailBinding.etFanWarrantyStatus.setText("Out of Warranty");

                }*/
                 prevClaimStatus = job.getString("ClaimStatus");
                 if(prevClaimStatus.equalsIgnoreCase("Accepted")){
                     addProductFanDetailBinding.radioFanClaimAccepted.setChecked(true);
                     addProductFanDetailBinding.radioFanClaimRejected.setChecked(false);
                 }else if(prevClaimStatus.equalsIgnoreCase("Rejected")){
                     addProductFanDetailBinding.radioFanClaimAccepted.setChecked(false);
                     addProductFanDetailBinding.radioFanClaimRejected.setChecked(true);
                 }

                String serialNo  = job.getString("SerialNo");
                addProductFanDetailBinding.etFanSerialNo.setText(""+serialNo);


            if(addProductFanDetailBinding.etFanSerialNo.getText().toString().trim().length()>0){

                addProductFanDetailBinding.etFanSerialNo.setError(null);
            }else{

                addProductFanDetailBinding.etFanSerialNo.setError("Scan serial no");
            }





                 prevDistributorAddress = job.getString("DistributorAddress");

                 prevDistributorName = job.getString("DistributorName");
                 prevDivisionID = job.getString("DivisionID");
                 prevDivisionName = job.getString("DivisionName");
                 prevInvoiceID = job.getString("InvoiceID");

              if (prevInvoiceID != null) {
                int spinnerSerialPosition =  serialNoItemSpinnerAdapter .getPosition(prevInvoiceID);
                addProductFanDetailBinding.spFanInvoice.setSelection(spinnerSerialPosition);
               }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void bindDataIntoJson(String mClickFlag) {

        if (addProductFanDetailBinding.radFanSold.isChecked()) {
            productStatus = "Sold";
        } else if (addProductFanDetailBinding.radFanUnSold.isChecked()) {
            productStatus = "Unsold";
        }
        if (addProductFanDetailBinding.radioFanClaimRejected.isChecked()) {
            claimStatus = "Rejected";
/*
            try {
                JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                job.remove("ClaimStatus");
                job.put("ClaimStatus", "" + claimStatus);
            }catch (Exception e){
            }*/


        } else if (addProductFanDetailBinding.radioFanClaimAccepted.isChecked()) {
            claimStatus = "Accepted";
/*
            try {
                JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                job.remove("ClaimStatus");
                job.put("ClaimStatus", "" + claimStatus);
            }catch (Exception e){
            }*/

        }

        int jsnCount = MainArray.length();
         if(jsnCount>startQnty){ // read
             try {
                 JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");

                 JSONObject job = (JSONObject) jsonArray.get(startQnty);
                 String totalQnty = job.getString("TotalQuantity");
                 String visitDate = job.getString("VisitDate");
                 addProductFanDetailBinding.etFanInspectionDate.setText(visitDate);
                 String inspDate = job.getString("InspectionDate");
                 addProductFanDetailBinding.etFanInspectionDate.setText(""+inspDate);
                 String materialName = job.getString("MaterialName");
                 addProductFanDetailBinding.etMaterialName.setText(""+materialName);
                 prevFaultId = job.getString("FaultId");
                 if (prevFaultId != null) {
                     int spinnerfaultPosition =0;
                     for(int kCount =0; kCount<fId.length; kCount++){
                         if(prevFaultId.equalsIgnoreCase(fId[kCount]) ){
                             spinnerfaultPosition = kCount;

                         }
                     }
                     int spinnerSerialPosition =  faultItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                     addProductFanDetailBinding.spFanFault.setSelection(spinnerSerialPosition);
                 }
                 prevProductStatus = job.getString("ProductStatus");
                 if(prevProductStatus.equalsIgnoreCase("Sold")){
                     addProductFanDetailBinding.radFanSold.setChecked(true);
                     addProductFanDetailBinding.radFanUnSold.setChecked(false);
                 }else if(prevProductStatus.equalsIgnoreCase("Unsold")){
                     addProductFanDetailBinding.radFanSold.setChecked(false);
                     addProductFanDetailBinding.radFanUnSold.setChecked(true);
                 }

                 String DatePurchage = job.getString("DateOfPurchase");
                 addProductFanDetailBinding.etFanFanPurchaseDate.setText(""+DatePurchage);
                 String warrantyStatus = job.getString("WarrantyStatus");
                 addProductFanDetailBinding.etFanWarrantyStatus.setText(""+warrantyStatus);
                 prevClaimStatus = job.getString("ClaimStatus");
                 if(prevClaimStatus.equalsIgnoreCase("Accepted")){
                     addProductFanDetailBinding.radioFanClaimAccepted.setChecked(true);
                     addProductFanDetailBinding.radioFanClaimRejected.setChecked(false);
                 }else if(prevClaimStatus.equalsIgnoreCase("Rejected")){
                     addProductFanDetailBinding.radioFanClaimAccepted.setChecked(false);
                     addProductFanDetailBinding.radioFanClaimRejected.setChecked(true);
                 }

                 String serialNo  = job.getString("SerialNo");
                 addProductFanDetailBinding.etFanSerialNo.setText(""+serialNo);
                 prevDistributorAddress = job.getString("DistributorAddress");

                 prevDistributorName = job.getString("DistributorName");
                 prevDivisionID = job.getString("DivisionID");
                 prevDivisionName = job.getString("DivisionName");
                 prevInvoiceID = job.getString("InvoiceID");

                 if (prevInvoiceID != null) {
                     int spinnerSerialPosition =  serialNoItemSpinnerAdapter .getPosition(prevInvoiceID);
                     addProductFanDetailBinding.spFanInvoice.setSelection(spinnerSerialPosition);
                 }

             } catch (JSONException e) {
                 e.printStackTrace();
             }

            try{
                JSONArray jsonArray = MainObject.getJSONArray("SaveFanDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                job.remove("TotalQuantity");
                job.put("TotalQuantity", "" + quantyTotal);
                job.remove("VisitDate");
                job.put("VisitDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                job.remove("InspectionDate");
                job.put("InspectionDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                job.remove("MaterialName");
                job.put("MaterialName", "" + addProductFanDetailBinding.etMaterialName.getText().toString().trim());
                job.put("MaterialID",addProductFanDetailBinding.etMaterialName.getTag());
                //job.remove("FaultId");
                //job.put("FaultId", "" + selectFaultId);
                job.remove("ProductStatus");
                job.put("ProductStatus", "" + productStatus);
                job.remove("DateOfPurchase");
                job.put("DateOfPurchase", "" + addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim());
                job.remove("WarrantyStatus");
                job.put("WarrantyStatus", "" + fanWarrantyStatus.trim());
               // job.remove("ClaimStatus");
              //  job.put("ClaimStatus", "" + claimStatus);
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
              //   job.remove("InvoiceID");
                //job.put("InvoiceID", "" + selectedSerialNo);
                //adding json objects to json array
              //  MainArray.put(job);
                //adding json array to json object
              //  MainObject.put("SaveFanDetails", MainArray);

            }catch(Exception ex){
                ex.getMessage();
            }


            }else if(jsnCount==startQnty){

             if(mClickFlag.equalsIgnoreCase("Sub")){

             }else{
                 resetFanUI();
             }



            }else { // write

             JSONObject person = new JSONObject();
             try {
                 person.put("TotalQuantity", "" + quantyTotal);
                 person.put("VisitDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                 person.put("InspectionDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                 person.put("MaterialName", "" + addProductFanDetailBinding.etMaterialName.getText().toString().trim());
                 //MaterialID
                 person.put("MaterialID",addProductFanDetailBinding.etMaterialName.getTag());
                 person.put("FaultId", "" + selectFaultId);
                 person.put("ProductStatus", "" + productStatus);
                 person.put("DateOfPurchase", "" + addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim());
                 person.put("WarrantyStatus", "" + addProductFanDetailBinding.etFanWarrantyStatus.getText().toString());
                 person.put("ClaimStatus", "" + claimStatus);
                 person.put("SerialNo", "" + addProductFanDetailBinding.etFanSerialNo.getText().toString().trim());
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

    private void resetFanUI() {
       // getFaultList();
       // setFanData();
        addProductFanDetailBinding.etFanSerialNo.setText("");
        addProductFanDetailBinding.etMaterialName.setText("");
        addProductFanDetailBinding.etFanFanPurchaseDate.setText("");
        addProductFanDetailBinding.etFanWarrantyStatus.setText("");
      //  addProductFanDetailBinding.radFanSold.setChecked(false);
       // addProductFanDetailBinding.radFanUnSold.setChecked(false);
      //  addProductFanDetailBinding.radioFanClaimAccepted.setChecked(false);
      //  addProductFanDetailBinding.radioFanClaimRejected.setChecked(false);
    }

    private boolean isValidate() {
        boolean isChecked = false;
        if(addProductFanDetailBinding.etFanSerialNo.getText().toString().trim().length()>0){
            isChecked = true;
            addProductFanDetailBinding.etFanSerialNo.setError(null);
        }else{
            isChecked = false;
            addProductFanDetailBinding.etFanSerialNo.setError("Scan serial no");
        }

        if(isChecked) {
            if (addProductFanDetailBinding.etMaterialName.getText().toString().trim().length() > 0) {
                isChecked = true;
                addProductFanDetailBinding.etMaterialName.setError(null);
            } else {
                isChecked = false;
                addProductFanDetailBinding.etMaterialName.setError("Enter Material Name");
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
            if (addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim().length() > 0) {
                isChecked = true;
                addProductFanDetailBinding.etFanFanPurchaseDate.setError(null);
            } else {
                isChecked = false;
                addProductFanDetailBinding.etFanFanPurchaseDate.setError("Enter purchage date");
            }
        }

        if(isChecked) {
            if (addProductFanDetailBinding.etFanWarrantyStatus.getText().toString().trim().length() > 0) {
                isChecked = true;
                addProductFanDetailBinding.etFanWarrantyStatus.setError(null);
            } else {
                isChecked = false;
                addProductFanDetailBinding.etFanWarrantyStatus.setError("Enter warranty status");
            }
        }

        if(isChecked) {
            if (addProductFanDetailBinding.radFanSold.isChecked() || addProductFanDetailBinding.radFanUnSold.isChecked()) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Select product status", Toast.LENGTH_LONG).show();
            }
        }

        if(isChecked) {
            if (addProductFanDetailBinding.radioFanClaimAccepted.isChecked() || addProductFanDetailBinding.radioFanClaimRejected.isChecked()) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Select claim status", Toast.LENGTH_LONG).show();
            }
        }
        if(isChecked) {
            if (selectedSerialNo.equalsIgnoreCase("Select Invoice No.")) {

                isChecked = false;
                Toast.makeText(mContext, "Select Invoice No.", Toast.LENGTH_LONG).show();
            } else {
                isChecked = true;
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
                addProductFanDetailBinding.etFanFanPurchaseDate.setText(purchageDate);


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
        datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());

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

                IntentIntegrator.forSupportFragment(AddProductFanDetailFragment.this).initiateScan();

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
                    addProductFanDetailBinding.etFanSerialNo.setText(barCode);
                    addProductFanDetailBinding.etFanSerialNo.setEnabled(false);
                    addProductFanDetailBinding.etFanSerialNo.setFocusable(false);
                    getMaterialList();
                }else{
                    Toast.makeText(getActivity(), "You can't scan same serial no", Toast.LENGTH_SHORT).show();
                }
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
        //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.VISIBLE);
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
                    // addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                    if (entity.getStatus().equalsIgnoreCase("200")) {
                        Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                       /* startQnty = startQnty+1;
                        resetFanUI();*/
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, HomePageFragment.newInstance("", ""), fragmentTag).addToBackStack(null).commit();

                    } else {
                        //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                        Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
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
                        // addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
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
                            //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
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
                            saveData(status_draft_id);
                        }
                    }
                }
            }
        });



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
                           addProductFanDetailBinding.addproductfanvisitlistDeleteImage.setVisibility(View.GONE);
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
//
                if (addProductFanDetailBinding.radFanSold.isChecked()) {
                    productStatus = "Sold";
                } else if (addProductFanDetailBinding.radFanUnSold.isChecked()) {
                    productStatus = "Unsold";
                }
                if (addProductFanDetailBinding.radioFanClaimRejected.isChecked()) {
                    claimStatus = "Rejected";

                } else if (addProductFanDetailBinding.radioFanClaimAccepted.isChecked()) {
                    claimStatus = "Accepted";

                }
                JSONObject person = new JSONObject();
                try {
                    person.put("TotalQuantity", "" + quantyTotal);
                    person.put("VisitDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                    person.put("InspectionDate", "" + addProductFanDetailBinding.etFanInspectionDate.getText().toString().trim());
                    person.put("MaterialName", "" + addProductFanDetailBinding.etMaterialName.getText().toString().trim());
                    //MaterialID
                    person.put("MaterialID",addProductFanDetailBinding.etMaterialName.getTag());
                    person.put("FaultId", "" + selectFaultId);
                    person.put("ProductStatus", "" + productStatus);
                    person.put("DateOfPurchase", "" + addProductFanDetailBinding.etFanFanPurchaseDate.getText().toString().trim());
                    person.put("WarrantyStatus", "" + addProductFanDetailBinding.etFanWarrantyStatus.getText().toString());
                    person.put("ClaimStatus", "" + claimStatus);
                    person.put("SerialNo", "" + addProductFanDetailBinding.etFanSerialNo.getText().toString().trim());
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
                    Toast.makeText(mContext,"Data saved",Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int showQnty21 = startQnty;
                showQnty21 = showQnty21+1;
                if (quantyTotal==showQnty21) {
                    addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                    addProductFanDetailBinding.btnFanNext.setEnabled(false);
                    addProductFanDetailBinding.btnFanNext.setFocusable(false);
                    addProductFanDetailBinding.btnFanNext.setClickable(false);
                }else{
                    addProductFanDetailBinding.btnFanNext.setVisibility(View.VISIBLE);
                    addProductFanDetailBinding.btnFanNext.setEnabled(true);
                    addProductFanDetailBinding.btnFanNext.setFocusable(true);
                    addProductFanDetailBinding.btnFanNext.setClickable(true);
                }
 //
                dialog.dismiss();
            }
        });
    }


    //

}
