package com.example.lostfoundapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new Thread(() -> {
            List<Item> items = dbHelper.getAllItemsList();
            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boolean hasMarkers = false;

            for (Item item : items) {
                try {
                    // Skip empty or invalid locations
                    if (item.location == null || item.location.trim().isEmpty()) continue;
                    if (item.location.length() < 3 || item.location.matches(".*\\d.*")) continue;

                    List<Address> addresses = geocoder.getFromLocationName(item.location, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        boundsBuilder.include(latLng);
                        hasMarkers = true;

                        runOnUiThread(() -> {
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(item.type + ": " + item.description));
                        });

                        Thread.sleep(300); // Avoid geocoder overload
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(MapActivity.this, "Couldn't locate: " + item.location, Toast.LENGTH_SHORT).show());
                }
            }

            // Move camera only once at the end
            if (hasMarkers) {
                runOnUiThread(() -> {
                    try {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
    }
}
