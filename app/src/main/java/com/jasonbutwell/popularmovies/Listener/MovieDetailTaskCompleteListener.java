package com.jasonbutwell.popularmovies.Listener;

import com.jasonbutwell.popularmovies.Model.MovieItem;

/**
 * Created by J on 25/02/2017.
 */

public interface MovieDetailTaskCompleteListener {
    void onTaskComplete(MovieItem movie);           // The method we must implement
}

