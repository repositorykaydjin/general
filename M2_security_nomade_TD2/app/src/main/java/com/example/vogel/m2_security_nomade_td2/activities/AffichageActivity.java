package com.example.vogel.m2_security_nomade_td2.activities;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vogel.m2_security_nomade_td2.R;
import com.example.vogel.m2_security_nomade_td2.bdd.BDDManager;
import com.example.vogel.m2_security_nomade_td2.util.Entry;
import com.example.vogel.m2_security_nomade_td2.util.EntryAdapter;
import com.example.vogel.m2_security_nomade_td2.utils.PkgCert;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AffichageActivity extends AppCompatActivity implements LocationListener {

    private BDDManager datasource;
    private ListView mListView;
    private EntryAdapter adapter;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //We used the database to get all existing entries.
        datasource = new BDDManager(this);
        //Mode test
        datasource.allRemove();

        datasource.open();

        List<Entry> entries = datasource.getAllEntry();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {

            Toast.makeText(getBaseContext(), "No permission 1 ", Toast.LENGTH_LONG).show();
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permissions,42);
        }

        mListView = (ListView) findViewById(R.id.liste);
        adapter = new EntryAdapter(AffichageActivity.this, entries);
        mListView.setAdapter(adapter);

        Log.e("onCreation", "ici");
        /*String hash_app = PkgCert.hash(getApplicationContext(),
                "com.example.vogel.m2_security_nomade_td2_emetteur");
        Log.e("hash", hash_app);*/

        MyBroadcast br = new MyBroadcast(datasource, adapter);

        IntentFilter filter = new IntentFilter("com.example.vogel.m2_security_nomade_td2_emetteur");
        registerReceiver(br, filter, Manifest.permission.ACCESS_FINE_LOCATION, null );
    }

    @Override
    protected void onResume() {
        Log.v("onResume", "ici");
        if (!datasource.isOpen())
            datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("onPause", "ici");
        if (datasource.isOpen())
            datasource.close();
        super.onPause();
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
}
