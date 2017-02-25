package com.jasonbutwell.popularmovies.Api;

import android.content.Context;
import android.widget.ImageView;

import com.jasonbutwell.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by J on 25/02/2017.
 */

public class PicassoHelper {

    private static int placeHolderResource = R.drawable.clapboard;

    public static void loadImage(Context context, String imageURL, ImageView destination) {
        if (imageURL != null) {
            Picasso.with(context)
                    .load(imageURL)
                    .placeholder(placeHolderResource)
                    .fit()
                    .into(destination); // set the image thumbnail for the movie
        }
    }
}
