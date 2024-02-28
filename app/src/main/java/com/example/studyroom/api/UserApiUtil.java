package com.example.studyroom.api;
import com.example.studyroom.model.User;

public class UserApiUtil{
    public void sendPostRequest(User user) {
        String apiEndpointUrl = "http://localhost:8080/api/users";
        NetworkManager<User> manager = new NetworkManager<>(apiEndpointUrl, user,"POST");
        manager.execute();
    }

    public void getJson(String phoneNumber, NetworkManager.NetworkListener listener) {
        String apiEndpointUrl = "http://localhost:8080/api/users/phoneNumber/"+phoneNumber;
        NetworkManager<NetworkManager.NetworkListener> manager = new NetworkManager<>(apiEndpointUrl,listener,"GET");
        manager.execute();
    }
}
