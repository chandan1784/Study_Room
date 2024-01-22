package com.example.studyroom.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.studyroom.R;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CountryCodePicker countryCodePicker = findViewById(R.id.login_countrycode);
        EditText mobileNumber = findViewById(R.id.editTextPhoneNumber);
        Button sendOtpBtn = findViewById(R.id.buttonContinue);
        countryCodePicker.registerCarrierNumberEditText(mobileNumber);
        sendOtpBtn.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){
                mobileNumber.setError("Phone number not valid");
                return;
            }
            Intent intent = new Intent(LoginActivity.this, VerifyOtpActivity.class);
            intent.putExtra("mobile",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}