package com.example.vogel.testlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.vogel.testlist.R;
import com.example.vogel.testlist.bdd.BDDManager;

public class SplashActivity extends AppCompatActivity {

    private BDDManager datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        datasource = new BDDManager(this);
        datasource.allRemove();
        datasource.open();

        Intent intent;
        if(datasource.existUtilisateur()) {
            intent = new Intent(com.example.vogel.testlist.activities.SplashActivity.this, ConnexionActivity.class);
        }else{
            intent = new Intent(com.example.vogel.testlist.activities.SplashActivity.this, InscriptionActivity.class);
        }
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        if(!datasource.isOpen())
            datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(datasource.isOpen())
            datasource.close();
        super.onPause();
    }
}
