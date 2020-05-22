package com.example.appwatchhicker2;

import androidx.annotation.NonNull;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Locate getLocation = new Locate();
        getLocation.execute();
    }
    public void grantPermission(){

        if(Build.VERSION.SDK_INT > 23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    updateLocationInfo(location);
                }
            }
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
    public class Locate extends AsyncTask<LocationManager,Void,Void>{
        @Override
        protected Void doInBackground(LocationManager... locationManagers) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            grantPermission();
            return null;
        }

    }
    public void updateLocationInfo(final Location location){

        Log.i("LocationInfo", "locate");
        TextView latTextView = (TextView) findViewById(R.id.latTextView);

        TextView lonTextView = (TextView) findViewById(R.id.lonTextView);

        TextView altTextView = (TextView) findViewById(R.id.altTextView);

        TextView accTextView = (TextView) findViewById(R.id.accTextView);

        latTextView.setText("Latitude: " + location.getLatitude());

        lonTextView.setText("Longitude: " + location.getLongitude());

        altTextView.setText("Altitude: " + location.getAltitude());

        accTextView.setText("Accuracy: " + location.getAccuracy());
        Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String add = "Could not find Address";
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList != null){
                Log.i("placeInfo",addressList.get(0).toString());
                add = "";
                if(addressList.get(0).getSubThoroughfare() != null)
                    add += addressList.get(0).getSubThoroughfare()+" ";
                if(addressList.get(0).getThoroughfare() != null)
                    add += addressList.get(0).getThoroughfare()+" ";
                if(addressList.get(0).getLocality() != null)
                    add += addressList.get(0).getLocality()+" ";
                if(addressList.get(0).getPostalCode() != null)
                    add += addressList.get(0).getPostalCode()+" ";
                if(addressList.get(0).getCountryName() != null)
                    add += addressList.get(0).getCountryName()+" ";
            }
            TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
            addressTextView.setText(add);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

}


