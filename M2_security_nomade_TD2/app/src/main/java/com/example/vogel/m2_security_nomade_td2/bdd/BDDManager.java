package com.example.vogel.m2_security_nomade_td2.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vogel.m2_security_nomade_td2.util.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vogel on 28/10/17.
 * help from http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
 */

public class BDDManager {

    //The database
    private SQLiteDatabase database;
    //The database meta-data.
    private BDDHelper bddHelper;

    //If we have multiple tables, all the tables must give their list of columns names.
    private String[][] allColumns = {
            BDDHelper.LocationTable.allcolumns,
            BDDHelper.LocationTable.allcolumns
    };

    private boolean open;

    /**
     * Build a mangaging tool to link to an already specified database.
     * @param context the context of the activity in which the bdd will be access.
     */
    public BDDManager(Context context) {
        bddHelper = new BDDHelper(context, BDDHelper.DATABASE_NAME, null,
                BDDHelper.DATABASE_VERSION);
        open = false;
    }

    /**
     * Create a link to the database.
     * @throws SQLException throws when database opening doesn't work.
     */
    public void open() throws SQLException {
        database = bddHelper.getWritableDatabase();
        open = true;
    }

    /**
     * Close the link to the database.
     */
    public void close() {
        bddHelper.close();
        open = false;
    }

    public void allRemove(){
        database = bddHelper.getWritableDatabase();
        bddHelper.allRemove(database);
        bddHelper.onCreate(database);
        bddHelper.close();
    }

    public Entry createEntry(String message, String longitude, String latitude) {
        ContentValues values = new ContentValues();
        values.put(BDDHelper.LocationTable.KEY_COL_MESSAGE, message);
        values.put(BDDHelper.LocationTable.KEY_COL_LONGITUDE, longitude);
        values.put(BDDHelper.LocationTable.KEY_COL_LATITUDE, latitude);

        long insertId = database.insert(BDDHelper.LocationTable.NAME_TABLE, null,
                values);
        Cursor cursor = database.query(BDDHelper.LocationTable.NAME_TABLE,
                allColumns[BDDHelper.LocationTable.NUM_TABLE], BDDHelper.LocationTable.KEY_COL_ID
                        + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Entry newEntry = cursorToEntry(cursor);
        cursor.close();
        return newEntry;
    }


    /**
     * Specify how an entry is delete in the database
     * @param id_entry the id of the entry to delete
     * @return the entry remove from the database
     */
    public Entry deleteEntry(int id_entry) {
        long id = id_entry;
        Entry ent = getEntry(id_entry);
        int num = database.delete(BDDHelper.LocationTable.NAME_TABLE, BDDHelper.LocationTable.KEY_COL_ID
                + " = " + id, null);
        return ent;
    }

    /**
     * Get one entry of the database
     * @param id_entry the specified id of the entry we want to retrieve
     * @return the entry queried with all its arguments
     */
    public Entry getEntry(int id_entry){
        long id = id_entry;
        Cursor cursor = database.query(BDDHelper.LocationTable.NAME_TABLE,
                allColumns[BDDHelper.LocationTable.NUM_TABLE],
                BDDHelper.LocationTable.KEY_COL_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Entry entry = cursorToEntry(cursor);
        cursor.close();
        return entry;
    }

    /**
     * Useful method which returns all the entries in the database.
     * @return the entries as a List collection
     */
    public List<Entry> getAllEntry() {
        List<Entry> entries = new ArrayList<Entry>();

        Cursor cursor = database.query(BDDHelper.LocationTable.NAME_TABLE,
                allColumns[BDDHelper.LocationTable.NUM_TABLE], null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = cursorToEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return entries;
    }

    /**
     * Transform the result of a query in a class type format
     * @param cursor the result of a query as a cursor type
     * @return the result of a query as a class type format specified.
     */
    private Entry cursorToEntry(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId((int)cursor.getLong(BDDHelper.LocationTable.NUM_ID_COLUMN));
        entry.setMessage(cursor.getString(BDDHelper.LocationTable.NUM_MESSAGE_COLUMN));
        entry.setLongitude(cursor.getString(BDDHelper.LocationTable.NUM_LONGITUDE_COLUMN));
        entry.setLatitude(cursor.getString(BDDHelper.LocationTable.NUM_MESSAGE_COLUMN));
        return entry;
    }

    public boolean isOpen() {
        return open;
    }
}
