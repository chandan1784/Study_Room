package com.example.studyroom.api;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkManagerGet<T extends NetworkManagerGet.NetworkListener> extends AsyncTask<Void, Void, JSONObject> {

    private final String apiEndpointUrl;
    private final T yourObject;

    public NetworkManagerGet(String apiEndpointUrl, T yourObject ){
        this.apiEndpointUrl = apiEndpointUrl;
        this.yourObject = yourObject;
    }

    //GET request
    @Override
    protected JSONObject doInBackground(Void... voids) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject addressJson = null;
        try {
            URL url = new URL(apiEndpointUrl);
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

            // Check the Content-Type header
            String contentType = connection.getContentType();

            //if (response.startsWith("{")){
            if (contentType != null && contentType.startsWith("application/json")) {
                addressJson = new JSONObject(response);}
            else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", response);
                    addressJson=jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            /*JSONArray results = addressJson.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                Log.d("Reverse Geocode", "addressJson: " + addressJson);
            }*/
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
    protected void onPostExecute( JSONObject addressJson) {
        if (yourObject != null) {
            yourObject.onNetworkCompleted(addressJson);
        }
    }

    public interface NetworkListener {
        void onNetworkCompleted(JSONObject addressJson);
    }
}