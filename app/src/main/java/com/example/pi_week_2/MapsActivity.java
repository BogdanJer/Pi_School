package com.example.pi_week_2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.pi_week_2.MainActivity.USER_TAG;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int REQUEST_GPS = 2;
    private final String LOCATION_TAG = "Location permission";
    private FloatingActionButton fab;

    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private GoogleApiClient googleApiClient;

    private GoogleMap googleMap;
    private Marker marker;

    private double latitude;
    private double longitude;

    private boolean tracking = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback();

        fab = findViewById(R.id.find_me_fab);

        //  if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission is necessary for find your location or any other place to search photos by location tag",
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
            mapFragment.getMapAsync(this);
            Log.d(LOCATION_TAG, "granted");
        }
       /* }else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
            mapFragment.getMapAsync(this);
            Log.d(LOCATION_TAG,"granted");
        }*/

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location loc = locationResult.getLastLocation();

                latitude = loc.getLatitude();
                longitude = loc.getLongitude();

                LatLng pos = new LatLng(latitude, longitude);
                marker = googleMap.addMarker(new MarkerOptions().position(pos).title("My location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.main_screen).setVisible(true);
        menu.findItem(R.id.change_view).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.history) {
            intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(USER_TAG, MainActivity.name);
            startActivity(intent);
        } else if (id == R.id.main_screen) {
            finish();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
                    mapFragment.getMapAsync(this);
                    Log.i("Permission", "Ok");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(latLng -> {
            if (marker != null)
                marker.remove();
            marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Somewhere"));
            latitude = latLng.latitude;
            longitude = latLng.longitude;
        });
    }

    public void findMeLocation(View view) {
        if (!tracking) {
            startTracking();
        } else {
            stopTracking();
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("GPS", "ON");
                        fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                                locationCallback, null);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), ":(", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(20000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(10000);

        return locationRequest;
    }

    public void findPhotosByLocation(View view) {
        Intent intent = new Intent(this, MapPhotosActivity.class);
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        startActivity(intent);
    }

    private void startTracking() {
        LocationRequest locReq = getLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locReq);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i("GPS", "ON");
                    fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                            locationCallback, null);
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(
                                MapsActivity.this,
                                REQUEST_GPS);

                    } catch (IntentSender.SendIntentException e) {
                        Log.e("Permission answer", "Denied");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Toast.makeText(getBaseContext(), "GPS isn't available!", Toast.LENGTH_LONG).show();
                    break;
            }
        });
        tracking = true;
        fab.setImageDrawable(getResources().getDrawable(R.drawable.my_location_on));
    }

    private void stopTracking() {
        tracking = false;
        fab.setImageDrawable(getResources().getDrawable(R.drawable.my_location));
    }

}
