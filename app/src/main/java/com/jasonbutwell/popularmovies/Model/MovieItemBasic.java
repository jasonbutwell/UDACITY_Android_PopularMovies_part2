package com.jasonbutwell.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J on 26/02/2017.
 */

public class MovieItemBasic implements Parcelable {
    protected String id;
    protected String originalTitle;
    protected String posterURL;

    public MovieItemBasic() {}

    public MovieItemBasic(String id, String originalTitle, String posterURL) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
    }

    protected MovieItemBasic(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterURL = in.readString();
    }

    public static final Creator<MovieItemBasic> CREATOR = new Creator<MovieItemBasic>() {
        @Override
        public MovieItemBasic createFromParcel(Parcel in) {
            return new MovieItemBasic(in);
        }

        @Override
        public MovieItemBasic[] newArray(int size) {
            return new MovieItemBasic[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterURL);
    }
}
