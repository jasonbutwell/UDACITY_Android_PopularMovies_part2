package com.jasonbutwell.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by J on 23/01/2017.
 */

// Will be used to store specific information on each movie

public class MovieItem extends MovieItemBasic implements Parcelable {

    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String runTime;

    private ArrayList<TrailerItem> trailers;
    private ArrayList<ReviewItem> reviews;

    private static final String ratingOutOfTen = " / 10";

    public MovieItem() {}

    public MovieItem(String id, String originalTitle, String posterURL, String plotSynopsis, String userRating, String releaseDate, String runTime) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.runTime = runTime;
    }

    protected MovieItem(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterURL = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        runTime = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterURL);
        parcel.writeString(plotSynopsis);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(runTime);
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
