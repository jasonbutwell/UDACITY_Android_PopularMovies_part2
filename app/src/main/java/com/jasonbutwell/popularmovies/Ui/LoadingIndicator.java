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
            layoutBinding.loadingIndicator.setVisibility( (show ? View.VISIBLE : View.INVISIBLE) );
    }

    // Set the load error message and show it or hide it
    public static void showError( boolean show, String errorMessage ) {

        if (show) {
            if (layoutBinding != null) {
                layoutBinding.errorMessage.setVisibility(View.VISIBLE);

                if (errorMessage != null && !errorMessage.equals(""))
                    layoutBinding.errorTextView.setText(errorMessage);
                else
                    layoutBinding.errorTextView.setText("");
            } else
                layoutBinding.errorMessage.setVisibility(View.INVISIBLE);
        }
    }
}
