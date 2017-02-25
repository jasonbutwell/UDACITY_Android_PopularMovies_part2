package com.jasonbutwell.popularmovies.BackgroundTask;

import android.os.AsyncTask;

import com.jasonbutwell.popularmovies.Listener.MovieDetailTaskCompleteListener;
import com.jasonbutwell.popularmovies.Network.NetworkUtils;
import com.jasonbutwell.popularmovies.Utils.JSONUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by J on 25/02/2017.
 */

// We use this to grab the runtime from the movie info using just the id
// We could build upon this to grab additional information in future

public class TMDBQueryDetailsTask extends AsyncTask<URL, Void, String> {

    private URL UrlToSearch = null;
    private String searchResults = null;
    private String data = null;

    private MovieDetailTaskCompleteListener completed_listener;

    public TMDBQueryDetailsTask(MovieDetailTaskCompleteListener listener) {
        completed_listener = listener;
    }

    @Override
    protected String doInBackground(URL... urls) {

        URL searchURL = null;
        searchURL = urls[0];

        try {
            searchResults = NetworkUtils.getResponseFromHttpUrl( searchURL );

        } catch (IOException e) {
            e.printStackTrace();
        }

        data = JSONUtils.extractJSONString( searchResults, "runtime" );

        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        completed_listener.onTaskComplete(data);
    }
}
