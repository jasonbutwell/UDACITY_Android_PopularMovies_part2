package com.jasonbutwell.popularmovies.Ui;

import android.view.View;

import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

/**
 * Created by J on 25/02/2017.
 */

public class LoadingIndicator {

    public static MoviePosterLayoutBinding layoutBinding = null;

    public static void setBinding( MoviePosterLayoutBinding binding ) {
        layoutBinding = binding;
    }

    public static void show( boolean show ) {
        if ( layoutBinding != null )
            layoutBinding.loadingLayout.loadingIndicator.setVisibility( (show ? View.VISIBLE : View.INVISIBLE) );
    }

    // Set the load error message and show it or hide it
    public static void showError( boolean show, String errorMessage ) {

        if (show) {
            if (layoutBinding != null) {
                layoutBinding.loadingLayout.errorMessage.setVisibility(View.VISIBLE);

                if (errorMessage != null && !errorMessage.equals(""))
                    layoutBinding.loadingLayout.errorTextView.setText(errorMessage);
                else
                    layoutBinding.loadingLayout.errorTextView.setText("");
            }
        } else
            layoutBinding.loadingLayout.errorMessage.setVisibility(View.INVISIBLE);
    }
}
