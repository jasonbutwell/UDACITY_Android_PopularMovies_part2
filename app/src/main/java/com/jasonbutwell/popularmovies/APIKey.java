package com.jasonbutwell.popularmovies;

/**
 * Created by J on 21/01/2017.
 */

public final class APIKey {

    private final static String API_KEY = "PLEASE_ENTER_YOUR_OWN_API_KEY_HERE";

    private APIKey() {}

    public static final String get() {
        return API_KEY;
    }
}
