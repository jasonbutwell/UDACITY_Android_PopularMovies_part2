package com.jasonbutwell.popularmovies.Utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by J on 05/03/2017.
 */

public class SysUtil {
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
