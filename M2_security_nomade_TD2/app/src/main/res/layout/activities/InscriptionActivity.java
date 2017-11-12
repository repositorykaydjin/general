package com.example.vogel.testlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vogel.testlist.R;
import com.example.vogel.testlist.bdd.BDDManager;

public class InscriptionActivity extends AppCompatActivity {

    private BDDManager datasource;
    private EditText mpassword;
    private EditText mpassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new BDDManager(this);
        datasource.open();

        mpassword = (EditText) findViewById(R.id.password);
        mpassword2 = (EditText) findViewById(R.id.password2);

        Button validate = (Button)findViewById(R.id.button_validate);

        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(mpassword.getText().toString().equals(mpassword2.getText().toString())) {
                    datasource.ajoutUtilisateur(mpassword.getText().toString());

                    if (datasource.connexion(mpassword.getText().toString())) {
                        Intent intent = new Intent(com.example.vogel.testlist.activities.InscriptionActivity.this, ListeActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
