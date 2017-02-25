package com.jasonbutwell.popularmovies.Listener;

import com.jasonbutwell.popularmovies.Model.MovieItem;

import java.util.ArrayList;

/**
 * Created by J on 25/02/2017.
 */

public interface MovieTaskCompleteListener {
    void onTaskComplete(ArrayList<MovieItem> movies);           // The method we must implement
}
