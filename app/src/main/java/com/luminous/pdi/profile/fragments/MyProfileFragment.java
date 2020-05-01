package com.luminous.pdi.profile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luminous.pdi.R;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;
import com.luminous.pdi.databinding.FragmentMyProfileBinding;
import com.luminous.pdi.home.activities.HomePageActivity;


public class MyProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentMyProfileBinding myProfileBinding;
    private Context mContext;
    private SharedPrefsManager sharedPrefsManager;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        myProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false);
        sharedPrefsManager = new SharedPrefsManager(mContext);
        displayDataUI();

        TextView toolbarTextViews  = (TextView) ((HomePageActivity) this.getActivity()).findViewById(R.id.tvHeaderTitle);
        toolbarTextViews.setText("My Profile");
        View view1=(View) ((HomePageActivity)this.getActivity()).findViewById(R.id.toolbar_layout);
        view1.setVisibility(View.VISIBLE);


        return myProfileBinding.getRoot();
    }

    private void displayDataUI() {
        if (sharedPrefsManager != null) {

    /*        if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_NAME) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_NAME).equalsIgnoreCase("")) {
                myProfileBinding.txtUsrName.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_NAME));
                myProfileBinding.tvProfileUsername.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_NAME));
            }
            if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_MOBILE) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_MOBILE).equalsIgnoreCase("")) {
                myProfileBinding.tvProfileUsermobile.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_MOBILE));
            }

            if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_ADD) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_ADD).equalsIgnoreCase("")) {
                myProfileBinding.tvProfileUseradd.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_ADD));
            }*/

//            if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_STATE) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_STATE).equalsIgnoreCase("")) {
//                myProfileBinding.tvProfileUserstate.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_STATE));
//            }
//
//            if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_CITY) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_CITY).equalsIgnoreCase("")) {
//                myProfileBinding.tvProfileUsercity.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_CITY));
//            }
//            if (sharedPrefsManager.getString(SharedPreferenceKeys.USER_PIN) != null && !sharedPrefsManager.getString(SharedPreferenceKeys.USER_PIN).equalsIgnoreCase("")) {
//                myProfileBinding.tvProfileUserpincode.setText("" + sharedPrefsManager.getString(SharedPreferenceKeys.USER_PIN));
//            }
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


}
