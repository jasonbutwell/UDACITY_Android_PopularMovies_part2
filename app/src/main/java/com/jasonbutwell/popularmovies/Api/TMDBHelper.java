package com.jasonbutwell.popularmovies.Api;

import android.net.Uri;
import android.util.Log;

import com.jasonbutwell.popularmovies.Model.MovieItem;

import java.net.MalformedURLException;
import java.net.URL;

import static com.jasonbutwell.popularmovies.Api.TMDBInfo.BASE_URL;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.IMAGE_SIZE;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.PARAM_API_KEY;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.PARAM_PAGE;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.filterQuery;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.queryFilters;

/**
 * Created by J on 21/01/2017.
 */

 public class TMDBHelper {
    private TMDBHelper() {}     // Private constructor

    // For expansion, for grabbing multiple pages later on
    private static int page_number = 1;

    // Builds a movie item and returns it
    public static MovieItem buildMovie(String id, String title, String posterURL, String synopsis, String rating, String release ) {
        MovieItem movie = new MovieItem();
        movie.setId(id);
        movie.setOriginalTitle( title );
        movie.setPosterURL( TMDBHelper.buildImageURL( posterURL ));
        movie.setPlotSynopsis( synopsis );
        movie.setUserRating( rating );
        movie.setReleaseDate( release );

        return movie;
    }

    public static URL buildDetailURL( String id ) {
        URL url = null;

        Uri.Builder buildUri =
                Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(PARAM_API_KEY, APIKey.get());

        try {
            url = new URL(buildUri.toString());

            Log.i("URL",url.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Quick way to build an Image URL to fetch image extracted from JSON
    public static String buildImageURL(String imageName) {
        return (com.jasonbutwell.popularmovies.Api.TMDBInfo.BASE_IMAGE_URL+IMAGE_SIZE+imageName);
    }

    // Builds the base URL to retrieve the JSON
    public static URL buildBaseURL() {
        URL url = null;

        Uri.Builder buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_PAGE, getPage_number_string())
                .appendPath(filterQuery)
                .appendQueryParameter(PARAM_API_KEY, APIKey.get());

        try {
            url = new URL(buildUri.toString());

            Log.i("URL", url.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Increase current page
    public static void nextPage() {
        page_number++;
    }

    // Set page number we are on
    public static void setPageNumber( int pagenumber ) {
        page_number = pagenumber;
    }

    // Gets the current page number
    public static int getPage_number() {
        return page_number;
    }

    // Get the page number as a string
    public static String getPage_number_string() {
        return String.valueOf(page_number);
    }

    // Get the filter query component
    static String getFilterQueryString() {
        return filterQuery;
    }

    // Set filter query component
    public static void setSortByText(int id) {
        filterQuery = getSortByText(id);
    }

    // Get the sort query component as a string
    public static String getSortByText(int id) {
        if ( id < queryFilters.length )
            return queryFilters[id];
        else
            return "";
    }
}
