package com.jasonbutwell.popularmovies.Ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Api.PicassoHelper;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;
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
        String movieId = setMovieDetails( generateMovie(movieDetailsIntent) );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), NetworkUtils.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        else
            // Call a new task to obtain the run duration from the movies JSON using it's id
            new TMDBQueryDetailsTask().execute( TMDBHelper.buildDetailURL(movieId) );
    }

    // Generate a MovieItem object based on information pulled from the Intent
    private MovieItem generateMovie(Intent movieIntent) {

        String id="", movieTitle="", moviePoster="", movieOverview="", movieRating="", movieRelease="";

        // Obtain the data passed with the intent as extras
        if (movieIntent != null) {
            id = movieIntent.getStringExtra(TMDBInfo.MOVIE_ID);
            movieTitle = movieIntent.getStringExtra(TMDBInfo.MOVIE_TITLE);
            moviePoster = movieIntent.getStringExtra(TMDBInfo.MOVIE_POSTER);
            movieOverview = movieIntent.getStringExtra(TMDBInfo.MOVIE_OVERVIEW);
            movieRating = movieIntent.getStringExtra(TMDBInfo.MOVIE_VOTES);
            movieRelease = movieIntent.getStringExtra(TMDBInfo.MOVIE_RELEASEDATE);
        }

        return new MovieItem(id, movieTitle, movieOverview, movieRating, movieRelease, moviePoster);
    }

    // Updates the UI with movie Item details
    private String setMovieDetails(MovieItem movieItem) {

        String rating = movieItem.getUserRating() + " / 10";

        // Load image into ImageView with Picasso helper
        PicassoHelper.loadImage( getApplicationContext(), movieItem.getPosterURL(), movieDetailsBinding.moviePoster );

        movieDetailsBinding.movieTitle.setText(movieItem.getOriginalTitle());                                       // set the movie title
        movieDetailsBinding.movieDescription.setText(movieItem.getPlotSynopsis());                                  // set the synopsis
        movieDetailsBinding.movieRating.setText(rating);                                                            // show the rating
        movieDetailsBinding.movieReleaseDate.setText( DateTimeUtils.USDateToUKDate(movieItem.getReleaseDate()) );   // show the release date (reformatted)

        return movieItem.getId();
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
