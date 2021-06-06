package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akshay.otptest.Common.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeHome extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap mMap;
    Button button, setLocation;
    double oldLat, oldLong;
    double newLat, newLong;
    String userPhoneNumber;
    private FusedLocationProviderClient mLocationClient;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_home);

        userPhoneNumber = Common.CurrentUserModel.getPhoneNumber();

        mapView = findViewById(R.id.selectHome);
        button = findViewById(R.id.current_loc);
        setLocation = findViewById(R.id.setLocation);

        mLocationClient = new FusedLocationProviderClient(this);
        reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GPSisEnabled()) {
                    getCurrentLocation();
                }
            }
        });

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UpdateLoc(newLat, newLong)){
                    Intent intent = new Intent(ChangeHome.this,HomePageMaps.class);
                    startActivity(intent);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private boolean GPSisEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnable) {
            return true;
        } else {
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setTitle("Enable location")
                    .setMessage("Enable GPS to access this feature")
                    .setPositiveButton("enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMarker();
        MarkerOptions options = new MarkerOptions();
        LatLng latLng = new LatLng(oldLat, oldLong);
        options.position(latLng);
        options.title("Current Location");
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(options);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (options != null){
                    mMap.clear();
                }
                MarkerOptions options1 = new MarkerOptions();
                options1.position(latLng);
                options1.title("New Location");
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 19);
                mMap.animateCamera(cameraUpdate);
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.moveCamera(cameraUpdate);
                mMap.addMarker(options1);
                newLat = latLng.latitude;
                Log.d("new Lat",String.valueOf(newLat));
                newLong = latLng.longitude;
                UpdateLoc(newLat,newLong);
            }
        });

    }

    private boolean UpdateLoc(double newlat, double newlong) {
        reference.child(userPhoneNumber).child("lattitude").setValue(newlat);
        reference.child(userPhoneNumber).child("longitude").setValue(newlong);
        return  true;
    }

    private void getMarker() {
        oldLat = Common.CurrentUserModel.getLattitude();
        oldLong = Common.CurrentUserModel.getLongitude();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

}