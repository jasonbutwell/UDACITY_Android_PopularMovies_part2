package com.jasonbutwell.popularmovies.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.jasonbutwell.popularmovies.Utils.SysUtil;
import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

import java.util.ArrayList;

import static com.jasonbutwell.popularmovies.Api.TMDBInfo.FIELD_SEPERATOR;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.POSTERS_PER_ROW_LANDSCAPE;
import static com.jasonbutwell.popularmovies.Api.TMDBInfo.POSTERS_PER_ROW_PORTRAIT;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, MovieTaskCompleteListener, CursorLoadCompleteListener, SharedPreferences.OnSharedPreferenceChangeListener {

    // IMPORTANT! - Replace API KEY in 'Api / APIKey.java' with your own 'TMDB API KEY'

    private String LIST_STATE_KEY = "saved_layout_manager";
    private String LIST_STATE_MOVIES = "movies_data";

    private Parcelable mListState;

    // Enable binding so we can access UI view components easier
    private MoviePosterLayoutBinding binding;
    private MovieRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public FavMovieCursorAdapter mFavAdapter;

    // This is where we will store our movies
    private ArrayList<MovieItemBasic> movies = new ArrayList<>();

    // default for sort Filter
    private String sortFilter = TMDBInfo.MOVIE_FILTER_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            movies = savedInstanceState.getParcelableArrayList(LIST_STATE_MOVIES);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.movie_poster_layout);

        if(SysUtil.isPortrait(this))
            layoutManager = new GridLayoutManager(this, POSTERS_PER_ROW_PORTRAIT);
        else
            layoutManager = new GridLayoutManager(this, POSTERS_PER_ROW_LANDSCAPE);

        //layoutManager = new GridLayoutManager(this, TMDBInfo.NO_OF_POSTERS_PER_ROW);

        binding.moviePosterView.setLayoutManager(layoutManager);
        binding.moviePosterView.setHasFixedSize(true);

        mAdapter = new MovieRecyclerViewAdapter(this, movies, this);
        binding.moviePosterView.setAdapter(mAdapter);

        // Cursor Adapter for favourite movies
        mFavAdapter = new FavMovieCursorAdapter(this, this);

        setUpPreferences();

        binding.loadingLayout.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovies();
            }
        });

        if ( savedInstanceState == null)
            loadMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mListState = layoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
        state.putParcelableArrayList(LIST_STATE_MOVIES, movies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if(state != null) {
            mListState = state.getParcelable(LIST_STATE_KEY);
            movies = state.getParcelableArrayList(LIST_STATE_MOVIES);
        }
    }

    private void setUpPreferences() {
        // Obtain the SharedPreferences and register the listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // set the filter preference from the prefs
        setFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload movies again if we navigate back and favourites is set
        if ( sortFilter.equals(TMDBInfo.MOVIE_FILTER_FAVOURITES))
          loadMovies();

        if (mListState != null)
          layoutManager.onRestoreInstanceState(mListState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister shared preference change listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // Create our options menu so we can filter movies by (1.Popular, 2.Top rated)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    // Allows for settings activity to be called from menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If action was the settings
        if ( item.getItemId() == R.id.action_settings ) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    // Loads movies by sort order
    private void loadMovies() {
        movies.clear();
        mAdapter.setData(movies);       // clear what's in our movies and reset the adapter

        if ( !sortFilter.equals(TMDBInfo.MOVIE_FILTER_FAVOURITES))
            TMDBHelper.loadMovieData(getApplicationContext(), this, sortFilter, binding, getSupportLoaderManager() );
        else
            new TMDBMovieCursorLoader(getApplicationContext(), getSupportLoaderManager(), binding, this, -1);
        // Reset position of GridView
        //binding.moviePosterView.getLayoutManager().smoothScrollToPosition(binding.moviePosterView,null,0);
    }

    // When a movie poster in the RecyclerView is clicked on
    @Override
    public void onListItemClick(int clickedItemIndex, View v) {
        // Are we getting our data from favourites or one of the other two categories?
        if (sortFilter.equals(TMDBInfo.MOVIE_FILTER_FAVOURITES)) {
            String movieFields[] = v.getTag().toString().split(FIELD_SEPERATOR);
            MovieDetail.launchIntent( getApplicationContext(), new MovieItemBasic(movieFields[0],movieFields[1],movieFields[2]) );
        }
        else
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
        loadMovies();
    }

    // gets a preference value based on its key
    public String getPreferenceValue( SharedPreferences sharedPreferences, String key, int defaultValue ) {
        return sharedPreferences.getString(key,getResources().getString(defaultValue));
    }

    // Set up the sort filter based on preference
    public void setFilter() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortFilter = getPreferenceValue(sharedPreferences, getString(R.string.pref_sortby_key), R.string.pref_sortby_option_value_popular);
    }

    // This callback method is called when the Preference is changed so we can act on it
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // change the filter to reflect the preference and reload the movies
        setFilter();
        loadMovies();
    }
}
