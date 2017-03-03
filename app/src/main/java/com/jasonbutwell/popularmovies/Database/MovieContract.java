package com.jasonbutwell.popularmovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by J on 02/03/2017.
 */

public class MovieContract {

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.jasonbutwell.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        // Scheme + Authority + Path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID          = "id";
        public static final String COLUMN_MOVIE_TITLE       = "title";
        public static final String COLUMN_MOVIE_POSTER_URL  = "poster";
    }
}
