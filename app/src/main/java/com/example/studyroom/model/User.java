package com.example.studyroom.model;

import com.example.studyroom.api.NetworkManager;

public class User implements NetworkManager.NetworkListener {
    private String uid;
    private String phoneNumber;

    public User(String uid, String phoneNumber) {
        this.uid = uid;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public void onNetworkCompleted(ResponseApi responseApi) {

    }
}
