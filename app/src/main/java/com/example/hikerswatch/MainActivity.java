package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);

            }
        };

        // Checking if we have permission

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        // permission denied here
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            // permission granted
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null){

            }
        }

    }

    // If someone says yes or no to our asked permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();

        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }

    }

    public void  updateLocationInfo(Location location) {
        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView arcTextView = findViewById(R.id.arcTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView addTextView = findViewById(R.id.addTextView);


        latTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: " + Double.toString(location.getLongitude()));
        arcTextView.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: " + Double.toString(location.getAltitude()));

        // Hail mary message

        String address = "Could not find address  :(";

        // Address issue

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if(listAddresses != null && listAddresses.size() > 0){
                address = "Address:\n";


                // to get different pieces of info
                if(listAddresses.get(0).getThoroughfare() != null){
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if(listAddresses.get(0).getLocality() != null){
                    address += listAddresses.get(0).getLocality() + " ";
                }

                if(listAddresses.get(0).getPostalCode() != null){
                    address += listAddresses.get(0).getPostalCode() + " ";
                }

                if(listAddresses.get(0).getAdminArea() != null){
                    address += listAddresses.get(0).getAdminArea();
                }
            }
        }


        catch(Exception e){
            e.printStackTrace();
        }

        // in case our address has some issues, we write it at the outside just for some backup
        addTextView.setText(address);

    }
}