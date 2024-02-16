package com.example.studyroom.api;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReverseGeocodingTask extends AsyncTask<Double, Void, String> {

    private ReverseGeocodeListener listener;

    public ReverseGeocodingTask(ReverseGeocodeListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Double... params) {
        double latitude = params[0];
        double longitude = params[1];

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String formattedAddress = null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=YOUR_API_KEY");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String response = builder.toString();
            Log.d("Reverse Geocode", "Response: " + response);

            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                formattedAddress = result.getString("formatted_address");
                Log.d("Reverse Geocode", "Formatted Address: " + formattedAddress);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return formattedAddress;
    }

    @Override
    protected void onPostExecute(String formattedAddress) {
        if (listener != null) {
            listener.onReverseGeocodeCompleted(formattedAddress);
        }
    }

    public interface ReverseGeocodeListener {
        void onReverseGeocodeCompleted(String formattedAddress);
    }
}