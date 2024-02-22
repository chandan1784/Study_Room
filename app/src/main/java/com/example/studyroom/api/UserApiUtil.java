package com.example.studyroom.api;

import com.example.studyroom.model.User;

public class UserApiUtil{
    public void sendPostRequest(User user) {
        String apiEndpointUrl = "http://localhost:port_no/api/users";
        NetworkManager<User> manager = new NetworkManager<>(apiEndpointUrl, user,"POST");
        manager.execute();
    }
}
