package com.example.szage.mytopmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Movie containing all the needed parameters and getter methods.
 */

public class Movie implements Parcelable{


    // Title of the movie
    @SerializedName("original_title")
    private String mTitle;

    // Release date of the movie
    @SerializedName("release_date")
    private String mRelease;

    // Path of the movie porter image of the movie
    @SerializedName("poster_path")
    private String mPosterPath;

    // Rating of the movie
    @SerializedName("vote_average")
    private double mRating;

    // Overview of the movie
    @SerializedName("overview")
    private String mOverview;

    @SerializedName("id")
    private String mMovieId;

    public Movie(String title, String release, String posterPath, double rating, String overview, String movieId) {
        mTitle = title;
        mRelease = release;
        mPosterPath = posterPath;
        mRating = rating;
        mOverview = overview;
        mMovieId = movieId;
    }

    private Movie(Parcel in) {
        mTitle = in.readString();
        mRelease = in.readString();
        mPosterPath = in.readString();
        mRating = in.readDouble();
        mOverview = in.readString();
        mMovieId = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mRelease);
        parcel.writeString(mPosterPath);
        parcel.writeDouble(mRating);
        parcel.writeString(mOverview);
        parcel.writeString(mMovieId);
    }

    /**
     * @return the mTitle of the movie
     */
    public String getTitle() { return mTitle; }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * @return the mRelease date of the movie
     */
    public String getRelease() { return mRelease; }

    /**
     * @return the poster image of the movie
     */
    public String getPosterPath() { return mPosterPath; }

    /**
     * @return the mRating of the movie
     */
    public double getRating() {return mRating; }

    /**
     * @return the mOverview of the movie
     */
    public String getOverview() { return mOverview; }

    /**
     * @return the mMovieId of the movie
     */
    public String getMovieId() { return mMovieId; }


}
