package com.jasonbutwell.popularmovies.Ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBQueryDetailsTask;
import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

public class MovieDetails extends AppCompatActivity implements MovieDetailTaskCompleteListener {

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
        String movieId = MovieDetail.setMovieDetails( getApplicationContext(), MovieDetail.generateMovie(movieDetailsIntent), movieDetailsBinding );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), NetworkUtils.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        else
            // Call a new task to obtain the run duration from the movies JSON using it's id
            // (this) means pass the listener instance
            new TMDBQueryDetailsTask(this).execute( TMDBHelper.buildDetailURL(movieId) );
    }

    @Override
    public void onTaskComplete(String text) {
        movieDetailsBinding.movieDuration.setText( DateTimeUtils.convertToHoursMins( text ) );              // Show the movie duration
    }
}
