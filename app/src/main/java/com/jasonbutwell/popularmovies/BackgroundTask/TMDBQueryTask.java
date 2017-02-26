package com.jasonbutwell.popularmovies.BackgroundTask;

import android.os.AsyncTask;

import com.jasonbutwell.popularmovies.Listener.MovieTaskCompleteListener;
import com.jasonbutwell.popularmovies.Model.MovieItem;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Ui.LoadingIndicator;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by J on 25/02/2017.
 */

// We use this to grab the JSON for the movies we want to see
// We then break up the results and store them in an arraylist of custom type MovieItem

public class TMDBQueryTask extends AsyncTask< URL, Void, ArrayList<MovieItem> > {

    private String searchResults = null;
    private MovieTaskCompleteListener listener;
    Object binding;

    public TMDBQueryTask(MovieTaskCompleteListener listener, Object binding) {
        this.listener = listener;
        this.binding = binding;
    }

    @Override
    protected void onPreExecute() {
        LoadingIndicator.show( binding, true );  // Loading Indicator visible
    }

    @Override
    protected ArrayList<MovieItem> doInBackground( URL... urls ) {

        ArrayList<MovieItem> arrayList = null;
        URL searchURL = null;
        searchURL = urls[0];

        try {
            searchResults = NetworkUtils.getResponseFromHttpUrl( searchURL );

        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayList = JSONUtils.extractJSONArray( searchResults );

        return arrayList;
    }

    @Override
    protected void onPostExecute( ArrayList<MovieItem> arrayList ) {
        listener.onTaskComplete(arrayList);

        LoadingIndicator.show( binding, false ); // Loading indicator invisible
    }
}