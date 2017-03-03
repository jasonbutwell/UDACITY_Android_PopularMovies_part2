package com.jasonbutwell.popularmovies.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import static com.jasonbutwell.popularmovies.Database.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by J on 02/03/2017.
 */

public class MovieContentProvider extends ContentProvider {

    // Useful constants
    private static final String UNKNOWN_URI = "Unknown Uri: ";
    private static final String SQL_INSERT_ERROR = "Failed to insert row into: ";
    private static final String PLACE_HOLDER = "=?";
    private static final String WILDCARD = "/#";

    // Our DB Helper
    private MovieDBHelper mMovieDbHelper;

    // Integer constants
    public static final int MOVIES = 100;               // Whole directory
    public static final int MOVIES_WITH_ID = 101;       // Single item

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Helper class to build URI's we want to match
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add each URI we want to match
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);                  // directory
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + WILDCARD, MOVIES_WITH_ID);   // single item

        return uriMatcher;  // Return the URI matcher
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDBHelper( getContext() );      // create and store new MovieDBHelper instance
        return true;                                             // return true
    }

    @Override
    public Cursor query(@NotNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor returnCursor;

        switch ( sUriMatcher.match(uri) ) {
            case MOVIES:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIES_WITH_ID:
                // selection and selectionArgs required
                String id = uri.getPathSegments().get(1);   // get path segment for  the id
                String mSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + PLACE_HOLDER;
                String []mSelectionArgs = new String[]{id};

                returnCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return returnCursor;    // return the cursor from the query for field extraction
    }

    @Override
    public Uri insert(@NotNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;
        long id;

        switch ( sUriMatcher.match(uri) ) {
            case MOVIES:
                id = db.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 )
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException(SQL_INSERT_ERROR + uri);
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        // Notify resolver that change has occurred
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;   // return the uri after data insertion
    }

    @Override
    public int delete(@NotNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int moviesDeleted = 0;

        switch ( sUriMatcher.match(uri) ) {
            case MOVIES_WITH_ID:
                // selection and selectionArgs required
                String id = uri.getPathSegments().get(1);
                String mSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + PLACE_HOLDER;
                String []mSelectionArgs = new String[]{id};

                moviesDeleted = db.delete(TABLE_NAME, mSelection, mSelectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (moviesDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);  // Notify change to resolver

        return moviesDeleted;   // number of rows affected by the delete
    }

    @Override
    public int update(@NotNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int moviesUpdated;

        switch ( sUriMatcher.match(uri) ) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String []mSelectionArgs = new String[]{id};
                moviesUpdated = db.update(TABLE_NAME, contentValues, PLACE_HOLDER, mSelectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (moviesUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);  // Notify change to resolver

        return moviesUpdated;   // number of rows affected by the update
    }

    @Override
    public String getType(@NotNull Uri uri) {
        String returnType;

        switch ( sUriMatcher.match(uri) ) {
            case MOVIES:
                returnType = "vnd.android.cursor.dir" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
                break;

            case MOVIES_WITH_ID:
                returnType = "vnd.android.cursor.item" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        return returnType;  // returns the MIME type for directory or single item
    }
}
