package com.example.vogel.m2_security_nomade_td2.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vogel on 26/10/17.
 * http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
 */

public class BDDHelper extends SQLiteOpenHelper {

    //The database name
    public static final String DATABASE_NAME = "Location.bdd";
    public static final int DATABASE_VERSION = 1;

    /**
     * We specify a class for all tables
     */
    public static class LocationTable {

        //Table name
        public static final String NAME_TABLE = "Location";
        public static final int NUM_TABLE = 0;
        public static final String[] allcolumns = {
                LocationTable.KEY_COL_ID,
                LocationTable.KEY_COL_MESSAGE,
                LocationTable.KEY_COL_LONGITUDE,
                LocationTable.KEY_COL_LATITUDE
        };

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_MESSAGE = "_message";
        public static final String KEY_COL_LONGITUDE = "_longitude";
        public static final String KEY_COL_LATITUDE = "_latitude";

        public static final int NUM_ID_COLUMN = 0;
        public static final int NUM_MESSAGE_COLUMN = 1;
        public static final int NUM_LONGITUDE_COLUMN = 2;
        public static final int NUM_LATITUDE_COLUMN = 3;

        public static final String SQL_TABLE = "create table "
                + LocationTable.NAME_TABLE + "(" + LocationTable.KEY_COL_ID
                + " integer primary key autoincrement, "
                +LocationTable.KEY_COL_MESSAGE + " TEXT, "
                + LocationTable.KEY_COL_LONGITUDE + " TEXT, "
                + LocationTable.KEY_COL_LATITUDE + " TEXT) ";
    }

    //The static string to create the database. Here there is no relation between table
    //If not the creation could be mandatory to be in one String.
    private static final String[] DATABASE_CREATE = {
            LocationTable.SQL_TABLE
    };

    /**
     * Constructor of the database meta-data and connection.
     *
     * @param context the context of the activity in which the bdd will be access.
     * @param name    the name of the database
     * @param factory the factory (null works nicely too)
     * @param version version (useless most of the time)
     */
    public BDDHelper(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * One of the two method to override, when there is no table as specified, create it.
     *
     * @param db the database link to this database informations.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            for (int i = 0; i < DATABASE_CREATE.length; i++) {
                db.execSQL(DATABASE_CREATE[i]);
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
    }

    public void allRemove(SQLiteDatabase db) {
        try{
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS "+ LocationTable.NAME_TABLE);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        try{
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS "+ LocationTable.NAME_TABLE);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
    }

    /**
     * The second method to override
     * @param db the database link to this database informations.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("BDDHelper", "Mise à jour de la version "+
                oldVersion + "vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        db.execSQL("DROP TABLE IF EXISTS " + LocationTable.NAME_TABLE);
        onCreate(db);
    }



}
