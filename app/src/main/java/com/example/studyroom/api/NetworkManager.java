package com.example.studyroom.api;

import android.os.AsyncTask;

import com.example.studyroom.model.ResponseApi;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class NetworkManager<T extends NetworkManager.NetworkListener> extends AsyncTask<Void, Void, ResponseApi> {

    private final String apiEndpointUrl;
    private final T yourObject;

    private final String requestType;

    public NetworkManager(String apiEndpointUrl, T yourObject,String requestType) {
        this.apiEndpointUrl = apiEndpointUrl;
        this.yourObject = yourObject;
        this.requestType = requestType;
    }
    @Override
    protected ResponseApi doInBackground(Void... voids) {
        switch (requestType) {
            case "GET":
                return executeGetRequest();
            case "POST":
                return executePostRequest();
            default:
                return null;
        }
    }

    //POST request
    private ResponseApi executePostRequest() {
        ResponseApi responseApi = new ResponseApi();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiEndpointUrl);
            connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Convert your object to JSON
            Gson gson = new Gson();
            String jsonBody = gson.toJson(yourObject);

            // Write the JSON data to the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response from the server
            int responseCode = connection.getResponseCode();
            responseApi.setResponseCode(responseCode);

            System.out.println("Response Code: " + responseCode);

            return responseApi;
        } catch (Exception e) {
            e.printStackTrace();
            responseApi.setResponseCode(-1); // Return an error code or handle errors accordingly
            return responseApi;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    //GET request
    private ResponseApi executeGetRequest()  {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject addressJson = null;
        ResponseApi responseApi = new ResponseApi(); // Initialize responseApi
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
            Log.d("Initial Response", "Response: " + response);

            // Check the Content-Type header
            String contentType = connection.getContentType();

            //if (response.startsWith("{")){
            if (contentType != null && contentType.startsWith("application/json")) {
                addressJson = new JSONObject(response);
            }
            else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", response);
                    addressJson=jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            responseApi.setResponseObject(addressJson);
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

        return responseApi;
    }
    @Override
    protected void onPostExecute(ResponseApi responseApi) {
        if (yourObject != null) {
            yourObject.onNetworkCompleted(responseApi);
        }
    }

    public interface NetworkListener {
        void onNetworkCompleted(ResponseApi responseApi);
    }
}