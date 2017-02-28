package com.jasonbutwell.popularmovies.AdapterViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonbutwell.popularmovies.Api.PicassoImageHelper;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.R;

/**
 * Created by J on 26/02/2017.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

// The custom view holder outer class, used for assigning data to views

    public ImageView ImageView;                       // location for the view we want to change
    public TextView description;
    private ListItemClickListener mOnClickListener;         // store location for list click listener

    public TrailerViewHolder(View itemView, ListItemClickListener mOnClickListener) {
        super(itemView);

        // Get a reference to the view to change
        ImageView = (ImageView) itemView.findViewById(R.id.imageView);
        description = (TextView) itemView.findViewById(R.id.trailerDescription);

        this.mOnClickListener = mOnClickListener;

        // set our click listener to respond to that view item
        itemView.setOnClickListener(this);
    }

    public void bind(Context context, TrailerItem trailerItem) {

        PicassoImageHelper.loadImage( context, trailerItem.getYoutubeThumbnailURL(), ImageView);
        description.setText(trailerItem.getDescription());
    }

    // Our internal click handler that signifies what item was actually clicked

    @Override
    public void onClick(View view) {
        mOnClickListener.onListItemClick(getAdapterPosition()); // passes the adapter position on
    }
}