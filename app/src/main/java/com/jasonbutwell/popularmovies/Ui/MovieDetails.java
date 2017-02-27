package com.jasonbutwell.popularmovies.Ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.jasonbutwell.popularmovies.Adapter.TrailerRecyclerViewAdapter;
import com.jasonbutwell.popularmovies.BackgroundTask.TMDBDetailsLoader;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.R;
import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity implements ListItemClickListener,MovieDetailTaskCompleteListener {

    // Use data binding to reduce boilerplate code
    public ActivityMovieDetailsBinding movieDetailsBinding;
    private TrailerRecyclerViewAdapter mAdapter;

    private ArrayList<TrailerItem> trailers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our <layout> enclosed layout with the data binding
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        movieDetailsBinding.movieTrailerView.setLayoutManager(new GridLayoutManager(this, 4));
        movieDetailsBinding.movieTrailerView.setHasFixedSize(true);

        mAdapter = new TrailerRecyclerViewAdapter(this, trailers, this);
        movieDetailsBinding.movieTrailerView.setAdapter(mAdapter);

        // Get the calling itent passed over
        Intent movieDetailsIntent = getIntent();

        MovieItemBasic mMovie = MovieDetail.generateFromIntent(movieDetailsIntent);

        // Update the UI with the details obtained
        //MovieDetail.setUI( getApplicationContext(), MovieDetail.generateFromIntent(movieDetailsIntent), movieDetailsBinding );

        if ( !NetworkUtils.isNetworkAvailable(getApplicationContext()))
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
        else {
            // Call a new task to obtain the run duration from the movies JSON using it's id and poster url
            new TMDBDetailsLoader(getApplicationContext(),getSupportLoaderManager(),movieDetailsBinding,mMovie.getId(), mMovie.getPosterURL(), this);
        }
    }

    // What happens when a trailer is clicked

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // TODO: Launch youtube intent here
        Toast.makeText(getApplicationContext(), "You clicked trailer #"+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskComplete(MovieItem movie) {
        // Testing
        trailers.clear();
        trailers.addAll(movie.getTrailers());
        mAdapter.setData(trailers);       // reset the data set for the adapter
    }
}
