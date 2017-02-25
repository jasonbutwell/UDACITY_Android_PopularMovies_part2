package com.jasonbutwell.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.jasonbutwell.popularmovies.Adapter.MovieRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBQueryTask;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, MovieTaskCompleteListener {

    // IMPORTANT!
    // Replace API KEY in Api / APIKey.java with your own 'TMDB API KEY'

    // Enable binding so we can access UI view components easier
    MoviePosterLayoutBinding binding;

    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView mList;

//    private FrameLayout loadingIndicator;
//    private FrameLayout errorLayout;
//    private TextView errorMessageTV;

    // This is where we will store our movies
    private ArrayList<MovieItem> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.movie_poster_layout);

        LoadingIndicator.setBinding(binding);

        mList = (RecyclerView) findViewById(R.id.moviePosterView);
        mList.setLayoutManager(new GridLayoutManager(this,TMDBInfo.NO_OF_POSTERS_PER_ROW));
        mList.setHasFixedSize(true);

        mAdapter = new MovieRecyclerViewAdapter(this, movies, this);
        mList.setAdapter(mAdapter);

//        TODO: Need to re-implement loading indicator components once views are back in layout

//        errorLayout = (FrameLayout) findViewById(R.id.errorMessage);
//        errorMessageTV = (TextView) findViewById(R.id.errorTextView);
//        Button retryButton = (Button) findViewById(R.id.retryButton);
        
//        // Retry button listener
//        retryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadMovieData(TMDBInfo.POPULAR);
//            }
//        });

        // Load popular as default initially - To be replaced by sharedPreference later
        loadMovieData(TMDBInfo.POPULAR);
    }

    // Set the loading indicator to be visible or invisible
    // Shows and hides a frame layout with 2 child views

    // Set the error message and show it or hide it
//    private void showErrorMessage( boolean show, String errorMessage ) {
//
//        if ( show ) {
//            errorLayout.setVisibility(View.VISIBLE);
//
//            if ( errorMessage != null && !errorMessage.equals("") )
//                errorMessageTV.setText(errorMessage);
//            else
//                errorMessageTV.setText("");
//        }
//        else
//            errorLayout.setVisibility(View.INVISIBLE);
//    }

    // Reset position of GridView
    public void resetGridViewPosition() {
        // Scroll to first item in grid
        mList.getLayoutManager().smoothScrollToPosition(mList,null,0);
    }

    // update the movie list arraylist and then the adapter
    private void updateMovies(ArrayList<MovieItem> arrayList) {
        // Clear & rebuild the movie item array list
        movies.clear();
        movies.addAll(arrayList);

        // reset the data set for the adapter
        mAdapter.setData(movies);
    }

    // Check if we have a network connection
    private void loadMovieData( int sortByParam ) {

        //LoadingIndicator.show(true);

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
            new TMDBQueryTask(this).execute( TMDBHelper.buildBaseURL() );

            resetGridViewPosition(); // reset the gridView
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

    // When a movie poster in the RecyclerView is clicked on

    @Override
    public void onListItemClick(int clickedItemIndex) {
        MovieDetail.launchIntent( getApplicationContext(), movies.get(clickedItemIndex) );
    }

    // Callback for when the asyncTask completes

    @Override
    public void onTaskComplete(ArrayList<MovieItem> moviesData) {
        updateMovies(moviesData);
    }
}
