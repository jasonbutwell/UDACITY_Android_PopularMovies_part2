package com.jasonbutwell.popularmovies.Model;

/**
 * Created by J on 23/01/2017.
 */

// Will be used to store specific information on each movie

public class MovieItem extends MovieItemBasic {

    private String plotSynopsis;
    private String userRating;
    private String releaseDate;

    private static final String ratingOutOfTen = " / 10";

    public MovieItem() {}

    public MovieItem(String id, String originalTitle, String plotSynopsis, String userRating, String releaseDate, String posterURL) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getUserRatingOutOfTen() {
        return userRating.concat(ratingOutOfTen);
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
