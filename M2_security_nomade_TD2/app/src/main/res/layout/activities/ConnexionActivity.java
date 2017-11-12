package com.example.vogel.testlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vogel.testlist.R;

public class ConnexionActivity extends AppCompatActivity {

    private com.example.vogel.testlist.bdd.BDDManager datasource;
    private EditText mpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new com.example.vogel.testlist.bdd.BDDManager(this);
        datasource.open();
        mpassword = (EditText) findViewById(R.id.password);

        Button validate = (Button)findViewById(R.id.button_validate);

        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (datasource.connexion(mpassword.getText().toString())) {
                        Intent intent = new Intent(com.example.vogel.testlist.activities.ConnexionActivity.this, com.example.vogel.testlist.activities.ListeActivity.class);
                        startActivity(intent);
                        finish();
                }
            }
        });
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
