package com.jasonbutwell.popularmovies.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jasonbutwell.popularmovies.AdapterViewHolder.FavMovieViewHolder;
import com.jasonbutwell.popularmovies.Database.MovieContract;
import com.jasonbutwell.popularmovies.Listener.ListItemClickListener;
import com.jasonbutwell.popularmovies.R;

import static com.jasonbutwell.popularmovies.Api.TMDBInfo.FIELD_SEPERATOR;

/**
 * Created by J on 03/03/2017.
 */

public class FavMovieCursorAdapter extends RecyclerView.Adapter<FavMovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private ListItemClickListener mOnClickListener;

    public FavMovieCursorAdapter( Context context, ListItemClickListener listener ) {
        mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public FavMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavMovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listview_item_image, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(FavMovieViewHolder holder, int position) {

        // Obtain indexes for the fields to extract from the Cursor
        int movieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int posterIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL);
        int TitleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);

        mCursor.moveToPosition(position);                   // get the location based on the view position

        final int id = mCursor.getInt(movieIdIndex);        // extract movie id
        String posterURL = mCursor.getString(posterIndex);  // extract poster url string
        String movieTitle = mCursor.getString(TitleIndex);  // extract poster url string


        holder.itemView.setTag(id + FIELD_SEPERATOR + movieTitle + FIELD_SEPERATOR + posterURL);    // set the itemView tag to be the movie id
        holder.bind(mContext, posterURL);                                                           // Call ViewHolder to bind the posterURL
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;    // returns 0 if cursor is null or the count if not
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor)                      // Check to see if the same cursor
            return null;                            // Nothing has changed

        Cursor tempCursor = mCursor;
        mCursor = cursor;                           // new cursor value assigned

        if (cursor != null)
            notifyDataSetChanged();                 // check Cursor valid for update

        return tempCursor;                          // return the temp cursor
    }
}
