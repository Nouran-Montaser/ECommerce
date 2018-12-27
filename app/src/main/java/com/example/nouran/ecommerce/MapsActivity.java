package com.example.nouran.ecommerce;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private String addr ="";

    Button getLocationBtn;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        getLocationBtn = findViewById(R.id.map_btn);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener(getApplicationContext());

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


//        try{
////            locationManager.getLastKnownLocation();
//            locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER,6000,0,locationListener);
//        }catch (SecurityException ex)
//        {
//            Toast.makeText(getApplicationContext(), "You are not allowed to access the current location", Toast.LENGTH_SHORT).show();
//
//        }
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("Location",addr);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (userCurrentLocation != null) {
                MarkerOptions currentUserLocation = new MarkerOptions();
                LatLng currentUserLatLang = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                currentUserLocation.position(currentUserLatLang);
                currentUserLocation.draggable(true);
                mMap.addMarker(currentUserLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 16));



                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addList;
                LatLng location = null;
                location = currentUserLocation.getPosition();
                if (location != null) {
                    LatLng position = location;
                    try {
                        addList = coder.getFromLocation(position.latitude, position.longitude, 1);

                        if (!addList.isEmpty()) {
                            addr = "";
                            addr = addList.get(0).getAddressLine(0);
//                            for (int i = 0; i < addList.get(0).getMaxAddressLineIndex(); i++) {
//                                addr += addList.get(0).getAddressLine(i) + ", ";
//                            }
                            mMap.addMarker(new MarkerOptions().position(position).title("My Location").snippet(addr)).setDraggable(true);
                        } else {
                            Toast.makeText(MapsActivity.this, "no address for this location", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException ex) {
                        Toast.makeText(MapsActivity.this, "nCan't get the address ,check your network", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(MapsActivity.this, "No Permitions Granted", Toast.LENGTH_SHORT).show();
        }
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.04441960, 31.235711600), 8));
//        getLocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMap.clear();
//                Geocoder geocoder = new Geocoder(getApplicationContext());
//                List<Address> addressList;
//                Location location = null;
//
//                try {
//                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//                } catch (SecurityException ex) {
//                    Toast.makeText(getApplicationContext(), "You are not allowed to access the current location", Toast.LENGTH_SHORT).show();
//                }
//                if (location != null) {
//                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//                    try {
//                        addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);
//                        if (!addressList.isEmpty()) {
//                            String addr = "";
//                            for (int i = 0; i < addressList.get(0).getMaxAddressLineIndex(); i++) {
//                                addr += addressList.get(0).getAddressLine(i) + ", ";
//                            }
//                            mMap.addMarker(new MarkerOptions().position(position).title("My Location").snippet(addr)).setDraggable(true);
//                            Log.i("FFFFFf", addr);
//                            mapEtxt.setText(addr);
//                        }
//                    } catch (IOException ex) {
//                        mMap.addMarker(new MarkerOptions().position(position).title("My Location"));
//
//                    }
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
//                } else {
//                    Toast.makeText(MapsActivity.this, "Please wait untill your position is determined", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> addList;
                LatLng location = null;

//                try {
                    location = marker.getPosition();
//                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//                } catch (SecurityException ex) {
//                    Toast.makeText(getApplicationContext(), "You are not allowed to access the current location", Toast.LENGTH_SHORT).show();
//                }
                if (location != null) {
                    mMap.clear();
                    LatLng position = location;
                    try {
                        addList = coder.getFromLocation(position.latitude, position.longitude, 1);
                        if (!addList.isEmpty()) {
                             addr = "";
//                            for (int i = 0; i < addList.get(0).getMaxAddressLineIndex(); i++) {
//                                addr += addList.get(0).getAddressLine(i) + ", ";
//                            }
                            addr = addList.get(0).getAddressLine(0);
                            mMap.addMarker(new MarkerOptions().position(position).title("My Location").snippet(addr)).setDraggable(true);
//                            mapEtxt.setText(addr);
                        } else {
                            Toast.makeText(MapsActivity.this, "no address for this location", Toast.LENGTH_SHORT).show();
//                            mapEtxt.getText().clear();
                        }
                    } catch (IOException ex) {
                        Toast.makeText(MapsActivity.this, "nCan't get the address ,check your network", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

