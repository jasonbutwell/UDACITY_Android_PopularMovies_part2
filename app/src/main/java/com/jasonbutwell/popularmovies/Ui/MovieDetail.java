package com.jasonbutwell.popularmovies.Ui;

import android.content.Context;
import android.content.Intent;

import com.jasonbutwell.popularmovies.Api.PicassoImageHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Utils.DateTimeUtils;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

/**
 * Created by J on 25/02/2017.
 */

public class MovieDetail {

    // Pass the selected movie's details to the intent to show that information to the user.
    public static void launchMovieDetailIntent(Context context, MovieItem movieItem) {

        Intent movieDetailsIntent = new Intent( context, MovieDetails.class );
        movieDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_ID, movieItem.getId());
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_TITLE, movieItem.getOriginalTitle() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_POSTER, movieItem.getPosterURL() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_OVERVIEW, movieItem.getPlotSynopsis() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_VOTES, movieItem.getUserRating() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_RELEASEDATE, movieItem.getReleaseDate() );

        context.startActivity(movieDetailsIntent);
    }

    // Generate a MovieItem object based on information pulled from the Intent
    public static MovieItem generateMovie(Intent movieIntent) {

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
    public static String setMovieDetails(Context context, MovieItem movieItem, ActivityMovieDetailsBinding binding) {

        String rating = movieItem.getUserRating() + " / 10";

        // Load image into ImageView with Picasso helper
        PicassoImageHelper.loadImage( context, movieItem.getPosterURL(), binding.moviePoster );

        binding.movieTitle.setText(movieItem.getOriginalTitle());                                       // set the movie title
        binding.movieDescription.setText(movieItem.getPlotSynopsis());                                  // set the synopsis
        binding.movieRating.setText(rating);                                                            // show the rating
        binding.movieReleaseDate.setText( DateTimeUtils.USDateToUKDate(movieItem.getReleaseDate()) );   // show the release date (reformatted)

        return movieItem.getId();
    }

}
