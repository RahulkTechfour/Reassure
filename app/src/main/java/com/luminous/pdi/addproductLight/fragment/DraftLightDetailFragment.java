package com.luminous.pdi.addproductLight.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.Interface.OnEditTextChanged;
import com.luminous.pdi.addproductLight.LightRes.FaultList;
import com.luminous.pdi.addproductLight.LightRes.InvoiceQntyBaseResponse;
import com.luminous.pdi.addproductLight.LightRes.InvoiceQntyEntity;
import com.luminous.pdi.addproductLight.LightRes.MaterialLIghtBaseRes;
import com.luminous.pdi.addproductLight.LightRes.MaterialLightRess;
import com.luminous.pdi.addproductLight.LightRes.MaterialList;
import com.luminous.pdi.addproductLight.dto.InvoiceListDto;
import com.luminous.pdi.addproductLight.dto.ItemAdded_view;
import com.luminous.pdi.addproductLight.dto.Items_quanitySHow;
import com.luminous.pdi.addproductLight.dto.Product;
import com.luminous.pdi.addproductLight.dto.RequestView;
import com.luminous.pdi.addproductLight.dto.SoldQuantity;
import com.luminous.pdi.addproduct_fan.dto.SaveStatusBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.StatusListDto;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.client_apis.RetrofitClient;
import com.luminous.pdi.client_apis.RetrofitInterface;
import com.luminous.pdi.client_apis.ServerConfig;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.RecycleerviewLightInvoiceSoldQuantityBinding;
import com.luminous.pdi.draft.DraftDetail;
import com.luminous.pdi.draft.dto.DraftVisitDto;
import com.luminous.pdi.draft.dto.WarrantyBaseResponse;
import com.luminous.pdi.draft.dto.WarrantyDto;
import com.luminous.pdi.draft.fragments.DraftFragment;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.fragments.HomePageFragment;
import com.luminous.pdi.visit_detail.AddProductVisitDetailFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luminous.pdi.core.CommonUtility.getCurrentDate;
import static com.luminous.pdi.home.activities.HomePageActivity.binding;

public class DraftLightDetailFragment extends Fragment implements OnEditTextChanged {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    String selectedMaterialName, selectedMaterialId, selectedSerialNo, productID;
    private List<Items_quanitySHow> movieList = new ArrayList<>();
    private String[] fName;
    private String[] fId;
    JSONObject MainObject = new JSONObject();
    //creating json array
    JSONArray MainArray = new JSONArray();
    boolean isNextClick = false;


    JSONArray MainArrayList = new JSONArray();
    JSONArray MainFormData = new JSONArray();
    LightInvoiceSoldAdapter soldListadapter;
    private ImageView img_back,mivDraftEditImage;
    boolean ispreviousdelete;

    String InvoiceNo, AvailableQuanitity, SoldQuanitity;

    List<InvoiceQntyEntity> invoiceQntySoldList=new ArrayList<>();
    List<InvoiceQntyEntity> invoiceQntySoldLists = new ArrayList<>();

    JSONArray TempMainArrayList = new JSONArray();
    boolean editpage;

    JsonObject gsonObject = new JsonObject();

    String finalJsonData;
   boolean isDeleteFirstIndex=false;
   boolean isDelete=false;

    private String[] fMName;
    private String[] fMId;
    private String[] fMNameMaerialID;

    int status_pending_id, status_confirm_id, status_reject_id, status_completed_id, status_short_closed_id, status_draft_id, status_approval_id, status_delete_id;

    String prevFaultId, prevProductStatus, prevClaimStatus, prevDistributorAddress, prevDistributorName, prevDivisionID, prevDivisionName, prevInvoiceID;
    String PrematerialId;
    int currentIndex=1;


    private TableViewItemAdapter mAdapter;
    int totalQuantity=0;


    int quantyTotal = 0, startQnty = 0, divisionId;
    String distributornname = "";
    String distributoraddress = "";
    String distributerNames = "";

    boolean isPreviousClick = false;

    String fragmentTag = "", Imei;
    private List<ItemAdded_view> itemlist = new ArrayList<>();
    Button mnextbtnone_fragmentsearchvisit;
    boolean sumbitSave;


    private DraftVisitDto visitDetailEntity;
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPrefsManager sharedPrefsManager;
    EditText met_searchvisitfragquantityammount, met_searchfragvisitinspectiondate, metsearchvisit_materialname, mett_searchvisitfragmentquantityammount, met_searchvisitfragbatchno, met_addproductwarrantystatus,
            mett_searchvisitfragmentacceptedquan, met_searchvisitfragquantityhint, mett_addproductselectdate;
    RecyclerView msearchvisitfrag_recyclerviewaddeditem, mInvoiceSoldRecyclerView;
    EditText met_inspectiondate;
    public static Dialog dialog;

    Spinner mfragmentSPinner_faultList, msp_MaterialName;
    Button msearchvisitfrag_addbtn;
    private String state_selected;
    boolean ispreviousMaterial;

    private ArrayList<Product> ItemModelList;
    private ArrayList<SoldQuantity> ItemModeSoldQuantity = new ArrayList<>();

    List<FaultList> faultDataList;
    List<MaterialList> materialDataList;
    LinearLayout msearchvisitfrag_linthree;
    RelativeLayout mfragmentsearchvisit_relthree;
    Button msearchvisitfrag_btnsubmit, mnextbtntwo_fragmentsearchvisit;
    EditText metsecond_addproductquantityammount;
    Button msearchvisitfrag_btnnext;
    String LightWarrantyStatus,fanWarrantyInputSerialNo;
    ArrayList<String> arrPackage=new ArrayList<>();
    String getselectedData;
    int sum_FaultQuanity = 0;
    boolean addField;

    String selectFaultName, selectFaultId;
    JSONObject jObQuantity =new JSONObject();


    String CharseqQuantuty;

    int totalQuantityInvoice;
    boolean shortclose;


    String faultList, selectedDate, selcted_quntity, selcted_batchNo, warranty_status, warranty_accepted_quan,
            warranty_rejected_quan;
    Button msearchvisitfrag_btnprevious;

    ArrayAdapter<CharSequence> allItemSpinnerAdapter;
    ArrayAdapter<CharSequence> mAllItemSpinnerAdapter;
    List<StatusListDto> statusList;
    public static boolean isHome = false;

    String selectMaterialName, selectMaterialId;

    EditText ett_searchvisitfragmentquantityammount;
    boolean isAvailable=false;
    JSONObject jObSecondaryQuantity =new JSONObject();

    private String[] serialNo;
    int sum_AddQantity =0;
    int DistributerId;

    //http://166.62.100.102:6262/Api/PDI_Api/Get_InvoiceQtySold_By_MaterialName?user_id=10763202&Materialname=1200MM AUDIE CEILING FAN BUTTER CREAM&token=2d5abed4e62d56c71889d371f33479c6&app_version=1.0.0&appversion_code=1&device_id=865393040118393&device_name=vivo%201812&os_type=Android&os_version_code=27&os_version_name=8.1.0&ip_address=192.168.1.115&language=EN&screen_name=EN&network_type=LoginActivity&network_operator=wifi&time_captured=&channel=M
    JSONObject InvoiceList = new JSONObject(); //for invoice list data

    public DraftLightDetailFragment() {
        // Required empty public constructor
    }


