package com.luminous.pdi.visit_detail;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.fragment.AddProductLightDetailFragment;
import com.luminous.pdi.addproductLight.fragment.DraftLightDetailFragment;
import com.luminous.pdi.addproduct_fan.fragments.AddProductFanDetailFragment;
import com.luminous.pdi.databinding.FragmentAddProductVisitDetailBinding;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.dto.PendingvisitDetail;


public class AddProductVisitDetailFragment extends Fragment implements  View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private FragmentAddProductVisitDetailBinding addProductVisitDetailBinding;
    private String etQnty = "0";
    private PendingvisitDetail visitDetailEntity;
    private String tag = "TAG_FRAGMENT";


    public AddProductVisitDetailFragment() {
    }


    public static AddProductVisitDetailFragment newInstance(String param1, String param2) {
        AddProductVisitDetailFragment fragment = new AddProductVisitDetailFragment();
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

        addProductVisitDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product_visit_detail, container, false);

        View view = addProductVisitDetailBinding.getRoot();

        View view1 = (View) ((HomePageActivity) this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.GONE);


        Bundle args = getArguments();
        if (args != null) {
            visitDetailEntity = (PendingvisitDetail) args.getSerializable("VisitDetail");
            Log.e("getDataVisitDetail", "VisitDetail" + visitDetailEntity);
            if (visitDetailEntity.getQuantity().isEmpty()||visitDetailEntity.getQuantity().equalsIgnoreCase("0")){
                addProductVisitDetailBinding.etAddQnty.setClickable(true);
                addProductVisitDetailBinding.etAddQnty.setFocusable(true);
            }else {
                    addProductVisitDetailBinding.etAddQnty.setText(visitDetailEntity.getQuantity());
                    addProductVisitDetailBinding.etAddQnty.setClickable(false);
                    addProductVisitDetailBinding.etAddQnty.setFocusable(false);
            }
            setAddVisitDetailUI();
        } else {
            Log.w("VisitDetail", "Arguments expected, but missing");
        }
        setOnItemClickListener();

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        addProductVisitDetailBinding.addproductvisitlistBackimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("VisitDetail", visitDetailEntity);


                Fragment fragment = VisitDetailFragment.newInstance("", "");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();

            }
        });


        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(tag, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!");
                    Bundle args = new Bundle();
                    args.putSerializable("VisitDetail", visitDetailEntity);

                    Fragment fragment = VisitDetailFragment.newInstance("", "");
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void setAddVisitDetailUI() {

        if (visitDetailEntity != null) {

            if (visitDetailEntity.getDistributorname() != null && !visitDetailEntity.getDistributorname().equalsIgnoreCase("")) {
                addProductVisitDetailBinding.tvAddvisitlistName.setText("" + visitDetailEntity.getDistributorname());
            }

            if (visitDetailEntity.getVisitdate() != null && !visitDetailEntity.getVisitdate().equalsIgnoreCase("")) {

                String Date = visitDetailEntity.getVisitdate();
                String DateSplitOne[] = Date.split(" ");
                addProductVisitDetailBinding.tvAddscheduledatevalue.setText("" + DateSplitOne[0]);
            }

            if (visitDetailEntity.getDivisionname() != null && !visitDetailEntity.getDivisionname().equalsIgnoreCase("")) {
                addProductVisitDetailBinding.txtAddVisitDivision.setText(visitDetailEntity.getDivisionname());
            }

            if (visitDetailEntity.getStatus() != null && !visitDetailEntity.getStatus().equalsIgnoreCase("")) {
                addProductVisitDetailBinding.txtAddVisitStatus.setText(visitDetailEntity.getStatus());
            }
            if (visitDetailEntity.getAddress() != null && !visitDetailEntity.getAddress().equalsIgnoreCase("")) {
                addProductVisitDetailBinding.tvAddvisitaddress.setText(visitDetailEntity.getAddress());
            }
        }


    }

    private void setOnItemClickListener() {
        addProductVisitDetailBinding.btnAddProduct.setOnClickListener(this);

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

        switch (v.getId()) {

            case R.id.btn_addProduct:
                if (isValidate()) {
                    if (visitDetailEntity.getDivisionname() != null && visitDetailEntity.getDivisionname().equalsIgnoreCase("FAN")) {

                        try {
                            Bundle args = new Bundle();
                            args.putSerializable("VisitDetail", visitDetailEntity);
                            args.putString("Quantity", etQnty);
                            args.putInt("distributerId", visitDetailEntity.getDistributorid());

                            Fragment fragment = AddProductFanDetailFragment.newInstance("", "");
                            fragment.setArguments(args);
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }

                    } else {
                        try {
                            Bundle args = new Bundle();
                            args.putSerializable("VisitDetail", visitDetailEntity);
                            args.putString("Quantity", etQnty);
                            args.putInt("GET_DIVISIONID", visitDetailEntity.getDivisionid());
                            args.putString("getdivisionname", visitDetailEntity.getDivisionname());
                            args.putString("divisionaddress", visitDetailEntity.getAddress());
                            args.putString("distributerName", visitDetailEntity.getDistributorname());
                            args.putInt("distributerId", visitDetailEntity.getDistributorid());


                            // Fragment fragment = AddProductLightDetailFragment.newInstance("", "");
                            Fragment fragment = AddProductLightDetailFragment.newInstance("", "");


                            fragment.setArguments(args);
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }
                    }
                }
                break;
        }
    }

    //
    private boolean isValidate() {
        boolean isChecked = false;
        etQnty = addProductVisitDetailBinding.etAddQnty.getText().toString().trim();
        etQnty = etQnty.replaceFirst("^0+(?!$)", "");
        if (visitDetailEntity.getDivisionname() != null && visitDetailEntity.getDivisionname().equalsIgnoreCase("FAN")) {
            if (etQnty.trim().length() != 0) {
                int number = Integer.parseInt(etQnty);

                if (number > 50) {
                    isChecked = false;
                    addProductVisitDetailBinding.etAddQnty.setError("Quantity not more than 50");

                } else {
                    isChecked = true;
                    addProductVisitDetailBinding.etAddQnty.setError(null);
                }


            } else {
                isChecked = true;
                addProductVisitDetailBinding.etAddQnty.setError(null);

            }

        } else {
            if (etQnty.trim().length() != 0) {
                isChecked = true;
                addProductVisitDetailBinding.etAddQnty.setError(null);

            } else {

                isChecked = false;
                addProductVisitDetailBinding.etAddQnty.setError("quantity should not be empty");

            }


        }
        return isChecked;

    }
}

