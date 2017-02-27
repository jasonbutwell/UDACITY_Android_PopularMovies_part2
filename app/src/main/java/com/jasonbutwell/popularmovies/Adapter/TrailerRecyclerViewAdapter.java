package com.jasonbutwell.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.TrailerItem;
import com.jasonbutwell.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by J on 26/02/2017.
 */

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerViewHolder>{

    // Class variable to store the data to show
    private ArrayList<TrailerItem> trailers;
    private Context context;

    // Somewhere to store the click list item listener
    private ListItemClickListener mOnClickListener;

    // To refresh the data set once it's been changed
    public void setData(ArrayList<TrailerItem> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    // Recycler constructor, stores the data and the list item click listener to use
    public TrailerRecyclerViewAdapter(Context context, ArrayList<TrailerItem> dataItems, ListItemClickListener listener) {
        this.context = context;
        this.trailers = dataItems;
        mOnClickListener = listener;
    }

    // This is our view holder that inflates our list item layout for us
    // Note we can tweak this to show different layouts for different types of items if we want
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        boolean shouldAttachToParentImmediately = false;

        // Inflate a view from our custom layout list_item.xml
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailers_list_item, viewGroup, shouldAttachToParentImmediately);

        // return a new CustomViewHolder for that View
        return new TrailerViewHolder(view, mOnClickListener);
    }

    // Calls our view holder with the position of the item to show
    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(context, trailers.get(position));
    }

    // Returns back the number of items to show
    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
