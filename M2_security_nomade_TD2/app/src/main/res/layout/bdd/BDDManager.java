package com.example.vogel.testlist.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vogel.testlist.util.Entry;

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
    private com.example.vogel.testlist.bdd.BDDHelper bddHelper;

    //If we have multiple tables, all the tables must give their list of columns names.
    private String[][] allColumns = {
            com.example.vogel.testlist.bdd.BDDHelper.JournalTable.allcolumns,
            com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.allcolumns
    };

    private boolean open;

    /**
     * Build a mangaging tool to link to an already specified database.
     * @param context the context of the activity in which the bdd will be access.
     */
    public BDDManager(Context context) {
        bddHelper = new com.example.vogel.testlist.bdd.BDDHelper(context, com.example.vogel.testlist.bdd.BDDHelper.DATABASE_NAME, null,
                com.example.vogel.testlist.bdd.BDDHelper.DATABASE_VERSION);
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

    /**
     * Ajout le mot de passe de l'utilisateur unique.
     * @param mdp
     */
    public void ajoutUtilisateur(String mdp){
        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NUM_TABLE], null, null, null, null, null);

        //If there isn't any entry we add a new entry.
        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.KEY_COL_MDP, mdp);
            database.insert(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NAME_TABLE, null, values);
        }
        cursor.close();
    }

    /**
     * Test if a user already exist;
     * @return true or false;
     */
    public boolean existUtilisateur(){
        boolean test= false;
        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NUM_TABLE], null, null, null, null, null);
        if(cursor.moveToFirst())
            test=true;

        cursor.close();
        return test;
    }

    /**
     * Try to connect with a password
     * @param mdp password
     * @return true if password is right, false otherwise.
     */
    public boolean connexion(String mdp){
        boolean test = false;
        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NUM_TABLE], null, null, null, null, null);

        //If there is an entry we test
        if(cursor.moveToFirst()) {
            String m = cursor.getString(com.example.vogel.testlist.bdd.BDDHelper.UtilisateurTable.NUM_MDP_COLUMN);
            if(m.equals(mdp))test = true;
        }
        cursor.close();
        return test;
    }


    /**
     * Specify how a new entry is create in the database with an image add
     * @param date the data of the new entry
     * @param titre the titre of the new entry
     * @param message the message of the new entry
     * @return a type Entry
     */
    public Entry createEntry(String date, String titre, String message, String image) {
        ContentValues values = new ContentValues();
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_DATE, date);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_TITRE, titre);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_MESSAGE, message);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_IMAGE, image);

        long insertId = database.insert(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE, null,
                values);
        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_TABLE], com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_ID
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
        int num = database.delete(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE, com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_ID
                + " = " + id, null);
        return ent;
    }

    /**
     * Specify how an entry is update in the database
     * @param id_entry the id of the entry to update
     * @return the entry update with all its arguments
     */
    public Entry updateEntry(int id_entry, String date, String titre, String message, String image) {
        long id = id_entry;
        ContentValues values = new ContentValues();
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_DATE, date);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_TITRE, titre);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_MESSAGE, message);
        values.put(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_IMAGE, image);

        database.update(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE, values,
                com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_ID + " = " + id, null);
        return getEntry(id_entry);
    }

    /**
     * Get one entry of the database
     * @param id_entry the specified id of the entry we want to retrieve
     * @return the entry queried with all its arguments
     */
    public Entry getEntry(int id_entry){
        long id = id_entry;
        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_TABLE],
                com.example.vogel.testlist.bdd.BDDHelper.JournalTable.KEY_COL_ID + " = " + id, null, null, null, null);
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

        Cursor cursor = database.query(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NAME_TABLE,
                allColumns[com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_TABLE], null, null, null, null, null);

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
        entry.setId((int)cursor.getLong(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_ID_COLUMN));
        entry.setDate(cursor.getString(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_DATE_COLUMN));
        entry.setTitre(cursor.getString(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_TITRE_COLUMN));
        entry.setTexte(cursor.getString(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_MESSAGE_COLUMN));
        entry.setImage(cursor.getString(com.example.vogel.testlist.bdd.BDDHelper.JournalTable.NUM_IMAGE_COLUMN));
        return entry;
    }

    public boolean isOpen() {
        return open;
    }
}
