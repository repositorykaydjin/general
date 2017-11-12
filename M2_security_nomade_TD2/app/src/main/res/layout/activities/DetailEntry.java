package com.example.vogel.testlist.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vogel.testlist.R;

import java.io.File;

public class DetailEntry extends AppCompatActivity {

    private String id;
    public static Integer REQUEST_CODE= 2;

    //Image
    private Bitmap bitmap;
    private String image=null;
    private ImageView imageView;

    private final static int READ_EXTERNAL_RESULT = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mDate = (TextView) findViewById(R.id.date);
        TextView mTitre = (TextView) findViewById(R.id.titre);
        TextView mTexte = (TextView) findViewById(R.id.texte);
        imageView = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        id = intent.getStringExtra(ListeActivity.EXTRA_ID)+"";
        String date = intent.getStringExtra(ListeActivity.EXTRA_DATE);
        String titre = intent.getStringExtra(ListeActivity.EXTRA_TITRE);
        String texte = intent.getStringExtra(ListeActivity.EXTRA_TEXTE);
        image = intent.getStringExtra(ListeActivity.EXTRA_IMAGE);

        mDate.setText(date);
        mTitre.setText(titre);
        mTexte.setText(texte);

        Button modifier = (Button)findViewById(R.id.button_modifier);
        Button supprimer = (Button)findViewById(R.id.button_supprimer);

        modifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("DetailEntry","modifier");
                Intent resultIntent = new Intent(com.example.vogel.testlist.activities.DetailEntry.this, ListeActivity.class);
                resultIntent.putExtra(ListeActivity.EXTRA_ACTION, "modifier");
                resultIntent.putExtra(ListeActivity.EXTRA_ID, id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        supprimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("DetailEntry","supprimer");
                Intent resultIntent = new Intent(com.example.vogel.testlist.activities.DetailEntry.this, ListeActivity.class);
                resultIntent.putExtra(ListeActivity.EXTRA_ACTION, "supprimer");
                resultIntent.putExtra(ListeActivity.EXTRA_ID, id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_RESULT);

        }else {
            if (image != null && !image.equals("")) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                File file = new File(image);
                bitmap = BitmapFactory.decodeFile(file.getPath(),bmOptions);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    File file = new File(image);
                    bitmap = BitmapFactory.decodeFile(file.getPath(),bmOptions);
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Image unable to be access", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

}
