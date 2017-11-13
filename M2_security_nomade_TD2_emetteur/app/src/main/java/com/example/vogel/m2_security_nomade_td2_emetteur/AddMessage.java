package com.example.vogel.m2_security_nomade_td2_emetteur;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vogel.m2_security_nomade_td2_emetteur.utils.PkgCert;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AddMessage extends AppCompatActivity implements LocationListener {

    private EditText message;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        message = (EditText) findViewById(R.id.message);
        Button validate = (Button)findViewById(R.id.envoi);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {

            Toast.makeText(getBaseContext(), "No permission 1 ", Toast.LENGTH_LONG).show();
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permissions,42);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,   // 3 sec
                10, this);

        latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        Log.e("latitude", latitude+"");
        Log.e("longitude", longitude+"");
        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*String hash_app = PkgCert.hash(getApplicationContext(),
                        "com.example.vogel.m2_security_nomade_td2_emetteur");
                Log.e("hash", hash_app);*/

                Intent intent = new Intent();
                intent.setAction("com.example.vogel.m2_security_nomade_td2_emetteur");
                intent.putExtra("message", message.getText().toString()+"");
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                /*intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);*/
                /*intent.setComponent(
                        new ComponentName("com.example.vogel.m2_security_nomade_td2",
                                "com.example.vogel.m2_security_nomade_td2.activities.MyBroadcast")
                );*/
                sendBroadcast(intent, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e("latitude", latitude+"");
        Log.e("longitude", longitude+"");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PERMISSION_GRANTED )
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        3000,   // 3 sec
                        10, this);
            }
            catch (SecurityException e) {}
        else
        {
            for (int i=0;i<permissions.length;++i)
                Log.d("Permissions",permissions[i]+" - "+grantResults[i]);
            Toast.makeText(getBaseContext(), "No permission 2 ", Toast.LENGTH_LONG).show();
        }

    }
}
