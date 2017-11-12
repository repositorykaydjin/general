package com.example.vogel.testlist.bdd;

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
    public static final String DATABASE_NAME = "Journal.bdd";
    public static final int DATABASE_VERSION = 1;

    /**
     * We specify a class for all tables
     */
    public static class JournalTable {

        //Table name
        public static final String NAME_TABLE = "Journal";
        public static final int NUM_TABLE = 0;
        public static final String[] allcolumns = {
                JournalTable.KEY_COL_ID,
                JournalTable.KEY_COL_DATE,
                JournalTable.KEY_COL_MESSAGE,
                JournalTable.KEY_COL_TITRE,
                JournalTable.KEY_COL_IMAGE
        };

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_DATE = "_date";
        public static final String KEY_COL_TITRE = "_titre";
        public static final String KEY_COL_MESSAGE = "_message";
        public static final String KEY_COL_IMAGE = "_image";

        public static final int NUM_ID_COLUMN = 0;
        public static final int NUM_DATE_COLUMN = 1;
        public static final int NUM_TITRE_COLUMN = 2;
        public static final int NUM_MESSAGE_COLUMN = 3;
        public static final int NUM_IMAGE_COLUMN = 4;

        public static final String SQL_TABLE = "create table "
                + JournalTable.NAME_TABLE + "(" + JournalTable.KEY_COL_ID
                + " integer primary key autoincrement, "
                + JournalTable.KEY_COL_DATE + " TEXT, "
                + JournalTable.KEY_COL_MESSAGE + " TEXT, "
                + JournalTable.KEY_COL_TITRE + " TEXT, "
                + JournalTable.KEY_COL_IMAGE + " TEXT) ";
    }

    public static class UtilisateurTable {
        //Table name
        public static final String NAME_TABLE = "Utilisateur";
        public static final int NUM_TABLE = 1;
        public static final String[] allcolumns = {
                UtilisateurTable.KEY_COL_ID,
                UtilisateurTable.KEY_COL_MDP,
        };

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_MDP = "_mdp";

        public static final int NUM_ID_COLUMN = 0;
        public static final int NUM_MDP_COLUMN = 1;

        public static final String SQL_TABLE = "create table "
                + UtilisateurTable.NAME_TABLE + "(" + UtilisateurTable.KEY_COL_ID
                + " integer primary key autoincrement, "
                + UtilisateurTable.KEY_COL_MDP + " TEXT) ";
    }

    //The static string to create the database. Here there is no relation between table
    //If not the creation could be mandatory to be in one String.
    private static final String[] DATABASE_CREATE = {
            UtilisateurTable.SQL_TABLE,
            JournalTable.SQL_TABLE
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
            db.execSQL("DROP TABLE IF EXISTS "+ JournalTable.NAME_TABLE);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        try{
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS "+ UtilisateurTable.NAME_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + JournalTable.NAME_TABLE);
        onCreate(db);
    }



}
