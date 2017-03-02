package com.jasonbutwell.popularmovies.Model;

/**
 * Created by J on 26/02/2017.
 */

public class TrailerItem {
    private String id;
    private String description;
    private String youtubeURL;
    private String youtubeThumbnailURL;

    public TrailerItem() {}

    public TrailerItem(String description, String youtubeURL, String youtubeThumbnailURL) {
//        this.id = id;
        this.description = description;
        this.youtubeURL = youtubeURL;
        this.youtubeThumbnailURL = youtubeThumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    public String getYoutubeThumbnailURL() {
        return youtubeThumbnailURL;
    }

    public void setYoutubeThumbnailURL(String youtubeThumbnailURL) {
        this.youtubeThumbnailURL = youtubeThumbnailURL;
    }
}
