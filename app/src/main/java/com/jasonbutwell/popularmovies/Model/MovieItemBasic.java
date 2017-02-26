package com.jasonbutwell.popularmovies.Model;

/**
 * Created by J on 26/02/2017.
 */

public class MovieItemBasic {
    protected String id;
    protected String originalTitle;
    protected String posterURL;

    public MovieItemBasic() {}

    public MovieItemBasic(String id, String originalTitle, String posterURL) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public int getIntId() {
        return Integer.parseInt(id);
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public String getPosterURL() {
        return posterURL;
    }
    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
}
