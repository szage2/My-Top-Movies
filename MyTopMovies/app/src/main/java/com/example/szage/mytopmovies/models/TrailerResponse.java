package com.example.szage.mytopmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by szage on 2017. 03. 22..
 */

public class TrailerResponse {

    // List of the trailers (result of server communication)
    @SerializedName("results")
    private List<Trailer> trailerResults;

    /**
     * @return list of trailers
     */
    public List<Trailer> getTrailerResults() {
        return trailerResults;
    }
}
