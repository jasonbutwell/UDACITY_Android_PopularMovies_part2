package com.jasonbutwell.popularmovies.BackgroundTask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.jasonbutwell.popularmovies.Database.MovieContract;
import com.jasonbutwell.popularmovies.Listener.CursorLoadCompleteListener;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;

/**
 * Created by J on 03/03/2017.
 */

public class TMDBMovieCursorLoader implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int LOADER_ID = 3;

    private Context mContext;
    private Object mBinding;
    private CursorLoadCompleteListener mListener;
    private static boolean isLoaded;
    private int mId = -1;

    public TMDBMovieCursorLoader(Context context, LoaderManager loaderManager, Object binding, CursorLoadCompleteListener listener, int id) {
        mContext = context;
        mBinding = binding;
        mListener = listener;
        mId = id;

        isLoaded = false;

        if (loaderManager == null)
            loaderManager.initLoader(LOADER_ID, null, this);   // null parameter can be queryBundle
        else
            loaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mContext) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                // Workaround to stop this re-running when we navigate back to the activity
                if ( !isLoaded )
                    LoadingIndicator.show(mBinding, true);  // loading indicator on

                    if (mMovieData != null)
                        deliverResult(mMovieData);      // Delivers any previously loaded data immediately
                    else
                        forceLoad();                    // Force a new load
            }

            // execute query to retrieve all records as selection and selection args are null
            @Override
            public Cursor loadInBackground() {
                String selection = null;
                String[] selectionArgs = null;      // Initial selection (selects all records)

                // Set up our selection to query by ID
                if (mId != -1) {
                    selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                    selectionArgs = new String[]{String.valueOf(mId)};
                }
                try {
                    // Run the query here
                    return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            selection,
                            selectionArgs,
                            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);   // sort results by title

                }catch (Exception e ) {
                    e.printStackTrace();
                    return null;
                }
            }

            // deliver result for caching the cursor
            public void deliverResult( Cursor data ) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        isLoaded = true;                            // stop loading indicator running again
        LoadingIndicator.show(mBinding, false);     // loading indicator on
        mListener.onTaskComplete(data);             // call back to return cursor so we can swap the cursor
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
