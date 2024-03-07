package com.example.studyroom.ui.activity;

import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyroom.R;
import com.example.studyroom.model.Address;
import com.example.studyroom.model.Library;
import com.example.studyroom.model.ResponseApi;
import com.example.studyroom.ui.adapter.LibraryAdapter;
import com.example.studyroom.ui.adapter.LibraryRecyclerViewAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {

    protected static void populateLibraries(HomepageActivity homepageActivity, RecyclerView recyclerView, ResponseApi responseApi) {
        try {
            JSONArray results = responseApi.getResponseObject().getJSONArray("results");
            List<Library> libraries = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject libraryJson = results.getJSONObject(i);
                Library library = new Library();

                // Set the name of the library
                library.setName(libraryJson.getString("name"));

                // Extract district details
                JSONObject districtJson = libraryJson.getJSONObject("district");
                String districtName = districtJson.getString("name");
                library.setDistrict(districtName);

                // Extract locality details
                JSONObject localityJson = libraryJson.getJSONObject("locality");
                String localityName = localityJson.getString("name");
                library.setLocality(localityName);

                // Create address object
                Address address = new Address();
                // Set district and locality for the address
                address.setDistrict(districtName);
                address.setLocality(localityName);

                // Set address for the library
                library.setAddress(address);

                // Add the library to the list
                libraries.add(library);
            }


            // Set LayoutManager for RecyclerView (e.g., LinearLayoutManager or GridLayoutManager)
            recyclerView.setLayoutManager(new LinearLayoutManager(homepageActivity));

            // Set LayoutManager for RecyclerView (e.g., LinearLayoutManager or GridLayoutManager)
            recyclerView.setLayoutManager(new LinearLayoutManager(homepageActivity));
            // Create adapter using LibraryRecyclerViewAdapter class
            LibraryRecyclerViewAdapter adapter = new LibraryRecyclerViewAdapter(homepageActivity, libraries);
            // Set adapter to RecyclerView
            recyclerView.setAdapter(adapter);


            /*// Populate the libraries in the list view
            // Get reference to ListView
            ListView listView = homepageActivity.findViewById(R.id.listView);

            // Create adapter using LibraryAdapter class
            LibraryAdapter adapter = new LibraryAdapter(homepageActivity, libraries);

            // Set adapter to ListView
            listView.setAdapter(adapter);*/

        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
