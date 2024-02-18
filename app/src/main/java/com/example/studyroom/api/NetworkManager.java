package com.example.studyroom.api;

import android.os.AsyncTask;

import com.example.studyroom.model.ResponseApi;
import com.example.studyroom.model.User;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import java.nio.charset.StandardCharsets;

public class NetworkManager<T> extends AsyncTask<Void, Void, ResponseApi> {

    private final String apiEndpointUrl;
    private final T yourObject;
    public NetworkManager(String apiEndpointUrl, T yourObject) {
        this.apiEndpointUrl = apiEndpointUrl;
        this.yourObject = yourObject;
    }
    @Override
    protected ResponseApi doInBackground(Void... voids) {
        ResponseApi responseApi = new ResponseApi();
        try {
            URL url = new URL(apiEndpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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

            // Handle the response as needed

            connection.disconnect();
            return responseApi;
        } catch (Exception e) {
            e.printStackTrace();
            responseApi.setResponseCode(-1); // Return an error code or handle errors accordingly
            return responseApi;
        }
    }

    @Override
    protected void onPostExecute(ResponseApi responseApi) {
        // This method is called on the main thread after the background task completes
        // You can update UI or perform any post-execution tasks here
        System.out.println("AsyncTask completed with Response Code: " + responseApi.getResponseCode());
        Log.i(String.valueOf(responseApi.getResponseCode()), "response code is :");
    }
}