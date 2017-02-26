package com.jasonbutwell.popularmovies.Model;

/**
 * Created by J on 26/02/2017.
 */

public class TrailerItem {
    private String id;
    private String description;
    private String youtubeURL;

    public TrailerItem() {}

    public TrailerItem(String id, String description, String youtubeURL) {
        this.id = id;
        this.description = description;
        this.youtubeURL = youtubeURL;
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
}
