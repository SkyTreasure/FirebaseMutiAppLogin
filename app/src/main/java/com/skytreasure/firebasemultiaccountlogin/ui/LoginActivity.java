package com.skytreasure.firebasemultiaccountlogin.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.skytreasure.firebasemultiaccountlogin.R;
import com.skytreasure.firebasemultiaccountlogin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityLoginBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_login);

        mBinding.btnEmailPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_email_password:
                Intent i=new Intent(this, SignInWithEmailActivity.class);
                startActivity(i);
                break;
        }
    }
}
