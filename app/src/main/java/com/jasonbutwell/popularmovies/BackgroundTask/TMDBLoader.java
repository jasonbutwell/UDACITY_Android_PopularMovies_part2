package com.jasonbutwell.popularmovies.BackgroundTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;
import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by J on 26/02/2017.
 */

public class TMDBLoader implements LoaderManager.LoaderCallbacks<ArrayList<MovieItemBasic>> {

    private static final int LOADER_ID = 2;

    private Context mContext;
    private Object mBinding;
    private MovieTaskCompleteListener mListener;

    private static boolean isLoaded;

    public TMDBLoader(Context context, LoaderManager loaderManager, MoviePosterLayoutBinding binding, MovieTaskCompleteListener listener) {
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
    public Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieItemBasic>>(mContext) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if ( !isLoaded) {
                    LoadingIndicator.show(mBinding, true);

                    Log.i("LOADER", "START");
                    forceLoad();

                }
            }

            @Override
            public ArrayList<MovieItemBasic> loadInBackground() {
                ArrayList<MovieItemBasic> movies = null;
                URL queryString = TMDBHelper.buildBaseURL();
                String JSONData = "";

                if (queryString == null) return null;

                try {
                    JSONData = NetworkUtils.getResponseFromHttpUrl(queryString);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                movies = JSONUtils.extractJSONArray( JSONData );

                return movies;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItemBasic>> loader, ArrayList<MovieItemBasic> movieData) {
        LoadingIndicator.show( mBinding, false );                   // Loading indicator invisible
        mListener.onTaskComplete(movieData);
        Log.i("LOADER","END");
        isLoaded = true;
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}