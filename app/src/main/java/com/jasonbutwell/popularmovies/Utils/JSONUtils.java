package com.jasonbutwell.popularmovies.Utils;

import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Model.ReviewItem;
import com.jasonbutwell.popularmovies.Model.TrailerItem;

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

    public static ArrayList<TrailerItem> extractTrailersJSONArray(String JSONData)  {

        ArrayList<TrailerItem> trailers = new ArrayList<>();

        JSONArray trailerDataArray = null;
        String JSONArray_start = "results";

        JSONObject trailerData = null;

        try {
            trailerData = new JSONObject( JSONData );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (trailerData != null) {
                trailerDataArray = trailerData.getJSONArray( JSONArray_start );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (trailerDataArray != null) {
            for ( int i=0; i < trailerDataArray.length(); i++ ) {
                JSONObject trailerItem = null;
                try {
                    trailerItem = trailerDataArray.getJSONObject( i );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String mName = "", mKey = "";

                try {
                    if (trailerItem != null) {
                        // extract the field strings needed
                        mName = trailerItem.getString(TMDBInfo.TRAILER_NAME);
                        mKey = trailerItem.getString(TMDBInfo.TRAILER_KEY);

                        // add the new Trailer to the array list
                        trailers.add( new TrailerItem(mName, mKey, TMDBHelper.getYouTubeThumbnailURL(mKey)) );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return trailers;
    }

    public static ArrayList<ReviewItem> extractReviewsJSONArray(String JSONData)  {

        ArrayList<ReviewItem> reviews = new ArrayList<>();

        JSONArray reviewDataArray = null;
        String JSONArray_start = "results";

        JSONObject reviewData = null;

        try {
            reviewData = new JSONObject( JSONData );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (reviewData != null) {
                reviewDataArray = reviewData.getJSONArray( JSONArray_start );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (reviewDataArray != null) {
            for ( int i=0; i < reviewDataArray.length(); i++ ) {
                JSONObject reviewItem = null;
                try {
                    reviewItem = reviewDataArray.getJSONObject( i );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String mId = "", mAuthor = "", mContent = "", mUrl = "";

                try {
                        // extract the field strings needed

                        mId = reviewItem.getString(TMDBInfo.REVIEW_ID);
                        mAuthor = reviewItem.getString(TMDBInfo.REVIEW_AUTHOR);
                        mContent = reviewItem.getString(TMDBInfo.REVIEW_CONTENT);
                        mUrl = reviewItem.getString(TMDBInfo.REVIEW_URL);

                        // add the new Trailer to the array list
                        reviews.add( new ReviewItem(mId, mAuthor, mContent, mUrl) );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return reviews;
    }
}
