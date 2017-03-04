package com.jasonbutwell.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J on 26/02/2017.
 */

public class ReviewItem implements Parcelable {
    private String id;
    private String author;
    private String review;
    private String url;

    public ReviewItem() {}

    public ReviewItem(String id, String author, String review, String url) {
        this.id = id;
        this.author = author;
        this.review = review;
        this.url = url;
    }

    protected ReviewItem(Parcel in) {
        id = in.readString();
        author = in.readString();
        review = in.readString();
        url = in.readString();
    }

    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(review);
        parcel.writeString(url);
    }
}
