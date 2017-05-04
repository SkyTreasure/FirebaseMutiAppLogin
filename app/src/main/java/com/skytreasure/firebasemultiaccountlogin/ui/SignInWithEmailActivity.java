package com.skytreasure.firebasemultiaccountlogin.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skytreasure.firebasemultiaccountlogin.R;
import com.skytreasure.firebasemultiaccountlogin.databinding.ActivitySignInWithEmailBinding;

public class SignInWithEmailActivity extends AppCompatActivity implements View.OnClickListener {

     ActivitySignInWithEmailBinding mBinding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_with_email);

        mBinding.btnLogin.setOnClickListener(this);
        mBinding.btnRegister.setOnClickListener(this);
        mBinding.tvForgotPassword.setOnClickListener(this);


    }


    private  void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInWithEmailActivity.this,"Success: "+user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInWithEmailActivity.this,""+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private  void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInWithEmailActivity.this,"Success: "+user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInWithEmailActivity.this,""+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }



    private void resetPassword(String emailAddress){
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInWithEmailActivity.this, "Email sent.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:

                register(mBinding.etEmail.getText().toString(),mBinding.etPwd.getText().toString());
                break;
            case R.id.btn_login:
                login(mBinding.etEmail.getText().toString(),mBinding.etPwd.getText().toString());
                break;
            case R.id.tv_forgot_password:
                if(mBinding.etEmail.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(SignInWithEmailActivity.this, "Please enter the email address in above and then click forgot password?",
                            Toast.LENGTH_SHORT).show();
                }else{
                    resetPassword(mBinding.etEmail.getText().toString());
                }

                break;
        }
    }
}
