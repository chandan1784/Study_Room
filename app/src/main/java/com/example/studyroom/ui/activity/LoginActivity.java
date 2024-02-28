package com.example.studyroom.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.studyroom.R;
import com.example.studyroom.api.NetworkManager;
import com.example.studyroom.api.UserApiUtil;
import com.example.studyroom.model.ResponseApi;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CountryCodePicker countryCodePicker = findViewById(R.id.login_countrycode);
        EditText mobileNumber = findViewById(R.id.editTextPhoneNumber);
        Button buttonContinue = findViewById(R.id.buttonContinue);
        countryCodePicker.registerCarrierNumberEditText(mobileNumber);
        buttonContinue.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){
                mobileNumber.setError("Phone number not valid");
                return;
            }
            UserApiUtil user = new UserApiUtil();
            String phoneNumber = countryCodePicker.getFullNumberWithPlus();

            user.getJson(phoneNumber, new NetworkManager.NetworkListener(){

                @Override
                public void onNetworkCompleted(ResponseApi responseApi) {
                  if(responseApi.getResponseObject()==null){
                      Intent intent = new Intent(LoginActivity.this, VerifyOtpActivity.class);
                      intent.putExtra("mobile",countryCodePicker.getFullNumberWithPlus());
                      startActivity(intent);
                  }
                  else{
                      Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                      startActivity(intent);
                  }
                }
            } );


        });
    }
}