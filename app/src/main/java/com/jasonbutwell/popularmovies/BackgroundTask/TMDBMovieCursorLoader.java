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

    public TMDBMovieCursorLoader(Context context, LoaderManager loaderManager, Object binding, CursorLoadCompleteListener listener) {
        mContext = context;
        mBinding = binding;
        mListener = listener;

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
                //super.onStartLoading();

                // Workaround to stop this re-running when we navigate back to the activity
                if ( !isLoaded )
                    LoadingIndicator.show(mBinding, true);

                    if (mMovieData != null)
                        deliverResult(mMovieData);      // Delivers any previously loaded data immediately
                    else
                        forceLoad();                    // Force a new load
            }

            // execute query to retrieve all records as selection and selection args are null
            @Override
            public Cursor loadInBackground() {
                try {
                    return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
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
        isLoaded = true;
        LoadingIndicator.show(mBinding, false);
        mListener.onTaskComplete(data); // listener call back to return cursor so we can swap the cursor
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
