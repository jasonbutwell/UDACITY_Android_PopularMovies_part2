package com.jasonbutwell.popularmovies.Listener;

import android.database.Cursor;

/**
 * Created by J on 03/03/2017.
 */

public interface CursorLoadCompleteListener {
    void onTaskComplete(Cursor cursor);           // The method we must implement
}