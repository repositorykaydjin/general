package com.example.vogel.m2_security_nomade_td2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.vogel.m2_security_nomade_td2.bdd.BDDManager;
import com.example.vogel.m2_security_nomade_td2.util.Entry;
import com.example.vogel.m2_security_nomade_td2.util.EntryAdapter;

import java.util.Calendar;

/**
 * Created by vogel on 10/11/17.
 */

public class MyBroadcast2 extends BroadcastReceiver{

    private BDDManager datasource;
    private EntryAdapter adapter;

    public MyBroadcast2(){
        Log.e("constructeur", "ici");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "voila");
    }


};