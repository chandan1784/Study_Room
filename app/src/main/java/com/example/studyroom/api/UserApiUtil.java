package com.example.studyroom.api;

import com.example.studyroom.model.User;

public class UserApiUtil {
    public void sendPostRequest(String yourApiEndpoint, User user) {
        NetworkManager networkManager = new NetworkManager(yourApiEndpoint, user);
        networkManager.execute();
    }
}
