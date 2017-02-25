package com.jasonbutwell.popularmovies.Ui;

import android.content.Context;
import android.content.Intent;

import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;

/**
 * Created by J on 25/02/2017.
 */

public class MovieDetail {

    // Pass the selected movie's details to the intent to show that information to the user.
    public static void launchIntent(Context context, MovieItem movieItem) {

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

}
