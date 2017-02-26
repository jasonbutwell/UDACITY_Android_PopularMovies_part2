package com.jasonbutwell.popularmovies.Ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import java.io.IOException;
import java.net.URL;

public class MovieDetails extends AppCompatActivity implements MovieDetailTaskCompleteListener, LoaderManager.LoaderCallbacks<String> {

    // Use data binding to reduce boilerplate code
    public ActivityMovieDetailsBinding movieDetailsBinding;

    private static final int LOADER_ID = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our <layout> enclosed layout with the data binding
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        // Get the calling itent passed over
        Intent movieDetailsIntent = getIntent();

        // Update the UI with the details obtained
        String movieId = MovieDetail.setUI( getApplicationContext(), MovieDetail.generateFromIntent(movieDetailsIntent), movieDetailsBinding );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
        else {
            // Call a new task to obtain the run duration from the movies JSON using it's id - (this) means pass the listener instance
            //new TMDBQueryDetailsTask(this).execute( TMDBHelper.buildDetailURL(movieId) );

            Bundle queryBundle = new Bundle();
            queryBundle.putString("json", TMDBHelper.buildDetailURL(movieId).toString());

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(LOADER_ID);

            if (loader == null)
                loaderManager.initLoader(LOADER_ID, queryBundle, this);
            else
                loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }
    }

    // This is run when the asyncTask completes
    @Override
    public void onTaskComplete(String text) {
        movieDetailsBinding.movieDuration.setText( DateTimeUtils.convertToHoursMins( text ) );              // Show the movie duration
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) return;

                LoadingIndicator.show(movieDetailsBinding, true);

                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String queryString = args.getString("json");

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
        LoadingIndicator.show(movieDetailsBinding, false);
        movieDetailsBinding.movieDuration.setText( DateTimeUtils.convertToHoursMins( data ) );
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
