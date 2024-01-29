package com.example.studyroom.api;

import android.os.AsyncTask;

import com.example.studyroom.model.User;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import java.nio.charset.StandardCharsets;

public class NetworkManager extends AsyncTask<Void, Void, Integer> {

    private String apiUrl;
    private User yourObject;

    public NetworkManager(String apiUrl, User yourObject) {
        this.apiUrl = apiUrl;
        this.yourObject = yourObject;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            URL url = new URL(apiUrl);
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
            System.out.println("Response Code: " + responseCode);

            // Handle the response as needed

            connection.disconnect();
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return an error code or handle errors accordingly
        }
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        // This method is called on the main thread after the background task completes
        // You can update UI or perform any post-execution tasks here
        System.out.println("AsyncTask completed with Response Code: " + responseCode);
        Log.i(String.valueOf(responseCode), "response code is :");
    }
}