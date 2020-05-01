package com.luminous.pdi.activities.forgot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.luminous.pdi.R;
import com.luminous.pdi.activities.LoginActivity.activities.LoginActivity;
import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityForgotPasswordBinding forgotBinding;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotBinding = DataBindingUtil.setContentView(ForgotPasswordActivity.this,R.layout.activity_forgot_password);
        setOnClickListener();
    }

    private void setOnClickListener() {
        forgotBinding.btnSub.setOnClickListener(this);
        forgotBinding.tvForgotclickhere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.tv_forgotclickhere:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.btnSub:
                if(isValidate()){
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    finish();
                }
                break;
        }
    }

    private boolean isValidate() {

        boolean isChecked = false;
        userEmail = forgotBinding.userEmail.getText().toString();
        userEmail = userEmail.replaceFirst("^0+(?!$)", "");
        if (userEmail.trim().length() > 0 && CommonUtility.validEmail(userEmail)) {
            forgotBinding.userEmail.setError(null);
            isChecked = true;

        } else {

            isChecked = false;
            forgotBinding.userEmail.setError("Enter Email Id");     //Edited by Anusha
        }

        return isChecked;
    }
}
