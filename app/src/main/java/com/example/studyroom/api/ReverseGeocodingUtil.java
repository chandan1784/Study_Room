package com.example.studyroom.api;
import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocodingUtil {

    public void getJson(LatLng latLng,  NetworkManager.NetworkListener listener) {
       String apiEndpointUrl = "http://localhost:port_no/api/users/welcome";
        //String apiEndpointUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&key=YOUR_API_KEY";
        NetworkManager<NetworkManager.NetworkListener> manager = new NetworkManager<>(apiEndpointUrl,listener,"GET");
        manager.execute();
    }
}

