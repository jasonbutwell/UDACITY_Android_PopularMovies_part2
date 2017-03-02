package com.jasonbutwell.popularmovies.AdapterViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.Model.ReviewItem;
import com.jasonbutwell.popularmovies.R;

import java.util.Locale;

/**
 * Created by J on 28/02/2017.
 */

public class ReviewViewHolder  extends RecyclerView.ViewHolder {

// The custom view holder outer class, used for assigning data to views

    private TextView reviewID, author, review;

    private ListItemClickListener mOnClickListener;         // store location for list click listener

    public ReviewViewHolder(View itemView) {
        super(itemView);
        reviewID = (TextView)itemView.findViewById(R.id.reviewID);
        author = (TextView) itemView.findViewById(R.id.reviewAuthor);
        review = (TextView) itemView.findViewById(R.id.reviewContent);
    }

    public void bind(ReviewItem reviewItem) {
        String reviewId = String.format(Locale.ENGLISH,"#%d",getAdapterPosition()+1);
        reviewID.setText(reviewId);
        author.setText(reviewItem.getAuthor());
        review.setText(reviewItem.getReview());
    }
}
