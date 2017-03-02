package com.jasonbutwell.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jasonbutwell.popularmovies.AdapterViewHolder.MoviePosterViewHolder;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.MovieItemBasic;
import com.jasonbutwell.popularmovies.R;

import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MoviePosterViewHolder> {

    // Class variable to store the data to show
    private ArrayList<MovieItemBasic> movies;
    private Context context;

    // Somewhere to store the click list item listener
    private ListItemClickListener mOnClickListener;

    // To refresh the data set once it's been changed
    public void setData(ArrayList<MovieItemBasic> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    // Recycler constructor, stores the data and the list item click listener to use
    public MovieRecyclerViewAdapter(Context context, ArrayList<MovieItemBasic> dataItems, ListItemClickListener listener) {
        this.context = context;
        this.movies = dataItems;
        mOnClickListener = listener;
    }

    // This is our view holder that inflates our list item layout for us
    // Note we can tweak this to show different layouts for different types of items if we want
    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflate a view from our custom layout list_item.xml
        // return a new CustomViewHolder for that View
        return new MoviePosterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_item_image, viewGroup, false), mOnClickListener);
    }

    // Calls our view holder with the position of the item to show
    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        holder.bind(context, movies, position);
    }

    // Returns back the number of items to show
    @Override
    public int getItemCount() {
        return movies.size();
    }
}