package com.jasonbutwell.popularmovies.Listener;

import com.jasonbutwell.popularmovies.Model.MovieItemBasic;

import java.util.ArrayList;

/**
 * Created by J on 25/02/2017.
 */

public interface MovieTaskCompleteListener {
    void onTaskComplete(ArrayList<MovieItemBasic> movies);           // The method we must implement
}
