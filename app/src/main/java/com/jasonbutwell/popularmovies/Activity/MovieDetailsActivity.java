package com.jasonbutwell.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jasonbutwell.popularmovies.Adapter.ReviewRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Adapter.TrailerRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.Api.TMDBInfo;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBDetailsLoader;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBMovieCursorLoader;
import com.jasonbutwell.popularmovies.Listener.CursorLoadCompleteListener;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Model.ReviewItem;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
import com.jasonbutwell.popularmovies.Utils.SysUtil;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements ListItemClickListener, MovieDetailTaskCompleteListener, CursorLoadCompleteListener {

    // Use data binding to reduce boilerplate code
    public ActivityMovieDetailsBinding movieDetailsBinding;

    private TrailerRecyclerViewAdapter mAdapter;
    private ReviewRecyclerViewAdapter mReviewsAdapter;

    private ArrayList<TrailerItem> trailers = new ArrayList<>();
    private ArrayList<ReviewItem> reviews = new ArrayList<>();

    private MovieItemBasic mMovie = new MovieItemBasic();

    private int trailers_per_row = TMDBInfo.TRAILERS_PER_ROW_PORTRAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our <layout> enclosed layout with the data binding
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        // Get the calling intent passed over
        mMovie = MovieDetail.generateFromIntent(getIntent());

        // Set Layout Manager for Movie Trailers

        if ( !SysUtil.isPortrait(this) )
            trailers_per_row = TMDBInfo.TRAILERS_PER_ROW_LANDSCAPE;

        movieDetailsBinding.movieTrailerView.setLayoutManager(new GridLayoutManager(this, trailers_per_row));
        movieDetailsBinding.movieTrailerView.setHasFixedSize(true);

        // Set Layout Manager for movie Reviews
        movieDetailsBinding.movieReviewView.setLayoutManager(new LinearLayoutManager(this));
        movieDetailsBinding.movieReviewView.setHasFixedSize(true);

        // Set adapter for Trailers
        mAdapter = new TrailerRecyclerViewAdapter(this, trailers, this);
        movieDetailsBinding.movieTrailerView.setAdapter(mAdapter);

        // Set adapter for Reviews
        mReviewsAdapter = new ReviewRecyclerViewAdapter(reviews);
        movieDetailsBinding.movieReviewView.setAdapter(mReviewsAdapter);

        movieDetailsBinding.loadingLayout.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieDetails();
            }
        });

        loadMovieDetails();
    }

    public void loadMovieDetails() {

        Context mContext = getApplicationContext();
        String errorMessage = mContext.getString(R.string.network_error_message);

        // Check if we have a network connection
        if ( !NetworkUtils.isNetworkAvailable(mContext)) {
            LoadingIndicator.showError(movieDetailsBinding, true, errorMessage );           // if no network connection,
        }                                                                       // show the error message and retry button
        else {
            LoadingIndicator.showError(movieDetailsBinding, false, "");                     // clear and hide the error message
            new TMDBDetailsLoader(getApplicationContext(), getSupportLoaderManager(), movieDetailsBinding, mMovie.getId(), mMovie.getPosterURL(), this);
        }
    }

    // Open YouTube Trailer via Intent
    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        MovieDetail.launchYouTubeIntent(this, trailers.get(clickedItemIndex) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourites_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                checkFavouriteExists();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Callback method for when our AsyncTaskLoader completes
    @Override
    public void onTaskComplete(MovieItem movie) {
        // Set the data for the trailers adapter
        trailers.clear();
        trailers.addAll(movie.getTrailers());
        mAdapter.setData(movie.getTrailers());              // reset the data set for the adapter

        // Set the data for the reviews adapter
        reviews.clear();
        reviews.addAll(movie.getReviews());
        mReviewsAdapter.setData(movie.getReviews());
    }

    // Check the record exists already
    public void checkFavouriteExists() {
        new TMDBMovieCursorLoader(getApplicationContext(),getSupportLoaderManager(),movieDetailsBinding, this, mMovie.getIntId());
    }

    // Used to check whether we are inserting the record or deleting an existing one
    @Override
    public void onTaskComplete(Cursor cursor) {
        MovieDetail.insertOrDeleteFavourite( getApplicationContext(), mMovie, cursor.getCount() );
    }
}
