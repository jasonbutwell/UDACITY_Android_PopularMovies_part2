package com.jasonbutwell.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J on 26/02/2017.
 */

public class TrailerItem implements Parcelable {
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

    protected TrailerItem(Parcel in) {
        id = in.readString();
        description = in.readString();
        youtubeURL = in.readString();
        youtubeThumbnailURL = in.readString();
    }

    public static final Creator<TrailerItem> CREATOR = new Creator<TrailerItem>() {
        @Override
        public TrailerItem createFromParcel(Parcel in) {
            return new TrailerItem(in);
        }

        @Override
        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeString(youtubeURL);
        parcel.writeString(youtubeThumbnailURL);
    }
}
