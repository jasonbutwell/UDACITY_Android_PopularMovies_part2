package com.jasonbutwell.popularmovies.BackgroundTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import java.io.IOException;
import java.net.URL;

/**
 * Created by J on 26/02/2017.
 */

public class TMDBDetailsLoader implements LoaderManager.LoaderCallbacks<String> {

    private static final int LOADER_ID = 1;

    private Context mContext;
    private ActivityMovieDetailsBinding mBinding;

    //private LoaderManager mLoaderManager;
    //private MovieDetailTaskCompleteListener listener;
    //private static String LOADER_ID_STRING = "json";

    private String mMovieId;

    public TMDBDetailsLoader(Context context, LoaderManager loaderManager, ActivityMovieDetailsBinding binding, String movieId) {
        mContext = context;
        mBinding = binding;
        mMovieId = movieId;
        //mLoaderManager = loaderManager;

//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(LOADER_ID_STRING, TMDBHelper.buildDetailURL(mMovieId).toString());

        if (loaderManager == null)
            loaderManager.initLoader(LOADER_ID, null, this);   // null parameter can be queryBundle
        else
            loaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                //if (args == null) return;

                LoadingIndicator.show(mBinding, true);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String queryString = TMDBHelper.buildDetailURL(mMovieId).toString();

                if (queryString == null) return null;

                try {
                    return JSONUtils.extractJSONString( NetworkUtils.getResponseFromHttpUrl(new URL(queryString)), TMDBInfo.MOVIE_RUNTIME );

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mBinding.movieDuration.setText( DateTimeUtils.convertToHoursMins( data ) );              // Show the movie duration
        LoadingIndicator.show(mBinding, false);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
