package com.jasonbutwell.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by J on 22/01/2017.
 */

final class JSONUtils {

    private static ArrayList<MovieItem> movies = new ArrayList<>();

    private JSONUtils() {}

    static String extractJSONString( String JSONData, String extractString ) {
        String JSONString = null;

        try {
            JSONObject json = new JSONObject(JSONData);
            JSONString = json.getString(extractString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONString;
    }

    static ArrayList<MovieItem> extractJSONArray(String JSONData)  {

        JSONArray movieDataArray = null;
        String JSONArray_start = "results";

        JSONObject movieData = null;

        movies.clear();

        try {
            movieData = new JSONObject( JSONData );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (movieData != null) {
                movieDataArray = movieData.getJSONArray( JSONArray_start );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (movieDataArray != null) {
            for ( int i=0; i < movieDataArray.length(); i++ ) {
                JSONObject movieItem = null;
                try {
                    movieItem = movieDataArray.getJSONObject( i );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String id = null, title = null, posterURL = null, synopsis = null, rating = null, release = null;

                try {
                    if (movieItem != null) {
                        // extract the field strings needed
                        id = movieItem.getString(TMDBHelper.MOVIE_ID);
                        title = movieItem.getString( TMDBHelper.MOVIE_TITLE );
                        posterURL = movieItem.getString( TMDBHelper.MOVIE_POSTER );
                        synopsis = movieItem.getString( TMDBHelper.MOVIE_OVERVIEW );
                        rating = movieItem.getString( TMDBHelper.MOVIE_VOTES );
                        release = movieItem.getString( TMDBHelper.MOVIE_RELEASEDATE );

                        // add the new movie to the array list
                        movies.add( TMDBHelper.buildMovie( id, title, posterURL, synopsis, rating, release ) );

                        // DEBUG OUTPUT
//                        Log.i("MOVIE id:",id);
//                        Log.i("MOVIE title:",title);
//                        Log.i("MOVIE poster:",posterURL);
//                        Log.i("MOVIE plot:",synopsis);
//                        Log.i("MOVIE rating:",rating);
//                        Log.i("MOVIE release:",release);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return movies;
    }
}
