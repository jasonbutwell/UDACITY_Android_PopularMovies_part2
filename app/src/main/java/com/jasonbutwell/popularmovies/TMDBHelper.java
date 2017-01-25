package com.jasonbutwell.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J on 21/01/2017.
 */

 final class TMDBHelper {
    private TMDBHelper() {}     // Private constructor

    private static String API_KEY;

    // string literals to facilitate easier extracting of fields from the JSON data
    static final String MOVIE_ID = "id";
    static final String MOVIE_TITLE = "original_title";
    static final String MOVIE_POSTER = "poster_path";
    static final String MOVIE_BACKGROUND = "backdrop_path";
    static final String MOVIE_OVERVIEW = "overview";
    static final String MOVIE_RELEASEDATE = "release_date";
    static final String MOVIE_ADULT = "adult";
    static final String MOVIE_VOTES = "vote_average";
    static final String MOVIE_DURATION = "duration";

    // For building the base URL and image URLs
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    // various query parameters needed
    private static final String PARAM_SORTBY = "sort_by";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";

    // Used for sorting
    static final int POPULAR = 0, TOP_RATED = 1;
    private static final String[] queryFilters = { "popular", "top_rated" };
    private static String filterQuery = queryFilters[POPULAR];

    // For expansion, for grabbing multiple pages later on
    private static int page_number = 1;

    // Builds a movie item and returns it
    static MovieItem buildMovie( String id, String title, String posterURL, String synopsis, String rating, String release ) {
        MovieItem movie = new MovieItem();
        movie.setId(id);
        movie.setOriginalTitle( title );
        movie.setPosterURL( TMDBHelper.buildImageURL( posterURL ));
        movie.setPlotSynopsis( synopsis );
        movie.setUserRating( rating );
        movie.setReleaseDate( release );

        return movie;
    }

    // Helper function to convert mins to hours and mins
    static String convertToHoursMins( String duration ) {
        String timeString = "";
        String hoursString = "";

        if ( duration != null && !duration.equals("")) {
            int hours = Integer.parseInt(duration) / 60;
            int mins = Integer.parseInt(duration) % 60;
            hoursString = "hour";

            // Hours or Hour?
            if ( hours == 0 )
                    timeString = String.format(Locale.ENGLISH,"%d mins", mins);
                else {
                    if (hours > 1)
                        hoursString = "hours";

                    timeString = String.format(Locale.ENGLISH, "%d %s %d mins", hours, hoursString, mins);
                }

            //timeString = String.format(Locale.ENGLISH,"%d %s %d mins", hours, hoursString, mins);
        }

        return timeString;
    }

    // Formats the US date to a UK one

    static String USDateToUKDate( String dateToParse ) {
        return formatDate("yyyy-MM-dd", dateToParse, "dd.MM.yyyy" );
    }

    // Helper routine to help reformat dates

    static String formatDate(String dateFormat, String dateToParse, String dateOutputFormat ) {
        Date date = null;
        String formattedDate = null;

        if ( dateFormat != null && dateToParse != null && dateOutputFormat != null ) {
            try {
                date = new SimpleDateFormat(dateFormat,Locale.getDefault()).parse(dateToParse);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            formattedDate = new SimpleDateFormat(dateOutputFormat, Locale.getDefault()).format(date);
        }

        return formattedDate;
    }

    static URL buildDetailURL( String id ) {
        URL url = null;

        Uri.Builder buildUri =
                Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(PARAM_API_KEY, API_KEY);

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Quick way to build an Image URL to fetch image extracted from JSON
    static String buildImageURL(String imageName) {
        return (BASE_IMAGE_URL+IMAGE_SIZE+imageName);
    }

    // Builds the base URL to retrieve the JSON
    static URL buildBaseURL() {
        URL url = null;

        Uri.Builder buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_PAGE, getPage_number_string())
                .appendPath(filterQuery)
                .appendQueryParameter(PARAM_API_KEY, API_KEY);

        try {
            url = new URL(buildUri.toString());

            //Log.i("URL", url.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Allow us to store API KEY here
    static void setApiKey( String ApiKey ) {
        API_KEY = ApiKey;
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
    private static String getPage_number_string() {
        return String.valueOf(page_number);
    }

    // Get the filter query component
    static String getFilterQueryString() {
        return filterQuery;
    }

    // Set filter query component
    static void setSortByText(int id) {
        filterQuery = getSortByText(id);
    }

    // Get the sort query component as a string
    private static String getSortByText(int id) {
        if ( id < queryFilters.length )
            return queryFilters[id];
        else
            return "";
    }
}
