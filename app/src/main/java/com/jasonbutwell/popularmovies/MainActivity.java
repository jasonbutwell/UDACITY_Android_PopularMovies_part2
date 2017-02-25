package com.jasonbutwell.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jasonbutwell.popularmovies.Adapter.MovieRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
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

    // This is where we will store our movies
    private ArrayList<MovieItem> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.movie_poster_layout);
        LoadingIndicator.setBinding(binding);

        binding.moviePosterView.setLayoutManager(new GridLayoutManager(this,TMDBInfo.NO_OF_POSTERS_PER_ROW));
        binding.moviePosterView.setHasFixedSize(true);

        mAdapter = new MovieRecyclerViewAdapter(this, movies, this);
        binding.moviePosterView.setAdapter(mAdapter);

        // Load popular as default initially - To be replaced by sharedPreference later
        loadMovies(TMDBInfo.POPULAR);
    }

    // Reset position of GridView
    public void resetGridViewPosition() {
        // Scroll to first item in grid
        binding.moviePosterView.getLayoutManager().smoothScrollToPosition(binding.moviePosterView,null,0);
    }

    // update the movie list arraylist and then the adapter
    private void updateMovies(ArrayList<MovieItem> arrayList) {
        // Clear & rebuild the movie item array list
        movies.clear();
        movies.addAll(arrayList);

        // reset the data set for the adapter
        mAdapter.setData(movies);
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
                loadMovies(TMDBInfo.POPULAR);
                return true;

            case R.id.sortby_top_rated :
                loadMovies(TMDBInfo.TOP_RATED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovies( int sortByParam ) {
        TMDBHelper.loadMovieData(getApplicationContext(), this, sortByParam );
        resetGridViewPosition();
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

    // Do not touch this as it's called from the XML layout
    public void onErrorReload( View view ) {
        loadMovies(TMDBInfo.POPULAR);
    }
}
