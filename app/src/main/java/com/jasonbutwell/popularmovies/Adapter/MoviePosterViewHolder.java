package com.jasonbutwell.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jasonbutwell.popularmovies.Api.PicassoHelper;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.R;

/**
 * Created by J on 25/02/2017.
 */

public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

// The custom view holder outer class, used for assigning data to views

    public ImageView imageView;                      // location for the view we want to change
    private ListItemClickListener mOnClickListener;     // store location for list click listener

    public MoviePosterViewHolder(View itemView, ListItemClickListener mOnClickListener) {
        super(itemView);

        // Get a reference to the view to change
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

        this.mOnClickListener = mOnClickListener;

        // set our click listener to respond to that view item
        itemView.setOnClickListener(this);
    }

    public void bind(Context context, MovieItem movieItem) {

        PicassoHelper.loadImage( context, movieItem.getPosterURL(), imageView);
    }

    // Our internal click handler that signifies what item was actually clicked

    @Override
    public void onClick(View view) {
        mOnClickListener.onListItemClick(getAdapterPosition()); // passes the adapter position on
    }
}
