package com.jasonbutwell.popularmovies.BackgroundTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by J on 26/02/2017.
 */

public class TMDBDetailsLoader implements LoaderManager.LoaderCallbacks<MovieItem> {

    private static final int LOADER_ID = 1;

    private Context mContext;
    private ActivityMovieDetailsBinding mBinding;
    private MovieDetailTaskCompleteListener completeListener;

    //private LoaderManager mLoaderManager;
    //private MovieDetailTaskCompleteListener listener;
    //private static String LOADER_ID_STRING = "json";

    //private String mMovieId;

    private String mId, mPosterURL;

    public TMDBDetailsLoader(Context context, LoaderManager loaderManager, ActivityMovieDetailsBinding binding, String id, String posterURL, MovieDetailTaskCompleteListener completeListener) {
        mContext = context;
        mBinding = binding;
        this.completeListener = completeListener;

        mId = id;
        mPosterURL = posterURL;

//        mMovieId = movieId;
//        mLoaderManager = loaderManager;

//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(LOADER_ID_STRING, TMDBHelper.buildDetailURL(mMovieId).toString());

        if (loaderManager == null)
            loaderManager.initLoader(LOADER_ID, null, this);   // null parameter can be queryBundle
        else
            loaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieItem>(mContext) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                //if (args == null) return;

                LoadingIndicator.show(mBinding, true);
                forceLoad();
            }

            @Override
            public MovieItem loadInBackground() {
                String queryString = TMDBHelper.buildDetailURL(mId).toString();
                String trailersQueryString = TMDBHelper.buildTrailersURL(mId).toString();

                String jsonData="";
                String jsonDataTrailers="";

                ArrayList<TrailerItem> trailers = null;
                MovieItem movie = null;

                if (queryString == null) return null;

                try {
                    jsonData = NetworkUtils.getResponseFromHttpUrl(new URL(queryString));
                    jsonDataTrailers = NetworkUtils.getResponseFromHttpUrl(new URL(trailersQueryString));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    movie = JSONUtils.extractJSONArray( jsonData, "");
                    movie.setPosterURL(mPosterURL);

                    trailers = JSONUtils.extractTrailersJSONArray(jsonDataTrailers);
                    movie.setTrailers(trailers);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return movie;
            }
        };
    }

    // update the UI with the full movie details

    @Override
    public void onLoadFinished(Loader<MovieItem> loader, MovieItem movie) {
        MovieDetail.setUI( mContext, movie, mBinding );
        LoadingIndicator.show(mBinding, false);
        completeListener.onTaskComplete(movie);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
