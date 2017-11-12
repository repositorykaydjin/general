package com.example.vogel.m2_security_nomade_td2.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vogel.m2_security_nomade_td2.R;

import java.util.List;

/**
 * Created by vogel on 23/10/17.
 */
public class EntryAdapter extends ArrayAdapter<Entry> {

    //tweets est la liste des models à afficher
    public EntryAdapter(Context context, List<Entry> entries) {
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
            viewHolder.message = convertView.findViewById(R.id.message);
            viewHolder.longitude = convertView.findViewById(R.id.longitude);
            viewHolder.latitude = convertView.findViewById(R.id.latitude);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Entry entry = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.message.setText(entry.getMessage());
        viewHolder.longitude.setText(entry.getLongitude());
        viewHolder.longitude.setText(entry.getLatitude());
       // viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }

    private class EntryViewHolder {
        public TextView message;
        public TextView longitude;
        public TextView latitude;
    }

}