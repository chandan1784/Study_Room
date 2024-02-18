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

public class ReverseGeocodingTask extends AsyncTask<Void, Void, JSONObject> {

    private String apiUrl;
    private ReverseGeocodeListener listener;

    public ReverseGeocodingTask(String apiUrl, ReverseGeocodeListener listener) {
        this.apiUrl=apiUrl;
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject addressJson = null;
        try {
            URL url = new URL(apiUrl);
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
            addressJson = jsonObject;
            JSONArray results = addressJson.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                Log.d("Reverse Geocode", "addressJson: " + addressJson);
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

        return addressJson;
    }

    @Override
    protected void onPostExecute(JSONObject addressJson) {
        if (listener != null) {
            listener.onReverseGeocodeCompleted(addressJson);
        }
    }

    public interface ReverseGeocodeListener {
        void onReverseGeocodeCompleted(JSONObject addressJson);
    }
}