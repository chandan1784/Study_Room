package com.example.studyroom.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.studyroom.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyroom.api.NearestLibrariesApiUtil;
import com.example.studyroom.api.NetworkManager;
import com.example.studyroom.api.ReverseGeocodingUtil;
import com.example.studyroom.model.ResponseApi;
import com.example.studyroom.ui.adapter.LibraryRecyclerViewAdapter;
import com.example.studyroom.ui.fragments.AboutFragment;
import com.example.studyroom.ui.fragments.HomeFragment;
import com.example.studyroom.ui.fragments.ManageprofileFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import java.util.Arrays;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private boolean autocompleteInitialized = false;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Find RecyclerView in onCreate()
        recyclerView = findViewById(R.id.recyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.black)); // Set the color
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //For testing Reverse Geocoding API without using Autocomplete API
        double latitude = 26.6830283;
        double longitude = 85.66869009999999;
        LatLng latLng = new LatLng(latitude,longitude);
        System.out.println("latitude is:" + latitude);
        System.out.println("longitude is:" + longitude);
        reverseGeocode(latLng);

        // Initialize Autocomplete API
        if (!autocompleteInitialized) {
            initializeAutocompleteFragment();
            autocompleteInitialized = true;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageprofileFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void initializeAutocompleteFragment() {
        // Initialize Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.API_KEY));
        }
        PlacesClient placesClient = Places.createClient(this);

        // Add AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = new AutocompleteSupportFragment();
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                System.out.println("latitude is:" + latitude);
                System.out.println("longitude is:" + longitude);
                //reverseGeocode(latLng);
                Toast.makeText(HomepageActivity.this, "Selected place: " + place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                // Handle the error
                Toast.makeText(HomepageActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.autocomplete_fragment_container, autocompleteFragment);
        ft.commit();
    }

    private void reverseGeocode(LatLng latLng) {


      /*  ReverseGeocodingUtil reverseGeocodingUtil = new ReverseGeocodingUtil();
        reverseGeocodingUtil.getJson(latLng, new NetworkManager.NetworkListener() {
            @Override
            public void onNetworkCompleted(ResponseApi responseApi) {
                System.out.println("addressJson inside HomepageActivity :" + responseApi.getResponseObject());
            }
        });*/


        NearestLibrariesApiUtil nearestLibrariesApiUtil = new NearestLibrariesApiUtil();
        nearestLibrariesApiUtil.getJson(latLng, new NetworkManager.NetworkListener() {
            @Override
            public void onNetworkCompleted(ResponseApi responseApi) {
                System.out.println("addressJson inside HomepageActivity :" + responseApi.getResponseObject());
                // Process the response to get list of libraries and display them
                // Delegate population to LibraryManager
               LibraryManager.populateLibraries(HomepageActivity.this, recyclerView, responseApi);

            }
        } );


    }


}