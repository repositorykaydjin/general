package com.example.vogel.testlist.activities;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vogel.testlist.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ModifierEntry extends AppCompatActivity {

    //datas
    private String id;
    private String date;
    private String image;

    //containers
    private EditText mtitre;
    private EditText mtexte;
    private ImageView imageView;
    private Bitmap bitmap;

    //The intent for this activity.
    public static final Integer REQUEST_CODE= 3;

    //Intent request to obtain an image
    private static final int REQUEST_IMAGE_CODE = 5;
    private static final int REQUEST_IMAGE_CAPTURE = 7;

    //Permission request
    private final static int TAKE_IMAGE_RESULT = 107;
    //Camera available or not
    private Boolean cam_dispo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //To know if the device has a camera.
        cam_dispo = this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        //We retrieve the input cursor
        mtitre = (EditText) findViewById(R.id.titre);
        mtexte = (EditText) findViewById(R.id.texte);
        imageView = (ImageView) findViewById(R.id.image);

        //We initialize the input.
        String titre;
        String texte;
        image = null;

        //Already update
        if (savedInstanceState != null){
            id = savedInstanceState.getString("id");
            date = savedInstanceState.getString("date");
            titre = savedInstanceState.getString("titre");
            texte = savedInstanceState.getString("texte");
            image = savedInstanceState.getString("image");
        }else{
            //Or we just create the activity and we retrieve the data given by the activity.
            Intent intent = getIntent();
            id = intent.getStringExtra(ListeActivity.EXTRA_ID);
            date = intent.getStringExtra(ListeActivity.EXTRA_DATE);
            titre = intent.getStringExtra(ListeActivity.EXTRA_TITRE);
            texte = intent.getStringExtra(ListeActivity.EXTRA_TEXTE);
            image = intent.getStringExtra(ListeActivity.EXTRA_IMAGE);
        }

        //We set the old data
        mtitre.setText(titre);
        mtexte.setText(texte);
        if(image != null && !image.equals("")) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            File file = new File(image);
            bitmap = BitmapFactory.decodeFile(file.getPath(), bmOptions);
            imageView.setImageBitmap(bitmap);
        }

        //Button to validate the modification
        Button validate = (Button)findViewById(R.id.button_validate);
        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent(com.example.vogel.testlist.activities.ModifierEntry.this, ListeActivity.class);
                resultIntent.putExtra(ListeActivity.EXTRA_ID, id);
                resultIntent.putExtra(ListeActivity.EXTRA_DATE, date);
                resultIntent.putExtra(ListeActivity.EXTRA_TITRE, mtitre.getText().toString());
                resultIntent.putExtra(ListeActivity.EXTRA_TEXTE, mtexte.getText().toString());

                //If no image we just put an empty string
                if(image != null)
                    resultIntent.putExtra(ListeActivity.EXTRA_IMAGE, image);
                else
                    resultIntent.putExtra(ListeActivity.EXTRA_IMAGE, "");

                //Validate and terminate the activity
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    // Use by a button and specified in xml of the layout.
    public void takeImage(View view) {

        if(cam_dispo) {
            boolean cam_perm = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
            boolean write_perm = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;

            if (!cam_perm || !write_perm) {

                if(!cam_perm && ! write_perm)
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            TAKE_IMAGE_RESULT);
                else
                if(!cam_perm){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            TAKE_IMAGE_RESULT);
                }else{
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            TAKE_IMAGE_RESULT);
                }
            } else {
                takeImageStart();
            }
        }else{
            Toast.makeText(this, "No camera", Toast.LENGTH_SHORT).show();
        }
    }
    private void takeImageStart(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //The permission are necessary for taking a picture.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case TAKE_IMAGE_RESULT: {
                // If request is cancelled, the result arrays are empty.
                boolean permission = true;
                for(int grant : grantResults)
                    if(grant!=PackageManager.PERMISSION_GRANTED)permission=false;

                if (grantResults.length > 0 && permission) {
                    takeImageStart();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // Use by a button and specified in xml of the layout.
    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    //The result of the intent for getting an image, by file or by taking a new picture.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if ((requestCode == REQUEST_IMAGE_CODE || requestCode == REQUEST_IMAGE_CAPTURE)
                && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap);
                image = getPath(data.getData());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }

    //We saved the data when the activity must reboot.
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putString("id", id);
        bundle.putString("date", date);
        bundle.putString("image", image);
        bundle.putString("titre", mtitre.getText().toString());
        bundle.putString("texte", mtexte.getText().toString());
    }

    //Use to obtain a path of a file from an uri.
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}
