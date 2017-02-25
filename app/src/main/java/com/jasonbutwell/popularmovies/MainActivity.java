package com.jasonbutwell.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jasonbutwell.popularmovies.Adapter.CustomRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Api.APIKey;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
import com.jasonbutwell.popularmovies.Ui.MovieDetails;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    // IMPORTANT!
    // Replace API KEY here or in APIKey.java with your own 'TMDB API KEY'

    private final String YOUR_API_KEY = APIKey.get();

    private CustomRecyclerViewAdapter mAdapter;
    private RecyclerView mList;

    private FrameLayout loadingIndicator;
    private FrameLayout errorLayout;
    private TextView errorMessageTV;

    // This is where we will store our movies
    private ArrayList<MovieItem> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_poster_layout);

        mList = (RecyclerView) findViewById(R.id.moviePosterView);
        mList.setLayoutManager(new GridLayoutManager(this,2));
        mList.setHasFixedSize(true);

        mAdapter = new CustomRecyclerViewAdapter(this, movies, this);
        mList.setAdapter(mAdapter);
        //movieAdapter = new MovieAdapter(this, movies);

//        errorLayout = (FrameLayout) findViewById(R.id.errorMessage);
//        errorMessageTV = (TextView) findViewById(R.id.errorTextView);
//        Button retryButton = (Button) findViewById(R.id.retryButton);

//        gridView = (GridView) findViewById(R.id.gridView);

//        loadingIndicator = (FrameLayout)findViewById(R.id.loadingIndicator);

//        gridView.setAdapter(movieAdapter);
//
//        // Click Listener for the gridView
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                showMovieDetails( position );
//            }
//        });
//
//        // Retry button listener
//        retryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadMovieData(TMDBInfo.POPULAR);
//            }
//        });

        loadMovieData(TMDBInfo.POPULAR);
    }

    // Set the loading indicator to be visible or invisible
    // Shows and hides a frame layout with 2 child views

    private void showLoadingIndicator( boolean show ) {
//        if ( show )
//            loadingIndicator.setVisibility(View.VISIBLE);
//        else
//            loadingIndicator.setVisibility(View.INVISIBLE);
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

        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_ID, movies.get(position).getId());
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_TITLE, movies.get(position).getOriginalTitle() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_POSTER, movies.get(position).getPosterURL() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_OVERVIEW, movies.get(position).getPlotSynopsis() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_VOTES, movies.get(position).getUserRating() );
        movieDetailsIntent.putExtra( TMDBInfo.MOVIE_RELEASEDATE, movies.get(position).getReleaseDate() );

        startActivity(movieDetailsIntent);
    }

    // Reset position of GridView
    public void resetGridViewPosition() {
        // Scroll to first item in grid
//        gridView.smoothScrollToPosition(0);

        mList.getLayoutManager().smoothScrollToPosition(mList,null,0);
    }

    // update the movie list arraylist and then the adapter
    private void updateMovies(ArrayList<MovieItem> arrayList) {
        // Clear & rebuild the movie item array list
        movies.clear();
        movies.addAll(arrayList);

        // reset the data set for the adapter
        mAdapter.setData(movies);
//        movieAdapter.setData(movies);
    }

    // Check if we have a network connection
    private void loadMovieData( int sortByParam ) {

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            // if no network connection, show the error message and retry button
            //showErrorMessage(true, NetworkUtils.ERROR_MESSAGE);
        }
        else {
            // clear and hide the error message
            //showErrorMessage(false, "");

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
                loadMovieData(TMDBInfo.POPULAR);
                return true;

            case R.id.sortby_top_rated :
                loadMovieData(TMDBInfo.TOP_RATED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This is where we would call the Movie details Intent here! :)

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Just display a toast message with the index of the item clicked on for now.
//        String message = "You clicked on item #" + String.valueOf(clickedItemIndex);
//
//        // Create new Toast message and display it
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        MovieDetail.launchIntent( getApplicationContext(), movies.get(clickedItemIndex) );
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
            //showLoadingIndicator( false );
            updateMovies(arrayList);
        }
    }
}
