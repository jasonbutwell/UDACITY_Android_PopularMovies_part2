package com.jasonbutwell.popularmovies.Model;

/**
 * Created by J on 23/01/2017.
 */

// Will be used to store specific information on each movie

public class MovieItem {
    private String id;
    private String originalTitle;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String posterURL;

    public MovieItem() {
    }

    public MovieItem(String id, String originalTitle, String plotSynopsis, String userRating, String releaseDate, String posterURL) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
    }

    public String getId() {
        return id;
    }

    public int getIntId() {
        return Integer.parseInt(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
}
