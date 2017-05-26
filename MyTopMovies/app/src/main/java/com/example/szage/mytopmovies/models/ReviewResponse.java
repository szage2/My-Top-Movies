package com.example.szage.mytopmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by szage on 2017. 03. 22..
 */

public class ReviewResponse {

    // List of the reviews (result of server communication)
    @SerializedName("results")
    private List<Review> reviewResults;

    /**
     * @return list of reviews
     */
    public List<Review> getReviewResults() {
        return reviewResults;
    }
}
