package com.mdolzhansky.sensors_geolocation_wifi;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    String currPosition;
    Button btnOk;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private double longPosition;
    private double latPosition;
    MarkerOptions markerOpt;
    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        Intent intent = getIntent();
        longPosition = intent.getDoubleExtra("longitude",0);
        latPosition = intent.getDoubleExtra("latitude",0);

        LatLng place = new LatLng(latPosition, longPosition);

        MarkerOptions markerOpt = new MarkerOptions()
                .position(place)
                .title("Marker is in your current position")
                .draggable(true);

        marker = mMap.addMarker(markerOpt);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,16f));
       // mMap.animateCamera(CameraUpdateFactory.zoomIn());


    }

    @Override
    public void onClick(View v) {
        LatLng position = marker.getPosition();

        Intent intent = new Intent();
        intent.putExtra("lat_from_map", position.latitude);
        intent.putExtra("long_from_map", position.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }


}
