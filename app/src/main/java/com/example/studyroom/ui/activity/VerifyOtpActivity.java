package com.example.studyroom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studyroom.R;
import com.example.studyroom.api.UserApiUtil;
import com.example.studyroom.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {
    EditText enteredOtp;
    Button verifyButton;
    String mobileNumber;
    String otpVerificationId;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mobileNumber = getIntent().getStringExtra("mobile").toString();
        enteredOtp = (EditText) findViewById(R.id.editTextOtp);
        verifyButton = (Button) findViewById(R.id.buttonVerify);
        mAuth = FirebaseAuth.getInstance();

        initiateotp();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enteredOtp.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
                else if (enteredOtp.getText().toString().length() != 6)
                    Toast.makeText(getApplicationContext(), "INvalid OTP", Toast.LENGTH_LONG).show();
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId, enteredOtp.getText().toString());
                    System.out.println("PhoneAuthCredential is : "+credential);
                    Log.i(String.valueOf(credential), "PhoneAuthCredential is : ");
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
    }

    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpVerificationId = verificationId;
                        Log.i(otpVerificationId, "otp id is: ");
                        System.out.println("otp id is :"+otpVerificationId);
                        System.out.println("token is :"+forceResendingToken);
                        Log.i(otpVerificationId, "token is: ");
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is authenticated.
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                            User user = new User(uid,phoneNumber);
                            String userid=user.getUid();
                            String userphonenumber= user.getPhoneNumber();
                            // Call the user utility to send the POST request to store the user profile in db
                            UserApiUtil userApiUtil = new UserApiUtil();
                            userApiUtil.sendPostRequest("http://192.168.1.9:8080/api/users", user);
                            startActivity(new Intent(VerifyOtpActivity.this, HomepageActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Signin Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}













