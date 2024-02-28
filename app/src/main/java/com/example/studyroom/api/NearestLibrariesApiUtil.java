package com.example.studyroom.api;

import com.google.android.gms.maps.model.LatLng;

public class NearestLibrariesApiUtil {
    public void getJson(LatLng latLng, NetworkManager.NetworkListener listener) {
        String apiEndpointUrl = "http://localhost:9090/libraries/nearest-libraries?localityName=bailey&userLat=25.6051&userLon=85.0849&limit=10";
        NetworkManager<NetworkManager.NetworkListener> manager = new NetworkManager<>(apiEndpointUrl,listener,"GET");
        manager.execute();
    }
}
