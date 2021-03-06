package com.jasonbutwell.popularmovies.Ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Activity.MovieDetailsActivity;
import com.jasonbutwell.popularmovies.Api.PicassoImageHelper;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Database.MovieContract;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

/**
 * Created by J on 25/02/2017.
 */

public class MovieDetail {

    // checks the result of the check query and if not found inserts the movie into the favourites DB
    // Removes the record if it already exists

    public static int insertOrDeleteFavourite( Context context, MovieItemBasic movie, int count ) {
        int result = -1;

        String movieId = movie.getId();                             // get Movie ID
        String movieTitle = movie.getOriginalTitle();               // get Movie Title

        if (count == 0) {
            String moviePoster = movie.getPosterURL();         // get Movie Poster URL

            ContentValues contentValues = new ContentValues();
            contentValues.put( MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId );
            contentValues.put( MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,  movieTitle );
            contentValues.put( MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL, moviePoster );

            // insert
            if ( context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues ) != null ) {
                String toastMessageAdd = "Favoured " + movieTitle;
                MovieDetail.showMessage(context, toastMessageAdd, Toast.LENGTH_SHORT);
                result = 1;
            }

        } else {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
            // delete
            if (context.getContentResolver().delete(uri, null, null) == 1) {
                String toastMessageDelete = "Unfavoured " + movieTitle;
                MovieDetail.showMessage(context, toastMessageDelete, Toast.LENGTH_SHORT);
                result = 0;
            }
        }

        return result;
    }

    // Pass the selected movie's details to the intent to show that information to the user.
    public static void launchIntent(Context context, MovieItemBasic movieItem) {
        Intent movieDetailsIntent = new Intent( context, MovieDetailsActivity.class );
        movieDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Just store the id, poster, title in the intent for now
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_ID, movieItem.getId());
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_TITLE, movieItem.getOriginalTitle());
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_POSTER, movieItem.getPosterURL() );
        context.startActivity(movieDetailsIntent);
    }

    // Used to check if the youtube package is installed by its official package name
    // If no YouTube app installed, then the URL is to be opened in the web browser instead

    public static void launchYouTubeIntent(Context context, TrailerItem trailer ) {
        if ( context != null && trailer != null ) {
            // Display a message to say what trailer we are opening
            showMessage(context, "Opening " + trailer.getDescription(), Toast.LENGTH_SHORT);

            // If the youTube Intent is not null then the package was found, so we can open the YouTube app
            if ( checkPackageInstalled(context, TMDBInfo.YOUTUBE_PACKAGE_NAME) )
                openURI(context, TMDBInfo.YOUTUBE_URI + trailer.getYoutubeURL());
            else
                openBrowserURL( context, trailer.getYoutubeURL());
        }
    }

    // Check to see if a particular software package is installed
    public static boolean checkPackageInstalled( Context context, String packageName ) {
        Intent youTubeIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return youTubeIntent != null;
    }

    // Open a specified URI
    public static void openURI(Context context, String URIString ) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URIString));
        context.startActivity(intent);
    }

    // Open a URL in the web browser
    public static void openBrowserURL(Context context, String url ) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TMDBHelper.buildYouTubeURL(url)));
        context.startActivity(browserIntent);
    }

    // Generate a MovieItem object based on information pulled from the Intent
    public static MovieItemBasic generateFromIntent(Intent movieIntent) {
        // Obtain the data passed with the intent as extras
        if (movieIntent != null)
            return new MovieItemBasic(
                    movieIntent.getStringExtra(TMDBInfo.MOVIE_ID),
                    movieIntent.getStringExtra(TMDBInfo.MOVIE_TITLE),
                    movieIntent.getStringExtra(TMDBInfo.MOVIE_POSTER)
            );
            else return null;
    }

    // Updates the UI with movie Item details
    public static void setUI(Context context, MovieItem movieItem, ActivityMovieDetailsBinding binding) {

        // Load image into ImageView with Picasso helper
        PicassoImageHelper.loadImage( context, movieItem.getPosterURL(), binding.moviePoster );

        // Set extended UI Movie details fields from just the movie ID
        binding.movieTitle.setText(movieItem.getOriginalTitle());                                       // set the movie title
        binding.movieDescription.setText(movieItem.getPlotSynopsis());                                  // set the synopsis
        binding.movieRating.setText(movieItem.getUserRatingOutOfTen());                                 // show the rating
        binding.movieReleaseDate.setText( DateTimeUtils.USDateToUKDate(movieItem.getReleaseDate()) );   // show the release date (reformatted)
        binding.movieDuration.setText( DateTimeUtils.convertToHoursMins( movieItem.getRunTime() ) );
    }

    // Displays a simple Toast message
    public static void showMessage(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }
}
