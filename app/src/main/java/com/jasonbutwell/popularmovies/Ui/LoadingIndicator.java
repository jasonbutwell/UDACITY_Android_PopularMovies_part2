package com.jasonbutwell.popularmovies.Ui;

import android.view.View;

import com.jasonbutwell.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.jasonbutwell.popularmovies.databinding.MoviePosterLayoutBinding;

/**
 * Created by J on 25/02/2017.
 */

public class LoadingIndicator {

    // Show loading Indicator on/off

    public static void show( Object layoutBinding, boolean show ) {
        if ( layoutBinding != null ) {
            if (layoutBinding instanceof MoviePosterLayoutBinding)
                showLoadIndicator((MoviePosterLayoutBinding) layoutBinding, show);
            else
                if (layoutBinding instanceof ActivityMovieDetailsBinding)
                    showLoadIndicator((ActivityMovieDetailsBinding) layoutBinding, show);
        }
    }

    // Show error on / off with message

    public static void showError( Object layoutBinding, boolean show, String errorMessage ) {
        if (layoutBinding != null) {
            if (show) {
                if (layoutBinding instanceof MoviePosterLayoutBinding)
                    handleError((MoviePosterLayoutBinding) layoutBinding, show, errorMessage);
                else if (layoutBinding instanceof ActivityMovieDetailsBinding)
                    handleError((ActivityMovieDetailsBinding) layoutBinding, show, errorMessage);
            } else {
                if (layoutBinding instanceof MoviePosterLayoutBinding)
                    showErrorIndicator((MoviePosterLayoutBinding) layoutBinding, show, errorMessage);
                else if (layoutBinding instanceof ActivityMovieDetailsBinding)
                    showErrorIndicator((ActivityMovieDetailsBinding) layoutBinding, show, errorMessage);
            }
        }
    }

    // Handle error cases for different types of bindings

    public static void handleError(MoviePosterLayoutBinding layoutBinding, boolean show, String errorMessage) {
        layoutBinding.loadingLayout.errorMessage.setVisibility(View.VISIBLE);

        if (errorMessage != null && !errorMessage.equals(""))
            layoutBinding.loadingLayout.errorTextView.setText(errorMessage);
        else
            layoutBinding.loadingLayout.errorTextView.setText("");
    }

    public static void handleError(ActivityMovieDetailsBinding layoutBinding, boolean show, String errorMessage) {
        layoutBinding.loadingLayout.errorMessage.setVisibility(View.VISIBLE);

        if (errorMessage != null && !errorMessage.equals(""))
            layoutBinding.loadingLayout.errorTextView.setText(errorMessage);
        else
            layoutBinding.loadingLayout.errorTextView.setText("");
    }

    // Loading indicator cases for different bindings

    public static void showLoadIndicator( MoviePosterLayoutBinding layout, boolean show ) {
            layout.loadingLayout.loadingIndicator.setVisibility( (show ? View.VISIBLE : View.INVISIBLE) );
    }

    public static void showLoadIndicator( ActivityMovieDetailsBinding layout, boolean show ) {
            layout.loadingLayout.loadingIndicator.setVisibility( (show ? View.VISIBLE : View.INVISIBLE) );
    }

    // Signatures for showError for different bindings

    public static void showErrorIndicator( MoviePosterLayoutBinding layout, boolean show, String errorMessage ) {
            layout.loadingLayout.errorMessage.setVisibility(View.INVISIBLE);
    }

    public static void showErrorIndicator( ActivityMovieDetailsBinding layout, boolean show, String errorMessage ) {
            layout.loadingLayout.errorMessage.setVisibility(View.INVISIBLE);
    }
}
