package com.jasonbutwell.popularmovies.Listener;

import android.view.View;

/**
 * Created by J on 25/02/2017.
 */

// Interface for RecyclerView List Item Click Listener

public interface ListItemClickListener {
    // The method we must implement
    void onListItemClick(int clickedItemIndex, View view);
}
