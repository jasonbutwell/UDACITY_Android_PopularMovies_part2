package com.jasonbutwell.popularmovies.Model;

import java.util.ArrayList;

/**
 * Created by J on 23/01/2017.
 */

// Will be used to store specific information on each movie

public class MovieItem extends MovieItemBasic {

    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String runTime;

    private ArrayList<TrailerItem> trailers;
    private ArrayList<ReviewItem> reviews;

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

    public int getTrailersSize() {
        return trailers.size();
    }

    public int getReviewsSize() {
        return reviews.size();
    }

    public void setTrailers(ArrayList<TrailerItem> trailers) {
        this.trailers = trailers;
    }

    public void setReviews( ArrayList<ReviewItem> reviews) {
        this.reviews = reviews;
    }

    public ReviewItem getReviewAt( int index ) {
        return (reviews.size()>index) ? reviews.get(index) : null;
    }

    public TrailerItem getTrailerAt( int index ) {
        return (trailers.size()>index) ? trailers.get(index) : null;
    }

    public ArrayList<TrailerItem> getTrailers() {
        return trailers;
    }

    public ArrayList<ReviewItem> getReviews() {
        return reviews;
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

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }
}
