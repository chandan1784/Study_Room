package com.example.studyroom.api;

import com.example.studyroom.model.User;

public class UserApiUtil {
    public void sendPostRequest(User user) {
        String apiEndpointUrl = "http://localhost:8080/api/users";
        NetworkManagerPost<User> manager = new NetworkManagerPost<>(apiEndpointUrl, user);
        manager.execute();
    }
}
