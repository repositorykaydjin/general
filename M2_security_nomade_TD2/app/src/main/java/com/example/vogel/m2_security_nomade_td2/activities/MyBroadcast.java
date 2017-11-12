package com.example.vogel.m2_security_nomade_td2.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vogel.m2_security_nomade_td2.bdd.BDDManager;
import com.example.vogel.m2_security_nomade_td2.util.Entry;
import com.example.vogel.m2_security_nomade_td2.util.EntryAdapter;

import java.util.Calendar;

/**
 * Created by vogel on 10/11/17.
 */

public class MyBroadcast extends BroadcastReceiver{

    private BDDManager datasource;
    private EntryAdapter adapter;

    public MyBroadcast(){
        Log.e("constructeur", "ici");
    }

    public MyBroadcast(BDDManager bdd, EntryAdapter adapter){
        this.datasource = bdd;
        this.adapter = adapter;
        Log.e("constructeur", "ici2");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!datasource.isOpen())
            datasource.open();

        Log.e("onReceive", "ici");

        String message = intent.getStringExtra("message");
        String longitude = intent.getStringExtra("longitude");
        String latitude = intent.getStringExtra("latitude");

        Calendar now = Calendar.getInstance();
        String n = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.YEAR);

        //We add the new entry in the database
        Entry e = datasource.createEntry(message, longitude, latitude);

        //We modify the view
        adapter.add(e);
        adapter.notifyDataSetChanged();
    }


};