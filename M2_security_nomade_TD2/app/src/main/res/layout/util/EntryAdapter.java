package com.example.vogel.testlist.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vogel.testlist.R;

import java.util.List;

/**
 * Created by vogel on 23/10/17.
 */
public class EntryAdapter extends ArrayAdapter<com.example.vogel.testlist.util.Entry> {

    //tweets est la liste des models à afficher
    public EntryAdapter(Context context, List<com.example.vogel.testlist.util.Entry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_entry,parent, false);
        }

        EntryViewHolder viewHolder = (EntryViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EntryViewHolder();
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.titre= convertView.findViewById(R.id.titre);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        com.example.vogel.testlist.util.Entry entry = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.date.setText(entry.getDate());
        viewHolder.titre.setText(entry.getTitre());
       // viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }

    private class EntryViewHolder {
        public TextView date;
        public TextView titre;
    }

}