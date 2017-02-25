package com.jasonbutwell.popularmovies.Utils;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by J on 22/01/2017.
 */

public class JSONUtils {

    private static ArrayList<MovieItem> movies = new ArrayList<>();

    private JSONUtils() {}

    public static String extractJSONString( String JSONData, String extractString ) {
        String JSONString = null;

        try {
            JSONObject json = new JSONObject(JSONData);
            JSONString = json.getString(extractString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONString;
    }

    public static JSONObject getJSONObject( String JSONData ) throws JSONException {
        return new JSONObject( JSONData);
    }

    public static ArrayList<MovieItem> extractJSONArray(String JSONData)  {

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
                        id = movieItem.getString(TMDBInfo.MOVIE_ID);
                        title = movieItem.getString( TMDBInfo.MOVIE_TITLE );
                        posterURL = movieItem.getString( TMDBInfo.MOVIE_POSTER );
                        synopsis = movieItem.getString( TMDBInfo.MOVIE_OVERVIEW );
                        rating = movieItem.getString( TMDBInfo.MOVIE_VOTES );
                        release = movieItem.getString( TMDBInfo.MOVIE_RELEASEDATE );

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
