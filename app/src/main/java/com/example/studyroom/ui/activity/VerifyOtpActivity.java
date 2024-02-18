package com.example.studyroom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyroom.R;
import com.example.studyroom.api.UserApiUtil;
import com.example.studyroom.model.User;
import com.example.studyroom.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {
    EditText enteredOtp;
    Button verifyButton;
    String mobileNumber;
    String otpVerificationId;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    Long timeoutSeconds = 45L;
    PhoneAuthProvider.ForceResendingToken  resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mobileNumber = getIntent().getStringExtra("mobile");
        enteredOtp = (EditText) findViewById(R.id.editTextOtp);
        verifyButton = (Button) findViewById(R.id.buttonVerify);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.textViewResend);
        mAuth = FirebaseAuth.getInstance();

        initiateOtp(mobileNumber,false);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enteredOtp.getText().toString().isEmpty() || enteredOtp.getText()==null)
                    Toast.makeText(getApplicationContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
                else if (enteredOtp.getText().toString().length() != 6)
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                else {
                    if (otpVerificationId != null && !otpVerificationId.isEmpty()) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId, enteredOtp.getText().toString());
                        System.out.println("PhoneAuthCredential is : " + credential);
                        Log.d(String.valueOf(credential), "PhoneAuthCredential is : ");
                        signInWithPhoneAuthCredential(credential);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Verification ID is null or empty", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        resendOtpTextView.setOnClickListener((v)->{
            initiateOtp(mobileNumber,true);
        });
    }

    private void initiateOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                timeoutSeconds,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {*/
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpVerificationId = verificationId;
                        Log.i(otpVerificationId, "otp id is: ");
                        System.out.println("otp id is :" + otpVerificationId);
                        System.out.println("token is :" + forceResendingToken);
                        Log.i(otpVerificationId, "token is: ");
                        AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully");
                        setInProgress(false);
                        resendingToken = forceResendingToken;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        setInProgress(false);
                    }
                });        // OnVerificationStateChangedCallbacks

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        setInProgress(true);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setInProgress(false);
                        if (task.isSuccessful()) {
                            // User is authenticated.
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                            User user = new User(uid, phoneNumber);
                            String userid = user.getUid();
                            String userphonenumber = user.getPhoneNumber();
                            // Call the user utility to send the POST request to store the user profile in db
                            UserApiUtil userApiUtil = new UserApiUtil();
                            userApiUtil.sendPostRequest(user);
                            startActivity(new Intent(VerifyOtpActivity.this, HomepageActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            verifyButton.setVisibility(View.VISIBLE);
        }
    }

    void startResendTimer() {
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();

        Handler handler = new Handler(Looper.getMainLooper());

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;

                // Use handler to update UI on the main thread
                handler.post(() -> {
                    resendOtpTextView.setText("Resend OTP in " + timeoutSeconds + " seconds");
                });

                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L;
                    timer.cancel();

                    // Use handler to enable the resend button on the main thread
                    handler.post(() -> {
                        resendOtpTextView.setEnabled(true);
                    });
                }
            }
        }, 0, 1000);
    }

}













