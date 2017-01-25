package com.jasonbutwell.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by J on 22/01/2017.
 */

public class MovieAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieItem> movies;

    public MovieAdapter( Context context, ArrayList<MovieItem> movies ) {
        this.context = context;
        this.movies = movies;
    }

    public void setData( ArrayList<MovieItem> movies ) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //  Commented out code recycles the view
//        if ( convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_image, parent, false);
//        } else {
//            view = (View) convertView;
//        }

        if ( movies.get(position).getPosterURL() != null ) {
            // Handle the caching of the image with the Picasso library
            Picasso
                    .with(context)
                    .load( movies.get( position ).getPosterURL() )
                    .fit()
                    .into((ImageView)view);
        }

        return view;
    }
}