package com.mdolzhansky.sensors_geolocation_wifi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener, OnConnectionFailedListener {
    Button btnGet;
    Button btnSend;
    double longPosition;
    double latPosition;
    TextView txtVw_longPosition, txtVw_currLongPosition;
    TextView txtVw_latPosition, txtVw_currLatPosition;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtVw_longPosition = (TextView) findViewById(R.id.txtVw_longPosition);
        txtVw_currLongPosition = (TextView) findViewById(R.id.txtVw_currLongPosition);
        txtVw_latPosition = (TextView) findViewById(R.id.txtVw_latPosition);
        txtVw_currLatPosition = (TextView) findViewById(R.id.txtVw_currLatPosition);
        btnGet = (Button) findViewById(R.id.btn_get);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnGet.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("Connected?", String.valueOf(mGoogleApiClient.isConnected()));
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("longitude", longPosition);
                intent.putExtra("latitude", latPosition);
                MainActivity.this.startActivityForResult(intent, 1);
                break;
            case R.id.btn_send:
                sendResult();
        }
    }

    private void sendResult() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Double latFromMap = data.getDoubleExtra("lat_from_map", 0);
        Double longFromMap = data.getDoubleExtra("long_from_map", 0);
        txtVw_currLatPosition.setText(String.valueOf(latFromMap));
        txtVw_currLongPosition.setText(String.valueOf(longFromMap));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(this, "SelfPermission", Toast.LENGTH_SHORT).show();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latPosition = mLastLocation.getLatitude();
            longPosition = mLastLocation.getLongitude();
            txtVw_latPosition.setText(String.valueOf(latPosition));
            txtVw_longPosition.setText(String.valueOf(longPosition));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
