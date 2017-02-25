package com.jasonbutwell.popularmovies.Ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import java.io.IOException;
import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    // Use data binding to reduce boilerplate code
    public ActivityMovieDetailsBinding movieDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our <layout> enclosed layout with the data binding
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        // Get the calling itent passed over
        Intent movieDetailsIntent = getIntent();

        // Update the UI with the details obtained
        String movieId = MovieDetail.setMovieDetails( getApplicationContext(),MovieDetail.generateMovie(movieDetailsIntent), movieDetailsBinding );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), NetworkUtils.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        else
            // Call a new task to obtain the run duration from the movies JSON using it's id
            new TMDBQueryDetailsTask().execute( TMDBHelper.buildDetailURL(movieId) );
    }

    // We use this to grab the runtime from the movie info using just the id
    // We could build upon this to grab additional information in future

    public class TMDBQueryDetailsTask extends AsyncTask<URL, Void, String> {

        URL UrlToSearch = null;
        String searchResults = null;
        String data = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(URL... urls) {

            URL searchURL = null;
            searchURL = urls[0];

            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl( searchURL );

            } catch (IOException e) {
                e.printStackTrace();
            }

            data = JSONUtils.extractJSONString( searchResults, "runtime" );

            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            movieDetailsBinding.movieDuration.setText( DateTimeUtils.convertToHoursMins( data ) );              // Show the movie duration
        }
    }
}
