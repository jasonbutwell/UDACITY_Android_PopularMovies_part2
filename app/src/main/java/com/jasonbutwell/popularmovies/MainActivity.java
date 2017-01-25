package com.jasonbutwell.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // IMPORTANT!
    // Replace API KEY here or in APIKey.java with your own 'TMDB API KEY'

    private final String YOUR_API_KEY = APIKey.get();

    private GridView gridView;
    private MovieAdapter movieAdapter;
    private FrameLayout loadingIndicator;

    private FrameLayout errorLayout;
    private TextView errorMessageTV;

    // This is where we will store our movies
    private ArrayList<MovieItem> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TMDBHelper.setApiKey( YOUR_API_KEY );

        movieAdapter = new MovieAdapter(this, movies);

        errorLayout = (FrameLayout) findViewById(R.id.errorMessage);
        errorMessageTV = (TextView) findViewById(R.id.errorTextView);

        Button retryButton = (Button) findViewById(R.id.retryButton);

        gridView = (GridView) findViewById(R.id.gridView);
        loadingIndicator = (FrameLayout)findViewById(R.id.loadingIndicator);

        gridView.setAdapter(movieAdapter);

        // Click Listener for the gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showMovieDetails( position );
            }
        });

        // Retry button listener
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData(TMDBHelper.POPULAR);
            }
        });

        loadMovieData(TMDBHelper.POPULAR);
    }

    // Set the loading indicator to be visible or invisible
    // Shows and hides a frame layout with 2 child views

    private void showLoadingIndicator( boolean show ) {
        if ( show )
            loadingIndicator.setVisibility(View.VISIBLE);
        else
            loadingIndicator.setVisibility(View.INVISIBLE);
    }

    // Set the error message and show it or hide it
    private void showErrorMessage( boolean show, String errorMessage ) {

        if ( show ) {
            errorLayout.setVisibility(View.VISIBLE);

            if ( errorMessage != null && !errorMessage.equals("") )
                errorMessageTV.setText(errorMessage);
            else
                errorMessageTV.setText("");
        }
        else
            errorLayout.setVisibility(View.INVISIBLE);
    }

    // Pass the selected movie's details to the intent to show that information to the user.
    private void showMovieDetails( int position ) {

        Intent movieDetailsIntent = new Intent( getApplicationContext(), MovieDetails.class );

        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_ID, movies.get(position).getId());
        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_TITLE, movies.get(position).getOriginalTitle() );
        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_POSTER, movies.get(position).getPosterURL() );
        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_OVERVIEW, movies.get(position).getPlotSynopsis() );
        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_VOTES, movies.get(position).getUserRating() );
        movieDetailsIntent.putExtra( TMDBHelper.MOVIE_RELEASEDATE, movies.get(position).getReleaseDate() );

        startActivity(movieDetailsIntent);
    }

    // Reset position of GridView
    public void resetGridViewPosition() {
        // Scroll to first item in grid
        gridView.smoothScrollToPosition(0);
    }

    // update the movie list arraylist and then the adapter
    private void updateMovies(ArrayList<MovieItem> arrayList) {
        // Clear & rebuild the movie item array list
        movies.clear();
        movies.addAll(arrayList);

        // reset the data set for the adapter
        movieAdapter.setData(movies);
    }

    // Check if we have a network connection
    private void loadMovieData( int sortByParam ) {

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            // if no network connection, show the error message and retry button
            showErrorMessage(true, NetworkUtils.ERROR_MESSAGE);
        }
        else {
            // clear and hide the error message
            showErrorMessage(false, "");

            // set to sort by selected parameter
            TMDBHelper.setSortByText( sortByParam );

            // create new query to download and extract the JSON data
            new TMDBQueryTask().execute( TMDBHelper.buildBaseURL());

            // reset the gridView
            resetGridViewPosition();
        }
    }

    // Create our options menu so we can filter the movies by
    // 1. Popular, 2. Top rated

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by_menu, menu);
        return true;
    }

    // Detect what filter was requested and set that filter
    // to be used for the http get request

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId() ) {

            case R.id.sortby_popular :
                loadMovieData(TMDBHelper.POPULAR);
                return true;

            case R.id.sortby_top_rated :
                loadMovieData(TMDBHelper.TOP_RATED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // We use this to grab the JSON for the movies we want to see
    // We then break up the results and store them in an arraylist of custom type MovieItem

    public class TMDBQueryTask extends AsyncTask< URL, Void, ArrayList<MovieItem> > {

        URL UrlToSearch = null;
        String searchResults = null;
        ArrayList<MovieItem> arrayList = null;

        @Override
        protected void onPreExecute() {
            // Loading Indicator visible
            showLoadingIndicator( true );
        }

        @Override
        protected ArrayList<MovieItem> doInBackground( URL... urls ) {

            URL searchURL = null;
            searchURL = urls[0];

            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl( searchURL );

            } catch (IOException e) {
                e.printStackTrace();
            }

            arrayList = JSONUtils.extractJSONArray( searchResults );

            return arrayList;
        }

        @Override
        protected void onPostExecute( ArrayList<MovieItem> arrayList ) {
            // Loading indicator invisible
            showLoadingIndicator( false );
            updateMovies(arrayList);
        }
    }
}
