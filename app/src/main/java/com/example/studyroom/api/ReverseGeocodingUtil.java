package com.example.studyroom.api;
import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocodingUtil {

    public void getJson(LatLng latLng, ReverseGeocodingTask.ReverseGeocodeListener listener) {
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&key=YOUR_API_KEY";
        ReverseGeocodingTask reverseGeocodingTask = new ReverseGeocodingTask(apiUrl,listener);
        reverseGeocodingTask.execute();
    }
}

