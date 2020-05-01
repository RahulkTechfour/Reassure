package com.luminous.pdi.visit_detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.luminous.pdi.R;
import com.luminous.pdi.databinding.FragmentVisitDetailBinding;
import com.luminous.pdi.home.activities.HomePageActivity;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.fragments.CreateListDTO.ResponseDatum;
import com.luminous.pdi.home.fragments.HomePageFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class VisitDetailFragment extends Fragment implements  View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private FragmentVisitDetailBinding visitDetailBinding;
    ResponseDatum visitDetailEntity;
    private String tag="TAG_FRAGMENT";
    static PendingvisitDetail cardEntity= new PendingvisitDetail();

    int contractCount=0;
    int customerCount=0;
    int productCount=0;
    int repairSummCount=0;



    public VisitDetailFragment() {
        // Required empty public constructor
    }


    public static VisitDetailFragment newInstance(String param1, String param2) {
        VisitDetailFragment fragment = new VisitDetailFragment();
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

        visitDetailBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_visit_detail, container, false);

        View view=visitDetailBinding.getRoot();

        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.GONE);


        visitDetailBinding.pdivisitlistBackimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new HomePageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        Bundle args = getArguments();
        if (args != null) {
             visitDetailEntity = (ResponseDatum) args.getSerializable("VisitDetail");
             setVisitDetailUI();
        } else {
            Log.w("VisitDetail", "Arguments expected, but missing");
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        setOnItemClickListener();

        return view ;
    }


    private void setOnItemClickListener() {
        visitDetailBinding.visitfloatbtn.setOnClickListener(this);
        visitDetailBinding.llVisitCall.setOnClickListener(this);
      visitDetailBinding.IvCuEmail.setOnClickListener(this);
        visitDetailBinding.IvcuAddress.setOnClickListener(this);
        visitDetailBinding.IvCuAlternateNo.setOnClickListener(this);
        visitDetailBinding.IvcuState.setOnClickListener(this);
        visitDetailBinding.IvcuPincode.setOnClickListener(this);

        visitDetailBinding.llHeadContractDetails.setOnClickListener(this);
        visitDetailBinding. llheadCustomerDetails.setOnClickListener(this);
        visitDetailBinding. llHeadProductDetails.setOnClickListener(this);
        visitDetailBinding.  llHeadRepairSum.setOnClickListener(this);


    }

    private void setVisitDetailUI() {
        if(visitDetailEntity!=null){

            if(visitDetailEntity.getCuFirstName()!=null && !visitDetailEntity.getCuFirstName().equalsIgnoreCase("")){
                visitDetailBinding.idivisitlistName.setText(""+visitDetailEntity.getCuFirstName());
            }

            /*should come date */
            if(visitDetailEntity.getAssetCareNumber()!=null && !visitDetailEntity.getAssetCareNumber().equalsIgnoreCase("")){
               // String Date=visitDetailEntity.getAssetCareNumber();
              //  String DateSplitOne[]=Date.split(" ");
             //   visitDetailBinding.tvscheduledate.setText(""+DateSplitOne[0]);
                visitDetailBinding.tvScheduledate.setText("10 March 2020 10:30 AM");
            }

            if(visitDetailEntity.getCuPhone()!=null && !visitDetailEntity.getCuPhone().equalsIgnoreCase("")){
                visitDetailBinding.txtVisitTitle1.setText(visitDetailEntity.getCuPhone());
            }

            /*should come km distnce */

            if(visitDetailEntity.getAssetCareNumber()!=null && !visitDetailEntity.getAssetCareNumber().equalsIgnoreCase("")){
                visitDetailBinding.txtVisitDivision.setText("8.1KM");

            }

            if(visitDetailEntity.getPoPolicyNumber()!=null && !visitDetailEntity.getPoPolicyNumber().equalsIgnoreCase("")){
                visitDetailBinding.tvContactNo.setText(visitDetailEntity.getPoPolicyNumber());

            }

            if(visitDetailEntity.getPoCreditAvailable()!=null && !visitDetailEntity.getPoCreditAvailable().equals("")){
                visitDetailBinding.tvcreditAvailable.setText(""+visitDetailEntity.getPoCreditAvailable());

            }

            if(visitDetailEntity.getPoStartDate()!=null && !visitDetailEntity.getPoStartDate().equals("")){
                visitDetailBinding.tvCrstartDate.setText(visitDetailEntity.getPoStartDate());

            }

            if(visitDetailEntity.getPoEndDate()!=null && !visitDetailEntity.getPoEndDate().equals("")){
                visitDetailBinding.tvCrEndDate.setText(visitDetailEntity.getPoEndDate());

            }

            if(visitDetailEntity.getCuFirstName()!=null && !visitDetailEntity.getCuFirstName().equals("")){
                visitDetailBinding.cuName.setText(visitDetailEntity.getCuFirstName());

            }

            if(visitDetailEntity.getCuFirstName()!=null && !visitDetailEntity.getCuFirstName().equals("")){
                visitDetailBinding.cuName.setText(visitDetailEntity.getCuFirstName());

            }

            if(visitDetailEntity.getCuLastName()!=null && !visitDetailEntity.getCuLastName().equals("")){
                visitDetailBinding.cuLastName.setText(visitDetailEntity.getCuLastName());

            }

            if(visitDetailEntity.getCuEmail()!=null && !visitDetailEntity.getCuEmail().equals("")){
                visitDetailBinding.tvCuEmail.setText(visitDetailEntity.getCuEmail());



            }

            if(visitDetailEntity.getCuPhone()!=null && !visitDetailEntity.getCuPhone().equals("")){
                visitDetailBinding.cuPhone.setText(visitDetailEntity.getCuPhone());

            }

            if(visitDetailEntity.getCuPhone()!=null && !visitDetailEntity.getCuPhone().equals("")){
                visitDetailBinding.tvAlterName.setText(visitDetailEntity.getCuPhone());
            }

            if(visitDetailEntity.getCuAddress()!=null && !visitDetailEntity.getCuAddress().equals("")){
                visitDetailBinding.cuAddress.setText(visitDetailEntity.getCuAddress());

            }
            if(visitDetailEntity.getCuCity()!=null && !visitDetailEntity.getCuCity().equals("")){
                visitDetailBinding.cuCity.setText(visitDetailEntity.getCuCity());

            }
            if(visitDetailEntity.getCuState()!=null && !visitDetailEntity.getCuState().equals("")){
                visitDetailBinding.cuState.setText(visitDetailEntity.getCuState());

            }
            if(visitDetailEntity.getCuPincode()!=null && !visitDetailEntity.getCuPincode().equals("")){
                visitDetailBinding.cuPincode.setText(visitDetailEntity.getCuPincode());

            }
            if(visitDetailEntity.getPrProductType()!=null && !visitDetailEntity.getPrProductType().equals("")){
                visitDetailBinding.productType.setText(visitDetailEntity.getPrProductType());

            }
            if(visitDetailEntity.getPrBrand()!=null && !visitDetailEntity.getPrBrand().equals("")){
                visitDetailBinding.Prbrand.setText(visitDetailEntity.getPrBrand());

            }

            if(visitDetailEntity.getPrBrand()!=null && !visitDetailEntity.getPrBrand().equals("")){
                visitDetailBinding.Prbrand.setText(visitDetailEntity.getPrBrand());

            }
            if(visitDetailEntity.getPrModel()!=null && !visitDetailEntity.getPrModel().equals("")){
                visitDetailBinding.prModel.setText(visitDetailEntity.getPrModel());

            }
            if(visitDetailEntity.getPrSerialNumber()!=null && !visitDetailEntity.getPrSerialNumber().equals("")){
                visitDetailBinding.prtvSerialNO.setText(visitDetailEntity.getPrSerialNumber());

            }

            if(visitDetailEntity.getPrSerialNumber()!=null && !visitDetailEntity.getPrSerialNumber().equals("")){
                visitDetailBinding.prtvSerialNO.setText(visitDetailEntity.getPrSerialNumber());

            }
            if(visitDetailEntity.getPrName()!=null && !visitDetailEntity.getPrName().equals("")){
                visitDetailBinding.prName.setText(visitDetailEntity.getPrName());

            }
            if(visitDetailEntity.getPrPrice()!=null && !visitDetailEntity.getPrPrice().equals("")){
                visitDetailBinding.prPrice.setText(""+visitDetailEntity.getPrPrice());

            }
            if(visitDetailEntity.getPrProperties()!=null && !visitDetailEntity.getPrProperties().equals("")){
                visitDetailBinding.prProperties.setText(""+visitDetailEntity.getPrProperties());

            }

            if(visitDetailEntity.getPrProperties()!=null && !visitDetailEntity.getPrProperties().equals("")){
                visitDetailBinding.prProperties.setText(""+visitDetailEntity.getPrProperties());

            }
            if(visitDetailEntity.getPrPurchaseDate()!=null && !visitDetailEntity.getPrPurchaseDate().equals("")){
                visitDetailBinding.prPurchaseDate.setText(""+visitDetailEntity.getPrPurchaseDate());

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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.visitfloatbtn:
                try {
                    Bundle args = new Bundle();
                    args.putSerializable("VisitDetail",  visitDetailEntity);
                    Fragment fragment = AddProductVisitDetailFragment.newInstance("", "");
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "VisitDetailFragment").addToBackStack(null).commit();
                }catch(Exception ex){
                    ex.getStackTrace();
                }
                break;

            case R.id.llVisitCall:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" +cardEntity.getContactNo()));
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getActivity().startActivity(callIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

                /*case of edit address, state, alternate no, email*/

            case R.id.IvCuEmail:
                visitDetailBinding.tvCuEmail.setFocusable(true);
                visitDetailBinding.tvCuEmail.setClickable(true);
                visitDetailBinding.tvCuEmail.setEnabled(true);
                break;

            case R.id.IvCuAlternateNo:
                visitDetailBinding.tvAlterName.setFocusable(true);
                visitDetailBinding.tvAlterName.setClickable(true);
                visitDetailBinding.tvAlterName.setEnabled(true);

                break;
            case R.id.IvcuAddress:
                visitDetailBinding.cuAddress.setFocusable(true);
                visitDetailBinding.cuAddress.setClickable(true);
                visitDetailBinding.cuAddress.setEnabled(true);

                break;
            case R.id.IvcuState:
                visitDetailBinding.cuState.setFocusable(true);
                visitDetailBinding.cuState.setClickable(true);
                visitDetailBinding.cuState.setEnabled(true);

                break;
            case R.id.IvcuPincode:
                visitDetailBinding.cuPincode.setFocusable(true);
                visitDetailBinding.cuPincode.setClickable(true);
                visitDetailBinding.cuPincode.setEnabled(true);

                break;
            case R.id.llHeadContractDetails:
                if (contractCount == 0) {
                    contractCount++;
                    visitDetailBinding.llContractDetails.setVisibility(View.VISIBLE);
                    visitDetailBinding.llRepair.setVisibility(View.GONE);
                    visitDetailBinding.llProductDetails.setVisibility(View.GONE);
                    visitDetailBinding.llCustomerDetails.setVisibility(View.GONE);
                }else {
                    contractCount = 0;
                    visitDetailBinding.llContractDetails.setVisibility(View.GONE);

                }

                break;
            case R.id.llheadCustomerDetails:
                if (customerCount==0){
                    customerCount++;
                    visitDetailBinding.llCustomerDetails.setVisibility(View.VISIBLE);
                    visitDetailBinding.llContractDetails.setVisibility(View.GONE);
                    visitDetailBinding.llRepair.setVisibility(View.GONE);
                    visitDetailBinding.llProductDetails.setVisibility(View.GONE);
                }else {
                    customerCount=0;
                    visitDetailBinding.llCustomerDetails.setVisibility(View.GONE);

                }
                break;
            case R.id.llHeadProductDetails:
                if (productCount==0){
                    productCount++;
                    visitDetailBinding.llProductDetails.setVisibility(View.VISIBLE);
                    visitDetailBinding.llCustomerDetails.setVisibility(View.GONE);
                    visitDetailBinding.llContractDetails.setVisibility(View.GONE);
                    visitDetailBinding.llRepair.setVisibility(View.GONE);
                }else {
                    productCount=0;
                    visitDetailBinding.llProductDetails.setVisibility(View.GONE);
                }
                break;
            case R.id.llHeadRepairSum:
                if (repairSummCount==0){
                    repairSummCount++;
                    visitDetailBinding.llRepair.setVisibility(View.VISIBLE);
                    visitDetailBinding.llProductDetails.setVisibility(View.GONE);
                    visitDetailBinding.llCustomerDetails.setVisibility(View.GONE);
                    visitDetailBinding.llContractDetails.setVisibility(View.GONE);
                }else {
                    repairSummCount=0;
                    visitDetailBinding.llRepair.setVisibility(View.GONE);

                }


                break;


        }
    }

   

}
