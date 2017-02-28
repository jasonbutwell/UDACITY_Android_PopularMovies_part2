package com.jasonbutwell.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasonbutwell.popularmovies.AdapterViewHolder.ReviewViewHolder;
import com.jasonbutwell.popularmovies.Model.ReviewItem;
import com.jasonbutwell.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by J on 28/02/2017.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

    // Class variable to store the data to show
    private ArrayList<ReviewItem> mReviews;
    private Context context;

    // To refresh the data set once it's been changed
    public void setData(ArrayList<ReviewItem> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    // Recycler constructor, stores the data and the list item click listener to use
    public ReviewRecyclerViewAdapter(Context context, ArrayList<ReviewItem> dataItems) {
        this.context = context;
        mReviews = dataItems;
    }

    // This is our view holder that inflates our list item layout for us
    // Note we can tweak this to show different layouts for different types of items if we want
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        boolean shouldAttachToParentImmediately = false;

        // Inflate a view from our custom layout list_item.xml
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviews_list_item, viewGroup, shouldAttachToParentImmediately);

        // return a new CustomViewHolder for that View
        return new ReviewViewHolder(view);
    }

    // Calls our view holder with the position of the item to show
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    // Returns back the number of items to show
    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
