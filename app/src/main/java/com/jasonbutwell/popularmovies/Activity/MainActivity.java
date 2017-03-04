package com.jasonbutwell.popularmovies.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jasonbutwell.popularmovies.Adapter.FavMovieCursorAdapter;
import com.jasonbutwell.popularmovies.Adapter.MovieRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Api.TMDBHelper;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBMovieCursorLoader;
import com.jasonbutwell.popularmovies.Listener.CursorLoadCompleteListener;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, MovieTaskCompleteListener, CursorLoadCompleteListener, SharedPreferences.OnSharedPreferenceChangeListener {

    // IMPORTANT! - Replace API KEY in 'Api / APIKey.java' with your own 'TMDB API KEY'

    // Enable binding so we can access UI view components easier
    private MoviePosterLayoutBinding binding;
    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FavMovieCursorAdapter mFavAdapter;

    // This is where we will store our movies
    private ArrayList<MovieItemBasic> movies = new ArrayList<>();

    // default for sort Filter
    private String sortFilter = TMDBInfo.MOVIE_FILTER_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.movie_poster_layout);

        layoutManager = new GridLayoutManager(this, TMDBInfo.NO_OF_POSTERS_PER_ROW);

        binding.moviePosterView.setLayoutManager(layoutManager);
        binding.moviePosterView.setHasFixedSize(true);

        mAdapter = new MovieRecyclerViewAdapter(this, movies, this);
        binding.moviePosterView.setAdapter(mAdapter);

        // Cursor Adapter for favourite movies
        mFavAdapter = new FavMovieCursorAdapter(this, this);

        // Obtain the SharedPreferences and register the listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        setFilter();

        // Load popular as default initially - (To be replaced by sharedPreferences)
        loadMovies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    // Create our options menu so we can filter movies by (1.Popular, 2.Top rated)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // If action was the settings
        if ( id == R.id.action_settings ) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Loads movies by sort order
    private void loadMovies() {

        movies.clear();
        mAdapter.setData(movies);       // clear what's in our movies and reset the adapter

        if ( !sortFilter.equals(TMDBInfo.MOVIE_FILTER_FAVOURITES))
            TMDBHelper.loadMovieData(getApplicationContext(), this, sortFilter, binding, getSupportLoaderManager() );
        else {
            new TMDBMovieCursorLoader(getApplicationContext(),getSupportLoaderManager(),binding, this);
        }
        // Reset position of GridView
        //binding.moviePosterView.getLayoutManager().smoothScrollToPosition(binding.moviePosterView,null,0);
    }

    // When a movie poster in the RecyclerView is clicked on
    @Override
    public void onListItemClick(int clickedItemIndex) {
        MovieDetail.launchIntent( getApplicationContext(), movies.get(clickedItemIndex) );
    }

    // Callback for when the asyncTask completes
    @Override
    public void onTaskComplete(ArrayList<MovieItemBasic> moviesData) {
        //movies.clear();                 // update the movie list arraylist and then the adapter
        movies.addAll(moviesData);
        mAdapter.setData(movies);       // reset the data set for the adapter
        binding.moviePosterView.setAdapter(mAdapter);
    }

    // When the cursor comes back
    @Override
    public void onTaskComplete(Cursor cursor) {
        mFavAdapter.swapCursor(cursor);
        binding.moviePosterView.setAdapter(mFavAdapter);
    }

    // Do not touch this as it's called from the XML layout
    public void onErrorReload( View view ) {
        //loadMovies(TMDBInfo.POPULAR);
    }

    // gets a preference value based on its key
//    public String getPreferenceValue( SharedPreferences sharedPreferences, String key, int defaultValue ) {
//        return sharedPreferences.getString(key,getResources().getString(defaultValue));
//    }

    public void setFilter() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortFilter = sharedPreferences.getString(getString(R.string.pref_sortby_key),
                getResources().getString(R.string.pref_sortby_option_value_popular));
    }

    // This callback method is called when the Preference is changed so we can act on it
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setFilter();
        loadMovies();
    }
}
