package com.jasonbutwell.popularmovies.AdapterViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jasonbutwell.popularmovies.Api.PicassoImageHelper;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by J on 25/02/2017.
 */

public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

// The custom view holder outer class, used for assigning data to views

    public final ImageView posterImageView;                      // location for the view we want to change
    private final ListItemClickListener mOnClickListener;     // store location for list click listener

    public MoviePosterViewHolder(View itemView, ListItemClickListener mOnClickListener) {
        super(itemView);

        // Get a reference to the view to change
        posterImageView = (ImageView) itemView.findViewById(R.id.imageView);

        this.mOnClickListener = mOnClickListener;

        // set our click listener to respond to that view item
        itemView.setOnClickListener(this);
    }

    public void bind(Context context, ArrayList<MovieItemBasic> movies, int position) {

        PicassoImageHelper.loadImage( context, movies.get(position).getPosterURL(), posterImageView);
    }

    // Our internal click handler that signifies what item was actually clicked

    @Override
    public void onClick(View view) {
        mOnClickListener.onListItemClick(getAdapterPosition()); // passes the adapter position on
    }
}