    public static DraftLightDetailFragment newInstance(String param1, String param2) {
        DraftLightDetailFragment fragment = new DraftLightDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_draf_product_light_detail, container, false);
        Bundle args = getArguments();
        sharedPrefsManager = new SharedPrefsManager(mContext);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        Menu menu = binding.bottomNavigation.getMenu();
        binding.bottomNavigation.getMenu().setGroupCheckable(0, true, false);
        binding.bottomNavigation.setClickable(false);
        fragmentTag = "HomePageFragment";
        binding.bottomNavigation.getMenu().setGroupCheckable(0, false, false);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                binding.bottomNavigation.getMenu().setGroupCheckable(0, true, true);
                String bottomTabNavName = menuItem.getTitle().toString();
                if (HomePageActivity.isLightScreen) {
                    DialogNavigationShow(bottomTabNavName);
                    isHome = true;
                }
                return true;
            }
        });
        try {
            if (args != null) {
                visitDetailEntity = (DraftVisitDto) args.getSerializable("VisitDetail");
                 DistributerId=visitDetailEntity.getDistributorid();
                String data = args.getString("SaveLightDraftDetailData");
                JSONObject jsonObject = new JSONObject(data);
                MainArray = jsonObject.getJSONArray("SaveLightDetails");
                JSONObject jsnObj = MainArray.getJSONObject(0);
                quantyTotal = jsnObj.getInt("TotalQuantity");
                startQnty = (MainArray.length()-1);

                if (MainArray.length()==0){
                    startQnty=0;
                    Log.e("startQnty+++","startQnty+++"+startQnty);
                }else {
                    startQnty = (MainArray.length() - 1);
                    Log.e("startQnty+++Two","startQnty+++Two"+startQnty);

                }

                MainObject.put("SaveLightDetails", MainArray);
                Log.e("DraftDataJson", "" + jsonObject);

                // quantyTotal = Integer.parseInt(args.getString("Quantity"));
                divisionId = visitDetailEntity.getDivisionid();
                distributornname = visitDetailEntity.getDivisionname();
                distributoraddress = visitDetailEntity.getAddress();
                distributerNames = visitDetailEntity.getDistributorname();


            } else {
                Log.w("FanVisitDetail", "Arguments expected, but missing");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getidView(view);
        softKeyBoardHide(mContext, view);
        if (startQnty==0){
            int minimumQuan=startQnty;
            minimumQuan=minimumQuan+1;
            met_searchvisitfragquantityammount.setText(String.valueOf(minimumQuan) + "/" + String.valueOf(quantyTotal));
            mivDraftEditImage.setVisibility(View.GONE);

        }

        met_searchfragvisitinspectiondate.setText(getCurrentDate());
        met_searchvisitfragquantityammount.setFocusable(false);
        met_searchvisitfragquantityammount.setEnabled(false);
        met_searchfragvisitinspectiondate.setEnabled(false);
        met_searchfragvisitinspectiondate.setFocusable(false);

        msearchvisitfrag_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateAddField()) {
                    if (met_addproductwarrantystatus.getText().toString().trim().length() > 0) {
                        met_addproductwarrantystatus.setError(null);
                    } else {
                        met_addproductwarrantystatus.setError("Enter Warranty Status");
                        //   return;
                    }
                    String accepetedQuantity = mett_searchvisitfragmentacceptedquan.getText().toString();
                    String rejectedQuantity = met_searchvisitfragquantityhint.getText().toString();
                    String Quantity = mett_searchvisitfragmentquantityammount.getText().toString(); /*third quantity*/
                    String PDI_QUANITY = metsecond_addproductquantityammount.getText().toString();  /*secondquantity*/
                    try {
                        int result = Integer.parseInt(accepetedQuantity) + Integer.parseInt(rejectedQuantity);
                        if (Integer.parseInt(Quantity) <= Integer.parseInt(PDI_QUANITY)) {
                            if (Integer.parseInt(Quantity) != result) {
                                Toast.makeText(getActivity(), "Quantity should be less than the sum of accepted and rejected quantity ", Toast.LENGTH_SHORT).show();
                                mett_searchvisitfragmentquantityammount.setError("Quantity should be less than the sum of accepted and rejected quantity ");
                            } else {
                                onAddField();
                                mett_searchvisitfragmentquantityammount.setError(null);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Fault Quantity should be less than or equal PDI Quantity", Toast.LENGTH_SHORT).show();
                            mett_searchvisitfragmentquantityammount.setError("Fault Quantity should be less than or equal PDI Quantity");
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter valid Fault details", Toast.LENGTH_SHORT).show();
                }


            }
        });

        msearchvisitfrag_btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumbitSave = true;
                submit_dialog();

            }
        });
        metsecond_addproductquantityammount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    if (!charSequence.equals("")) {
                        if (compareJsonLenth()) {
                            getInvoiceQntyList();
                            ResetonMaterial();
                            sum_FaultQuanity = 0;
                            sum_AddQantity = 0;
                            TempMainArrayList = new JSONArray(new ArrayList<String>());

                        } else {

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mett_addproductselectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPurchageDate();
            }
        });
        try {
            setCustomAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        msearchvisitfrag_btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accepetedQuantity = metsecond_addproductquantityammount.getText().toString();
                try {
                    jObSecondaryQuantity.put("SecondaryQnty", accepetedQuantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isDeleteFirstIndex = false;
                ispreviousMaterial=true;
                isNextClick=true;
                isPreviousClick=false;
                sum_AddQantity=0;
                if (isValidate() && getCheckPdiQuantity()) {
                    int jsonLenth=MainArray.length();

                    if (compareJsonLenth()){
                        if (addField==true){
                            String SECOND_PDI_QUANITY = metsecond_addproductquantityammount.getText().toString();  /*secondquantity*/

                            if (sum_FaultQuanity==Integer.parseInt(SECOND_PDI_QUANITY)){

                            }else {
                                Toast.makeText(getActivity(), "Fault field is not complete Fault field should be equal Material quantity", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }else {
                            sum_FaultQuanity=0;
                            Toast.makeText(getActivity(), "Please fill fault Field Data", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    sum_FaultQuanity=0;
                    if (startQnty < quantyTotal) {
                        startQnty = startQnty + 1;
                        Log.e("getData","getDataBefore"+jsonLenth+"startQuantity"+startQnty);
                        if (jsonLenth>=startQnty){
                            mivDraftEditImage.setVisibility(View.VISIBLE);

                        }else {
                            mivDraftEditImage.setVisibility(View.GONE);

                        }
                        msearchvisitfrag_btnprevious.setBackgroundColor(getResources().getColor((R.color.color_yellow)));
                        msearchvisitfrag_btnprevious.setFocusable(true);
                        saveData();
                        int jsnCount = MainArray.length();
                        if (jsnCount == startQnty) {
                            int totalQuantity=getTotalCount(startQnty);
                            if (totalQuantity == quantyTotal) {

                            } else {
                                resetLightUI();
                            }
                        } else if (jsnCount > startQnty) {
                        }
                    } else {
                        int totalQuantity=getTotalCount(startQnty);
                        if (totalQuantity == quantyTotal) {
                        } else {
                            resetLightUI();
                        }
                        Toast.makeText(mContext, "Form is completed . Please check and save", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        msearchvisitfrag_btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNextClick = false;
                isPreviousClick = true;
                if (startQnty == 0) {
                    msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
                    msearchvisitfrag_btnprevious.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                } else {
                    showPreviousData();
                }
            }
        });

        mivDraftEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jsnCount = MainArray.length();
                if (jsnCount == 0) {
                    mnextbtntwo_fragmentsearchvisit.setBackgroundColor(getResources().getColor(R.color.next));
                    msearchvisitfrag_btnprevious.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                    Toast.makeText(mContext, "Please press NEXT to save this record first", Toast.LENGTH_LONG).show();

                } else {
                    try {
                        JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
                        if (jsonArray.length() == currentIndex) {
                            Toast.makeText(mContext, "Please press NEXT to save this record first", Toast.LENGTH_LONG).show();

                        } else {
                            delete_dialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mnextbtnone_fragmentsearchvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selectedMaterialId != null &&
                            !selectedMaterialId.equalsIgnoreCase("0")) {
                    } else {
                        Toast.makeText(mContext, "please select a material", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (metsecond_addproductquantityammount.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(),
                            "You Can't enter 0 Quantity,Please enter valid quantity", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String accepetedQuantity = metsecond_addproductquantityammount.getText().toString();
                try {
                    jObSecondaryQuantity.put("SecondaryQnty", accepetedQuantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*chnaged by shalu on 2 aprail*/
                if (!accepetedQuantity.equals("")) {
                    int secondQuantity = Integer.parseInt(accepetedQuantity);
                    if (secondQuantity <= quantyTotal) {
                        String accepetedQuantitySaveS = metsecond_addproductquantityammount.getText().toString();
                        int secondQuantitySaves = Integer.parseInt(accepetedQuantitySaveS);
                        int totalQuantityInvoice = 0;
                        totalQuantityInvoice = totalQuantityInvoice + secondQuantitySaves;
                        if (quantyTotal >= totalQuantityInvoice) {

                        } else {
                            Toast.makeText(getActivity(),
                                    "PDI quantity should be less than or equal to total PDI quantity", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        getInvoiceQntyList();
                        msearchvisitfrag_linthree.setVisibility(View.VISIBLE);
                        metsecond_addproductquantityammount.setError(null);
                    } else {
                        Toast.makeText(getActivity(), "PDI Quantity should be less than or equal to quantity", Toast.LENGTH_SHORT).show();
                        metsecond_addproductquantityammount.setError("PDI Quantity should be less than or equal to quantity");
                    }
                }
            }
        });
        mInvoiceSoldRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mInvoiceSoldRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (Build.VERSION.SDK_INT >= 21)
            mInvoiceSoldRecyclerView.setNestedScrollingEnabled(true);
            mfragmentsearchvisit_relthree.setVisibility(View.GONE);

        mnextbtntwo_fragmentsearchvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accepetedQuantitySave = metsecond_addproductquantityammount.getText().toString();
                if (accepetedQuantitySave.equalsIgnoreCase("")) {
                    return;
                }
                int secondQuantitySave = Integer.parseInt(accepetedQuantitySave);
                int totalCountShow = getTotalCount(startQnty-1);
                totalQuantity = totalCountShow + secondQuantitySave;
                if (quantyTotal >= totalQuantity) {
                } else {
                    Toast.makeText(getActivity(),
                            "PDI quantity should be less than or equal to total PDI quantity", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                try {
                    sum_AddQantity = 0;
                    String EnterIndividualQuantity = CharseqQuantuty;
                    for (int i = 0; i < TempMainArrayList.length(); i++) {
                        JSONObject jobInvoiceData = (JSONObject) TempMainArrayList.get(i);
                        String PDIQnty_ = jobInvoiceData.getString("PDIQnty");
                        if (!PDIQnty_.equalsIgnoreCase("")) {
                            sum_AddQantity += Integer.parseInt(PDIQnty_);
                            Log.e("getValue+sum_AddQantity", "sum_AddQantity" + sum_AddQantity);
                            mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);

                        } else {
                            try {
                                sum_AddQantity = 0;
                                Toast.makeText(getActivity(), "Please enter Invoice Quantity", Toast.LENGTH_SHORT).show();
                                mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String SecondPdiQuantity = metsecond_addproductquantityammount.getText().toString();  /*secondquantity*/
                    if (jObQuantity.get("AvailableQnty") != null && jObQuantity.get("AvailableQnty").equals(null)) {
                    } else {
                        String avaialbleQuantity = String.valueOf(jObQuantity.get("AvailableQnty"));
                        Integer avalaibleresultQuantSec = Integer.valueOf((int) Math.round(Float.parseFloat(avaialbleQuantity)));
                        try {
                            if (Integer.parseInt(EnterIndividualQuantity) <= avalaibleresultQuantSec) {
                                if (Integer.parseInt(EnterIndividualQuantity) <= quantyTotal) {
                                    mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);
                                    if (sum_AddQantity == Integer.parseInt(SecondPdiQuantity)) {
                                        mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);
                                    } else {
                                        sum_AddQantity = 0;
                                        mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), "Sun of Invoice quantity should be equal PDI quantity ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    sum_AddQantity = 0;
                                    mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(), CharseqQuantuty.toString() + "should equal" + quantyTotal, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                sum_AddQantity = 0;
                                mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), CharseqQuantuty.toString() + "should not be greator than or less than" + avalaibleresultQuantSec, Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
        mfragmentSPinner_faultList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        msp_MaterialName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if ((isPreviousClick == true) || (isNextClick == true)) {
                        isNextClick = false;
                        isPreviousClick = false;
                        selectedMaterialId = fMId[position];
                        return;
                    } else {
                        if (materialDataList != null) {
                            if (position > 0) {
                                selectedMaterialName = fMName[position];
                                selectedMaterialId = fMId[position];
                                productID = fMNameMaerialID[position];
                                if (compareJsonLenth()) {
                                    metsecond_addproductquantityammount.setText("");
                                    getInvoiceQntyList();
                                    ResetonMaterial();
                                    sum_FaultQuanity = 0;
                                    sum_AddQantity = 0;
                                    TempMainArrayList = new JSONArray(new ArrayList<String>());
                                } else {
                                }
                                int jsnCount = MainArray.length();
                                if (jsnCount != 0) {
                                    if (isValidMaterialName()) {
                                        mnextbtnone_fragmentsearchvisit.setClickable(true);
                                    } else {
                                        mnextbtnone_fragmentsearchvisit.setClickable(false);
                                        if (compareJsonLenth()){
                                            Toast.makeText(getContext(), "Please select Unique Material ID", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                selectedMaterialId = fMId[position];
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                    ex.getMessage();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getMaterialList();
        getStatusList();
        setCurrentdata();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = DraftFragment.newInstance("", "");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void DialogNavigationShow(String bottomTabNavName) {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_preserve_data_layout);
        TextView tvCancel = dialog.findViewById(R.id.tvPreserveCancel);
        TextView tvOK = dialog.findViewById(R.id.tvPreserveOK);
        TextView tvText = dialog.findViewById(R.id.dialogmessage);
        tvText.setText("If you exit the form your data will be lost");
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
                Fragment fragment = null;

                if (bottomTabNavName.equalsIgnoreCase("Dashboard")) {
                    //  isLightScreen=false;

                    isHome = true;
                    fragment = HomePageFragment.newInstance("", "");
                    fragmentTag = "HomePageFragment";
                } else if (bottomTabNavName.equalsIgnoreCase("Pending")) {
                    //isLightScreen=false;
                    isHome = true;
                    fragment = AttendenceFragment.newInstance("", "");
                    fragmentTag = "PendingFragment";
                } else if (bottomTabNavName.equalsIgnoreCase("Draft")) {
                    //isLightScreen=false;
                    isHome = true;

                    fragment = DraftFragment.newInstance("", "");
                    fragmentTag = "DraftFragment";
                } else if (bottomTabNavName.equalsIgnoreCase("Completed")) {
                    //isLightScreen=false;
                    isHome = true;

                    fragment = CompletedFragment.newInstance("", "");
                    fragmentTag = "CompletedFragment";
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, fragmentTag).addToBackStack(null).commit();
                dialog.dismiss();
            }
        });
    }

    private boolean isValidMaterialName() {
        boolean materialFlag = true;
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = (JSONObject) jsonArray.get(i);
                PrematerialId = job.getString("MaterialID");
                if (productID.equalsIgnoreCase(PrematerialId)) {
                    materialFlag = false;
                } else if (materialFlag) {
                    materialFlag = true;

                }
            }
        } catch (Exception e) {
        }
        return materialFlag;
    }
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

                isDelete = true;
                if (startQnty > currentIndex) {
                    startQnty = startQnty - 1;
                }
                try {
                    jsonArray = MainObject.getJSONArray("SaveLightDetails");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        if (startQnty == 0) {
                            isDeleteFirstIndex = true;
                        } else {
                            isDeleteFirstIndex = false;
                        }
                        jsonArray.remove(startQnty);
                        currentIndex--;
                        if (currentIndex == -1) {
                            resetLightUI();
                            mivDraftEditImage.setVisibility(View.GONE);
                        } else {
                            showPreviousData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }
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


        tvShortClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int selected_item;
                if (shortclose==true) {
                    if (statusList != null && statusList.size() > 0) {
                        if (statusList != null && statusList.size() > 0) {
                            for (int kCount = 0; kCount < statusList.size(); kCount++) {
                                if (statusList.get(kCount).getName().equalsIgnoreCase("SHORT CLOSE")) {
                                    selected_item = statusList.get(kCount).getID();
                                    // saveData();
                                    hitSaveAPI(status_short_closed_id);
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "You can short-close this visit only if  your inspected quantity is less than PDI quantity.", Toast.LENGTH_SHORT).show();

                }


            }
        });
        Log.e("quantyTotal","quantyTotal"+quantyTotal+"totalQuantity"+totalQuantity);

        if (quantyTotal == totalQuantity) {
            tvCompleted.setFocusable(true);
            tvCompleted.setClickable(true);
            tvCompleted.setTextColor(getResources().getColor(R.color.completed));

            tvShortClosed.setFocusable(false);
            tvShortClosed.setClickable(false);
            tvShortClosed.setTextColor(Color.GRAY);
            shortclose=false;
        } else {
            tvCompleted.setFocusable(false);
            tvCompleted.setClickable(false);
            tvCompleted.setTextColor(Color.GRAY);

            tvShortClosed.setFocusable(true);
            tvShortClosed.setClickable(true);
            tvShortClosed.setTextColor(getResources().getColor(R.color.shortClose));

            shortclose =true;
        }


        tvDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int selected_item;
                if (statusList != null && statusList.size() > 0) {
                    for (int kCount = 0; kCount < statusList.size(); kCount++) {
                        if (statusList.get(kCount).getName().equalsIgnoreCase("DRAFT")) {
                            selected_item = statusList.get(kCount).getID();
                            // saveData();

                            hitSaveAPI(status_draft_id);
                        }
                    }
                }
            }
        });


        tvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (quantyTotal == totalQuantity) {
                    int selected_item;
                    if (statusList != null && statusList.size() > 0) {
                        if (statusList != null && statusList.size() > 0) {
                            for (int kCount = 0; kCount < statusList.size(); kCount++) {
                                if (statusList.get(kCount).getName().equalsIgnoreCase("COMPLETED")) {
                                    selected_item = statusList.get(kCount).getID();
                                    hitSaveAPI(status_completed_id);
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "You can complete this visit only if your inspected quantity is same as the PDI quantity.", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
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
        String url = String.format(ServerConfig.getSaveStatusListUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(mContext), "EN", "LoginActivity", CommonUtility.getNetworkType(mContext), CommonUtility.getNetworkOperator(mContext), "" + System.currentTimeMillis(), "M", "");
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
                        if (entity.getStatus() != null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                statusList = entity.getStatusList();
                                if (statusList != null && statusList.size() > 0) {

                                    for (int iCount = 0; iCount < statusList.size(); iCount++) {
                                        String statusName = statusList.get(iCount).getName();
                                        if (statusName.equalsIgnoreCase("PENDING")) {
                                            status_pending_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("CONFIRM")) {
                                            status_confirm_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("REJECT")) {
                                            status_reject_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("COMPLETED")) {
                                            status_completed_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("SHORT CLOSE")) {
                                            status_short_closed_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("DRAFT")) {
                                            status_draft_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("APPROVAL")) {
                                            status_approval_id = statusList.get(iCount).getID();
                                        } else if (statusName.equalsIgnoreCase("DELETE")) {
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
    private void showPreviousData() {
        int jsoncount = MainArray.length();

        if (jsoncount >= startQnty) {
            mivDraftEditImage.setVisibility(View.VISIBLE);

        } else {
            mivDraftEditImage.setVisibility(View.GONE);

        }
        if (isDeleteFirstIndex == false) {
            if (currentIndex == 0 && startQnty == 1) {  /*added on first index and go to the previous*/
                startQnty = startQnty - 1;
            } else {
                if (startQnty > currentIndex) {
                    if (isDelete == true) {
                      //  startQnty = startQnty - 1;
                        startQnty = startQnty - 1;

                    } else {
                        if (compareJsonLenth()){
                            startQnty--; /*this method change by shalu*/
                            startQnty = startQnty - 1;  /*this method change by shalu*/
                            Log.e("getData","getData++"+startQnty);
                            /*0*/  /*current index in place of startq*/
                        }else{
                            startQnty--;
                            Log.e("getData","getData"+startQnty);
                        }
                    }
                } else {
                    startQnty = startQnty - 1;
                }
            }
        }
        if (isDelete) {

        } else {
          //  UpDatedEditData(startQnty + 1);

        }

        ispreviousdelete = true;
        ispreviousMaterial = true;
        if (startQnty == 0) {
            msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
            msearchvisitfrag_btnprevious.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
            msearchvisitfrag_btnprevious.setEnabled(true);
            msearchvisitfrag_btnnext.setEnabled(true);
        } else {

            int TotalQuantity = getTotalCount(startQnty);
            if (TotalQuantity == quantyTotal) {
                msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                msearchvisitfrag_btnnext.setEnabled(false);

            } else {
                msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
                msearchvisitfrag_btnnext.setEnabled(true);
            }
        }
        resetPreviousLightUI();
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            String SecondaryQnty = job.getString("SecondaryQnty");
            String FisrtQnty = job.getString("FirstQnty");
            //met_searchvisitfragquantityammount.setText(String.valueOf(FisrtQnty));
            metsecond_addproductquantityammount.setText(SecondaryQnty);

            int totalCountShow = getTotalCount(startQnty);
            met_searchvisitfragquantityammount.setText(String.valueOf(totalCountShow) + "/" + String.valueOf(quantyTotal));
            String inspDate = job.getString("InspectionDate");
            met_searchfragvisitinspectiondate.setText("" + inspDate);
            metsecond_addproductquantityammount.setText("" + metsecond_addproductquantityammount.getText().toString().trim());
            JSONArray jsonArrayFormArray = job.getJSONArray("FormData");
            mfragmentsearchvisit_relthree.setVisibility(View.GONE);
            for (int i = 0; i < jsonArrayFormArray.length(); i++) {
                JSONObject jobFormData = (JSONObject) jsonArrayFormArray.get(i);
                String availableQuantity = jobFormData.getString("FaultQnty");
                String BatchNo = jobFormData.getString("FaultBatchNo");
                String productDate = jobFormData.getString("FaultMFG");
                String WarrantyStatus = jobFormData.getString("FaultWarranty");
                String accept = jobFormData.getString("FaultAccept");
                String reject = jobFormData.getString("FaultReject");
                prevFaultId = jobFormData.getString("FaultID");
                String prevFaultName = jobFormData.getString("FaultName"); /*should select fault */

/*
                if (prevFaultId != null) {
                    int spinnerfaultPosition = 0;
                    for (int kCount = 0; kCount < fId.length; kCount++) {
                        if (prevFaultId.equalsIgnoreCase(fId[kCount])) {
                            spinnerfaultPosition = kCount;

                        }
                    }
                    int spinnerSerialPosition = allItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                    mfragmentSPinner_faultList.setSelection(spinnerSerialPosition);
                }*/
                Product md = new Product(prevFaultName, productDate
                        , availableQuantity, BatchNo, WarrantyStatus, accept, reject);
                // ItemModelList = new ArrayList<>();

                ItemModelList.add(md);
            }
            msearchvisitfrag_recyclerviewaddeditem.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new TableViewItemAdapter(getActivity(), ItemModelList);
            msearchvisitfrag_recyclerviewaddeditem.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            JSONArray jsonArraInoiceListArray = job.getJSONArray("InvoiceList");
            invoiceQntySoldLists = new ArrayList<>();

            for (int i = 0; i < jsonArraInoiceListArray.length(); i++) {
                JSONObject jobInvoiceData = (JSONObject) jsonArraInoiceListArray.get(i);
                String InvoiceNo_ = jobInvoiceData.getString("InvoiceNo");
                String availableQuantity_ = jobInvoiceData.getString("AvailableQnty");
                String PDIQnty_ = jobInvoiceData.getString("PDIQnty");
                InvoiceQntyEntity md = new InvoiceQntyEntity(InvoiceNo_, availableQuantity_, PDIQnty_);
                invoiceQntySoldLists.add(md);
            }
            soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldLists, this);
            mInvoiceSoldRecyclerView.setAdapter(soldListadapter);
            soldListadapter.notifyDataSetChanged();

            PrematerialId = job.getString("MaterialID");
            if (PrematerialId != null) {
                int spinnerfaultPosition = 0;
                for (int kCount = 0; kCount < fMNameMaerialID.length; kCount++) {
                    if (PrematerialId.equalsIgnoreCase(fMNameMaerialID[kCount])) {
                        spinnerfaultPosition = kCount;
                    }
                }
                int spinnerSerialPosition = mAllItemSpinnerAdapter.getPosition(fMName[spinnerfaultPosition]);
                msp_MaterialName.setSelection(spinnerSerialPosition);
            }
            prevDistributorAddress = job.getString("DistributorAddress");
            prevDistributorName = job.getString("DistributorName");
            prevDivisionID = job.getString("DivisionID");
            prevDivisionName = job.getString("DivisionName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //   isNextClick=false;
        //  isPreviousClick=false;
    }
    private void resetPreviousLightUI() {
        mett_addproductselectdate.setText("");
        metsecond_addproductquantityammount.setText("");
        mett_searchvisitfragmentquantityammount.setText("");
        met_searchvisitfragbatchno.setText("");
        met_addproductwarrantystatus.setText("");
        mett_searchvisitfragmentacceptedquan.setText("");
        met_searchvisitfragquantityhint.setText("");
        MainFormData = new JSONArray(new ArrayList<String>());
        TempMainArrayList = new JSONArray(new ArrayList<String>());
        invoiceQntySoldList.clear();
        ItemModelList.clear();
        mAdapter.notifyDataSetChanged();
        soldListadapter.notifyDataSetChanged();
        if (currentIndex == -1) {
            int minimumQuan = startQnty;
            minimumQuan = minimumQuan + 1;
            met_searchvisitfragquantityammount.setText(String.valueOf(minimumQuan) + "/" + String.valueOf(quantyTotal));
            metsecond_addproductquantityammount.setText("");
            msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
            msearchvisitfrag_btnnext.setEnabled(true);
        }
    }
    private void resetLightUI() {
        mett_addproductselectdate.setText("");
        metsecond_addproductquantityammount.setText("");
        mett_searchvisitfragmentquantityammount.setText("");
        met_searchvisitfragbatchno.setText("");
        met_addproductwarrantystatus.setText("");
        mett_searchvisitfragmentacceptedquan.setText("");
        met_searchvisitfragquantityhint.setText("");
        MainFormData = new JSONArray(new ArrayList<String>());
        TempMainArrayList = new JSONArray(new ArrayList<String>());

        invoiceQntySoldList.clear();
        try {
            invoiceQntySoldLists.clear();
            soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldLists, this);
            mInvoiceSoldRecyclerView.setAdapter(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemModelList.clear();
        materialDataList.clear();
        setMaterialSearchUI(materialDataList);
        mAllItemSpinnerAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
        soldListadapter.notifyDataSetChanged();
        if (currentIndex == -1) {
            int minimumQuan = startQnty;
            minimumQuan = minimumQuan + 1;
            met_searchvisitfragquantityammount.setText(String.valueOf(minimumQuan) + "/" + String.valueOf(quantyTotal));
            metsecond_addproductquantityammount.setText("");
            msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
            msearchvisitfrag_btnnext.setEnabled(true);
        }
    }
    private void softKeyBoardHide(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private boolean isValidateAddField() {
        boolean isChecked = false;
        try {
            if (selectFaultId != null && !selectFaultId.equalsIgnoreCase("0")) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Please select Fault Name", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isChecked){
            if (!mett_searchvisitfragmentquantityammount.getText().toString().equals(null)
                    && !mett_searchvisitfragmentquantityammount.getText().toString().equals("0")
                    && !mett_searchvisitfragmentquantityammount.getText().toString().equals("")
            ) {
                isChecked = true;
                mett_searchvisitfragmentquantityammount.setError(null);
            } else {
                isChecked = false;
                mett_searchvisitfragmentquantityammount.setError("Please Enter Quantity above 0");
            }
        }



        if (isChecked) {
            if (met_searchvisitfragbatchno.getText().toString().trim().length() > 0 || met_searchvisitfragbatchno.getText().toString().equals(null)) {
                isChecked = true;
                met_searchvisitfragbatchno.setError(null);
            } else {
                isChecked = false;
                met_searchvisitfragbatchno.setError("Please Enter Batch No");
            }
        }

        if (isChecked) {
            if (mett_addproductselectdate.getText().toString().trim().length() > 0 || mett_addproductselectdate.getText().toString().equals(null)) {
                isChecked = true;
                mett_addproductselectdate.setError(null);
            } else {
                isChecked = false;
                mett_addproductselectdate.setError("Enter Date");
            }
        }

        if (isChecked) {
            if (met_addproductwarrantystatus.getText().toString().trim().length() > 0) {
                isChecked = true;
                met_addproductwarrantystatus.setError(null);
            } else {
                isChecked = false;
                met_addproductwarrantystatus.setError("Enter Warranty Status");
            }
        }


        if (isChecked) {
            if (mett_searchvisitfragmentacceptedquan.getText().toString().trim().length() > 0) {
                isChecked = true;
                mett_searchvisitfragmentacceptedquan.setError(null);
            } else {
                isChecked = false;
                mett_searchvisitfragmentacceptedquan.setError("Enter Accepted Quantity");
            }
        }
        if (isChecked) {
            if (met_searchvisitfragquantityhint.getText().toString().trim().length() > 0) {
                isChecked = true;
                met_searchvisitfragquantityhint.setError(null);
            } else {
                isChecked = false;
                met_searchvisitfragquantityhint.setError("Enter Rejected Quantity");
            }
        }

        return isChecked;
    }
    private boolean isValidate() {
        boolean isChecked = false;

        try {
            if (selectedMaterialId != null && !selectedMaterialId.equalsIgnoreCase("0")) {
                isChecked = true;
            } else {
                isChecked = false;
                Toast.makeText(mContext, "Please select material", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isChecked) {
            if (metsecond_addproductquantityammount.getText().toString().trim().length() > 0 || metsecond_addproductquantityammount.getText().toString().equals(null)) {
                isChecked = true;
                metsecond_addproductquantityammount.setError(null);
            } else {

                isChecked = false;
                metsecond_addproductquantityammount.setError("Please Enter Quantity");

            }
        }

        if (compareJsonLenth()){
            if (isChecked) {
                if (selectFaultId != null || selectFaultId != null) {
                    isChecked = true;
                } else {
                    isChecked = false;
                    Toast.makeText(mContext, "Select Fault", Toast.LENGTH_LONG).show();
                    isNextClick = false;

                }
            }
        }



        return isChecked;
    }
    private void saveData() {
        int jsnCount = MainArray.length();
        if (jsnCount > startQnty) { // read
            ispreviousMaterial = true;
            try {
                JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                String SecondaryQnty = job.getString("SecondaryQnty");
                metsecond_addproductquantityammount.setText(String.valueOf(SecondaryQnty));
                String FisrtQnty = job.getString("FirstQnty");
                //  met_searchvisitfragquantityammount.setText(String.valueOf(FisrtQnty));
                int totalCountShow = getTotalCount(startQnty);
                met_searchvisitfragquantityammount.setText(String.valueOf(totalCountShow) + "/" + String.valueOf(quantyTotal));
                String spilitTotalQuan[] = FisrtQnty.split("/");
                int spilitTotalQuanNew = Integer.parseInt(spilitTotalQuan[0]);

                // if (totalCountShow == quantyTotal) {
                //  if (spilitTotalQuanNew == quantyTotal) {
                if (totalCountShow == quantyTotal) {
                    msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                    msearchvisitfrag_btnnext.setEnabled(false);
                    Toast.makeText(mContext, "Form is completed . Please check and save", Toast.LENGTH_LONG).show();
                } else {
                    msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
                    msearchvisitfrag_btnnext.setEnabled(true);
                }
                String inspDate = job.getString("InspectionDate");
                met_searchfragvisitinspectiondate.setText("" + inspDate);
                metsecond_addproductquantityammount.setText("" + metsecond_addproductquantityammount.getText().toString().trim());
                JSONArray jsonArrayFormArray = job.getJSONArray("FormData");
                mfragmentsearchvisit_relthree.setVisibility(View.GONE);
                ItemModelList.clear();//added by shalu on 26 aprail
                for (int i = 0; i < jsonArrayFormArray.length(); i++) {
                    JSONObject jobFormData = (JSONObject) jsonArrayFormArray.get(i);
                    String availableQuantity = jobFormData.getString("FaultQnty");
                    String BatchNo = jobFormData.getString("FaultBatchNo");
                    String productDate = jobFormData.getString("FaultMFG");
                    String WarrantyStatus = jobFormData.getString("FaultWarranty");
                    String accept = jobFormData.getString("FaultAccept");
                    String reject = jobFormData.getString("FaultReject");
                    prevFaultId = jobFormData.getString("FaultID");
                    String prevFaultName = jobFormData.getString("FaultName");

                 /*   if (prevFaultId != null) {
                        int spinnerfaultPosition = 0;
                        for (int kCount = 0; kCount < fId.length; kCount++) {
                            if (prevFaultId.equalsIgnoreCase(fId[kCount])) {
                                spinnerfaultPosition = kCount;
                            }
                        }
                        int spinnerSerialPosition = allItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                        mfragmentSPinner_faultList.setSelection(spinnerSerialPosition);
                    }*/
                    Product md = new Product(prevFaultName, productDate
                            , availableQuantity, BatchNo, WarrantyStatus, accept, reject);
                   // ItemModelList = new ArrayList<>();
                    ItemModelList.add(md);
                }
                msearchvisitfrag_recyclerviewaddeditem.setLayoutManager(new LinearLayoutManager(getActivity()));
                mAdapter = new TableViewItemAdapter(getActivity(), ItemModelList);
                msearchvisitfrag_recyclerviewaddeditem.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                JSONArray jsonArraInoiceListArray = job.getJSONArray("InvoiceList");
                invoiceQntySoldLists = new ArrayList<>();
                for (int i = 0; i < jsonArraInoiceListArray.length(); i++) {
                    JSONObject jobInvoiceData = (JSONObject) jsonArraInoiceListArray.get(i);
                    String InvoiceNo_ = jobInvoiceData.getString("InvoiceNo");
                    String availableQuantity_ = jobInvoiceData.getString("AvailableQnty");
                    String PDIQnty_ = jobInvoiceData.getString("PDIQnty");
                    InvoiceQntyEntity md = new InvoiceQntyEntity(InvoiceNo_, availableQuantity_, PDIQnty_);
                    invoiceQntySoldLists.add(md);
                }
                soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldLists, this);
                mInvoiceSoldRecyclerView.setAdapter(soldListadapter);
                soldListadapter.notifyDataSetChanged();
                PrematerialId = job.getString("MaterialID");
                if (PrematerialId != null) {
                    int spinnerfaultPosition = 0;
                    for (int kCount = 0; kCount < fMNameMaerialID.length; kCount++) {
                        if (PrematerialId.equalsIgnoreCase(fMNameMaerialID[kCount])) {
                            spinnerfaultPosition = kCount;
                        }
                    }
                    int spinnerSerialPosition = mAllItemSpinnerAdapter.getPosition(fMName[spinnerfaultPosition]);
                    msp_MaterialName.setSelection(spinnerSerialPosition);
                }
                prevDistributorAddress = job.getString("DistributorAddress");
                prevDistributorName = job.getString("DistributorName");
                prevDivisionID = job.getString("DivisionID");
                prevDivisionName = job.getString("DivisionName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
                JSONObject job = (JSONObject) jsonArray.get(startQnty);
                job.remove("TotalQuantity");
                job.put("TotalQuantity", "" + quantyTotal);
                //  totalQuantity=0;
                job.remove("InspectionDate");
                job.put("InspectionDate", "" + met_searchfragvisitinspectiondate.getText().toString().trim());
                job.remove("FaultID");
                job.put("FaultID", "" + selectFaultId);
                job.remove("FaultQnty");
                job.put("FaultQnty", "" + ett_searchvisitfragmentquantityammount.getText().toString().trim());
                job.remove("FaultMFG");
                job.put("FaultMFG", "" + mett_addproductselectdate.getText().toString().trim());
                job.remove("FaultWarranty");
                job.put("FaultWarranty", "" + met_addproductwarrantystatus.getText().toString().trim());
                job.remove("FaultAccept");
                job.put("FaultAccept", "" + mett_searchvisitfragmentacceptedquan.getText().toString().trim());
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

            } catch (Exception ex) {
                ex.getMessage();
            }
        } else if (MainArray.length() == startQnty) {
            Log.e("getData" + MainArray.length(), "startQnty" + startQnty + "totalQuantity" + totalQuantity);
            mivDraftEditImage.setVisibility(View.GONE);

        } else {
            /*on third next press so it will increase the jsoncount*/
            if (jsnCount == 1 && startQnty == 1 && currentIndex == 0 && ispreviousMaterial == true) {
                if (totalQuantity == quantyTotal) {
                    msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                    msearchvisitfrag_btnnext.setEnabled(false);
                    Toast.makeText(mContext, "Form is completed . Please check and save", Toast.LENGTH_LONG).show();
                } else {
                    msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
                    msearchvisitfrag_btnnext.setEnabled(true);
                }
                return;
            } else {

            }
            JSONObject person = new JSONObject(); // for add the data
            if (currentIndex == -1) {
                String accepetedQuantitySave = metsecond_addproductquantityammount.getText().toString(); /*second filed*/
                int secondQuantitySave = Integer.parseInt(accepetedQuantitySave);
                met_searchvisitfragquantityammount.setText(String.valueOf(secondQuantitySave) + "/" + String.valueOf(quantyTotal));
                totalQuantity = secondQuantitySave;

            } else {
                String accepetedQuantitySave = metsecond_addproductquantityammount.getText().toString();
                int secondQuantitySave = Integer.parseInt(accepetedQuantitySave);
                int totalCountShow = getTotalCount(startQnty);
                totalQuantity = totalCountShow + secondQuantitySave;
                if (quantyTotal >= totalQuantity) {
                    met_searchvisitfragquantityammount.setText(String.valueOf(totalQuantity) + "/" + String.valueOf(quantyTotal));

                } else {
                    Toast.makeText(getActivity(),
                            "PDI quantity should be less than or equal to total PDI quantity", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

            }
            currentIndex++;
            String accepetedQuantity = metsecond_addproductquantityammount.getText().toString();
            int secondQuantity = Integer.parseInt(accepetedQuantity);
            String FirstQuantity = met_searchvisitfragquantityammount.getText().toString(); /*second filed*/
            if (totalQuantity == quantyTotal) {
                msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.currentMonthDayColor));
                msearchvisitfrag_btnnext.setEnabled(false);
                Toast.makeText(mContext, "Form is completed . Please check and save", Toast.LENGTH_LONG).show();
            } else {
                msearchvisitfrag_btnnext.setBackgroundColor(getResources().getColor(R.color.next));
                msearchvisitfrag_btnnext.setEnabled(true);
            }
            try {
                person.put("TotalQuantity", "" + quantyTotal);
                person.put("DistributorAddress", "" + distributoraddress);
                person.put("DistributorName", "" + distributerNames);
                person.put("DivisionID", "" + divisionId);
                person.put("DivisionName", "" + distributornname);
                person.put("InspectionDate", "" + met_inspectiondate.getText().toString());
                person.put("MaterialID", "" + productID);
                person.put("MaterialName", "" + selectedMaterialName);
                person.put("FormData", MainFormData);
                person.put("InvoiceList", TempMainArrayList);
                person.put("SecondaryQnty", secondQuantity);
                person.put("FirstQnty", FirstQuantity);
                //adding json objects to json array
                MainArray.put(person);
                //adding json array to json object
                MainObject.put("SaveLightDetails", MainArray);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(MainObject.toString());
                finalJsonData = MainObject.toString();
                Log.d("page json ", finalJsonData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // isNextClick=false;
        //isPreviousClick=false;
    }
    private void UpDatedEditData(int startQnty) {
        String accepetedQuantitys = metsecond_addproductquantityammount.getText().toString();
        metsecond_addproductquantityammount.isInEditMode();
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            JSONObject jobOne = (JSONObject) MainArray.get(startQnty);
            if (accepetedQuantitys.equalsIgnoreCase("")) {

            } else {
                int secondQuantitySaves = Integer.parseInt(accepetedQuantitys);
                job.put("SecondaryQnty", secondQuantitySaves);
                jobOne.put("SecondaryQnty", secondQuantitySaves);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getInvoiceQntyList() {
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
        String selectedMaterialNames = selectedMaterialName;
        String url = String.format(ServerConfig.getInvoiceQntyListUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), selectedMaterialNames, sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(mContext), "EN", "LoginActivity", CommonUtility.getNetworkType(mContext), CommonUtility.getNetworkOperator(mContext), "" + System.currentTimeMillis(), "M", "");
        apiInterface.getInvoiceQntyListData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InvoiceQntyBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(InvoiceQntyBaseResponse entity) {

                        if (entity.getStatus().equalsIgnoreCase("200")) {

                            invoiceQntySoldList = entity.getGet_invoice_qty_sold();
                            if (invoiceQntySoldList != null && invoiceQntySoldList.size() > 0) {
                                setInvoiceQntySoldUI(invoiceQntySoldList);
                            }
                        }else {
                            if (compareJsonLenth()) {
                                metsecond_addproductquantityammount.setText("");
                                ResetonMaterial();
                                sum_FaultQuanity = 0;
                                sum_AddQantity = 0;
                                invoiceQntySoldList.clear();
                                TempMainArrayList = new JSONArray(new ArrayList<String>());
                                soldListadapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "No invoice data found", Toast.LENGTH_SHORT).show();
                            } else {

                            }
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
    private void setInvoiceQntySoldUI(List<InvoiceQntyEntity> invoiceQntySoldList) {
        try {
            for (int i = 0; i < invoiceQntySoldList.size(); i++) {
                JSONObject TempInvoiceListQuantity = new JSONObject(); //for invoice list data
                TempInvoiceListQuantity.put("InvoiceNo", invoiceQntySoldList.get(i).getBillingDocumentNumner());
                TempInvoiceListQuantity.put("AvailableQnty", invoiceQntySoldList.get(i).getAvailableQuantity());
                TempInvoiceListQuantity.put("PDIQnty", "0");
                TempMainArrayList.put(TempInvoiceListQuantity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldList, this);
        mInvoiceSoldRecyclerView.setAdapter(soldListadapter);
        soldListadapter.notifyDataSetChanged();


        // InvoiceListAdapter invoiceListAdapter=new InvoiceListAdapter(mContext,invoiceQntySoldList);
        // mInvoiceSoldRecyclerView.setAdapter(invoiceListAdapter);


    }
    private void getidView(View view) {
        met_searchvisitfragquantityammount = view.findViewById(R.id.Draftet_searchvisitfragquantityammount);
        met_searchfragvisitinspectiondate = view.findViewById(R.id.Draftet_searchfragvisitinspectiondate);
        img_back=view.findViewById(R.id.addproductlight_backimage);
        mivDraftEditImage=view.findViewById(R.id.ivDraftEditImage);
        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.GONE);
        msearchvisitfrag_recyclerviewaddeditem = view.findViewById(R.id.Draftsearchvisitfrag_recyclerviewaddeditem);
        mInvoiceSoldRecyclerView = view.findViewById(R.id.Draftsearchvisitfrag_recyclerviewqty);
        mnextbtnone_fragmentsearchvisit = view.findViewById(R.id.Draftnextbtnone_fragmentsearchvisit);

        mfragmentSPinner_faultList = view.findViewById(R.id.Draftsp_FanFault);
        msp_MaterialName = view.findViewById(R.id.Draftsp_MaterialName);
        mett_searchvisitfragmentquantityammount = view.findViewById(R.id.Draftett_searchvisitfragmentquantityammount);
        met_searchvisitfragbatchno = view.findViewById(R.id.Draftet_searchvisitfragbatchno);
        met_addproductwarrantystatus = view.findViewById(R.id.Draftet_addproductwarrantystatus);
        mett_searchvisitfragmentacceptedquan = view.findViewById(R.id.Draftett_searchvisitfragmentacceptedquan);
        met_searchvisitfragquantityhint = view.findViewById(R.id.Draftet_searchvisitfragquantityhint);

        mett_addproductselectdate = view.findViewById(R.id.Draftett_addproductselectdate);

        msearchvisitfrag_addbtn = view.findViewById(R.id.Draftsearchvisitfrag_addbtn);
        met_inspectiondate = view.findViewById(R.id.Draftet_searchfragvisitinspectiondate);
        msearchvisitfrag_linthree = view.findViewById(R.id.Draftsearchvisitfrag_linthree);
        msearchvisitfrag_btnsubmit = view.findViewById(R.id.searchvisitfrag_btnsubmit);
        mnextbtntwo_fragmentsearchvisit = view.findViewById(R.id.Draftnextbtntwo_fragmentsearchvisit);
        mfragmentsearchvisit_relthree = view.findViewById(R.id.Draftfragmentsearchvisit_relthree);
        metsecond_addproductquantityammount = view.findViewById(R.id.Draftetsecond_addproductquantityammount);
        msearchvisitfrag_btnnext = view.findViewById(R.id.searchvisitfrag_btnnext);
        msearchvisitfrag_btnprevious = view.findViewById(R.id.searchvisitfrag_btnprevious);
    }
    private void setCustomAdapter() {
        ItemModelList = new ArrayList<>();
        msearchvisitfrag_recyclerviewaddeditem.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TableViewItemAdapter(getActivity(), ItemModelList);
        msearchvisitfrag_recyclerviewaddeditem.setAdapter(mAdapter);
    }
    private void getMaterialList() {
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
        String url = String.format(ServerConfig.getMaterialLightUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), divisionId, DistributerId,sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(mContext), "EN", "LoginActivity", CommonUtility.getNetworkType(mContext), CommonUtility.getNetworkOperator(mContext), "" + System.currentTimeMillis(), "M", "");
        apiInterface.getMaterialLightData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MaterialLightRess>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(MaterialLightRess entity) {

                        if (entity.getStatus()!=null)
                        if (entity.getStatus().equalsIgnoreCase("200")) {

                            faultDataList = entity.getData().get(0).getFaultList();
                            materialDataList = entity.getData().get(0).getMaterialList();

                            fName = new String[faultDataList.size() + 1];
                            fId = new String[faultDataList.size() + 1];
                            fName[0] = "Select Fault";
                            fId[0] = "0";

                            if (faultDataList != null && faultDataList.size() > 0) {

                                for (int iCount = 0; iCount < faultDataList.size(); iCount++) {
                                    fName[iCount + 1] = faultDataList.get(iCount).getFaultName();
                                    fId[iCount + 1] = faultDataList.get(iCount).getId();
                                }
                                setFaultSearchUI(faultDataList);
                            }


                            fMName = new String[materialDataList.size() + 1];
                            fMId = new String[materialDataList.size() + 1];
                            fMNameMaerialID = new String[materialDataList.size() + 1];

                            fMName[0] = "Select Material Name";
                            fMId[0] = "0";
                            fMNameMaerialID[0] = "0";
                            if (materialDataList != null && materialDataList.size() > 0) {

                                for (int iCount = 0; iCount < materialDataList.size(); iCount++) {
                                    fMName[iCount + 1] = materialDataList.get(iCount).getMaterialName();
                                    fMNameMaerialID[iCount + 1] = materialDataList.get(iCount).getMaterialID();

                                    fMId[iCount + 1] = materialDataList.get(iCount).getId();
                                }
                                setMaterialSearchUI(materialDataList);

                            }
                            setCurrentdata();

                        }else {

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
    private void setMaterialSearchUI(List<MaterialList> materialDataList) {
        mAllItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, fMName);
        mAllItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        msp_MaterialName.setAdapter(mAllItemSpinnerAdapter);
    }
    private void setFaultSearchUI(List<FaultList> faultDataList) {
        allItemSpinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_text, fName);
        allItemSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        mfragmentSPinner_faultList.setAdapter(allItemSpinnerAdapter);

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
    public void onAddField() {
        addField = true;
        selectedDate = mett_addproductselectdate.getText().toString();
        selcted_quntity = mett_searchvisitfragmentquantityammount.getText().toString();
        selcted_batchNo = met_searchvisitfragbatchno.getText().toString();
        warranty_status = met_addproductwarrantystatus.getText().toString();
        warranty_accepted_quan = mett_searchvisitfragmentacceptedquan.getText().toString();
        warranty_rejected_quan = met_searchvisitfragquantityhint.getText().toString();
        sum_FaultQuanity += Integer.parseInt(selcted_quntity);

        String SECOND_PDI_QUANITY = metsecond_addproductquantityammount.getText().toString();  /*secondquantity*/
        Log.e("sum_FaultQuanity", "sum_FaultQuanity" + sum_FaultQuanity);
        if (sum_FaultQuanity <= Integer.parseInt(SECOND_PDI_QUANITY)) {
            Log.e("sum_Fault", "sum_Fault" + sum_FaultQuanity);
            Product md = new Product(selectFaultName, selectedDate
                    , selcted_quntity, selcted_batchNo, warranty_status, warranty_accepted_quan, warranty_rejected_quan);

            ItemModelList.add(md);
            JSONObject FormDataAddField = new JSONObject(); // for form list data
            try {
                FormDataAddField.put("FaultID", selectFaultId);
                FormDataAddField.put("ProductStatus", "sold");
                FormDataAddField.put("FaultQnty", selcted_quntity);
                FormDataAddField.put("FaultBatchNo", selcted_batchNo);
                FormDataAddField.put("FaultMFG", selectedDate);
                FormDataAddField.put("FaultWarranty", warranty_status);
                FormDataAddField.put("FaultAccept", warranty_accepted_quan);
                FormDataAddField.put("FaultReject", warranty_rejected_quan);
                FormDataAddField.put("FaultName", selectFaultName);

                MainFormData.put(FormDataAddField);
                mAdapter.notifyDataSetChanged();
                mett_addproductselectdate.setText("");
                mett_searchvisitfragmentquantityammount.setText("");
                met_searchvisitfragbatchno.setText("");
                met_addproductwarrantystatus.setText("");
                mett_searchvisitfragmentacceptedquan.setText("");
                met_searchvisitfragquantityhint.setText("");
                faultDataList.clear(); /*for clear the Fault data*/
                setFaultSearchUI(faultDataList);
                allItemSpinnerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fault quantity should be equal material quantity", Toast.LENGTH_SHORT).show();
            int value = Integer.parseInt(mett_searchvisitfragmentquantityammount.getText().toString());
            sum_FaultQuanity = sum_FaultQuanity - value;
            Log.e("getSumFaultQuantity", "getSumFaultQuantity" + sum_FaultQuanity + "value" + value);
            return;
        }


    }
    private void selectPurchageDate() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePicker datePicker = null;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);

                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                StringBuilder purchageDate = new StringBuilder().append(monthOfYear + 1).append("-").append(year);
                String DateTimeStamp = sdf.format(myCalendar.getTime());
                try {
                    Date dateObj = sdf.parse(DateTimeStamp);
                    String monthString_MothString = (String) DateFormat.format("MMM", dateObj); // Jun
                    String monthString_Year = (String) DateFormat.format("yyyy", dateObj); // Jun
                    String MonthYearCOnCat = monthString_MothString + "-" + monthString_Year;

                    String monthNumber = (String) DateFormat.format("MM", dateObj); // 06
                    String MonthYearCOnCatWarranty = monthNumber + "-" + monthString_Year;

                    mett_addproductselectdate.setText(MonthYearCOnCat);
                    getproductWarrantyStatus(MonthYearCOnCatWarranty);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        final Calendar minCalendar = Calendar.getInstance();
        int minDay = minCalendar.get(Calendar.DAY_OF_MONTH),
                minMonth = minCalendar.get(Calendar.MONTH),
                minYear = minCalendar.get(Calendar.YEAR) - 2;
        minCalendar.set(Calendar.YEAR, minYear);
        minCalendar.set(Calendar.MONTH, minMonth);
        minCalendar.set(Calendar.DAY_OF_MONTH, minDay);
        datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void getproductWarrantyStatus(String monthYearCOnCatWarranty) {
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
        String url = String.format(ServerConfig.getproductLightWarrantyStatusUrl(),
                sharedPrefsManager.getString(
                        SharedPreferenceKeys.USER_ID),
                productID,
                monthYearCOnCatWarranty
                , sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(mContext), "EN", "LoginActivity", CommonUtility.getNetworkType(mContext), CommonUtility.getNetworkOperator(mContext), "" + System.currentTimeMillis(), "M", "");
        apiInterface.getProductLightWarrantyStatusData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RequestView>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(RequestView entity) {
                        // draftFanBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus() != null && entity.getStatus().equalsIgnoreCase("200")) {
                            try {
                                List<String> warrantyStatusList = entity.getData();
                                if (warrantyStatusList != null && warrantyStatusList.size() > 0) {
                                    LightWarrantyStatus = warrantyStatusList.get(0);
                                    if (LightWarrantyStatus != null && !LightWarrantyStatus.isEmpty())

                                        if (LightWarrantyStatus.equalsIgnoreCase("IN")) {
                                            met_addproductwarrantystatus.setText("In Warranty");

                                        } else if (LightWarrantyStatus.equalsIgnoreCase("OUT")) {
                                            met_addproductwarrantystatus.setText("Out of Warranty");

                                        }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Warranty Status not found", Toast.LENGTH_LONG).show();
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


    @Override
    public void onTextChanged(int position, String charSeq) {
        try {
            jObQuantity = (JSONObject) TempMainArrayList.get(position);
            jObQuantity.put("PDIQnty", charSeq.toString());
            CharseqQuantuty = charSeq.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class TableViewItemAdapter extends RecyclerView.Adapter<TableViewItemAdapter.MyViewHolder> {
        private final Context mContext;
        private final ArrayList<Product> taskList;
        TableViewItemAdapter(Context applicationContext, ArrayList<Product> itemModelList) {
            taskList = itemModelList;
            mContext = applicationContext;
        }
        @NonNull
        @Override
        public TableViewItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycleerview_light_added_view, parent, false);
            return new TableViewItemAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TableViewItemAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            Product task = taskList.get(position);
            holder.mtvproductsFault.setText(task.getFaultList());
            holder.mtvQuantity.setText(task.getSelcted_quntity());
            holder.mtvBatch.setText(task.getSelcted_batchNo());
            holder.mselectDate.setText(task.getSelectedDate());
            holder.mwarrantyStatus.setText(task.getWarranty_status());
            holder.macceptedQuantity.setText(task.getWarranty_accepted_quan());
            holder.mRejectedQuantity.setText(task.getWarranty_rejected_quan());
            holder.mtvproductsFault.setEnabled(false);
            holder.mtvQuantity.setEnabled(false);
            holder.mtvBatch.setEnabled(false);
            holder.mselectDate.setEnabled(false);
            holder.mwarrantyStatus.setEnabled(false);
            holder.macceptedQuantity.setEnabled(false);
            holder.mRejectedQuantity.setEnabled(false);

            holder.mIvedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mett_addproductselectdate.setText(taskList.get(position).getSelectedDate());
                    mett_searchvisitfragmentquantityammount.setText(taskList.get(position).getSelcted_quntity());
                    met_searchvisitfragbatchno.setText(taskList.get(position).getSelcted_batchNo());
                    met_addproductwarrantystatus.setText(taskList.get(position).getWarranty_status());
                    mett_searchvisitfragmentacceptedquan.setText(taskList.get(position).getWarranty_accepted_quan());
                    met_searchvisitfragquantityhint.setText(taskList.get(position).getWarranty_rejected_quan());
                }
            });


            holder.mdeleteProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskList.remove(position);
                    sum_FaultQuanity = sum_FaultQuanity - Integer.parseInt(task.getSelcted_quntity());
                    Log.e("getFaultDeleteData","getFaultDeleteData"+sum_FaultQuanity);
                    notifyDataSetChanged();
                }
            });

            /*delete Functionality*/

         
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView mtvproductsFault;
            private final TextView mtvQuantity;
            final TextView mtvBatch;
            private final TextView mselectDate;
            private final TextView mwarrantyStatus;
            private final TextView macceptedQuantity;
            private final TextView mRejectedQuantity;
            private final ImageView mdeleteProd;
            private final ImageView mIvedit;



            MyViewHolder(View itemView) {
                super(itemView);
                mtvproductsFault = itemView.findViewById(R.id.tvaadeditem_itemone);
                mtvQuantity = itemView.findViewById(R.id.tvaadeditem_itemtwo);
                mtvBatch = itemView.findViewById(R.id.tvaadeditem_itemthree);
                mselectDate = itemView.findViewById(R.id.tvaadeditem_itemfour);
                mwarrantyStatus = itemView.findViewById(R.id.tvaadeditem_itemfive);
                macceptedQuantity = itemView.findViewById(R.id.tv_itemsix);
                mRejectedQuantity = itemView.findViewById(R.id.tv_itemseven);
                mdeleteProd = itemView.findViewById(R.id.Ivdelete);
                mIvedit = itemView.findViewById(R.id.Ivedit);

            }
        }
    }

    private void hitSaveAPI(int status_draft_id) {

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

        String url = String.format(ServerConfig.saveLightDetailUrl(), sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID), visitDetailEntity.getDistributorid(), visitDetailEntity.getRequestno(), status_draft_id, sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN), appVersionName, appVersionCode, Imei, CommonUtility.getDeviceName(), "Android", "" + Build.VERSION.SDK_INT, "" + Build.VERSION.RELEASE, "" + CommonUtility.getIpAddress(mContext), "EN", "LoginActivity", CommonUtility.getNetworkType(mContext), CommonUtility.getNetworkOperator(mContext), "" + System.currentTimeMillis(), "M");
        apiInterface.saveLightDetailData(url, gsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MaterialLIghtBaseRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(MaterialLIghtBaseRes entity) {
                        // addProductFanDetailBinding.pbHomeLoading.setVisibility(View.GONE);
                        if (entity.getStatus().equalsIgnoreCase("200")) {
                            Toast.makeText(mContext, entity.getMessage(), Toast.LENGTH_LONG).show();
                       /* startQnty = startQnty+1;
                        resetFanUI();*/
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, HomePageFragment.newInstance("", ""), fragmentTag).commit();

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

    public class LightInvoiceSoldAdapter extends RecyclerView.Adapter<LightInvoiceSoldAdapter.MyItemViewHolder> {

        private List<InvoiceQntyEntity> invoiceQntySoldList;
        private Context mContext_;
        private OnEditTextChanged onEditTextChanged;


        JSONObject InvoiceListQuantity = new JSONObject(); //for invoice list data

        public LightInvoiceSoldAdapter(Context mContext, List<InvoiceQntyEntity> invoiceQntySoldList, OnEditTextChanged onEditTextChanged) {
            this.mContext_ = mContext;
            this.onEditTextChanged = onEditTextChanged;
            this.invoiceQntySoldList = invoiceQntySoldList;
        }

        @NonNull
        @Override
        public LightInvoiceSoldAdapter.MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            RecycleerviewLightInvoiceSoldQuantityBinding recycletableViewitemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.recycleerview_light_invoice_sold_quantity, parent, false);

            LightInvoiceSoldAdapter.MyItemViewHolder MyItemViewHolder = new LightInvoiceSoldAdapter.MyItemViewHolder(recycletableViewitemBinding);
            return MyItemViewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull LightInvoiceSoldAdapter.MyItemViewHolder holder, int position) {

            try {
                InvoiceQntyEntity qntyEntity = invoiceQntySoldList.get(position);
                if (qntyEntity.getBillingDocumentNumner() != null && !qntyEntity.getBillingDocumentNumner().equalsIgnoreCase(""))
                    InvoiceNo = qntyEntity.getBillingDocumentNumner();
                holder.recycletableViewitemBinding.txtLightSoldInvoiceNo.setText("" + InvoiceNo);
                if (qntyEntity.getAvailableQuantity() != null && !qntyEntity.getAvailableQuantity().equalsIgnoreCase(""))
                    AvailableQuanitity = qntyEntity.getAvailableQuantity();
                Integer intValue = Integer.valueOf((int) Math.round(Float.parseFloat(AvailableQuanitity)));

                holder.recycletableViewitemBinding.txtLightSoldAvailableQnty.setText("" + intValue);

                SoldQuanitity = qntyEntity.getSoldQuantity();
                if (SoldQuanitity != null) {
                    holder.recycletableViewitemBinding.etLightSoldPdiQnty.setText(SoldQuanitity);

                } else {
                    holder.recycletableViewitemBinding.etLightSoldPdiQnty.setText("0");

                }

                holder.recycletableViewitemBinding.etLightSoldPdiQnty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int positionvalue, int before, int count) {
                        onEditTextChanged.onTextChanged(position, charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {

            return invoiceQntySoldList.size();
        }

        public class MyItemViewHolder extends RecyclerView.ViewHolder {

            RecycleerviewLightInvoiceSoldQuantityBinding recycletableViewitemBinding;

            public MyItemViewHolder(@NonNull RecycleerviewLightInvoiceSoldQuantityBinding itemView) {
                super(itemView.getRoot());
                recycletableViewitemBinding = itemView;
            }
        }

    }


    public void setCurrentdata() {

        int showQnty = startQnty;
        showQnty = showQnty+1;
        met_searchvisitfragquantityammount.setText(String.valueOf(showQnty) + "/" + String.valueOf(quantyTotal));
       // resetLightUI();
        ResetCurrentData();
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
            JSONObject job = (JSONObject) jsonArray.get(startQnty);
            String inspDate = job.getString("InspectionDate");
            met_searchfragvisitinspectiondate.setText("" + inspDate);
            String firstQuantity = job.getString("FirstQnty");
            Log.e("getData","getData"+firstQuantity);
            String SecondQuantity = job.getString("SecondaryQnty");
            met_searchvisitfragquantityammount.setText(""+firstQuantity);
             metsecond_addproductquantityammount.setText(""+SecondQuantity);
            JSONArray jsonArrayFormArray = job.getJSONArray("FormData");
            mfragmentsearchvisit_relthree.setVisibility(View.GONE);
            for (int i = 0; i < jsonArrayFormArray.length(); i++) {
                JSONObject jobFormData = (JSONObject) jsonArrayFormArray.get(i);
                String availableQuantity = jobFormData.getString("FaultQnty");
                String BatchNo = jobFormData.getString("FaultBatchNo");
                String productDate = jobFormData.getString("FaultMFG");
                String WarrantyStatus = jobFormData.getString("FaultWarranty");
                String accept = jobFormData.getString("FaultAccept");
                String reject = jobFormData.getString("FaultReject");
                prevFaultId = jobFormData.getString("FaultID");  /*changed fault id */
                String prevFaultName = jobFormData.getString("FaultName");

                if (prevFaultId != null && fId != null) {
                    int spinnerfaultPosition = 0;
                    for (int kCount = 0; kCount < fId.length; kCount++) {
                        if (prevFaultId.equalsIgnoreCase(fId[kCount])) {
                            spinnerfaultPosition = kCount;
                        }
                    }
                    int spinnerSerialPosition = allItemSpinnerAdapter.getPosition(fName[spinnerfaultPosition]);
                    mfragmentSPinner_faultList.setSelection(spinnerSerialPosition);
                }
                Product md = new Product(prevFaultName, productDate
                        , availableQuantity, BatchNo, WarrantyStatus, accept,reject);
           //     ItemModelList = new ArrayList<>();
                ItemModelList.add(md);
            }
            msearchvisitfrag_recyclerviewaddeditem.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new TableViewItemAdapter(getActivity(), ItemModelList);
            msearchvisitfrag_recyclerviewaddeditem.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            JSONArray jsonArraInoiceListArray = job.getJSONArray("InvoiceList");
            List<InvoiceQntyEntity> invoiceQntySoldLists = new ArrayList<>();
            for (int i = 0; i < jsonArraInoiceListArray.length(); i++) {
                JSONObject jobInvoiceData = (JSONObject) jsonArraInoiceListArray.get(i);
                String InvoiceNo_ = jobInvoiceData.getString("InvoiceNo");
                String availableQuantity_ = jobInvoiceData.getString("AvailableQnty");
                String PDIQnty_ = jobInvoiceData.getString("PDIQnty");
                InvoiceQntyEntity md = new InvoiceQntyEntity(InvoiceNo_, availableQuantity_, PDIQnty_);
                invoiceQntySoldLists.add(md);
            }
            soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldLists, this);
            mInvoiceSoldRecyclerView.setAdapter(soldListadapter);
            soldListadapter.notifyDataSetChanged();
            PrematerialId = job.getString("MaterialID");
            try {
                if (PrematerialId != null) {
                    int spinnerfaultPosition = 0;
                    for (int kCount = 0; kCount < fMNameMaerialID.length; kCount++) {
                        if (PrematerialId.equalsIgnoreCase(fMNameMaerialID[kCount])) {
                            spinnerfaultPosition = kCount;

                        }
                    }
                    int spinnerSerialPosition = mAllItemSpinnerAdapter.getPosition(fMName[spinnerfaultPosition]);
                    msp_MaterialName.setSelection(spinnerSerialPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
                getMaterialList();
            }
            prevDistributorAddress = job.getString("DistributorAddress");
            prevDistributorName = job.getString("DistributorName");
            prevDivisionID = job.getString("DivisionID");
            prevDivisionName = job.getString("DivisionName");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResetCurrentData() {
        try {
            mett_addproductselectdate.setText("");
            mett_searchvisitfragmentquantityammount.setText("");
            met_searchvisitfragbatchno.setText("");
            met_addproductwarrantystatus.setText("");
            mett_searchvisitfragmentacceptedquan.setText("");
            met_searchvisitfragquantityhint.setText("");
            MainFormData = new JSONArray(new ArrayList<String>());
            TempMainArrayList = new JSONArray(new ArrayList<String>());
            invoiceQntySoldList.clear();
            soldListadapter = new LightInvoiceSoldAdapter(mContext, invoiceQntySoldLists, this);
            mInvoiceSoldRecyclerView.setAdapter(null);
            ItemModelList.clear();
            materialDataList.clear();
            setMaterialSearchUI(materialDataList);
            mAllItemSpinnerAdapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
            soldListadapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalCount(int startQnty){
        int TotalSum = 0;
        try {
            JSONArray jsonArray = MainObject.getJSONArray("SaveLightDetails");
            for (int i = 0; i <= this.startQnty; i++) {
                JSONObject job = (JSONObject) jsonArray.get(i);
                String SecondaryQnty = job.getString("SecondaryQnty");
                TotalSum = TotalSum + Integer.parseInt(SecondaryQnty);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // totalQuantity=TotalSum;
        return TotalSum;
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
    private void ResetonMaterial() {
        sum_FaultQuanity = 0;
        mett_addproductselectdate.setText("");
        mett_searchvisitfragmentquantityammount.setText("");
        met_searchvisitfragbatchno.setText("");
        met_addproductwarrantystatus.setText("");
        mett_searchvisitfragmentacceptedquan.setText("");
        met_searchvisitfragquantityhint.setText("");
        ItemModelList.clear();
        mAdapter.notifyDataSetChanged();
        mfragmentsearchvisit_relthree.setVisibility(View.GONE);
    }
    private boolean getCheckPdiQuantity() {
        boolean SecondaryQtyChanges = false;
        int jcount = MainArray.length();
        if (jcount > startQnty) {
            SecondaryQtyChanges = true;
        } else {
            try {
                sum_AddQantity = 0;
                String EnterIndividualQuantity = CharseqQuantuty;
                for (int i = 0; i < TempMainArrayList.length(); i++) {
                    JSONObject jobInvoiceData = (JSONObject) TempMainArrayList.get(i);
                    String PDIQnty_ = jobInvoiceData.getString("PDIQnty");
                    if (!PDIQnty_.equalsIgnoreCase("")) {
                        sum_AddQantity += Integer.parseInt(PDIQnty_);
                        Log.e("getValue+sum_AddQantity", "sum_AddQantity" + sum_AddQantity);
                        mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);
                        SecondaryQtyChanges = true;
                    } else {
                        try {
                            if (!PDIQnty_.equalsIgnoreCase("0")) {
                                SecondaryQtyChanges = true;

                            } else {
                                sum_AddQantity = 0;
                                Toast.makeText(getActivity(), "Please enter Invoice Quantity", Toast.LENGTH_SHORT).show();
                                mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                SecondaryQtyChanges = false;
                                isNextClick = false;

                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (SecondaryQtyChanges) {
                    String SecondPdiQuantity = metsecond_addproductquantityammount.getText().toString();  /*secondquantity*/
                    Log.e("jObQuantity", "jObQuantity" + jObQuantity);
                    if (jObQuantity.length() == 0) {
                        SecondaryQtyChanges = false;

                        Toast.makeText(mContext, "Please fill invoice quantity", Toast.LENGTH_SHORT).show();
                        isNextClick = false;

                    } else {
                        SecondaryQtyChanges = true;
                    }
                    if (SecondaryQtyChanges) {
                        if (jObQuantity.get("AvailableQnty") != null && jObQuantity.get("AvailableQnty").equals(null)) {
                            SecondaryQtyChanges = false;

                        } else {
                            String avaialbleQuantity = String.valueOf(jObQuantity.get("AvailableQnty"));
                            Integer avalaibleresultQuantSec = Integer.valueOf((int) Math.round(Float.parseFloat(avaialbleQuantity)));
                            try {
                                if (Integer.parseInt(EnterIndividualQuantity) <= avalaibleresultQuantSec) {
                                    SecondaryQtyChanges = true;

                                    if (Integer.parseInt(EnterIndividualQuantity) <= quantyTotal) {
                                        mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);
                                        SecondaryQtyChanges = true;


                                        if (sum_AddQantity == Integer.parseInt(SecondPdiQuantity)) {
                                            mfragmentsearchvisit_relthree.setVisibility(View.VISIBLE);
                                            SecondaryQtyChanges = true;

                                        } else {
                                            sum_AddQantity = 0;
                                            mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                            SecondaryQtyChanges = false;
                                            isNextClick = false;
                                            Toast.makeText(getActivity(), "Sun of Invoice quantity should be equal PDI quantity ", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        sum_AddQantity = 0;
                                        mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                        SecondaryQtyChanges = false;
                                        isNextClick = false;

                                        Toast.makeText(getActivity(), CharseqQuantuty.toString() + "should equal" + quantyTotal, Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    sum_AddQantity = 0;
                                    mfragmentsearchvisit_relthree.setVisibility(View.INVISIBLE);
                                    SecondaryQtyChanges = false;
                                    isNextClick = false;

                                    Toast.makeText(getActivity(), CharseqQuantuty.toString() + "should not be greator than or less than" + avalaibleresultQuantSec, Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        return SecondaryQtyChanges;
    }

}