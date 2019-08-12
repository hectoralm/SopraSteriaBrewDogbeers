package com.soprasteria.brewdog.objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hector1.soprasteriabrewdogbeers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BeerAdapter extends ArrayAdapter<Beer>{


    public BeerAdapter(@NonNull Context context, int resource, ArrayList<Beer> objects) {
        super(context, resource, objects);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        Beer beer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        TextView name = convertView.findViewById(R.id.listview_item_title);
        TextView tagLine = convertView.findViewById(R.id.listview_item_tagline);
        TextView abv = convertView.findViewById(R.id.listview_item_abv);
        TextView description = convertView.findViewById(R.id.listview_item_description);

        name.setText(beer.getBeerName());
        tagLine.setText(beer.getTagLine());
        abv.setText(beer.getAbv().toString());
        description.setText(beer.getDescription());

        ImageView image = convertView.findViewById(R.id.listview_image);
        Picasso .get()
                .load(beer.getImage_url())
                .into(image);

        return convertView;
    }
}
