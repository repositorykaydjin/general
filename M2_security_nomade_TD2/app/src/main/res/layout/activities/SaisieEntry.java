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

public class SaisieEntry extends AppCompatActivity {

    private EditText titre;
    private EditText texte;
    public static Integer REQUEST_CODE= 1;

    //Image
    private static final int REQUEST_IMAGE_CODE = 4;
    private static final int REQUEST_IMAGE_CAPTURE = 6;

    private final static int TAKE_IMAGE_RESULT = 106;

    private Bitmap bitmap;
    private String image=null;
    private ImageView imageView;

    private Boolean cam_dispo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        titre = (EditText) findViewById(R.id.titre);
        texte = (EditText) findViewById(R.id.texte);
        Button validate = (Button)findViewById(R.id.button_validate);
        imageView = (ImageView) findViewById(R.id.image);
        cam_dispo = this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        if (savedInstanceState != null){
            titre.setText(savedInstanceState.getString("titre"));
            texte.setText(savedInstanceState.getString("texte"));

            image = savedInstanceState.getString("image");
            if(image != null && !image.equals("")){
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                File file = new File(image);
                bitmap = BitmapFactory.decodeFile(file.getPath(),bmOptions);
                imageView.setImageBitmap(bitmap);
            }
        }

        validate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent(com.example.vogel.testlist.activities.SaisieEntry.this, ListeActivity.class);
                resultIntent.putExtra(ListeActivity.EXTRA_TITRE, titre.getText().toString());
                resultIntent.putExtra(ListeActivity.EXTRA_TEXTE, texte.getText().toString());
                if(image != null) {
                    resultIntent.putExtra(ListeActivity.EXTRA_IMAGE, image);
                }else
                    resultIntent.putExtra(ListeActivity.EXTRA_IMAGE, "");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }


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

    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if ((requestCode == REQUEST_IMAGE_CODE || requestCode == REQUEST_IMAGE_CAPTURE)
                && resultCode == Activity.RESULT_OK) {
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
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putString("image", image);
        bundle.putString("titre", titre.getText().toString());
        bundle.putString("texte", texte.getText().toString());
    }

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


    /* 2 others method to get the path. Keep it for test if the first fail.

    public String getPathByContent(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public static String getFilePathByUri(Context context, Uri uri)
    {
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content")==0)
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                return filePathUri.getPath().toString();
            }
        }
        else if (uri.getScheme().compareTo("file")==0)
        {
            return filePathUri.getPath().toString();
        }
        else
        {
            fileName = fileName+"_"+filePathUri.getPath().toString();
        }
        return fileName;
    }*/

}
