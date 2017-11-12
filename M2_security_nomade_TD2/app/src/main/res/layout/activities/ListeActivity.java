package com.example.vogel.testlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vogel.testlist.R;
import com.example.vogel.testlist.util.EntryAdapter;

import java.util.Calendar;
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    private com.example.vogel.testlist.bdd.BDDManager datasource;
    private ListView mListView;
    private EntryAdapter adapter;

    public static final String EXTRA_ID = "entry_id";
    public static final String EXTRA_DATE = "entry_date";
    public static final String EXTRA_TITRE = "entry_titre";
    public static final String EXTRA_TEXTE = "entry_texte";
    public static final String EXTRA_IMAGE = "entry_image";
    public static final String EXTRA_ACTION = "user_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //We used the database to get all existing entries.
        datasource = new com.example.vogel.testlist.bdd.BDDManager(this);
        datasource.open();
        List<com.example.vogel.testlist.util.Entry> entries = datasource.getAllEntry();

        mListView = (ListView) findViewById(R.id.liste);
        adapter = new EntryAdapter(com.example.vogel.testlist.activities.ListeActivity.this, entries);
        mListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.vogel.testlist.activities.ListeActivity.this, com.example.vogel.testlist.activities.SaisieEntry.class);
                startActivityForResult(intent, com.example.vogel.testlist.activities.SaisieEntry.REQUEST_CODE);
            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView.getItemAtPosition(position);
                com.example.vogel.testlist.util.Entry ent = (com.example.vogel.testlist.util.Entry)o;

                Intent intent = new Intent(com.example.vogel.testlist.activities.ListeActivity.this, com.example.vogel.testlist.activities.DetailEntry.class);
                intent.putExtra(EXTRA_ID, ent.getId()+"");
                intent.putExtra(EXTRA_DATE, ent.getDate());
                intent.putExtra(EXTRA_TITRE, ent.getTitre());
                intent.putExtra(EXTRA_TEXTE, ent.getTexte());
                intent.putExtra(EXTRA_IMAGE, ent.getImage());
                startActivityForResult(intent, com.example.vogel.testlist.activities.DetailEntry.REQUEST_CODE);
            }

        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(!datasource.isOpen())
            datasource.open();

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //We get all the data
            String texte = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_TEXTE);
            String titre = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_TITRE);
            String image = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_IMAGE);

            Calendar now = Calendar.getInstance();
            String n = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.YEAR);

            //We add the new entry in the database
            com.example.vogel.testlist.util.Entry e = datasource.createEntry(n, texte, titre, image+"");

            //We modify the view
            adapter.add(e);
            adapter.notifyDataSetChanged();
        }

        if (requestCode == 2 && resultCode ==Activity.RESULT_OK) {
            //We get all the data
            Integer id = Integer.valueOf(data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_ID));
            String action = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_ACTION);

            if(action.equals("modifier")){
                com.example.vogel.testlist.util.Entry ent = datasource.getEntry(id);
                Log.v("test", ent.getId()+"");
                Intent intent = new Intent(com.example.vogel.testlist.activities.ListeActivity.this, com.example.vogel.testlist.activities.ModifierEntry.class);
                intent.putExtra(EXTRA_ID, ent.getId()+"");
                intent.putExtra(EXTRA_DATE, ent.getDate());
                intent.putExtra(EXTRA_TITRE, ent.getTitre());
                intent.putExtra(EXTRA_TEXTE, ent.getTexte());
                intent.putExtra(EXTRA_IMAGE, ent.getImage());
                startActivityForResult(intent, com.example.vogel.testlist.activities.ModifierEntry.REQUEST_CODE);
            }
            if(action.equals("supprimer")){
                com.example.vogel.testlist.util.Entry e = datasource.deleteEntry(id);
                adapter.remove(e);
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Integer id = Integer.valueOf(data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_ID));
            String date = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_DATE);
            String texte = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_TEXTE);
            String titre = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_TITRE);
            String image = data.getStringExtra(com.example.vogel.testlist.activities.ListeActivity.EXTRA_IMAGE);

            com.example.vogel.testlist.util.Entry prec = datasource.getEntry(id);
            int pos = adapter.getPosition(prec);
            com.example.vogel.testlist.util.Entry e = datasource.updateEntry(id, date, texte, titre, image);
            adapter.remove(prec);
            adapter.insert(e, pos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_supprimer){
            datasource.allRemove();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        Log.v("onResume","ici");
        if(!datasource.isOpen())
            datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("onPause","ici");
        if(datasource.isOpen())
            datasource.close();
        super.onPause();
    }
}
