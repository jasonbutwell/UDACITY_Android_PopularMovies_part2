package com.jasonbutwell.popularmovies.Api;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.LoaderManager;

import com.jasonbutwell.popularmovies.BackgroundTask.TMDBLoader;
import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;

import java.net.MalformedURLException;
import java.net.URL;

import static com.jasonbutwell.popularmovies.Api.TMDBInfo.BASE_URL;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.IMAGE_SIZE;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.PARAM_API_KEY;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.PARAM_PAGE;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.PARAM_YOUTUBE_VIEW;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.REVIEWS;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.TRAILERS;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.YOUTUBE_BASE_URL;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.YOUTUBE_THUMBNAIL;
//import static com.jasonbutwell.popularmovies.Api.TMDBInfo.filterQuery;
//import static com.jasonbutwell.popularmovies.Api.TMDBInfo.queryFilters;

/**
 * Created by J on 21/01/2017.
 */

 public class TMDBHelper {
    private TMDBHelper() {}                 // Private constructor

    private static int page_number = 1;     // For expansion, for grabbing multiple pages later on
    private static String mFilter;

    // Builds a basic movie obj
    public static MovieItemBasic buildMovie(String id, String title, String posterURL ) {
        MovieItem movie = new MovieItem();
        movie.setId(id);
        movie.setOriginalTitle( title );
        movie.setPosterURL( TMDBHelper.buildImageURL( posterURL ));

        return movie;
    }

    // Load initial movie info for posters
    public static void loadMovieData(Context context, MovieTaskCompleteListener listener, String sortByParam, Object binding, LoaderManager loaderManager ) {

        String errorMessage = context.getString(R.string.network_error_message);

        // Check if we have a network connection
        if ( !NetworkUtils.isNetworkAvailable(context)) {
            LoadingIndicator.showError(binding, true, errorMessage );           // if no network connection,
        }                                                                       // show the error message and retry button
        else {
            LoadingIndicator.showError(binding, false, "");                     // clear and hide the error message

            mFilter = sortByParam;
            new TMDBLoader(context, loaderManager, binding, listener);
        }                                                                       // and extract the JSON data
    }

    public static URL buildDetailURL( String id ) {
        URL url = null;

        Uri.Builder buildUri =
                Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(PARAM_API_KEY, APIKey.get());
        try {
                url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailersURL( String id ) {
        URL url = null;

        Uri.Builder buildUri =
                Uri.parse(BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(TRAILERS)
                        .appendQueryParameter(PARAM_API_KEY, APIKey.get());
        try {
            url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewsURL( String id ) {
        URL url = null;

        Uri.Builder buildUri =
                Uri.parse(BASE_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(REVIEWS)
                        .appendQueryParameter(PARAM_API_KEY, APIKey.get());
        try {
            url = new URL(buildUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getYouTubeThumbnailURL( String key ) {
        return TMDBInfo.YOUTUBE_IMG_BASE_URL + key + "/" + YOUTUBE_THUMBNAIL;
    }

    // Needed to take our key and transform it into a proper youtube URL
    public static String buildYouTubeURL( String key ) {
        String youTubeURL = null;

        Uri.Builder buildUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_YOUTUBE_VIEW, key);

        try {
                youTubeURL = new URL(buildUri.toString()).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return youTubeURL;
    }

    // Quick way to build an Image URL to fetch image extracted from JSON
    public static String buildImageURL(String imageName) {
        return (TMDBInfo.BASE_IMAGE_URL+IMAGE_SIZE+imageName);
    }

    // Builds the base URL to retrieve the JSON
    public static URL buildBaseURL() {
        URL url = null;

        Uri.Builder buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_PAGE, getPage_number_string())
                .appendPath(mFilter)
                .appendQueryParameter(PARAM_API_KEY, APIKey.get());

        try {
                url = new URL(buildUri.toString());

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
    static String getSortByText() {
        return mFilter;
    }

    // Set filter query component
    public static void setSortByText(String filter) {
        mFilter = filter;
    }

}
