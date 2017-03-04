package com.jasonbutwell.popularmovies.Activity;

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
import android.widget.Toast;

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
import com.jasonbutwell.popularmovies.Ui.MovieDetail;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our <layout> enclosed layout with the data binding
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        // Set Layout Manager for Movie Trailers
        movieDetailsBinding.movieTrailerView.setLayoutManager(new GridLayoutManager(this, TMDBInfo.NO_OF_TRAILERS_PER_ROW));
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

        // Get the calling intent passed over
        mMovie = MovieDetail.generateFromIntent( getIntent() );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
        else {
            // Call a new task to obtain the run duration from the movies JSON using it's id and poster url
            new TMDBDetailsLoader(getApplicationContext(),getSupportLoaderManager(),movieDetailsBinding,mMovie.getId(), mMovie.getPosterURL(), this);
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
