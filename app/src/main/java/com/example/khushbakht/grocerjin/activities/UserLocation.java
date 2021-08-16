package com.example.khushbakht.grocerjin.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserLocation extends AppCompatActivity implements OnMapReadyCallback {

    String latitude, longitude;
    Double lat, lng;
    LatLng currentLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latitude = getIntent().getStringExtra("lat");
        longitude = getIntent().getStringExtra("lng");
        if (!latitude.equalsIgnoreCase("")&& !longitude.equalsIgnoreCase("")) {
            lat = Double.valueOf(latitude);
            lng = Double.valueOf(longitude);
        }
        setContentView(R.layout.activity_user_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocation.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
        TextView header = (TextView) findViewById(R.id.headerText);
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);
        header.setText("User Location");
        initilizeMap();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        //gpsTrackerService.getLocation();
//        origin = new LatLng(currentlat, currentlng);
        if (lat!= null && lng!= null) {
            currentLoc = new LatLng(lat, lng);


            map.addMarker(new MarkerOptions().position(currentLoc));
//            map.addMarker(new MarkerOptions().position(origin));
            //Polyline polyline = map.addPolyline(new PolylineOptions().clickable(true).add(origin).add(destination));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,15));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

//        polyline.setColor(Color.RED);
//        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }
    private void initilizeMap() {
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.profileMap));
        mapFragment.getMapAsync(this);

    }

}
