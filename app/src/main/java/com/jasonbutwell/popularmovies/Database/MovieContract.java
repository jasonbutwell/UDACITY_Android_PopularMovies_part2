package com.jasonbutwell.popularmovies.Database;

import android.provider.BaseColumns;

/**
 * Created by J on 02/03/2017.
 */

public class MovieContract {
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID          = "id";
        public static final String COLUMN_MOVIE_TITLE       = "title";
        public static final String COLUMN_MOVIE_POSTER_URL  = "poster";
    }
}
