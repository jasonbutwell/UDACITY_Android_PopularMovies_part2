package com.jasonbutwell.popularmovies.Api;

import android.content.Context;
import android.widget.ImageView;

import com.jasonbutwell.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by J on 25/02/2017.
 */

// Picasso helper class to load an image URL into an ImageView

public class PicassoHelper {

    public static void loadImage(Context context, String imageURL, ImageView destination) {

        // The image for the loading placeholder
        int placeHolderResource = R.drawable.clapboard;

        if (imageURL != null) {
            Picasso.with(context)
                    .load(imageURL)
                    .placeholder(placeHolderResource)
                    .fit()
                    .into(destination); // set the image thumbnail for the movie
        }
    }
}
