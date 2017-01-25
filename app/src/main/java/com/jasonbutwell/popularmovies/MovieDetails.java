package com.jasonbutwell.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    // References for movie duration UI component
    private TextView movieDurationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // References for our UI components
        TextView movieTitleView;
        ImageView moviePosterURLView;
        TextView movieSynopsisView;
        TextView movieRatingView;
        TextView movieReleaseView;

        // obtain and store those UI references
        movieTitleView = (TextView)findViewById(R.id.movieTitle);
        moviePosterURLView = (ImageView) findViewById(R.id.moviePoster);
        movieSynopsisView = (TextView) findViewById(R.id.movieDescription);
        movieRatingView = (TextView) findViewById(R.id.movieRating);
        movieReleaseView = (TextView) findViewById(R.id.movieReleaseDate);
        movieDurationView = (TextView) findViewById(R.id.movieDuration);

        // Get the calling itent passed over
        Intent movieDetailsIntent = getIntent();

        // Obtain the data passed with the intent as extras
        String id = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_ID);
        String movieTitle = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_TITLE);
        String moviePoster = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_POSTER);
        String movieSynopsis = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_OVERVIEW);
        String movieRating = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_VOTES);
        String movieRelease = movieDetailsIntent.getStringExtra(TMDBHelper.MOVIE_RELEASEDATE);

        // show the movie title
        if ( movieTitle != null )
            movieTitleView.setText(movieTitle);

        // Shows the image thumbnail for the movie
        if ( moviePoster != null ) {
            Picasso
                    .with(getApplicationContext())
                    .load( moviePoster )
                    .fit()
                    .into((ImageView)moviePosterURLView);
        }

        // show the synopsis
        if ( movieSynopsis != null )
            movieSynopsisView.setText(movieSynopsis);

        // show the rating
        if ( movieRating != null ) {
            String rating = movieRating + " / 10";
            movieRatingView.setText(rating);
        }

        // show the release date (reformatted)
        if ( movieRelease != null )
            movieReleaseView.setText(TMDBHelper.USDateToUKDate( movieRelease ));

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), NetworkUtils.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        else
            // Call a new task to obtain the run duration from the movies JSON using it's id
            new TMDBQueryDetailsTask().execute( TMDBHelper.buildDetailURL(id) );
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
            // Show the movie duration
            movieDurationView.setText( TMDBHelper.convertToHoursMins( data ) );
        }
    }
}
