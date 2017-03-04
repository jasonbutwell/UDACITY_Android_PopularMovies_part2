package com.jasonbutwell.popularmovies.Api;
/**
 * Created by J on 25/02/2017.
 */

public class TMDBInfo {

    public static final int NO_OF_POSTERS_PER_ROW = 2;  // For the Recycler layout manager
    public static final int NO_OF_TRAILERS_PER_ROW = 3; // For the Recycler's grid layout

    // String literals to facilitate easier extracting of fields from the JSON data
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "original_title";
    public static final String MOVIE_POSTER = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RELEASEDATE = "release_date";
    public static final String MOVIE_VOTES = "vote_average";
    public static final String MOVIE_RUNTIME = "runtime";

    // Review JSON fields
    public static final String REVIEW_ID = "id";
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";
    public static final String REVIEW_URL = "url";

    // Trailer JSON fields
    public static final String TRAILER_NAME = "name";
    public static final String TRAILER_KEY = "key";

    public static final String YOUTUBE_URI = "vnd.youtube://";
    public static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";

//    public static final String MOVIE_BACKGROUND = "backdrop_path";
//    public static final String MOVIE_ADULT = "adult";
//    public static final String MOVIE_DURATION = "duration";

    // For building the base URL and image URLs
    public static final String YOUTUBE_IMG_BASE_URL = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";

    // various query parameters needed
//    public static final String PARAM_SORTBY = "sort_by";
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_YOUTUBE_VIEW = "v";

    // For Trailers and Reviews
    public static final String TRAILERS = "videos";
    public static final String REVIEWS = "reviews";

    // Youtube Thumbnail default thumbnail filename
    public static final String YOUTUBE_THUMBNAIL = "0.jpg";

    public static final String MOVIE_FILTER_POPULAR = "popular";
    public static final String MOVIE_FILTER_TOP_RATED = "top_rated";
    public static final String MOVIE_FILTER_FAVOURITES = "favourites";

    // Used for sorting
    //public static final int POPULAR = 0, TOP_RATED = 1;
    //public static final String[] queryFilters = { getApplicationContext, "top_rated" };
    //public static String filterQuery = queryFilters[POPULAR];
}
