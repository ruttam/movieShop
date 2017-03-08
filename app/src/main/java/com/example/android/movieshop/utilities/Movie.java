package com.example.android.movieshop.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String thumbnailURL;
    private String title;
    private String overview;
    private String voteAverage;
    private String releaseDate;

    public Movie(String url, String t, String o, String v, String r) {
        thumbnailURL = url;
        title = t;
        overview = o;
        voteAverage = v;
        releaseDate = r;
    }

    private Movie(Parcel in) {
        thumbnailURL = in.readString();
        title = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbnailURL);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }
}
