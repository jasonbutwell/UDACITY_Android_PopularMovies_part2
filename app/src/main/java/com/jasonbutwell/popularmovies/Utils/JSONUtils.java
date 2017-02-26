package com.jasonbutwell.popularmovies.Utils;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by J on 22/01/2017.
 */

public class JSONUtils {

    private static ArrayList<MovieItemBasic> movies = new ArrayList<>();

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

    public static String extractJSONObjectString( JSONObject json, String extractString ) throws JSONException {
        String extracted = json.getString(extractString);
        return extracted;
    }

    public static MovieItem extractJSONArray(String JSONData, String n) throws JSONException {
        JSONObject movieData = null;
        MovieItem movieItem = new MovieItem();

        try {
            movieData = new JSONObject( JSONData );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set the movie Item info - minus the poster as that's not available here
        movieItem.setId(extractJSONObjectString( movieData, TMDBInfo.MOVIE_ID ));
        movieItem.setOriginalTitle(extractJSONObjectString( movieData, TMDBInfo.MOVIE_TITLE ));
        movieItem.setPlotSynopsis(extractJSONObjectString( movieData, TMDBInfo.MOVIE_OVERVIEW ));
        movieItem.setUserRating(extractJSONObjectString( movieData, TMDBInfo.MOVIE_VOTES ));
        movieItem.setReleaseDate(extractJSONObjectString( movieData, TMDBInfo.MOVIE_RELEASEDATE ));
        movieItem.setRunTime(extractJSONObjectString( movieData, TMDBInfo.MOVIE_RUNTIME));

        return movieItem;   // return the movie we created
    }

    public static ArrayList<MovieItemBasic> extractJSONArray(String JSONData)  {

        JSONArray movieDataArray = null;
        String JSONArray_start = "results";

        JSONObject movieData = null;

        movies.clear(); // clear arraylist

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

                String id = "", title = "", posterURL = "";

                try {
                    if (movieItem != null) {
                        // extract the field strings needed
                        id = movieItem.getString(TMDBInfo.MOVIE_ID);
                        title = movieItem.getString( TMDBInfo.MOVIE_TITLE );
                        posterURL = movieItem.getString( TMDBInfo.MOVIE_POSTER );

                        // add the new movie to the array list
                        movies.add( TMDBHelper.buildMovie( id, title, posterURL ) );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return movies;
    }
}
