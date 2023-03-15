package com.example.projetmajeur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecentSongsAdapter extends ArrayAdapter<Song> {

    private int resourceLayout;
    private Context context;

    public RecentSongsAdapter(Context context, int resource, List<Song> items) {
        super(context, resource, items);
        this.context = context;
        this.resourceLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }

        Song p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.song_title);
            TextView tt2 = v.findViewById(R.id.song_artist);
            TextView tt3 = v.findViewById(R.id.song_style);

            if (tt1 != null) {
                tt1.setText(p.getTitle());
            }
            if (tt2 != null) {
                tt2.setText(p.getArtist());
            }
            if (tt3 != null) {
                tt3.setText(p.getStyle());
            }
        }

        return v;
    }

}
