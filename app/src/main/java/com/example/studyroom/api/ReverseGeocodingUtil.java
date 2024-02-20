package com.example.studyroom.api;
import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocodingUtil {

   public void getJson(LatLng latLng,  NetworkManagerGet.NetworkListener listener) {
        //String apiEndpointUrl = "http://localhost:8080/welcome";
        String apiEndpointUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&key=YOUR_API_KEY";
        NetworkManagerGet<NetworkManagerGet.NetworkListener> manager = new NetworkManagerGet<>(apiEndpointUrl,listener);
        manager.execute();
    }
}

